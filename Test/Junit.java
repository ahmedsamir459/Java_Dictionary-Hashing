import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Junit {
    private UniversalHashing<Integer> hashing=new UniversalHashing<>(5);
    private Dictionary dictionary=new Dictionary("O(N^2)", 100);
    private Dictionary dictionary2=new Dictionary("O(N)", 100);

    @Test
    public void testInsert() {
        hashing.insert(5);
        assertTrue(hashing.search(5));
    }

    @Test
    public void testDelete() {
        hashing.insert(5);
        hashing.delete(5);
        assertFalse(hashing.search(5));
    }

    @Test
    public void testingBatchInsert() {
        Integer[] keys = {1, 2, 3, 4, 5};
        Integer[] keys2 = {6, 7, 8, 9, 10};
        hashing.batchInsert(keys);
        hashing.batchInsert(keys2);
        for (Integer key : keys) {
            assertTrue(hashing.search(key));
        }
        for (Integer key : keys2) {
            assertTrue(hashing.search(key));
        }
    }

    @Test
    public void testBatchDelete() {
        Integer[] keys = {1, 2, 3, 4, 5};
        Integer[] keys2 = {6, 7, 8, 9, 10};
        hashing.batchInsert(keys);
        hashing.batchDelete(keys);
        for (Integer key : keys) {
            assertFalse(hashing.search(key));
        }
        for (Integer key : keys2) {
            assertFalse(hashing.search(key));
        }
    }

    @Test
    public void testInsertAndContains() {
        UniversalHashing<String> hashTable = new UniversalHashing<>(5);

        hashTable.insert("apple");
        hashTable.insert("banana");
        hashTable.insert("cherry");

        assertTrue(hashTable.search("apple"));
        assertTrue(hashTable.search("banana"));
        assertTrue(hashTable.search("cherry"));

        assertFalse(hashTable.search("orange"));
    }

    @Test
    public void testCollisionDetection() {

        Integer[] keys = {1, 2, 3, 4, 5};

        UniversalHashing<Integer> hashTable = new UniversalHashing<>(keys);

        assertFalse(hashTable.isCollisionDetected());

        System.out.println("Collision count: " + hashTable.getCollisionCount());
        Assert.assertEquals(true,hashTable.getCollisionCount()<=5);
    }

    @Test
    public void testEmptyHashTable() {
        UniversalHashing<String> hashTable = new UniversalHashing<>(10);
        assertFalse(hashTable.search("apple"));
    }

    @Test
    public void testLargeHashTable() {
        UniversalHashing<Integer> hashTable = new UniversalHashing<>(1000);

        for (int i = 0; i < 1000; i++) {
            hashTable.insert(i);
        }
        for (int i = 0; i < 1000; i++) {
            assertTrue(hashTable.search(i));
        }
    }

    @Test
    public void testRebuildCount() {
        Integer[] keys = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        UniversalHashing<Integer> hashing = new UniversalHashing<>(keys);

        int rebuildCount = hashing.getRebuildCount();
        System.out.println("Initial rebuild count: " + rebuildCount);

        hashing.insert(11);

        rebuildCount = hashing.getRebuildCount();
        System.out.println("Rebuild count after 1st collision: " + rebuildCount);

        hashing.insert(12);
        hashing.insert(13);

        rebuildCount = hashing.getRebuildCount();
        System.out.println("Rebuild count after 2nd and 3rd collision: " + rebuildCount);
    }
    @Test
    public void testSpaceComplexity() {
        int numElements = 1000;
        UniversalHashing<String> hashTable = new UniversalHashing<>(numElements);

        for (int i = 0; i < numElements; i++) {
            String key = "key_" + i;
            hashTable.insert(key);
        }

        long memoryUsage = getMemoryUsage(hashTable);
        long expectedSpaceComplexity = numElements * numElements;

        assert memoryUsage >= expectedSpaceComplexity : "Space complexity violated!";
    }

    private long getMemoryUsage(Object obj) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        return memoryUsed;
    }



    @Test
    public void testDictionaryInsert() {
        dictionary.insert("apple");
        dictionary.insert("banana");
        dictionary.insert("cherry");

        assertTrue(dictionary.search("apple"));
        assertTrue(dictionary.search("banana"));
        assertTrue(dictionary.search("cherry"));
    }

    @Test
    public void testDictionaryDelete() {
        dictionary.insert("apple");
        dictionary.insert("banana");
        dictionary.insert("cherry");

        dictionary.delete("banana");

        assertTrue(dictionary.search("apple"));
        assertFalse(dictionary.search("banana"));
        assertTrue(dictionary.search("cherry"));
    }

    @Test
    public void testDictionarySearch() {
        dictionary.insert("apple");
        dictionary.insert("banana");
        dictionary.insert("cherry");

        assertTrue(dictionary.search("apple"));
        assertTrue(dictionary.search("banana"));
        assertTrue(dictionary.search("cherry"));
        assertFalse(dictionary.search("orange"));
    }

    @Test
    public void testDictionaryBatchInsert() {
        dictionary.insert("apple");
        dictionary.insert("banana");

        dictionary.batchInsert("test1.txt");

        assertTrue(dictionary.search("apple"));
        assertTrue(dictionary.search("banana"));
        assertTrue(dictionary.search("yet"));
        assertTrue(dictionary.search("ever"));
        assertTrue(dictionary.search("Then"));
    }

    @Test
    public void testDictionaryBatchDelete() {
        dictionary.insert("apple");
        dictionary.insert("banana");

        dictionary.batchDelete("test2.txt");
    }
}


