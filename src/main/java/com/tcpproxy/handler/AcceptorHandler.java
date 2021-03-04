package com.tcpproxy.handler;

import com.tcpproxy.configuration.ConfigurationEntry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Queue;

public class AcceptorHandler implements Handler {

    private final ConfigurationEntry entry;
    private final ServerSocketChannel channel;
    private final Queue<Handler> handlers;

    public AcceptorHandler(final ConfigurationEntry entry, final Queue<Handler> handlers) throws IOException {
        this.entry = entry;
        this.channel = ServerSocketChannel.open();
        this.channel.bind(new InetSocketAddress(entry.getLocalPort()));
        this.channel.configureBlocking(false);
        this.handlers = handlers;
    }

    @Override
    public void register(final Selector selector) throws ClosedChannelException {
        channel.register(selector, SelectionKey.OP_ACCEPT, this);
    }

    @Override
    public void process(final SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            final var clientChannel = channel.accept();
            clientChannel.configureBlocking(false);
            final var serverChannel = SocketChannel.open(new InetSocketAddress(entry.getRemoteHost(), entry.getRemotePort()));
            serverChannel.configureBlocking(false);
            handlers.add(new ProxyHandler(clientChannel, serverChannel));
        }
    }
}

