package application.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import application.exceptions.BackendException;
import application.items.ItemInterface;
import application.tools.embeddables.Token;
import application.tools.services.ItemService;
import application.tools.wrappers.ItemGetWrapper;
import application.tools.wrappers.ListReturnWrapper;
import application.tools.wrappers.ObjectReturnWrapper;
import application.tools.wrappers.ReturnWrapper;

/**
 * Controller for all objects that extend ItemInterface
 * 
 * @author Theodore Davis
 * @author Sean Griffen
 */
@RestController
@RequestMapping(path = "/item")
public class ItemController {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Service for this controller
	 */
	private ItemService iService;
	
	/**
	 * Logger object
	 */
	private final Logger log = LoggerFactory.getLogger(MainController.class);
	
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/****************************************************************** START CONSTRUCTORS *******************************************************************/
	
	/**
	 * Constructs an {@code ItemController} object with service object {@code ItemService}
	 * @param iService
	 * 		{@code ItemService} object desired
	 */
	public ItemController(ItemService iService) { this.iService = iService; }
	
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/****************************************************************** START GET MAPPINGS *******************************************************************/
	
	/**
	 * Method for item controller connectivity testing
	 * @return
	 * 		Connectivity phrase
	 */
	@GetMapping(path = "")
	public @ResponseBody Map<String, String> welcome() {
		
		log.info("Item screen accessed");
		return Collections.singletonMap("Result", "Welcome to the item screen of 007:Knockout");
	}
	
	/******************************************************************* END GET MAPPINGS ********************************************************************/
	
	/***************************************************************** START POST MAPPINGS *******************************************************************/
	
	/******************************************************************* END POST MAPPINGS *******************************************************************/
	
	/****************************************************************** START PUT MAPPINGS *******************************************************************/
	
	/**
	 * Gets an {@code Item} from the database
	 * @param wrapper
	 * 		See {@code ItemGetWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<ItemInterface>}
	 */
	@PutMapping(path = "/get", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<ItemInterface> getItem(@RequestBody ItemGetWrapper wrapper) {
		
		ItemInterface item;
		ObjectReturnWrapper<ItemInterface> wrap = new ObjectReturnWrapper<>();
		try { item = iService.getItem(wrapper); } 
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Item #" + item.getId() + " fetched";
		
		wrap.excecute(item, message);
		log.info(message);
		
		return wrap;
	}
	
	/**
	 * Gets all {@code Item}s in the database
	 * @param token
	 * 		{@code Token} of a {@code User} with an {@code authLevel} equal to 2
	 * @return
	 * 		{@code ListReturnWrapper<ItemInterface>} containing all {@code Item}s in the database
	 */
	@PutMapping(path = "/get/all", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ListReturnWrapper<ItemInterface> getAllItems(@RequestBody Token token) {
		
	    List<ItemInterface> items;
	    ListReturnWrapper<ItemInterface> wrap = new ListReturnWrapper<>();
	    try { items = iService.getItems(token); }
	    catch (BackendException e) {
	    	
	    	exceptionHandler(e, wrap);
	    	return wrap;
	    }
	    
	    String message = "Number of Items Fetched:" + items.size();
	    
	    wrap.excecute(items, message);
	    log.info(message);
	    
	    return wrap;
	}
	
	/******************************************************************* END PUT MAPPINGS ********************************************************************/
	
	/**************************************************************** START DELETE MAPPINGS ******************************************************************/
	
	/**
	 * Deletes an {@code Item} from the database
	 * @param wrapper
	 * 		See {@code ItemGetWrapper}
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteItem(@RequestBody ItemGetWrapper wrapper) {
		
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { iService.deleteItem(wrapper); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "Item #" + wrapper.getToGet() + " deleted from database";
		excecutionHandler(message, wrap);
		
		return wrap;
	}
	
	/**
	 * Deletes all {@code Item}s from the database
	 * @param token
	 * 		{@code Token} of a {@code User} with an {@code authLevel} equal to 2
	 * @return
	 * 		{@code ObjectReturnWrapper<Integer>}
	 */
	@DeleteMapping(path = "/delete/all", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ObjectReturnWrapper<Integer> deleteAll(@RequestBody Token token) {
		
		ObjectReturnWrapper<Integer> wrap = new ObjectReturnWrapper<>();
		try { iService.deleteAll(token); }
		catch (BackendException e) {
			
			exceptionHandler(e, wrap);
			return wrap;
		}
		
		String message = "All Items removed from the database";
		excecutionHandler(message, wrap);
		
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
	
	/****************************************************************** END ITEM CONTROLLER ******************************************************************/
}
