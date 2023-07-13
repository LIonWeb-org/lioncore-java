package io.lionweb.lioncore.java.language;

import io.lionweb.lioncore.java.model.impl.M3Node;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A LanguageElement is an element with an identity within a {@link Language}.
 *
 * <p>For example, Invoice, Currency, Named, or String could be LanguageElements.
 *
 * @see org.eclipse.emf.ecore.EClassifier Ecore equivalent <i>EClassifier</i>
 * @see <a
 *     href="http://127.0.0.1:63320/node?ref=r%3A00000000-0000-4000-0000-011c89590292%28jetbrains.mps.lang.structure.structure%29%2F1588368162880706270">MPS
 *     equivalent <i>IStructureElement</i> in local MPS</a>
 * @see org.jetbrains.mps.openapi.language.SElement MPS equivalent <i>SElement</i> in SModel
 */
public abstract class LanguageElement<T extends M3Node> extends M3Node<T>
    implements NamespacedEntity, HasKey<T> {

  public LanguageElement() {}

  public LanguageElement(@Nullable Language language, @Nullable String name, @Nonnull String id) {
    this(language, name);
    this.setID(id);
  }

  public LanguageElement(@Nullable Language language, @Nullable String name) {
    // TODO enforce uniqueness of the name within the Language
    this.setName(name);
    if (language != null) {
      language.addElement(this);
    } else {
      this.setParent(null);
    }
  }

  /**
   * This method returns the Language containing this element. It is the parent, casted to Language.
   *
   * @return
   */
  public @Nullable Language getLanguage() {
    if (getParent() == null) {
      return null;
    } else if (getParent() instanceof Language) {
      return (Language) getParent();
    } else {
      throw new IllegalStateException("The parent of this LanguageElement is not a Language");
    }
  }

  @Override
  public @Nullable String getName() {
    return this.getPropertyValue("name", String.class);
  }

  public T setName(String name) {
    this.setPropertyValue("name", name);
    return (T) this;
  }

  @Override
  public @Nullable NamespaceProvider getContainer() {
    if (this.getParent() == null) {
      return null;
    }
    if (this.getParent() instanceof NamespaceProvider) {
      return (NamespaceProvider) this.getParent();
    } else {
      throw new IllegalStateException("The parent is not a NamespaceProvider");
    }
  }

  @Override
  public String getKey() {
    return this.getPropertyValue("key", String.class);
  }

  @Override
  public T setKey(String key) {
    setPropertyValue("key", key);
    return (T) this;
  }

  protected Object getDerivedValue(Property property) {
//    if (property.getKey().equals(this.getConcept().getPropertyByName("qualifiedName").getKey())) {
//      return qualifiedName();
//    }
    return null;
  }

  @Override
  public String toString() {
    String qualifier = "<no language>";
    if (this.getContainer() != null) {
      if (this.getContainer().namespaceQualifier() != null) {
        qualifier = this.getContainer().namespaceQualifier();
      } else {
        qualifier = "<unnamed language>";
      }
    }
    ;
    String qualified = "<unnamed>";
    if (this.getName() != null) {
      qualified = this.getName();
    }
    ;
    String qn = qualifier + "." + qualified;
    return this.getClass().getName() + "(" + qn + ")";
  }
}
