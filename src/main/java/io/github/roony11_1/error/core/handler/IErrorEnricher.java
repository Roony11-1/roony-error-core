package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.dto.ErrorResponse;

public interface IErrorEnricher 
{
    ErrorResponse enrich(ErrorResponse response, Throwable ex);
}
