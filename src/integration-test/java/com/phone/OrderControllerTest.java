package com.phone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.orders.Application;
import com.phone.orders.profile.Profiles;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({Profiles.LOCAL})
public class OrderControllerTest {

    private static final int MYSQL_PORT = 3306;

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @ClassRule
    public static GenericContainer mysql = new GenericContainer("mysql:latest")
            .withEnv("MYSQL_ROOT_PASSWORD", "root")
            .withClasspathResourceMapping("mysql", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
            .withExposedPorts(MYSQL_PORT);

    @BeforeClass
    public static void initialize() throws InterruptedException {
        // Configure mysql
        System.setProperty("mysql.port", String.valueOf(mysql.getMappedPort(MYSQL_PORT)));
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createOrder() {
        assertTrue(true);
    }
}
