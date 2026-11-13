package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class InternalErrorException extends AppException
{
    private static final String CODE = "INTERNAL_ERROR";
    private static final String DEFAULT_MESSAGE = "Error interno";

    public InternalErrorException(String operation)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.INTERNAL_ERROR,
              DEFAULT_MESSAGE + " en " + operation);
    }

    public InternalErrorException(String operation, Throwable cause)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.INTERNAL_ERROR,
              DEFAULT_MESSAGE + " en " + operation, cause);
    }
}