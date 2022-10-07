/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lioncore.java;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleLanguageMetamodelTest {

    @Test public void emptyMetamodelDefinition() {
        Metamodel metamodel = new Metamodel("SimpleLanguage");
        assertEquals("SimpleLanguage", metamodel.getQualifiedName());
        assertEquals("SimpleLanguage", metamodel.namespaceQualifier());
        assertEquals(0, metamodel.dependsOn().size());
        assertEquals(0, metamodel.getElements().size());
    }

    @Test public void emptyConceptDefinition() {
        Metamodel metamodel = new Metamodel("SimpleLanguage");
        Concept expression = new Concept(metamodel, "Expression");
        assertEquals("Expression", expression.getSimpleName());
        assertSame(metamodel, expression.getContainer());
        assertEquals("SimpleLanguage.Expression", expression.qualifiedName());
        assertEquals("SimpleLanguage.Expression", expression.namespaceQualifier());
        assertNull(expression.getExtendedConcept());
        assertEquals(0, expression.getImplemented().size());
        assertFalse(expression.isAbstract());
    }
}
