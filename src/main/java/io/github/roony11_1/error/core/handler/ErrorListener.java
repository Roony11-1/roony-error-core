package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.dto.ErrorInfo;

@FunctionalInterface
public interface ErrorListener
{
    enum Severity { HANDLED, UNEXPECTED }

    void onError(Throwable cause, ErrorInfo info, Severity severity);

    ErrorListener NOOP = (cause, info, severity) -> {};
}