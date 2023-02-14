package org.lionweb.lioncore.java.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Ignore;
import org.junit.Test;
import org.lionweb.lioncore.java.metamodel.Concept;
import org.lionweb.lioncore.java.metamodel.Enumeration;
import org.lionweb.lioncore.java.metamodel.Metamodel;
import org.lionweb.lioncore.java.metamodel.Property;
import org.lionweb.lioncore.java.model.Node;
import org.lionweb.lioncore.java.model.impl.DynamicNode;
import org.lionweb.lioncore.java.self.LionCore;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.lionweb.lioncore.java.serialization.SerializedJsonComparisonUtils.assertEquivalentLionWebJson;

public class JsonSerializationTest {

    @Test
    public void unserializeLionCoreToConcreteClasses() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/lioncore.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        List<Node> unserializedNodes = jsonSerialization.unserialize(jsonElement);

        Metamodel lioncore = (Metamodel) unserializedNodes.get(0);
        assertEquals(LionCore.getMetamodel(), lioncore.getConcept());
        assertEquals("LIonCore_M3", lioncore.getID());
        assertEquals("LIonCore.M3", lioncore.getQualifiedName());
        assertEquals(16, lioncore.getChildren().size());
        assertEquals(null, lioncore.getParent());

        Concept namespacedEntity = (Concept) unserializedNodes.get(1);
        assertEquals(LionCore.getConcept(), namespacedEntity.getConcept());
        assertEquals("LIonCore_M3_NamespacedEntity", namespacedEntity.getID());
        assertEquals(true, namespacedEntity.isAbstract());
        assertEquals("NamespacedEntity", namespacedEntity.getSimpleName());
        assertEquals(2, namespacedEntity.getChildren().size());
        assertEquals(lioncore, namespacedEntity.getParent());

