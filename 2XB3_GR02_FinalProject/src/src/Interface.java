package src; 

import java.io.IOException;
import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.Scanner;

import src.Map;
public class Interface {
	static Scanner scan;

    public static void main(String[] args) throws IOException, InterruptedException {
    	printLogo();
        Map.init();
    	while(true) {
        	scan = new Scanner(System.in);
    		System.out.println("Please select an option and press enter: ");
    		System.out.println("1) Safest neighbourhoods");
    		System.out.println("2) Frequently occuring crimes");
    		System.out.println("3) Crimes within a timeframe");
    		System.out.println("4) Crimes at a specific location");
    		System.out.println("5) Safest path");
    		System.out.println("6) Exit");
    		System.out.print("\nChoice: ");
    		
    		int option = getUserOption();
    		
    		switch(option) {
    			case 1:
    				System.out.println("Would you like to sort by:");
    				System.out.println("1) Safest");
    				System.out.println("2) Most dangerous");
    				
    				int optionTwo = getUserOption();
    				
    				switch(optionTwo) {
    					case 1:
    						displayBureauSafety(false);
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    					case 2:
    						displayBureauSafety(true);
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    					default:
    						System.out.println("You did not choose a valid option");
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    				}
    				break;
    				
    			case 2:
    				System.out.println("Would you like to sort by:");
    				System.out.println("1) Most frequent occuring");
    				System.out.println("2) Least frequent occuring");
    				
    				optionTwo = getUserOption();
    				
    				switch(optionTwo) {
    					case 1:
    						getTotalCrimeStats(true);
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    						
    					case 2:
    						getTotalCrimeStats(false);
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    						
    					default:
    						System.out.println("You did not choose a valid option");
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    				}
    				break;
    				
    			case 3:
    				System.out.println("Please enter the start and end date of the time frame following the format below:");
    				System.out.println("year-month-date,year-month-date.  Ex. 2018-01-31,2020-01-31");
    				String[] dates = scan.nextLine().split(",");
    				LocalDate a = null;
    				LocalDate b = null;
    				try {
    					a = LocalDate.parse(dates[0]);
    					b = LocalDate.parse(dates[1]);
    				}
    				catch(Exception e) {
    					System.out.println("Invalid date format");
    					break;
    				}
    				
    				System.out.println("Would you like to sort by:");
    				System.out.println("1) Most frequent occuring");
    				System.out.println("2) Least frequent occuring");
    				
    				optionTwo = getUserOption();
    				
    				switch(optionTwo) {
    					case 1:
    						getFrequencyByDate(true, a, b);
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    						
    					case 2:
    						getFrequencyByDate(false, a, b);
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    						
    					default:
    						System.out.println("You did not choose a valid option");
    						System.out.println("\n\n");
    						Thread.sleep(2000);
    						break;
    				}
    				break;
    				
    			case 4:
    				System.out.println("Enter a location: (ex. \"Hunt's Point, New York City\", \"1435 Broadway, New York, NY 10018\")");
    				String address = scan.nextLine();
    				System.out.println("Enter a radius (km): ");
    				int radius = scan.nextInt();
    				getLocationCrimeStats(address, radius);
    				Thread.sleep(2000);
    				break;
    				
    			case 5:
    				System.out.println("Please enter a starting neighbourhood and destination in the format \"start,end\"");
    				System.out.println("Options: [Manhattan, Brooklyn, Staten Island, Bronx, Queens]");
    				String choice = scan.nextLine().toUpperCase();
    				String[] options = choice.split(",");
    				String path = Map.safestPath(options[0], options[1]);
    				System.out.println(path);
    				Thread.sleep(2000);
    				break;
    				
    				
    			case 6:
    				System.exit(0);
    				
    			default:
    				break;
    		}
    			
    	}
        
    }
    /**
     * Method for ranking the safest bureaus in NYC.
     * @param safety
     */
    public static void displayBureauSafety(boolean safety) {
    	int counter = 1;
        Iterable<String> bureaus = Map.getFrequency(safety);
    	for (String bureau : bureaus) {
    		System.out.println(counter + ": " + bureau );
    		counter++;
    	}
    }
    
