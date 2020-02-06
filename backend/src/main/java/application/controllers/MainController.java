package application.controllers;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import application.tools.services.MainService;

/**
 * Main controller for non-specific types
 * 
 * @author Theodore Davis
 * @author Sean Griffen
 * 
 */
@RestController
@RequestMapping(path = "")
public class MainController {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Service for this controller
	 */
	private MainService mService;
	
	/**
	 * Logger object
	 */
	private final Logger log = LoggerFactory.getLogger(MainController.class);
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/****************************************************************** START CONSTRUCTORS *******************************************************************/
	
	/**
	 * Constructs a {@code MainController} object with service object {@code MainService}
	 * @param mService
	 * 		{@code MainService} object desired
	 */
	public MainController(MainService mService) { this.mService = mService; }
	
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/****************************************************************** START GET MAPPINGS *******************************************************************/
	
	/**
	 * Welcome page
	 * @return
	 * 		Connectivity phrase
	 */
	@GetMapping(path = "")
	public @ResponseBody Map<String, String> welcome() {
		
		log.info("Welcome screen accessed");
		return Collections.singletonMap("Result", "Welcome to the backend division of 007:Knockout");
	}
	
	/******************************************************************* END GET MAPPINGS ********************************************************************/
	
	/***************************************************************** END MAIN CONTROLLER *******************************************************************/
}
