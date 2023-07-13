package io.lionweb.lioncore.java.self;

import io.lionweb.lioncore.java.language.*;
import io.lionweb.lioncore.java.model.impl.M3Node;
import java.util.*;

public class LionCore {

  private LionCore() {
    // prevent instantiation of instances outside of this class
  }

  private static Language INSTANCE;

  public static Concept getAnnotation() {
    return getInstance().requireConceptByName("Annotation");
  }

  public static Concept getConcept() {
    return getInstance().requireConceptByName("Concept");
  }

  public static Concept getConceptInterface() {
    return getInstance().requireConceptByName("ConceptInterface");
  }

  public static Concept getContainment() {
    return getInstance().requireConceptByName("Containment");
  }

  public static Concept getDataType() {
    return getInstance().requireConceptByName("DataType");
  }

  public static Concept getEnumeration() {
    return getInstance().requireConceptByName("Enumeration");
  }

  public static Concept getEnumerationLiteral() {
    return getInstance().requireConceptByName("EnumerationLiteral");
  }

  public static Concept getFeature() {
    return getInstance().requireConceptByName("Feature");
  }

  public static Concept getFeaturesContainer() {
    return getInstance().requireConceptByName("FeaturesContainer");
  }

  public static Concept getLink() {
    return getInstance().requireConceptByName("Link");
  }

  public static Concept getLanguage() {
    return getInstance().requireConceptByName("Language");
  }

  public static Concept getLanguageElement() {
    return getInstance().requireConceptByName("LanguageElement");
  }

  public static Concept getNamespacedEntity() {
    return getInstance().getConceptByName("NamespacedEntity");
  }

  public static ConceptInterface getNamespaceProvider() {
    return getInstance().getConceptInterfaceByName("NamespaceProvider");
  }

  public static Concept getPrimitiveType() {
    return getInstance().requireConceptByName("PrimitiveType");
  }

  public static Concept getProperty() {
    return getInstance().requireConceptByName("Property");
  }

  public static Concept getReference() {
    return getInstance().requireConceptByName("Reference");
  }

  public static Language getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Language("LIonCore.M3");
      INSTANCE.setID("-id-LIonCore-M3");
      INSTANCE.setKey("LIonCore-M3");
      INSTANCE.setVersion("1");

      // We first instantiate all Concepts and ConceptInterfaces
      // we add features only after as the features will have references to these elements
      Concept concept = INSTANCE.addElement(new Concept("Concept"));
      Concept conceptInterface = INSTANCE.addElement(new Concept("ConceptInterface"));
      Concept containment = INSTANCE.addElement(new Concept("Containment"));
      Concept dataType = INSTANCE.addElement(new Concept("DataType"));
      Concept enumeration = INSTANCE.addElement(new Concept("Enumeration"));
      Concept enumerationLiteral = INSTANCE.addElement(new Concept("EnumerationLiteral"));
      Concept feature = INSTANCE.addElement(new Concept("Feature"));
      Concept classifier = INSTANCE.addElement(new Concept("Classifier"));
      //ConceptInterface hasKey = INSTANCE.addElement(new ConceptInterface("HasKey"));
      Concept link = INSTANCE.addElement(new Concept("Link"));
      Concept language = INSTANCE.addElement(new Concept("Language"));
      Concept languageEntity = INSTANCE.addElement(new Concept("LanguageEntity"));
//      Concept namespacedEntity = INSTANCE.addElement(new Concept("NamespacedEntity"));
//      ConceptInterface namespaceProvider =
//          INSTANCE.addElement(new ConceptInterface("NamespaceProvider"));
      Concept primitiveType = INSTANCE.addElement(new Concept("PrimitiveType"));
      Concept property = INSTANCE.addElement(new Concept("Property"));
      Concept reference = INSTANCE.addElement(new Concept("Reference"));
      ConceptInterface iKeyed = INSTANCE.addElement(new ConceptInterface("IKeyed"));

      // Now we start adding the features to all the Concepts and ConceptInterfaces

//      concept.setAbstract(false);
//      concept.setPartition(false);
      concept.setExtendedConcept(classifier);
      concept.addFeature(
          Property.createRequired(
              "abstract", LionCoreBuiltins.getBoolean(), "-id-Concept-abstract"));
      concept.addFeature(
              Property.createRequired(
                      "partition", LionCoreBuiltins.getBoolean(), "-id-Concept-partition"));
      concept.addFeature(
          Reference.createOptional("extends", concept, "-id-Concept-extends"));
      concept.addFeature(
          Reference.createMultiple(
              "implements", conceptInterface, "-id-Concept-implements"));

