
package io.github.roony11_1.error.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.roony11_1.error.core.exceptions.AppException;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts application exceptions and unexpected failures into standardized
 * {@link ErrorResponse} instances.
 *
 * <p>Application exceptions preserve their error codes and display messages.
 * Unexpected exceptions are mapped to a generic internal error response.</p>
 *
 * <p>The handler searches the exception cause chain for an {@link AppException},
 * allowing application errors to be recognized even when wrapped by
 * infrastructure exceptions.</p>
 *
 * <p>Development details are included when the configured application profile
 * indicates a development environment. All handled exceptions are logged
 * according to their classification.</p>
 */
public final class ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Prevents instantiation of this utility class.
     */
    private ErrorHandler() {}

    /**
     * Converts a throwable into a standardized error response.
     *
     * <p>If the throwable is an {@link AppException}, or wraps one in its
     * cause chain, the application error code and display message are used.
     * Otherwise, a generic internal error response is returned.</p>
     *
     * <p>A {@code null} throwable is treated as an unexpected error.</p>
     *
     * @param throwable the exception or error to convert; may be {@code null}
     * @return the corresponding standardized error response
     */
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

    /**
     * Searches the cause chain for an application exception.
     *
     * <p>This allows application exceptions wrapped by infrastructure
     * exceptions, such as persistence exceptions, to retain their original
     * application error semantics.</p>
     *
     * <p>Tracks previously visited throwables to prevent an infinite loop
     * if a cyclic cause chain is encountered.</p>
     *
     * @param throwable the throwable from which to begin the search
     * @return the first {@link AppException} found, or {@code null} if none
     *         exists in the cause chain
     */
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

    /**
     * Builds an error response from an application exception.
     *
     * <p>Preserves the exception's error code and display message, adds
     * development details when applicable, and logs the error as a warning.</p>
     *
     * @param ex the application exception to process
     * @return the corresponding error response
     */
    private static ErrorResponse buildFromAppException(AppException ex)
    {
        ErrorResponse response = new ErrorResponse(
            ex.getCode(),
            ex.getDisplayMessage()
        );

        enrichWithDevelopmentDetails(response, ex);

        log.warn("AppException: {} - {}", ex.getCode(), ex.getDisplayMessage());

        return response;
    }

    /**
     * Builds a generic response for an unexpected error.
     *
     * <p>Uses error code {@code ERR-0005} and a generic message to avoid
     * exposing internal exception information to consumers.</p>
     *
     * <p>The original throwable is logged at error level. Development
     * details are added when the configured profile allows them.</p>
     *
     * @param ex the unexpected throwable; may be {@code null}
     * @return the generic internal error response
     */
    private static ErrorResponse buildFromUnexpected(Throwable ex)
    {
        ErrorResponse response = new ErrorResponse(
            "ERR-0005",
            "Error interno inesperado"
        );

        enrichWithDevelopmentDetails(response, ex);

        log.error("Error inesperado", ex);

        return response;
    }

    /**
     * Adds exception details to the response when a development profile
     * is active.
     *
     * <p>In other environments, the response details remain unchanged.</p>
     *
     * @param response the response to enrich
     * @param ex the exception whose details may be included
     */
    private static void enrichWithDevelopmentDetails(
        ErrorResponse response,
        Throwable ex
    )
    {
        if (isDevelopment())
        {
            response.setDetails(List.of(String.valueOf(ex)));
        }
    }

    /**
     * Determines whether the application is running in a development profile.
     *
     * @return {@code true} if the resolved profile contains {@code "dev"},
     *         ignoring case; {@code false} otherwise
     */
    private static boolean isDevelopment()
    {
        String profile = resolveProfile();

        return profile != null && profile.toLowerCase().contains("dev");
    }

    /**
     * Resolves the active application profile from system properties
     * and environment variables.
     *
     * <p>Checks the following sources in order:</p>
     * <ol>
     *   <li>{@code app.profile} system property</li>
     *   <li>{@code spring.profiles.active} system property</li>
     *   <li>{@code SPRING_PROFILES_ACTIVE} environment variable</li>
     *   <li>{@code APP_PROFILE} environment variable</li>
     * </ol>
     *
     * @return the first non-empty configured profile, or {@code null}
     *         if no profile is available
     */
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