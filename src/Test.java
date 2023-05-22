import java.math.BigInteger;
import java.util.*;

public class Test <T extends Comparable<T>>{
    private int outerTableSize;
    private String[][] outerTable;
    private int[][] outerHashFunction;
    private int[][][] innerHashFunctions;
    private int[] counter;
    private Random random;
    private boolean collisionDetected;
    public int collisionCount;
    private int rebuildCount;


    public Test(int n){

        this.outerTableSize=nextPrime(n);
//        System.out.println(n);
//        System.out.println("next prime is "+this.outerTableSize);
        random = new Random();
        outerTable=  new String[this.outerTableSize][];
        outerHashFunction=generateHashFunction(this.outerTableSize);
        innerHashFunctions=new int[this.outerTableSize][][];
        counter=new int[outerTableSize];
        Arrays.fill(counter,0);


    }


    public boolean singleInsert(String  key) {

        int outerIndex = calculateIndex(key,outerHashFunction,outerTableSize);
        //case no innerHashTable
        if (outerTable[outerIndex] == null) {
            outerTable[outerIndex] =  new String[1];
            innerHashFunctions[outerIndex]=generateHashFunction(nextPrime(1));
            outerTable[outerIndex][calculateIndex(key,innerHashFunctions[outerIndex],nextPrime(1))]=key;

//            System.out.println(key+" was inserted at ["+outerIndex+"]["+0+"]");
            counter[outerIndex]++;
            return true;

        }
        int innerIndex=calculateIndex(key,innerHashFunctions[outerIndex],outerTable[outerIndex].length);
//        System.out.println(key+" was mapped to ["+outerIndex+"]["+innerIndex+"]");
        if (outerTable[outerIndex][innerIndex]==null) {

            outerTable[outerIndex][innerIndex]=key;
//            System.out.println(key+" was inserted at ["+outerIndex+"]["+innerIndex+"]");
            counter[outerIndex]++;
            return true;
        }
        if (outerTable[outerIndex][innerIndex].equals(key)) {
            return false;
        } else {
            if (outerTable[outerIndex][innerIndex]!=null) {
//                System.out.println(key+" collided with "+outerTable[outerIndex][innerIndex]);
                collisionCount++;
                List<String> elementsToReinsert = new ArrayList<>();
                for (String  bin : outerTable[outerIndex]) {
                    if (bin != null) {
                        elementsToReinsert.add(bin);
                    }
                }
                elementsToReinsert.add(key);
                String[] reinsertArray = toArrayWithElementType(elementsToReinsert);
                outerTable[outerIndex]=buildInnerHashTable(reinsertArray,outerIndex);
            }
            counter[outerIndex]++;
            return true;
        }
    }
//
//    @SuppressWarnings("unchecked")
    private String[] toArrayWithElementType(List<String> list) {
        String[] array =  new String[list.size()];
        return list.toArray(array);
    }

//
//
    public boolean delete(String  key) {
        int outerIndex = calculateIndex(key,outerHashFunction,outerTableSize);
        if(outerTable[outerIndex]==null){System.out.println("inner table is null");return false;}
        int secondIndex=calculateIndex(key,innerHashFunctions[outerIndex],outerTable[outerIndex].length );
        if(outerTable[outerIndex][secondIndex] == null){System.out.println(key +" nothing to delete at [" + outerIndex + "][" + secondIndex + "]");return false;}
        if(!outerTable[outerIndex][secondIndex].equals(key)){System.out.println("cannot delete expected "+key+" found "+outerTable[outerIndex][secondIndex]+" at [" + outerIndex + "][" + secondIndex + "]");return false;}
//        System.out.println( outerTable[outerIndex][secondIndex]+"at [" + outerIndex + "][" + secondIndex + "] deleted");

        outerTable[outerIndex][secondIndex]=null;
        return true;
    }


