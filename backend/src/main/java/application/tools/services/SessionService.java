package application.tools.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.exceptions.BackendException;
import application.exceptions.InvalidAdministratorException;
import application.exceptions.InvalidListException;
import application.exceptions.InvalidLocationException;
import application.exceptions.InvalidModeratorException;
import application.exceptions.InvalidSessionException;
import application.exceptions.InvalidTokenException;
import application.exceptions.InvalidUserException;
import application.items.Item;
import application.items.ItemInterface;
import application.items.ItemRepository;
import application.sessions.Session;
import application.sessions.SessionInterface;
import application.sessions.SessionRepository;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.tools.wrappers.ListMapReturnWrapper;
import application.tools.wrappers.MultiListReturnWrapper;
import application.tools.wrappers.OnTapUpdateWrapper;
import application.tools.wrappers.SessionAddUpdateWrapper;
import application.tools.wrappers.SessionAddUserWrapper;
import application.tools.wrappers.SessionStartUpdateWrapper;
import application.users.User;
import application.users.UserInterface;
import application.users.UserRepository;

/**
 * Service for {@code SessionController}
 * 
 * @author Sean Griffen
 */
@Service
public class SessionService {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * {@code SessionRepository} accessed by this service
	 */
	@Autowired
	private SessionRepository sRepo;
	
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
	 * Constructs a {@code SessionService} with a {@code SessionRepository}, {@code ItemRepository} and a {@code UserRepository}
	 * @param sRepo
	 * 		{@code SessionRepository} to use
	 * @param uRepo
	 * 		{@code UserRepository} to use
	 * @param iRepo
	 * 		{@code ItemRepository} to use
	 */
	public SessionService(SessionRepository sRepo, UserRepository uRepo, ItemRepository iRepo) {
		
		this.sRepo = sRepo;
		this.uRepo = uRepo;
		this.iRepo = iRepo;
	}
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/******************************************************************* START GET HELPERS *******************************************************************/
	
	
	
	/******************************************************************** END GET HELPERS ********************************************************************/
	
	/****************************************************************** START POST HELPERS *******************************************************************/
	
	/**
	 * Adds a {@code Session} to the database
	 * @param wrapper
	 * 		See {@code SessionAddUpdateWrapper}
	 * @return
	 * 		{@code passcode} of the new {@code Session}
	 * @throws InvalidSessionException
	 * 		Throws if the inputed {@code Session} has an invalid {@code name}, {@code location}, or {@code radius}
	 * @throws InvalidModeratorException 
	 * @throws InvalidTokenException 
	 */
	public int addSession(SessionAddUpdateWrapper wrapper) throws InvalidSessionException, InvalidTokenException, InvalidModeratorException {
		
		Random rand = new Random();
		
		validateModeraterToken(wrapper.getToken());
		
		SessionInterface toStart = new Session (wrapper.getName(), wrapper.getCenter(), wrapper.getRadius());
		
		//Check if toStart has a blank name, has no center, or has a radius = 0
		if (toStart.getName() == null || toStart.getName().trim().isEmpty() || toStart.getCenter() == null || toStart.getRadius() == 0)
						   { throw new InvalidSessionException("Cannot add a Session that has a blank name, no center, or radius of 0."); }
		List<Session> sessions = sRepo.findAll();
		
		for (Session s : sessions) {
			
			if (s.getName().equals(toStart.getName())) { throw new InvalidSessionException("Name for Session already exists."); }
		}
		
		toStart.setPasscode(rand.nextInt(999998) + 1);
		boolean passEqual = true;
		
		if (!sessions.isEmpty()) {
			while (passEqual) {
				
				for (SessionInterface s : sessions) {
					
					if (s.getPasscode() == toStart.getPasscode()) {
						
						passEqual = true;
						break;
					} else { passEqual = false; }
				}
			}
		}
		
		generateNewToken(toStart);
		
		//Save session to database
		sRepo.save((Session) toStart);
		
		int result = toStart.getPasscode();
		return result;
	}
	
	/******************************************************************* END POST HELPERS ********************************************************************/
	
	/******************************************************************* START PUT HELPERS *******************************************************************/
	
	/**
	 * Gets a {@code Session} from the database
	 * @param get
	 * 		{@code Token} of the {@code Session} to get
	 * @return
	 * 		{@code Session} associated with {@code get}
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if {@code Session} trying to get does not exist
	 */
	public SessionInterface getSession(Token get) throws InvalidTokenException, InvalidSessionException {
		
		//Validate Token
		this.validateSessionToken(get);
		
		SessionInterface sess = getSessionToken(get);
		if (sess == null) { throw new InvalidSessionException("SessionInterface does not exist"); }
		
		return sess;
	}
	
