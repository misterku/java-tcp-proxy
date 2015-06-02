package com.tcpproxy.worker;

import com.tcpproxy.handler.Handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Queue;

public class Worker implements Runnable {

    private static final long TIMEOUT = 200L;
    private final Queue<Handler> handlers;


    public Worker(Queue<Handler> handlers) {
        this.handlers = handlers;

    }

    @Override
    public void run() {
        try (Selector selector = Selector.open()) {
            while (true) {
                Handler handler = handlers.poll();
                if (handler != null) {
                    handler.register(selector);
                }

                if (selector.select(TIMEOUT) == 0) {
                    continue;
                }

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isValid()) {
                        ((Handler) key.attachment()).process(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
}
