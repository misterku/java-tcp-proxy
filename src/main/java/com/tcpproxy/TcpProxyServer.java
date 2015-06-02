package com.tcpproxy;

import com.tcpproxy.configuration.ConfigurationEntry;
import com.tcpproxy.configuration.ConfigurationParser;
import com.tcpproxy.handler.AcceptorHandler;
import com.tcpproxy.handler.Handler;
import com.tcpproxy.worker.Worker;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TcpProxyServer {

    private static final Queue<Handler> handlers = new ConcurrentLinkedQueue<>();
    private static final int WORKERS_NUMBER = 4;
    public static final String RESOURCE_NAME = "/proxy.properties";

    public static void main(String[] args) {
        List<ConfigurationEntry> configuration = null;
        try {
            configuration = ConfigurationParser.parseConfiguration(RESOURCE_NAME);
        } catch (IOException e) {
            System.err.println("Couldn't load properties from the resource.");
            System.exit(1);
        }
        for (ConfigurationEntry entry : configuration) {
            AcceptorHandler handler = null;
            try {
                handler = new AcceptorHandler(entry, handlers);
            } catch (IOException e) {
                System.err.println("Couldn't create server socket channel.");
                e.printStackTrace();
                System.exit(1);
            }
            handlers.add(handler);
        }

        Thread[] workers = new Thread[WORKERS_NUMBER];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(new Worker(handlers));
        }
        for (Thread worker : workers) {
            worker.start();
        }
    }
}
