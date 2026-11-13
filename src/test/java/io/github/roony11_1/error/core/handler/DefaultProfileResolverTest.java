package io.github.roony11_1.error.core.handler;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultProfileResolverTest
{
    private static DefaultProfileResolver resolver(
        Map<String, String> systemProperties,
        Map<String, String> environment)
    {
        return new DefaultProfileResolver(
            systemProperties::get,
            environment::get);
    }

    @Test
    void prefersAppProfileOverSpringProfile()
    {
        var r = resolver(
            Map.of("app.profile", "dev",
                   "spring.profiles.active", "prod"),
            Map.of());

        assertEquals("dev", r.resolve());
    }

    @Test
    void fallsBackToSpringProfileWhenAppProfileIsEmpty()
    {
        var r = resolver(
            Map.of("app.profile", "",
                   "spring.profiles.active", "staging"),
            Map.of());

        assertEquals("staging", r.resolve());
    }

    @Test
    void fallsBackToSpringProfileWhenAppProfileIsMissing()
    {
        var r = resolver(
            Map.of("spring.profiles.active", "staging"),
            Map.of());

        assertEquals("staging", r.resolve());
    }

    @Test
    void prefersSpringProfilesActiveEnvOverAppProfileEnv()
    {
        var r = resolver(
            Map.of(),
            Map.of("SPRING_PROFILES_ACTIVE", "prod",
                   "APP_PROFILE", "dev"));

        assertEquals("prod", r.resolve());
    }

    @Test
    void fallsBackToAppProfileEnvWhenSpringProfilesActiveEnvIsEmpty()
    {
        var r = resolver(
            Map.of(),
            Map.of("SPRING_PROFILES_ACTIVE", "",
                   "APP_PROFILE", "dev"));

        assertEquals("dev", r.resolve());
    }

    @Test
    void ignoresEmptySystemPropertyValue()
    {
        var r = resolver(
            Map.of("app.profile", ""),
            Map.of("APP_PROFILE", "dev"));

        assertEquals("dev", r.resolve());
    }

    @Test
    void returnsNullWhenNothingIsConfigured()
    {
        var r = resolver(Map.of(), Map.of());

        assertNull(r.resolve());
    }

    @Test
    void returnsEmptyStringWhenAllSourcesAreEmpty()
    {
        var r = resolver(
            Map.of("app.profile", "",
                "spring.profiles.active", ""),
            Map.of("SPRING_PROFILES_ACTIVE", "",
                "APP_PROFILE", ""));

        assertEquals("", r.resolve());
    }
}