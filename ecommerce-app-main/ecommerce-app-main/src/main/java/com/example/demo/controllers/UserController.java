package com.example.demo.controllers;

import java.text.MessageFormat;
import java.util.Optional;

import com.example.demo.exceptions.AppUserException;
import com.example.demo.security.SecurityConstants;
import org.omg.CORBA.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import static com.example.demo.security.SecurityConstants.MINIMUM_PASSWORD_LENGTH;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest) {
		//validate user
		try {
			validateCreateUserRequest(createUserRequest);
		} catch (AppUserException e) {
			logger.error(e.toString());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
		}

		//build user
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		Cart cart = new Cart();
//		Cart cart1 = cartRepository.save(cart);
		user.setCart(cart);
		//persist user
		try {
			User user1 = userRepository.save(user);
			logger.info("managed to create user: ",user1.getUsername(),"with id",user1.getId());
			return ResponseEntity.ok(user1);
		} catch (Exception e) {
			logger.error(e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}



	}

	private void validateCreateUserRequest(CreateUserRequest createUserRequest) throws AppUserException {

		//validate password
		String username =createUserRequest.getUsername();
		String password = createUserRequest.getPassword();
		String confirmPassword = createUserRequest.getConfirmPassword();
		if(!password.equals(confirmPassword)) {
			throw new AppUserException( username, "password doesn't meeet confirmPassword");
		}

		if(password.length()<MINIMUM_PASSWORD_LENGTH) {
			String msg = String.format("password length %d is short of required length %d", password.length(),MINIMUM_PASSWORD_LENGTH);
			throw new AppUserException( username, msg);
		}
	}

}
