package application.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.controllers.MainController;
import application.exceptions.BackendException;
import application.exceptions.InvalidListException;
import application.exceptions.InvalidSessionException;
import application.exceptions.InvalidTokenException;
import application.exceptions.InvalidUserException;
import application.items.ItemInterface;
import application.sessions.SessionInterface;
import application.tools.embeddables.ItemLocationWrapper;
import application.tools.embeddables.Location;
import application.tools.embeddables.Token;
import application.tools.services.SessionService;
import application.tools.services.UserService;
import application.tools.wrappers.ListMapReturnWrapper;
import application.tools.wrappers.LocationUpdateWrapper;
import application.tools.wrappers.MultiListReturnWrapper;
import application.users.UserInterface;

@ServerEndpoint(value = "/websocket/{tokenString}", configurator = CustomConfigurator.class)
@Component
public class Endpoint {
	
	// Store all socket session and their corresponding user    
	private static Map<Session, Token> sessionUsertokenMap = new HashMap<>();
    private static Map<Token, Session> usertokenSessionMap = new HashMap<>();
    private static Map<Token, Token>  usertokenSeshtokenMap   = new HashMap<>();
//    private static Map<User, Token>  usernameUsertokenMap   = new HashMap<>();
    
    private ObjectMapper objectMapper;
    
    @Autowired
    UserService uService;
    @Autowired
    SessionService seshService;
	
	/**
	 * Logger object
	 */
	private final Logger log = LoggerFactory.getLogger(MainController.class);
	
