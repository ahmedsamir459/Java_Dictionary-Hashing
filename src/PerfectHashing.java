import java.util.*;

public class PerfectHashing <T extends Comparable<T>> implements HashTable<T>{
    private int outerTableSize;
    private int totalElementsSize;
    List<T>[][] outerTable;

    private int[][] outerHashFunction;

    private int[][][] innerHashFunctions;

    private int[] counter;
    private Random random;
    private boolean collisionDetected;
    private int collisionCount;
    private int rebuildCount;

    public PerfectHashing (int outerTableSize) {
        this.outerTableSize = outerTableSize;
        random = new Random();
        // Create the hash table
        outerTable = new List[outerTableSize][];
        outerHashFunction=generateHashFunction(outerTableSize);
        innerHashFunctions=new int[outerTableSize][][];
        counter=new int[outerTableSize];
        Arrays.fill(counter,0);
    }

    @Override
    public boolean insert(T key) {
        int index1 = calculateIndex(key,outerHashFunction, outerTableSize);
        // no Inner Table
        if (outerTable[index1] == null) {
            outerTable[index1] = new List[1];
            innerHashFunctions[index1]=generateHashFunction(1);
            int index2=calculateIndex(key,innerHashFunctions[index1],1);
            outerTable[index1][index2]=new ArrayList<>();
            outerTable[index1][index2].add(key);
            counter[index1]++;
            totalElementsSize++;
            return true;
        }
        int index2=calculateIndex(key,innerHashFunctions[index1],outerTable[index1].length);
        if(outerTable[index1][index2]==null||outerTable[index1][index2].isEmpty()){
            outerTable[index1][index2]=new ArrayList<>();
            outerTable[index1][index2].add(key);
            totalElementsSize++;
            return true;
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
                outerTable[index1]=buildHashTable(reinsertArray,index1);
            }
            counter[index1]++;
            totalElementsSize++;
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
        if(search(key)){
            int index1=calculateIndex(key,outerHashFunction, outerTableSize);
            int index2=calculateIndex(key,innerHashFunctions[index1],outerTable[index1].length );
            outerTable[index1][index2].remove(key);
            totalElementsSize--;
            return true;
        }
        return false;
    }

    @Override
    public void batchInsert(T[] keys) {
        for (T key : keys) {
            int index1 = calculateIndex(key, outerHashFunction, outerTableSize);
            counter[index1]++;
            totalElementsSize++;
        }
        for (T key : keys) {
            int i=calculateIndex(key, outerHashFunction, outerTableSize);
            if (outerTable[i] == null) { //No inner Table
                outerTable[i]=new List[(int) Math.pow(counter[i],2)];
                innerHashFunctions[i]=generateHashFunction((int) Math.pow(counter[i],2));
                int index2 = calculateIndex(key, innerHashFunctions[i], outerTable[i].length);
                outerTable[i][index2]=new ArrayList<>();
                outerTable[i][index2].add(key);
            } else {
                int index2 = calculateIndex(key, innerHashFunctions[i], outerTable[i].length);
                if (outerTable[i][index2] == null) {
                    outerTable[i][index2]=new ArrayList<>();
                    outerTable[i][index2].add(key);
                }
                else if (outerTable[i][index2].contains(key)) {
                    continue;
                }
                else if (outerTable[i][index2].isEmpty()){
                    outerTable[i][index2].add(key);
                }
                else {
                    outerTable[i][index2].add(key);
                    collisionCount++;
                    rebuildCount++;
                    List<T> elementsToReinsert = new ArrayList<>();
                    for (List<T> bin : outerTable[i]) {
                        if (bin != null) {
                            elementsToReinsert.addAll(bin);
                        }
                    }
                    Arrays.fill(outerTable[i], null);
                    T[] reinsertArray = toArrayWithElementType(elementsToReinsert);
                    outerTable[i]=buildHashTable(reinsertArray,i);
                    break;
                }

            }
        }


    }


    public T[] merge(ArrayList<T> newKeys,List<T>[] oldKeys){

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
        int size=keys.length* keys.length;
//        System.out.println("Keys Size= "+size+" Keys are "+Arrays.toString(keys));
        List<T>[] table=new List[size];
        int count=-1;
        do {
            count++;
//            System.out.println("In loop for the "+(count+1)+" time");
            Arrays.fill(table,null);
            collisionDetected = false;
            innerHashFunctions[outerIndex]=generateHashFunction(keys.length* keys.length);
            for (T key : keys) {
                int index = calculateIndex(key,innerHashFunctions[outerIndex],size);
                if (table[index] != null && !table[index].isEmpty()) {

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


    private int calculateIndex(T  key,int[][] hashFunction,int size) {
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
        int index1 = calculateIndex(key,outerHashFunction, outerTableSize);
        if(outerTable[index1]==null){return false;}
        int index2=calculateIndex(key,innerHashFunctions[index1],outerTable[index1].length);
        if (outerTable[index1][index2] != null) {
            return outerTable[index1][index2].contains(key);
        }
        return false;
    }

    public void print() {
        int count=0;
        for (int i = 0; i < outerTableSize; i++) {
            System.out.print(i + "=>");
            if (outerTable[i] == null) {
            System.out.print(" Null");
            System.out.println();
                continue;
            }
            for (int j = 0; j <outerTable[i].length ; j++) {
                if (outerTable[i][j] == null || outerTable[i][j].isEmpty()) {
                System.out.print(" [" + j + "] X    ");
                } else {
                System.out.print(" [" + j + "] " + outerTable[i][j] + "    ");
                    count++;
                }
            }
            System.out.println();
        }
        System.out.println("Size ="+count);
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
    public  int customHashFunction(Object key) {
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

    public int getAllocatedSize(){
        int allocatedSize=0;
        for (int i = 0; i < outerTableSize; i++) {
            if(outerTable[i]==null){continue;}
//            allocatedSize= allocatedSize+outerTable[i].length;
            allocatedSize= (int) (allocatedSize+Math.pow(counter[i],2));
        }
        allocatedSize=allocatedSize+ outerTableSize;
        return allocatedSize;
    }
    public int getActualSize(){

        return totalElementsSize;

    }
}