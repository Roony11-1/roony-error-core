package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class AlreadyExistsException extends AppException
{
    private static final String CODE = "ALREADY_EXISTS";
    private static final String DEFAULT_MESSAGE = "El recurso ya existe";

    public AlreadyExistsException(String resourceName, Object id)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.ALREADY_EXISTS,
              resourceName + " ya existe: " + id);
    }

    public AlreadyExistsException(String message)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.ALREADY_EXISTS, message);
    }
}