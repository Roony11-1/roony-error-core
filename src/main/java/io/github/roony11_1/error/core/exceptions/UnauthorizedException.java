package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class UnauthorizedException extends AppException
{
    private static final String CODE = "UNAUTHORIZED";
    private static final String DEFAULT_MESSAGE = "No autenticado";

    public UnauthorizedException(String message)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.UNAUTHORIZED, message);
    }

    public UnauthorizedException(String message, Throwable cause)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.UNAUTHORIZED, message, cause);
    }
}