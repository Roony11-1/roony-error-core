package io.github.roony.error.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.roony.error.core.exceptions.AppException;

import java.util.List;

public final class ErrorHandler 
{
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    private ErrorHandler() {}

    public static ErrorResponse toErrorResponse(Throwable throwable) 
    {
        if (throwable instanceof AppException appEx) 
        {
            return buildFromAppException(appEx);
        } 
        else 
        {
            return buildFromUnexpected(throwable);
        }
    }

    private static ErrorResponse buildFromAppException(AppException ex) 
    {
        ErrorResponse response = new ErrorResponse(ex.getCode(), ex.getDisplayMessage());
        enrichWithDevelopmentDetails(response, ex);
        log.warn("AppException: {} - {}", ex.getCode(), ex.getDisplayMessage(), ex);
        return response;
    }

    private static ErrorResponse buildFromUnexpected(Throwable ex) 
    {
        ErrorResponse response = new ErrorResponse("ERR-0005", "Error interno inesperado");
        enrichWithDevelopmentDetails(response, ex);
        log.error("Error inesperado", ex);
        return response;
    }

    /**
     * En entorno de desarrollo añade los detalles de la excepción al ErrorResponse.
     */
    private static void enrichWithDevelopmentDetails(ErrorResponse response, Throwable ex) 
    {
        if (isDevelopment()) 
        {
            response.setDetails(List.of(ex.toString()));
        }
    }

    private static boolean isDevelopment() 
    {
        String profile = System.getProperty("app.profile", "prod");
        return "dev".equalsIgnoreCase(profile);
    }
}