    public void batchInsert(String [] keys) {
        ArrayList<String>[] mapped=new ArrayList[outerTableSize];
        for (int i = 0; i <outerTableSize ; i++) {
            mapped[i]=new ArrayList<>();
        }
        for (String  key:keys) {
            int outerIndex = calculateIndex(key, outerHashFunction, outerTableSize);
            if(outerTable[outerIndex]!=null && Arrays.asList(outerTable[outerIndex]).contains(key)){
//                System.out.println(key+" repeated");

                continue;}
            mapped[outerIndex].add(key);
        }
        for (int i = 0; i <outerTableSize ; i++) {

            if(mapped[i].isEmpty()){continue;}
            if(counter[i]==0){
//                System.out.println("counter=0______________________________________________________");
//                System.out.println("mapped Size= "+mapped[i].size());

                outerTable[i]=buildInnerHashTable(mapped[i].toArray(new String[mapped[i].size()]),i);
                counter[i]=counter[i]+mapped[i].size();
            }

            else {
                for (Iterator<String> iterator = mapped[i].iterator(); iterator.hasNext();) {
                    String key = iterator.next();
                    int innerIndex = calculateIndex(key, innerHashFunctions[i], outerTable[i].length);

                    if (outerTable[i][innerIndex] == null) {
                        outerTable[i][innerIndex] = key;
//                        System.out.println(key + " was inserted at  [" + i + "][" + innerIndex + "] in new Inner Table");

                        counter[i]++;
                        iterator.remove(); // Use the iterator's remove() method instead of mapped[i].remove(key)
                    }

                    else {
                        collisionCount++;
//                        System.out.println(key+" collided with "+outerTable[i][innerIndex]);
                        outerTable[i] = buildInnerHashTable(merge(mapped[i], outerTable[i]), i);
                        counter[i] =counter[i] + mapped[i].size();
                        break;
                    }
                }



            }

        }





    }

//
//
    public void batchDelete(String [] keys) {
        for (String  key : keys) {
            delete(key);
        }

    }
//
    private String[] merge(ArrayList<String> newKeys,String[] innerTable){
        for (String  key :innerTable){
            if(key!=null){
                newKeys.add(key);
            }
        }
//        System.out.println("Merge Size=_____________________________________________________________ "+newKeys.size());
        return  newKeys.toArray(new String[newKeys.size()]);

    }
    private String [] buildInnerHashTable(String [] keys,int outerTableIndex) {
//        System.out.println("Rebuilding Inner Table at "+outerTableIndex);
//        System.out.println(" with keys : "+Arrays.toString(keys));
//        System.out.println(" New Size= "+nextPrime(power2of(keys.length)));
            String[] innerTable =  new String[nextPrime(power2of(keys.length))];
            int count=0;
            do {
                count++;
                Arrays.fill(innerTable, null);
                collisionDetected = false;
                innerHashFunctions[outerTableIndex] = generateHashFunction(nextPrime(power2of(keys.length)));

                for (String key : keys) {
                    int index = calculateIndex(key, innerHashFunctions[outerTableIndex], nextPrime(power2of(keys.length)));
//                    System.out.println(key + " was mapped to  [" + outerTableIndex + "][" + index + "] in new Inner Table");
                    if (innerTable[index] == null) {
                        innerTable[index] = key;
//                        System.out.println(key + " was inserted at  [" + outerTableIndex + "][" + index + "] in new Inner Table");
                    }

                    else if (innerTable[index] != null) {
                        collisionDetected = true;
                        collisionCount++;
//                        System.out.println("Collision while building inner table");
//                        System.out.println(key + " collided with " + innerTable[index]);
                        break;
                    }


                }

                if (collisionDetected) {

                    rebuildCount++;
//                innerTable = (String []) new Object[power2of(keys.length)]; // Clear the table if collisions occurred
                }
            }
            while (collisionDetected);
//            System.out.println("While loop repeated "+count+" times");
        return innerTable;
    }
//
    private int[][] generateHashFunction(int size) {
        int [][]hashFunction = new int[log2(size * size)][64];
        for (int i = 0; i < log2(size * size); i++) {
            for (int j = 0; j < 64; j++) {
                hashFunction[i][j] = random.nextInt(2);
            }
        }
        return hashFunction;
    }
//
    private int calculateIndex(String  key,int[][] hashFunction,int size) {
        int index = 0;
        for (int i = 0; i < log2(size * size); i++) {
            int[] hash = hashFunction[i];
            int h = dotProduct(hash, key);
            index = (index << 1) | h;
        }
        return index % size;
    }



    private int dotProduct(int[] hash,String key) {
        int result = 0;
        int[] keyBits = toBinaryArray(key.hashCode());
        for (int i = 0; i < 64; i++) {
            result += hash[i] * keyBits[i];
        }
        return result % 2;
    }
//
    private int[] toBinaryArray(int n) {
        int[] binaryArray = new int[64];
        for (int i = 63; i >= 0; i--) {
            binaryArray[i] = n & 1;
            n >>= 1;
        }
        return binaryArray;
    }
//
    private int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    private int power2of(int i){
        return (int) Math.pow(i,2);

    }
//
//
    public boolean search(String  key) {
        int outerIndex = calculateIndex(key,outerHashFunction,outerTableSize);
        if(outerTable[outerIndex]==null){return false;}
        int secondIndex=calculateIndex(key,innerHashFunctions[outerIndex],outerTable[outerIndex].length );
        return outerTable[outerIndex][secondIndex] != null && outerTable[outerIndex][secondIndex].equals(key);
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

public void print() {
        int size=0;
//    System.out.println("HASH TABLE");
//    System.out.println("_______________________________________________________________________");
    for (int i = 0; i < outerTable.length; i++) {
//        System.out.print(i + "=>");
        if (outerTable[i] == null) {
//            System.out.print(" Null");
//            System.out.println();
            continue;
        }
        for (int j = 0; j < outerTable[i].length; j++) {
            if (outerTable[i][j] == null) {
//                System.out.print(" [" + j + "] X    ");
            } else {
//                System.out.print(" [" + j + "] " + outerTable[i][j] + "    ");
                size++;
            }
        }
//        System.out.println();
    }
    System.out.println("Size ="+size);
    System.out.println("_______________________________________________________________________");
}
public int nextPrime(int number){
    BigInteger bigInt = BigInteger.valueOf(number);
    BigInteger nextPrime = bigInt.nextProbablePrime();
    return  nextPrime.intValue();

}
}
