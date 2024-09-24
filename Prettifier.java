import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class Prettifier {
    public static void main(String[] args) {
        if (args[0].equals("-h") && args.length > 0) {
            printUsage();
            return;
        }
        String input = args[0];
        String output = args[1];
        String database = args[2];
        ArrayList<String[]> tokenLines = scanInput(input);
        ArrayList<String> processedLines = new ArrayList<>();
        ArrayList<ArrayList<String>> data = scanDatabase(database);
        for (String[] tokens : tokenLines) {
            tokens = convertToAirportNames(tokens, data);
            tokens = convertToReadableTime(tokens);
            processedLines.add(String.join(" ", tokens));
        }
        for (String line : processedLines) {
            System.out.println(line);
        }
        writeToFile(output, processedLines);
    }
    private static void printUsage() {
        System.out.println("itinerary usage:");
        System.out.println("$ java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv");
    }
    public static String[] convertToReadableTime(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            ArrayList<String> newFormats = new ArrayList<>();
            String newFormat = tokens[i];
            if (tokens[i].charAt(0) == 'D') {
                String[] dateTokens = tokens[i].split("[-T]");
                dateTokens[0] = dateTokens[0].substring(2);
                newFormats.add(dateTokens[2]);
                String month = dateTokens[1];
                switch (month) {
                    case "01":
                        newFormats.add("Jan");
                        break;
                    case "02":
                        newFormats.add("Feb");
                        break;
                    case "03":
                        newFormats.add("Mar");
                        break;
                    case "04":
                        newFormats.add("Apr");
                        break;
                    case "05":
                        newFormats.add("May");
                        break;
                    case "06":
                        newFormats.add("Jun");
                        break;
                    case "07":
                        newFormats.add("Jul");
                        break;
                    case "08":
                        newFormats.add("Aug");
                        break;
                    case "09":
                        newFormats.add("Sep");
                        break;
                    case "10":
                        newFormats.add("Oct");
                        break;
                    case "11":
                        newFormats.add("Nov");
                        break;
                    case "12":
                        newFormats.add("Dec");
                        break;
                    default:
                        break;

                }
                newFormats.add(dateTokens[0]);
                newFormat = String.join(" ", newFormats);
            } else if (tokens[i].charAt(0) == 'T' && tokens[i].charAt(1) == '1' && tokens[i].charAt(2) == '2') {
                String[] timeTokens = tokens[i].split("[T]");
                String time = timeTokens[2].substring(0, 5);
                String hour = time.substring(0, 2);
                String minute = time.substring(3, 5);
                int hourInt = Integer.parseInt(hour);
                int minuteInt = Integer.parseInt(minute);
                if (hourInt >= 12 && hourInt < 24) {
                    minute = minute + "PM";
                } else if (hourInt < 12 && hourInt > 00) {
                    minute = minute + "AM";
                }
                hourInt = hourInt % 12;
                hour = String.format("%02d", hourInt);
                minute = String.format(":%02d", minuteInt) + minute.substring(2);
                time = hour + minute;
                String timezone = timeTokens[2].substring(5).replace(")", "");
                if (timezone.equals("Z")) {
                    timezone = "+00:00";
                }
                timezone = "(" + timezone + ")";
                newFormats.add(time);
                newFormats.add(timezone);
                newFormat = String.join(" ", newFormats);
            } else if (tokens[i].charAt(0) == 'T' && tokens[i].charAt(1) == '2' && tokens[i].charAt(2) == '4') {
                String[] timeTokens = tokens[i].split("[T]");
                String time = timeTokens[2].substring(0, 5);
                String hour = time.substring(0, 2);
                String minute = time.substring(2, 5);
                time = hour + minute;
                String timezone = timeTokens[2].substring(5).replace(")", "");
                if (timezone.equals("Z")) {
                    timezone = "+00:00";
                }
                timezone = "(" + timezone + ")";
                for (int l = 0; l < timeTokens.length; l++) {
                }
                newFormats.add(time);
                newFormats.add(timezone);
                newFormat = String.join(" ", newFormats);
            }
            tokens[i] = newFormat;
        }
        return tokens;
    }

    public static String[] convertToAirportNames(String[] tokens, ArrayList<ArrayList<String>> data) {
        if (tokens.length > 0) {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].charAt(0) == '#') {
                    if (tokens[i].charAt(1) == '#') {
                        tokens[i] = tokens[i].substring(2);
                        boolean comma = false;
                        boolean dot = false;
                        if (tokens[i].endsWith(".")) {
                            dot = true;
                            tokens[i] = tokens[i].replace(".", "");
                        }
                        if (tokens[i].endsWith(",")) {
                            comma = true;
                            tokens[i] = tokens[i].replace(",", "");
                        }
                        for (int k = 1; k < data.get(3).size(); k++) {
                            if (tokens[i].equals(data.get(3).get(k))) {
                                tokens[i] = data.get(0).get(k);
                                if (comma) {
                                    tokens[i] += ",";
                                }
                                if (dot) {
                                    tokens[i] += ".";
                                }
                            }
                        }
                    } else {
                        tokens[i] = tokens[i].substring(1);
                        boolean comma = false;
                        boolean dot = false;
                        if (tokens[i].endsWith(".")) {
                            dot = true;
                            tokens[i] = tokens[i].replace(".", "");
                        }
                        if (tokens[i].endsWith(",")) {
                            comma = true;
                            tokens[i] = tokens[i].replace(",", "");
                        }
                        for (int k = 1; k < data.get(4).size(); k++) {
                            if (tokens[i].equals(data.get(4).get(k))) {
                                tokens[i] = data.get(0).get(k);
                                if (comma) {
                                    tokens[i] += ",";
                                }
                                if (dot) {
                                    tokens[i] += ".";
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("No data found in input.txt");
        }
        return tokens;
    }

    public static ArrayList<String[]> scanInput(String input) {
        ArrayList<String[]> tokenList = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(input))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] tokens = line.split("[\\s]");
                        tokenList.add(tokens);
                    }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokenList;
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
        return groups;
    }

    public static void writeToFile(String output, ArrayList<String> content) {
        try (FileWriter writer = new FileWriter(output)) {
            String result = String.join("\n", content);
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
