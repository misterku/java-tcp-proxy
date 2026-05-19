package com.tcpproxy.testutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EchoServer implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService executor;
    private final AtomicBoolean running;
    private final int port;

    public EchoServer() throws IOException {
        this.serverSocket = new ServerSocket(0);
        this.port = serverSocket.getLocalPort();
        this.executor = Executors.newCachedThreadPool();
        this.running = new AtomicBoolean(true);
    }

    public int getPort() {
        return port;
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
                e.printStackTrace();
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
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                out.flush();
            }
        } catch (IOException e) {
            if (running.get()) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running.set(false);
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EchoServer startNew() throws IOException {
        EchoServer server = new EchoServer();
        Thread thread = new Thread(server);
        thread.setDaemon(true);
        thread.start();
        return server;
    }
}