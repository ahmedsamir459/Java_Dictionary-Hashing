

public class HashTableSpaceComplexityTest {
    public  long calculateSpaceComplexity(UniversalHashing<String> hashTable) {
        long tableSize = calculateArraySize(hashTable.table);
        long totalSpace = tableSize/8 ;
        return totalSpace;
    }

    public  long calculateSpaceComplexity(PerfectHashing<String> hashTable) {
        long totalSpace = 0;
        int sum=0;
        for (Object o : hashTable.outerTable) {
            if (o != null) {
                long i=calculateArraySize(o)/8;
                totalSpace += i;
            }
        }
        totalSpace += hashTable.outerTable.length;
        return totalSpace;
    }

    public  long calculateArraySize(Object array) {
        if (array == null) {
            return 0;
        }

        Class<?> arrayClass = array.getClass();
        if (!arrayClass.isArray()) {
            return 0;
        }

        int length = java.lang.reflect.Array.getLength(array);
        long elementSize = calculateElementSize(arrayClass.getComponentType());
        return elementSize * length;
    }

    public  long calculateElementSize(Class<?> elementClass) {
        if (elementClass == null) {
            return 0;
        }
        if (elementClass.isArray()) {
            return calculateArraySize(elementClass);
        }
        long referenceSize = 8;
        if (elementClass == boolean.class || elementClass == byte.class) {
            return 1;
        } else if (elementClass == char.class || elementClass == short.class) {
            return 2;
        } else if (elementClass == int.class || elementClass == float.class) {
            return 4;
        } else if (elementClass == long.class || elementClass == double.class) {
            return 8;
        } else {
            return referenceSize;
        }
    }
}
