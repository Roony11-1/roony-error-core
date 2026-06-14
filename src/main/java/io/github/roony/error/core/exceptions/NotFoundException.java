package io.github.roony.error.core.exceptions;

import io.github.roony.error.core.StandardErrorCategories;

public class NotFoundException extends AppException 
{
    public NotFoundException(String resourceName, Object id) 
    {
        super("ERR-0003", resourceName + " no encontrado: " + id, StandardErrorCategories.NOT_FOUND, resourceName + " no encontrado: " + id);
    }

    public NotFoundException(String message) 
    {
        super("ERR-0003", message, StandardErrorCategories.NOT_FOUND, message);
    }
}
