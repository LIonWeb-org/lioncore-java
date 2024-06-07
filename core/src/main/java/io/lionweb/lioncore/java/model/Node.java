package io.lionweb.lioncore.java.model;

import io.lionweb.lioncore.java.language.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A node is an instance of a Concept. It contains all the values associated to that instance.
 *
 * @see org.eclipse.emf.ecore.EObject Ecore equivalent <i>EObject</i>
 * @see <a href="https://www.jetbrains.com/help/mps/basic-notions.html">MPS equivalent <i>Node</i>
 *     in documentation</a>
 * @see org.modelix.model.api.INode Modelix equivalent <i>INode</i>
 *     <p>TODO consider if the Model should have a version too
 */
public interface Node extends ClassifierInstance<Concept> {

  /**
   * This return the Node ID.
   *
   * <p>A valid Node ID should not be null, but this method can return a null value in case the Node
   * is in invalid state.
   */
  @Nullable
  String getID();

  /**
   * If a Node is a root node in a Model, this method returns the node itself. Otherwise it returns
   * the ancestor which is a root node. This method should return null only if the Node is not
   * inserted in a Model and it is therefore considered a dangling Node.
   */
  default Node getRoot() {
    List<Node> ancestors = new LinkedList<>();
    Node curr = this;
    while (curr != null) {
      if (!ancestors.contains(curr)) {
        ancestors.add(curr);
        curr = (Node) curr.getParent();
      } else {
        throw new IllegalStateException("A circular hierarchy has been identified");
      }
    }
    return ancestors.get(ancestors.size() - 1);
  }

  default boolean isRoot() {
    return getParent() == null;
  }

  @Override
  Node getParent();

  /** The concept of which this Node is an instance. The Concept should not be abstract. */
  Concept getClassifier();

  /**
   * Return the Containment feature used to hold this Node within its parent. This will be null only
   * for root nodes or dangling nodes (which are not distinguishable by looking at the node itself).
   *
   * @see <a
   *     href="https://download.eclipse.org/modeling/emf/emf/javadoc/2.6.0/org/eclipse/emf/ecore/EObject.html#eContainingFeature()">Ecore
   *     equivalent <i>EObject.eContainingFeature</i> in documentation</a>.
   */
  Containment getContainmentFeature();

  /**
   * Return a list containing this node and all its descendants. Does <i>not</i> include
   * annotations.
   */
  default @Nonnull List<Node> thisAndAllDescendants() {
    List<Node> result = new ArrayList<>();
    ClassifierInstance.collectSelfAndDescendants(this, false, result);
    return result;
  }

  // Properties methods

  default Object getPropertyValueByName(String propertyName) {
    Property property = this.getClassifier().getPropertyByName(propertyName);
    if (property == null) {
      throw new IllegalArgumentException(
          "Concept "
              + this.getClassifier().qualifiedName()
              + " does not contained a property named "
              + propertyName);
    }
    return getPropertyValue(property);
  }

  default void setPropertyValueByName(String propertyName, Object value) {
    Classifier<?> classifier = this.getClassifier();
    if (classifier == null) {
      throw new IllegalStateException(
          "Classifier should not be null for "
              + this
              + " (class "
              + this.getClass().getCanonicalName()
              + ")");
    }
    Property property = classifier.getPropertyByName(propertyName);
    if (property == null) {
      throw new IllegalArgumentException(
          "Concept "
              + this.getClassifier().qualifiedName()
              + " does not contained a property named "
              + propertyName);
    }
    setPropertyValue(property, value);
  }

  default Object getPropertyValueByID(String propertyID) {
    Property property = this.getClassifier().getPropertyByID(propertyID);
    return getPropertyValue(property);
  }

  // Containments methods

  default List<? extends Node> getChildrenByContainmentName(String containmentName) {
    return getChildren(getClassifier().requireContainmentByName(containmentName));
  }

  default @Nullable Node getOnlyChildByContainmentName(String containmentName) {
    List<? extends Node> children = getChildrenByContainmentName(containmentName);
    if (children.size() > 1) {
      throw new IllegalStateException();
    } else if (children.isEmpty()) {
      return null;
    } else {
      return children.get(0);
    }
  }

  void setOnlyChildByContainmentName(@Nonnull String containmentName, @Nullable Node child);

  // References methods

  default List<ReferenceValue> getReferenceValueByName(String referenceName) {
    Classifier<?> classifier = this.getClassifier();
    if (classifier == null) {
      throw new IllegalStateException(
          "Concept should not be null for "
              + this
              + " (class "
              + this.getClass().getCanonicalName()
              + ")");
    }
    Reference reference = classifier.getReferenceByName(referenceName);
    if (reference == null) {
      throw new IllegalArgumentException(
          "Concept "
              + this.getClassifier().qualifiedName()
              + " does not contained a property named "
              + referenceName);
    }
    return getReferenceValues(reference);
  }

  default @Nullable ReferenceValue getOnlyReferenceValueByReferenceName(String referenceName) {
    List<ReferenceValue> referenceValues = getReferenceValueByName(referenceName);
    if (referenceValues.size() > 1) {
      throw new IllegalStateException();
    } else if (referenceValues.isEmpty()) {
      return null;
    } else {
      return referenceValues.get(0);
    }
  }
}
