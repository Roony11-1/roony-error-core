package io.github.roony11_1.error.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.roony11_1.error.core.dto.ErrorResponse;
import io.github.roony11_1.error.core.exceptions.AppException;

public final class ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    private final IErrorEnricher enricher;
    private final ErrorResponseMapper mapper;

    public ErrorHandler(IErrorEnricher enricher, ErrorResponseMapper mapper)
    {
        this.enricher = enricher;
        this.mapper = mapper;
    }

    public ErrorResponse toErrorResponse(Throwable throwable)
    {
        if (throwable == null)
            return enricher.enrich(mapper.toResponse((Throwable) null), null);

        AppException appEx = ExceptionInspector.find(throwable);

        if (appEx != null)
            return buildFromAppException(appEx);
        else
            return buildFromUnexpected(throwable);
    }

    private ErrorResponse buildFromAppException(AppException ex)
    {
        ErrorResponse response = mapper.toResponse(ex);
        response = enricher.enrich(response, ex);
        log.warn("AppException: {} - {}", ex.getCode(), ex.getDisplayMessage());
        return response;
    }

    private ErrorResponse buildFromUnexpected(Throwable ex)
    {
        ErrorResponse response = mapper.toResponse(ex);
        response = enricher.enrich(response, ex);
        log.error("Error inesperado", ex);
        return response;
    }
}