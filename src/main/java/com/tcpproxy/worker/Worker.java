package com.tcpproxy.worker;

import com.tcpproxy.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;

public class Worker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

    private static final long TIMEOUT = 200L;
    private final Queue<Handler> handlers;

    public Worker(final Queue<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void run() {
        try (var selector = Selector.open()) {
            while (!Thread.currentThread().isInterrupted()) {

                final var handler = handlers.poll();
                if (handler != null) {
                    handler.register(selector);
                }

                if (selector.select(TIMEOUT) == 0) {
                    continue;
                }

                final var keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    final var key = keyIterator.next();
                    if (key.isValid()) {
                        ((Handler) key.attachment()).process(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (final IOException e) {
            LOG.error("Worker encountered an error", e);
        }
    }
}
