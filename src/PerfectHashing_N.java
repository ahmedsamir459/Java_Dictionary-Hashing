import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
public class PerfectHashing_N {

    private String[][] Table;
    int n;
    int count[];
    private Random random;
    int[][][] hash_functions;
    int [][] outer_hash_function;
    public PerfectHashing_N(int N)
    { random=new Random();
        this.n=N;
        Build_Hash_Table();
    }
    public void Build_Hash_Table()
    {
        hash_functions=new int[n][][];
        Table=new String[n][];
        count = new int[n];
        Arrays.fill(count,0);
        for(int i=0;i<n;i++)
        {
            Table[i]=null;
            hash_functions[i]=null;
        }
        outer_hash_function=generateHashFunction(n);
    }
    public String[] ReHash_Insert(String[] level2,String collidedKey){
        int first_index=calculateIndex(outer_hash_function,collidedKey, n);
        Boolean hashed=false;
        String[] new_level2=new String[(int)Math.pow(count[first_index],2)];
        while (!hashed) {
            hashed=true;
            hash_functions[first_index] = generateHashFunction((int)Math.pow(count[first_index],2));
            new_level2 = new String[(int)Math.pow(count[first_index],2)];
            for (int i = 0; i < level2.length; i++) {
                if (level2[i] != null) {
                    int second_index = calculateIndex(hash_functions[first_index], level2[i], (int)Math.pow(count[first_index],2));
                    if(new_level2[second_index]==null || new_level2[second_index].equals("")) {
                        new_level2[second_index] = level2[i];
                    }
                    else {
                        hashed=false;
                        break;
                    }
                }

            }
            int second_index = calculateIndex(hash_functions[first_index], collidedKey, (int)Math.pow(count[first_index],2));
            if(new_level2[second_index]==null || new_level2[second_index].equals("")) {
                new_level2[second_index] = collidedKey;
                System.out.println(collidedKey+" was inserted at after collision ["+first_index+"]"+" ["+second_index+"]");
            }
            else {
                hashed=false;

            }
        }
        return new_level2;
    }
    public Boolean Search(String key)
    {
        int first_index=calculateIndex(outer_hash_function,key, n);
        if(Table[first_index]==null) return false;
        int second_index=calculateIndex(hash_functions[first_index],key,Table[first_index].length);
        System.out.println(key+" was searched at ["+first_index+"]"+" ["+second_index+"]");
        if(Table[first_index][second_index]!=null && Table[first_index][second_index].equals(key))
        {
            return true;
        }
        else return false;
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

    public int calculateIndex(int[][] hashFunction, String key,int size) {
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
        return index%(size);
    }
    private int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }
    public Boolean Insert(String key)
    {   if(Search(key))return false;
        else {
        int first_index = calculateIndex(outer_hash_function, key, n);
        int second_index;
        if (count[first_index] == 0) {
            Table[first_index] = new String[1];
            hash_functions[first_index] = generateHashFunction(1);
            second_index = calculateIndex(hash_functions[first_index], key, 1);
        } else {
            System.out.println((int) Math.pow(count[first_index], 2));
            second_index = calculateIndex(hash_functions[first_index], key, Table[first_index].length);
        }
        System.out.println(key + " was mapped to [" + first_index + "]" + " [" + second_index + "]");
        if (Table[first_index][second_index] == null) {
            Table[first_index][second_index] = key;
            count[first_index]++;
            System.out.println(key + " was inserted at [" + first_index + "]" + " [" + second_index + "]");

        } else {
            count[first_index]++;
            Table[first_index] = ReHash_Insert(Table[first_index], key);
        }
        return true;
    }
    }
    public Boolean Delete(String key)
    {
        int first_index=calculateIndex(outer_hash_function,key,n);
        if(Table[first_index]==null) return false;
        int second_index=calculateIndex(hash_functions[first_index],key,Table[first_index].length);
        if(Table[first_index][second_index]==null)
        {
            return false;
        }
        else
        {  if(Table[first_index][second_index]!=key)return false;
            Table[first_index][second_index]=null;
            return true;
        }
    }
    public void Batch_insert(String[] keys)
    {
        for(int i=0;i< keys.length;i++)
        {
            Insert(keys[i]);
        }
    }
    public void Batch_delete(String [] keys)
    {
        for(int i=0;i< keys.length;i++)
        {
            Delete(keys[i]);
        }
    }
    public  void print(){
        System.out.println("HASH TABLE ");
        System.out.println("_______________________________________________________________________");
        for (int i = 0; i <Table.length ; i++) {
            System.out.print(i+"=>");
            if(Table[i]==null){System.out.print(" Null");System.out.println(); continue;}
            for (int j = 0; j <Table[i].length ; j++) {
                if(Table[i][j]==null){System.out.print(" ["+j+"] "+" X "+"    ");}
                else System.out.print(" ["+j+"] "+Table[i][j]+"    ");
            }
            System.out.println();

        }
        System.out.println("_______________________________________________________________________");
    }
}
