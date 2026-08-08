package io.github.roony11_1.error.core;

public enum StandardErrorCategories implements ErrorCategory 
{
    NOT_FOUND("Recurso no encontrado"),
    ALREADY_EXISTS("El recurso ya existe"),
    INVALID_INPUT("Entrada inválida"),
    INTERNAL_ERROR("Error interno"),
    UNAUTHORIZED("No autenticado"),
    FORBIDDEN("Acceso denegado"),
    ACCESS_DENIED("Acceso denegado");

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