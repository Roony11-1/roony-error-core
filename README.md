# roony-error-core

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.roony11-1/roony-error-core?style=flat-square)](https://search.maven.org/artifact/io.github.roony11-1/roony-error-core)

Manejo centralizado de errores para aplicaciones Java, **sin dependencias de framework**.

El core define una jerarquía de excepciones de negocio, categorías estándar, un DTO inmutable para las respuestas de error y un orquestador (`ErrorHandler`) que convierte cualquier `Throwable` en una respuesta consistente.

La idea es que el core no dependa de un framework HTTP ni de Spring o Quarkus. Cada adaptador decide cómo exponer el error, qué status usar y cómo obtener datos de infraestructura como el `path` o la correlación de trazas.

## Instalación

```xml
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

> `2.0.0` es la versión de esta línea de desarrollo. Si todavía no está publicada en Maven Central, puedes instalarla localmente con `mvn install`.

Si usas el BOM del ecosistema, comprueba que ya incluya esta versión. Si el BOM todavía apunta a la línea 1.x, declara la versión explícita de `roony-error-core`:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>1.2.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Piezas principales

| Pieza | Responsabilidad |
|---|---|
| `AppException` | Base abstracta de los errores de dominio. |
| `ErrorCategory` | Contrato para clasificar errores. |
| `StandardErrorCategories` | Catálogo de categorías estándar. |
| `ErrorResponse` | DTO inmutable de la respuesta de error. |
| `ErrorHandler` | Convierte un `Throwable` en `ErrorResponse`. |
| `ErrorResponseMapper` | Mapea excepciones al DTO canónico. |
| `ExceptionInspector` | Busca una `AppException` en la cadena de causas. |
| `IProfileResolver` | Punto de extensión para resolver el perfil activo. |
| `DefaultProfileResolver` | Implementación por defecto basada en propiedades y variables de entorno. |
| `IErrorEnricher` | Punto de extensión para enriquecer la respuesta. |
| `DevelopmentDetailsEnricher` | Enricher que agrega detalle técnico en desarrollo. |

## Excepciones de negocio

`AppException` es la base de todos los errores de dominio. Lleva un `code` de negocio, un `defaultMessage`, una `category` y un mensaje mostrable (`getDisplayMessage()`).

Excepciones incluidas:

| Excepción | Categoría | HTTP sugerido |
|---|---|---|
| `NotFoundException` | `NOT_FOUND` | 404 |
| `AlreadyExistsException` | `ALREADY_EXISTS` | 409 |
| `InvalidInputException` | `INVALID_INPUT` | 400 |
| `UnauthorizedException` | `UNAUTHORIZED` | 401 |
| `ForbiddenException` | `FORBIDDEN` | 403 |
| `InternalErrorException` | `INTERNAL_ERROR` | 500 |
| `ServiceUnavailableException` | `SERVICE_UNAVAILABLE` | 503 |

```java
throw new NotFoundException("Usuario", 123L);
throw new AlreadyExistsException("Email ya registrado");
throw new InvalidInputException("El campo 'nombre' es obligatorio");
throw new UnauthorizedException("Token inválido");
throw new ForbiddenException("No tenés permiso para ver este recurso");
throw new InternalErrorException("calcular total");
throw new ServiceUnavailableException("Pasarela de pagos no disponible");
```

La categoría `CONFLICT` existe para errores de conflicto que no encajen en `ALREADY_EXISTS`; puedes usarla en una excepción propia.

## Categorías estándar

`StandardErrorCategories` implementa `ErrorCategory`:

| Categoría | Descripción |
|---|---|
| `NOT_FOUND` | Recurso no encontrado |
| `ALREADY_EXISTS` | El recurso ya existe |
| `INVALID_INPUT` | Entrada inválida |
| `UNAUTHORIZED` | No autenticado |
| `FORBIDDEN` | Acceso denegado |
| `CONFLICT` | Conflicto con el estado actual del recurso |
| `INTERNAL_ERROR` | Error interno |
| `SERVICE_UNAVAILABLE` | Servicio temporalmente no disponible |

### Extensión: `ErrorCategory`

Puedes crear tus propias categorías sin tocar el core:

```java
public enum PagoErrorCategory implements ErrorCategory {
    SALDO_INSUFICIENTE("Saldo insuficiente"),
    TARJETA_RECHAZADA("Tarjeta rechazada");

    private final String description;

    PagoErrorCategory(String description) {
        this.description = description;
    }

    @Override
    public String description() {
        return description;
    }
}
```

La `description()` se usa como `title` de la respuesta, así que conviene que sea corta y legible.

## Crear tu propia excepción de dominio

```java
public class SaldoInsuficienteException extends AppException {
    public SaldoInsuficienteException(BigDecimal disponible, BigDecimal requerido) {
        super(
            "PAGO-001",
            "Saldo insuficiente",
            PagoErrorCategory.SALDO_INSUFICIENTE,
            "Saldo " + disponible + " < " + requerido
        );
    }
}
```

```java
throw new SaldoInsuficienteException(disponible, requerido);
```

## `ErrorResponse`

`ErrorResponse` es un `record` inmutable con estos campos:

| Campo | Descripción |
|---|---|
| `type` | URI del tipo de problema. Por defecto `about:blank`. |
| `title` | Resumen legible del error. |
| `detail` | Detalle específico de esta ocurrencia. |
| `instance` | URI del recurso o endpoint afectado. |
| `code` | Código de negocio. |
| `timestamp` | Se genera automáticamente si no se indica. |
| `errors` | Lista inmutable de detalles adicionales. |

> El record incluye un campo `traceId`, pero `ErrorHandler` no lo completa. La correlación de trazas corresponde a telemetría.

```java
ErrorResponse response = new ErrorResponse("PAGO-001", "Saldo insuficiente", "Saldo 100 < 250")
    .withInstance(URI.create("/api/pagos/42"))
    .withType(URI.create("https://api.example.com/problems/saldo-insuficiente"))
    .withError("Intentá con otro medio de pago");
```

No hay setters. Cada modificación devuelve una nueva instancia; por ejemplo, con `withType`, `withInstance` o `withError`.

### Sobre `traceId` y datos de infraestructura

El core **no genera ni propaga `traceId`**. La correlación de trazas es responsabilidad de la capa de telemetría (MDC, OpenTelemetry, Micrometer, etc.) o del adaptador HTTP.

Lo mismo aplica a `instance`/`path` y al status HTTP: son datos del adaptador, no del dominio.

## Convertir cualquier excepción en `ErrorResponse`

`ErrorHandler` recibe un enricher y un mapper:

```java
IProfileResolver profiles = new DefaultProfileResolver();
IErrorEnricher enricher = new DevelopmentDetailsEnricher(profiles);
ErrorHandler handler = new ErrorHandler(enricher, new ErrorResponseMapper());

ErrorResponse response = handler.toErrorResponse(exception);
```

Comportamiento:

- Si el `Throwable` o **cualquier causa de su cadena** es una `AppException`, usa esa excepción. Así se resuelven correctamente errores de dominio envueltos por excepciones de infraestructura.
- Si no encuentra ninguna `AppException`, devuelve un error interno genérico (`UNK-001`).
- Aplica el `IErrorEnricher` configurado.
- Hace logging automático: `warn` para `AppException`, `error` para errores inesperados.
- Si recibe `null`, devuelve la respuesta inesperada sin intentar inspeccionar la cadena.

El adaptador puede completar los datos de infraestructura después:

```java
ErrorResponse response = handler.toErrorResponse(exception)
    .withInstance(URI.create(request.getRequestURI()));
```

## Extensiones por interfaz

El core está pensado para extenderse por composición. Las tres interfaces principales son `ErrorCategory`, `IProfileResolver` e `IErrorEnricher`.

### `IProfileResolver`

Resuelve el perfil activo. El `DevelopmentDetailsEnricher` lo usa para decidir si agrega detalle técnico.

```java
public interface IProfileResolver {
    String resolve();
}
```

Implementación por defecto: `DefaultProfileResolver`, que consulta en este orden:

1. Propiedad de sistema `app.profile`.
2. Propiedad de sistema `spring.profiles.active`.
3. Variable de entorno `SPRING_PROFILES_ACTIVE`.
4. Variable de entorno `APP_PROFILE`.

Si ninguna está definida, no se considera un entorno de desarrollo.

Puedes reemplazarla por una implementación propia:

```java
public final class ConfigProfileResolver implements IProfileResolver {
    private final String profile;

    public ConfigProfileResolver(String profile) {
        this.profile = profile;
    }

    @Override
    public String resolve() {
        return profile;
    }
}
```

```java
IProfileResolver profiles = new ConfigProfileResolver("dev");
ErrorHandler handler = new ErrorHandler(
    new DevelopmentDetailsEnricher(profiles),
    new ErrorResponseMapper()
);
```

Al ser una interfaz funcional, también puedes usar una lambda:

```java
IProfileResolver profiles = () -> "dev";
```

### `IErrorEnricher`

Permite transformar la respuesta antes de devolverla. Recibe la respuesta canónica y el `Throwable` original.

```java
public interface IErrorEnricher {
    ErrorResponse enrich(ErrorResponse response, Throwable ex);
}
```

La implementación incluida, `DevelopmentDetailsEnricher`, agrega detalle técnico cuando el perfil contiene `dev` (sin distinguir mayúsculas). Si no querés ese comportamiento, simplemente no la configures.

Ejemplo de enricher propio:

```java
public final class RequestMetadataEnricher implements IErrorEnricher {
    private final String requestId;

    public RequestMetadataEnricher(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public ErrorResponse enrich(ErrorResponse response, Throwable ex) {
        return response.withError("requestId=" + requestId);
    }
}
```

También puedes combinar varios enrichers con un composite:

```java
public final class CompositeEnricher implements IErrorEnricher {
    private final List<IErrorEnricher> enrichers;

    public CompositeEnricher(IErrorEnricher... enrichers) {
        this.enrichers = List.of(enrichers);
    }

    @Override
    public ErrorResponse enrich(ErrorResponse response, Throwable ex) {
        ErrorResponse result = response;

        for (IErrorEnricher enricher : enrichers) {
            result = enricher.enrich(result, ex);
        }

        return result;
    }
}
```

```java
IErrorEnricher enricher = new CompositeEnricher(
    new DevelopmentDetailsEnricher(new DefaultProfileResolver()),
    new RequestMetadataEnricher("req-123")
);
```

El orden importa: cada enricher recibe la respuesta que devolvió el anterior.

### Composición completa

Este es el punto de extensión que usan los adaptadores: construir un `ErrorHandler` con las implementaciones que correspondan.

```java
IProfileResolver profiles = new DefaultProfileResolver();
IErrorEnricher enricher = new DevelopmentDetailsEnricher(profiles);
ErrorResponseMapper mapper = new ErrorResponseMapper();

ErrorHandler handler = new ErrorHandler(enricher, mapper);
```

Si mañana necesitás otro perfil, otro enricher o un mapper distinto, cambiás la composición sin modificar el core ni las excepciones de dominio.

## Integración con frameworks

El core no depende de Spring, Quarkus ni Jakarta. Los adaptadores HTTP viven en artefactos separados (por ejemplo `roony-error-spring` y `roony-error-quarkus`) y son los que resuelven:

- El status HTTP a partir de la categoría.
- El `instance`/`path` del request.
- La correlación de trazas y otros datos de telemetría.
- La exposición del `ErrorResponse` en el formato de la plataforma.

## Requisitos

- Java 21 o superior.
- SLF4J API como dependencia de logging.

## Licencia

MIT License · [Roony11-1](https://github.com/roony11-1)
