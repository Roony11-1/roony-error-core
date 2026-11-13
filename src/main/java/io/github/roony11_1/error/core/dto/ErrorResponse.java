package io.github.roony11_1.error.core.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ErrorResponse(
    String code,
    String message,
    Instant timestamp,
    List<String> details,
    String path,
    String traceId)
{
    public ErrorResponse
    {
        Objects.requireNonNull(code, "code");
        Objects.requireNonNull(message, "message");

        if (timestamp == null)
            timestamp = Instant.now();

        if (details == null)
            details = List.of();
        else
            details = List.copyOf(details);
    }

    public ErrorResponse(String code, String message)
    {
        this(code, message, Instant.now(), List.of(), null, null);
    }

    public ErrorResponse withDetail(String detail)
    {
        var copy = new ArrayList<>(this.details);
        copy.add(detail);
        return new ErrorResponse(code, message, timestamp, copy, path, traceId);
    }

    public ErrorResponse withPath(String path)
    {
        return new ErrorResponse(code, message, timestamp, details, path, traceId);
    }

    public ErrorResponse withTraceId(String traceId)
    {
        return new ErrorResponse(code, message, timestamp, details, path, traceId);
    }
}