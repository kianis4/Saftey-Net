package src;
/** An ADT for representing crimes committed. */

import java.time.LocalDate;

public class CrimeT implements Comparable<CrimeT> {

    private final LocalDate date;
    private final String description;
    private final CoordinateT coordinates;
    
    /**
     * Constructor for CrimeT object
     * @param d LocalDate object, the date of the crime
     * @param desc String, description of the crime
     * @param cords CoordinateT object, coordinate of the crime
     */
    public CrimeT(LocalDate d, String desc, CoordinateT cords) {
        date = d;
        description = desc;
        coordinates = cords;
    }
    /**
     * Getter method for date of crime
     * @return LocalDate the date of the crime
     */
    public LocalDate date() {
        return this.date;
    }
    /**
     * Getter method for description of the crime
     * @return String description of the crime
     */
    public String description() {
        return this.description;
    }
    /**
     * Getter method for coordinates of the crime
     * @return CoordinateT the coordinates
     */
    public CoordinateT coordinates() {
        return this.coordinates;
    }
    /**
     * Method for comparing the date of 2 crimes
     * @return int, positive value if larger, negative if smaller
     */
    public int compareTo(CrimeT other) {
    	return this.date().compareTo(other.date());
    }


}