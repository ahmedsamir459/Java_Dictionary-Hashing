import java.util.*;

public class PerfectHashing<T> implements HashTable<T> {

    private int size;
    List<T>[][] outerTable;

    int[][] outerHashFunction;

    int[][][] innerHashFunctions;

    int[] counter;
    private Random random;
    private boolean collisionDetected;
    private int collisionCount;
    private int rebuildCount;

    public PerfectHashing(int size) {
        this.size = size;
        random = new Random();
        // Create the hash table
        outerTable = new List[size][];
        outerHashFunction = generateHashFunction(size);
        innerHashFunctions = new int[size][][];
        counter = new int[size];
        Arrays.fill(counter, 0);
    }

    @Override
    public boolean insert(T key) {
        int index1 = calculateIndex(key, outerHashFunction, size);
        // no Inner Table
        if (outerTable[index1] == null) {
            outerTable[index1] = new List[(int) Math.ceil(0.001*size)];
            innerHashFunctions[index1] = generateHashFunction((int) Math.ceil(0.001*size));
            int index2 = calculateIndex(key, innerHashFunctions[index1], (int) Math.ceil(0.001*size));
            outerTable[index1][index2] = new ArrayList<>();
            outerTable[index1][index2].add(key);
            counter[index1]++;
            return true;
        }
        int index2 = calculateIndex(key, innerHashFunctions[index1], outerTable[index1].length);
        if (outerTable[index1][index2] == null || outerTable[index1][index2].isEmpty()) {
            outerTable[index1][index2] = new ArrayList<>();
            outerTable[index1][index2].add(key);
        }
        if (outerTable[index1][index2].contains(key)) {
            return false;
        } else {
            outerTable[index1][index2].add(key);
            if (outerTable[index1][index2].size() > 1) {
                collisionCount++;
                List<T> elementsToReinsert = new ArrayList<>();
                for (List<T> bin : outerTable[index1]) {
                    if (bin != null) {
                        elementsToReinsert.addAll(bin);
                    }
                }
                Arrays.fill(outerTable[index1], null);
                T[] reinsertArray = toArrayWithElementType(elementsToReinsert);
                outerTable[index1] = buildHashTable(reinsertArray, index1);
            }
            counter[index1]++;
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private T[] toArrayWithElementType(List<T> list) {
        T[] array = (T[]) new Comparable[list.size()];
//        System.out.println("Array converted");
        return list.toArray(array);
    }


    @Override
    public boolean delete(T key) {
        if (search(key)) {
            int index1 = calculateIndex(key, outerHashFunction, size);
            int index2 = calculateIndex(key, innerHashFunctions[index1], outerTable[index1].length);
            outerTable[index1][index2].remove(key);
            return true;
        }
        return false;
    }

    @Override
    public void batchInsert(T[] keys) {
        for (T key : keys) {
            insert(key);
        }
//        ArrayList<T>[] mapped = new ArrayList[size];
//        for (int i = 0; i < size; i++) {
//            mapped[i] = new ArrayList<>();
//        }
//        for (T key : keys) {
//            int index1 = calculateIndex(key, outerHashFunction, size);
////            if (!mapped[index1].contains(key)) {
//            mapped[index1].add(key);
////            }
//        }
//        for (int i = 0; i < size; i++) {
//            if (mapped[i].isEmpty()) {
//                continue;
//            }
//            if (counter[i] == 0) {
//                outerTable[i] = buildHashTable(toArrayWithElementType(mapped[i]), i);
//                counter[i] = counter[i] + mapped[i].size();
//            } else {
//
//                for (Iterator<T> iterator = mapped[i].iterator(); iterator.hasNext(); ) {
//                    T key = iterator.next();
//                    int index2 = calculateIndex(key, innerHashFunctions[i], outerTable[i].length);
//                    if (outerTable[i][index2] == null) {
//                        outerTable[i][index2] = new ArrayList<>();
//                        outerTable[i][index2].add(key);
//                        counter[i]++;
//                        iterator.remove(); // Use the iterator's remove() method instead of mapped[i].remove(key)
//                    } else if (outerTable[i][index2].isEmpty()) {
//                        outerTable[i][index2].add(key);
//                        counter[i]++;
//                        iterator.remove();
//                    } else {
////                        System.out.println("Collision Detected need to rebuild");
//                        collisionCount++;
////                        System.out.println(key+" collided with "+outerTable[i][innerIndex]);
//                        rebuildCount++;
//                        outerTable[i] = buildHashTable(merge(mapped[i], outerTable[i]), i);
//                        counter[i] = counter[i] + mapped[i].size();
//                        break;
//                    }
//
//                }
//            }
//
//
//        }
    }


    public T[] merge(ArrayList<T> newKeys, List<T>[] oldKeys) {

        for (List<T> bin : oldKeys) {
            if (bin != null) {
                newKeys.addAll(bin);
            }
        }
        return toArrayWithElementType(newKeys);

    }

    @Override
    public void batchDelete(T[] keys) {
        for (T key : keys) {
            delete(key);
        }
    }

    private List<T>[] buildHashTable(T[] keys, int outerIndex) {
        int size = keys.length * keys.length;
//        System.out.println("Keys Size= "+size+" Keys are "+Arrays.toString(keys));
        List<T>[] table = new List[size];
        int count = -1;
        do {
            count++;
//            System.out.println("In loop for the "+(count+1)+" time");
            Arrays.fill(table, null);
            collisionDetected = false;
            innerHashFunctions[outerIndex] = generateHashFunction(keys.length * keys.length);
            for (T key : keys) {
                int index = calculateIndex(key, innerHashFunctions[outerIndex], size);
//                System.out.println(key+" mapped to ["+index+"]");
                if (table[index] != null && !table[index].isEmpty()) {
//                    System.out.println(key+" collided with "+table[index] +" at ["+index+"] ");
//                    System.out.println("Keys Size= "+size+" Keys are "+Arrays.toString(keys));
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
//                table = new List[size]; // Clear the table if collisions occurred
            }
        } while (collisionDetected);
//        System.out.println("While loop executed "+count+ " times");
        return table;
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


    private int calculateIndex(T key, int[][] hashFunction, int size) {
        int index = 0;
        for (int i = 0; i < log2(size); i++) {
            int[] hash = hashFunction[i];
            int h = 0;
            for (int j = 0; j < 32; j++) {
                if (hash[j] == 1) {
                    h ^= (customHashFunction(key) >> j) & 1;
                }
            }
            index = (index << 1) | h;
        }
        return index % size;
    }

    private int dotProduct(int[] hash, T key) {
        int result = 0;
        int[] keyBits = toBinaryArray(customHashFunction(key));
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
        int index1 = calculateIndex(key, outerHashFunction, size);
        if (outerTable[index1] == null) {
            return false;
        }
        int index2 = calculateIndex(key, innerHashFunctions[index1], outerTable[index1].length);
        if (outerTable[index1][index2] != null) {
            return outerTable[index1][index2].contains(key);
        }
        return false;
    }

    public void print() {
        int count = 0;
        for (int i = 0; i < size; i++) {
//            System.out.print(i + "=>");
            if (outerTable[i] == null) {
//            System.out.print(" Null");
//            System.out.println();
                continue;
            }
            for (int j = 0; j < outerTable[i].length; j++) {
                if (outerTable[i][j] == null || outerTable[i][j].isEmpty()) {
//                System.out.print(" [" + j + "] X    ");
                } else {
//                System.out.print(" [" + j + "] " + outerTable[i][j] + "    ");
                    count++;
                }
            }
//            System.out.println();
        }
        System.out.println("Size =" + count);
        System.out.println("_______________________________________________________________________");
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