	/**
	 * Gets all {@code Session}s in the database
	 * @param token
	 * 		{@code Token} of a {@code User} with {@code authLevel} equal to 2
	 * @return
	 * 		{@code List<SessionInterface>} that contains all {@code Session}s in the database
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code User}'s {@code authLevel} does not equal 2
	 * 		
	 */
	public List<SessionInterface> getSessions(Token token) throws InvalidTokenException, InvalidAdministratorException {
		
		this.validateAdminToken(token);
		
		List<Session> inRepo = sRepo.findAll();
		List<SessionInterface> result = new ArrayList<>();
		
		for (SessionInterface s : inRepo) { result.add((SessionInterface) s); }
		
		return result;
	}
	
	/**
	 * Gets a {@code User} in a {@code Session}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to get the {@code User} from, and the {@code Token} of the {@code User} to get
	 * @return
	 * 		{@code List<UserInterface>} of {@code User}s that were retrieved
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to grab from does not exist
	 * @throws InvalidUserException
	 * 		Throws if the {@code User} trying to grab does not exist
	 * @throws InvalidListException
	 * 		Throws if {@code tokens} is not in the expected format
	 */
	public List<UserInterface> getUser(List<Token> tokens) throws InvalidTokenException, InvalidSessionException, InvalidUserException, InvalidListException {
		
		//Check if first token is session token, and rest are user tokens to add
		if (tokens.size() < 2)
			{ throw new InvalidListException("List not in expected format"); }
		
		SessionInterface from = null;
		List<UserInterface> toGet = new ArrayList<>();
		
		//Valiate and decode tokens
		for (Token t : tokens) {
			
			if (tokens.indexOf(t) == 0) { from = getSession(t); }
			else {
				
				validateUserToken(t);
				toGet.add(getUserToken(t));
			}
		}
		
		if (toGet.size() < 1) { throw new InvalidListException("List of users to get in a sesion has a size less than 1"); }
		if (from == null) { throw new InvalidSessionException("Session trying to get from is null"); }
		
		return this.getUserHelper(toGet, from);
	}
	
	/**
	 * Gets all {@code User}s in the play area of the {@code Session}
	 * @param token
	 * 		{@code Token} of the {@code Session} to grab from
	 * @return
	 * 		{@code List<UserInterface>} of {@code User}s in the play area
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to grab from doesn't exist
	 */
	public List<UserInterface> getUserInRadius(Token token) throws InvalidTokenException, InvalidSessionException {
		
		//Get Session from token
		SessionInterface from = this.getSession(token);
		
		List<UserInterface> result = new ArrayList<>();
		
		for (UserInterface u : from.getUsers()) {
			
			if (from.inPlayArea(u.getLocation())) { result.add(u); }
		}
		
		return result;
	}
	
	/**
	 * Gets all {@code User}s from a {@code Session}
	 * @param token
	 * 		{@code token} of the {@code Session} to grab from
	 * @return
	 * 		{@code List<UserInterface>} of {@code User}s in the database
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to grab from does not exist
	 */
	public List<UserInterface> getAllUsers(Token token) throws InvalidTokenException, InvalidSessionException {
		
		SessionInterface from = getSession(token);
		
		if (from.getUsers().isEmpty()) { return null; }
		
		List<UserInterface> users = from.getUsers();
		
		return users;
	}
	
	public ListMapReturnWrapper<UserInterface, Location, ItemInterface> getOnTap(List<Token> tokens) throws InvalidListException, InvalidTokenException, InvalidSessionException, InvalidUserException {
		
		//Get Items in view radius
		Map<Location, ItemInterface> items = getItemsOnTapHelper(tokens);
		//Get Users in view radius
		List<UserInterface> users = getUsersOnTapHelper(tokens);
		
		return new ListMapReturnWrapper<UserInterface, Location, ItemInterface>(users, items);
	}
	
