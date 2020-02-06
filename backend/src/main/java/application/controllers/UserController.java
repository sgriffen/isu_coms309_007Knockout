package application.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import application.exceptions.BackendException;
import application.exceptions.InvalidUserException;
import application.items.ItemInterface;
import application.sessions.SessionInterface;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.tools.services.SessionService;
import application.tools.services.UserService;
import application.tools.wrappers.MultiObjectReturnWrapper;
import application.tools.wrappers.InventoryUpdateWrapper;
import application.tools.wrappers.ListReturnWrapper;
import application.tools.wrappers.LocationUpdateWrapper;
import application.tools.wrappers.ObjectReturnWrapper;
import application.tools.wrappers.ReturnWrapper;
import application.tools.wrappers.SetReturnWrapper;
import application.tools.wrappers.StatisticUpdateWrapper;
import application.tools.wrappers.UserUpdateWrapper;
import application.users.User;
import application.users.UserInterface;

/**
 * Controller for all objects that extend UserInterface
 * @author Sean Griffen
 */
@RestController
@RequestMapping(path = "/user")
public class UserController {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Service for this controller
	 */
	@Autowired
	private UserService uService;
	
	/**
	 * Service for this controller
	 */
	@Autowired
	private SessionService sService;
	
	/**
	 * Logger object
	 */
	private final Logger log = LoggerFactory.getLogger(MainController.class);
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/****************************************************************** START CONSTRUCTORS *******************************************************************/
	
	/**
	 * Constructs a {@code UserController} object with service object {@code UserService}
	 * @param uService
	 * 		{@code UserService} object desired
	 * @param sService
	 * 		{@code SessionService} object desired
	 */
	public UserController(UserService uService, SessionService sService) {
		
		this.uService = uService;
		this.sService = sService;
	}
	
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/****************************************************************** START GET MAPPINGS *******************************************************************/
	
	/**
	 * Method for user controller connectivity testing
	 * @return
	 * 		Connectivity phrase
	 */
	@GetMapping(value = "")
	public @ResponseBody Map<String, String> welcome() {
		
		log.info("User screen accessed");
		
		return Collections.singletonMap("Result", "Welcome to the User screen of 007:Knockout");
	}
	
	/******************************************************************* END GET MAPPINGS ********************************************************************/
	
	/***************************************************************** START POST MAPPINGS *******************************************************************/
	
