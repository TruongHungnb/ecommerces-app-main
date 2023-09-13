package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class SareetaApplicationTests {
	@Autowired
	private ItemController itemController;
	@Autowired
	private UserController  userController;
	@Autowired
	private CartController cartController;
	@Autowired
	private OrderController orderController;

	private final String TEST_USER1_NAME="gavin";
	private static final String TEST_USER1_PASSWORD = "testPassword";
	private final String TEST_ITEM1_NAME="Round Widget";
	private final String TEST_ITEM1_DESCRIPTION="A widget that is round";
	private final String TEST_ITEM2_NAME="Square Widget";
	private final String TEST_ITEM2_DESCRIPTION="A widget that is square";


	@Test
	public void test1ItemModule() {
		//test getItems
		ResponseEntity<List<Item>> r0 = itemController.getItems();
		assertEquals(HttpStatus.OK,r0.getStatusCode());
		List<Item> items = r0.getBody();
		assertEquals(2,items.size());
		assertEquals(TEST_ITEM1_NAME,items.get(0).getName());
		assertEquals(TEST_ITEM2_DESCRIPTION,items.get(1).getDescription());

		//test getItemById

		ResponseEntity<Item> r1 = itemController.getItemById(1l);
		assertEquals(HttpStatus.OK,r1.getStatusCode());
		Item item1 = r1.getBody();
		assertEquals(TEST_ITEM1_NAME,item1.getName());
		assertEquals(TEST_ITEM1_DESCRIPTION,item1.getDescription());

		//void getItemsByName
		ResponseEntity<List<Item>> r2 = itemController.getItemsByName(TEST_ITEM1_NAME);
		assertEquals(HttpStatus.OK,r2.getStatusCode());
		List<Item> items2 = r2.getBody();
		assertEquals(TEST_ITEM1_DESCRIPTION,items2.get(0).getDescription());
	}

	@Test
	public void test2UserCartOrderFlow() {
		//1 create user sad path 1
		CreateUserRequest rqum1 = new CreateUserRequest();
		rqum1.setUsername(TEST_USER1_NAME);
		rqum1.setPassword("testPassword");
		rqum1.setConfirmPassword("wrongConfirmPassword");
		ResponseEntity<User> rum1 = userController.createUser(rqum1);
		assertEquals(HttpStatus.BAD_REQUEST,rum1.getStatusCode());

		//2 create user sad path 2
		CreateUserRequest rqum2 = new CreateUserRequest();
		rqum2.setUsername(TEST_USER1_NAME);
		rqum2.setPassword("short");
		rqum2.setConfirmPassword("short");
		ResponseEntity<User> rum2 = userController.createUser(rqum2);
		assertNotNull(rum2);
		assertEquals(HttpStatus.BAD_REQUEST,rum2.getStatusCode());

		//3 create user happy path
		CreateUserRequest rqum3 = new CreateUserRequest();
		rqum3.setUsername(TEST_USER1_NAME);
		rqum3.setPassword(TEST_USER1_PASSWORD);
		rqum3.setConfirmPassword(TEST_USER1_PASSWORD);
		ResponseEntity<User> rum3 = userController.createUser(rqum3);
		assertEquals(HttpStatus.OK,rum3.getStatusCode());
		User u3 = rum3.getBody();
		assertEquals(TEST_USER1_NAME,u3.getUsername());
		//verify password encrypted
		assertNotEquals(TEST_USER1_PASSWORD,u3.getPassword());

		//4 get user by id
		ResponseEntity<User> rum4 = userController.findById(u3.getId());
		User u4 = rum4.getBody();
		assertEquals(TEST_USER1_NAME,u4.getUsername());
		// 5 get user by name
		ResponseEntity<User> rum5 = userController.findByUserName(u3.getUsername());
		User u5 = rum5.getBody();
		assertEquals(TEST_USER1_NAME,u5.getUsername());

//	}



//	@Test
//	public void test3CartModule() {


		//add item to cart
		ModifyCartRequest rqcm1 = new ModifyCartRequest();
		rqcm1.setItemId(1);
		rqcm1.setUsername(TEST_USER1_NAME);
		rqcm1.setQuantity(100);
		ResponseEntity<Cart> rcm1 = cartController.addTocart(rqcm1);
		assertEquals(HttpStatus.OK,rcm1.getStatusCode());
		Cart cart1 = rcm1.getBody();
		assertEquals(100,cart1.getItems().size());
		assertEquals(BigDecimal.valueOf(299.00).setScale(2),cart1.getTotal());
		assertEquals(TEST_USER1_NAME,cart1.getUser().getUsername());
		//remove item from cart
		ModifyCartRequest rqcm2 = new ModifyCartRequest();
		rqcm2.setItemId(1);
		rqcm2.setUsername(TEST_USER1_NAME);
		rqcm2.setQuantity(98);
		ResponseEntity<Cart> rcm2 = cartController.removeFromcart(rqcm2);
		assertEquals(HttpStatus.OK,rcm2.getStatusCode());
		Cart cart2 = rcm2.getBody();
		assertEquals(2,cart2.getItems().size());
		assertEquals(BigDecimal.valueOf(5.98).setScale(2),cart2.getTotal());
		assertEquals(TEST_USER1_NAME,cart2.getUser().getUsername());

//	}
//	@Test
//	public void test4OrderModule() {
		//test submit order
		ResponseEntity<UserOrder> rom1 = orderController.submit(TEST_USER1_NAME);
		assertEquals(HttpStatus.OK,rom1.getStatusCode());
		UserOrder userOrder = rom1.getBody();
		assertEquals(2,userOrder.getItems().size());
		assertEquals(TEST_USER1_NAME,userOrder.getUser().getUsername());
		assertEquals(BigDecimal.valueOf(5.98).setScale(2),userOrder.getTotal());
		// test check order history
		ResponseEntity<List<UserOrder>> rom2 = orderController.getOrdersForUser(TEST_USER1_NAME);
		assertEquals(HttpStatus.OK,rom2.getStatusCode());
		UserOrder userOrderHistory = rom2.getBody().get(0);
		assertEquals(2,userOrderHistory.getItems().size());
		assertEquals(TEST_USER1_NAME,userOrderHistory.getUser().getUsername());
		assertEquals(BigDecimal.valueOf(5.98).setScale(2),userOrderHistory.getTotal());

	}



}
