package io.github.roony11_1.error.core;

public enum StandardErrorCategories implements ErrorCategory
{
    NOT_FOUND("Recurso no encontrado"),
    ALREADY_EXISTS("El recurso ya existe"),
    INVALID_INPUT("Entrada inválida"),
    INTERNAL_ERROR("Error interno"),
    UNAUTHORIZED("No autenticado"),
    FORBIDDEN("Acceso denegado"),
    CONFLICT("Conflicto con el estado actual del recurso"),
    SERVICE_UNAVAILABLE("Servicio temporalmente no disponible");

    private final String description;

    StandardErrorCategories(String description)
    {
        this.description = description;
    }

    @Override
    public String description()
    {
        return description;
    }
}