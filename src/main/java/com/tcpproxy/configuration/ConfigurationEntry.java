package com.tcpproxy.configuration;

public class ConfigurationEntry {

    private final int localPort;
    private final int remotePort;
    private final String remoteHost;

    public ConfigurationEntry(Integer localPort, String remoteHost, Integer remotePort) {
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

}
