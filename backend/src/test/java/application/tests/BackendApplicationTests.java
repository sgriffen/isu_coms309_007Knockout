package application.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import application.BackendApplication;
import application.webSocket.WebSocketConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes= {WebSocketConfig.class})
public class BackendApplicationTests {

	@Test
	public void contextLoads() {
	}
}

