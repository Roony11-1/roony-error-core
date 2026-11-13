package io.github.roony11_1.error.core.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ErrorResponseTest
{
    @Test
    void constructor_rejectsNullCode()
    {
        assertThrows(NullPointerException.class,
            () -> new ErrorResponse(null, "title válido", "detail"));
    }

    @Test
    void constructor_rejectsNullTitle()
    {
        assertThrows(NullPointerException.class,
            () -> new ErrorResponse("C-001", null, "detail"));
    }

    @Test
    void constructor_defaultsTypeToBlankWhenNull()
    {
        ErrorResponse r = new ErrorResponse(
            null, "title", "detail", null, "code",
            Instant.now(), List.of(), null);

        assertEquals(ErrorResponse.BLANK_TYPE, r.type());
    }

    @Test
    void constructor_defaultsTimestampWhenNull()
    {
        Instant before = Instant.now();
        ErrorResponse r = new ErrorResponse(
            null, "title", "detail", null, "code",
            null, null, null);
        Instant after = Instant.now();

        assertNotNull(r.timestamp());
        assertFalse(r.timestamp().isBefore(before));
        assertFalse(r.timestamp().isAfter(after));
    }

    @Test
    void constructor_defaultsErrorsToEmptyWhenNull()
    {
        ErrorResponse r = new ErrorResponse(
            null, "title", "detail", null, "code",
            Instant.now(), null, null);

        assertEquals(List.of(), r.errors());
    }

    @Test
    void constructor_copiesErrorsDefensively()
    {
        List<String> original = new ArrayList<>();
        original.add("primero");

        ErrorResponse r = new ErrorResponse(
            null, "title", "detail", null, "code",
            Instant.now(), original, null);

        original.add("segundo");

        assertEquals(List.of("primero"), r.errors());
    }

    @Test
    void constructor_errorsAreImmutable()
    {
        List<String> mutable = new ArrayList<>();
        mutable.add("a");

        ErrorResponse r = new ErrorResponse(
            null, "title", "detail", null, "code",
            Instant.now(), mutable, null);

        assertThrows(UnsupportedOperationException.class,
            () -> r.errors().add("b"));
    }

    @Test
    void withError_returnsNewInstance()
    {
        ErrorResponse r = new ErrorResponse("code", "title", "detail");
        ErrorResponse newer = r.withError("error");

        assertNotSame(r, newer);
    }

    @Test
    void withError_doesNotMutateOriginal()
    {
        ErrorResponse r = new ErrorResponse("code", "title", "detail");
        ErrorResponse newer = r.withError("error");

        assertEquals(List.of(), r.errors());
        assertEquals(List.of("error"), newer.errors());
    }

    @Test
    void withError_appendsToExistingErrors()
    {
        ErrorResponse r = new ErrorResponse("code", "title", "detail")
            .withError("error1");

        ErrorResponse edited = r.withError("error2");

        assertEquals(List.of("error1"), r.errors());
        assertEquals(List.of("error1", "error2"), edited.errors());
    }

    @Test
    void withInstance_returnsNewInstanceWithInstance()
    {
        ErrorResponse r = new ErrorResponse("code", "title", "detail");
        URI instance = URI.create("/api/users/1");

        ErrorResponse newer = r.withInstance(instance);

        assertNotSame(r, newer);
        assertEquals(instance, newer.instance());
    }

    @Test
    void withTraceId_returnsNewInstanceWithTraceId()
    {
        ErrorResponse r = new ErrorResponse("code", "title", "detail");
        ErrorResponse newer = r.withTraceId("trace-abc");

        assertNotSame(r, newer);
        assertEquals("trace-abc", newer.traceId());
    }

    @Test
    void convenienceConstructor_setsTimestampAndEmptyErrors()
    {
        Instant before = Instant.now();
        ErrorResponse r = new ErrorResponse("C-001", "title", "detail");
        Instant after = Instant.now();

        assertFalse(r.timestamp().isBefore(before));
        assertFalse(r.timestamp().isAfter(after));
        assertEquals(List.of(), r.errors());
        assertEquals(ErrorResponse.BLANK_TYPE, r.type());
    }
}