	/**
	 * Generates a {@code Token} for a {@code Session}
	 * @param session
	 * 		See {@code SessionStartUpdateWrapper}
	 * @return
	 * 		{@code String} stating successful execution
	 * @throws InvalidSessionException
	 * 		Throws if the {@code name} or {@code passcode} does not match a {@code Session} that does not exist
	 * @throws InvalidModeratorException 
	 * @throws InvalidTokenException 
	 */
	public String startSession(SessionStartUpdateWrapper wrapper) throws InvalidSessionException, InvalidTokenException, InvalidModeratorException {
		
		Random rand = new Random();
		
		validateModeraterToken(wrapper.getUser());
		SessionInterface session = getSession(wrapper.getSession());
		
		//Set targets
		setTargetHelper(session.getUsers());
		//Reset passcode
		session.setPasscode(-1);
		//Set Items
		setItemsHelper(session, rand);
		//Set started to 1
		session.setStarted(new Integer(1));
		
		//session.setRandTime((6 / session.getUsers().size() + rand.nextInt(2) + rand.nextDouble() - 1) + System.currentTimeMillis());
		
		sRepo.save((Session) session);
		
		return "Started Session '" + session.getName() + "'";
	}
	
	/**
	 * Adds a {@code User} to the database
	 * @param wrap
	 * 		See {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code String} stating success or not in adding a {@code User}
	 * @throws InvalidListException
	 * 		Throws if the {@code Session} trying to add to has a non-null {@code Token}
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to add to does not exist
	 */
	public Token addUser(SessionAddUserWrapper wrap) throws InvalidListException, InvalidTokenException, InvalidSessionException {
		
		int sessCode = wrap.getPasscode();
		Token toAdd = wrap.getToAdd();
		SessionInterface addTo = null;
		List<UserInterface> uAdd = new ArrayList<>();
		
		//Check if passcode corresponds to an existing session
		for (SessionInterface s : sRepo.findAll()) {
			
			if (s.getPasscode() == sessCode) { addTo = (SessionInterface) s; }
		}
		if (addTo == null) { throw new InvalidSessionException("Passcode does not correspond to an existing Session"); }
		if (addTo.getStarted() != 0) { throw new InvalidListException("Session has been started. Cannot add any more users"); }
		
		//Validate and decode Tokens
			
		validateUserToken(toAdd);
			
		if (!addTo.getUsers().isEmpty()) {
				
			//If the session does not already have the user trying to add, and the user isn't in a session already, add it.
			if (!addTo.getUsers().contains(getUserToken(toAdd)) && getUserToken(toAdd).getSession().size() <= 0) { uAdd.add(getUserToken(toAdd)); }
		}
		//If the session has no users, and the user trying to add isn't already in a session
		else if (getUserToken(toAdd).getSession().size() <= 0) { uAdd.add(getUserToken(toAdd)); }
		
		if (uAdd.isEmpty()) { throw new InvalidListException("No users were added to the Session"); }
		
		//Add users
		for (UserInterface u : uAdd) { addUserHelper(u, addTo); }
		
		return addTo.getToken();
	}
	
	/**
	 * Sets the {@code authenticator} of a {@code Token} to null
	 * @param token
	 * 		{@code Token} to modify
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if {@code Session} trying to stop does not exist
	 */
	public String stopSession(Token token) throws InvalidTokenException, InvalidSessionException {
		
		SessionInterface toStop = getSession(token);
		
		toStop.getToken().setAuthenticator(null);
		sRepo.save((Session) toStop);
		
		return "Stopped SessionInterface #" + toStop.getId() + " by clearing its Token";
	}
	
	/**
	 * Updates the {@code User}s involved in an assassination (When a {@code User} attempts to eliminate its {@code target})
	 * @param wrapper
	 * 		See {@code OnTapUpdateWrapper}
	 * @return
	 * 		{@code 1} if the assassination resulted in a win, or {@code 0} if there was no win, but the {@code target} was eliminated
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to use doesn't exist
	 * @throws InvalidUserException
	 * 		Throws if the {@code User} who is trying to eliminate does not exist
	 * @throws InvalidListException
	 * 		Shoudn't happen if the other {@code Exceptions} weren't thrown
	 * @throws InvalidLocationException
	 * 		Throws if the {@code Location} tapped on does not contain the {@code User}'s {@code target}
	 */
	public int updateOnTap(OnTapUpdateWrapper wrapper) throws InvalidTokenException, InvalidSessionException, InvalidUserException, InvalidListException, InvalidLocationException {
		
		//Get Session
		SessionInterface from = getSession(wrapper.getSession());
		//Get user that did the tapping
		List<Token> l1 = new ArrayList<>();
		l1.add(wrapper.getSession());
		l1.add(wrapper.getTapper());
		UserInterface tapper = getUser(l1).get(0);
		
		UserInterface uTapped = null;
		ItemInterface iTapped = null;
		
		if (tapper.getLocation().getDistance(wrapper.getTapped()) <= tapper.getKillRadius() / (tapper.getKillRadius() / tapper.getLocation().getAccuracy())) {
			
			//Get User that was tapped
			uTapped = getLocationsUserHelper(wrapper.getTapped(), tapper, from);
			if (uTapped == null) { iTapped = getLocationsItemHelper(wrapper.getTapped(), tapper, from); }
		} else { throw new InvalidLocationException("Tapped outside of kill radius"); }
		
		if (uTapped != null) { killConfirmedHelper(tapper, uTapped, from); }
		else if (iTapped != null) { addItemUserHelper(tapper, iTapped, getSession(wrapper.getSession()), wrapper.getTapped()); }
		
		if (from.getUsers().size() == 1) { return 1; }
		else if (iTapped != null) { return 2; }
		else { return 0; }
	}
	
