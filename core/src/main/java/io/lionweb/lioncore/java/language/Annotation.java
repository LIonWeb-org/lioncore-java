package io.lionweb.lioncore.java.language;

import io.lionweb.lioncore.java.model.ReferenceValue;
import io.lionweb.lioncore.java.self.LionCore;
import io.lionweb.lioncore.java.versions.LionWebVersionToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This represents additional metadata relative to some orthogonal concern.
 *
 * <p>While an AnnotationInstance implements HasFeatureValues, it is forbidden to hold any
 * Containment links. This is checked during validation.
 *
 * <p>A DocumentationComment could be specified as an annotation.
 *
 * @see org.eclipse.emf.ecore.EAnnotation Ecore equivalent <i>EAnnotation</i>
 * @see <a href="https://www.jetbrains.com/help/mps/structure.html#attributes">MPS equivalent
 *     <i>Attribute</i> in documentation</a>
 * @see <a
 *     href="http://127.0.0.1:63320/node?ref=r%3A00000000-0000-4000-0000-011c89590288%28jetbrains.mps.lang.core.structure%29%2F3364660638048049748">MPS
 *     equivalent <i>NodeAttribute</i> in local MPS</a>
 */
public class Annotation<V extends LionWebVersionToken> extends Classifier<Annotation<V>, V> {

  public Annotation() {
    super();
  }

  public Annotation(@Nullable Language<V> language, @Nullable String name) {
    super(language, name);
  }

  public Annotation(@Nullable Language<V> language, @Nullable String name, @Nonnull String id) {
    super(language, name, id);
  }

  public Annotation(
      @Nullable Language<V> language,
      @Nullable String name,
      @Nonnull String id,
      @Nullable String key) {
    this(language, name, id);
    setKey(key);
  }

  public @Nullable Classifier<?, V> getAnnotates() {
    return this.getReferenceSingleValue("annotates");
  }

  /**
   * An Annotation extending another annotation should not redefine annotates. So the value is
   * effectively inherited from the super annotation.
   */
  public @Nullable Classifier<?, V> getEffectivelyAnnotated() {
    Classifier<?, V> annotates = getAnnotates();
    if (annotates == null && getExtendedAnnotation() != null) {
      return getExtendedAnnotation().getAnnotates();
    } else {
      return annotates;
    }
  }

  public @Nullable Annotation<V> getExtendedAnnotation() {
    return this.getReferenceSingleValue("extends");
  }

  public @Nonnull List<Interface<V>> getImplemented() {
    return this.getReferenceMultipleValue("implements");
  }

  public void addImplementedInterface(@Nonnull Interface<V> iface) {
    Objects.requireNonNull(iface, "iface should not be null");
    this.addReferenceMultipleValue("implements", new ReferenceValue(iface, iface.getName()));
  }

  // TODO should we verify the Annotation does not extend itself, even indirectly?
  public void setExtendedAnnotation(@Nullable Annotation<V> extended) {
    if (extended == null) {
      this.setReferenceSingleValue("extends", null);
    } else {
      this.setReferenceSingleValue("extends", new ReferenceValue(extended, extended.getName()));
    }
  }

  public void setAnnotates(@Nullable Classifier<?, V> target) {
    if (target == null) {
      this.setReferenceSingleValue("annotates", null);
    } else {
      this.setReferenceSingleValue("annotates", new ReferenceValue(target, target.getName()));
    }
  }

  @Nonnull
  @Override
  public List<Classifier<?, V>> directAncestors() {
    List<Classifier<?, V>> directAncestors = new ArrayList<>();
    // TODO add base ancestor common to all Concepts
    if (this.getExtendedAnnotation() != null) {
      directAncestors.add(this.getExtendedAnnotation());
    }
    directAncestors.addAll(this.getImplemented());
    return directAncestors;
  }

  @Nonnull
  @Override
  public List<Feature<?, V>> inheritedFeatures() {
    List<Feature<?, V>> result = new LinkedList<>();
    if (this.getExtendedAnnotation() != null) {
      combineFeatures(result, this.getExtendedAnnotation().allFeatures());
    }
    for (Interface<V> superInterface : this.getImplemented()) {
      combineFeatures(result, superInterface.allFeatures());
    }
    return result;
  }

  @Override
  public Concept<V> getClassifier() {
    return LionCore.getAnnotation(getLionWebVersion());
  }
}
