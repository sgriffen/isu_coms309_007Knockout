package application.tools.embeddables;

import java.util.Random;
import javax.persistence.*;
import org.springframework.core.style.ToStringCreator;

/**
 * Tokens used for communicating to the client side
 * @author Sean Griffen
 */
@Embeddable
public class Token {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Random {@code String} of characters
	 */
	private String authenticator;
	
	/**
	 * {@code Date} when to expire in milliseconds
	 */
	private long expiration;
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/******************************************************************* START CONSTRUCTORS ******************************************************************/
	
	/**
	 * Default constructor for {@code Token}
	 */
	public Token() {
		
		setExpiration(System.currentTimeMillis() + (60 * 1000 * 60 * 12));
		setAuthenticator(null);
	}
	
	/**
	 * Constructs a {@code Token} based on an inputed {@code String[]}
	 * @param s
	 * 		{@code String[]} that this {@code Token} will be based off of
	 * @param time
	 * 		{@code time} in hours to expire from now
	 */
	public Token(String[] s, int time) {
		
		this();
		setExpiration(System.currentTimeMillis() + (60 * 1000 * 60 * time));
		setAuthenticator(generateAuthenticator(s));
	}
	
	/******************************************************************** END CONSTRUCTORS *******************************************************************/
	
	/***************************************************************** START GETTERS/SETTERS *****************************************************************/
	
	/**
	 * Get {@code authenticator} of the {@code Token}
	 * @return
	 * 		{@code authenticator}
	 */
	public String getAuthenticator() { return authenticator; }
	/**
	 * Set {@code authenticator} of the {@code Token}
	 * @param authenticator
	 * 		Desired {@code authenticator} for the {@code Token}
	 */
	public void setAuthenticator(String authenticator) { this.authenticator = authenticator; }
	
	/**
	 * Get {@code expiration} of the {@code Token}
	 * @return
	 * 		{@code expiration}
	 */
	public long getExpiration() { return expiration; }
	/**
	 * Set {@code expiration} of the {@code Token}
	 * @param expiration
	 * 		Desired {@code expiration} for the {@code Token}
	 */
	public void setExpiration(long expiration) { this.expiration = expiration; }
	
	/******************************************************************* END GETTERS/SETTERS *****************************************************************/
	
	/******************************************************************** START MISC METHODS *****************************************************************/
	
	/**
	 * Checks if the {@code Token} is expired or not
	 * @return
	 * 		{@code true} if not expired, {@code false} otherwise
	 */
	public boolean isValid() {
		
		if (expiration < System.currentTimeMillis()) { return false; }
		return true;
	}
	
	/**
	 * Generates a random {@code authenticator} for the {@code Token}
	 * @param s
	 * 		{@code String[]} to base the {@code authenticator} off of
	 * @return
	 * 		{@code authenticator} for the {@code Token}
	 */
	public String generateAuthenticator(String[] s) {
		
		int pow = 0;
		
		int parts[] = new int[s.length];
		for (int i = 0; i < s.length; i++) { parts[i] = stringToInt(s[i]); }
		
		if (s.length > 1) {
			
			for (int i = 1; i < s.length - 1; i++) { pow += Math.pow(parts[i - 1], parts[i]); }
		} else { pow = (int) Math.pow(parts[0], 2); }
		
		//Get a random int value for the authenticator
		long keyValue =  pow * randNum(Integer.MAX_VALUE);
		//Create the random string of characters
		String authenticator = createAuthenticator(keyValue);
		while (authenticator.contains("#") || authenticator.contains("%") || authenticator.contains(",") || authenticator.contains("/") || 
				authenticator.contains("?") || authenticator.contains("[") || authenticator.contains("\\") || authenticator.contains("]") || 
				authenticator.contains("{") || authenticator.contains("}") || authenticator.contains(";"))
					{ authenticator = createAuthenticator(keyValue); }
		//Check the length
		String prev = authenticator;
		authenticator = checkAuthenticator(authenticator);
		while (authenticator.contains("#") || authenticator.contains("%") || authenticator.contains(",") || authenticator.contains("/") || 
				authenticator.contains("?") || authenticator.contains("[") || authenticator.contains("\\") || authenticator.contains("]") || 
				authenticator.contains("{") || authenticator.contains("}") || authenticator.contains(";"))
					{ authenticator = checkAuthenticator(prev); }
		
		return authenticator;
	}
	
