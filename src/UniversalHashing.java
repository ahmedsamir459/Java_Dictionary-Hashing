import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UniversalHashing <T extends Comparable<T>> implements HashTable<T>{
    private int size;
    private List<T>[] table;
    private int[][] hashFunction;
    private Random random;
    private boolean collisionDetected;
    private int collisionCount;
    private int rebuildCount;

    public UniversalHashing(T[] keys) {
        size = keys.length;
        random = new Random();
        // Create the hash table
        table = new List[size * size];
        collisionDetected = false;
        collisionCount = 0;
        rebuildCount = 0;
        buildHashTable(keys);
    }

    public UniversalHashing(int size) {
        this.size = size;
        random = new Random();
        // Create the hash table
        table = new List[size * size];
        generateHashFunction();
    }

    @Override
    public void insert(T key) {
        int index = calculateIndex(key);
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }
        table[index].add(key);

        // Handle collision
        if (table[index].size() > 1) {
            List<T> elementsToReinsert = new ArrayList<>();
            for (List<T> bin : table) {
                if (bin != null) {
                    elementsToReinsert.addAll(bin);
                }
            }
            Arrays.fill(table, null);
            // Reinsert the elements
            T[] reinsertArray = toArrayWithElementType(elementsToReinsert);
            buildHashTable(reinsertArray);
        }
    }
    @SuppressWarnings("unchecked")
    private T[] toArrayWithElementType(List<T> list) {
        T[] array = (T[]) new Comparable[list.size()];
        return list.toArray(array);
    }


    @Override
    public void delete(T key) {
        int index = calculateIndex(key);
        if (table[index] != null) {
            table[index].remove(key);
        }
    }

    @Override
    public void batchInsert(T[] keys) {
        for (T key : keys) {
            insert(key);
        }
    }

    @Override
    public void batchDelete(T[] keys) {
        for (T key : keys) {
            delete(key);
        }
    }

    private void buildHashTable(T[] keys) {
        do {
            collisionDetected = false;
            generateHashFunction();

            for (T key : keys) {
                int index = calculateIndex(key);
                if (table[index] != null && !table[index].isEmpty()) {
                    collisionDetected = true;
                    collisionCount++;
                    break;
                }
                if (table[index] == null) {
                    table[index] = new ArrayList<>();
                }
                table[index].add(key);
            }

            if (collisionDetected) {
                rebuildCount++;
                table = new List[size * size]; // Clear the table if collisions occurred
            }
        } while (collisionDetected);
    }

    private void generateHashFunction() {
        hashFunction = new int[log2(size * size)][32];
        for (int i = 0; i < log2(size * size); i++) {
            for (int j = 0; j < 32; j++) {
                hashFunction[i][j] = random.nextInt(2);
            }
        }
    }

    private int calculateIndex(T key) {
        int index = 0;
        for (int i = 0; i < log2(size * size); i++) {
            int[] hash = hashFunction[i];
            int h = dotProduct(hash, key);
            index = (index << 1) | h;
        }
        return index % (size * size);
    }

    private int dotProduct(int[] hash, T key) {
        int result = 0;
        int[] keyBits = toBinaryArray(key.hashCode());
        for (int i = 0; i < 32; i++) {
            result += hash[i] * keyBits[i];
        }
        return result % 2;
    }

    private int[] toBinaryArray(int n) {
        int[] binaryArray = new int[32];
        for (int i = 31; i >= 0; i--) {
            binaryArray[i] = n & 1;
            n >>= 1;
        }
        return binaryArray;
    }

    private int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    @Override
    public boolean search(T key) {
        int index = calculateIndex(key);
        if (table[index] != null) {
            return table[index].contains(key);
        }
        return false;
    }

    public void print() {
        for (int i = 0; i < size * size; i++) {
            System.out.println(table[i]);
        }
    }

    public boolean isCollisionDetected() {
        return collisionDetected;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public int getRebuildCount() {
        return rebuildCount;
    }
}
