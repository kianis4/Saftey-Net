package src;

import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Stack;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Map {
	private static ChunkT[] neighbourhoods;
	private static boolean isInit = false;
	
	public static void init() {
		neighbourhoods = new ChunkT[5]; /* There are 5 bureaus in NYC. */
		neighbourhoods[0] = new ChunkT("MANHATTAN", -74.014559, -73.909000, 40.880117, 40.69966);
		neighbourhoods[1] = new ChunkT("BROOKLYN", -74.044355, -73.835905, 40.700050, 40.5666398);
		neighbourhoods[2] = new ChunkT("QUEENS", -73.926968, -73.703860, 40.803194, 40.700050);
		neighbourhoods[3] = new ChunkT("BRONX", -73.929838, -73.815040, 40.918918, 40.802566);
		neighbourhoods[4] = new ChunkT("STATEN ISLAND", -74.256718, -74.055169, 40.648498, 40.494628);

		try {
			readInputFile("Datasets/NYPD_Arrests_Data__Historic_.csv");
			isInit = true;
		} catch (IOException e) {
			System.out.println("Error: could not open CSV file");
		} catch (CsvException e) {
			System.out.println("Error: could not parse CSV file");
		}
	}
	
	/**
	 * Method ranks the bureaus with most/least frequent crime
	 * @param highest a boolean value, if true ranks by most frequent crime. If false ranks by least frequent crime.
	 * @return an Iterable<String> of the bureaus, in order by least or most frequent crime
	 */
	public static Iterable<String> getFrequency(boolean highest) {
		TreeMap<Integer, String> frequencies;
		if(highest) {
			frequencies = new TreeMap<>(Collections.reverseOrder());
		} else {
			frequencies = new TreeMap<>();
		}
		for(ChunkT C : neighbourhoods) {
			frequencies.put(C.getCrimeCount(), C.getName());
		}
		return frequencies.values();
	}
	/**
	 * Method outputs the most/least frequent crimes in NYC
	 * @param highest a boolean value, if true ranks by most frequent crime. If false ranks by least frequent crime.
	 * @return an NavigableMap<String, Integer> of the most frequent crimes in NYC
	 */
	public static NavigableMap<String, Integer> getCrimeStatistics(boolean highest){
		//TODO need comparator for TreeMap to sort by values.
		TreeMap<String, Integer> frequencies = new TreeMap<>();
		for(ChunkT C : neighbourhoods) {
			for(CrimeT crime : C.getCrimes()) {
				if(frequencies.get(crime.description()) == null) {
					frequencies.put(crime.description(), 1);
				} else {
					frequencies.put(crime.description(), frequencies.get(crime.description()) + 1);
				}
			}
		}
		if(highest) {
			return sortMapByValues(frequencies).descendingMap();
		} else {
			return sortMapByValues(frequencies);
		}
	}
	/**
	 * Method outputs the most/least frequent crimes in a specific bureau
	 * @param highest a boolean value, if true ranks by most frequent crime. If false ranks by least frequent crime.
	 * @param neighbourhoodName a String value, the String being an NYC bureau
	 * @return an NavigableMap<String, Integer> of the most frequent crimes in NYC
	 */
	public static NavigableMap<String, Integer> getCrimeStatistics(boolean highest, String neighbourhoodName) {
		TreeMap<String, Integer> frequencies = new TreeMap<>();
		for(ChunkT C : neighbourhoods) {
			if(C.getName().equals(neighbourhoodName)) {
				for(CrimeT crime : C.getCrimes()) {
					if(frequencies.get(crime.description()) == null) {
						frequencies.put(crime.description(), 1);
					} else {
						frequencies.put(crime.description(), frequencies.get(crime.description()) + 1);
					}
				}
			} else {
				continue;
			}
		}
		if(highest) {
			return sortMapByValues(frequencies).descendingMap();
		} else {
			return sortMapByValues(frequencies);
		}
	}
	/**
	 * Method outputs the most/least frequent crimes in NYC
	 * @param highest a boolean value, if true ranks by most frequent crime. If false ranks by least frequent crime.
	 * @param crimes an Iterable of the crimes
	 * @return an NavigableMap<String, Integer> of the most frequent crimes in NYC
	 */
	public static NavigableMap<String, Integer> getCrimeStatistics(boolean highest, Iterable<CrimeT> crimes) {
		TreeMap<String, Integer> frequencies = new TreeMap<>();
		for(CrimeT crime : crimes) {
			if(frequencies.get(crime.description()) == null) {
				frequencies.put(crime.description(), 1);
			} else {
				frequencies.put(crime.description(), frequencies.get(crime.description()) + 1);
			}
		}
		if(highest) {
			return sortMapByValues(frequencies).descendingMap();
		} else {
			return sortMapByValues(frequencies);
		}
	}
	/**
	 * Local method for sorting a Map object by its values
	 * @param map a TreeMap which is not sorted
	 * @return a TreeMap<String, Integer> sorted by its values
	 */
	private static TreeMap<String, Integer> sortMapByValues(TreeMap<String, Integer> map) {
		Comparator<String> cmp = new Comparator<String>() {
			public int compare(String A, String B) {
				return map.get(A).compareTo(map.get(B));
			}
		};
		
		TreeMap<String, Integer> sortedMap = new TreeMap<>(cmp);
		sortedMap.putAll(map);
		return sortedMap;
	}
	/**
	 * Method for reading csv files
	 * @param filePath a String of the filePath
	 * @throws IOException
	 * @throws CsvException
	 */
	private static void readInputFile(String filePath) throws IOException, CsvException {
		BufferedReader input = new BufferedReader(new FileReader(filePath));
		CSVReader csv = new CSVReader(input);
		String[] line;
		csv.readNext();
		int i = 0;
		while ((line = csv.readNext()) != null) {
			i++;
			String[] d = line[1].split("/");
			LocalDate date = LocalDate.parse(d[2] + "-" + d[0] + "-" + d[1]);
			assignCrimeToChunk(new CrimeT(date, line[3],
					new CoordinateT(Double.parseDouble(line[16]), Double.parseDouble(line[17]))));
		}
		csv.close();
		System.out.println(i);
	}
	/**
	 * Getter method for neighbourhoods
	 * @return ChunkT[] the instance variable neigbourhoods
	 */
	public static ChunkT[] getNeighbourhoods() {
		return neighbourhoods;
	}
	
	/**
	 * Getter method for isInit
	 * @return A boolean value that determines whether Map has been initialized or not.
	 */
	public static boolean isInit() {
		return isInit;
	}
	
	/**
	 * Search for a chunk by name
	 * @return Returns a ChunkT that matches the provided name. Null if no such chunk is found.
	 */
	public static ChunkT getChunk(String name) {
		for(ChunkT C : neighbourhoods) {
			if(C.getName().equals(name)) {
				return C;
			}
		}
		return null;
	}
	
	/**
	 * Method for comparing crime frequency in a given time frame
	 * @param aStart LocalDate the starting time/date
	 * @param aFinal LocalDate the final time/date
	 * @return Iterable<CrimeT> of the crimes commited in the time frame 
	 */
	public static Iterable<CrimeT> compareTimeframe(LocalDate aStart, LocalDate aFinal) {
		ArrayList<CrimeT> mergedCrimes = new ArrayList<>();
		ArrayList<CrimeT> result = new ArrayList<>();
		for(ChunkT C : neighbourhoods) {
			mergedCrimes.addAll(C.getCrimes());
		}
		/* Potential performance increase, (takes more memory) but instead of merging here, 
		 * perhaps we can merge when we read file, creating two separate datastructures */
		mergedCrimes.sort(null); /* Once merged, we have to sort the combined arrays. */
		int AIndex = binarySearch(mergedCrimes, aStart);
		int index = AIndex;
		while(index < mergedCrimes.size() && mergedCrimes.get(index).date().compareTo(aFinal) < 1) {
			/* Keep looping upwards until the current date is larger than the end bound for A */
			result.add(mergedCrimes.get(index));
			index += 1;
		}
		return result;
	}

	/**
	 * Method for adding a crime to a Chunk
	 * @param crime a CrimeT object
	 */
	private static void assignCrimeToChunk(CrimeT crime) {
		for(ChunkT C : neighbourhoods) {
			if(C.isInBounds(crime)) {
				C.addCrime(crime);
				return;
			}
		}
	}
	
	/**
	 * Local Method for Binary Search
	 * @param arr 
	 * @param date
	 * @return
	 */
	private static int binarySearch(ArrayList<CrimeT> arr, LocalDate date) {
		return binarySearch(arr, date, 0, arr.size() - 1);
	}
	/**
	 * Local Method for Binary Search
	 * @param arr
	 * @param date
	 * @param lo
	 * @param hi
	 * @return
	 */
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
	
	class GsonLocation {
    	double lat;
    	double lon;
    }
  
    public static CoordinateT geocode(String address) throws Exception {
    	address = URLEncoder.encode(address, "UTF-8");
    	URL url = new URL("https://us1.locationiq.com/v1/search.php?key=4ce3f13063fb1e&q=" + address + "&format=json");
    	URLConnection conn = url.openConnection();
    	InputStream is = conn.getInputStream();
    	GsonLocation[] results = new Gson().fromJson(new InputStreamReader(is, "UTF-8"), GsonLocation[].class);
    	if (results.length > 0) return new CoordinateT(results[0].lat, results[0].lon);
    	else throw new Exception();
    }
    
    public static List<CrimeT> crimesAroundLocation(CoordinateT location, double radius) {
    	List<CrimeT> crimes = new ArrayList<>();
    	for(ChunkT chunk : neighbourhoods) {
			for (CrimeT crime: chunk.getCrimes()) {
				if (crime.coordinates().distanceTo(location) <= radius) {
					crimes.add(crime);
				}
			}
		}
    	return crimes;
    }
    
    public static String safestPath(String start, String destination) {
    	ShortestPath P = new ShortestPath(new EdgeWeightedDiGraph(), start);
    	String pathTo = "";
    	Stack<DirectedEdge> path = P.pathTo(destination);
		int index = 0;
		while(! path.isEmpty()) {
			DirectedEdge connection = path.pop();
			if(index == 0) {
				pathTo += connection.from() + ": --> " + connection.to();
				index++;
			}
			else {
				pathTo += " --> " + connection.to();
			}
		}
    	return pathTo;
    }
	
	

}