	/**
	 * Removes a {@code User} from a {@code Session}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to grab from, and the {@code Token} of the {@code User} to remove
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to remove from does not exist
	 * @throws InvalidListException
	 * 		Throws if {@code tokens} is not in the expected format
	 */
	public String deleteUser(List<Token> tokens) throws InvalidTokenException, InvalidSessionException, InvalidListException {
		
		//Check if first token is session token, and rest are user tokens to add
		if (tokens.size() < 2)
			{ throw new InvalidListException("List not in expected format"); }
		
		//Validate Tokens
		for (int i = 0; i < tokens.size(); i++) { validateUserToken(tokens.get(i)); }
		
		SessionInterface from = getSession(tokens.get(0));
		List<UserInterface> toDelete = new ArrayList<>();
		
		for (int i = 1; i < tokens.size(); i++) {
				
			toDelete.add(getUserToken(tokens.get(i)));
			if (toDelete.get(toDelete.size() - 1) == null) { throw new InvalidTokenException("User Token does not correspond to an existing User"); }
		}
		
		String usernames = "";
		for (UserInterface u : toDelete) {
				
			if (toDelete.indexOf(u) == toDelete.size() - 1) { usernames += u.getUsername(); }
			else { usernames += u.getUsername() + ", "; }
			
			deleteUserHelper(u, from);
		}
		
		return "Deleted Users '" + usernames + "' from SessionInterface #" + from.getId();
	}
	
	/**
	 * Removes all {@code User}s from a {@code Session}
	 * @param token
	 * 		{@code Token} of the {@code Session} to remove from
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to remove from does not exist
	 * @throws InvalidListException
	 * 		Shouldn't throw if the previous {@code Exception}s did not throw
	 */
	public String deleteAllUsers(Token token) throws InvalidTokenException, InvalidSessionException, InvalidListException {
		
		SessionInterface from = getSession(token);
		
		List<Token> tokens = new ArrayList<>();
		tokens.add(from.getToken());
		for (UserInterface u : from.getUsers()) { tokens.add(u.getToken()); }
			
		return this.deleteUser(tokens);
	}
	
	/******************************************************************** END PUT HELPERS ********************************************************************/
	
	/***************************************************************** START DELETE HELPERS ******************************************************************/
	
	/**
	 * Deletes a {@code Session} from the database
	 * @param token
	 * 		{@code Token} of the {@code Session} to delete
	 * @return
	 * 		{@code String} stating success or not
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to delete does not exist
	 */
	public String deleteSession(Token token) throws InvalidTokenException, InvalidSessionException {
		
		SessionInterface toStop = getSession(token);
		if (toStop == null) { throw new InvalidSessionException("SessionInterface does not exist in the database"); }
		
		List<UserInterface> inSession = toStop.getUsers();
		
		for (UserInterface u : inSession) {
			
			u.getSession().remove(toStop.getToken());
			uRepo.save((User) u);
		}
		
		sRepo.delete((Session) toStop);
		return "Deleted SessionInterface #" + toStop.getId() + " and removed the Session's Token from the Users' data";
	}
	
	/****************************************************************** END DELETE HELPERS *******************************************************************/
	
	/***************************************************************** START PRIVATE METHODS *****************************************************************/
	
