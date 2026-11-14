package io.github.roony11_1.error.core.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.roony11_1.error.core.ErrorCategory;

public record ErrorInfo(
    String code,
    String title,
    String detail,
    Instant timestamp,
    List<String> errors,
    String traceId,
    Map<String, Object> attributes,
    ErrorCategory category)
{
    public ErrorInfo
    {
        Objects.requireNonNull(code,     "code");
        Objects.requireNonNull(title,    "title");
        Objects.requireNonNull(category, "category");

        if (timestamp == null)
            timestamp = Instant.now();

        errors     = errors     == null ? List.of() : List.copyOf(errors);
        attributes = attributes == null ? Map.of()  : Map.copyOf(attributes);
    }

    public ErrorInfo(String code, String title, String detail, ErrorCategory category)
    {
        this(code, title, detail, null, null, null, null, category);
    }

    public ErrorInfo withError(String error)
    {
        var copy = new ArrayList<>(this.errors);
        copy.add(error);
        return new ErrorInfo(
            code, title, detail, timestamp, copy, traceId, attributes, category);
    }

    public ErrorInfo withTraceId(String traceId)
    {
        return new ErrorInfo(
            code, title, detail, timestamp, errors, traceId, attributes, category);
    }

    public ErrorInfo withAttribute(String key, Object value)
    {
        var copy = new HashMap<>(attributes);
        copy.put(key, value);
        return new ErrorInfo(
            code, title, detail, timestamp, errors, traceId, copy, category);
    }

    public ErrorInfo withCategory(ErrorCategory category)
    {
        return new ErrorInfo(
            code, title, detail, timestamp, errors, traceId, attributes, category);
    }
}