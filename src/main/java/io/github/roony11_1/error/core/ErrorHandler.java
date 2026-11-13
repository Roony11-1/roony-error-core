
package io.github.roony11_1.error.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.roony11_1.error.core.exceptions.AppException;

import java.util.ArrayList;
import java.util.List;

public final class ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    private ErrorHandler() {}

    public static ErrorResponse toErrorResponse(Throwable throwable)
    {
        if (throwable == null)
        {
            return buildFromUnexpected(null);
        }

        AppException appEx = findAppException(throwable);

        if (appEx != null)
        {
            return buildFromAppException(appEx);
        }
        else
        {
            return buildFromUnexpected(throwable);
        }
    }

    private static AppException findAppException(Throwable throwable)
    {
        Throwable current = throwable;
        List<Throwable> seen = new ArrayList<>();

        while (current != null && !seen.contains(current))
        {
            if (current instanceof AppException appEx)
            {
                return appEx;
            }

            seen.add(current);
            current = current.getCause();
        }

        return null;
    }

    private static ErrorResponse buildFromAppException(AppException ex)
    {
        ErrorResponse response = new ErrorResponse(
            ex.getCode(),
            ex.getDisplayMessage()
        );

        response = enrichWithDevelopmentDetails(response, ex);

        log.warn("AppException: {} - {}", ex.getCode(), ex.getDisplayMessage());

        return response;
    }

    private static ErrorResponse buildFromUnexpected(Throwable ex)
    {
        ErrorResponse response = new ErrorResponse(
            "ERR-0005",
            "Error interno inesperado"
        );

        response = enrichWithDevelopmentDetails(response, ex);

        log.error("Error inesperado", ex);

        return response;
    }

    private static ErrorResponse enrichWithDevelopmentDetails(ErrorResponse response, Throwable ex)
    {
        if (isDevelopment())
            response.withDetail(String.valueOf(ex));

        return response;
    }

    private static boolean isDevelopment()
    {
        String profile = resolveProfile();

        return profile != null && profile.toLowerCase().contains("dev");
    }

    private static String resolveProfile()
    {
        for (String property : new String[] {
            "app.profile",
            "spring.profiles.active"
        })
        {
            String value = System.getProperty(property);

            if (value != null && !value.isEmpty())
            {
                return value;
            }
        }

        String env = System.getenv("SPRING_PROFILES_ACTIVE");

        return (env == null || env.isEmpty())
            ? System.getenv("APP_PROFILE")
            : env;
    }
}