package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class ForbiddenException extends AppException
{
    private static final String CODE = "FORBIDDEN";
    private static final String DEFAULT_MESSAGE = "Acceso denegado";

    public ForbiddenException(String message)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.FORBIDDEN, message);
    }

    public ForbiddenException(String message, Throwable cause)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.FORBIDDEN, message, cause);
    }
}