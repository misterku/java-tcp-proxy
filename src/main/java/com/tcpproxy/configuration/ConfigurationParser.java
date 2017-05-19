package com.tcpproxy.configuration;

import com.tcpproxy.TcpProxyServer;

import java.io.IOException;
import java.util.*;

public class ConfigurationParser {
    public static List<ConfigurationEntry> parseConfiguration(final String resourceName) throws IOException {
        final Properties props = new Properties();
        props.load(TcpProxyServer.class.getResourceAsStream(resourceName));

        final Map<String, Integer> localPorts = new HashMap<>();
        final Map<String, Integer> remotePorts = new HashMap<>();
        final Map<String, String> remoteHosts = new HashMap<>();

        final Set<String> handlerNames = new HashSet<>();

        for (final String key : props.stringPropertyNames()) {
            final String[] parsedKey = key.split("\\.");
            handlerNames.add(parsedKey[0]);
            switch (parsedKey[1]) {
                case "localPort":
                    localPorts.put(parsedKey[0], Integer.parseInt(props.getProperty(key)));
                    break;
                case "remotePort":
                    remotePorts.put(parsedKey[0], Integer.parseInt(props.getProperty(key)));
                    break;
                case "remoteHost":
                    remoteHosts.put(parsedKey[0], props.getProperty(key));
                    break;
                default:
            }
        }
        final List<ConfigurationEntry> configuration = new ArrayList<>();

        for (final String handlerName : handlerNames) {
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
