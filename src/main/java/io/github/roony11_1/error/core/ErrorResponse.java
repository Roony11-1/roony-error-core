package io.github.roony11_1.error.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse 
{
    private String code;
    private String message;
    private Instant timestamp;
    private List<String> details;
    private String path;
    private String traceId;

    public ErrorResponse(String code, String message) 
    {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now();
        this.details = new ArrayList<>();
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }

    public String getPath() { return this.path; }
    public void setPath(String path) { this.path = path; }

    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getTraceId() { return this.traceId; }
}