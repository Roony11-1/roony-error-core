package io.github.roony.error.core.exceptions;

import io.github.roony.error.core.StandardErrorCategories;

public class InternalErrorException extends AppException 
{
    public InternalErrorException(String operation) 
    {
        super("ERR-0005", "Error interno en " + operation, StandardErrorCategories.INTERNAL_ERROR, "Error interno en " + operation);
    }

    public InternalErrorException(String operation, Throwable cause) 
    {
        super("ERR-0005", "Error interno en " + operation, StandardErrorCategories.INTERNAL_ERROR, "Error interno en " + operation);
        initCause(cause);
    }
}