import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;

public class Prettifier {
    public static final String blue_bold = "\u001B[1;34m";
    public static final String green_bold = "\u001B[1;32m";
    public static final String red = "\u001B[31m";
    public static final String reset = "\u001B[0m";

    public static void main(String[] args) {
        if ((args.length > 0 && args[0].equals("-h")) || args.length != 3) {
            printUsage();
            return;
        }
        String input = args[0];
        String output = args[1];
        String database = args[2];
        File inputFile = new File(input);
        if (!inputFile.exists()) {
            System.out.println(red + "Input not found" + reset);
            return;
        }
        File databaseFile = new File(database);
        if (!databaseFile.exists()) {
            System.out.println(red + "Airport lookup not found" + reset);
            return;
        }
        ArrayList<String[]> tokenLines = scanInput(input);
        ArrayList<String> processedLines = new ArrayList<>();
        HashMap<String, Integer> columnMap = new HashMap<>();
        ArrayList<String[]> data = scanDatabase(database, columnMap);
        for (String[] tokens : tokenLines) {
            tokens = convertToAirportNames(tokens, data, columnMap);
            tokens = convertToReadableTime(tokens);
            processedLines.add(String.join(" ", tokens));
        }
        boolean isMalformed = isDatabaseMalformed(data);
        if (isMalformed == true) {
            System.out.println(red + "Airport lookup malformed" + reset);
        } else {
            for (String line : processedLines) {
                System.out.println(blue_bold + line + reset);
            }
            writeToFile(output, processedLines);
        }
    }

    private static void printUsage() {
        System.out.println(green_bold + "itinerary usage:" + reset);
        System.out.println(green_bold + "$ java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv" + reset);
    }