        Property simpleName = (Property) unserializedNodes.get(2);
        assertEquals(LionCore.getProperty(), simpleName.getConcept());
        assertEquals("simpleName", simpleName.getSimpleName());
        assertEquals("LIonCore_M3_NamespacedEntity", simpleName.getParent().getID());
        assertEquals("LIonCore_M3_String", simpleName.getType().getID());
    }

    @Test
    public void unserializeLionCoreToDynamicNodes() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/lioncore.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getBasicSerialization();
        jsonSerialization.getConceptResolver().registerMetamodel(LionCore.getInstance());
        jsonSerialization.getNodeInstantiator().enableDynamicNodes();
        jsonSerialization.getPrimitiveValuesSerialization().registerLionBuiltinsPrimitiveSerializersAndUnserializers();
        List<Node> unserializedNodes = jsonSerialization.unserialize(jsonElement);

        DynamicNode lioncore = (DynamicNode) unserializedNodes.get(0);
        assertEquals(LionCore.getMetamodel(), lioncore.getConcept());
        assertEquals("LIonCore_M3", lioncore.getID());
        assertEquals("LIonCore.M3", lioncore.getPropertyValueByName("qualifiedName"));
        assertEquals(16, lioncore.getChildren().size());
        assertEquals(null, lioncore.getParent());

        DynamicNode namespacedEntity = (DynamicNode) unserializedNodes.get(1);
        assertEquals(LionCore.getConcept(), namespacedEntity.getConcept());
        assertEquals("LIonCore_M3_NamespacedEntity", namespacedEntity.getID());
        assertEquals(true, namespacedEntity.getPropertyValueByName("abstract"));
        assertEquals("NamespacedEntity", namespacedEntity.getPropertyValueByName("simpleName"));
        assertEquals(2, namespacedEntity.getChildren().size());
        assertEquals(lioncore, namespacedEntity.getParent());

        DynamicNode simpleName = (DynamicNode) unserializedNodes.get(2);
        assertEquals(LionCore.getProperty(), simpleName.getConcept());
        assertEquals("simpleName", simpleName.getPropertyValueByName("simpleName"));
        assertEquals("LIonCore_M3_NamespacedEntity", simpleName.getParent().getID());
    }

    @Test(expected = RuntimeException.class)
    public void unserializeLionCoreFailsWithoutRegisteringTheClassesOrEnablingDynamicNodes() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/lioncore.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getBasicSerialization();
        jsonSerialization.getConceptResolver().registerMetamodel(LionCore.getInstance());
        jsonSerialization.getPrimitiveValuesSerialization().registerLionBuiltinsPrimitiveSerializersAndUnserializers();
        jsonSerialization.unserialize(jsonElement);
    }

    @Ignore // Eventually we should have the same serialization. Right now there are differences in the LionCore M3 that we need to solve
    @Test
    public void serializeLionCore() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/lioncore.json");
        JsonElement serializedElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        JsonElement reserialized = jsonSerialization.serialize(LionCore.getMetamodel());
        assertEquals(serializedElement, reserialized);
    }

    @Test
    public void unserializeLibrary() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/library-metamodel.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        List<Node> unserializedNodes = jsonSerialization.unserialize(jsonElement);
        Node book = unserializedNodes.stream().filter(n -> n.getID().equals("OcDK2GESljInG-ApIqtkXUoA2UeviB97u0UuiZzM0Hs")).findFirst().get();
        assertEquals("Book", book.getPropertyValueByName("simpleName"));
    }

    @Test
    public void reserializeLibrary() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/library-metamodel.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        List<Node> unserializedNodes = jsonSerialization.unserialize(jsonElement);
        JsonElement reserialized = jsonSerialization.serialize(unserializedNodes.get(0));
        assertEquivalentLionWebJson(jsonElement.getAsJsonArray(), reserialized.getAsJsonArray());
    }

    @Test
    public void serializeLibraryInstance() {
        Library library = new Library("lib-1", "Language Engineering Library");
        Writer mv = new Writer("mv", "Markus Völter");
        Writer mb = new Writer("mb", "Meinte Boersma");
        Book de = new Book("de", "DSL Engineering", mv).setPages(558);
        Book bfd = new Book("bfd", "Business-Friendly DSLs", mb).setPages(517);
        library.addBook(de);
        library.addBook(bfd);
        JsonArray jsonSerialized = JsonSerialization.getStandardSerialization().serialize(library).getAsJsonArray();
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/langeng-library.json");
        JsonArray jsonRead = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray();
        assertEquivalentLionWebJson(jsonRead, jsonSerialized);
    }

    @Test
    public void serializeMultipleSubtrees() {
        Library bobsLibrary = new Library("bl", "Bob's Library");
        GuideBookWriter jackLondon = new GuideBookWriter("jl", "Jack London");
        jackLondon.setCountries("Alaska");
        Book explorerBook = new Book("eb", "Explorer Book", jackLondon);
        bobsLibrary.addBook(explorerBook);
        assertEquals(Arrays.asList(explorerBook), bobsLibrary.getChildren());
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        JsonArray jsonSerialized = jsonSerialization.serialize(bobsLibrary, jackLondon).getAsJsonArray();
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/bobslibrary.json");
        JsonArray jsonRead = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray();
        assertEquivalentLionWebJson(jsonRead, jsonSerialized);
    }

    @Test
    public void serializeMultipleSubtreesSkipDuplicateNodes() {
        Library bobsLibrary = new Library("bl", "Bob's Library");
        GuideBookWriter jackLondon = new GuideBookWriter("jl", "Jack London");
        jackLondon.setCountries("Alaska");
        Book explorerBook = new Book("eb", "Explorer Book", jackLondon);
        bobsLibrary.addBook(explorerBook);
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        JsonArray jsonSerialized = jsonSerialization.serialize(bobsLibrary, jackLondon, explorerBook).getAsJsonArray();
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/bobslibrary.json");
        JsonArray jsonRead = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonArray();
        assertEquivalentLionWebJson(jsonRead, jsonSerialized);
    }

    @Test
    public void unserializeMetamodelWithEnumerations() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/TestLang-metamodel.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        List<Node> unserializedNodes = jsonSerialization.unserialize(jsonElement);

        Enumeration testEnumeration1 = (Enumeration) unserializedNodes.stream().filter(n -> n.getID().equals("MDhjYWFkNzUtODI0Ni00NDI3LWJiNGQtODQ0NGI2YzVjNzI5LzI1ODUzNzgxNjU5NzMyMDQ1ODI")).findFirst().get();
        assertEquals("TestEnumeration1", testEnumeration1.getSimpleName());
        assertEquals(2, testEnumeration1.getLiterals().size());

        Concept sideTransformInfo = (Concept) unserializedNodes.stream().filter(n -> n.getID().equals("Y2VhYjUxOTUtMjVlYS00ZjIyLTliOTItMTAzYjk1Y2E4YzBjLzc3OTEyODQ5Mjg1MzM2OTE2NQ")).findFirst().get();
        assertEquals("SideTransformInfo", sideTransformInfo.getSimpleName());
        assertEquals(false, sideTransformInfo.isAbstract());
        assertEquals(3, sideTransformInfo.getFeatures().size());
        assertEquals(3, sideTransformInfo.getChildren().size());
    }

    @Test
    public void reserializeMetamodelWithEnumerations() {
        InputStream inputStream = this.getClass().getResourceAsStream("/serialization/TestLang-metamodel.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonSerialization jsonSerialization = JsonSerialization.getStandardSerialization();
        List<Node> unserializedNodes = jsonSerialization.unserialize(jsonElement);
        JsonArray reserialized = jsonSerialization.serialize(unserializedNodes).getAsJsonArray();

        List<JsonObject> metamodels = getNodesByConcept(reserialized, "LIonCore_M3_Metamodel");
        assertEquals(2, metamodels.size());

        List<JsonObject> concepts = getNodesByConcept(reserialized, "LIonCore_M3_Concept");
        assertEquals(19, concepts.size());

        assertEquivalentLionWebJson(jsonElement.getAsJsonArray(), reserialized);
    }

    private List<JsonObject> getNodesByConcept(JsonArray nodes, String conceptID) {
        return nodes.asList().stream().map(JsonElement::getAsJsonObject).filter(e -> e.get("concept").getAsString().equals(conceptID)).collect(Collectors.toList());
    }

}