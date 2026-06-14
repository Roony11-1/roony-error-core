package io.github.roony.error.core.exceptions;

import io.github.roony.error.core.StandardErrorCategories;

public class AlreadyExistsException extends AppException 
{
    public AlreadyExistsException(String resourceName, Object id) 
    {
        super("ERR-0004", resourceName + " ya existe: " + id, StandardErrorCategories.ALREADY_EXISTS, resourceName + " ya existe: " + id);
    }

    public AlreadyExistsException(String message) 
    {
        super("ERR-0004", message, StandardErrorCategories.ALREADY_EXISTS, message);
    }
}