    public static String[] convertToReadableTime(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            ArrayList<String> newFormats = new ArrayList<>();
            String newFormat = tokens[i];
            if (!tokens[i].isEmpty() && tokens[i].charAt(0) == 'D') {
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
            } else if (!tokens[i].isEmpty() && tokens[i].charAt(0) == 'T' && tokens[i].charAt(1) == '1' && tokens[i].charAt(2) == '2') {
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
                boolean isDateMalformed = true;
                if (tokens[i].charAt(15) >= '0' && tokens[i].charAt(15) <= '2' && tokens[i].charAt(16) >= '0' && tokens[i].charAt(16) <= '9' && tokens[i].charAt(17) == ':' && tokens[i].charAt(18) >= '0' && tokens[i].charAt(18) <= '5' && tokens[i].charAt(19) >= '0' && tokens[i].charAt(19) <= '9') {
                    if (tokens[i].charAt(20) == 'Z' || (tokens[i].charAt(21) >= '0' && tokens[i].charAt(21) <= '1' && tokens[i].charAt(22) >= '0' && tokens[i].charAt(22) <= '9' &&
                            tokens[i].charAt(23) == ':' && tokens[i].charAt(24) == '0' && tokens[i].charAt(25) == '0')) {
                        isDateMalformed = false;
                        if (tokens[i].charAt(15) == '2' && tokens[i].charAt(16) >= '4' && tokens[i].charAt(16) <= '9') {
                            isDateMalformed = true;
                        }
                    }
                }
                if (isDateMalformed) {
                    newFormat = tokens[i];
                }
            } else if (!tokens[i].isEmpty() && tokens[i].charAt(0) == 'T' && tokens[i].charAt(1) == '2' && tokens[i].charAt(2) == '4') {
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
                boolean isDateMalformed = true;
                if (tokens[i].charAt(15) >= '0' && tokens[i].charAt(15) <= '2' && tokens[i].charAt(16) >= '0' && tokens[i].charAt(16) <= '9' && tokens[i].charAt(17) == ':' && tokens[i].charAt(18) >= '0' && tokens[i].charAt(18) <= '5' && tokens[i].charAt(19) >= '0' && tokens[i].charAt(19) <= '9') {
                    if (tokens[i].charAt(20) == 'Z' || (tokens[i].charAt(21) >= '0' && tokens[i].charAt(21) <= '1' && tokens[i].charAt(22) >= '0' && tokens[i].charAt(22) <= '9' &&
                            tokens[i].charAt(23) == ':' && tokens[i].charAt(24) == '0' && tokens[i].charAt(25) == '0')) {
                        isDateMalformed = false;
                        if (tokens[i].charAt(15) == '2' && tokens[i].charAt(16) >= '4' && tokens[i].charAt(16) <= '9') {
                            isDateMalformed = true;
                        }
                    }
                }
                if (isDateMalformed) {
                    newFormat = tokens[i];
                }
            }
            tokens[i] = newFormat;
        }
        return tokens;
    }

    public static String[] convertToAirportNames(String[] tokens, ArrayList<String[]> data, HashMap<String, Integer> columnMap) {
        if (tokens.length > 0) {
            for (int i = 0; i < tokens.length; i++) {
                if (!tokens[i].isEmpty() && tokens[i].charAt(0) == '*') {
                    if (tokens[i].charAt(1) == '#') {
                        if (tokens[i].charAt(2) == '#') {
                            tokens[i] = tokens[i].substring(3);
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
                            int municipalityIndex = columnMap.get("municipality");
                            int icaoIndex = columnMap.get("icao_code");
                            for (int k = 0; k < data.size(); k++) {
                                if (tokens[i].equals(data.get(k)[icaoIndex])) {
                                    tokens[i] = data.get(k)[municipalityIndex];
                                    if (comma) {
                                        tokens[i] += ",";
                                    }
                                    if (dot) {
                                        tokens[i] += ".";
                                    }
                                    break;
                                }
                            }
                        } else {
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
                            int municipalityIndex = columnMap.get("municipality");
                            int iataIndex = columnMap.get("iata_code");
                            for (int k = 0; k < data.size(); k++) {
                                if (tokens[i].equals(data.get(k)[iataIndex])) {
                                    tokens[i] = data.get(k)[municipalityIndex];
                                    if (comma) {
                                        tokens[i] += ",";
                                    }
                                    if (dot) {
                                        tokens[i] += ".";
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!tokens[i].isEmpty() && tokens[i].charAt(0) == '#') {
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
                        int nameIndex = columnMap.get("name");
                        int icaoIndex = columnMap.get("icao_code");
                        for (int k = 0; k < data.size(); k++) {
                            if (tokens[i].equals(data.get(k)[icaoIndex])) {
                                tokens[i] = data.get(k)[nameIndex];
                                if (comma) {
                                    tokens[i] += ",";
                                }
                                if (dot) {
                                    tokens[i] += ".";
                                }
                                break;
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
                        int nameIndex = columnMap.get("name");
                        int iataIndex = columnMap.get("iata_code");
                        for (int k = 0; k < data.size(); k++) {
                            if (tokens[i].equals(data.get(k)[iataIndex])) {
                                tokens[i] = data.get(k)[nameIndex];
                                if (comma) {
                                    tokens[i] += ",";
                                }
                                if (dot) {
                                    tokens[i] += ".";
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println(red + "No data found in input.txt" + reset);
        }
        return tokens;
    }

    public static ArrayList<String[]> scanInput(String input) {
        ArrayList<String[]> tokenList = new ArrayList<>();
        try {
            String fileContent = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(input)));
            fileContent = fileContent.replaceAll("\r\n", "\n");
            fileContent = fileContent.replaceAll("[\u000B\u000C]", "\n");
            fileContent = fileContent.replaceAll("\n{3,}", "\n\n");
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    tokenList.add(new String[]{""});
                } else {
                    String[] tokens = line.trim().split("[\\s]");
                    tokenList.add(tokens);
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return tokenList;
    }

    public static ArrayList<String[]> scanDatabase(String input, HashMap<String, Integer> columnMap) {
        ArrayList<String[]> data = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(input))) {
            if (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                String[] headers = headerLine.split(",");
                for (int i = 0; i < headers.length; i++) {
                    columnMap.put(headers[i].trim().toLowerCase().replaceAll("\"", ""), i);
                }
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    ArrayList<String> tokens = new ArrayList<>();
                    boolean inQuotes = false;
                    StringBuilder currentToken = new StringBuilder();
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        if (c == '"') {
                            inQuotes = !inQuotes;
                        } else if (c == ',' && !inQuotes) {
                            tokens.add(currentToken.toString().trim().replaceAll("\"", ""));
                            currentToken.setLength(0);
                        } else {
                            currentToken.append(c);
                        }
                    }
                    tokens.add(currentToken.toString().trim().replaceAll("\"", ""));
                    data.add(tokens.toArray(new String[0]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;

    }

    public static boolean isDatabaseMalformed(ArrayList<String[]> data) {
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            for (String token : row) {
                if (token == null || token.trim().isEmpty()) {
                    return true;
                }
            }
        }
        int expectedColumns = data.get(0).length;
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).length != expectedColumns) {
                return true;
            }
        }
        return false;
    }

    public static void writeToFile(String output, ArrayList<String> content) {
        try (FileWriter writer = new FileWriter(output)) {
            for (String line : content) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