      conceptInterface.setExtendedConcept(classifier);
      conceptInterface.addFeature(
          Reference.createMultiple(
              "extends", conceptInterface, "-id-ConceptInterface-extends"));

      containment.setExtendedConcept(link);

      dataType.setExtendedConcept(languageEntity);
      dataType.setAbstract(true);

      enumeration.setExtendedConcept(dataType);
      //enumeration.addImplementedInterface(namespaceProvider);
      enumeration.addFeature(Containment.createMultiple("literals", enumerationLiteral).setID("-id-Enumeration-literals"));

      //enumerationLiteral.setExtendedConcept(namespacedEntity);
      enumerationLiteral.addImplementedInterface(iKeyed);

      //feature.setExtendedConcept(namespacedEntity);
      feature.setAbstract(true);
      feature.addImplementedInterface(iKeyed);
      feature.addFeature(
          Property.createRequired(
              "optional", LionCoreBuiltins.getBoolean(), "-id-Feature-optional"));

      classifier.setAbstract(true);
      classifier.setExtendedConcept(languageEntity);
      classifier.addFeature(
          Containment.createMultiple(
              "features", feature, "-id-Classifier-features"));

//      hasKey.addFeature(
//          Property.createRequired("key", LionCoreBuiltins.getString(), "LIonCore_M3_HasKey_key"));

      link.setAbstract(true);
      link.setExtendedConcept(feature);
      link.addFeature(
          Property.createRequired(
              "multiple", LionCoreBuiltins.getBoolean(), "-id-Link-multiple"));
      link.addFeature(Reference.createRequired("type", classifier, "-id-Link-type"));

      //language.addImplementedInterface(namespaceProvider);
      language.setPartition(true);
      language.addImplementedInterface(iKeyed);
//      language.addFeature(
//          Property.createRequired(
//              "name", LionCoreBuiltins.getString(), "LIonCore_M3_Language_name"));
      language.addFeature(
          Property.createRequired(
              "version", LionCoreBuiltins.getString(), "LIonCore_M3_Language_version"));
      language.addFeature(Reference.createMultiple("dependsOn", language));
      language.addFeature(
          Containment.createMultiple("entities", languageEntity, "LIonCore_M3_Language_elements").setKey("Language-elements"));

      //languageEntity.setExtendedConcept(namespacedEntity);
      languageEntity.addImplementedInterface(iKeyed);

//      namespacedEntity.setAbstract(true);
//      namespacedEntity.addImplementedInterface(LionCoreBuiltins.getINamed());
//      namespacedEntity.addFeature(
//          Property.createRequired(
//                  "qualifiedName",
//                  LionCoreBuiltins.getString(),
//                  "LIonCore_M3_NamespacedEntity_qualifiedName")
//              .setDerived(true));

      primitiveType.setExtendedConcept(dataType);

      property.setExtendedConcept(feature);
      property.addFeature(Reference.createRequired("type", dataType, "LIonCore_M3_Property_type"));

      reference.setExtendedConcept(link);

      iKeyed.addExtendedInterface(LionCoreBuiltins.getINamed());
      iKeyed.addFeature(Property.createRequired("key", LionCoreBuiltins.getString()));

      checkIDs(INSTANCE);
    }
    checkIDs(INSTANCE);
    return INSTANCE;
  }

  private static void checkIDs(M3Node node) {
    if (node.getID() == null) {
      if (node instanceof NamespacedEntity) {
        NamespacedEntity namespacedEntity = (NamespacedEntity) node;
        node.setID("-id-" + namespacedEntity.getName().replaceAll("\\.", "_"));
        if (node instanceof HasKey<?> && ((HasKey<?>) node).getKey() == null) {
          ((HasKey<?>) node).setKey(namespacedEntity.getName());
        }
      } else {
        throw new IllegalStateException(node.toString());
      }
    }
    if (node instanceof FeaturesContainer<?>) {
      FeaturesContainer<?> featuresContainer = (FeaturesContainer<?>) node;
      featuresContainer
          .getFeatures()
          .forEach(
              feature -> {
                if (feature.getKey() == null) {
                  feature.setKey(featuresContainer.getName() + "-" + feature.getName());
                }
              });
    }

    // TODO To be changed once getChildren is implemented correctly
    getChildrenHelper(node).forEach(c -> checkIDs(c));
  }

  private static List<? extends M3Node> getChildrenHelper(M3Node node) {
    if (node instanceof Language) {
      return ((Language) node).getElements();
    } else if (node instanceof FeaturesContainer) {
      return ((FeaturesContainer) node).getFeatures();
    } else if (node instanceof Feature) {
      return Collections.emptyList();
    } else {
      throw new UnsupportedOperationException("Unsupported " + node);
    }
  }
}