	/**
	 * Adds a {@code User} to the database based on the inputed information
	 * @param user
	 * 		{@code JSON} version of {@code User} object
	 * @return
	 * 		{@code new Boolean(true)} if successful in adding {@code User}, {@code new Boolean(false)} otherwise
	 */
	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> addUser(@RequestBody User user) {
		
		log.info("Attempting to add User '" + user.getUsername() + "'");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { result = uService.addUser(user); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		wrap.excecute(result);
		log.info(result);
		return wrap;
	}
	
	/******************************************************************* END POST MAPPINGS *******************************************************************/
	
	/****************************************************************** START PUT MAPPINGS *******************************************************************/
	
	/**
	 * Gets a specific {@code User} from the database based on an inputed {@code Token}
	 * @param token
	 * 		{@code JSON} version of {@code Token} object
	 * @return
	 * 		{@code token}'s {@code User} or {@code null} if unsuccessful
	 */
	@PutMapping(value = "/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<UserInterface> getUser(@RequestBody Token token) {
		
		log.info("Attempting to fetch User");
		
		log.info("Token Entered: " + token.toString());
		
		UserInterface u1;
		ObjectReturnWrapper<UserInterface> wrap = new ObjectReturnWrapper<>();
		try { u1 = uService.getUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		String message = "User '" + u1.getUsername() +"' fetched";
		
		//Clear sensitive data
		u1.setToken(null);
		u1.setSession(null);
		u1.setPassword(null);
		
		wrap.excecute(u1, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets a {@code List} of all {@code Users} in the database based on an inputed {@code Token}
	 * @param token
	 * 		{@code JSON} version of {@code Token} object
	 * @return
	 * 		{@code List} of {@code User}s in the database, or null unsuccessful
	 */
	@PutMapping(value = "/get/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<UserInterface> getUsers(@RequestBody Token token) {
		
		log.info("Attempting to fetch all Users");
		
		log.info("Token Entered: " + token.toString());
		
		List<UserInterface> users;
		ListReturnWrapper<UserInterface> wrap = new ListReturnWrapper<>();
		try { users = uService.getUsers(token); } 
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Number of Users Fetched:" + users.size();
		
		//Clear sensitive data
		for (UserInterface u : users) {
			
			u.setToken(null);
			u.setPassword(null);
			u.setSession(null);
		}
		
		wrap.excecute(users, message);
		log.info(message);
		return wrap;
	}
	
	 /**
	  * Gets the {@code Location} of a {@code User} based on an inputed {@code Token}
	  * @param token
	  * 	{@code JSON} version of {@code Token} object
	  * @return
	  * 	{@code Location} data of {@code token}'s {@code User}
	  */
	@PutMapping(value = "/get/location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Location> getLocation(@RequestBody Token token) {
		
		log.info("Attempting to fetch User's Location");
		
		log.info("Token Entered: " + token.toString());
		
		UserInterface user;
		ObjectReturnWrapper<Location> wrap = new ObjectReturnWrapper<>();
		try { user = uService.getUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "User '" + user.getUsername() + "'s' location data fetched";
		wrap.excecute(user.getLocation(), message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets a {@code User}'s {@code Session}s' {@code Token}
	 * @param token
	 * 		{@code Token} of the {@code User} to get from
	 * @return
	 * 		{@code ListReturnWrapper<Token>} that contains all the {@code Token}s of the {@code Session}s that the {@code User} is in
	 */
	@PutMapping(value = "/get/session", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MultiObjectReturnWrapper<Token, Integer> getSessions(@RequestBody Token token) {
		
		log.info("Attempting to fetch User's Session(s)'s Token(s)");
		
		log.info("Token Entered: " + token.toString());
		
		Token sessionToken = null;
		SessionInterface session = null;
		Integer started;
		UserInterface user;
		MultiObjectReturnWrapper<Token, Integer> wrap = new MultiObjectReturnWrapper<>();
		try {
			
			user = uService.getUser(token);
			Set<Token> in = uService.getSessionToken(token);
			if (in.isEmpty()) {
				
				started = new Integer(-1);
				sessionToken = new Token();
			}
			else {
				for (Iterator<Token> i = in.iterator(); i.hasNext();) { sessionToken = i.next(); }
				session = sService.getSession(sessionToken);
				started = session.getStarted();
			}
		}
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "User '" + user.getUsername() + "'s' session(s)'s tokens have been fetched";
		if (sessionToken == null) { wrap.excecute(new Token(), started, message); }
		else { wrap.excecute(sessionToken, started, message); }
		
		log.info(message);
		
		return wrap;
	}
	
	/**
	 * Gets the {@code currency} of a {@code User}
	 * @param token
	 * 		{@code Token} of the {@code User} to get from
	 * @return
	 * 		{@code ObjectReturnWrapper<Double>}
	 */
	@PutMapping(value = "/get/money", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Double> getMoney(@RequestBody Token token) {
		
		log.info("Attempting to fetch User's balance");
		
		log.info("Token Entered: " + token.toString());
		
		UserInterface user;
		ObjectReturnWrapper<Double> wrap = new ObjectReturnWrapper<>();
		try { user = uService.getUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "User '" + user.getUsername() + "'s' balance is $" + user.getCurrency();
		wrap.excecute(new Double(user.getCurrency()), message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets the {@code items} of a {@code User}
	 * @param token
	 * 		{@code Token} of tht {@code User} to get from
	 * @return
	 * 		{@code ListReturnWrapper<ItemInterface>}
	 */
	@PutMapping(value = "/get/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SetReturnWrapper<ItemInterface> getItems(@RequestBody Token token) {
		
		log.info("Attempting to fetch User's items");
		
		log.info("Token Entered: " + token.toString());
		
		UserInterface user;
		SetReturnWrapper<ItemInterface> wrap = new SetReturnWrapper<>();
		try { user = uService.getUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "User '" + user.getUsername() + "'s' items are " + user.getItems().toString();
		wrap.excecute(user.getItems(), message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets the statistics (the {@code level}, {@code kills}, and {@code deaths}) of a {@code User}
	 * @param token
	 * 		{@code Token} of the {@code User} to get from
	 * @return
	 * 		{@code ListReturnWrapper<Double>} of stats
	 */
	@PutMapping(value = "/get/statistics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<Double> getStats(@RequestBody Token token) {
		
		log.info("Attempting to fetch User's statistics");
		
		log.info("Token Entered: " + token.toString());
		
		UserInterface user;
		ListReturnWrapper<Double> wrap = new ListReturnWrapper<>();
		try { user = uService.getUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "User '" + user.getUsername() + "'s' statistics are fetched";
		
		List<Double> arr = new ArrayList<>(); 
		arr.add(new Double((double) user.getLevel()));
		arr.add(new Double((double) user.getKills())); 
		arr.add(new Double(user.getKDRatio()));
		
		wrap.excecute(arr, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets the {@code targets} of a {@code User}
	 * @param token
	 * 		{@code Token} of the {@code User} to get from
	 * @return
	 * 		{@code ListReturnWrapper<UserInterface>} of {@code targets}
	 */
	@PutMapping(value = "/get/targets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<UserInterface> getTargets(@RequestBody Token token) {
		
		log.info("Attempting to fetch User's Targets");
		
		log.info("Token Entered: " + token.toString());
		
		UserInterface user;
		ListReturnWrapper<UserInterface> wrap = new ListReturnWrapper<>();
		try { user = uService.getUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "User '" + user.getUsername() + "'s' targets are fetched";
		
		List<UserInterface> targets = user.getTargets();
		
		//Clear sensitive data
		for (UserInterface u : targets) {
			u.setToken(null);
			u.setPassword(null);
			u.setSession(null);
		}
		
		wrap.excecute(targets, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Logs in a {@code User} by generating a {@code Token} and sending that to the client side for further communication
	 * @param user
	 * 		{@code User} to log in
	 * @return
	 * 		The {@code Token} if successful in logging in, {@code null} if not
	 */
	@PutMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MultiObjectReturnWrapper<Token, Integer> getToken(@RequestBody User user) {
		
		log.info("Attempting to fetch token for User '" + user.getUsername() + "'");
		
		Token token;
		int authLevel = 0;
		MultiObjectReturnWrapper<Token, Integer> wrap = new MultiObjectReturnWrapper<>();
		try {
			
			token = uService.getToken(user);
			authLevel = uService.getUser(token).getAuthLevel();
		}
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Token for User '" + user.getUsername() + "' fetched";
		
		wrap.excecute(token, new Integer(authLevel), message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Updates a {@code User}'s information based on an inputed {@code List<Token>} object
	 * @param toUpdate
	 * 		{@code JSON} version of a {@code List<Token>} object
	 * @return
	 * 		{@code new Boolean(true)} if {@code User}'s info was updated, {@code new Boolean(false)} otherwise
	 */
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> updateUserInfo(@RequestBody UserUpdateWrapper toUpdate) {
		
		log.info("Attempting to update User");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.updateUserInfo(toUpdate); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		if (result.contains("was not updated")) { wrap.error(new InvalidUserException("No user data was changed")); }
		
		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Updates a {@code User}'s location
	 * @param wrapper
	 * 		See {@code LocationUpdateWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/update/location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> updateUserLocation(@RequestBody LocationUpdateWrapper wrapper) {
		
		log.info("Attempting to update a User's Location");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.updateUserLocation(wrapper); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}

		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Updates the statistics (the {@code level}, {@code kills}, or {@code deaths} of a {@code User}
	 * @param wrapper
	 * 		See {@code StatisticUpdateWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/update/statistics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> updateUserStatistics(@RequestBody StatisticUpdateWrapper wrapper) {
		
		log.info("Attempting to update a User's stats");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.updateUserStatistics(wrapper); }
		catch (BackendException e) {  
			
			exceptionHandler(e, wrap);
			return wrap;
		}

		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Updates the {@code items} of a {@code User}
	 * @param wrapper
	 * 		See {@code InventoryUpdateWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/update/inventory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> updateUserInventory(@RequestBody InventoryUpdateWrapper wrapper) {
		
		log.info("Attempting to update a User's inventroy");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.updateUserInventory(wrapper); }
		catch (BackendException e) {  
			
			exceptionHandler(e, wrap);
			return wrap;
		}

		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Updates the {@code target} of a {@code User}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code User} to update, and the {@code Token}s of the {@code target} to set
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/update/targets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> updateUserTargets(@RequestBody List<Token> tokens) {
		
		log.info("Attempting to update a User's targets");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.updateUserTargets(tokens); }
		catch (BackendException e) {  
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Logs out a {@code User} from the client side by deleting it's active {@code Token}
	 * @param token
	 * 		{@code JSON} version of a {@code Token} object
	 * @return
	 * 		{@code new Boolean(true)} if successful log out, {@code new Boolean(false)} otherwise
	 */
	@PutMapping(value = "/logout", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteToken(@RequestBody Token token) {
		
		log.info("Attempting to logout user");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.deleteToken(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/******************************************************************* END PUT MAPPINGS ********************************************************************/
	
	/**************************************************************** START DELETE MAPPINGS ******************************************************************/
	
	/**
	 * Deletes a {@code User} from the database based on an inputed {@code Token}
	 * @param token
	 * 		{@code JSON} version of a {@code Token} object
	 * @return
	 * 		{@code new Boolean(true)} if successful in deletion, {@code new Boolean(false)} otherwise
	 */
	@DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteUser(@RequestBody Token token) {
		
		log.info("Attempting to delete User");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<Integer>();
		try { result = uService.deleteUser(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/****************************************************************** END DELETE MAPPINGS ******************************************************************/
	
	/***************************************************************** START PRIVATE METHODS *****************************************************************/
	
	/**
	 * Logs an {@code Exception} and updates a {@code ReturnWrapper} to an {@code Exception} state
	 * @param e
	 * 		{@code Exception} that was thrown
	 * @param wrap
	 * 		{@code ReturnWrapper} to update
	 */
	private void exceptionHandler(BackendException e, ReturnWrapper wrap) {
		
		wrap.error(e);
		
		log.info(e.getMessage());
		log.info(e.getStackTrace().toString());
	}
	
	/**
	 * Logs a successful method execution and updates a {@code ReturnWrapper} to an {@code Execution} state
	 * @param message
	 * 		{@code String} to log
	 * @param wrap
	 * 		{@code ReturnWrapper} to update
	 */
	private void excecutionHandler(String message, ReturnWrapper wrap) {
		
		wrap.excecute(message);
		log.info(message);
	}
	
	/****************************************************************** END PRIVATE METHODS ******************************************************************/
	
	/****************************************************************** END USER CONTROLLER ******************************************************************/
}