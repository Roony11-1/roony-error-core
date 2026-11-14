package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.dto.ErrorInfo;
import io.github.roony11_1.error.core.exceptions.AppException;

public final class ErrorHandler
{
    private final IErrorEnricher enricher;
    private final ErrorInfoMapper mapper;
    private final ErrorListener listener;

    public ErrorHandler(IErrorEnricher enricher, ErrorInfoMapper mapper)
    {
        this(enricher, mapper, ErrorListener.NOOP);
    }

    public ErrorHandler(IErrorEnricher enricher,
                        ErrorInfoMapper mapper,
                        ErrorListener listener)
    {
        this.enricher = enricher;
        this.mapper   = mapper;
        this.listener = listener != null ? listener : ErrorListener.NOOP;
    }

    public ErrorInfo toErrorInfo(Throwable throwable)
    {
        if (throwable == null)
            return enricher.enrich(mapper.toInfo((Throwable) null), null);

        AppException appEx = ExceptionInspector.find(throwable);

        return appEx != null
            ? buildFromAppException(appEx)
            : buildFromUnexpected(throwable);
    }

    private ErrorInfo buildFromAppException(AppException ex)
    {
        ErrorInfo info = enricher.enrich(mapper.toInfo(ex), ex);
        listener.onError(ex, info, ErrorListener.Severity.HANDLED);
        return info;
    }

    private ErrorInfo buildFromUnexpected(Throwable ex)
    {
        ErrorInfo info = enricher.enrich(mapper.toInfo(ex), ex);
        listener.onError(ex, info, ErrorListener.Severity.UNEXPECTED);
        return info;
    }
}