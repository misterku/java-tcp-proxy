package com.tcpproxy.handler;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public interface Handler {
    void register(Selector selector) throws ClosedChannelException;
    void process(SelectionKey key) throws IOException;
}
