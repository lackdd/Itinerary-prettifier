Purpose:
The Prettifier program reads input data from a file (input.txt), processes each line according to specific rules, and outputs the formatted data to another file (output.txt). It utilizes a database file (airport-lookup.csv) to convert codes to human-readable names and formats dates and times for readability.

Features:
Input Handling: Reads input from input.txt and splits lines into tokens for processing.
Database Lookup: Uses airport-lookup.csv to convert airport codes to corresponding city names or airport names.
Date and Time Formatting: Converts date and time tokens (Dyyyy-mm-dd and T12:00Z formats) into human-readable formats.
Error Handling: Checks for malformed database files and outputs appropriate error messages.
Example Usage:
Assume input.txt contains the following lines:

D2023-01-15 T12:30Z *#AAA #BBB T2400Z
D2023-03-20 T0700Z
After running the program with the command:

$ java Prettifier input.txt output.txt airport-lookup.csv
The output in output.txt might look like:

Jan 15 2023 12:30 PM (+00:00) Atlanta Chicago Mar 20 2023 07:00 AM (+00:00)
Breakdown of Code Sections:
main Method:
Parses command-line arguments (input.txt, output.txt, airport-lookup.csv).
Checks existence of input and database files.
Processes each line from input.txt, applies transformations, checks for database errors, and outputs formatted lines to console and file.
convertToReadableTime Method:
Converts date (Dyyyy-mm-dd) and time (T12:00Z, T2400Z) tokens into human-readable formats (Jan 15 2023 12:30 PM (+00:00)).
convertToAirportNames Method:
Converts airport code tokens (*#AAA, #BBB) into corresponding city or airport names using airport-lookup.csv.
scanInput Method:
Reads and tokenizes lines from input.txt, handling different line terminations (\r\n, \n, \u000B, \u000C).
scanDatabase Method:
Reads and parses airport-lookup.csv into a structured data format (ArrayList<String[]>), mapping column headers to indices.
isDatabaseMalformed Method:
Checks if the airport-lookup.csv file is malformed by verifying non-empty cells and consistent column counts.
writeToFile Method:
Writes formatted output lines to output.txt.