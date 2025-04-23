# Safety Net - NYC Crime Analysis Application

## Overview
Safety Net is a sophisticated desktop application designed to analyze and visualize crime data across different regions of New York City using comprehensive NYPD arrest data (approximately four million crime records). The application employs advanced data processing techniques and graph algorithms to provide critical safety insights for tourists and real estate investors.

By leveraging spatial data analysis, statistical computation, and pathfinding algorithms, Safety Net enables users to make informed decisions about safe areas to stay, potentially saving money by identifying secure locations outside of Manhattan's premium-priced areas. Real estate investors can use the application to analyze crime patterns and evaluate potential investment opportunities in areas showing signs of gentrification.

## Key Features
- **Crime Distribution Analysis**: Compare crime frequency distribution across NYC's five boroughs (Manhattan, Brooklyn, Queens, Bronx, Staten Island) with statistical visualization
- **Crime Type Classification**: Analyze the prevalence of different crime categories in specific neighborhoods with frequency ranking
- **Temporal Crime Analysis**: Examine crime patterns across different time periods using binary search algorithms for efficient data retrieval
- **Geo-specific Crime Lookup**: Query crime data within a specified radius of any NYC address using LocationIQ's geocoding API and coordinate-based filtering
- **Safest Path Navigation**: Calculate the optimal route between NYC neighborhoods using Dijkstra's shortest path algorithm with crime-weighted edges

## Technology Stack
- **Programming Language**: Java SE 10
- **Data Processing**: OpenCSV 5.1 for efficient parsing of large crime datasets
- **JSON Processing**: Google Gson 2.6.2 for handling API responses
- **Geocoding**: LocationIQ API for address-to-coordinate conversion 
- **Testing Framework**: JUnit 4 for comprehensive unit testing
- **Supporting Libraries**: Apache Commons Lang 3.10 for string manipulation and other utilities

## Data Architecture
The application utilizes a sophisticated data model built on several interconnected ADTs (Abstract Data Types):

```
                       ┌───────────┐
                       │    Map    │
                       └─────┬─────┘
                             │
                 ┌───────────┼───────────┐
                 │           │           │
          ┌──────▼─────┐    ┌▼─┐   ┌─────▼──────┐
          │   ChunkT   │    │ │   │ ShortestPath│
          └──────┬─────┘    └─┘   └─────────────┘
                 │                       │
                 │                       │
          ┌──────▼─────┐         ┌──────▼──────┐
          │   CrimeT   │         │EdgeWeightedDiGraph│
          └──────┬─────┘         └───────┬──────┘
                 │                       │
          ┌──────▼─────┐          ┌─────▼─────┐
          │ CoordinateT│          │DirectedEdge│
          └────────────┘          └───────────┘
```

## Implementation Details

### Data Models

#### CoordinateT
A fundamental data structure that encapsulates GPS coordinates.

```java
public class CoordinateT {
    private final double latitude;
    private final double longitude;
    
    // Constructor and accessor methods
    
    public double distanceTo(CoordinateT coordinate) {
        // Implements the Haversine formula to calculate the great-circle distance
        // between two points on the Earth's surface
    }
}
```

#### CrimeT
Represents a specific crime incident with complete metadata.

```java
public class CrimeT implements Comparable<CrimeT> {
    private final LocalDate date;
    private final String description;
    private final CoordinateT coordinates;
    
    // Constructor and accessor methods
    
    public int compareTo(CrimeT other) {
        // Compares crimes by date for temporal analysis
        return this.date().compareTo(other.date());
    }
}
```

#### ChunkT
Represents a geographical region (NYC borough) with defined boundaries and crime data.

```java
public class ChunkT {
    private final String neighbourhoodName;
    private final double[] boundaries;  // [leftLongitude, rightLongitude, topLatitude, bottomLatitude]
    private ArrayList<CrimeT> crimesCommitedInArea;
    private int crimeCount;
    
    // Methods for adding crimes and verifying spatial containment
    
    public boolean isInBounds(CrimeT crime) {
        // Determines if a crime occurred within this geographic chunk
    }
}
```

### Core Components

#### Map
The central module that orchestrates data processing and analysis.

Key functionalities:
- Initialization of geographic regions (5 NYC boroughs)
- CSV parsing of ~4 million crime records using optimized OpenCSV implementation
- Binary search algorithms for temporal data queries
- Statistical analysis of crime frequencies
- Geocoding integration for address-based queries
- Proximity-based crime filtering

#### Interface
The user interface controller that handles all user interactions.

Features:
- Interactive command-line interface with menu-driven navigation
- Multi-level option selection for different query types
- Data visualization through formatted text output
- Error handling and input validation
- Asynchronous processing for time-intensive operations

#### Path Finding System

The application implements a sophisticated path-finding subsystem using Dijkstra's algorithm to determine the safest routes between NYC neighborhoods:

