import java.util.Arrays;
import java.util.Random;

public class PerfectHashing<T> implements HashTable<T> {

    private Object[][] table;
    private int n;
    private int[] count;
    private Random random;
    private int[][][] hashFunctions;
    private int[][] outerHashFunction;
    private int collisionCount;
    public PerfectHashing(int N) {
        random = new Random();
        this.n = N;
        buildHashTable();
    }

    public void buildHashTable() {
        hashFunctions = new int[n][][];
        table = new Object[n][];
        count = new int[n];
        Arrays.fill(count, 0);
        for (int i = 0; i < n; i++) {
            table[i] = null;
            hashFunctions[i] = null;
        }
        outerHashFunction = generateHashFunction(n);
    }

    public T[] reHashInsert(T[] level2, T collidedKey) {
        int firstIndex = calculateIndex(outerHashFunction, collidedKey, n);
        boolean hashed = false;
        T[] newLevel2 = (T[]) new Object[(int) Math.pow(count[firstIndex], 2)];
        while (!hashed) {
            hashed = true;
            hashFunctions[firstIndex] = generateHashFunction((int) Math.pow(count[firstIndex], 2));
            newLevel2 = (T[]) new Object[(int) Math.pow(count[firstIndex], 2)];
            for (int i = 0; i < level2.length; i++) {
                if (level2[i] != null) {
                    int secondIndex = calculateIndex(hashFunctions[firstIndex], level2[i],
                            (int) Math.pow(count[firstIndex], 2));
                    if (newLevel2[secondIndex] == null) {
                        newLevel2[secondIndex] = level2[i];
                    } else {
                        hashed = false;
                        break;
                    }
                }
            }
            int secondIndex = calculateIndex(hashFunctions[firstIndex], collidedKey,
                    (int) Math.pow(count[firstIndex], 2));
            if (newLevel2[secondIndex] == null) {
                newLevel2[secondIndex] = collidedKey;
            } else {
                hashed = false;
            }
        }
        return newLevel2;
    }

    public boolean search(T key) {
        int firstIndex = calculateIndex(outerHashFunction, key, n);
        if (table[firstIndex] == null)
            return false;
        int secondIndex = calculateIndex(hashFunctions[firstIndex], key, table[firstIndex].length);
        return table[firstIndex][secondIndex] != null && table[firstIndex][secondIndex].equals(key);
    }

    public int[][] generateHashFunction(int size) {
        int[][] hashFunction = new int[log2(size)][];
        for (int i = 0; i < log2(size); i++) {
            hashFunction[i] = new int[32]; // Assuming 32-bit integers
            for (int j = 0; j < 32; j++) {
                hashFunction[i][j] = random.nextInt(2);
            }
        }
        return hashFunction;
    }

    public int calculateIndex(int[][] hashFunction, T key, int size) {
        int index = 0;
        for (int i = 0; i < log2(size); i++) {
            int[] hash = hashFunction[i];
            int h = 0;
            for (int j = 0; j < 32; j++) {
                if (hash[j] == 1) {
                    h ^= (key.hashCode() >> j) & 1;
                }
            }
            index = (index << 1) | h;
        }
        return index % size;
    }

    private int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    public boolean insert(T key) {
        if (search(key))
            return false;
        else {
            int firstIndex = calculateIndex(outerHashFunction, key, n);
            int secondIndex;
            if (count[firstIndex] == 0) {
                table[firstIndex] = (T[]) new Object[1];
                hashFunctions[firstIndex] = generateHashFunction(1);
                secondIndex = calculateIndex(hashFunctions[firstIndex], key, 1);
            } else {
                secondIndex = calculateIndex(hashFunctions[firstIndex], key, table[firstIndex].length);
            }
            if (table[firstIndex][secondIndex] == null) {
                table[firstIndex][secondIndex] = key;
                count[firstIndex]++;
            } else {
                count[firstIndex]++;
                table[firstIndex] = reHashInsert((T[]) table[firstIndex], key);
                collisionCount++;
            }
            return true;
        }
    }

    public boolean delete(T key) {
        int firstIndex = calculateIndex(outerHashFunction, key, n);
        if (table[firstIndex] == null)
            return false;
        int secondIndex = calculateIndex(hashFunctions[firstIndex], key, table[firstIndex].length);
        if (table[firstIndex][secondIndex] == null) {
            return false;
        } else {
            if (!table[firstIndex][secondIndex].equals(key))
                return false;
            table[firstIndex][secondIndex] = null;
            return true;
        }
    }

    public void batchInsert(T[] keys) {
        for (int i = 0; i < keys.length; i++) {
            insert(keys[i]);
        }
    }

    public void batchDelete(T[] keys) {
        for (int i = 0; i < keys.length; i++) {
            delete(keys[i]);
        }
    }

    public void print() {
        System.out.println("HASH TABLE");
        System.out.println("_______________________________________________________________________");
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + "=>");
            if (table[i] == null) {
                System.out.print(" Null");
                System.out.println();
                continue;
            }
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == null) {
                    System.out.print(" [" + j + "] X    ");
                } else {
                    System.out.print(" [" + j + "] " + table[i][j] + "    ");
                }
            }
            System.out.println();
        }
        System.out.println("_______________________________________________________________________");
    }
}
