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
    <version>1.0.0</version>
</dependency>