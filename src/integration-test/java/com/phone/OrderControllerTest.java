package com.phone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.orders.Application;
import com.phone.orders.core.Currency;
import com.phone.orders.core.OrderEntity;
import com.phone.orders.core.OrderRepository;
import com.phone.orders.core.OrderRequest;
import com.phone.orders.core.OrderResponse;
import com.phone.orders.core.PhoneOrder;
import com.phone.orders.core.PhonePrice;
import com.phone.orders.core.PhonePriceList;
import com.phone.orders.core.PhoneService;
import com.phone.orders.core.RestPath;
import com.phone.orders.profile.Profiles;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({Profiles.LOCAL})
public class OrderControllerTest {

    private static final int MYSQL_PORT = 3306;

    private static final Long ORDER_ID_1 = 1L;
    private static final String FIRST_NAME = "FirstName";
    private static final String SECOND_NAME = "SecondName";
    private static final String EMAIL = "test@gmail.email";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private PhoneService phoneService;

    @ClassRule
    public static GenericContainer mysql = new GenericContainer("mysql:latest")
            .withEnv("MYSQL_ROOT_PASSWORD", "root")
            .withClasspathResourceMapping("mysql", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
            .withExposedPorts(MYSQL_PORT);

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void initialize() throws InterruptedException {
        // Configure mysql
        System.setProperty("mysql.port", String.valueOf(mysql.getMappedPort(MYSQL_PORT)));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
    }

    @After
    public void cleanUp() {
        orderRepository.deleteAll();
    }

    @Test
    public void shouldCreateOrder() throws Exception {

        final PhonePrice price = new PhonePrice(1L, 100, Currency.EUR);
        final PhonePriceList priceList = new PhonePriceList(Lists.newArrayList(price));

        when(restTemplate.getForObject(any(String.class), eq(PhonePriceList.class))).thenReturn(priceList);

        final List<PhoneOrder> orders = Lists.newArrayList();
        orders.add(PhoneOrder.builder().id(ORDER_ID_1).count(1).build());

        final OrderRequest orderRequest = new OrderRequest(FIRST_NAME, SECOND_NAME, EMAIL, orders);

        mockMvc.perform(post(RestPath.Order.ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        final List<OrderEntity> all = orderRepository.findAll();

        assertTrue(all.size() == 1);

        final OrderEntity orderEntity = all.get(0);

        assertEquals(FIRST_NAME, orderEntity.getName());
        assertEquals(SECOND_NAME, orderEntity.getSurname());
        assertEquals(EMAIL, orderEntity.getEmail());
        assertEquals(100, orderEntity.getTotalPrice());
    }

    @Test
    public void shouldSumOrderPrice() throws Exception {

        final PhonePrice price = new PhonePrice(1L, 100, Currency.EUR);
        final PhonePriceList priceList = new PhonePriceList(Lists.newArrayList(price));

        when(restTemplate.getForObject(any(String.class), eq(PhonePriceList.class))).thenReturn(priceList);

        final List<PhoneOrder> orders = Lists.newArrayList();
        orders.add(PhoneOrder.builder().id(ORDER_ID_1).count(2).build());

        final OrderRequest orderRequest = new OrderRequest(FIRST_NAME, SECOND_NAME, EMAIL, orders);

        mockMvc.perform(post(RestPath.Order.ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        final List<OrderEntity> all = orderRepository.findAll();

        assertTrue(all.size() == 1);

        final OrderEntity orderEntity = all.get(0);

        assertEquals(FIRST_NAME, orderEntity.getName());
        assertEquals(SECOND_NAME, orderEntity.getSurname());
        assertEquals(EMAIL, orderEntity.getEmail());
        assertEquals(200, orderEntity.getTotalPrice());
    }

    @Test
    public void shouldGetCreatedOrder() throws Exception {

        final PhonePrice price = new PhonePrice(1L, 100, Currency.EUR);
        final PhonePriceList priceList = new PhonePriceList(Lists.newArrayList(price));

        when(restTemplate.getForObject(any(String.class), eq(PhonePriceList.class))).thenReturn(priceList);

        final List<PhoneOrder> orders = Lists.newArrayList();
        orders.add(PhoneOrder.builder().id(ORDER_ID_1).count(1).build());

        final OrderRequest orderRequest = new OrderRequest(FIRST_NAME, SECOND_NAME, EMAIL, orders);

        mockMvc.perform(post(RestPath.Order.ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        final List<OrderEntity> all = orderRepository.findAll();

        assertTrue(all.size() == 1);

        final OrderEntity orderEntity = all.get(0);

        final MvcResult mvcResult = mockMvc.perform(get(RestPath.Order.ROOT + "/" + String.valueOf(orderEntity.getId()))
                .param("key", orderEntity.getUniqueKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(mvcResult);

        final OrderResponse createdOrderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);

        assertNotNull(createdOrderResponse);
        assertEquals(FIRST_NAME, createdOrderResponse.getName());
        assertEquals(SECOND_NAME, createdOrderResponse.getSurname());
        assertEquals(EMAIL, createdOrderResponse.getEmail());
        assertEquals("1", createdOrderResponse.getTotalPrice());
        assertNotNull(createdOrderResponse.getPhoneOrders());
        assertEquals(1, createdOrderResponse.getPhoneOrders().size());
        assertEquals(Long.valueOf(1), createdOrderResponse.getPhoneOrders().get(0).getId());
        assertEquals(1, createdOrderResponse.getPhoneOrders().get(0).getCount());
    }
}
