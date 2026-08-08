# roony-error-core

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.roony11-1/roony-error-core?style=flat-square)](https://search.maven.org/artifact/io.github.roony11-1/roony-error-core)

Manejo centralizado de errores para aplicaciones Java, **sin dependencias de framework**.

Proporciona una jerarquía de excepciones de negocio (`AppException`), categorías estándar (`StandardErrorCategories`) y un formateador (`ErrorHandler`) para que todos tus proyectos respondan errores con el mismo formato, tanto en Spring Boot como en Quarkus o Java puro.

## Instalación

```xml
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-core</artifactId>
    <version>1.0.2</version>
</dependency>
```

O, si usas el BOM del ecosistema:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>1.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>io.github.roony11-1</groupId>
        <artifactId>roony-error-core</artifactId>
        <!-- sin versión: la hereda del BOM -->
    </dependency>
</dependencies>
```

## Excepciones de negocio

`AppException` es la base abstracta de todos los errores de dominio. Lleva un `code` de negocio, un `defaultMessage`, una `category` y un mensaje mostrable (`getDisplayMessage()`).

Bloque incluido:

- `NotFoundException` → `StandardErrorCategories.NOT_FOUND` (404)
- `AlreadyExistsException` → `StandardErrorCategories.ALREADY_EXISTS` (409)
- `InvalidInputException` → `StandardErrorCategories.INVALID_INPUT` (400)
- `InternalErrorException` → `StandardErrorCategories.INTERNAL_ERROR` (500)

```java
throw new NotFoundException("Usuario", 123L);
throw new AlreadyExistsException("Email ya registrado");
throw new InvalidInputException("El campo 'nombre' es obligatorio");
```

## Categorías estándar

`StandardErrorCategories` implementa `ErrorCategory` y agrupa las categorías conocidas:

| Categoría | Descripción |
|---|---|
| `NOT_FOUND` | Recurso no encontrado |
| `ALREADY_EXISTS` | El recurso ya existe |
| `INVALID_INPUT` | Entrada inválida |
| `UNAUTHORIZED` | No autenticado |
| `FORBIDDEN` | Acceso denegado |
| `ACCESS_DENIED` | Acceso denegado |
| `INTERNAL_ERROR` | Error interno |

Crear tu propia categoría es tan simple como implementar `ErrorCategory`:

```java
public enum MiCategoria implements ErrorCategory {
    SALDO_INSUFICIENTE("Saldo insuficiente");

    private final String description;
    MiCategoria(String description) { this.description = description; }

    @Override
    public String description() { return description; }
}
```

## Crear tu propia excepción de dominio

```java
public class SaldoInsuficienteException extends AppException {
    public SaldoInsuficienteException(BigDecimal disponible, BigDecimal requerido) {
        super("PAGO-001", "Saldo insuficiente", MiCategoria.SALDO_INSUFICIENTE,
              "Saldo " + disponible + " < " + requerido);
    }
}
```

```java
throw new SaldoInsuficienteException(disponible, requerido);
```

## Convertir cualquier excepción en un `ErrorResponse`

`ErrorHandler.toErrorResponse(Throwable)`:

- Si la excepción (o **cualquier causa en su cadena**) es una `AppException`, la usa; así las excepciones de infraestructura que envuelven errores de dominio (ej. una `DataIntegrityViolationException` con causa `AppException`) se resuelven correctamente.
- En caso contrario, devuelve un error interno genérico.
- En entorno de desarrollo (perfil con `dev` detectado vía `app.profile`, `spring.profiles.active`, `SPRING_PROFILES_ACTIVE` o `APP_PROFILE`) incluye `details` con el detalle técnico.
- Hace logging automáticamente (warn para `AppException`, error para inesperadas).

```java
try {
    // lógica de negocio
} catch (Exception e) {
    ErrorResponse error = ErrorHandler.toErrorResponse(e);
}
```

## `ErrorResponse`

```java
ErrorResponse response = new ErrorResponse("CODE", "Mensaje");
response.setPath("/api/venta");
response.setTraceId("trace-1");
```

Campos: `code`, `message`, `timestamp` (auto), `details`, `path`, `traceId`.

---

MIT License · [Roony11-1](https://github.com/roony11-1)