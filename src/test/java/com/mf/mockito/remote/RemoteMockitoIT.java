package com.mf.mockito.remote;

import com.example.Bar;
import com.example.Foo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;

import static com.mf.mockito.remote.BDDRemoteMockito.given;
import static com.mf.mockito.remote.BDDRemoteMockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoteMockitoIT {

    @Mock
    Foo foo;

    @Mock
    Bar bar;

    RemoteMockitoClient fooServer = new RemoteMockitoClient("localhost", 8081);

    @Before
    public void remoteControl() {
        fooServer.remoteControl(foo, bar);
    }

    @Test
    public void shouldBeAbleToStubAndVerifyOnRemoteApplication() throws Exception {
        given(bar.bar()).willReturn("mock response for bar");

        new URL("http://localhost:8080/someRemoteApplication/endpoint").getContent();

        verify(foo).foo("mock response for bar");
    }

    @Test(expected = IOException.class)
    public void foo() throws Exception {
        given(bar.bar()).willThrow(IllegalArgumentException.class);

        new URL("http://localhost:8080/someRemoteApplication/endpoint").getContent();
    }
}