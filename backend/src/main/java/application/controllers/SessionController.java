package application.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import application.items.ItemInterface;
import application.sessions.SessionInterface;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.tools.services.SessionService;
import application.tools.wrappers.ReturnWrapper;
import application.tools.wrappers.SessionAddUpdateWrapper;
import application.tools.wrappers.ObjectReturnWrapper;
import application.tools.wrappers.OnTapUpdateWrapper;
import application.tools.wrappers.ListMapReturnWrapper;
import application.tools.wrappers.ListReturnWrapper;
import application.tools.wrappers.MultiListReturnWrapper;
import application.tools.wrappers.SessionAddUserWrapper;
import application.tools.wrappers.SessionStartUpdateWrapper;
import application.users.UserInterface;
import application.webSocket.Endpoint;

/**
 * Controller for all objects that extend SessionInterface
 * 
 * @author Sean Griffen
 */

@RestController
@RequestMapping(path = "/session")
public class SessionController {

	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Service for this controller
	 */
	@Autowired
	private SessionService sService;
	
	/**
	 * Logger object
	 */
	private final Logger log = LoggerFactory.getLogger(MainController.class);
	
	Endpoint endpoint = new Endpoint(null, sService);
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/****************************************************************** START CONSTRUCTORS *******************************************************************/
	
	/**
	 * Constructs a {@code ServiceController} object with service object {@code SessionService}
	 * @param sService
	 * 		{@code SessionService} object desired
	 */
	public SessionController(SessionService sService) { this.sService = sService; }
	
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/****************************************************************** START GET MAPPINGS *******************************************************************/
	
	/**
	 * Method for session controller connectivity testing
	 * @return
	 * 		Connectivity phrase
	 */
	@GetMapping(path = "")
	public @ResponseBody Map<String, String> welcome() {
		
		log.info("Session screen accessed");
		return Collections.singletonMap("Result", "Welcome to the session screen of 007:Knockout");
	}
	
	/******************************************************************* END GET MAPPINGS ********************************************************************/
	
	/***************************************************************** START POST MAPPINGS *******************************************************************/
	
	/**
	 * Adds a {@code Session} to the database
	 * @param session
	 * 		See {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> addSession(@RequestBody SessionAddUpdateWrapper wrapper) {
		
		log.info("Attempting to add Session");
		
		int result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { result = sService.addSession(wrapper); }
		catch(BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "SessionInterface with code " + result + " added to the database";
		wrap.excecute(new Integer(result), message);
		log.info(message);
		return wrap;
	}
	
	/******************************************************************* END POST MAPPINGS *******************************************************************/
	
	/****************************************************************** START PUT MAPPINGS *******************************************************************/
	