```
┌─────────────┐     ┌──────────────────┐     ┌───────────────┐
│ ShortestPath│◄────┤EdgeWeightedDiGraph│◄────┤ DirectedEdge  │
└─────┬───────┘     └──────────────────┘     └───────────────┘
      │                      ▲
      │                      │
      │                      │
      └──────────────────────┘
```

- **DirectedEdge**: Represents a one-way connection between neighborhoods with crime-weighted cost
- **EdgeWeightedDiGraph**: Maintains the topological structure of NYC with all possible routes
- **ShortestPath**: Implements Dijkstra's algorithm to calculate minimum-crime paths

## Algorithm Implementation Details

### Binary Search for Crime Data

The application employs a custom binary search implementation to efficiently locate crimes within a specific timeframe:

```java
private static int binarySearch(ArrayList<CrimeT> arr, LocalDate date, int lo, int hi) {
    while(lo <= hi) {
        int mid = lo + (hi - lo)/2;
        int cmp = date.compareTo(arr.get(mid).date());
        if(cmp < 0) {
            hi = mid - 1;
        } else if(cmp > 0) {
            lo = mid + 1;
        } else    return mid;
    }
    return lo;
}
```

### Dijkstra's Shortest Path Algorithm

The application implements Dijkstra's algorithm for finding the safest path between NYC boroughs:

```java
public ShortestPath(EdgeWeightedDiGraph G, String source) {
    distTo = new HashMap<>();
    edgeTo = new HashMap<>();
    PQ = new TreeMap<>();
    
    // Initialize distances
    for(String vertex : G.adj().keySet()) {
        distTo.put(vertex, Double.POSITIVE_INFINITY);
    }
    distTo.put(source, 0.0);
    PQ.put(0.0, source);
    
    // Process vertices in order of distance
    while(! PQ.isEmpty()) {
        relax(G, PQ.pollFirstEntry().getValue());
    }
}
```

### Haversine Formula for Distance Calculation

The application uses the Haversine formula to calculate accurate distances between geographic coordinates:

```java
public double distanceTo(CoordinateT coordinate) {
    double dLat = Math.toRadians(coordinate.latitude - this.latitude);
    double dLon = Math.toRadians(coordinate.longitude - this.longitude);
    double lat1 = Math.toRadians(this.latitude);
    double lat2 = Math.toRadians(coordinate.latitude);

    double a = Math.pow(Math.sin(dLat / 2), 2) + 
               Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return 6372.8 * c; // Earth radius in kilometers
}
```

## Installation Guide

### System Requirements
- Java SE Development Kit (JDK) 10 or higher
- Minimum 8GB RAM recommended due to large dataset processing
- 500MB available disk space (excluding the crime dataset)
- Internet connection for geocoding functionality