    public static void crimeStatsbyNeigbourhood(boolean safety, String neighbourhood) {
    	if (!validNeighbourHood(neighbourhood)){
            throw new IllegalArgumentException("The provided neighbourhood is not defined");
        }
    	Map.getCrimeStatistics(safety, neighbourhood);
    }
    
    public static void getTotalCrimeStats(boolean max) {
    	NavigableMap<String, Integer> i = Map.getCrimeStatistics(max);
    	int j = 0;
    	for(String s : i.keySet()) {
    		System.out.println((j+1) + ": " + s + " (" + i.get(s) + ")");
    		j++;
        	if (j == 10) {
        		System.out.print("Show more? (y/n): ");
        		String in = scan.next();
        		if (in.equals("n")) break;
        	}
    	}
    }
    
    public static void getLocationCrimeStats(String address, int radius) {
    	CoordinateT location;
    	try {
    		location = Map.geocode(address);
    	}
    	catch (Exception e) {
    		System.out.println("Could not find location.");
    		return;
    	}
    	NavigableMap<String, Integer> crimes = Map.getCrimeStatistics(true, Map.crimesAroundLocation(location, radius));
    	if (crimes.isEmpty()) {
    		System.out.println("\nNo crimes recorded within radius.\n");
    		return;
    	}
		System.out.println("\nThere have been " + crimes.values().stream().reduce(0, Integer::sum) + " crimes recorded within a " + radius + "km radius.");
		System.out.println("Most frequent crimes: ");
    	int j = 0;
    	for(String s : crimes.keySet()) {
    		System.out.println((j+1) + ": " + s + " (" + crimes.get(s) + ")");
    		j++;
    		if (j == 5) {
        		System.out.print("Show more? (y/n): ");
        		String in = scan.next();
        		if (in.equals("n")) break;
        	}
    	}
    	System.out.println("\n");
    }
    
    public static void getFrequencyByDate(boolean max, LocalDate start, LocalDate end) throws InterruptedException {
    	Iterable<CrimeT> i = Map.compareTimeframe(start, end);
    	NavigableMap<String, Integer> result = Map.getCrimeStatistics(max, i);
    	int j = 0;
    	for(String s : result.keySet()) {
    		System.out.println((j+1) + ": " + s + " (" + result.get(s) + ")");
    		j++;
    		if(j > 5) break;
    	}
    }
    
    private static int getUserOption() {
    	int result = -1;
    	while(result == -1) {
    		try {
    			result = scan.nextInt();
    			scan.nextLine();
    		}
    		catch (Exception e) {
    			System.out.println("Please choose a valid option");
    		}
    	}
    	return result;
    }
    
   
    public static boolean validNeighbourHood(String neighbourhood){
        boolean isValid = false;
        for (ChunkT chunk : Map.getNeighbourhoods()){
            if (chunk.getName().equals(neighbourhood)){
                isValid = true;
            }
        }
        return isValid;
    }
    
    public static void printLogo() {
    	System.out.println("\r\n" + 
    			"  ____    _    _____ _____ _______   __  _   _ _____ _____ \r\n" + 
    			" / ___|  / \\  |  ___| ____|_   _\\ \\ / / | \\ | | ____|_   _|\r\n" + 
    			" \\___ \\ / _ \\ | |_  |  _|   | |  \\ V /  |  \\| |  _|   | |  \r\n" + 
    			"  ___) / ___ \\|  _| | |___  | |   | |   | |\\  | |___  | |  \r\n" + 
    			" |____/_/   \\_|_|   |_____| |_|   |_|   |_| \\_|_____| |_|  \r\n" + 
    			"                                                           \r\n");
    }
    
}