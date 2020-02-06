package application.tools.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.exceptions.InvalidAdministratorException;
import application.exceptions.InvalidListException;
import application.exceptions.InvalidTokenException;
import application.exceptions.InvalidUserException;
import application.items.ItemInterface;
import application.items.ItemRepository;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.tools.wrappers.InventoryUpdateWrapper;
import application.tools.wrappers.LocationUpdateWrapper;
import application.tools.wrappers.StatisticUpdateWrapper;
import application.tools.wrappers.UserUpdateWrapper;
import application.users.User;
import application.users.UserInterface;
import application.users.UserRepository;

/**
 * Service for {@code UserController}
 * 
 * @author Sean Griffen
 */
@Service
public class UserService {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * {@code UserRepository} accessed by this service
	 */
	@Autowired
	private UserRepository uRepo;
	/**
	 * {@code ItemRepository} accessed by this service
	 */
	@Autowired
	private ItemRepository iRepo;
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/******************************************************************* START CONTRUCTORS *******************************************************************/
	
	/**
	 * Constructs a {@code UserService} object with repository object {@code UserRepository}
	 * @param uRepo
	 * 		{@code UserRepository} object desired
	 * @param iRepo
	 * 		{@code ItemRepository} object desired
	 */
	public UserService (UserRepository uRepo, ItemRepository iRepo) {
		
		this.uRepo = uRepo;
		this.iRepo = iRepo;
	}
	
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/******************************************************************* START GET HELPERS *******************************************************************/
	
	
	/******************************************************************** END GET HELPERS ********************************************************************/
	
	/****************************************************************** START POST HELPERS *******************************************************************/
	
	/**
	 * Adds a {@code User} to the database
	 * @param toAdd
	 * 		{@code User} to add
	 * @return
	 * 		String if successful
	 * @throws InvalidUserException
	 * 		Throws if {@code toAdd} is null, or if {@code username} already exists in database
	 */
	public String addUser(UserInterface toAdd) throws InvalidUserException {
		
		//Check if user to add is null or empty
		if (toAdd == null) { throw new InvalidUserException("Cannot add null User"); }
		if (toAdd.isEmpty()) { throw new InvalidUserException("Username and/or password is either null and/or blank"); }
		//Check if name desired already exists
		if (!checkName(toAdd)) { throw new InvalidUserException("Username '" + toAdd.getUsername() +"' already exists."); }
		
		//Save that user to the database
		uRepo.save((User) toAdd);
		
		return "User '" + toAdd.getUsername() +"' added to database with authorization level of 'Player'";
	}
	
	/******************************************************************* END POST HELPERS ********************************************************************/
	
	/******************************************************************* START PUT HELPERS *******************************************************************/
	
	/**
	 * Returns a user based on the inputed {@code Token}
	 * @param token
	 * 		{@code token} of the {@code User} desired
	 * @return
	 * 		{@code User} with ID {@code id}
	 * @throws InvalidTokenException 
	 * 		Throws if {@code token} is null
	 */
	public UserInterface getUser(Token token) throws InvalidTokenException {
		
		validateToken(token);
		
		//Get ID of user associated with this token
		UserInterface user = this.getTokensUser(token);
		//If yes, then return user with ID
		return user;
	}
	
	public Set<Token> getSessionToken(Token token) throws InvalidTokenException {
		
		validateToken(token);
		
		UserInterface user = this.getTokensUser(token);
		//If yes, then return Session Token List
		return user.getSession();
	}
	
	public void setTimeUpdated(Token toUpdate, long timeUpdated) throws InvalidTokenException {
		
		UserInterface u = getUser(toUpdate);
		
		u.setTimeUpdated(timeUpdated);
		uRepo.save((User) u);
	}
	
	public long getTimeUpdated(Token toGet) throws InvalidTokenException {
		
		return getUser(toGet).getTimeUpdated();
	}
	
	/**
	 * Returns all users in the database if {@code token} is associated with an admin
	 * @param token
	 * 		{@code Token} of the {@code User} with {@code authLevel} = 2
	 * @return
	 * 		{@code List} of {@code User} objects
	 * @throws InvalidTokenException
	 * 		Throws if {@code token} is not a valid {@code Token} in the database
	 * @throws InvalidUserException 
	 * 		Throws if {@code token}'s user is null
	 * @throws InvalidAdministratorException 
	 * 		Throws if {@code token}'s {@code User}'s authLevel is less than 2
	 */
	public List<UserInterface> getUsers(Token token) throws InvalidTokenException, InvalidUserException, InvalidAdministratorException {
		
		//Check if token belongs to an administrator. If yes, return
		if(validateAdmin(getUser(token))) {
			
			List<User> inRepo = uRepo.findAll();
			List<UserInterface> result = new ArrayList<>();
			
			for (UserInterface u : inRepo) { result.add((UserInterface) u); }
			return result;
		}
		//Else, return null
		return null;
	}
	
