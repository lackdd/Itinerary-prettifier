import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Prettifier {
    public static void main(String[] args) {
        String input = args[0];
        String output = args[1];
        String database = args[2];
        String[] tokens = new String[0];
        try (Scanner scanner = new Scanner(new File(input))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                tokens = line.split("\\s+");
            }
        }   catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (tokens.length > 0) {
        for (int i = 0; i < tokens.length; i++) {
            System.out.print(tokens[i] + " ");
        }
        } else {
            System.out.println("No data found");
        }
    }
}
