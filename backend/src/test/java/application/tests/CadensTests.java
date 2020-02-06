package application.tests;

import static org.mockito.Mockito.mock;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import application.webSocket.Endpoint;

@RunWith(MockitoJUnitRunner.class)
public class CadensTests {
	private final String USERTOKEN = "";
	
	private Socket clientMock;

	@Before
	public void setUp() {
		clientMock = mock(Socket.class);
	}
	
	@Test
	public void opens() {
		//clientMock.connect("ws://localhost:8080/websocket/" + USERTOKEN);
	}
}
