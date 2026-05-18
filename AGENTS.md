# Java TCP Proxy - Agent Instructions

## Build & Run

- **Build**: `./mvnw compile` (or `mvn compile`)
- **Package**: `./mvnw package` (creates executable jar at `target/tcpproxy-1.0.jar`)
- **Run**: `java -jar target/tcpproxy-1.0.jar`

## Configuration

- Configuration is loaded from classpath resource `/proxy.properties`.
- Format: `<name>.localPort`, `<name>.remoteHost`, `<name>.remotePort` (example in `src/main/resources/proxy.properties`).
- To change config, edit `src/main/resources/proxy.properties` and rebuild.

## Project Details

- Java 25 (source/target 25 in pom.xml).
- Single main class: `com.tcpproxy.TcpProxyServer`.
- No dependencies beyond standard library.
- No tests, lint, or formatting configured.
- Uses Maven wrapper (`mvnw`).

## Common Tasks

- **Clean build**: `./mvnw clean package`
- **Run with custom config**: replace the resource file and rebuild, or override classpath with external directory containing `proxy.properties`.