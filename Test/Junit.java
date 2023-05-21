import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Junit {
    private UniversalHashing<Integer> hashing=new UniversalHashing<>(5);

    @Test
    public void testInsert() {
        hashing.insert(5);
        Assert.assertTrue(hashing.search(5));
    }

    @Test
    public void testDelete() {
        hashing.insert(5);
        hashing.delete(5);
        Assert.assertFalse(hashing.search(5));
    }

    @Test
    public void testingBatchInsert() {
        Integer[] keys = {1, 2, 3, 4, 5};
        Integer[] keys2 = {6, 7, 8, 9, 10};
        hashing.batchInsert(keys);
        hashing.batchInsert(keys2);
        for (Integer key : keys) {
            Assert.assertTrue(hashing.search(key));
        }
        for (Integer key : keys2) {
            Assert.assertTrue(hashing.search(key));
        }
    }

    @Test
    public void testBatchDelete() {
        Integer[] keys = {1, 2, 3, 4, 5};
        Integer[] keys2 = {6, 7, 8, 9, 10};
        hashing.batchInsert(keys);
        hashing.batchDelete(keys);
        for (Integer key : keys) {
            Assert.assertFalse(hashing.search(key));
        }
        for (Integer key : keys2) {
            Assert.assertFalse(hashing.search(key));
        }
    }

    @Test
    public void testInsertAndContains() {
        UniversalHashing<String> hashTable = new UniversalHashing<>(5);

        // Insert some keys
        hashTable.insert("apple");
        hashTable.insert("banana");
        hashTable.insert("cherry");

        // Check if the keys are present
        Assert.assertTrue(hashTable.search("apple"));
        Assert.assertTrue(hashTable.search("banana"));
        Assert.assertTrue(hashTable.search("cherry"));

        // Check for non-existent key
        Assert.assertFalse(hashTable.search("orange"));
    }

    @Test
    public void testCollisionDetection() {
        // Create an array with keys that are known to cause collisions
        Integer[] keys = {1, 2, 3, 4, 5};

        UniversalHashing<Integer> hashTable = new UniversalHashing<>(keys);

        // Check if collision is detected
        Assert.assertFalse(hashTable.isCollisionDetected());

        // Check the collision count
        System.out.println("Collision count: " + hashTable.getCollisionCount());
        Assert.assertEquals(true,hashTable.getCollisionCount()<=5);
    }

    @Test
    public void testEmptyHashTable() {
        UniversalHashing<String> hashTable = new UniversalHashing<>(10);

        // Check for non-existent key in an empty hash table
        Assert.assertFalse(hashTable.search("apple"));
    }

    @Test
    public void testLargeHashTable() {
        // Create a large hash table with 1000 elements
        UniversalHashing<Integer> hashTable = new UniversalHashing<>(1000);

        // Insert elements from 0 to 999
        for (int i = 0; i < 1000; i++) {
            hashTable.insert(i);
        }

        // Check if all elements are present
        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue(hashTable.search(i));
        }
    }

    @Test
    public void testBatchInsert() {
        int size = 10000; // Size of the array
        Integer[] keys = new Integer[size];
        for (int i = 0; i < size; i++) {
            keys[i] = i;
        }

        UniversalHashing<Integer> hashTable = new UniversalHashing<>(keys);

        // Check if all keys are present in the hash table
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(hashTable.search(i));
        }

        // Check if a collision occurred during insertion
        Assert.assertFalse(hashTable.isCollisionDetected());
        System.out.println("Collision count: " + hashTable.getCollisionCount());
    }

    @Test
    public void testRebuildCount() {
        Integer[] keys = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        UniversalHashing<Integer> hashing = new UniversalHashing<>(keys);

        int rebuildCount = hashing.getRebuildCount();
        System.out.println("Initial rebuild count: " + rebuildCount);

        // Insert a key causing a collision
        hashing.insert(11);

        rebuildCount = hashing.getRebuildCount();
        System.out.println("Rebuild count after 1st collision: " + rebuildCount);

        // Insert more keys causing collisions
        hashing.insert(12);
        hashing.insert(13);

        rebuildCount = hashing.getRebuildCount();
        System.out.println("Rebuild count after 2nd and 3rd collision: " + rebuildCount);
    }
    @Test
    public void testSpaceComplexity() {
        int numElements = 1000; // Number of elements to insert
        UniversalHashing<String> hashTable = new UniversalHashing<>(numElements);

        // Insert elements into the hash table
        for (int i = 0; i < numElements; i++) {
            String key = "key_" + i;
            hashTable.insert(key);
        }

        long memoryUsage = getMemoryUsage(hashTable);
        long expectedSpaceComplexity = numElements * numElements;

        // Verify that the memory usage is approximately O(N^2)
        assert memoryUsage >= expectedSpaceComplexity : "Space complexity violated!";
    }

    private long getMemoryUsage(Object obj) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Run garbage collector to release memory
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        return memoryUsed;
    }
}


