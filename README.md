* * *

### Itinerary-Prettifier

#### Functional Requirements

**Input:** Path to the input file containing the text-based itinerary.

**Output:** Path to the output file where the prettified itinerary will be written.

**Airport Lookup:** Path to a CSV file (`airport-lookup.csv`) containing airport data for converting codes to names. Supports dynamic column order and city name lookup.

**Usage:**

```bash
$ java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv
```

**Help Flag (`-h`):**

```bash
$ java Prettifier.java -h
itinerary usage:
$ java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv
```

#### Features

1.  **Airport Names Conversion:**
    
    *   Converts IATA (`#LAX`) and ICAO (`##EGLL`) airport codes to corresponding airport names using the provided CSV lookup.
    *   Supports dynamic column order in the airport lookup CSV.
2.  **City Names Conversion:**
    
    *   Optionally converts city names using `*` symbol prefixed codes (e.g., `*#LHR` -> "London").
3.  **Date and Time Formatting:**
    
    *   Formats dates (`D(...)`) as `DD-Mmm-YYYY` (e.g., `05 Apr 2007`).
    *   Formats 12-hour times (`T12(...)`) as `12:30PM (-02:00)` and 24-hour times (`T24(...)`) as `12:30 (-02:00)`.
    *   Handles "Zulu time" (`Z`) as `(00:00)`.
4.  **Whitespace Trimming:**
    
    *   Converts vertical whitespace characters (`\v`, `\f`, `\r`) to newline characters (`\n`).
    *   Ensures no more than two consecutive blank lines.
5.  **Error Handling:**
    
    *   Displays usage if incorrect number of arguments provided.
    *   Checks for existence and validity of input files.
    *   Validates airport lookup file structure and content integrity.
    *   Prevents creation of output file on error conditions.
6.  **Additional Features:**
    
    *   Supports formatting enhancements for output to stdout (optional).
    *   Highlights specific information like dates, times, offsets, airport names, and cities using color and formatting (optional).

#### Example

**Input (`input.txt`):**

```
Flight Itinerary
Departure: #JFK at D(2023-09-25T08:00-04:00)
Arrival: ##LHR at T12(2023-09-25T18:30Z)

Connecting Flight:
Departure: *#LAX at D(2023-09-25T20:00-07:00)
Arrival: ##SIN at T24(2023-09-26T06:00+08:00)
```

**Output (`output.txt`):**

```
Flight Itinerary
Departure: New York (JFK) at 25-Sep-2023 08:00AM (-04:00)
Arrival: London Heathrow (LHR) at 25-Sep-2023 06:30PM (00:00)

Connecting Flight:
Departure: Los Angeles at 25-Sep-2023 08:00PM (-07:00)
Arrival: Singapore (SIN) at 26-Sep-2023 06:00 (+08:00)
```

* * *