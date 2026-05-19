package com.tcpproxy.configuration;

import com.tcpproxy.TcpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Properties;

public final class ConfigurationParser {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationParser.class);

    private ConfigurationParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String LOCAL_PORT = "localPort";
    public static final String REMOTE_PORT = "remotePort";
    public static final String REMOTE_HOST = "remoteHost";

    public static List<ConfigurationEntry> parseConfiguration(final String resourceName) throws IOException {
        final var props = new Properties();
        props.load(TcpProxyServer.class.getResourceAsStream(resourceName));

        final var localPorts = new HashMap<String, Integer>();
        final var remotePorts = new HashMap<String, Integer>();
        final var remoteHosts = new HashMap<String, String>();

        final Set<String> handlerNames = new HashSet<>();

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
            if (localPorts.containsKey(handlerName) && remoteHosts.containsKey(handlerName)
                && remotePorts.containsKey(handlerName)) {
                configuration.add(new ConfigurationEntry(localPorts.get(handlerName), remoteHosts.get(handlerName),
                        remotePorts.get(handlerName)));
            } else {
                LOG.error("Invalid configuration file: not enough parameters for handler {}", handlerName);
            }
        }
        return configuration;
    }
}

