# roony-error-core

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)

Manejo centralizado de errores para aplicaciones Java.  
Proporciona una jerarquía de excepciones de negocio (`AppException`) y un formateador estándar (`ErrorHandler`) para que todos tus proyectos devuelvan errores con el mismo formato, sin depender de frameworks.

## Instalación

```xml
<dependency>
    <groupId>io.github.roony</groupId>
    <artifactId>roony-error-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

O, si usas el BOM del ecosistema:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>1.0.0</version>
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

## Uso básico
```java
throw new NotFoundException("Usuario", 123L);
throw new AlreadyExistsException("Email ya registrado");
throw new InvalidInputException("El campo 'nombre' es obligatorio");
```

## Convertir cualquier excepción en un ErrorResponse
```java
try 
{
    // lógica de negocio
} 
catch (Exception e) 
{
    // Ya hace logging
    ErrorResponse error = ErrorHandler.toErrorResponse(e);
}
```

## Crear tu propia excepción de dominio
```java
public class SaldoInsuficienteException extends AppException 
{
    public SaldoInsuficienteException(BigDecimal disponible, BigDecimal requerido) 
    {
        super("PAGO-001", "Saldo insuficiente", StandardErrorCategories.INVALID_INPUT, "Saldo " + disponible + " < " + requerido);
    }
}
```

## Luego la lanzas como cualquier otra:
```java
throw new SaldoInsuficienteException(disponible, requerido);
```

LICENCIA MIT [Roony11-1]