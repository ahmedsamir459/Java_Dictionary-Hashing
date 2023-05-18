import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class Junit<T extends Comparable<T>> {
    Dictionary d1 = new Dictionary(1); // AVL
    Dictionary d2 = new Dictionary(2); // Red black
    int test1_getSize = 84402;
    int test2_getSize = 10000;
    int test10_getSize = 466472;

    // AVL TESTS

    @Test
    public void single_insert_avl_test() {
        d1.insert("apple");
        assertEquals(1, d1.getSize());
        assertEquals(1, d1.getHeight());
        assertTrue(d1.search("apple"));
    }

    @Test
    public void single_delete_avl_test() {
        d1.insert("apple");
        assertEquals(1, d1.getSize());
        assertEquals(1, d1.getHeight());
        d1.delete("apple");
        assertEquals(0, d1.getSize());
        assertEquals(0, d1.getHeight());
        assertFalse(d1.search("apple"));
    }

    @Test
    public void batch_insert_avl_test() {
        d1.batch_insert("test1.txt");
        assertEquals(test1_getSize, d1.getSize());
        assertEquals(19, d1.getHeight());
        d1.batch_insert("test1.txt"); // insert again but still the same
        assertEquals(test1_getSize, d1.getSize());
        assertEquals(19, d1.getHeight());
    }

    @Test
    public void batch_insert_empty_avl_test() {
        d1.batch_insert("empty.txt");
        assertEquals(0, d1.getSize());
        assertEquals(0, d1.getHeight());
    }

    @Test
    public void batch_delete_avl_test() {
        d1.batch_insert("test1.txt");
        d1.batch_delete("test1.txt");
        assertEquals(0, d1.getSize());
        d1.batch_insert("test2.txt");
        d1.batch_delete("test2.txt");
        assertEquals(0, d1.getSize());
    }

    @Test
    public void insert_bigdata_avl_test() {
        d1.batch_insert("test8.txt");
        System.out.println(d1.getHeight());
    }


    // RED BLACK TESTS

    @Test
    public void single_insert_rb_test() {
        d2.insert("apple");
        assertEquals(1, d2.getSize());
        assertEquals(1, d2.getHeight());
        assertTrue(d2.search("apple"));
    }

    @Test
    public void single_delete_rb_test() {
        d2.insert("apple");
        assertEquals(1, d2.getSize());
        assertEquals(1, d2.getHeight());
        d2.delete("apple");
        assertEquals(0, d2.getSize());
        assertEquals(0, d2.getHeight());
        assertFalse(d2.search("apple"));
    }

    @Test
    public void batch_insert_rb_test() {
        d2.batch_insert("test1.txt");
        assertEquals(test1_getSize, d2.getSize());
        d2.batch_insert("test1.txt"); // insert again but still the same
        assertEquals(test1_getSize, d2.getSize());
    }

    @Test
    public void batch_insert_empty_rb_test() {
        d2.batch_insert("empty.txt");
        assertEquals(0, d2.getSize());
        assertEquals(0, d2.getHeight());
    }

    @Test
    public void batch_delete_rb_test() {
        d2.batch_insert("test1.txt");
        d2.batch_delete("test1.txt");
        assertEquals(0, d2.getSize());
        d2.batch_insert("test2.txt");
        d2.batch_delete("test2.txt");
        assertEquals(0, d2.getSize());
    }

    @Test
    public void insert_bigdata_rb_test() {
        d2.batch_insert("test8.txt");
        System.out.println(d2.getHeight());
    }

    //Comparsion

    @Test
    public void operations_time() {
        long start1 = System.nanoTime();
        d2.batch_insert("test8.txt");
        long end1 = System.nanoTime();

        long start2 = System.nanoTime();
        d2.search("nonatomical");
        d2.search("alreadyexist");
        long end2 = System.nanoTime();

        long start3 = System.nanoTime();
        d2.delete("parade");
        long end3 = System.nanoTime();

        long start4 = System.nanoTime();
        d1.batch_insert("test8.txt");
        long end4 = System.nanoTime();

        long start5 = System.nanoTime();
        d1.search("nonatomical");
        d1.search("alreadyexist");
        long end5 = System.nanoTime();

        long start6 = System.nanoTime();
        d1.delete("parade");
        long end6 = System.nanoTime();

        System.out.println("\u001B[35mTime taken to insert in AVL tree = (" + (end4 - start4) + ") nanoseconds\u001B[0m");
        System.out.println("\u001B[35mTime taken to search in AVL tree = (" + (end5 - start5) + ") nanoseconds\u001B[0m");
        System.out.println("\u001B[35mTime taken to delete in AVL BLACK tree = (" + (end6 - start6) + ") nanoseconds\u001B[0m");

        System.out.println("\u001B[34mTime taken to insert in RED BLACK tree = (" + (end1 - start1) + ") nanoseconds\u001B[0m");
        System.out.println("\u001B[34mTime taken to search in RED BLACK tree = (" + (end2 - start2) + ") nanoseconds\u001B[0m");
        System.out.println("\u001B[34mTime taken to delete in RED BLACK tree = (" + (end3 - start3) + ") nanoseconds\u001B[0m");
    }

    @Test
    public void testAvlTree() {
        Tree<Integer> avlTree = new AVL<Integer>();
        avlTree.insert(10);
        avlTree.insert(5);
        avlTree.insert(15);
        avlTree.insert(3);
        avlTree.insert(7);
        avlTree.insert(12);
        avlTree.insert(18);
        avlTree.insert(1);
        avlTree.insert(4);
        avlTree.insert(6);
        avlTree.insert(8);
        avlTree.insert(11);
        avlTree.insert(13);
        avlTree.insert(16);
        avlTree.insert(19);
        avlTree.insert(2);
        avlTree.insert(9);
        avlTree.insert(14);
        avlTree.insert(17);
        avlTree.insert(20);

        assertEquals(20, avlTree.getSize());
        assertEquals(5, avlTree.getHeight());
        assertEquals(1, (int) avlTree.getMin());
        assertEquals(20, (int) avlTree.getMax());
        assertThrows(IllegalArgumentException.class, () -> avlTree.insert(20));
//        testbalnce(avlTree.getRoot());
    }

    @Test
    public void testRedBlackTree() {
        Tree<Integer> rbTree = new RB<Integer>();
        rbTree.insert(10);
        rbTree.insert(5);
        rbTree.insert(15);
        rbTree.insert(3);
        rbTree.insert(7);
        rbTree.insert(12);
        rbTree.insert(18);
        rbTree.insert(1);
        rbTree.insert(4);
        rbTree.insert(6);
        rbTree.insert(8);
        rbTree.insert(11);
        rbTree.insert(13);
        rbTree.insert(16);
        rbTree.insert(19);
        rbTree.insert(2);
        rbTree.insert(9);
        rbTree.insert(14);
        rbTree.insert(17);
        rbTree.insert(20);
        assertEquals(20, rbTree.getSize());
        assertEquals(5, rbTree.getHeight());
        assertEquals(1, (int) rbTree.getMin());
        assertEquals(20, (int) rbTree.getMax());
        testBlackHight((Node<Integer>) rbTree.getRoot(), 0, 0);
    }

    private void testBlackHight(Node<Integer> node, int blackHight, int currentBlackHight) {

        if (node == null) {
            if (blackHight == 0) {
                blackHight = currentBlackHight;
            } else {
                try {
                    assertEquals(blackHight, currentBlackHight);
                } catch (AssertionError e) {
                    throw new AssertionError("Black height is not equal on all paths (blackHeightFirstPath =" + blackHight + "; blackHeightThisPath = " + currentBlackHight + ")");
                }
            }
            return;
        }
        if (node.getColor() == Color.BLACK) {
            currentBlackHight++;
        } else if (node.getParent() != null && node.getParent().getColor() == Color.RED) {
            fail("Red node has red parent");
        }
        testBlackHight(node.getLeft(), blackHight, currentBlackHight);
        testBlackHight(node.getRight(), blackHight, currentBlackHight);
    }

    void testbalnce(Node<Integer> node) {
        if (node == null) {
            return;
        }
        try {
            int h1 = node.getLeft() == null ? 0 : node.getLeft().getHeight();
            int h2 = node.getRight() == null ? 0 : node.getRight().getHeight();
            assertEquals(1, Math.abs(h1 - h2));
        } catch (AssertionError e) {
            int h1 = node.getLeft() == null ? 0 : node.getLeft().getHeight();
            int h2 = node.getRight() == null ? 0 : node.getRight().getHeight();
            assertEquals(0, Math.abs(h1 - h2));
        }
        testbalnce(node.getLeft());
        testbalnce(node.getRight());
    }

}
