package io.github.roony11_1.error.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Caracterización del comportamiento actual.
 * Se espera que desaparescan/cambien/actualicen
 * en el refactor a record inmutable
 */
public class ErrorResponseTest 
{
    @Test
    void details_isMutableOnFreshResponse()
    {
        ErrorResponse r = new ErrorResponse("TEST-01", "TEST");

        r.getDetails().add("primer detalle");

        assertEquals(List.of("primer detalle"), r.getDetails());
    }

    @Test
    void details_becomesImmutableAfterSetDetailsWithListOf() 
    {
        ErrorResponse r = new ErrorResponse("TEST-02", "TEST");

        r.setDetails(List.of("a"));

        assertThrows(UnsupportedOperationException.class, () -> r.getDetails().add("b"));
    }
}
