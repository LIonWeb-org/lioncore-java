package org.lionweb.lioncore.java.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

class SerializedJsonComparisonUtils {

    private SerializedJsonComparisonUtils() {

    }

    static void assertEquivalentLionWebJson(JsonArray expected, JsonArray actual) {
        Map<String, JsonObject> expectedElements = new HashMap<>();
        Map<String, JsonObject> actualElements = new HashMap<>();
        Function<Map<String, JsonObject>, Consumer<JsonElement>> idCollector = collection -> e -> {
            String id = e.getAsJsonObject().get("id").getAsString();
            collection.put(id, e.getAsJsonObject());
        };
        expected.forEach(idCollector.apply(expectedElements));
        actual.forEach(idCollector.apply(actualElements));
        Set<String> unexpectedIDs = new HashSet<>(actualElements.keySet());
        unexpectedIDs.removeAll(expectedElements.keySet());
        Set<String> missingIDs = new HashSet<>(expectedElements.keySet());
        missingIDs.removeAll(actualElements.keySet());
        if (!unexpectedIDs.isEmpty()) {
            throw new AssertionError("Unexpected IDs found: " + unexpectedIDs);
        }
        if (!missingIDs.isEmpty()) {
            throw new AssertionError("Missing IDs found: " + missingIDs);
        }
        assertEquals("The number of nodes is different", expected.size(), actual.size());
        for (String id: expectedElements.keySet()) {
            JsonObject expectedElement = expectedElements.get(id);
            JsonObject actualElement = actualElements.get(id);
            assertEquivalentNodes(expectedElement, actualElement, "Node " + id);
        }
    }

    private static void assertEquivalentNodes(JsonObject expected, JsonObject actual, String context) {
        Set<String> actualMeaningfulKeys = actual.keySet();
        Set<String> expectedMeaningfulKeys = expected.keySet();
        if (actualMeaningfulKeys.contains("parent") && actual.get("parent") instanceof JsonNull) {
            actualMeaningfulKeys.remove("parent");
        }
        if (expectedMeaningfulKeys.contains("parent") && expected.get("parent") instanceof JsonNull) {
            expectedMeaningfulKeys.remove("parent");
        }

        Set<String> unexpectedKeys = new HashSet<>(actualMeaningfulKeys);
        unexpectedKeys.removeAll(expectedMeaningfulKeys);
        Set<String> missingKeys = new HashSet<>(expectedMeaningfulKeys);
        missingKeys.removeAll(actualMeaningfulKeys);
        if (!unexpectedKeys.isEmpty()) {
            throw new AssertionError("(" + context + ") Unexpected keys found: " + unexpectedKeys);
        }
        if (!missingKeys.isEmpty()) {
            throw new AssertionError("(" + context + ") Missing keys found: " + missingKeys);
        }
        for (String key: actualMeaningfulKeys) {
            if (key.equals("parent")) {
                assertEquals("(" + context + ") different parent", expected.get("parent"), actual.get("parent"));
            } else if (key.equals("concept")) {
                assertEquals("(" + context + ") different concept", expected.get("concept"), actual.get("concept"));
            } else if (key.equals("id")) {
                assertEquals("(" + context + ") different id", expected.get("id"), actual.get("id"));
            } else if (key.equals("references")) {
                assertEquivalentObjects(expected.getAsJsonObject("references"), actual.getAsJsonObject("references"), "References of " + context);
            } else if (key.equals("children")) {
                assertEquivalentObjects(expected.getAsJsonObject("children"), actual.getAsJsonObject("children"), "Children of " + context);
            } else if (key.equals("properties")) {
                assertEquivalentObjects(expected.getAsJsonObject("properties"), actual.getAsJsonObject("properties"), "Properties of " + context);
            } else {
                throw new AssertionError("(" + context + ") unexpected top-level key found: " + key);
            }
        }
    }

    private static void assertEquivalentObjects(JsonObject expected, JsonObject actual, String context) {
        Set<String> actualMeaningfulKeys = actual.keySet().stream().filter(k ->
                !actual.get(k).equals(new JsonObject())
                        && !actual.get(k).equals(new JsonArray())
                        && !actual.get(k).equals(JsonNull.INSTANCE)).collect(Collectors.toSet());
        Set<String> expectedMeaningfulKeys = expected.keySet().stream().filter(k -> !expected.get(k).equals(new JsonObject())
                && !expected.get(k).equals(new JsonArray())
                && !expected.get(k).equals(JsonNull.INSTANCE)).collect(Collectors.toSet());

        Set<String> unexpectedKeys = new HashSet<>(actualMeaningfulKeys);
        unexpectedKeys.removeAll(expectedMeaningfulKeys);
        Set<String> missingKeys = new HashSet<>(expectedMeaningfulKeys);
        missingKeys.removeAll(actualMeaningfulKeys);
        if (!unexpectedKeys.isEmpty()) {
            throw new AssertionError("(" + context + ") Unexpected keys found: " + unexpectedKeys);
        }
        if (!missingKeys.isEmpty()) {
            throw new AssertionError("(" + context + ") Missing keys found: " + missingKeys);
        }
        for (String key: actualMeaningfulKeys) {
            assertEquals("(" + context + ") Different values for key " + key, expected.get(key), actual.get(key));
        }
    }
}