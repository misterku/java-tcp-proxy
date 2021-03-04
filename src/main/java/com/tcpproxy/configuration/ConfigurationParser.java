package com.tcpproxy.configuration;

import com.tcpproxy.TcpProxyServer;

import java.io.IOException;
import java.util.*;

public class ConfigurationParser {

    public static final String LOCAL_PORT = "localPort";
    public static final String REMOTE_PORT = "remotePort";
    public static final String REMOTE_HOST = "remoteHost";

    public static List<ConfigurationEntry> parseConfiguration(final String resourceName) throws IOException {
        final var props = new Properties();
        props.load(TcpProxyServer.class.getResourceAsStream(resourceName));

        final var localPorts = new HashMap<String, Integer>();
        final var remotePorts = new HashMap<String, Integer>();
        final var remoteHosts = new HashMap<String, String>();

        final var handlerNames = new HashSet<String>();

        for (final var key : props.stringPropertyNames()) {
            final var parsedKey = key.split("\\.");
            handlerNames.add(parsedKey[0]);
            switch (parsedKey[1]) {
                case LOCAL_PORT:
                    localPorts.put(parsedKey[0], Integer.parseInt(props.getProperty(key)));
                    break;
                case REMOTE_PORT:
                    remotePorts.put(parsedKey[0], Integer.parseInt(props.getProperty(key)));
                    break;
                case REMOTE_HOST:
                    remoteHosts.put(parsedKey[0], props.getProperty(key));
                    break;
                default:
            }
        }
        final List<ConfigurationEntry> configuration = new ArrayList<>();

        for (final var handlerName : handlerNames) {
            if (localPorts.containsKey(handlerName) && remoteHosts.containsKey(handlerName) &&
                remotePorts.containsKey(handlerName)) {
                configuration.add(new ConfigurationEntry(localPorts.get(handlerName), remoteHosts.get(handlerName),
                        remotePorts.get(handlerName)));
            } else {
                System.err.println("Invalid configuration file: not enough parameters for handler " + handlerName);
            }
        }
        return configuration;
    }
}
