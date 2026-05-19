package com.tcpproxy.configuration;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationParserTest {

    @Test
    void parseConfigurationReturnsValidEntries() throws IOException {
        List<ConfigurationEntry> entries = ConfigurationParser.parseConfiguration("/proxy.properties");
        assertNotNull(entries);
        assertEquals(1, entries.size());
        ConfigurationEntry entry = entries.get(0);
        assertEquals(8080, entry.getLocalPort());
        assertEquals("localhost", entry.getRemoteHost());
        assertEquals(9090, entry.getRemotePort());
    }
}

