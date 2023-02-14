package org.lionweb.lioncore.java.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.lionweb.lioncore.java.metamodel.*;
import org.lionweb.lioncore.java.model.Node;
import org.lionweb.lioncore.java.model.impl.DynamicNode;
import org.lionweb.lioncore.java.model.impl.M3Node;
import org.lionweb.lioncore.java.self.LionCore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for unserializing models.
 *
 * The unserialization of each node _requires_ the unserializer to be able to resolve the Concept used.
 * If this requirement is not satisfied the unserialization will fail.
 * The actual class implementing Node being instantiated will depend on the configuration.
 * Specific classes for specific Concepts can be registered, and the usage of DynamicNode for all others can be enabled.
 *
 * Note that by default JsonSerialization will require specific Node subclasses to be specified.
 * For example, it will need to know that the concept with id 'foo-library' can be unserialized to instances of the
 * class Library.
 * If you want serialization to instantiate DynamicNodes for concepts for which you do not have a corresponding Node
 * subclass, then you need to enable that behavior explicitly by calling getNodeInstantiator().enableDynamicNodes().
 */
public class JsonSerialization {

    private static final String CONCEPT_LABEL = "concept";
    private static final String ID_LABEL = "id";

    /**
     * This has specific support for LionCore or LionCoreBuiltins.
     */
    public static JsonSerialization getStandardSerialization() {
        JsonSerialization jsonSerialization = new JsonSerialization();
        jsonSerialization.conceptResolver.registerMetamodel(LionCore.getInstance());
        jsonSerialization.nodeInstantiator.registerLionCoreCustomUnserializers();
        jsonSerialization.primitiveValuesSerialization.registerLionBuiltinsPrimitiveSerializersAndUnserializers();
        return jsonSerialization;
    }

    /**
     * This has no specific support for LionCore or LionCoreBuiltins.
     */
    public static JsonSerialization getBasicSerialization() {
        JsonSerialization jsonSerialization = new JsonSerialization();
        return jsonSerialization;
    }

    private ConceptResolver conceptResolver;
    private NodeInstantiator nodeInstantiator;
    private PrimitiveValuesSerialization primitiveValuesSerialization;

    private Map<String, JsonObject> nodeIdToData = new HashMap<>();
    private Map<String, Node> nodeIdToNode = new HashMap<>();

    private JsonSerialization() {
        // prevent public access
        conceptResolver = new ConceptResolver();
        nodeInstantiator = new NodeInstantiator();
        primitiveValuesSerialization = new PrimitiveValuesSerialization();
    }

    public ConceptResolver getConceptResolver() {
        return conceptResolver;
    }

    public NodeInstantiator getNodeInstantiator() {
        return nodeInstantiator;
    }

    public PrimitiveValuesSerialization getPrimitiveValuesSerialization() {
        return primitiveValuesSerialization;
    }

    public JsonElement serialize(Node node) {
        JsonArray arrayOfNodes = new JsonArray();
        serialize(node, arrayOfNodes, new HashSet<>());
        return arrayOfNodes;
    }

    public JsonElement serialize(List<Node> nodes) {
        JsonArray arrayOfNodes = new JsonArray();
        Set<String> encounteredIDs = new HashSet<>();
        nodes.forEach(node -> serialize(node, arrayOfNodes, encounteredIDs));
        return arrayOfNodes;
    }

    public JsonElement serialize(Node... nodes) {
        return serialize(Arrays.asList(nodes));
    }

    private void serialize(Node node, JsonArray arrayOfNodes, Set<String> encounteredIDs) {
        if (encounteredIDs.contains(node.getID())) {
            return;
        }
        arrayOfNodes.add(serializeThisNode(node));
        encounteredIDs.add(node.getID());
        node.getChildren().forEach(c -> serialize(c, arrayOfNodes, encounteredIDs));
    }

    private String serializePropertyValue(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    private JsonObject serializeThisNode(Node node) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CONCEPT_LABEL, node.getConcept().getID());
        jsonObject.addProperty(ID_LABEL, node.getID());

        JsonObject properties = new JsonObject();
        node.getConcept().allProperties().stream().filter(p -> !p.isDerived()).forEach(property -> {
            properties.addProperty(property.getID(), serializePropertyValue(node.getPropertyValue(property)));
        });
        jsonObject.add("properties", properties);

        JsonObject children = new JsonObject();
        node.getConcept().allContainments().forEach(containment -> {
            JsonArray serializedValue = new JsonArray();
            node.getChildren(containment).forEach(c -> serializedValue.add(c.getID()));
            children.add(containment.getID(), serializedValue);
        });
        jsonObject.add("children", children);

        JsonObject references = new JsonObject();
        node.getConcept().allReferences().forEach(reference -> {
            JsonArray serializedValue = new JsonArray();
            node.getReferredNodes(reference).forEach(c -> serializedValue.add(c.getID()));
            references.add(reference.getID(), serializedValue);
        });
        jsonObject.add("references", references);

