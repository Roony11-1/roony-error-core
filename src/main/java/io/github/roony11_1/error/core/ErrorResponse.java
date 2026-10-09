package io.github.roony11_1.error.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the standardized structure of an application error response.
 *
 * <p>Contains an error code, a human-readable message, a timestamp, optional
 * details, the request path, and a trace identifier.</p>
 *
 * <p>The path and trace identifier can be populated by the integration
 * layer that handles the exception.</p>
 */
public class ErrorResponse 
{
    private String code;
    private String message;
    private Instant timestamp;
    private List<String> details;
    private String path;
    private String traceId;

    /**
     * Creates an error response with the specified code and message.
     *
     * <p>The timestamp is initialized to the current instant, and the
     * details collection is initialized as an empty list.</p>
     *
     * @param code the application error code
     * @param message the human-readable error message
     */
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