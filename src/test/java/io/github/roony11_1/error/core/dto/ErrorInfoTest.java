package io.github.roony11_1.error.core.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ErrorInfoTest
{
    @Test
    void constructor_rejectsNullCode()
    {
        assertThrows(NullPointerException.class,
            () -> new ErrorInfo(null, "title válido", "detail"));
    }

    @Test
    void constructor_rejectsNullTitle()
    {
        assertThrows(NullPointerException.class,
            () -> new ErrorInfo("C-001", null, "detail"));
    }

    @Test
    void constructor_defaultsTimestampWhenNull()
    {
        Instant before = Instant.now();
        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", null, null, null, null);
        Instant after = Instant.now();

        assertNotNull(e.timestamp());
        assertFalse(e.timestamp().isBefore(before));
        assertFalse(e.timestamp().isAfter(after));
    }

    @Test
    void constructor_defaultsErrorsToEmptyWhenNull()
    {
        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", Instant.now(), null, null, null);

        assertEquals(List.of(), e.errors());
    }

    @Test
    void constructor_copiesErrorsDefensively()
    {
        List<String> original = new ArrayList<>();
        original.add("primero");

        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", Instant.now(), original, null, null);

        original.add("segundo");

        assertEquals(List.of("primero"), e.errors());
    }

    @Test
    void constructor_errorsAreImmutable()
    {
        List<String> mutable = new ArrayList<>();
        mutable.add("a");

        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", Instant.now(), mutable, null, null);

        assertThrows(UnsupportedOperationException.class,
            () -> e.errors().add("b"));
    }

    @Test
    void constructor_defaultsAttributesToEmptyWhenNull()
    {
        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", Instant.now(), null, null, null);

        assertEquals(Map.of(), e.attributes());
    }

    @Test
    void constructor_copiesAttributesDefensively()
    {
        Map<String, Object> original = new HashMap<>();
        original.put("k", "v");

        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", Instant.now(),
            null, null, original);

        original.put("otro", "valor");

        assertEquals(Map.of("k", "v"), e.attributes());
    }

    @Test
    void constructor_attributesAreImmutable()
    {
        Map<String, Object> mutable = new HashMap<>();
        mutable.put("k", "v");

        ErrorInfo e = new ErrorInfo(
            "code", "title", "detail", Instant.now(),
            null, null, mutable);

        assertThrows(UnsupportedOperationException.class,
            () -> e.attributes().put("x", "y"));
    }

    // ---------- withError ----------

    @Test
    void withError_returnsNewInstance()
    {
        ErrorInfo e = new ErrorInfo("code", "title", "detail");
        assertNotSame(e, e.withError("error"));
    }

    @Test
    void withError_doesNotMutateOriginal()
    {
        ErrorInfo e = new ErrorInfo("code", "title", "detail");
        ErrorInfo newer = e.withError("error");

        assertEquals(List.of(), e.errors());
        assertEquals(List.of("error"), newer.errors());
    }

    @Test
    void withError_appendsToExistingErrors()
    {
        ErrorInfo e = new ErrorInfo("code", "title", "detail")
            .withError("error1");

        ErrorInfo edited = e.withError("error2");

        assertEquals(List.of("error1"), e.errors());
        assertEquals(List.of("error1", "error2"), edited.errors());
    }

    // ---------- withTraceId ----------

    @Test
    void withTraceId_returnsNewInstanceWithTraceId()
    {
        ErrorInfo e = new ErrorInfo("code", "title", "detail");
        ErrorInfo newer = e.withTraceId("trace-abc");

        assertNotSame(e, newer);
        assertEquals("trace-abc", newer.traceId());
    }

    // ---------- withAttribute ----------

    @Test
    void withAttribute_returnsNewInstanceWithAttribute()
    {
        ErrorInfo e = new ErrorInfo("code", "title", "detail");
        ErrorInfo newer = e.withAttribute("userId", 42);

        assertNotSame(e, newer);
        assertEquals(Map.of("userId", 42), newer.attributes());
        assertEquals(Map.of(), e.attributes());
    }

    // ---------- conveniencia ----------

    @Test
    void convenienceConstructor_setsTimestampAndEmptyCollections()
    {
        Instant before = Instant.now();
        ErrorInfo e = new ErrorInfo("C-001", "title", "detail");
        Instant after = Instant.now();

        assertFalse(e.timestamp().isBefore(before));
        assertFalse(e.timestamp().isAfter(after));
        assertEquals(List.of(), e.errors());
        assertEquals(Map.of(), e.attributes());
    }
}