	/**
	 * Gets the {@code Token} of a given {@code User}
	 * @param user
	 * 		{@code User} who's {@code Token} is desired
	 * @return
	 * 		{@code Token} of {@code user}
	 * @throws InvalidUserException 
	 * 		Throws if {@code user} is null or 
	 */
	public Token getToken(UserInterface user) throws InvalidUserException {
		
		//Check if user exists in database
		user = validateUser(user);
		
		//Generate new token for user
		generateNewToken(user);
		
		//Return token
		return user.getToken();
	}
	
	/**
	 * Updates the username, password, and authLevel of a User
	 * @param wrap
	 * 		See {@code UpdateUserWrapper}
	 * @return
	 * 		String stating which User was updated, and what was Updated, if anything
	 * @throws InvalidUserException
	 * 		Throws if User trying to update doesn't exist.
	 * 		Throws if User updating to is null
	 * 		Throws if User updating to contains a username that already exists
	 * @throws InvalidAdministratorException
	 * 		Throws if User with authLevel = 2 is not valid
	 * @throws InvalidTokenException
	 * 		Throws if Token does not correspond to an existing User
	 * @throws InvalidListException
	 * 		Throws if List is not in expected format
	 */
	public String updateUserInfo(UserUpdateWrapper wrap) throws InvalidUserException, InvalidAdministratorException, InvalidTokenException, InvalidListException {
		
		//Get User to update, and User updating to
		UserInterface toUpdate = this.getUser(wrap.getToUpdate());
		UserInterface updateTo = wrap.getUpdateTo();
		
		//Check if user to update to is empty
		if (updateTo == null || updateTo.getUsername() == null && updateTo.getPassword() == null && updateTo.getAuthLevel() == 0) 
			{ throw new InvalidUserException("Cannot update user to null user"); }
		
		if (updateTo.getUsername() != null) {
			
			if (updateTo.getUsername().trim().isEmpty()) { throw new InvalidUserException("Cannot update user to empty user"); }
			if (!this.checkName(updateTo)) { throw new InvalidUserException("Username '" + updateTo.getUsername() +"' already exists."); }
		}
		if (updateTo.getPassword() != null) {
			if (updateTo.getPassword().trim().isEmpty()) { throw new InvalidUserException("Cannot update user to empty user"); }
		}
		
		String oldName = toUpdate.getUsername();
		//Make default message
		String result = "User '" + toUpdate.getUsername() + "'s' info was not updated";
		
		boolean updated = false;
		if (updateTo.getAuthLevel() > 0) {
			
			if (validateAdmin(this.getUser(wrap.getAdmin()))) {
				
				toUpdate.setAuthLevel(updateTo.getAuthLevel());
				updated = true;
			}
		}
		if (toUpdate.updateInfo(updateTo)) { updated = true; }
		if (updated) {
			
			uRepo.save((User) toUpdate);
			result = "User '" + oldName + "'s' username, password, and/or authority level updated";
		}
		return result;
	}
	
	/**
	 * Updates a {@code User}'s location
	 * @param wrap
	 * 		See {@code LocationUpdateWrapper}
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidUserException
	 * 		Throws if {@code User} trying to update doesn't exist
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 */
	public String updateUserLocation(LocationUpdateWrapper wrap) throws InvalidUserException, InvalidTokenException {
		
		UserInterface user = this.getUser(wrap.getToUpdate());
		Location l = wrap.getNewLocation();
		
		user.setLocation(l);
		//Save changes
		uRepo.save((User) user);
		return "Updated User '" + user.getUsername() + "' Location to " + l.toString();
	}
	
