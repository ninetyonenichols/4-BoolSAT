package bool_exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ASTNodeTest {

    @Test
    public void testIdNullChildren() {
        ASTNode testIdNode = ASTNode.createIdNode("testId");
        assertNull(testIdNode.child1);
        assertNull(testIdNode.child2);
    }
    

    @Test
    public void testNandNullChildren() {
        ASTNode testNandNode = ASTNode.createNandNode(null, null);
        assertNull(testNandNode.child1);
        assertNull(testNandNode.child2);
    }

    @Test
    public void testIdASTNodeChildren() {
        ASTNode testIdNode = ASTNode.createIdNode("testId");
        testIdNode.child1 = ASTNode.createIdNode("testChild1");
        testIdNode.child2 = ASTNode.createNandNode(null, null);
        assertTrue(testIdNode.child1.isId());
        assertTrue(testIdNode.child2.isNand());

    }

    @Test
    public void testNandASTNodeChildren() {
        ASTNode testChild1 = ASTNode.createIdNode("testChild1");
        ASTNode testChild2 = ASTNode.createNandNode(null, null);
        ASTNode testNandNode = ASTNode.createNandNode(testChild1, testChild2);
        assertTrue(testNandNode.child1.isId());
        assertTrue(testNandNode.child2.isNand());
    }

    @Test
    public void testNandIsId() {
        ASTNode testNandNode = ASTNode.createNandNode(null, null);
        assertFalse(testNandNode.isId());
    }

    @Test
    public void testIdIsId() {
        ASTNode testIdNode = ASTNode.createIdNode("testId");
        assertTrue(testIdNode.isId());
    }

    @Test
    public void testNandIsNand() {
        ASTNode testNandNode = ASTNode.createNandNode(null, null);
        assertTrue(testNandNode.isNand());
    }

    @Test
    public void testIdIsNand() {
        ASTNode testIdNode = ASTNode.createIdNode("testId");
        assertFalse(testIdNode.isNand());
    }

    @Test
    public void testGetIdMethod() {
        ASTNode testIdNode = ASTNode.createIdNode("testId");
        String id = testIdNode.getId();
        assertEquals(id, "testId");
    }
}
