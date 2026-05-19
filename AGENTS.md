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
- **No Lombok**: Project does not use Lombok annotations.
- No tests, lint, or formatting configured.
- Uses Maven wrapper (`mvnw`).

## Agent Code Conventions

When editing code, follow these conventions:

1. **No unused imports**: Remove any unused import statements.
2. **No star imports**: Use explicit imports instead of wildcard imports (e.g., `import java.util.List;` not `import java.util.*;`).
3. **No code comments**: Do not add comments to code unless explicitly requested by the user.
4. **LF line endings**: Use LF (Unix) line endings for all files.
5. **Trailing newline**: Ensure every file ends with a newline.

## Common Tasks

- **Clean build**: `./mvnw clean package`
- **Run with custom config**: replace the resource file and rebuild, or override classpath with external directory containing `proxy.properties`.
- **Run checkstyle**: `./mvnw checkstyle:check` (runs during validate phase automatically)