        if (node.getParent() != null) {
            jsonObject.addProperty("parent", node.getParent().getID());
        }

        return jsonObject;
    }

    private <T extends Node> T populateProperties(T instance, JsonObject jsonObject) {
        if (!jsonObject.has("properties") && jsonObject.get("properties").isJsonObject()) {
            throw new IllegalStateException();
        }
        JsonObject properties = jsonObject.getAsJsonObject("properties");
        for (String propertyId : properties.keySet()) {
            Property property = instance.getConcept().getPropertyByID(propertyId);
            if (property == null) {
                throw new IllegalArgumentException("Property with id " + propertyId + " not found in " + instance.getConcept());
            }
            String serializedValue = properties.get(propertyId).getAsString();
            String typeID = property.getType().getID();
            if (typeID == null) {
                throw new IllegalStateException("No Node ID for type " + property.getType());
            }
            Object unserializedValue = primitiveValuesSerialization.unserialize(typeID, serializedValue);
            instance.setPropertyValue(property, unserializedValue);
        }

        return instance;
    }

    private void populateLinks(Node node, JsonObject data) {
        if (data.has("children")) {
            JsonObject children = data.get("children").getAsJsonObject();
            for (String containmentID : children.keySet()) {
                Containment containment = node.getConcept().getContainmentByID(containmentID);
                if (containment == null) {
                    throw new IllegalStateException();
                }
                JsonArray value = children.get(containmentID).getAsJsonArray();
                for (JsonElement childEl : value.asList()) {
                    String childId = childEl.getAsString();
                    Node child = nodeIdToNode.get(childId);
                    if (child == null) {
                        throw new IllegalArgumentException("Child with ID " + childId + " not found");
                    }
                    node.addChild(containment, child);
                }
            }
        }
        if (data.has("references")) {
            JsonObject references = data.get("references").getAsJsonObject();
            for (String referenceID : references.keySet()) {
                Reference reference = node.getConcept().getReferenceByID(referenceID);
                if (reference == null) {
                    throw new IllegalStateException("Reference not found: "  + referenceID + " in " + node.getConcept());
                }
                JsonArray value = references.get(referenceID).getAsJsonArray();
                for (JsonElement referredEl : value.asList()) {
                    String referredId = referredEl.getAsString();
                    Node referred = nodeIdToNode.get(referredId);
                    node.addReferredNode(reference, referred);
                    //throw new UnsupportedOperationException(containmentID);
                }
            }
        }
        if (data.has("parent")) {
            JsonElement parentValue = data.get("parent");
            String parentNodeID = parentValue instanceof JsonNull ? null : parentValue.getAsString();
            Node parent = nodeIdToNode.get(parentNodeID);
            // FIXME this does not look great...
            // should we add setParent to the Node interface?
            if (node instanceof M3Node) {
                ((M3Node<M3Node>) node).setParent(parent);
            } else if (node instanceof DynamicNode) {
                ((DynamicNode) node).setParent(parent);
            }
        }
    }

    public List<Node> unserialize(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<Node> nodes = jsonElement.getAsJsonArray().asList().stream().map(element -> {
                try {
                    Node node = unserializeNode(element);
                    if (node.getID() == null) {
                        throw new IllegalStateException();
                    }
                    this.nodeIdToData.put(node.getID(), element.getAsJsonObject());
                    this.nodeIdToNode.put(node.getID(), node);
                    return node;
                } catch (Exception e) {
                    throw new RuntimeException("Issue while unserializing " + element, e);
                }
            }).collect(Collectors.toList());
            for (Map.Entry<String, JsonObject> entry : nodeIdToData.entrySet()) {
                populateLinks(nodeIdToNode.get(entry.getKey()), entry.getValue());
            }
            nodeIdToData.clear();
            nodeIdToNode.clear();;
            return nodes;
        } else {
            throw new IllegalArgumentException("We expected a Json Array, we got instead: " + jsonElement);
        }
    }

    private Node unserializeNode(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String type = getStringProperty(jsonObject, CONCEPT_LABEL);
            Concept concept = conceptResolver.resolveConcept(type);
            String nodeID = getStringProperty(jsonObject, ID_LABEL);
            Node node = nodeInstantiator.instantiate(concept, jsonObject, nodeID);
            populateProperties(node, jsonObject);
            return node;
        } else {
            throw new IllegalArgumentException("We expected a Json Object, we got instead: " + jsonElement);
        }
    }

    private String getStringProperty(JsonObject jsonObject, String propertyName) {
        if (!jsonObject.has(propertyName)) {
            throw new IllegalArgumentException(propertyName + " property not found in " + jsonObject);
        }
        JsonElement value = jsonObject.get(propertyName);
        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
            return value.getAsJsonPrimitive().getAsString();
        } else {
            throw new IllegalArgumentException(propertyName + " property expected to be a string while it is " + value);
        }
    }

}