import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniversalHashing <T extends Comparable<T>> {
    private int size;
    private List<T>[] table;
    private int [][] hashFunction;
    private Random random;

    public UniversalHashing(T[] keys) {
        size = keys.length;
        random = new Random();
        // Create the hash table
        table = new List[size * size];
        for (int i = 0; i < size * size; i++) {
            table[i] = new ArrayList<>();
        }
        buildHashTable(keys);
    }

    private void buildHashTable(T[] keys) {
        boolean collisions;
        do {
            collisions = false;

            // Generate a random hash function
            hashFunction = generateHashFunction();

            // Hash the keys into the table
            for (T key : keys) {
                int index = calculateIndex(hashFunction, key);
                if (table[index].size() > 0) {
                    collisions = true;
                    System.out.println("Collision detected!");
                    break;
                }
                table[index].add(key);
            }

            // Clear the table if collisions occurred
            if (collisions) {
                for (List<T> bin : table) {
                    bin.clear();
                }
            }
        } while (collisions);
    }

    private int[][] generateHashFunction() {
        int[][] hashFunction = new int[log2(size*size)][];
        for (int i = 0; i < log2(size*size); i++) {
            hashFunction[i] = new int[32]; // Assuming 32-bit integers
            for (int j = 0; j < 32; j++) {
                hashFunction[i][j] = random.nextInt(2);
            }
        }
        return hashFunction;
    }

    private int calculateIndex(int[][] hashFunction, T key) {
        int index = 0;
        for (int i = 0; i < log2(size*size); i++) {
            int[] hash = hashFunction[i];
            int h = 0;
            for (int j = 0; j < 32; j++) {
                if (hash[j] == 1) {
                    h ^= (key.hashCode() >> j) & 1;
                }
            }
            index = (index << 1) | h;
        }
        return index%(size*size);
    }

    public boolean contains(T key) {
        int index = calculateIndex(hashFunction, key);
        return table[index].contains(key);
    }
    public  void print(){
        for (int i = 0; i < size*size; i++) {
            System.out.println(table[i]);
        }
    }
    private int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

}