package application.tools.embeddables;

import javax.persistence.*;
import org.springframework.core.style.ToStringCreator;

/**
 * Location object for objects that need them
 * @author Sean Griffen
 */
@Embeddable
public class Location {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Latitude {@code Location} component of the {@code User}
	 */
	private double latitude;
	
	/**
	 * Longitude {@code Location} component of the {@code User}
	 */
	private double longitude;
	
	/**
	 * {@code Location} accuracy component of the {@code User}
	 */
	private int accuracy;
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/******************************************************************* START CONSTRUCTORS ******************************************************************/
	
	/**
	 * Default constructor for Location
	 */
	public Location() { 
		
		setLatitude(0);
		setLongitude(0);
		setAccuracy(0);
	}
	
	/**
	 * Create the {@code Location} of the {@code User}
	 * @param latitude
	 * 		Desired {@code latitude} component of the {@code Location}
	 * @param longitude
	 * 		Desired {@code longitude} component of the {@code Location}
	 * @param accuracy
	 * 		Desired {@code accuracy} component of the {@code Location}
	 */
	public Location(double latitude, double longitude, int accuracy) {
		
		setLatitude(latitude);
		setLongitude(longitude);
		setAccuracy(accuracy);
	}
	
	/******************************************************************** END CONSTRUCTORS *******************************************************************/
	
	/***************************************************************** START GETTERS/SETTERS *****************************************************************/
	
	/**
	 * Get {@code latitude} component of the {@code Location}
	 * @return
	 * 		{@code latitude}
	 */
	public double getLatitude() { return latitude; }
	/**
	 * Set {@code latitude} component of the {@code Location}
	 * Update reference in {@code User}
	 * @param latitude
	 * 		Desired {@code latitude} component of the {@code Location}
	 */
	public void setLatitude(double latitude) { this.latitude = latitude; }
	
	/**
	 * Get {@code longitude} component of the {@code Location}
	 * @return
	 * 		{@code longitude}
	 */
	public double getLongitude() { return longitude; }
	/**
	 * Set {@code longitude} component of the {@code Location}
	 * Update reference in {@code User}
	 * @param longitude
	 * 		Desired {@code longitude} component of the {@code Location}
	 */
	public void setLongitude(double longitude) { this.longitude = longitude; }
	
	/**
	 * Update (@code Location) by coping values in the param
	 * @param location
	 * 		Desired {@code location}
	 */
	public void setLocation(Location location) {
		this.longitude = location.longitude;
		this.latitude  = location.latitude;
	}
	
	/**
	 * Get {@code accuracy} component of the {@code Location}. 
	 * @return
	 * 		{@code accuracy}
	 */
	public int getAccuracy() { return accuracy; }
	/**
	 * Set {@code accuracy} component of the {@code Location}
	 * Update reference in {@code User}
	 * @param accuracy
	 * 		Desired {@code accuracy} component of the {@code Location}
	 */
	public void setAccuracy(int accuracy) { this.accuracy = accuracy; }
		
	/******************************************************************* END GETTERS/SETTERS *****************************************************************/
	
	/******************************************************************** START MISC METHODS *****************************************************************/
	
	/**
	 * Calculates distance between this and another location object in meters using the haversine formula
	 * @param location other location to use
	 * @return distance between this location and another in meters
	 */
	public double getDistance(Location location) {
		//d=2*asin(sqrt((sin((lat1-lat2)/2))^2 + cos(lat1)*cos(lat2)*(sin((lon1-lon2)/2))^2))
		//formula using no idea if it works or not
		
		//Radius of the Earth in meters
		final int R = 6371000;
		
		//Square of half the chord length between points
		double a = Math.pow(Math.sin((Math.toRadians(location.latitude - latitude)) / 2), 2) + 
				Math.cos(Math.toRadians(latitude))* Math.cos(Math.toRadians(location.latitude)) * 
				Math.pow((Math.sin((Math.toRadians(location.longitude - longitude)) / 2)), 2);
		
		//Angular distance in radians
		double c = 2 * (Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
		
		return R * c;
	}
	
	/**
	 * Converts {@code Location} to {@code String}
	 */
	@Override
	public String toString() {
		
		return new ToStringCreator(this)
				
				.append("Latitude", this.getLatitude())
				.append("Longitude", this.getLongitude())
				.append("Accuracy", this.getAccuracy())
				.toString();
	}
	
	/********************************************************************* END MISC METHODS ******************************************************************/
	
	/******************************************************************** END CLASS LOCATION *****************************************************************/
}
