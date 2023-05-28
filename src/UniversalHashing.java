import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UniversalHashing<T extends Comparable<T>> implements HashTable<T> {
    private int size;
    List<T>[] table;
    int[][] hashFunction;
    private Random random;
    private boolean collisionDetected;
    private int collisionCount;
    private int rebuildCount;

    public UniversalHashing(int size) {
        this.size = size;
        random = new Random();
        table = new List[size * size];
        generateHashFunction();
    }

    @Override
    public boolean insert(T key) {
        int index = calculateIndex(key);
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }

        if (table[index].contains(key)) {
            return false;
        } else {
            table[index].add(key);
            if (table[index].size() > 1) {
                rebuildCount++;
                collisionCount++;
                List<T> elementsToReinsert = new ArrayList<>();
                for (List<T> bin : table) {
                    if (bin != null) {
                        elementsToReinsert.addAll(bin);
                    }
                }
                Arrays.fill(table, null);
                T[] reinsertArray = toArrayWithElementType(elementsToReinsert);
                buildHashTable(reinsertArray);
            }
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private T[] toArrayWithElementType(List<T> list) {
        T[] array = (T[]) new Comparable[list.size()];
        return list.toArray(array);
    }


    @Override
    public boolean delete(T key) {
        int index = calculateIndex(key);
        if (table[index] != null) {
            table[index].remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int batchInsert(T[] keys) {
        int inserted = 0;
        for (T key : keys) {
            if (insert(key)) {
                inserted++;
            }
        }
        return inserted;
    }

    @Override
    public void batchDelete(T[] keys) {
        for (T key : keys) {
            delete(key);
        }
    }

    private int buildHashTable(T[] keys) {
        boolean collisionDetected;
        int count = keys.length;

        do {
            collisionDetected = false;
            generateHashFunction();

            for (T key : keys) {
                int index = calculateIndex(key);
                if (table[index] != null && !table[index].isEmpty()) {
                    if (table[index].contains(key)) {
                        count--;
                        continue;
                    }
                    table[index] = null;
                    collisionDetected = true;
                    this.collisionDetected = true;

                    break;
                }
                if (table[index] == null) {
                    table[index] = new ArrayList<>();
                }
                table[index].add(key);
            }

            if (collisionDetected) {
                collisionCount++;
                table = null;
                table = new List[size * size]; // Clear the table if collisions occurred
            }
        } while (collisionDetected);
        return count;
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
        int[] keyBits = toBinaryArray(this.customHashFunction(key));
        for (int i = 0; i < log2(size * size); i++) {
            int[] hash = hashFunction[i];
            int h = dotProduct(hash, key);
            index = (index << 1) | h;
        }
        return index % (size * size);
    }

    private int dotProduct(int[] hash, T key) {
        int result = 0;
        int[] keyBits = toBinaryArray(this.customHashFunction(key));
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
        return this.collisionDetected;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public int getRebuildCount() {
        return rebuildCount;
    }

    private int customHashFunction(Object key) {
        String str = key.toString();
        final int FNV_OFFSET_BASIS = 0x811C9DC5;
        final int FNV_PRIME = 0x01000193;

        int hash = FNV_OFFSET_BASIS;

        for (int i = 0; i < str.length(); i++) {
            hash ^= str.charAt(i);
            hash *= FNV_PRIME;
        }

        return hash;
    }

}