	/**
	 * Updates the statistics (the {@code level}, {@code kills}, or {@code deaths} of a {@code User}
	 * @param wrap
	 * 		See {@code StatisticUpdateWrapper}
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidUserException
	 * 		Throws if {@code User} trying to update doesn't exist
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidListException
	 * 		Shouldn't throw if above {@code Exception}s weren't thrown
	 */
	public String updateUserStatistics(StatisticUpdateWrapper wrap) throws InvalidUserException, InvalidTokenException, InvalidListException {
		
		UserInterface user = this.getUser(wrap.getToUpdate());
		Integer newLevel = wrap.getLevel();
		Integer newKills = wrap.getKills();
		Integer newDeaths = wrap.getDeaths();
		
		if (newLevel == null && newKills == null && newDeaths == null) { throw new InvalidListException("No statistics to change"); }
		
		if (newLevel != null) { user.setLevel(newLevel); }
		if (newKills != null) { user.setKills(newKills); }
		if (newDeaths != null) { user.setDeaths(newDeaths); }
		//Save changes
		uRepo.save((User) user);
		
		return "User '" + user.getUsername() + "' updated with new level, kills, and/or deaths";
	}
	
	/**
	 * Updates the {@code items} of a {@code User}
	 * @param wrap
	 * 		See {@code InventoryUpdateWrapper}
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidUserException
	 * 		Throws if {@code User} trying to update doesn't exist
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidListException
	 * 		Shouldn't throw if above {@code Exception}s weren't thrown
	 */
	public String updateUserInventory(InventoryUpdateWrapper wrap) throws InvalidUserException, InvalidTokenException, InvalidListException {
		
		UserInterface user = this.getUser(wrap.getToUpdate());
		int[] newItems = wrap.getItems();
		
		Set<ItemInterface> newerItems = new HashSet<>();
		
		for (int i = 1; i < newItems.length; i++) {
			
			ItemInterface it = iRepo.findById(newItems[i]).get();
			
			if (it == null) { throw new InvalidListException("Item in list does not exist."); }
			
			newerItems.add(iRepo.findById(newItems[i]).get());
		}
		
		user.setItems(newerItems);
		//Save changes
		uRepo.save((User) user);
		
		return "User " + user.getUsername() + "'s' inventory updated";
	}
	
	/**
	 * Updates the {@code target} of a {@code User}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code User} to update, and the {@code Token}s of the {@code target} to set
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidListException
	 * 		Throws if {@code tokens} was not in expected format
	 * @throws InvalidUserException
	 * 		Throws if {@code User} trying to update does not exist
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 */
	public String updateUserTargets(List<Token> tokens) throws InvalidListException, InvalidUserException, InvalidTokenException {
		
		if (tokens.size() < 2) { throw new InvalidListException("List must contain user to update, and at least 1 token"); }
		
		UserInterface user = this.getUser(tokens.get(0));
		List<UserInterface> targets = new ArrayList<>();
		
		for (int i = 1; i < tokens.size(); i++) {
			
			if (this.getUser(tokens.get(i)).equals(user)) { throw new InvalidUserException("Cannot set User to target itself"); }
			
			targets.add(this.getUser(tokens.get(i)));
		}
		
		user.setTargets(targets);
		//Save changes
		uRepo.save((User) user);
		
		return "User '" + user.getUsername() + " aquired new targets";
	}
	
	/**
	 * Deletes a {@code User}'s {@code Token} based on an inputed {@code Token}
	 * @param token
	 * 		{@code Token} to delete
	 * @return
	 * 		String stating success or not
	 * @throws InvalidUserException
	 * 		Throws if {@code token}'s {@code User} is null or nonexistent
	 * @throws InvalidTokenException 
	 * 		Throws if {@code token} is null
	 */
	public String deleteToken(Token token) throws InvalidTokenException, InvalidUserException {
		
		UserInterface user = this.getUser(token);
		
		user.setToken(new Token());
		
		uRepo.save((User) user);
		return "User '" + user.getUsername() + "' logged out";
	}
	
	/******************************************************************** END PUT HELPERS ********************************************************************/
	
	/***************************************************************** START DELETE HELPERS ******************************************************************/
	
	/**
	 * Deletes a {@code User} from the database based on an inputed {@code Token}
	 * @param token
	 * 		{@code Token} who's {@code User} desired to be deleted from database
	 * @return
	 * 		{@code String} saying if was deleted or not
	 * @throws InvalidUserException
	 * 		Throws if {@code User} associated with {@code token} is null or nonexistent
	 * @throws InvalidTokenException
	 * 		Throws if {@code token} is null
	 */
	public String deleteUser(Token token) throws InvalidTokenException, InvalidUserException {
		
		UserInterface delete = this.getUser(token);
		
		uRepo.deleteById(delete.getId());
		return "User '" + delete.getUsername() + "' removed from database";
	}
	
	/****************************************************************** END DELETE HELPERS *******************************************************************/
	
	/***************************************************************** START PRIVATE METHODS *****************************************************************/
	
