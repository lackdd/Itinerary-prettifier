import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class Prettifier {
    public static void main(String[] args) {
        String input = args[0];
        String output = args[1];
        String database = args[2];
        String[] tokens = scanInput(input);
        ArrayList<ArrayList<String>> data = scanDatabase(database);
        tokens = convertToAirportNames(tokens, data);
        writeToFile(output, tokens);
    }

    public static String[] convertToAirportNames(String[] tokens, ArrayList<ArrayList<String>> data) {
        if (tokens.length > 0) {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].charAt(0) == '#') {
                    if (tokens[i].charAt(1) == '#') {
                        tokens[i] = tokens[i].substring(2);
                        for (int k = 1; k < data.get(3).size(); k++) {
                            if (tokens[i].equals(data.get(3).get(k))) {
                                tokens[i] = data.get(0).get(k);
                            }
                        }
                    } else {
                        tokens[i] = tokens[i].substring(1);
                        for (int k = 1; k < data.get(4).size(); k++) {
                            if (tokens[i].equals(data.get(4).get(k))) {
                                tokens[i] = data.get(0).get(k);
                            }
                        }
                    }
                }
                System.out.print(tokens[i] + " ");
            }
        } else {
            System.out.println("No data found in input.txt");
        }
        return tokens;
    }

    public static String[] scanInput(String input) {
        String[] tokens = new String[0];
        try (Scanner scanner = new Scanner(new File(input))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                tokens = line.replaceAll("[,.]", "").split("\\s+");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static ArrayList<ArrayList<String>> scanDatabase(String input) {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<ArrayList<String>> groups = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(input))) {
            boolean isFirstLine = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (isFirstLine) {
                    String[] groupN = line.split(",");
                    for (String element : groupN) {
                        ArrayList<String> groupList = new ArrayList<>();
                        groupList.add(element);
                        groups.add(groupList);
                    }
                    isFirstLine = false;
                } else {
                    String[] tokens = line.split(",");
                    ArrayList<String> tokenList = new ArrayList<>(Arrays.asList(tokens));
                    data.add(tokenList);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < data.size(); i++) {
            ArrayList<String> tokenList = data.get(i);
            for (int j = 0; j < tokenList.size(); j++) {
                if (j < groups.size()) {
                    groups.get(j).add(tokenList.get(j));
                }
            }
        }
        /*for (ArrayList<String> group : groups) {
            System.out.println(group);
        }*/
        return groups;
    }

    public static void writeToFile(String output, String[] content) {
        try (FileWriter writer = new FileWriter(output)) {
            String result = String.join(" ", content);
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
