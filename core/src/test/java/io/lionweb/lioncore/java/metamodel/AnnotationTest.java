package io.lionweb.lioncore.java.metamodel;

import static org.junit.Assert.assertEquals;

import io.lionweb.lioncore.java.model.ReferenceValue;
import io.lionweb.lioncore.java.self.LionCore;
import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Test;

@Ignore // Ignoring the test as Annotation is still experimental and so is not yet reflected in
// LionCore
public class AnnotationTest {

  @Test
  public void getPropertyValuename() {
    Metamodel metamodel = new Metamodel();
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    assertEquals(
        "MyAnnotation",
        annotation.getPropertyValue(LionCore.getAnnotation().getPropertyByName("name")));
  }

  @Test
  public void setPropertyValuename() {
    Metamodel metamodel = new Metamodel();
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    annotation.setPropertyValue(
        LionCore.getAnnotation().getPropertyByName("name"), "MyAmazingAnnotation");
    assertEquals("MyAmazingAnnotation", annotation.getName());
  }

  @Test
  public void getPropertyValuePlatformSpecific() {
    Metamodel metamodel = new Metamodel();
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    assertEquals(
        null,
        annotation.getPropertyValue(
            LionCore.getAnnotation().getPropertyByName("platformSpecific")));

    annotation.setPlatformSpecific("java");
    assertEquals(
        "java",
        annotation.getPropertyValue(
            LionCore.getAnnotation().getPropertyByName("platformSpecific")));
  }

  @Test
  public void setPropertyValuePlatformSpecific() {
    Metamodel metamodel = new Metamodel();
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    annotation.setPropertyValue(
        LionCore.getAnnotation().getPropertyByName("platformSpecific"), "java");
    assertEquals("java", annotation.getPlatformSpecific());
  }

  @Test
  public void getReferenceValueTarget() {
    Metamodel metamodel = new Metamodel("mymm");
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    assertEquals(
        Arrays.asList(),
        annotation.getReferredNodes(LionCore.getAnnotation().getReferenceByName("target")));

    Concept myConcept = new Concept(metamodel, "myc");
    annotation.setTarget(myConcept);
    assertEquals(
        Arrays.asList(myConcept),
        annotation.getReferredNodes(LionCore.getAnnotation().getReferenceByName("target")));
  }

  @Test
  public void setReferenceValueTarget() {
    Metamodel metamodel = new Metamodel();
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");

    Concept myConcept = new Concept();
    annotation.addReferenceValue(
        LionCore.getAnnotation().getReferenceByName("target"), new ReferenceValue(myConcept, null));
    assertEquals(myConcept, annotation.getTarget());
  }

  @Test
  public void getPropertyValueFeatures() {
    Metamodel metamodel = new Metamodel();
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    assertEquals(
        Arrays.asList(),
        annotation.getChildren(LionCore.getAnnotation().getContainmentByName("features")));

    Property property = new Property();
    annotation.addFeature(property);
    assertEquals(
        Arrays.asList(property),
        annotation.getChildren(LionCore.getAnnotation().getContainmentByName("features")));
  }

  @Test
  public void getPropertyValueQualifiedName() {
    Metamodel metamodel = new Metamodel("my.amazing.metamodel");
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    assertEquals(
        "my.amazing.metamodel.MyAnnotation",
        annotation.getPropertyValue(LionCore.getAnnotation().getPropertyByName("qualifiedName")));
  }

  @Test
  public void getPropertyValueNamespaceQualifier() {
    Metamodel metamodel = new Metamodel("my.amazing.metamodel");
    Annotation annotation = new Annotation(metamodel, "MyAnnotation");
    assertEquals(
        "my.amazing.metamodel.MyAnnotation",
        annotation.getPropertyValue(
            LionCore.getAnnotation().getPropertyByName("namespaceQualifier")));
  }
}