	/**
	 * Constructs websocket class
	 * @param uService service for storing/ retreaving users
	 * @param seshService service for accessing sessions
	 */
	public Endpoint(UserService uService, SessionService seshService) { 
		objectMapper = new ObjectMapper();
		this.uService = uService;
		this.seshService = seshService;
		
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);//some objects don't need every variable
	}
	
	/**
	 * method that is called when new user connects to web socket
	 * @param session session which the user is connecting with
	 * @param tokenString string represtation of player token to grab player data
	 * @throws IOException invalid connection
	 * @throws InvalidTokenException invalid token
	 */
	@OnOpen
    public void onOpen(
    		  Session session,
    	      @PathParam("tokenString") String tokenString) throws IOException, InvalidTokenException {
       
		log.info("System: new client entered @OnOpen");
		log.info("System: new client sent: " + tokenString);
		if (tokenString == null) {
			
			log.info("Token String was null"); 
			throw new NullPointerException();
		}
		
		//look for comma seperating string and long values
		int parse = tokenString.indexOf(',', 0);
		if(parse == -1) {
			log.info("Error:  Could not find ',' in token string");
			throw new IllegalArgumentException("Could not find ',' in token string");
		}
		Token token = new Token();
		token.setAuthenticator(tokenString.substring(0, parse));//set string
		token.setExpiration(Long.parseLong(tokenString.substring(parse + 1, tokenString.length())));//set long
		
		UserInterface user;
		try {
			user = uService.getUser(token); //passes token to service to get user data back
		} catch (InvalidTokenException e) {
			e.printStackTrace();
			log.info("Error:  " + e.getMessage());
			throw new InvalidTokenException("Could not find user for given token");
		}
		
		log.info("System: " + user.getUsername() + " Opened endpoint" + getTime());
		
		//store data into maps for future use
		sessionUsertokenMap.put(session, user.getToken());
		usertokenSessionMap.put(user.getToken(), session);
		}
	
	/**
	 * method runs when user sends a string to websockets
	 * @param session in which user is connected to websockets
	 * @param message string containing intent and a json
	 * @throws IOException bad connection
	 */
	@OnMessage
    public void onMessage(Session session, String message) throws IOException {
		//log message
    	try {
			log.info("Input:  " + uService.getUser(sessionUsertokenMap.get(session)).getUsername()+ ": " + message + getTime());
		} catch (BackendException e) {
			e.printStackTrace();
		}
    	
    	//parse for intent
    	getIntent(message, sessionUsertokenMap.get(session));
    }
	
	@OnClose
	public void close(Session session) throws IOException {
    	
    	UserInterface user = null;
		try {
			user = uService.getUser(sessionUsertokenMap.get(session));
		} catch (BackendException e) {
			e.printStackTrace();
		}
    	log.info("System: " + user.getUsername() + " entered @OnClose" + getTime());
    	
    	sessionUsertokenMap.remove(session);
    	usertokenSessionMap.remove(user.getToken());
	}
	
	/**
	 * method that runs when an error is thrown
	 * @param t error that was thrown
	 */
	@OnError
	public void onError(Throwable t) {
        log.info("Error:  " + t.getMessage());
    }
	
	//send to everyone in session
	private void broadcastBuffer(String message, Token sesh) {
		log.info("Output: to all \"" + message + "\"");
		broadcast(message, sesh);
	}
	
	private static void broadcast(String message, Token sesh) {	
		for (Token u : sessionUsertokenMap.values()) {
			if (usertokenSeshtokenMap.get(u) != null)
				if (usertokenSeshtokenMap.get(u).getAuthenticator().equals(sesh.getAuthenticator())) {
					try {
						usertokenSessionMap.get(u).getBasicRemote().sendText(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		}
	}
	
	private void whisperBuffer(String message, Token user) {
		try {
			log.info("Output: to " + uService.getUser(user).getUsername() + " \"" + message + "\"");
		} catch (BackendException e) {
			e.printStackTrace();
		}
		int ret = whisper(message, user);
        log.info("returned: " + ret + " map size: " + 
                usertokenSessionMap.size());
	}
	
	private static int whisper(String message, Token user){
		int ret = 0;
            try {
            	if(usertokenSessionMap.containsKey(user)) ret += 10;
				Session u = usertokenSessionMap.get(user);
				ret++;
				Basic x = u.getBasicRemote();
				ret++;
				x.sendText(message);
				ret++;
			} catch (Exception e) {
				e.printStackTrace();
				return ret;
			}
			return ret;
	}
	
	private static String getTime() {
		return String.format("@ Date: %d/%d, Time: %d:%d",
				Calendar.MONTH+1, Calendar.DAY_OF_MONTH+1, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
	}
	
/*******************************************************************************************************************************************************************
End of webSocket methods

Start of intent methods
*******************************************************************************************************************************************************************/
	
	/**
	 * 
	 * @param JSon String starting with a three digit intent and ends with a Json object
	 * @param user user session calling this method
	 * @return whether method has run successfully or not
	 */
	private boolean getIntent(String jSon, Token userToken) {
		//create string to hold error message if there is one
		String errorMsg = new String();
		
		//grab the integer from front of string
		int intent;
		try {
			intent = Integer.parseInt(jSon.substring(0, 3));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			log.info("Error:  " + e.getLocalizedMessage());
			whisperBuffer(String.format("{\"intent\":400, \"reason\":\"error on intent N/A: %s\",\"object\":{}}", e.getLocalizedMessage()), userToken);
			return false;
		}
		jSon = jSon.substring(3);//removes the intent from the front
		
		log.info("Token recieved: " + userToken.toString());
		
		switch(intent) {
			case 200: errorMsg = addSessionToken(userToken);
				break;
			case 201: errorMsg = updateLocation(jSon, userToken);
				break;
			case 202: errorMsg = findPlayersInRadius(userToken);
				break;
			case 203: errorMsg = "";
				break;
			case 207: errorMsg = getLeaderboard(userToken);
		}
		
		if(errorMsg.isEmpty()) {
			log.info(String.format("System: intent %d was successful", intent));
		} else {
			log.info("Error:  " + errorMsg);
			whisperBuffer(String.format("{\"intent\":400, \"reason\":\"error on intent %d: %s\",\"object\":{}}", intent, errorMsg), userToken);
			return false;
		}
		
		return true;
	}

	/** 100
	 * Converts json to session token to allow user to access the game session
	 * @param jSon must be in the form of "100{"Authenticator":"adfgsdoaigsda","expiration":11234132513}"
	 * @param user user that is connecting to a session
	 * @return error message if there was one
	 */
	private String addSessionToken(Token userToken) {
		
		Token seshToken = null;
		UserInterface user = null;
		try {
			user = uService.getUser(userToken);
		} catch (BackendException e1) {
			e1.printStackTrace();
		}
		log.info("user: '" + user.getUsername() + "'");
		//get session token off of user
		try {
			log.info("Getting session from user");
			List<Token> tokens = new ArrayList<>();
			
			for(Token t : user.getSession()) { tokens.add(t); }
			
			if (tokens.isEmpty()) {
				log.info("no tokens");
				return null;
			}
			seshToken = tokens.get(0);
			log.info("200: Got Session Token from user '" + user.getUsername() + "'");
		} catch (Exception e) {
			log.info(e.getStackTrace().toString());
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		//check if token is valid
		application.sessions.Session curr;
		try {
			curr = (application.sessions.Session) seshService.getSession(seshToken);
			log.info("200: Got Session from session token");
		} catch (InvalidTokenException | InvalidSessionException e) {
			log.info(e.getStackTrace().toString());
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		if(usertokenSeshtokenMap.get(userToken) != null)
			return addSessionTokenWhisper(userToken, curr);
		
		//store session token into map
		usertokenSeshtokenMap.put(userToken, seshToken);
		
		//get list of all players in same session
		List<UserInterface> userList = curr.getUsers();
		
		//broadcast new list of users to everyone in session
		String s = new String("{\"intent\":100, \"reason\":\"Players in your session\",\"object\":{\"users\": [") ;
		for(UserInterface u : userList) {
			s += String.format("{\"username\": \"%s\"},", u.getUsername());
			}
		s = s.substring(0, s.length()-1);
		s += String.format("]}, \"passcode\":%d}", curr.getPasscode());
		
		broadcastBuffer(s, seshToken);
		
		return "";
}
	private String addSessionTokenWhisper(Token userToken, application.sessions.Session curr) {
		
		UserInterface user = null;
		try {
			user = uService.getUser(userToken);
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		}
		
		log.info("200: sending whisper to user '" + user.getUsername() + "'");
		
		// get list of all players in same session
		List<UserInterface> userList = new ArrayList<>();

		userList = curr.getUsers();
		
		String s = new String("{\"intent\":100, \"reason\":\"Players in your session\",\"object\":{\"users\": [") ;
		for(UserInterface u : userList) {
			s += String.format("{\"username\": \"%s\"},", u.getUsername());
			}
		s = s.substring(0, s.length()-1);
		s += String.format("]}, \"passcode\":%d}", curr.getPasscode());
		
		whisperBuffer(s, userToken);
		
		return "";
	}

	/**101
	 * @param jSon
	 * @param user
	 * @return whether method is successful or not
	 */
	private String updateLocation(String jSon, Token userToken) {
		Location l;
		
		try {
			l = objectMapper.readValue(jSon, Location.class);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		LocationUpdateWrapper luw = new LocationUpdateWrapper();
		luw.setNewLocation(l);
		
		luw.setToUpdate(userToken);
		
		try {
			uService.updateUserLocation(luw);
			uService.setTimeUpdated(userToken, System.currentTimeMillis());
			//user.setLocation(uService.getUser(usernameUsertokenMap.get(user)).getLocation());
		} catch (InvalidUserException | InvalidTokenException e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		return "";
	}
	
	/**102
	 * @param user
	 * @return
	 */
private String findPlayersInRadius(Token userToken) {
		
		ListMapReturnWrapper<UserInterface, Location, ItemInterface> wrapper = null;
		List<Token> list = new ArrayList<>();
		list.add(usertokenSeshtokenMap.get(userToken));
		list.add(userToken);
		
//		List<Token> userTokens = new ArrayList<>();
//		List<Token> haveSessions = new ArrayList<>();
//		for (Token t : sessionUsertokenMap.values()) { haveSessions.add(t); }
		
		for(Token t : sessionUsertokenMap.values()) {
			//check if in same game session
			if(usertokenSeshtokenMap.get(t) != null)
				if(usertokenSeshtokenMap.get(t).getAuthenticator().equals(usertokenSeshtokenMap.get(userToken).getAuthenticator()))
					whisperBuffer("{\"intent\":101, \"reason\":\"Need your location\",\"object\":{\"Message\":\"Need your location\"}}",t);
		}
		
//		long startTime = System.currentTimeMillis();
//		long timeout = startTime + 10000;
//		while (System.currentTimeMillis() < timeout || !haveSessions.isEmpty()) {
//			for (Token t : haveSessions) {
//				try {
//					if (uService.getTimeUpdated(t) > startTime) {
//							
//						userTokens.add(t);
//						haveSessions.remove(t);
//						log.info("Reply from: " + uService.getUser(t));
//					}
//				} catch (InvalidTokenException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
		
		log.info("442: %d", 15000 - System.currentTimeMillis());
		
		try {
			SessionInterface sesh = seshService.getSession(list.get(0));
			UserInterface user = uService.getUser(list.get(1));
			
			log.info("Session Getting from: " + sesh.getId());
			log.info("User Getting around: " + user.getUsername());
		} catch (InvalidTokenException | InvalidSessionException e1) {
			e1.printStackTrace();
		}
		
		try {
			wrapper = seshService.getOnTap(list);
			wrapper.getList().remove(uService.getUser(userToken));
//			userTokens.remove(userToken);
			wrapper.getList().remove(uService.getUser(userToken));
		} catch (InvalidListException | InvalidTokenException | InvalidSessionException | InvalidUserException e) {
			e.printStackTrace();
		}
		
		log.info("462");
		
		if(/*userTokens.isEmpty()*/ wrapper.getList().isEmpty()) {//if empty return an empty array
			log.info("465: wrapper list empty");
			whisperBuffer("{\"intent\":102, \"reason\":\"Players in your radius\",\"object\":{\"users\": [], \"items\"[]}}", userToken);
			return "";
		}
		
//		List<UserInterface> users = new ArrayList<>();
//		for(Token t : userTokens) { 
//			try {
//				users.add(uService.getUser(t));
//			} catch (InvalidTokenException e1) {
//				e1.printStackTrace();
//			} 
//		}
		
		//populate string with user info
		String s = new String("{\"intent\":102, \"reason\":\"Players in your radius\",\"object\":{\"users\": [") ;
		
		for(UserInterface u : /*users*/ wrapper.getList()) {
			s += String.format("{\"username\": \"%s\", \"Location\": { \"latitude\":%f,\"longitude\":%f}},",
								u.getUsername(), u.getLocation().getLatitude(), u.getLocation().getLongitude());
			//tell user they've been spotted
			whisperBuffer("{\"intent\":103, \"reason\":\"You've been spotted\",\"object\":{\"Message\":\"You've been spotted\"}", u.getToken());
		}
		
		log.info("487");
		
		//remove ',' off of end of string
		s = s.substring(0, s.length()-1);
		s += "], \"items\": [";
		
		if (!wrapper.getMap().isEmpty()) {
			
			for (Entry<Location, ItemInterface> e : wrapper.getMap().entrySet()) {
				
				s += String.format("{\"name\": \"%s\", \"Location\": { \"latitude\":%f,\"longitude\":%f}},", e.getValue().getName(),
						e.getKey().getLatitude(), e.getKey().getLongitude());
			}
			
			s = s.substring(0, s.length()-1);
		}
		s += "]}}";
		whisperBuffer(s, userToken);
		
		return "";
	}
	
	//207
	private String getLeaderboard(Token userToken) {
		List<UserInterface> userList  = new ArrayList<>();

		try {
			userList = seshService.getAllUsers(usertokenSeshtokenMap.get(userToken));//get all users in sesh
		} catch (InvalidTokenException | InvalidSessionException e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		
		// populate string with user info
		String s = new String("{\"intent\":107, \"reason\":\"Player leaderboard\",\"object\":{\"users\": [");
		for (UserInterface u : userList) {
			s += String.format("{\"username\": \"%s\", \"KD Ratio\":%f},",
					u.getUsername(), u.getKDRatio());
		}

		// remove ',' off of end of string
		if(!userList.isEmpty())
			s = s.substring(0, s.length() - 1);
		s += "]}}";
		whisperBuffer(s, userToken);
		
		return "";
	}
}