### Detailed Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/Saftey-Net.git
   cd Saftey-Net
   ```

2. **Set up the dependencies:**
   - Download and place the following JAR files in the `lib` directory:
     - [opencsv-5.1.jar](https://search.maven.org/remotecontent?filepath=com/opencsv/opencsv/5.1/opencsv-5.1.jar)
     - [gson-2.6.2.jar](https://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.6.2/gson-2.6.2.jar)
     - [commons-lang3-3.10.jar](https://search.maven.org/remotecontent?filepath=org/apache/commons/commons-lang3/3.10/commons-lang3-3.10.jar)

3. **Dataset preparation:**
   - Download the NYPD Arrests Data (Historic) CSV file from [NYC Open Data](https://data.cityofnewyork.us/Public-Safety/NYPD-Arrests-Data-Historic-/8h9b-rp9u)
   - Place the CSV file in the `Datasets` directory with the name `NYPD_Arrests_Data__Historic_.csv`

4. **Build the project:**
   - For Eclipse users:
     ```
     File > Import > Existing Projects into Workspace > Select root directory > Finish
     ```
   - For IntelliJ IDEA users:
     ```
     File > Open > Select project root directory > OK
     ```
   - For command line compilation:
     ```bash
     javac -cp ".:lib/*" -d bin src/src/*.java
     ```

5. **Configure API key (for location lookup functionality):**
   - Sign up for a free API key at [LocationIQ](https://locationiq.com/)
   - Replace the API key in `Map.java` line 221: 
     ```java
     URL url = new URL("https://us1.locationiq.com/v1/search.php?key=YOUR_API_KEY&q=" + address + "&format=json");
     ```

## Usage Examples

### Running the Application

```bash
java -cp "bin:lib/*" src.Interface
```

Upon startup, you'll see the Safety Net logo and a menu of options:

```
  ____    _    _____ _____ _______   __  _   _ _____ _____ 
 / ___|  / \  |  ___| ____|_   _\ \ / / | \ | | ____|_   _|
 \___ \ / _ \ | |_  |  _|   | |  \ V /  |  \| |  _|   | |  
  ___) / ___ \|  _| | |___  | |   | |   | |\  | |___  | |  
 |____/_/   \_|_|   |_____| |_|   |_|   |_| \_|_____| |_|  
                                                           

Please select an option and press enter: 
1) Safest neighbourhoods
2) Frequently occuring crimes
3) Crimes within a timeframe
4) Crimes at a specific location
5) Safest path
6) Exit

Choice: 
```

### Sample Interactions

#### Finding the Safest Neighborhoods
```
Choice: 1
Would you like to sort by:
1) Safest
2) Most dangerous
Choice: 1

1: STATEN ISLAND
2: QUEENS
3: BROOKLYN
4: BRONX
5: MANHATTAN
```

#### Analyzing Crime Types
```
Choice: 2
Would you like to sort by:
1) Most frequent occuring
2) Least frequent occuring
Choice: 1

1: ASSAULT 3 (54321)
2: PETIT LARCENY (43210)
3: CRIMINAL MISCHIEF (32109)
4: DANGEROUS DRUGS (21098)
5: HARRASSMENT 2 (10987)
Show more? (y/n): y
6: FELONY ASSAULT (9876)
...
```

#### Location-based Crime Analysis
```
Choice: 4
Enter a location: (ex. "Hunt's Point, New York City", "1435 Broadway, New York, NY 10018")
Times Square, New York
Enter a radius (km): 
1

There have been 5432 crimes recorded within a 1km radius.
Most frequent crimes: 
1: PETIT LARCENY (987)
2: ASSAULT 3 (654)
3: CRIMINAL MISCHIEF (321)
4: GRAND LARCENY (123)
5: DANGEROUS DRUGS (98)
Show more? (y/n): n
```

#### Finding Safest Path
```
Choice: 5
Please enter a starting neighbourhood and destination in the format "start,end"
Options: [Manhattan, Brooklyn, Staten Island, Bronx, Queens]
QUEENS,BROOKLYN

QUEENS: --> BROOKLYN
```

## Advanced Usage

### Programmatic API Usage

The Safety Net codebase can also be used programmatically by importing the relevant classes:

```java
import src.Map;
import src.CrimeT;
import src.CoordinateT;

// Initialize the Map (load data)
Map.init();

// Get crime statistics for Manhattan
NavigableMap<String, Integer> manhattanCrimes = Map.getCrimeStatistics(true, "MANHATTAN");

// Find crimes near a specific location (1km radius)
CoordinateT location = Map.geocode("Empire State Building, New York");
List<CrimeT> nearbyCrimes = Map.crimesAroundLocation(location, 1.0);

// Get the safest path
String safestPath = Map.safestPath("MANHATTAN", "BROOKLYN");
```

## Data Structure Design

### Memory Optimization

The application implements several strategies to optimize memory usage when dealing with the large NYPD crime dataset:

1. **Spatial Partitioning**: Crimes are stored in borough-specific chunks to allow efficient querying
2. **Lazy Loading**: Crime data is only processed when the application initializes
3. **Sorted Storage**: Crimes are sorted by date to enable efficient binary search for temporal queries

### Performance Considerations

For large datasets (>1 million records), consider:

1. Increasing Java heap size: `-Xmx4g` 
2. Using SSD storage for dataset access
3. Pre-filtering the dataset to a specific time range if full historical data is not needed

## Architecture and Design Patterns

The application follows a layered architecture with elements of the Model-View-Controller (MVC) pattern:

- **Model Layer**: CrimeT, CoordinateT, ChunkT classes define the data model
- **View Layer**: Interface class handles user interaction and result display
- **Controller Layer**: Map class coordinates data processing and business logic

The graph algorithm components (ShortestPath, EdgeWeightedDiGraph, DirectedEdge) implement a classic graph data structure with weighted edges to represent the crime-safety metric between locations.

## Unit Testing

The application includes comprehensive JUnit tests covering the core data structures:

```java
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestChunkT.class,
    TestCoordinateT.class,
    TestCrimeT.class
})
public class TestAll {
}
```

Run the tests using:
```bash
java -cp "bin:lib/*:lib/junit.jar:lib/hamcrest.jar" org.junit.runner.JUnitCore src.TestAll
```

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements
- **Data Source**: NYC Open Data for providing the NYPD Arrests Dataset
- **OpenCSV**: For efficient CSV parsing of large datasets
- **Gson**: For JSON processing of API responses
- **LocationIQ**: For providing geocoding services
- **Apache Commons**: For utility functions

## Project Contributors
- Sunny Bhatt (Developer/Tester): CSV parsing, location crime lookup, unit tests, Map MIS
- Nikola Milanovic (Team Leader/Developer/Maintainer): ADT's, Map, Interface, ShortestPath, UML, Design
- Suleyman Kiani (Developer): Module design, Application Design, MIS
- Senan Gohar (Developer): User interface, Java documentation, architecture design

## Contact
For any questions, bug reports, or feature requests, please file an issue on the project repository or contact the project maintainers directly.

