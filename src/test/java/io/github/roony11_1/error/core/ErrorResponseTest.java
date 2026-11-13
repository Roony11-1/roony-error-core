package io.github.roony11_1.error.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 
 */
public class ErrorResponseTest 
{
    @Test
    void constructor_rejectsNullCode()
    {
        assertThrows(NullPointerException.class,
            () -> new ErrorResponse(null, "mensaje válido"));
    }

    @Test
    void constructor_rejectsNullMessage()
    {
        assertThrows(NullPointerException.class,
            () -> new ErrorResponse("C-001", null));
    }

    @Test
    void constructor_defaultsTimestampWhenNull()
    {
        // Probamos con sandwich
        Instant before = Instant.now();
        ErrorResponse r = new ErrorResponse("TEST-01", "msg", null, List.of(), null, null);
        Instant after = Instant.now();

        assertNotNull(r.timestamp());
        assertFalse(r.timestamp().isBefore(before));
        assertFalse(r.timestamp().isAfter(after));
    }

    @Test 
    void constructor_defaultsDetailsToEmptyWhenNull()
    {
        ErrorResponse r = new ErrorResponse("TEST-01", "msg");

        assertEquals(List.of(), r.details());
    }

    @Test
    void constructor_copiesDetailsDefensively()
    {
        List<String> original = new ArrayList<>();

        original.add("primero");

        ErrorResponse r = new ErrorResponse(
            "C-001", "msg", Instant.now(), original, null, null);

        original.add("segundo");

        assertEquals(List.of("primero"), r.details());
    }

    @Test
    void constructor_detailsAreImmutable()
    {
        ErrorResponse r = new ErrorResponse("C-001", "msg", Instant.now(),
            List.of("a"), null, null);

        assertThrows(UnsupportedOperationException.class,
            () -> r.details().add("b"));
    }

    @Test 
    void withDetail_returnsNewInstance()
    {
        ErrorResponse r = new ErrorResponse("code", "msg");

        ErrorResponse newer = r.withDetail("detail");

        assertNotSame(r, newer);
    }

    @Test
    void withDetail_doesNotMutateOriginal()
    {
        ErrorResponse r = new ErrorResponse("code", "msg");

        ErrorResponse newer = r.withDetail("detail");

        assertEquals(List.of(), r.details());
        assertEquals(List.of("detail"), newer.details());

        assertNotEquals(r.details(), newer.details());
    }

    @Test 
    void withDetail_appendsToExistingDetails()
    {
        ErrorResponse r = new ErrorResponse("null", "null", null, List.of("detail"), null, null);

        ErrorResponse edited = r.withDetail("detail2");

        assertEquals(List.of("detail"), r.details());

        assertEquals(List.of("detail", "detail2"), edited.details());
    }

    @Test 
    void withPath_returnsNewInstance()
    {
        ErrorResponse r = new ErrorResponse("code", "msg");

        ErrorResponse newer = r.withPath("path");

        assertNotSame(r, newer);
    }

    @Test 
    void withTraceId_returnsNewinstance()
    {
        ErrorResponse r = new ErrorResponse("code", "msg");

        ErrorResponse newer = r.withTraceId("path");

        assertNotSame(r, newer);
    }

    @Test
    void twoArgConstructor_setsTimestampAndEmptyDetails()
    {
        Instant before = Instant.now();
        ErrorResponse r = new ErrorResponse("C-001", "mensaje");
        Instant after = Instant.now();

        assertFalse(r.timestamp().isBefore(before));
        assertFalse(r.timestamp().isAfter(after));

        assertEquals(List.of(), r.details());
    }
}
