import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Junit {
    int size = 20000;
    private HashTable<Integer> hashing;
    private Dictionary dictionary = new Dictionary("O(N^2)", size);
    private Dictionary dictionary2 = new Dictionary("O(N)", 85000);


    @Test
    public void testUniversalHashing() {
        HashTable<Integer> hashTable = new UniversalHashing<>(8);
        testHashTable(hashTable);
    }

    @Test
    public void testPerfectHashing() {
        HashTable<Integer> hashTable = new PerfectHashing<>(8);
        testHashTable(hashTable);
    }

    @Test
    public void timeComparisons() {
        List<String> keys = readKeysFromFile("1000.txt");

        HashTable<String> dictionary = new UniversalHashing<>(keys.size());
        HashTable<String> dictionary2 = new PerfectHashing<>(keys.size());

        long avr1 = 0;
        long avr2 = 0;

        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();
            for (String key : keys) {
                dictionary.insert(key);
            }
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            dictionary = new UniversalHashing<>(keys.size());
            avr1 += duration;
        }
        avr1 /= 10;
        System.out.println("Time taken to insert 20k keys in O(N^2) is: " + avr1 + " ms");

        for (int i = 0; i < 10; i++) {
            long startTime2 = System.currentTimeMillis();
            for (String key : keys) {
                dictionary2.insert(key);
            }
            long endTime2 = System.currentTimeMillis();
            long duration2 = (endTime2 - startTime2);
            dictionary2 = new PerfectHashing<>(keys.size());
            avr2 += duration2;
        }
        avr2 /= 10;
        System.out.println("Time taken to insert 20k keys in O(N) is: " + avr2 + " ms");
    }


    private void testHashTable(HashTable<Integer> hashTable) {
        // Insertion tests
        Assert.assertTrue(hashTable.insert(5));
        Assert.assertTrue(hashTable.insert(15));
        Assert.assertTrue(hashTable.insert(25));
        Assert.assertTrue(hashTable.insert(35));
        Assert.assertTrue(hashTable.insert(45));
        Assert.assertTrue(hashTable.insert(55));
        Assert.assertTrue(hashTable.insert(65));
        Assert.assertTrue(hashTable.insert(75));
        Assert.assertFalse(hashTable.insert(5)); // Already inserted, should return false

        // Search tests
        Assert.assertTrue(hashTable.search(5));
        Assert.assertTrue(hashTable.search(15));
        Assert.assertTrue(hashTable.search(25));
        Assert.assertTrue(hashTable.search(35));
        Assert.assertTrue(hashTable.search(45));
        Assert.assertTrue(hashTable.search(55));
        Assert.assertTrue(hashTable.search(65));
        Assert.assertTrue(hashTable.search(75));
        Assert.assertFalse(hashTable.search(105)); // Not inserted, should return false

        // Deletion tests
        Assert.assertTrue(hashTable.delete(5));
        Assert.assertTrue(hashTable.delete(15));
        Assert.assertTrue(hashTable.delete(25));
        Assert.assertTrue(hashTable.delete(35));
        Assert.assertTrue(hashTable.delete(45));
        Assert.assertTrue(hashTable.delete(55));
        Assert.assertTrue(hashTable.delete(65));
        Assert.assertTrue(hashTable.delete(75));
        Assert.assertFalse(hashTable.delete(105)); // Not inserted, should return false

        // Batch insertion tests
        Integer[] keys = {2, 4, 6, 8, 10};
        hashTable.batchInsert(keys);
        Assert.assertTrue(hashTable.search(2));
        Assert.assertTrue(hashTable.search(4));
        Assert.assertTrue(hashTable.search(6));
        Assert.assertTrue(hashTable.search(8));
        Assert.assertTrue(hashTable.search(10));

        // Batch deletion tests
        hashTable.batchDelete(keys);
        Assert.assertFalse(hashTable.search(2));
        Assert.assertFalse(hashTable.search(4));
        Assert.assertFalse(hashTable.search(6));
        Assert.assertFalse(hashTable.search(8));
        Assert.assertFalse(hashTable.search(10));

//         Performance tests
        int[] largeKeys = generateLargeKeys(10000);
        if (hashTable instanceof UniversalHashing) {
            System.out.println("Universal Hashing");
            hashTable = new UniversalHashing<>(10000);
        } else {
            System.out.println("Perfect Hashing");
            hashTable = new PerfectHashing<>(10000);

        }
        long start = System.currentTimeMillis();
        for (int key : largeKeys) {
            hashTable.insert(key);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken to insert 10,000 keys: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int key : largeKeys) {
            hashTable.search(key);
        }
        end = System.currentTimeMillis();
        System.out.println("Time taken to search 10,000 keys: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int key : largeKeys) {
            hashTable.delete(key);
        }
        end = System.currentTimeMillis();
        System.out.println("Time taken to delete 10,000 keys: " + (end - start) + " ms");

        System.out.println("Number of collisions: " + hashTable.getCollisionCount());
    }

    private int[] generateLargeKeys(int i) {
        int[] keys = new int[i];
        for (int j = 0; j < i; j++) {
            keys[j] = j;
        }
        return keys;
    }

    @Test
    public void testInsert() {
        //test universal hashing
        hashing = new UniversalHashing<>(5);
        hashing.insert(5);
        assertTrue(hashing.search(5));
        //test perfect hashing
        hashing = new PerfectHashing<>(5);
        hashing.insert(5);
        assertTrue(hashing.search(5));
    }

    @Test
    public void testDelete() {
        //test universal hashing
        hashing = new UniversalHashing<>(5);
        hashing.insert(5);
        hashing.delete(5);
        assertFalse(hashing.search(5));
        //test perfect hashing
        hashing = new PerfectHashing<>(5);
        hashing.insert(5);
        hashing.delete(5);
        assertFalse(hashing.search(5));
    }

    @Test
    public void testingBatchInsert() {
        //test universal hashing
        hashing = new UniversalHashing<>(5);
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
        hashing = new PerfectHashing<>(5);
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
        hashing = new UniversalHashing<>(5);
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
        hashing = new PerfectHashing<>(5);
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
    public void testEmptyHashTable() {
        UniversalHashing<String> hashTable = new UniversalHashing<>(10);
        PerfectHashing<String> hashTable2 = new PerfectHashing<>(10);

        assertFalse(hashTable.search("apple"));
        assertFalse(hashTable2.search("apple"));
    }

    @Test
    public void testLargeHashTable() {
        HashTable<String> hashTable = new UniversalHashing<>(1000);
        HashTable<String> hashTable2 = new PerfectHashing<>(1000);

        for (int i = 0; i < 1000; i++) {
            hashTable.insert("key" + i);
            hashTable2.insert("key" + i);
        }
        for (int i = 0; i < 1000; i++) {
            assertTrue(hashTable.search("key" + i));
            assertTrue(hashTable2.search("key" + i));
        }
    }

    @Test
    public void testCollisionCount() {

        List<String> words = readKeysFromFile("10k.txt");
        HashTable<String> hashTable = new PerfectHashing<>(words.size());
        HashTable<String> hashTable2 = new UniversalHashing<>(words.size());

        hashTable.batchInsert(words.toArray(new String[0]));
        hashTable2.batchInsert(words.toArray(new String[0]));

        int rebuildCount = hashTable.getCollisionCount();
        int rebuildCount2 = hashTable2.getCollisionCount();

        System.out.println("Collision count for O(N): " + rebuildCount);
        System.out.println("Collision count for O(N^2): " + rebuildCount2);

        Assert.assertEquals(true, rebuildCount <= words.size());
        Assert.assertEquals(true, rebuildCount2 <= words.size());
    }

    @Test
    public void testRebuildCount() {

        List<String> words = readKeysFromFile("20k.txt");
        HashTable<String> hashTable = new PerfectHashing<>(words.size());
        HashTable<String> hashTable2 = new UniversalHashing<>(words.size());

        hashTable.batchInsert(words.toArray(new String[0]));
        hashTable2.batchInsert(words.toArray(new String[0]));

        int rebuildCount = hashTable.getRebuildCount();
        int rebuildCount2 = hashTable2.getRebuildCount();

        System.out.println("ReBuild count for O(N): " + rebuildCount);
        System.out.println("ReBuild count for O(N^2): " + rebuildCount2);

        Assert.assertEquals(true, rebuildCount <= words.size());
        Assert.assertEquals(true, rebuildCount2 <= words.size());
    }


    /**
     * Space Complexity Tests
     */
    @Test
    public void testSpaceComplexity() {

        int n = 1000;
        UniversalHashing<String> hashTable = new UniversalHashing<>(n);
        HashTableSpaceComplexityTest hashTableSpaceComplexityTest = new HashTableSpaceComplexityTest();
        for (int i = 0; i < n; i++) {
            hashTable.insert("key" + i);
        }
        System.out.println("Space complexity: " + hashTableSpaceComplexityTest.calculateSpaceComplexity(hashTable));
        Assert.assertEquals(true, hashTableSpaceComplexityTest.calculateSpaceComplexity(hashTable) <= Math.pow(n, 2));

        //test perfect hashing
        PerfectHashing<String> hashTable2 = new PerfectHashing<>(n);

        for (int i = 0; i < n; i++) {
            hashTable2.insert("key" + i);
        }
        System.out.println("Space complexity: " + hashTableSpaceComplexityTest.calculateSpaceComplexity(hashTable2));
        Assert.assertEquals(true, n <= hashTableSpaceComplexityTest.calculateSpaceComplexity(hashTable2) && hashTableSpaceComplexityTest.calculateSpaceComplexity(hashTable2) <= Math.log(n) * n);
    }


    /**
     * Dictionary Tests
     */
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
        dictionary.batchInsert("20k.txt");

        assertTrue(dictionary.search("apple"));
        assertTrue(dictionary.search("banana"));
        assertTrue(dictionary.search("mQOD"));
        assertTrue(dictionary.search("ScW5cWX"));
        assertTrue(dictionary.search("p5gJsP"));
    }

    @Test
    public void testDictionaryBatchDelete() {
        dictionary.insert("apple");
        dictionary.insert("banana");


        dictionary.batchInsert("20k.txt");
        dictionary.batchDelete("20k.txt");

        assertTrue(dictionary.search("apple"));
        assertTrue(dictionary.search("banana"));
        assertFalse(dictionary.search("mQOD"));
        assertFalse(dictionary.search("ScW5cWX"));
        assertFalse(dictionary.search("p5gJsP"));

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


    private List<String> readKeysFromFile(String filePath) {
        List<String> keys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("testcases/" + filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keys.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("\u001B[31mAn ERROR occurred opening file\u001B[0m ");
        }
        return keys;
    }

    @Test
    public void testSearchTime() {

        List<String> keys = readKeysFromFile("20k.txt");
        HashTable<String> hashTable2 = new PerfectHashing<>(keys.size());

        for (String key : keys) {
            hashTable2.insert(key);
        }
        long start = System.currentTimeMillis();
        hashTable2.search("aa");
        hashTable2.search("nOXTL");
        hashTable2.search("QyOB");
        hashTable2.search("0U12qRX");
        hashTable2.search("q6a");
        hashTable2.search("4mHe14");
        long end = System.currentTimeMillis();
        long avr = (end - start) ;
        System.out.println("Average search time for Perfect Hashing: " + avr + "ns");

    }

}


