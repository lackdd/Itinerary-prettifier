### Prettifier Program Overview

The `Prettifier` program processes input data from `input.txt`, performs database lookups using `airport-lookup.csv`, and formats the output to `output.txt`. It handles date/time formatting and converts airport codes to human-readable names based on specific rules.

### Features and Components

1.  **Input Handling (`scanInput` method):**
    
    *   Reads and tokenizes lines from `input.txt`.
    *   Handles different line terminations (\\r\\n, \\n, \\u000B, \\u000C).
    
    **Example:**
    
    ```
    D2023-01-15 T12:30Z *#AAA #BBB T2400Z
    D2023-03-20 T0700Z
    ```
    
2.  **Database Lookup (`scanDatabase` method):**
    
    *   Reads and parses `airport-lookup.csv` into a structured data format (`ArrayList<String[]>`).
    *   Maps column headers to indices for efficient lookup.
    
    **Example:**
    
    ```
    icao_code,name,iata_code,municipality
    AAA,Airport A,#AAA,City A
    BBB,Airport B,#BBB,City B
    ```
    
3.  **Date and Time Formatting (`convertToReadableTime` method):**
    
    *   Converts date (`Dyyyy-mm-dd`) and time (`T12:00Z`, `T2400Z`) tokens into human-readable formats.
    *   Handles timezone conversions.
    
    **Example:**
    
    ```
    D2023-01-15 -> Jan 15 2023
    T12:30Z -> 12:30 PM (+00:00)
    T2400Z -> 00:00 (+00:00)
    ```
    
4.  **Airport Name Conversion (`convertToAirportNames` method):**
    
    *   Converts airport code tokens (`*#AAA`, `#BBB`) to corresponding city or airport names using data from `airport-lookup.csv`.
    
    **Example:**
    
    ```
    *#AAA -> City A
    #BBB -> City B
    ```
    
5.  **Error Handling:**
    
    *   Checks for file existence (`input.txt`, `airport-lookup.csv`).
    *   Validates the structure of `airport-lookup.csv` for potential errors.
6.  **Output (`writeToFile` method):**
    
    *   Writes formatted output lines to `output.txt`.

### Example Usage

Assume `input.txt` contains:

```
D2023-01-15 T12:30Z *#AAA #BBB T2400Z
D2023-03-20 T0700Z
```

After running the program with the command:

```
$ java Prettifier input.txt output.txt airport-lookup.csv
```

The output in `output.txt` might look like:

```
Jan 15 2023 12:30 PM (+00:00) City A City B
Mar 20 2023 07:00 AM (+00:00)
```

### Command Line Usage

To run the program:

```
$ java Prettifier input.txt output.txt airport-lookup.csv
```

This command reads from `input.txt`, processes each line according to specified rules, performs database lookups using `airport-lookup.csv`, and writes formatted data to `output.txt`.

* * *