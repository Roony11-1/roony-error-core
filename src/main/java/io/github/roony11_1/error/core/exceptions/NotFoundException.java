package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class NotFoundException extends AppException
{
    private static final String CODE = "NOT_FOUND";
    private static final String DEFAULT_MESSAGE = "Recurso no encontrado";

    public NotFoundException(String resourceName, Object id)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.NOT_FOUND,
              resourceName + " no encontrado: " + id);
    }

    public NotFoundException(String message)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.NOT_FOUND, message);
    }
}