	/**
	 * Generates a {@code Token} for a {@code Session}
	 * @param session
	 * 		{@code Session} to generate the {@code Token} for
	 */
	private void generateNewToken(SessionInterface session) {
		
		String[] s = { session.getName() };
		
		session.setToken(new Token(s, 8760));
	}
	
	/**
	 * Validates that a {@code Session} exists in the database
	 * @param toValidate
	 * 		{@code Session} to validate
	 * @return
	 * 		{@code Session} that was validated
	 * @throws InvalidSessionException
	 * 		Throws if {@code toValidate} did not correspond to one that exists in the database
	 */
	private SessionInterface validateSession(SessionInterface toValidate) throws InvalidSessionException {
		
		if (toValidate == null) { throw new InvalidSessionException("SessionInterface entered is either null or empty"); }
		
		List<Session> sessions = sRepo.findAll();
		
		//Get Session in database with matching name toValidate
		boolean validated = false;
		for (SessionInterface s : sessions) {
			
			if (toValidate.getName().equals(s.getName()) && toValidate.getPasscode() == s.getPasscode()) {
				
				toValidate = s;
				validated = true;
				break;
			}
		}
		if (!validated) { throw new InvalidSessionException("SessionInterface '" + toValidate.getName() + "' does not have a matching name and/or passcode in the database"); }
		return toValidate;
	}
	
	/**
	 * Gets a {@code User} associated with a {@code Token}
	 * @param token
	 * 		{@code Token} of the {@code User} to get
	 * @return
	 * 		{@code User} associated with {@code token}
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 */
	private UserInterface getUserToken(Token token) throws InvalidTokenException {
		
		List<User> users = uRepo.findAll();
		
		for (UserInterface u : users) {
			
			if (token.getAuthenticator().equals(u.getToken().getAuthenticator())) { return u; }
		}
		
		throw new InvalidTokenException("Token entered does not correspond to an existing User");
	}
	
	/**
	 * Gets a {@code Session} associated with a {@code Token}
	 * @param token
	 * 		{@code Token} of the {@code Session} to get
	 * @return
	 * 		{@code Session} associated with {@code token}
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 */
	private SessionInterface getSessionToken(Token token) throws InvalidTokenException {
				
		List<Session> sessions = sRepo.findAll();
		
		if (token == null) { throw new InvalidTokenException("Token cannot be null"); }
		if (token.getAuthenticator() == null || token.getAuthenticator().isEmpty()) { throw new InvalidTokenException("Token's authenticator cannot be null or empty");}
		
		for (SessionInterface s : sessions) {
			
			if (token.getAuthenticator().equals(s.getToken().getAuthenticator())) { return s; }
		}
		
		throw new InvalidTokenException("Token entered does not correspond to an existing Session");
	}
	
	/**
	 * Validates a {@code Token} that's associated with a {@code User}
	 * @param toValidate
	 * 		{@code Token} to validate
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 */
	private void validateUserToken(Token toValidate) throws InvalidTokenException {
		
		//Can't do anything if token is null
		if (toValidate == null) { throw new InvalidTokenException("Token entered is null"); }
		
		//If token is expired, create a new token
		if (!toValidate.isValid()) {
			
			getUserToken(toValidate).setToken(new Token());
			uRepo.save((User) getUserToken(toValidate));
			throw new InvalidTokenException("Token entered is expired");
		}
	}
	
	/**
	 * Validates a {@code Token} associated with a {@code Session}
	 * @param toValidate
	 * 		{@code Token} to validate
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to validate does not exist
	 */
	private void validateSessionToken(Token toValidate) throws InvalidTokenException, InvalidSessionException {
		
		//Can't do anything if token is null
		if (toValidate == null) { throw new InvalidTokenException("Token entered is null"); }
		
		//If token is expired, create a new token
		if (!toValidate.isValid()) {
			
			getUserToken(toValidate).setToken(new Token());
			uRepo.save((User) getUserToken(toValidate));
			throw new InvalidTokenException("Token entered is expired");
		}
	}
	
	/**
	 * Validates a {@code Token} that]'s associated with a {@code User} with an {@code authLevel} equal to 2
	 * @param toValidate
	 * 		{@code Token} to validate
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code toValidate}'s {@code User} does not have an {@code authLevel} equal to 2
	 */
	private void validateAdminToken(Token toValidate) throws InvalidTokenException, InvalidAdministratorException {
		
		//Can't do anything if token is null
		if (toValidate == null) { throw new InvalidTokenException("Token entered is null"); }
		
		//If token is expired, create a new token
		validateUserToken(toValidate);
		
		UserInterface u = getUserToken(toValidate);
		
		if (u.getAuthLevel() != 2) { throw new InvalidAdministratorException("Administrator entered is not valid. Check username, password, and/or id"); }
	}
	
