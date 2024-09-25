### Prettifier Program Overview

The `Prettifier` program processes data from an input file (`input.txt`), performs transformations based on a database lookup file (`airport-lookup.csv`), and outputs formatted data to an output file (`output.txt`). Let's go through each part:

#### Input Example (`input.txt`)

```
Your flight departs from *#HAJ, and your destination is *##EDDW.

1. D(2022-05-09T08:07Z)
2. T12(2069-04-24T19:18-02:00)
3. T12(2080-05-04T14:54Z)
4. T12(1980-02-17T03:30+11:00)
5. T12(2029-09-04T03:09Z)
6. T24(2032-07-17T04:08+13:00)
7. T24(2084-04-13T17:54Z)
8. T24(2024-07-23T15:29-11:00)
9. T24(2042-09-01T21:43Z)
```

#### Output Example (Markdown Format)

```
Your flight departs from Honiara, and your destination is Hamburg.

1. May 9 2022 08:07
2. Apr 24 2069 07:18 PM (-02:00)
3. May 4 2080 14:54
4. Feb 17 1980 03:30 PM (+11:00)
5. Sep 4 2029 03:09
6. Jul 17 2032 04:08 AM (+13:00)
7. Apr 13 2084 17:54
8. Jul 23 2024 03:29 PM (-11:00)
9. Sep 1 2042 21:43
```

### Explanation

1.  **Input Parsing (`scanInput` method)**:
    
    *   Reads the contents of `input.txt`, splits lines into tokens, and stores them for further processing.
2.  **Database Lookup (`scanDatabase` method)**:
    
    *   Reads `airport-lookup.csv`, parses it into data rows, and builds a mapping (`columnMap`) for quick lookup of airport details.
3.  **Token Transformation (`convertToAirportNames` method)**:
    
    *   Replaces tokens prefixed with `*#` or `*##` with corresponding airport names using data from the database lookup.
4.  **Date/Time Formatting (`convertToReadableTime` method)**:
    
    *   Converts tokens prefixed with `D` or `T12`/`T24` into readable date/time formats:
        *   Dates are formatted as `Month Day Year HH:MM`.
        *   Times are converted to 12-hour format with AM/PM and adjusted for time zones.
5.  **Error Handling (`isDatabaseMalformed` method)**:
    
    *   Checks if the database lookup file is malformed based on expected structure and non-empty fields.
6.  **Output (`writeToFile` method)**:
    
    *   Writes the processed lines into `output.txt` and prints formatted output to the console in blue color.

### Usage

To run the program:

```bash
$ java Prettifier ./input.txt ./output.txt ./airport-lookup.csv
```

This will process `input.txt`, apply transformations using `airport-lookup.csv`, and save formatted output to `output.txt`.