	/**
	 * 
	 * Validates a passed {@code User} by checking if it exists in the database
	 * @param toValidate
	 * 		{@code User} to validate
	 * @return
	 * 		{@code true} if valid, throws {@code InvalidUserException} otherwise
	 * @throws InvalidUserException
	 * 		Throws if {@code toValidate} is null, empty, or does not exist in the database
	 */
	private UserInterface validateUser(UserInterface toValidate) throws InvalidUserException {
		
		if (toValidate == null) { throw new InvalidUserException("User entered is either null or empty"); }
		
		List<User> users = uRepo.findAll();
		
		//Get user in database with matching name and password as toValidate
		for (UserInterface u : users) {
			
			if (toValidate.getUsername().equals(u.getUsername()) && toValidate.getPassword().equals(u.getPassword())) { toValidate = u; }
		}
		if (toValidate.getId() == 0) { throw new InvalidUserException("User '" + toValidate.getUsername() + "' does not have a matching username and/or password in the database"); }
		else { return toValidate; }
	}
	
	/**
	 * Checks to see if a {@code User} being used as an administrator is allowed to (I.E. if {@code User} has an {@code authLevel} = 2)
	 * @param toValidate
	 * 		{@code User} to check
	 * @return
	 * 		{@code true} if {@code toValidate} is validated, {@code false} otherwise
	 * @throws InvalidUserException
	 * 		Throws if {@code toValidata} is null or empty
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code toValidate} does not have an {@code authLevel} = 2
	 */
	private boolean validateAdmin(UserInterface toValidate) throws InvalidUserException, InvalidAdministratorException{
		
		if (toValidate == null || toValidate.isEmpty()) { throw new InvalidUserException("User entered is either null or empty"); }
		
		List<User> users = uRepo.findAll();
		List<UserInterface> admins = new ArrayList<>();
		
		for (UserInterface u : users) {
			
			if (u.getAuthLevel() == 2) { admins.add(u); }
		}
		for (UserInterface u : admins) {
			
			if (toValidate.equal(u)) { return true; }
		}
		
		throw new InvalidAdministratorException("Administrator entered is not valid. Check username, password, and/or id");
	}
	
	/**
	 * Checks if a {@code Token} is expired or not. If it is, this updates the {@code User} associated with this {@code Token} with a new {@code Token} and saves the change to the database
	 * @param toValidate
	 * 		{@code Token} to validate
	 * @throws InvalidTokenException
	 * 		Throws if {@code toValidate} is null
	 */
	private void validateToken(Token toValidate) throws InvalidTokenException {
		
		//Can't do anything if token is null
		if (toValidate == null || toValidate.getAuthenticator() == null) { throw new InvalidTokenException("Token entered is null"); }
		
		//If token is expired, create a new token, throw exception
		if (!toValidate.isValid()) { 
			
			getTokensUser(toValidate).setToken(new Token());
			uRepo.save((User) getTokensUser(toValidate));
			throw new InvalidTokenException("Token entered is expired");
		}
	}
	
	/**
	 * Helper method for getUser() validateToken(). Returns the {@code User} associated with a {@code Token}
	 * @param token
	 * 		{@Code Token}
	 * @return
	 * 		{@code User} associated with {@code token}
	 * @throws InvalidTokenException 
	 * 		
	 */
	private UserInterface getTokensUser(Token token) throws InvalidTokenException {
		
		List<User> users = uRepo.findAll();
		
		for (UserInterface u : users) {
			
			if (token.getAuthenticator().equals(u.getToken().getAuthenticator())) { return u; }
		}
		
		throw new InvalidTokenException("Token entered does not correspond to an existing User");
	}
	
	/**
	 * Generates a new {@code Token} for a given {@code User}
	 * @param user
	 * 		Desired {@code User} to generate a {@code Token} for
	 */
	private void generateNewToken(UserInterface user) {
		
		String[] s = { user.getUsername(), user.getPassword() };
		user.setToken(new Token(s, 24));
		uRepo.save((User) user);
	}
	
	/**
	 * Checks a {@code User}'s name to see if it exists in the database
	 * @param check
	 * 		{@code User} to be checked
	 * @return
	 * 		{@code true} if name does not exist, {@code false} otherwise
	 */
	private boolean checkName(UserInterface check) {
		
		List<User> users = uRepo.findAll();
		
		for (UserInterface u : users) {
			
			if (check.getUsername().equals(u.getUsername())) { return false; }
		}
		return true;
	}
	
	/****************************************************************** END PRIVATE METHODS ******************************************************************/
	
	/******************************************************************** END USER SERVICE *******************************************************************/
}