	/**
	 * Helper method for {@code generateAuthenticator}. Generates a random {@code int} given a range
	 * @param range
	 * 		Range to generate {@code int} between
	 * @return
	 * 		Random {@code int}
	 */
	private int randNum(int range) {
		
		Random rand = new Random();
		//Generate random number between 0 (inclusive) and range (exclusive)
		return rand.nextInt(range);
	}
	
	/**
	 * Helper method for {@code generateAuthenticator}. Converts a {@code String} to an {@code int}
	 * @param toInt
	 * 		{@code String} to convert to int
	 * @return
	 * 		{@code int} version of {@code toInt}
	 */
	private int stringToInt(String toInt) {
		
		//Convert string to char[]
		char[] string = toInt.toCharArray();
		int result = 1;
		
		//Convert char[] to a double value
		for (int i = 0; i < string.length; i++) { result += (int) string[i]; }
		
		return result;
	}
	
	/**
	 * Helper method for {@code generateAuthenticator}. Creates a {@code Token.authenticator} based on an inputed {@code int}
	 * @param keyInt
	 * 		{@code int} value of the {@code Token.authenticator}
	 * @return
	 * 		{@code Token.authenticator}
	 */
	private String createAuthenticator(long keyInt) {
		
		//Convert int to string
		String keyLength = String.valueOf(keyInt);
		//Get highest power of 10 in keyInt
		int power = keyLength.length();
		
		String authenticator = "";
		
		for (int i = power; i > 0; i--) {
			
			//Get number at 10^i
			int num = (int) ((keyInt % Math.pow(10, i)) / Math.pow(10, i - 1));
			//ASCII value of character to add to authenticator from 33 to 117 + number at 10^i of keyInt
			int asciiValue = randNum(85) + 33 + num;
			//Char to add to authenticator cannot be a period, back slash, question mark, forward slash, left bracket, right bracket, left curly brace, right curly brace, or a percent sign
			while ( asciiValue == 35 || asciiValue == 37 || asciiValue == 44 || asciiValue == 46 || asciiValue == 47 || asciiValue == 59 || 
					asciiValue == 63 || asciiValue == 91 || asciiValue == 92 || asciiValue == 93 || asciiValue == 123 || asciiValue == 125)
						{ asciiValue = randNum(85) + 33 + num; }
			
			//Append random character to authenticator
			authenticator += (char) asciiValue;
		}
		authenticator += '.';
		return authenticator;
	}
	
	/**
	 * Helper method for {@code generateAuthenticator}. Checks if the {@code Token.authenticator} length is at least 20. If not, appends {@code char}s until it is
	 * @param authenticator
	 * 		{@code Token.authenticator} to check
	 * @return
	 * 		{@code Token.authenticator} with a length of at least 20
	 */
	private String checkAuthenticator(String authenticator) {
		
		//If authenticator.length() is less than to, append characters
		while (authenticator.length() < 20) {
			
			//Generate random ascii value of char
			int toAdd = randNum(94) + 33;
			//Char to add to authenticator cannot be a period, back slash, question mark, forward slash, left bracket, right bracket, left curly brace, right curly brace, or a percent
			while ( toAdd == 35 || toAdd == 37 || toAdd == 44 || toAdd == 46 || toAdd == 47 || toAdd == 59 || toAdd == 63 ||  toAdd == 91 || 
					toAdd == 92 || toAdd == 93 || toAdd == 123 || toAdd == 125) 
						{ toAdd = randNum(94) + 33; }
			
			authenticator += (char) toAdd;
		}
		
		//If authenticator.length() is not divisible by 10
		while (authenticator.length() % 10 != 0) {
			
			//Generate random ascii value of char
			int toAdd = randNum(94) + 33;
			//Char to add to authenticator cannot be a period, back slash, question mark, forward slash, left bracket, right bracket, left curly brace, right curly brace, or a percent
			while ( toAdd == 35 || toAdd == 37 || toAdd == 44 || toAdd == 46 || toAdd == 47|| toAdd == 59 || toAdd == 63 ||  toAdd == 91 || 
					toAdd == 92 || toAdd == 93 || toAdd == 123 || toAdd == 125)
						{ toAdd = randNum(94) + 33; }
			
			authenticator += (char) toAdd;
		}
		
		return authenticator;
	}
	
	/**
	 * Converts {@code Token} to {@code String}
	 */
	@Override
	public String toString() {
		
		return new ToStringCreator(this)
				
				.append("Authenticator", this.getAuthenticator())
				.append("Expiration Date", this.getExpiration())
				.toString();
	}
	
	/********************************************************************* END MISC METHODS ******************************************************************/
	
	/********************************************************************** END CLASS USER *******************************************************************/
}
