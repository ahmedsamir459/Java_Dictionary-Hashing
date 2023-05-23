import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private HashTable<String> dictionary;

    public Dictionary(String backendType, int size) {
        switch (backendType) {
            case "O(N^2)":
                dictionary = new UniversalHashing<>(size);
                break;
            case "O(N)":
                dictionary = new PerfectHashing<>(size);
                break;
            default:
                throw new IllegalArgumentException("Invalid backend perfect hashing type: " + backendType);
        }
    }

    public void insert(String toInsert) {
        if (!dictionary.insert(toInsert)) {
            System.out.print("(" + toInsert + ")" + "\u001B[31mAlready EXIST\n\u001B[0m");
        } else {
            System.out.print("(" + toInsert + ")" + "\u001B[32m Succefully inserted ✅\n\u001B[0m");
        }
    }

    public void delete(String toDelete) {
        if (!dictionary.delete(toDelete)) {
            System.out.print("(" + toDelete + ")" + "\u001B[31m NOT FOUND ❌\n\u001B[0m");
        } else {
            System.out.print("(" + toDelete + ")" + "\u001B[32m Succefully DELETED ✅\n\u001B[0m");
        }
    }

    public boolean search(String toSearch) {
        if (dictionary.search(toSearch)) {
            System.out.println("(" + toSearch + ")" + "\u001B[32m FOUND\u001B[0m ✅");
        } else {
            System.out.println("(" + toSearch + ")" + "\u001B[31m NOT FOUND\u001B[0m ❌");
        }
        return dictionary.search(toSearch);
    }

    public void batchInsert(String filePath) {
        int Inserted = 0;
        int notInserted = 0;
        List<String> keys = readKeysFromFile(filePath);
        if (keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            if (!dictionary.search(key)){
                Inserted++;
                dictionary.insert(key);
            }
            else {
                notInserted++;
            }
        }
        System.out.print("(" + Inserted + ")" + "\u001B[32m word(s) SUCCEFULLY INSERTED ✅\n\u001B[0m");
        if (notInserted != 0) {
            System.out.print("(" + notInserted + ")" + "\u001B[31m word(s) ALREADY EXIST \n\u001B[0m");
        }
        System.out.println("Collision Count: " + dictionary.getCollisionCount());
    }

    public void batchDelete(String filePath) {
        int deleted = 0;
        int notDeleted = 0;
        List<String> keys = readKeysFromFile(filePath);
        if (keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            if (!dictionary.search(key))
                notDeleted++;
            else {
                deleted++;
                dictionary.delete(key);
            }
        }
        System.out.print("(" + deleted + ")" + "\u001B[32m word(s) SUCCEFULLY DELETED ✅\n\u001B[0m");
        if (notDeleted != 0) {
            System.out.print("(" + notDeleted + ")" + "\u001B[31m word(s) NOT FOUND \n\u001B[0m");
        }
    }

    private List<String> readKeysFromFile(String filePath) {
        List<String> keys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("testcases/"+filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keys.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("\u001B[31mAn ERROR occurred opening file\u001B[0m ");
        }
        return keys;
    }
}
