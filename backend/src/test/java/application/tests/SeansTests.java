package application.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import application.sessions.Session;
import application.sessions.SessionInterface;
import application.tools.embeddables.Location;
import application.tools.services.SessionService;
import application.users.User;
import application.users.UserInterface;


public class SeansTests {
	
	UserInterface u;
	SessionInterface s;
	Location l;
	SessionService sService;
	
	@Before
	public void setUp() {
		
		u = mock(UserInterface.class);
		s = mock(SessionInterface.class);
		l = mock(Location.class);
	}
	
	/**
	 * Tests if adding a user to a session works
	 */
	@Test
	public void testSessionAddUser() {
		
		List<UserInterface> users = new ArrayList<>();
		
		Mockito.when(u.getUsername()).thenReturn("sicko");
		Mockito.when(u.getPassword()).thenReturn("mode");
		users.add(new User(u.getUsername(), u.getPassword()));
		
		Mockito.when(u.getUsername()).thenReturn("mo");
		Mockito.when(u.getPassword()).thenReturn("bamba");
		users.add(new User(u.getUsername(), u.getPassword()));
		
		Mockito.when(u.getUsername()).thenReturn("fu");
		Mockito.when(u.getPassword()).thenReturn("ck");
		users.add(new User(u.getUsername(), u.getPassword()));
		
		Mockito.when(u.getUsername()).thenReturn("sh");
		Mockito.when(u.getPassword()).thenReturn("it");
		users.add(new User(u.getUsername(), u.getPassword()));
		
		s = new Session();
		
		for (UserInterface u : users) { s.addUser(u); }
		
		assertEquals(users, s.getUsers());
	}
	
	/**
	 * Tests if getUsersInRadius of Session works
	 */
	@Test
	public void testSessionInRadius() {

		List<UserInterface> users = new ArrayList<>();
		
		//In play area
		Mockito.when(u.getUsername()).thenReturn("sicko");
		Mockito.when(u.getPassword()).thenReturn("mode");
		Mockito.when(l.getLatitude()).thenReturn(41.6123374111);
		Mockito.when(l.getLongitude()).thenReturn(-95.5190119746);
		users.add(new User(u.getUsername(), u.getPassword(), 0, new Location(l.getLatitude(), l.getLongitude(), 0)));
		
		//In play area
		Mockito.when(u.getUsername()).thenReturn("mo");
		Mockito.when(u.getPassword()).thenReturn("bamba");
		Mockito.when(l.getLatitude()).thenReturn(41.6123474111);
		Mockito.when(l.getLongitude()).thenReturn(-95.5190119746);
		users.add(new User(u.getUsername(), u.getPassword(), 0, new Location(l.getLatitude(), l.getLongitude(), 0)));
		
		//Not in play area
		Mockito.when(u.getUsername()).thenReturn("fu");
		Mockito.when(u.getPassword()).thenReturn("ck");
		Mockito.when(l.getLatitude()).thenReturn(41.6124374111);
		Mockito.when(l.getLongitude()).thenReturn(-95.5190119746);
		users.add(new User(u.getUsername(), u.getPassword(), 0, new Location(l.getLatitude(), l.getLongitude(), 0)));
		
		//In play area
		Mockito.when(u.getUsername()).thenReturn("sh");
		Mockito.when(u.getPassword()).thenReturn("it");
		Mockito.when(l.getLatitude()).thenReturn(41.6123374112);
		Mockito.when(l.getLongitude()).thenReturn(-95.5190119747);
		users.add(new User(u.getUsername(), u.getPassword(), 0, new Location(l.getLatitude(), l.getLongitude(), 0)));
		
		
		Mockito.when(l.getLatitude()).thenReturn(41.6123374111);
		Mockito.when(l.getLongitude()).thenReturn(-95.5190119746);
		s = new Session("MinecraftChristianServer", new Location(l.getLatitude(), l.getLongitude(), 0), 10);
		
		List<UserInterface> uInPlayArea = new ArrayList<>();
		
		for (UserInterface u : users) {
			
			if (s.inPlayArea(u.getLocation())) { uInPlayArea.add(u); }
		}
		
		List<UserInterface> correct = new ArrayList<>();
		correct.add(users.get(0));
		correct.add(users.get(1));
		correct.add(users.get(3));
		
		assertEquals(uInPlayArea, correct);
	}
	
	/**
	 * tests if Location.getDistance returns the correct value
	 */
	@Test
	public void testLocationDistance() {
		
		u = new User();
		
		Mockito.when(l.getLatitude()).thenReturn(41.6123374111);
		Mockito.when(l.getLongitude()).thenReturn(-95.5190119746);
		u.setLocation(new Location(l.getLatitude(), l.getLongitude(), 0));
		
		Mockito.when(l.getLatitude()).thenReturn(41.6131889177);
		Mockito.when(l.getLongitude()).thenReturn(-95.4960621479);
		Location number2 = new Location(l.getLatitude(), l.getLongitude(), 0);
		
		if (u.getLocation().getDistance(number2) < 1910 || u.getLocation().getDistance(number2) >= 1911) { fail(); }
		assertTrue(true);
	}
	
	/**
	 * Tests if updating a User's username and/or password works
	 */
	@Test
	public void testUserUpdateInfo() {

		Mockito.when(u.getUsername()).thenReturn("sicko");
		Mockito.when(u.getPassword()).thenReturn("mode");
		UserInterface user = new User(u.getUsername(), u.getPassword());
		
		assertEquals(user.getUsername(), u.getUsername());
		assertEquals(user.getPassword(), u.getPassword());
		
		Mockito.when(u.getUsername()).thenReturn("mo");
		Mockito.when(u.getPassword()).thenReturn("bamba");
		user.updateInfo(new User(u.getUsername(), u.getPassword()));
		
		assertEquals(user.getUsername(), u.getUsername());
		assertEquals(user.getPassword(), u.getPassword());
	}
}