	/**
	 * Validates a {@code Token} that]'s associated with a {@code User} with an {@code authLevel} equal to 1
	 * @param toValidate
	 * 		{@code Token} to validate
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code toValidate}'s {@code User} does not have an {@code authLevel} equal to 1
	 */
	private void validateModeraterToken(Token toValidate) throws InvalidTokenException, InvalidModeratorException {
		
		//Can't do anything if token is null
		if (toValidate == null) { throw new InvalidTokenException("Token entered is null"); }
		
		//If token is expired, create a new token
		validateUserToken(toValidate);
		
		UserInterface u = getUserToken(toValidate);
		
		if (u.getAuthLevel() < 1) { throw new InvalidModeratorException("Moderator entered is not valid. Check username, password, and/or id"); }
	}
	
	/**
	 * Gets {@code User}s from a {@code Session}
	 * @param toGet
	 * 		{@code User}s to get
	 * @param from
	 * 		{@code Session} to get from
	 * @return
	 * 		{@code List<UserInterface>} of {@code User}s in the {@code Session} trying to get
	 * @throws InvalidUserException
	 * 		Throws if an inputted {@code User} does not exist in the {@code Session}
	 * @throws InvalidSessionException 
	 * @throws InvalidListException 
	 */
	private List<UserInterface> getUserHelper(List<UserInterface> toGet, SessionInterface from) throws InvalidUserException, InvalidSessionException, InvalidListException {
		
		List<UserInterface> result = new ArrayList<>();
		List<UserInterface> inSession = from.getUsers();
		
		for (UserInterface u : toGet) {
			boolean same = false;
			UserInterface k = null;
			for (UserInterface i : inSession) {
				if (i.getUsername().equals(u.getUsername())) {	
					
					result.add(u);
					same = true;
					break;
				}
				else k = i;
			}
			if (!same) { throw new InvalidUserException("User '" + u.getUsername() + "' compared to '" + k.getUsername() +"'does not exist in SessionInterface #" + from.getId()); }
			else { break; }
		}
		
		return result;
	}
	
	/**
	 * Adds a {@code User} to a {@code Session}
	 * @param toAdd
	 * 		{@code User} to add
	 * @param addTo
	 * 		{@code Session} to add to
	 */
	private void addUserHelper(UserInterface toAdd, SessionInterface addTo) {
		
		//Add user to session
		addTo.addUser(toAdd);
		toAdd.getSession().add(addTo.getToken());
		
		//save changes
		sRepo.save((Session) addTo);
		uRepo.save((User) toAdd);
	}
	
	/**
	 * Removes a {@code User} from a {@code Session}
	 * @param toDelete
	 * 		{@code User} to remove
	 * @param from
	 * 		{@code Session} to remove from
	 */
	private void deleteUserHelper(UserInterface toDelete, SessionInterface from) {
		
		if (from.getUsers().contains(toDelete)) {
			
			//Remove user from list
			from.getUsers().remove(toDelete);
			
			//Reset User's sessionToken
			Set<Token> uSessTok = toDelete.getSession();
			uSessTok.remove(from.getToken());
			toDelete.getSession().clear();
			toDelete.setSession(uSessTok);
			
			//Save changes to database
			sRepo.save((Session) from);
			uRepo.save((User) toDelete);
		}
	}
	
	/**
	 * Sets the {@code target} of a {@code User}
	 * @param assignTo
	 * 		{@code List<UserInterface>} to assign the {@code target}s to
	 */
	private void setTargetHelper(List<UserInterface> assignTo) {
		
		List<UserInterface> assign = new ArrayList<>();
		
		//Populate target list
		for (int i = 1; i < assignTo.size(); i++) { assign.add(assignTo.get(i)); }
		assign.add(assignTo.get(0));
		
		//Assign targets
		for (int i = 0; i < assignTo.size(); i++) {
			
			List<UserInterface> toAdd = new ArrayList<>();
			toAdd.add(assign.get(i));
			assignTo.get(i).setTargets(toAdd);
			
			uRepo.save((User) assignTo.get(i));
		}
	}
	