	/**
	 * Gets a {@code Session} from the database
	 * @param token
	 * 		{@code Token} of the {@code Session} to get
	 * @return
	 * 		{@code ObjectReturnWrapper<SessionInterface>}
	 */
	@PutMapping(path = "/get", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<SessionInterface> getSession(@RequestBody Token token) {
		
		log.info("Attempting to get a Session");
		
		SessionInterface fetched;
		ObjectReturnWrapper<SessionInterface> wrap = new ObjectReturnWrapper<>();
		try { fetched = sService.getSession(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Session #" + fetched.getId() + " fetched";
		
		//clear sensitive info
		fetched.setToken(null);
		for (UserInterface u : fetched.getUsers()) {
			
			u.setToken(null);
			u.setPassword(null);
			u.setSession(null);
		}
		
		wrap.excecute(fetched, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets all the {@code Session}s in the database
	 * @param token
	 * 		{@code Token} of a {@code User} whose {@code authLevel} is equal to 2
	 * @return
	 * 		{@code ListReturnWrapper<SessionInterface>} that contains all the {@code Session}s in the database
	 */
	@PutMapping(path = "/get/all", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<SessionInterface> getSessions(@RequestBody Token token) {
		
		log.info("Attempting to get all Sessions");
		
		List<SessionInterface> fetched;
		ListReturnWrapper<SessionInterface> wrap = new ListReturnWrapper<>();
		try { fetched = sService.getSessions(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Number of Sessionsn fetched: " + fetched.size();
		
		//clear sensitive info
		for (SessionInterface s : fetched) {
			
			s.setToken(null);
			s.setPasscode(-1);
			for (UserInterface u : s.getUsers()) {
				
				u.setToken(null);
				u.setPassword(null);
				u.setSession(null);
			}
		}
		
		wrap.excecute(fetched, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets a {@code User}(s) in a {@code Session}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to get from and the {@code Token}(s) of the {@code User}(s) to get
	 * @return
	 * 		{@code ListReturnWrapper<UserInterface>} that contains the {@code User}(s) desired
	 */
	@PutMapping(path = "/get/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<UserInterface> getUser(@RequestBody List<Token> tokens) {
		
		log.info("Attempting to get User in a Session");
		
		List<UserInterface> fetched;
		ListReturnWrapper<UserInterface> wrap = new ListReturnWrapper<>();
		try { fetched = sService.getUser(tokens); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String usernames = "";
		for (UserInterface u : fetched) {
			
			if (fetched.indexOf(u) == fetched.size() - 1) { usernames += u.getUsername(); }
			else { usernames += u.getUsername() + ", "; }
		}
		
		String message = "Users '" + usernames + "' from a Session fetched";
		
		//clear sensitive info
		for (UserInterface u : fetched) {
			
			u.setPassword(null);
			u.setSession(null);
			u.setToken(null);
		}
		
		wrap.excecute(fetched, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets all {@code User}s in the play area of the {@code Session}
	 * @param token
	 * 		{@code Token} of the {@code Session} to grab from
	 * @return
	 * 		{@code ListReturnWrapper<UserInterface>} that contains the {@code User}s desired
	 */
	@PutMapping(path = "/get/user/inRadius", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<UserInterface> getUserInRadius(@RequestBody Token token) {
		
		log.info("Attempting to get all Users in play area of a Session");
		
		List<UserInterface> fetched;
		ListReturnWrapper<UserInterface> wrap = new ListReturnWrapper<>();
		try { fetched = sService.getUserInRadius(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		if (fetched.size() == 0) { return null; }
		
		String usernames = "";
		for (UserInterface u : fetched) {
			
			if (fetched.indexOf(u) == fetched.size() - 1) { usernames += u.getUsername(); }
			else { usernames += u.getUsername() + ", "; }
		}
		
		String message = "Users '" + usernames + "' from a Session fetched";
		
		//clear sensitive info
		for (UserInterface u : fetched) {
			
			u.setPassword(null);
			u.setSession(null);
			u.setToken(null);
		}
		
		wrap.excecute(fetched, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets all {@code User}s from a {@code Session}
	 * @param token
	 * 		{@code token} of the {@code Session} to grab from
	 * @return
	 * 		{@code ListReturnWrapper<UserInterface>} that contains all the {@code User}s in the {@code Session}
	 */
	@PutMapping(path = "/get/user/all", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<UserInterface> getAllUsers(@RequestBody Token token) {
		
		log.info("Attempting to get all Users in a Session");
		
		List<UserInterface> fetched;
		ListReturnWrapper<UserInterface> wrap = new ListReturnWrapper<>();
		try { fetched = sService.getAllUsers(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String usernames = "";
		for (UserInterface u : fetched) {
			
			if (fetched.indexOf(u) == fetched.size() - 1) { usernames += u.getUsername(); }
			else { usernames += u.getUsername() + ", "; }
		}
		
		String message = "Users '" + usernames + "' from a Session fetched";
		
		//clear sensitive info
		for (UserInterface u : fetched) {
			
			u.setPassword(null);
			u.setSession(null);
			u.setToken(null);
		}
		
		wrap.excecute(fetched, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Gets all the {@code User}s inside the view area of another {@code User}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to grab from, and the {@code Token} of the {@code User} that is asking
	 * @return
	 * 		{@code ListReturnWrapper<UserInterface>} that contains the {@code User}s in a {@code User}'s view area
	 */
	@PutMapping(path = "/get/onTap", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListMapReturnWrapper<UserInterface, Location, ItemInterface> getUsersOnTap(@RequestBody List<Token> tokens) {
		
		log.info("Attempting to get Users and/or items in another User's view radius");
		
		List<UserInterface> users;
		Map<Location, ItemInterface> items;
		SessionInterface session;
		
		ListMapReturnWrapper<UserInterface, Location, ItemInterface> wrap = new ListMapReturnWrapper<>();
		try {
			
			users = sService.getOnTap(tokens).getList();
			items = sService.getOnTap(tokens).getMap();
			session = sService.getSession(tokens.get(0));
		}
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		//Get usernames
		String usernames = "";
		for (UserInterface u : users) {
			
			if (users.indexOf(u) == users.size() - 1) { usernames += u.getUsername(); }
			else { usernames += u.getUsername() + ", "; }
		}
		
		//Get item names
		String itemNames = "";
		
//		List<ItemInterface> it = new ArrayList<>();
//		for (Entry<Location, ItemInterface> i : items.entrySet()) { it.add(i.getValue()); }	
//		for (ItemInterface i : it) {
//			
//			if (it.indexOf(i) == items.size() - 1) { itemNames += i.getName(); }
//			else { itemNames += i.getName() + ", "; }
//		}
		//Generate return message
		String message = "Users '" + usernames + "' from Session #" + session.getId() + " fetched. Items " + itemNames + " fetched";
		//clear sensitive info
		for (UserInterface u : users) {
			
			u.setPassword(null);
			u.setSession(null);
			u.setToken(null);
		}
		//Execute
		wrap.excecute(users, items, message);
		log.info(message);
		return wrap;
	}
	
	/**
	 * Generates a {@code Token} for a {@code Session}
	 * @param session
	 * 		{@code Session} to generate the {@code Token for}. Should contain the {@code name} and {@code passcode}
	 * @return
	 * 		{@code ObjectReturnWrapper<Token>} that contains the {@code Token} of the started {@code Session}
	 */
	@PutMapping(path = "/start")
	public @ResponseBody ObjectReturnWrapper<Integer> getToken(@RequestBody SessionStartUpdateWrapper wrapper) {
		
		log.info("Attempting to start Session");
		
		String message;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try  { message = sService.startSession(wrapper); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		wrap.excecute(new Integer(1), message);
		log.info(message);
		log.info(wrap.toString());
		return wrap;
	}
	
	/**
	 * Adds a {@code User} to the database
	 * @param wrapper
	 * 		See {@code SessionAddUserWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/add/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Token> addUser(@RequestBody SessionAddUserWrapper wrapper) {
		
		log.info("Attemping to add User to Session");
		
		Token result;
		ObjectReturnWrapper<Token> wrap = new ObjectReturnWrapper<>();
		try { result = sService.addUser(wrapper); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Added User to Session";
		wrap.excecute(result, message);
		log.info(message);
		
		return wrap;
	}
	
	/**
	 * Sets the {@code authenticator} of a {@code Token} to null
	 * @param token
	 * 		{@code Token} to modify
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/stop", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> stopSession(@RequestBody Token token) {
		
		log.info("Attempting to stop a session");
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { result = sService.stopSession(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Updates the {@code User}s involved in an assassination (When a {@code User} attempts to eliminate its {@code target})
	 * @param wrapper
	 * 		See {@code OnKillUpdateWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/update/onTap", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> updateOnTap(@RequestBody OnTapUpdateWrapper wrapper) {
		
		log.info("Attempting to kill player or get item");
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		int result = -1;
		try { result = sService.updateOnTap(wrapper); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "";
		
		if (result == 0) { message = "Target has been eliminated, and a new target has been aquired."; }
		else if (result == 1){ message = "Target has been eliminated, and the game is over. YOU ARE THE WINNER"; }
		
		wrap.excecute(new Integer(result), message);
		log.info(message);
		
		return wrap;
	}
	
	/**
	 * Removes a {@code User} from a {@code Session}
	 * @param tokens
	 * 		{@code List<Token>} that contains the {@code Token} of the {@code Session} to grab from, and the {@code Token} of the {@code User} to remove
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/delete/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteUser(@RequestBody List<Token> tokens) {
		
		log.info("Attempting to delete a User from a Session");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { result = sService.deleteUser(tokens); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		excecutionHandler(result, wrap);
		return wrap;
	}
	
	/**
	 * Removes all {@code User}s from a {@code Session}
	 * @param token
	 * 		{@code Token} of the {@code Session} to remove from
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@PutMapping(path = "/delete/user/all", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteAllUsers(@RequestBody Token token) {
		
		log.info("Attempting to delete all users from a Session");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { result = sService.deleteAllUsers(token); }
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
	 * Deletes a {@code Session} from the database
	 * @param token
	 * 		{@code Token} of the {@code Session} to delete
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteSession(@RequestBody Token token) {
		
		log.info("Attempting to delete a Session");
		
		String result;
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { result = sService.deleteSession(token); }
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
	
	/***************************************************************** END SERVER CONTROLLER *****************************************************************/
}
