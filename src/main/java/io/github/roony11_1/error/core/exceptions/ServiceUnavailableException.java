package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class ServiceUnavailableException extends AppException
{
    private static final String CODE = "SERVICE_UNAVAILABLE";
    private static final String DEFAULT_MESSAGE = "Servicio no disponible";

    public ServiceUnavailableException(String message)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.SERVICE_UNAVAILABLE, message);
    }

    public ServiceUnavailableException(String message, Throwable cause)
    {
        super(CODE, DEFAULT_MESSAGE, StandardErrorCategories.SERVICE_UNAVAILABLE, message, cause);
    }
}