// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: lionweb.proto

// Protobuf Java Version: 4.26.1
package io.lionweb.lioncore.java.serialization.protobuf;

public interface SerializationChunkOrBuilder extends
    // @@protoc_insertion_point(interface_extends:lionweb.SerializationChunk)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string serializationFormatVersion = 1;</code>
   * @return The serializationFormatVersion.
   */
  java.lang.String getSerializationFormatVersion();
  /**
   * <code>string serializationFormatVersion = 1;</code>
   * @return The bytes for serializationFormatVersion.
   */
  com.google.protobuf.ByteString
      getSerializationFormatVersionBytes();

  /**
   * <code>map&lt;uint32, .lionweb.CompactedId&gt; idStrings = 2;</code>
   */
  int getIdStringsCount();
  /**
   * <code>map&lt;uint32, .lionweb.CompactedId&gt; idStrings = 2;</code>
   */
  boolean containsIdStrings(
      int key);
  /**
   * Use {@link #getIdStringsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Integer, io.lionweb.lioncore.java.serialization.protobuf.CompactedId>
  getIdStrings();
  /**
   * <code>map&lt;uint32, .lionweb.CompactedId&gt; idStrings = 2;</code>
   */
  java.util.Map<java.lang.Integer, io.lionweb.lioncore.java.serialization.protobuf.CompactedId>
  getIdStringsMap();
  /**
   * <code>map&lt;uint32, .lionweb.CompactedId&gt; idStrings = 2;</code>
   */
  /* nullable */
io.lionweb.lioncore.java.serialization.protobuf.CompactedId getIdStringsOrDefault(
      int key,
      /* nullable */
io.lionweb.lioncore.java.serialization.protobuf.CompactedId defaultValue);
  /**
   * <code>map&lt;uint32, .lionweb.CompactedId&gt; idStrings = 2;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.CompactedId getIdStringsOrThrow(
      int key);

  /**
   * <code>map&lt;uint32, .lionweb.MetaPointer&gt; metaPointers = 3;</code>
   */
  int getMetaPointersCount();
  /**
   * <code>map&lt;uint32, .lionweb.MetaPointer&gt; metaPointers = 3;</code>
   */
  boolean containsMetaPointers(
      int key);
  /**
   * Use {@link #getMetaPointersMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Integer, io.lionweb.lioncore.java.serialization.protobuf.MetaPointer>
  getMetaPointers();
  /**
   * <code>map&lt;uint32, .lionweb.MetaPointer&gt; metaPointers = 3;</code>
   */
  java.util.Map<java.lang.Integer, io.lionweb.lioncore.java.serialization.protobuf.MetaPointer>
  getMetaPointersMap();
  /**
   * <code>map&lt;uint32, .lionweb.MetaPointer&gt; metaPointers = 3;</code>
   */
  /* nullable */
io.lionweb.lioncore.java.serialization.protobuf.MetaPointer getMetaPointersOrDefault(
      int key,
      /* nullable */
io.lionweb.lioncore.java.serialization.protobuf.MetaPointer defaultValue);
  /**
   * <code>map&lt;uint32, .lionweb.MetaPointer&gt; metaPointers = 3;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.MetaPointer getMetaPointersOrThrow(
      int key);

  /**
   * <code>repeated .lionweb.UsedLanguage languages = 4;</code>
   */
  java.util.List<io.lionweb.lioncore.java.serialization.protobuf.UsedLanguage> 
      getLanguagesList();
  /**
   * <code>repeated .lionweb.UsedLanguage languages = 4;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.UsedLanguage getLanguages(int index);
  /**
   * <code>repeated .lionweb.UsedLanguage languages = 4;</code>
   */
  int getLanguagesCount();
  /**
   * <code>repeated .lionweb.UsedLanguage languages = 4;</code>
   */
  java.util.List<? extends io.lionweb.lioncore.java.serialization.protobuf.UsedLanguageOrBuilder> 
      getLanguagesOrBuilderList();
  /**
   * <code>repeated .lionweb.UsedLanguage languages = 4;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.UsedLanguageOrBuilder getLanguagesOrBuilder(
      int index);

  /**
   * <code>repeated .lionweb.Node nodes = 5;</code>
   */
  java.util.List<io.lionweb.lioncore.java.serialization.protobuf.Node> 
      getNodesList();
  /**
   * <code>repeated .lionweb.Node nodes = 5;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.Node getNodes(int index);
  /**
   * <code>repeated .lionweb.Node nodes = 5;</code>
   */
  int getNodesCount();
  /**
   * <code>repeated .lionweb.Node nodes = 5;</code>
   */
  java.util.List<? extends io.lionweb.lioncore.java.serialization.protobuf.NodeOrBuilder> 
      getNodesOrBuilderList();
  /**
   * <code>repeated .lionweb.Node nodes = 5;</code>
   */
  io.lionweb.lioncore.java.serialization.protobuf.NodeOrBuilder getNodesOrBuilder(
      int index);
}