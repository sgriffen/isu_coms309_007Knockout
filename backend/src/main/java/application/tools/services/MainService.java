package application.tools.services;

import org.springframework.stereotype.Service;

import application.items.*;
import application.users.*;

/**
 * Service for {@code MainController}
 * 
 * @author Sean Griffen
 * @author Theodore Davis
 */
@Service
public class MainService {
	
	/**
	 * {@code UserRepository} accessed by this service
	 */
	private UserRepository uRepo;
	
	/**
	 * {@code ItemRepository} accessed by this service
	 */
	private ItemRepository iRepo;
	
	/**
	 * Constructs a {@code MainController} object with repository objects {@code UserRepository} and {@code ItemRepository}
	 * @param uRepo
	 * 		{@code UserRepository} object desired
	 * @param iRepo
	 * 		{@code ItemRepository} object desired
	 */
	public MainService (UserRepository uRepo, ItemRepository iRepo) {
		
		this.uRepo = uRepo;
		this.iRepo = iRepo;
	}
}
