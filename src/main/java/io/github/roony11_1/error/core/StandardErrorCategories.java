package io.github.roony11_1.error.core;

public final class StandardErrorCategories 
{
    private StandardErrorCategories() {}

    public static final ErrorCategory NOT_FOUND = new ErrorCategory() 
    {
        @Override public String name() { return "NOT_FOUND"; }
        @Override public String description() { return "Recurso no encontrado"; }
    };

    public static final ErrorCategory ALREADY_EXISTS = new ErrorCategory() 
    {
        @Override public String name() { return "ALREADY_EXISTS"; }
        @Override public String description() { return "El recurso ya existe"; }
    };

    public static final ErrorCategory INVALID_INPUT = new ErrorCategory() 
    {
        @Override public String name() { return "INVALID_INPUT"; }
        @Override public String description() { return "Entrada inválida"; }
    };

    public static final ErrorCategory INTERNAL_ERROR = new ErrorCategory() 
    {
        @Override public String name() { return "INTERNAL_ERROR"; }
        @Override public String description() { return "Error interno"; }
    };

    public static final ErrorCategory ACCESS_DENIED = new ErrorCategory() 
    {
        @Override public String name() { return "ACCESS_DENIED"; }
        @Override public String description() { return "Acceso denegado"; }
    };
}
