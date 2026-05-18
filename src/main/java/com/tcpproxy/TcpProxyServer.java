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

public final class TcpProxyServer {

    private TcpProxyServer() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final Queue<Handler> HANDLERS =
        new ConcurrentLinkedQueue<>();
    private static final int WORKERS_NUMBER = 4;
    private static final String RESOURCE_NAME = "/proxy.properties";

    public static void main(final String[] args) {
        List<ConfigurationEntry> configuration = null;
        try {
            configuration = ConfigurationParser.parseConfiguration(
                RESOURCE_NAME
            );
        } catch (IOException e) {
            System.err.println("Couldn't load properties from the resource.");
            System.exit(1);
        }
        for (final ConfigurationEntry entry : configuration) {
            AcceptorHandler handler = null;
            try {
                handler = new AcceptorHandler(entry, HANDLERS);
            } catch (IOException e) {
                System.err.println("Couldn't create server socket channel.");
                e.printStackTrace();
                System.exit(1);
            }
            HANDLERS.add(handler);
        }

        final var workers = new Thread[WORKERS_NUMBER];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(new Worker(HANDLERS));
        }
        for (final Thread worker : workers) {
            worker.start();
        }
    }
}
