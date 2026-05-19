package com.tcpproxy.testutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class StaticResponseServer implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StaticResponseServer.class);
    private final ServerSocket serverSocket;
    private final ExecutorService executor;
    private final AtomicBoolean running;
    private final int port;
    private final byte[] response;

    public StaticResponseServer(String response) throws IOException {
        this.serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
        this.executor = Executors.newCachedThreadPool();
        this.running = new AtomicBoolean(true);
        this.response = response.getBytes(StandardCharsets.UTF_8);
    }

    public StaticResponseServer() throws IOException {
        this("STATIC_RESPONSE");
    }

    public int getPort() {
        return port;
    }

    public byte[] getResponse() {
        return response.clone();
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (running.get()) {
                LOG.error("Error accepting connection", e);
            }
        } finally {
            stop();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
            }
            out.write(response);
            out.flush();
        } catch (IOException e) {
            if (running.get()) {
                LOG.error("Error handling client", e);
            }
        }
    }

    public void stop() {
        running.set(false);
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOG.error("Error closing server socket", e);
        }
    }

    public static StaticResponseServer startNew() throws IOException {
        return startNew("STATIC_RESPONSE");
    }

    public static StaticResponseServer startNew(String response) throws IOException {
        StaticResponseServer server = new StaticResponseServer(response);
        Thread thread = new Thread(server);
        thread.setDaemon(true);
        thread.start();
        return server;
    }
}

