package org.lionweb.lioncore.java.metamodel;

/**
 * Something with a name and contained in a Namespace.
 *
 * A Concept Invoice, contained in a Metamodel com.foo.Accounting.
 * Therefore, Invoice will have the qualifiedName com.foo.Accounting.Invoice.
 */
public interface NamespacedEntity {
    // TODO add ID, once details are cleare
    String getSimpleName();
    String qualifiedName();
    NamespaceProvider getContainer();
}