	/**
	 * Gets a {@code Location}'s {@code User} if that {@code User} is another's {@code target}
	 * @param loc
	 * 		{@code Location} to get from
	 * @param tapper
	 * 		{@code User} who's {@code target} is at {@code Location} {@code loc}
	 * @param sess
	 * 		{@code Session} to search in
	 * @return
	 * 		{@code target} of {@code tapper}
	 * @throws InvalidLocationException
	 * 		Throws if {@code loc} does not contain a {@code User}'s {@code target}
	 */
	private UserInterface getLocationsUserHelper(Location loc, UserInterface tapper, SessionInterface sess) throws InvalidLocationException {
		
		UserInterface tapped = null;
		List<UserInterface> probs = new ArrayList<>();
		
		for (UserInterface u : sess.getUsers()) {
			if (tapper.getLocation().getDistance(u.getLocation()) <= tapper.getKillRadius() * (tapper.getKillRadius() * (Math.exp((tapper.getLocation().getAccuracy() / 
				u.getLocation().getAccuracy()) * -1) * Math.min(tapper.getLocation().getAccuracy(), u.getLocation().getAccuracy()) * 2.718282))) {
					if (!u.equals(tapper)) { probs.add(u); }
			}
		}
		
		boolean found = false;
		for (UserInterface t : tapper.getTargets()) {
			for (UserInterface u : probs) {
				if (t.getUsername().equals(u.getUsername())) {
					
					tapped = u;
					found = true;
					break;
				}
			}
			if (found) { break; }
		}
		if (tapped == null) { throw new InvalidLocationException("Target not located"); }
		return tapped;
	}
	
	/**
	 * Gets a {@code Location}'s {@code Item}
	 * @param loc
	 * 		{@code Location} to get at
	 * @param tapper
	 * 		{@code User} who's item getting is at {@code Location} {@code loc}
	 * @param sess
	 * 		{@code Session} to search in
	 * @return
	 * 		{@code Item} at {@code Location} {@code loc}
	 * @throws InvalidLocationException
	 * 		Throws if {@code loc} does not contain an {@code Item}
	 */
	private ItemInterface getLocationsItemHelper(Location loc, UserInterface tapper, SessionInterface sess) throws InvalidLocationException {
		
		ItemInterface tapped = null;
		
		for (Location l : sess.getILoc().keySet()) {
			
			if (tapper.getLocation().getDistance(l) <= tapper.getKillRadius() * (tapper.getKillRadius() * (Math.exp((tapper.getLocation().getAccuracy() / 
				l.getAccuracy()) * -1) * Math.min(tapper.getLocation().getAccuracy(), l.getAccuracy()) * 2.718282))) {
				
					tapped = sess.getILoc().get(loc);
			}
		}
		
		if (tapped == null) { throw new InvalidLocationException("Items not located"); }
		return tapped;
	}
	
	/**
	 * Exchanges {@code target}s between two {@code User}s, and increases or decreases the corresponding stats when an assassination is confirmed
	 * @param tapper
	 * 		{@code User} who has eliminated its {@code target}
	 * @param tapped
	 * 		{@code User} who has been eliminated
	 * @param session
	 * 		{@code Session} that this has happened in
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} this has happened in doesn't exist
	 * @throws InvalidListException
	 * 		Shouldn't throw if the above {@code Exception}s weren't thrown
	 */
	private void killConfirmedHelper(UserInterface tapper, UserInterface tapped, SessionInterface session) throws InvalidTokenException, InvalidSessionException, InvalidListException {
		
		//Extange targets
		tapper.setTargets(tapped.getTargets());
		tapped.setTargets(new ArrayList<>());
		
		//Update kills and deaths
		tapper.setKills(tapper.getKills() + 1);
		tapped.setDeaths(tapped.getDeaths() + 1);
		
		//Removed tapped from the session
		List<Token> l1 = new ArrayList<>();
		l1.add(session.getToken());
		l1.add(tapped.getToken());
		deleteUser(l1);
		
		//Increase level if won
		if (tapper.getToken().equals(tapper.getTargets().get(0).getToken())) { tapper.setLevel(tapper.getLevel() + 1); }
		
		//Save changes
		uRepo.save((User) tapper);
		uRepo.save((User) tapped);
	}
	
	/**
	 * Adds an {@code Item} to a {@code User}'s inventory
	 * @param tapper
	 * 		{@code User} to add to
	 * @param toAdd
	 * 		{@code Item} to add
	 */
	private void addItemUserHelper(UserInterface tapper, ItemInterface toAdd, SessionInterface sess, Location tapped) {
		
		//Add item to inventory
		tapper.getItems().add(toAdd);
		//Remove from the session (So other players don't pick it up)
		sess.getILoc().remove(tapped);
	}
	
