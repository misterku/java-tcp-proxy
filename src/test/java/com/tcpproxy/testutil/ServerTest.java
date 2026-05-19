package com.tcpproxy.testutil;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void echoServerEchoesMessage() throws IOException, InterruptedException {
        EchoServer server = EchoServer.startNew();
        Thread.sleep(100);
        
        try (Socket client = new Socket("localhost", server.getPort());
             OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {
            
            String testMessage = "Hello, Echo Server!";
            out.write(testMessage.getBytes(StandardCharsets.UTF_8));
            out.flush();
            client.shutdownOutput();
            
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            
            assertEquals(testMessage, response);
        } finally {
            server.stop();
        }
    }

    @Test
    void staticResponseServerReturnsStaticResponse() throws IOException, InterruptedException {
        String expectedResponse = "CUSTOM_STATIC_RESPONSE";
        StaticResponseServer server = StaticResponseServer.startNew(expectedResponse);
        Thread.sleep(100);
        
        try (Socket client = new Socket("localhost", server.getPort());
             OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {
            
            out.write("Any request".getBytes(StandardCharsets.UTF_8));
            out.flush();
            client.shutdownOutput();
            
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            
            assertEquals(expectedResponse, response);
        } finally {
            server.stop();
        }
    }

    @Test
    void staticResponseServerReturnsDefaultResponse() throws IOException, InterruptedException {
        StaticResponseServer server = StaticResponseServer.startNew();
        Thread.sleep(100);
        
        try (Socket client = new Socket("localhost", server.getPort());
             OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {
            
            out.write("Test".getBytes(StandardCharsets.UTF_8));
            out.flush();
            client.shutdownOutput();
            
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            
            assertEquals("STATIC_RESPONSE", response);
        } finally {
            server.stop();
        }
    }
}

