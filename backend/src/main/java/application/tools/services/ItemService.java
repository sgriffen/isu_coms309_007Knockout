package application.tools.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import application.exceptions.InvalidAdministratorException;
import application.exceptions.InvalidItemException;
import application.exceptions.InvalidTokenException;
import application.items.ItemRepository;
import application.items.ItemInterface;
import application.tools.embeddables.Token;
import application.tools.wrappers.ItemGetWrapper;
import application.users.User;
import application.users.UserInterface;
import application.users.UserRepository;

/**
 * Service for {@code ItemController}
 * 
 * @author Sean Griffen
 */
@Service
public class ItemService {
	
	/************************************************************** START VARIABLE DECLARATIONS **************************************************************/
	
	/**
	 * Item Repository accessed by this service
	 */
	private ItemRepository iRepo;
	/**
	 * User Repository accessed by this service
	 */
	private UserRepository uRepo;
	
	/*************************************************************** END VARIABLE DECLARATIONS ***************************************************************/
	
	/****************************************************************** START CONSTRUCTORS *******************************************************************/
	
	/**
	 * Constructs an {@code ItemService} object with repository object {@code ItemRepository}
	 * @param iRepo
	 * 		{@code ItemRepository} object desired
	 * @param uRepo
	 * 		{@code UserRepository} object desired
	 */
	public ItemService (ItemRepository iRepo, UserRepository uRepo) {
		
		this.iRepo = iRepo;
		this.uRepo = uRepo;
	}
	
	/******************************************************************* END CONSTRUCTORS ********************************************************************/
	
	/******************************************************************* START GET HELPERS *******************************************************************/
	
	/******************************************************************** END GET HELPERS ********************************************************************/
	
	/****************************************************************** START POST HELPERS *******************************************************************/
	
	/******************************************************************** END POST HELPERS *******************************************************************/
	
	/******************************************************************* START PUT HELPERS  ******************************************************************/
	
	/**
	 * Gets an {@code Item} from the database
	 * @param wrap
	 * 		See {@code ItemGetWrapper}
	 * @return
	 * 		{@code Item} that has a {@code id} that matched the one inputted
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code User} does not have an {@code authLevel} equal to 2
	 * @throws InvalidItemException
	 * 		Throws if {@code Item} does not exist in the database
	 */
	public ItemInterface getItem(ItemGetWrapper wrap) throws InvalidTokenException, InvalidAdministratorException, InvalidItemException {
		
		if (!iRepo.existsById(wrap.getToGet())) { throw new InvalidItemException("ID does not match one of an existing Item."); }
		
		validateAdminToken(wrap.getAdmin());
		
		return iRepo.findById(wrap.getToGet()).get();
	}
	
	/**
	 * Gets all {@code Item}s in the database
	 * @param token
	 * 		{@code Token} of {@code User} with {@code authLevel} equal to 2
	 * @return
	 * 		{@code List<ItemInterface>} that contains all {@code Item}s in the database
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code User} does not have an {@code authLevel} equal to 2
	 */
	public List<ItemInterface> getItems(Token token) throws InvalidTokenException, InvalidAdministratorException {
		
		validateAdminToken(token);
		
		List<ItemInterface> result = new ArrayList<>();
		
		for (ItemInterface i : iRepo.findAll()) { result.add((ItemInterface) i); }
		
		return result;
	}
	
	/******************************************************************** END PUT HELPERS ********************************************************************/
	
	/***************************************************************** START DELETE HELPERS ******************************************************************/
	
	/**
	 * Deletes an {@code Item} from the database
	 * @param wrap
	 * 		See {@code ItemGetWrapper}
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired or 
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code User} does not have an {@code authLevel} equal to 2
	 * @throws InvalidItemException
	 * 		Throws if {@code Item} does not exist in the database
	 */
	public void deleteItem(ItemGetWrapper wrap) throws InvalidTokenException, InvalidAdministratorException, InvalidItemException {
		
		if (!iRepo.existsById(wrap.getToGet())) { throw new InvalidItemException("ID does not match one of an existing Item."); }
		
		validateAdminToken(wrap.getAdmin());
		
		iRepo.deleteById(wrap.getToGet());
	}
	
	/**
	 * Deletes all {@code Item}s from the database
	 * @param token
	 * 		{@code Token} of {@code User} with {@code authLevel} equal to 2
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 * 		Throws if {@code User} does not have an {@code authLevel} equal to 2
	 */
	public void deleteAll(Token token) throws InvalidTokenException, InvalidAdministratorException {
		
		validateAdminToken(token);
		
		iRepo.deleteAll();
	}
	
	/******************************************************************* END DELETE HELPERS ******************************************************************/
	
	/***************************************************************** START PRIVATE METHODS *****************************************************************/
	
	/**
	 * Gets an {@code User} from the database based on an inputted {@code Token}
	 * @param token
	 * 		{@code Token} for the {@code User} trying to get
	 * @return
	 * 		{@code User} associated with {@code token}
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing or matching {@code authenticator} or was expired
	 * 
	 */
	private UserInterface getUserToken(Token token) throws InvalidTokenException {
		
		List<User> users = uRepo.findAll();
		
		for (UserInterface u : users) {
			
			if (token.getAuthenticator().equals(u.getToken().getAuthenticator())) { return u; }
		}
		
		throw new InvalidTokenException("Token entered does not correspond to an existing User");
	}
	
	/**
	 * Validates a {@code Token} by checking if the {@code expiration} is less than the {@code System.currentTime}
	 * Valid if {@code expiration} is less than {@code System.currentTime}, invalid otherwise
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
	 * Validates that a {@code Token} is associated with an {@code User} with {@code authLevel} equal to 2
	 * @param toValidate
	 * 		{@code Token} to validate
	 * @throws InvalidTokenException
	 * 		Throws if {@code Token} did not have an existing matching {@code authenticator} or was expired
	 * @throws InvalidAdministratorException
	 */
	private void validateAdminToken(Token toValidate) throws InvalidTokenException, InvalidAdministratorException {
		
		//Can't do anything if token is null
		if (toValidate == null) { throw new InvalidTokenException("Token entered is null"); }
		
		//If token is expired, create a new token
		validateUserToken(toValidate);
		
		UserInterface u = getUserToken(toValidate);
		
		if (u.getAuthLevel() != 2) { throw new InvalidAdministratorException("Administrator entered is not valid. Check username, password, and/or id"); }
	}
	
	/****************************************************************** END PRIVATE METHODS ******************************************************************/
	
	/****************************************************************** END ITEM CONTROLLER ******************************************************************/
}
