package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.dto.ErrorInfo;

public interface IErrorEnricher 
{
    ErrorInfo enrich(ErrorInfo response, Throwable ex);
}
