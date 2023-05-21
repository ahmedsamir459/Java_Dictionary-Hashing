import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Junit {
    private HashTable<Integer> hashing;
    private Dictionary dictionary=new Dictionary("O(N^2)", 100);
    private Dictionary dictionary2=new Dictionary("O(N)", 100);


    @Test
    public void compareCollisions(){
        HashTable<String> hash1=new UniversalHashing<>(100);
        HashTable<String> hash2=new PerfectHashing<>(100);
        int collisions1=0;
        int collisions2=0;
        for (int i=0;i<100;i++){
            hash1.insert("a"+i);
            hash2.insert("a"+i);
            collisions1+=hash1.getCollisionCount();
            collisions2+=hash2.getCollisionCount();
        }
        System.out.println(collisions1);
        System.out.println(collisions2);
        assertTrue(collisions2>collisions1);
    }
    @Test
    public void testInsert() {
        //test universal hashing
        hashing=new UniversalHashing<>(5);
        hashing.insert(5);
        assertTrue(hashing.search(5));
        //test perfect hashing
        hashing=new PerfectHashing<>(5);
        hashing.insert(5);
        assertTrue(hashing.search(5));
    }

    @Test
    public void testDelete() {
        //test universal hashing
        hashing=new UniversalHashing<>(5);
        hashing.insert(5);
        hashing.delete(5);
        assertFalse(hashing.search(5));
        //test perfect hashing
        hashing=new PerfectHashing<>(5);
        hashing.insert(5);
        hashing.delete(5);
        assertFalse(hashing.search(5));
    }

    @Test
    public void testingBatchInsert() {
        //test universal hashing
        hashing=new UniversalHashing<>(5);
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
        //test perfect hashing
        hashing=new PerfectHashing<>(5);
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
        //test universal hashing
        hashing=new UniversalHashing<>(5);
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
        //test perfect hashing
        hashing=new PerfectHashing<>(5);
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
        //test universal hashing
        UniversalHashing<String> hashTable = new UniversalHashing<>(5);
        hashTable.insert("apple");
        hashTable.insert("banana");
        hashTable.insert("cherry");

        assertTrue(hashTable.search("apple"));
        assertTrue(hashTable.search("banana"));
        assertTrue(hashTable.search("cherry"));
        assertFalse(hashTable.search("orange"));
        //test perfect hashing
        PerfectHashing<String> hashTable2 = new PerfectHashing<>(5);
        hashTable2.insert("apple");
        hashTable2.insert("banana");
        hashTable2.insert("cherry");

        assertTrue(hashTable2.search("apple"));
        assertTrue(hashTable2.search("banana"));
        assertTrue(hashTable2.search("cherry"));
        assertFalse(hashTable2.search("orange"));
    }

    @Test
    public void testCollisionDetection() {
        //test universal hashing
        Integer[] keys = {1, 2, 3, 4, 5};

        UniversalHashing<Integer> hashTable = new UniversalHashing<>(keys);

        assertFalse(hashTable.isCollisionDetected());

        System.out.println("Collision count: " + hashTable.getCollisionCount());
        Assert.assertEquals(true,hashTable.getCollisionCount()<=5);
        //test perfect hashing
        PerfectHashing<Integer> hashTable2 = new PerfectHashing<>(keys.length);
        hashTable2.batchInsert(keys);
        System.out.println("Collision count: " + hashTable2.getCollisionCount());
        Assert.assertEquals(true,hashTable2.getCollisionCount()<=5);

    }

    @Test
    public void testEmptyHashTable() {
        //test universal hashing
        UniversalHashing<String> hashTable = new UniversalHashing<>(10);
        assertFalse(hashTable.search("apple"));
    }

    @Test
    public void testLargeHashTable() {
        //test universal hashing
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
        //test universal hashing
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


    /** Space Complexity Tests */
    @Test
    public void testSpaceComplexity() {
        int numElements = 1000;
        HashTable<String> hashTable = new UniversalHashing<>(numElements);

        for (int i = 0; i < numElements; i++) {
            String key = "key_" + i;
            hashTable.insert(key);
        }

        long memoryUsage = getMemoryUsage(hashTable);
        long expectedSpaceComplexity = numElements * numElements;

        System.out.println("Memory usage: " + memoryUsage);
        System.out.println("Expected space complexity: " + expectedSpaceComplexity);
        assert memoryUsage >= expectedSpaceComplexity : "Space complexity violated!";
    }
    @Test
    public void testSpaceComplexity2() {
        int numElements = 1000;
        HashTable<String> hashTable = new PerfectHashing<>(numElements);

        for (int i = 0; i < numElements; i++) {
            String key = "key_" + i;
            hashTable.insert(key);
        }

        long memoryUsage = getMemoryUsage(hashTable);
        long expectedSpaceComplexity = numElements;
        System.out.println("Memory usage: " + memoryUsage);
        System.out.println("Expected space complexity: " + expectedSpaceComplexity);

        assert memoryUsage >= expectedSpaceComplexity : "Space complexity violated!";
    }

    private long getMemoryUsage(Object obj) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        return memoryUsed;
    }




    /** Dictionary Tests */
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

        assertTrue(dictionary.search("apple"));
        assertTrue(dictionary.search("banana"));
        assertFalse(dictionary.search("yet"));
        assertFalse(dictionary.search("ever"));
        assertFalse(dictionary.search("Then"));

    }
    @Test
    public void testDictionary2Insert() {
        dictionary2.insert("apple");
        dictionary2.insert("banana");
        dictionary2.insert("cherry");

        assertTrue(dictionary2.search("apple"));
        assertTrue(dictionary2.search("banana"));
        assertTrue(dictionary2.search("cherry"));
    }
    @Test
    public void testDictionary2Delete() {
        dictionary2.insert("apple");
        dictionary2.insert("banana");
        dictionary2.insert("cherry");

        dictionary2.delete("banana");

        assertTrue(dictionary2.search("apple"));
        assertFalse(dictionary2.search("banana"));
        assertTrue(dictionary2.search("cherry"));
    }
    @Test
    public void testDictionary2Search() {
        dictionary2.insert("apple");
        dictionary2.insert("banana");
        dictionary2.insert("cherry");

        assertTrue(dictionary2.search("apple"));
        assertTrue(dictionary2.search("banana"));
        assertTrue(dictionary2.search("cherry"));
        assertFalse(dictionary2.search("orange"));
    }
    @Test
    public void testDictionary2BatchInsert() {
        dictionary2.insert("apple");
        dictionary2.insert("banana");

        dictionary2.batchInsert("test1.txt");

        assertTrue(dictionary2.search("apple"));
        assertTrue(dictionary2.search("banana"));
        assertTrue(dictionary2.search("yet"));
        assertTrue(dictionary2.search("ever"));
        assertTrue(dictionary2.search("Then"));
    }
    @Test
    public void testDictionary2BatchDelete() {
        dictionary2.insert("apple");
        dictionary2.insert("banana");

        dictionary2.batchDelete("test2.txt");

        assertTrue(dictionary2.search("apple"));
        assertTrue(dictionary2.search("banana"));
        assertFalse(dictionary2.search("yet"));
        assertFalse(dictionary2.search("ever"));
        assertFalse(dictionary2.search("Then"));
    }
}


