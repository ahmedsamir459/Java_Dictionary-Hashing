import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\u001B[40mEnter the type of the backend tree of the dictionary  : " + "\u001B[0m");
        System.out.print("\u001B[33m1)'O(N)'\n2)'O(N^2)'\u001B[0m\nAnswer >> ");
        String D_type;
        Dictionary d;
        D_type = scanner.next();
        while (true) {

            try {
                if (Integer.parseInt(D_type) == 1 || Integer.parseInt(D_type) == 2) {
                    if (Integer.parseInt(D_type) == 1) {
                        D_type = "O(N)";
                    } else {
                        D_type = "O(N^2)";
                    }
                    break;
                } else {
                    System.out.print("\u001B[31mError,Please choose a right answer\n\u001B[0mAnswer >> ");
                    D_type = scanner.next();
                }

            } catch (Exception e) {
                System.out.print("\u001B[31mError,Please choose a right answer\n\u001B[0mAnswer >> ");
                D_type = scanner.next();
            }
        }
        String backendType = scanner.nextLine();
        System.out.println("\u001B[40mEnter the size of the dictionary: " + "\u001B[0m");
        int size = scanner.nextInt();

        d = new Dictionary(D_type, size);

        options(d);
    }

    private static void options(Dictionary d) {

        while (true) {
            System.out.println("-------------------------------------------------------------" + "\n\u001B[40mDictionary options : \u001B[0m");
            System.out.println("\u001B[33m1)Insert word\u001B[0m");
            System.out.println("\u001B[33m2)Insert batch\u001B[0m");
            System.out.println("\u001B[33m3)Delete word\u001B[0m");
            System.out.println("\u001B[33m4)Delete batch\u001B[0m");
            System.out.println("\u001B[33m5)Search word\u001B[0m");
            System.out.print("Choose option>> ");
            Scanner scanner = new Scanner(System.in);
            try {
                int option = scanner.nextInt();

                //Bounded options
                if (option <= 0 || option > 8) {
                    System.out.print("\u001B[31mError,Please choose a right option\n\u001B[0m");
                    options(d);
                }

                //INSERT WORD
                else if (option == 1) {
                    System.out.print("Enter the word to insert >> ");
                    String toInsert = scanner.next();
                    d.insert(toInsert);
                }

                //BATCH INSERT
                else if (option == 2) {
                    System.out.print("Enter the path of the file to insert >> ");
                    String fileToInsert = scanner.next();
                    d.batchInsert(fileToInsert);
                }

                //DELETE WORD
                else if (option == 3) {
                    System.out.print("Enter the word to delete >> ");
                    String toDelete = scanner.next();
                    d.delete(toDelete);
                }

                //BATCH DELETE
                else if (option == 4) {
                    System.out.print("Enter the path of the file to delete >> ");
                    String fileToDelete = scanner.next();
                    d.batchDelete(fileToDelete);
                }

                //SEARCH
                else if (option == 5) {
                    System.out.print("Enter the word to search >> ");
                    String toSearch = scanner.next();
                    d.search(toSearch);
                }
            } catch (InputMismatchException e) {
                System.out.print("\u001B[31mError,Please choose a right option\n\u001B[0m");
            }
        }
    }

}
