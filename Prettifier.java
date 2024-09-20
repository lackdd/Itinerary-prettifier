import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Prettifier {
    public static void main(String[] args) {
        String input = args[0];
        String output = args[1];
        String database = args[2];
        String[] tokens = scan(input);
        String[] tokens2 = scan(database);
        if (tokens.length > 0) {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].charAt(0) == '#') {
                    if (tokens[i].charAt(1) == '#') {
                        tokens[i] = tokens[i].substring(2);
                    } else {
                        tokens[i] = tokens[i].substring(1);
                    }

                }
                System.out.print(tokens[i] + " ");
            }
        } else {
            System.out.println("No data found in input.txt");
        }
    }
    public static String[] scan(String input) {
        String[]tokens = new String[0];
        try (Scanner scanner = new Scanner(new File(input))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                tokens = line.split("\\s+");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokens;
    }
}
