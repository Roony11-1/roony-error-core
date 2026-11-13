package io.github.roony11_1.error.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.roony11_1.error.core.dto.ErrorResponse;
import io.github.roony11_1.error.core.exceptions.AppException;

import java.util.ArrayList;
import java.util.List;

public final class ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    private final IProfileResolver profileResolver;
    private final ErrorResponseMapper mapper;

    public ErrorHandler(IProfileResolver profileResolver, ErrorResponseMapper mapper)
    {
        this.profileResolver = profileResolver;
        this.mapper = mapper;
    }

    public ErrorResponse toErrorResponse(Throwable throwable)
    {
        if (throwable == null)
            return enrichWithDevelopmentDetails(mapper.toResponse((Throwable) null), null);

        AppException appEx = ExceptionInspector.find(throwable);

        if (appEx != null)
            return buildFromAppException(appEx);
        else
            return buildFromUnexpected(throwable);
    }

    private ErrorResponse buildFromAppException(AppException ex)
    {
        ErrorResponse response = mapper.toResponse(ex);
        response = enrichWithDevelopmentDetails(response, ex);
        log.warn("AppException: {} - {}", ex.getCode(), ex.getDisplayMessage());
        return response;
    }

    private ErrorResponse buildFromUnexpected(Throwable ex)
    {
        ErrorResponse response = mapper.toResponse(ex);
        response = enrichWithDevelopmentDetails(response, ex);
        log.error("Error inesperado", ex);
        return response;
    }

    private ErrorResponse enrichWithDevelopmentDetails(ErrorResponse response, Throwable ex)
    {
        if (isDevelopment() && ex != null)
            return response.withError(String.valueOf(ex));
        return response;
    }

    private boolean isDevelopment()
    {
        String profile = profileResolver.resolve();
        return profile != null && profile.toLowerCase().contains("dev");
    }
}