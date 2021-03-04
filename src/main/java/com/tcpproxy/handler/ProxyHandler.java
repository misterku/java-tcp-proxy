package com.tcpproxy.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ProxyHandler implements Handler {
    private static final int CAPACITY = 16384;
    private final SocketChannel clientChannel;
    private final SocketChannel serverChannel;

    private final ByteBuffer clientBuffer = ByteBuffer.allocate(CAPACITY);
    private final ByteBuffer serverBuffer = ByteBuffer.allocate(CAPACITY);

    public ProxyHandler(final SocketChannel clientChannel, final SocketChannel serverChannel) {
        this.clientChannel = clientChannel;
        this.serverChannel = serverChannel;
    }

    @Override
    public void process(final SelectionKey key) throws IOException {
        if (key.isWritable()) {
            handleWrite(key);
        }
        if (key.isReadable()) {
            handleRead(key);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        final var channel = (SocketChannel) key.channel();
        ByteBuffer buffer;
        if (channel == clientChannel) {
            buffer = clientBuffer;
        } else if (channel == serverChannel) {
            buffer = serverBuffer;
        } else {
            throw new RuntimeException("Proxy handler was incorrectly created");
        }

        final int readBytes = channel.read(buffer);
        if (readBytes == -1) {
            terminate();
        }
    }

    private void handleWrite(final SelectionKey key) throws IOException {
        final var channel = (SocketChannel) key.channel();
        final ByteBuffer buffer;
        if (channel == clientChannel) {
            buffer = serverBuffer;
        } else if (channel == serverChannel) {
            buffer = clientBuffer;
        } else {
            throw new RuntimeException("Proxy handler was incorrectly created");
        }
        buffer.flip();
        channel.write(buffer);
        buffer.compact();
    }

    private void terminate() {
       closeChannel(clientChannel);
       closeChannel(serverChannel);
    }

    private void closeChannel(final SocketChannel channel) {
        try {
            channel.finishConnect();
            channel.close();
        } catch (IOException e) {
            System.err.println("Couldn't close client channel normally");
            e.printStackTrace();
        }
    }

    public void register(final Selector selector) throws ClosedChannelException {
        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, this);
        serverChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, this);
    }
}
