// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: lionweb.proto

// Protobuf Java Version: 4.26.1
package io.lionweb.lioncore.java.serialization.protobuf;

public interface NodeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:lionweb.Node)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>uint32 parent = 2;</code>
   * @return The parent.
   */
  int getParent();

  /**
   * <code>uint32 classifier = 3;</code>
   * @return The classifier.
   */
  int getClassifier();

  /**
   * <code>repeated .lionweb.Property properties = 4;</code>
   */
  java.util.List<io.lionweb.lioncore.java.serialization.protobuf.Property> 
      getPropertiesList();
  /**
   * <code>repeated .lionweb.Property properties = 4;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.Property getProperties(int index);
  /**
   * <code>repeated .lionweb.Property properties = 4;</code>
   */
  int getPropertiesCount();
  /**
   * <code>repeated .lionweb.Property properties = 4;</code>
   */
  java.util.List<? extends io.lionweb.lioncore.java.serialization.protobuf.PropertyOrBuilder> 
      getPropertiesOrBuilderList();
  /**
   * <code>repeated .lionweb.Property properties = 4;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.PropertyOrBuilder getPropertiesOrBuilder(
      int index);

  /**
   * <code>repeated .lionweb.Containment containments = 5;</code>
   */
  java.util.List<io.lionweb.lioncore.java.serialization.protobuf.Containment> 
      getContainmentsList();
  /**
   * <code>repeated .lionweb.Containment containments = 5;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.Containment getContainments(int index);
  /**
   * <code>repeated .lionweb.Containment containments = 5;</code>
   */
  int getContainmentsCount();
  /**
   * <code>repeated .lionweb.Containment containments = 5;</code>
   */
  java.util.List<? extends io.lionweb.lioncore.java.serialization.protobuf.ContainmentOrBuilder> 
      getContainmentsOrBuilderList();
  /**
   * <code>repeated .lionweb.Containment containments = 5;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.ContainmentOrBuilder getContainmentsOrBuilder(
      int index);

  /**
   * <code>repeated .lionweb.Reference references = 6;</code>
   */
  java.util.List<io.lionweb.lioncore.java.serialization.protobuf.Reference> 
      getReferencesList();
  /**
   * <code>repeated .lionweb.Reference references = 6;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.Reference getReferences(int index);
  /**
   * <code>repeated .lionweb.Reference references = 6;</code>
   */
  int getReferencesCount();
  /**
   * <code>repeated .lionweb.Reference references = 6;</code>
   */
  java.util.List<? extends io.lionweb.lioncore.java.serialization.protobuf.ReferenceOrBuilder> 
      getReferencesOrBuilderList();
  /**
   * <code>repeated .lionweb.Reference references = 6;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.ReferenceOrBuilder getReferencesOrBuilder(
      int index);

  /**
   * <code>repeated uint32 annotations = 7;</code>
   * @return A list containing the annotations.
   */
  java.util.List<java.lang.Integer> getAnnotationsList();
  /**
   * <code>repeated uint32 annotations = 7;</code>
   * @return The count of annotations.
   */
  int getAnnotationsCount();
  /**
   * <code>repeated uint32 annotations = 7;</code>
   * @param index The index of the element to return.
   * @return The annotations at the given index.
   */
  int getAnnotations(int index);
}