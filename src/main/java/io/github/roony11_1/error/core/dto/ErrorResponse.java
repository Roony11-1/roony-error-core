package io.github.roony11_1.error.core.dto;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ErrorResponse(
    URI type,
    String title,
    String detail,
    URI instance,
    String code,
    Instant timestamp,
    List<String> errors,
    String traceId)
{
    public static final URI BLANK_TYPE = URI.create("about:blank");

    public ErrorResponse
    {
        if (type == null)
            type = BLANK_TYPE;

        Objects.requireNonNull(title, "title");

        Objects.requireNonNull(code, "code");

        if (timestamp == null)
            timestamp = Instant.now();

        if (errors == null)
            errors = List.of();
        else
            errors = List.copyOf(errors);
    }

    /**
     * Constructor de conveniencia con lo mínimo del estándar.
     * detail e instance pueden ser null (son opcionales en el RFC).
     */
    public ErrorResponse(String code, String title, String detail)
    {
        this(BLANK_TYPE, title, detail, null, code,
             Instant.now(), List.of(), null);
    }

    public ErrorResponse withType(URI type)
    {
        return new ErrorResponse(type, title, detail, instance,
            code, timestamp, errors, traceId);
    }

    public ErrorResponse withError(String error)
    {
        var copy = new ArrayList<>(this.errors);
        copy.add(error);
        return new ErrorResponse(type, title, detail, instance,
            code, timestamp, copy, traceId);
    }

    public ErrorResponse withInstance(URI instance)
    {
        return new ErrorResponse(type, title, detail, instance,
            code, timestamp, errors, traceId);
    }

    public ErrorResponse withTraceId(String traceId)
    {
        return new ErrorResponse(type, title, detail, instance,
            code, timestamp, errors, traceId);
    }
}