package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class InvalidInputException extends AppException
{
    private static final String CODE = "INVALID_INPUT";
    private static final String DEFAULT_MESSAGE = "Entrada inválida";

    public InvalidInputException(String details)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.INVALID_INPUT,
              DEFAULT_MESSAGE + ": " + details);
    }

    public InvalidInputException(String details, Throwable cause)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.INVALID_INPUT,
              DEFAULT_MESSAGE + ": " + details, cause);
    }
}