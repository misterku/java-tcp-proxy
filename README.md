# Java TCP Proxy

A lightweight, multi-threaded TCP proxy server written in Java 25. Designed for simple TCP traffic forwarding with minimal configuration.

## Features

- **Multi-threaded architecture**: Uses worker threads for concurrent connection handling
- **Simple configuration**: Property-based configuration with minimal setup
- **No external dependencies**: Pure Java Standard Library implementation
- **Checkstyle compliance**: Enforces consistent code style
- **Test utilities**: Includes echo and static response servers for integration testing
- **Maven-based build**: Standard build process with Maven Wrapper

## Quick Start

### Prerequisites
- Java 25 or later
- Maven (or use the provided Maven Wrapper `./mvnw`)

### Build
```bash
./mvnw clean compile
```

### Package
```bash
./mvnw package
```

### Run
```bash
java -jar target/tcpproxy-1.0.jar
```

## Configuration

Configuration is defined in `src/main/resources/proxy.properties`. Each proxy rule consists of three properties:

```
<name>.localPort=<local_port>
<name>.remoteHost=<remote_host>
<name>.remotePort=<remote_port>
```

### Example
```
web.localPort=8080
web.remoteHost=example.com
web.remotePort=80

db.localPort=5432
db.remoteHost=database.internal
db.remotePort=5432
```

This creates two proxy rules:
- Local port 8080 → example.com:80
- Local port 5432 → database.internal:5432

## Project Structure

```
src/main/java/com/tcpproxy/
├── TcpProxyServer.java          # Main entry point
├── configuration/
│   ├── ConfigurationEntry.java  # Configuration data class
│   └── ConfigurationParser.java # Configuration file parser
├── handler/
│   ├── Handler.java             # Base handler interface
│   ├── AcceptorHandler.java     # Accepts new connections
│   └── ProxyHandler.java        # Proxies established connections
└── worker/
    └── Worker.java              # Worker thread implementation

src/test/java/com/tcpproxy/
├── configuration/
│   └── ConfigurationParserTest.java
└── testutil/
    ├── EchoServer.java          # Test echo server
    ├── StaticResponseServer.java # Test static response server
    └── ServerTest.java          # Test utilities tests
```

## Testing

### Run All Tests
```bash
./mvnw test
```

### Test Utilities

The project includes reusable test servers:

- **EchoServer**: Returns whatever data is sent to it
- **StaticResponseServer**: Always returns a predefined static response

These can be used for integration testing of the proxy functionality.

### Code Quality
```bash
./mvnw checkstyle:check
```

## Development

### Build Commands
```bash
# Clean build
./mvnw clean package

# Run with tests
./mvnw clean verify

# Check code style
./mvnw checkstyle:check
```

### Code Conventions
- No Lombok annotations
- No unused imports
- No wildcard imports
- Checkstyle enforced during build
- Java 25 language features

## License

This project is licensed under the BSD 3-Clause License - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Ensure code passes checkstyle: `./mvnw checkstyle:check`
2. Write tests for new functionality
3. Keep dependencies minimal
4. Follow existing code conventions