	/**
	 * Generates a random location inside a {@code Session}'s radius for an {@code Item}
	 * @param sess
	 * 		{@code Session} working in
	 * @return
	 * 		{@code Location} to place an {@code Item} at
	 */
	private Location generateLocation(SessionInterface sess) {
		
		Random rand = new Random();
		
		int lonDegrees = rand.nextInt(181) - rand.nextInt(360);
		int latDegrees = rand.nextInt(91) - rand.nextInt(181);
		int radius = rand.nextInt((int)sess.getRadius() + 1);
		
		double lat = radius * Math.sin((latDegrees * Math.PI) / 180.0);
		double lon = radius * Math.cos((lonDegrees * Math.PI) / 180.0);
		
		return new Location(lat, lon, 0);
	}
	
	private void setItemsHelper(SessionInterface sess, Random rand) {
		
		int numItems = (int) Math.max((sess.getRadius() / 100) * 3, 4);
			
		//add items
		for (int i = 0; i < numItems; i++) {
			
			int r = rand.nextInt(4) + 1;
			
			ItemInterface toAdd = iRepo.findById(r).get();
			Location toAddAt = generateLocation(sess);
			sess.getILoc().put(toAddAt, (Item) toAdd);
		}
	}
	
	/**
	 * Gets all the {@code User}s inside the view area of another {@code User}. Happens when a {@code User} taps the screen on client side
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to grab from, and the {@code Token} of the {@code User} that tapped
	 * @return
	 * 		{@code List<UserInterface>} of {@code User}s inside the view area of the {@code User} that tapped
	 * @throws InvalidListException
	 * 		Throws if {@code tokens} is not in the expected format
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to grab from does not exist
	 * @throws InvalidUserException
	 * 		Throws if the {@code User} trying to grab does not exist
	 */
	private List<UserInterface> getUsersOnTapHelper(List<Token> tokens) throws InvalidListException, InvalidTokenException, InvalidSessionException, InvalidUserException {
		
		//Check if first token is session token, and second is the user that tapped
		if (tokens.size() < 2)
			{ throw new InvalidListException("List not in expected format"); }
		
		//Get Session from token
		SessionInterface from = this.getSession(tokens.get(0));
		//Get User from token
		List<UserInterface> temp = this.getUser(tokens);
		
		List<UserInterface> result = new ArrayList<>();
		
		for (UserInterface u : from.getUsers()) {
			
			if (!u.equals(temp.get(0))) {
				if (temp.get(0).getLocation().getDistance(u.getLocation()) <= temp.get(0).getViewRadius()) { result.add(u); }
			}
		}
		
		return result;
	}
	
	/**
	 * Gets all the {@code Item}s inside the view area of a {@code User}. Happens when a {@code User} taps the screen on client side
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to grab from, and the {@code Token} of the {@code User} that tapped
	 * @return
	 * 		{@code List<ItemInterface>} of {@code User}s inside the view area of the {@code User} that tapped
	 * @throws InvalidListException
	 * 		Throws if {@code tokens} is not in the expected format
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidSessionException
	 * 		Throws if the {@code Session} trying to grab from does not exist
	 * @throws InvalidUserException
	 * 		Throws if the {@code User} trying to grab does not exist
	 */
	private Map<Location, ItemInterface> getItemsOnTapHelper(List<Token> tokens) throws InvalidListException, InvalidTokenException, InvalidSessionException, InvalidUserException {
		
		//Check if first token is session token, and second is the user that tapped
		if (tokens.size() < 2)
			{ throw new InvalidListException("List not in expected format"); }
		
		//Get Session from token
		SessionInterface from = this.getSession(tokens.get(0));
		//Get User from token
		List<UserInterface> temp = this.getUser(tokens);
		
		Map<Location, ItemInterface> result = new HashMap<>();
		
		for (Location l : from.getILoc().keySet()) {
			
			if (temp.get(0).getLocation().getDistance(l) <= temp.get(0).getViewRadius()) { result.put(l, from.getILoc().get(l)); }
		}
		
		return result;
	}
	
	/****************************************************************** END PRIVATE METHODS ******************************************************************/
	
	/****************************************************************** END SESSION SERVICE ******************************************************************/
}
