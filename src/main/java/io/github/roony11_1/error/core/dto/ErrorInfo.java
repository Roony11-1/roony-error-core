package io.github.roony11_1.error.core.dto;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ErrorInfo(
    String code,
    String title,
    String detail,
    Instant timestamp,
    List<String> errors,
    String traceId,
    Map<String, Object> attributes)
{
    public ErrorInfo 
    {
        Objects.requireNonNull(code, "code");
        Objects.requireNonNull(title, "title");

        if (timestamp == null) 
            timestamp = Instant.now();

        errors     = errors     == null ? List.of() : List.copyOf(errors);
        attributes = attributes == null ? Map.of()  : Map.copyOf(attributes);
    }

    public ErrorInfo(String code, String title, String detail) 
    {
        this(code, title, detail, Instant.now(), List.of(), null, Map.of());
    }

    public ErrorInfo withError(String error) 
    {
        var copy = new ArrayList<>(this.errors);
        copy.add(error);
        return new ErrorInfo(code, title, detail, timestamp, copy, traceId, attributes);
    }

    public ErrorInfo withTraceId(String traceId) 
    {
        return new ErrorInfo(code, title, detail, timestamp, errors, traceId, attributes);
    }

    public ErrorInfo withAttribute(String key, Object value) 
    {
        var copy = new HashMap<>(attributes);
        copy.put(key, value);
        return new ErrorInfo(code, title, detail, timestamp, errors, traceId, copy);
    }
}