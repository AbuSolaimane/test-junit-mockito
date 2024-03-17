package fadili.mostafa.userService.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fadili.mostafa.userService.dao.UserRepository;
import fadili.mostafa.userService.exception.EmailNotificationServiceException;
import fadili.mostafa.userService.exception.UserServiceException;
import fadili.mostafa.userService.model.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	EmailVerificationServiceImpl emailVerificationService;
	
	String lastName;
	String email;
	String password;
	String repeatedPassword;
	
	@BeforeEach
	void init() {
		
		lastName = "fadili";
		email = "mostafa@gmail.com";
		password = "1234";
		repeatedPassword = "1234";
	}

	@DisplayName("user creation")
	@Test
	void testCreateUser_whenUserDetailsisProvided_returnUserObject() {
		
		//given
		String firsName = "mostafa";
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(true);
		
		//when
		User user = userService.createUser(firsName, lastName, email, password, repeatedPassword);
		
		//then
		Assertions.assertNotNull(user, "user shouldn't be null");
		Assertions.assertEquals(firsName, user.getFirstName(), "user firstName is incorrect");
		Assertions.assertEquals(lastName, user.getLastName(), "user lastName is incorrect");
		Assertions.assertEquals(email, user.getEmail(), "user email is incorrect");
		Assertions.assertNotNull(user.getId(), "user should have contained an id");
		Mockito.verify(userRepository, Mockito.times(1))
			.save(Mockito.any(User.class));
		Mockito.verify(userRepository, Mockito.atLeast(1))
			.save(Mockito.any(User.class));
	}
	
	@DisplayName("empty firstName causes IllegalArgException")
	@Test
	void testCreateUser_whenFirstNameEmpty_throwIllegalArgException() {
		
		//given
		String firsName = "mostafa";
		String lastName = "";
		String expectedExceptionMessage = "lastName shouldn't be null";
		
		//when and then
		IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, 
				() -> userService.createUser(firsName, lastName, email, password, repeatedPassword),
				"empty lastName should have caused IllegalArgumentException");
		
		Assertions.assertEquals(expectedExceptionMessage,
				illegalArgumentException.getMessage(),
				"error message is incorrect");
	}
	
	@Test
	void testCreateUser_whenLastNameEmpty_throwIllegalArgException() {
		
		
	}
	
	@DisplayName("if save() method throws RuntimeException, a UserServiceException should be thrown")
	@Test
	void testCreateUser_whenSaveMethodThrowsException_thenThrowUserServiceException() {
		
		//given
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(RuntimeException.class);
		String firstName = "mostafa";
		
		
		//when and then
		Assertions.assertThrows(UserServiceException.class,
					() -> userService.createUser(firstName, lastName, email,
											password, repeatedPassword),
					"UserServiceException should have been thrown");
		
	}
	
	@Test
	void testCreateUser_whenScheduleEmailVerificationThrowsExc_thenThrowUserServiceException() {
		
		//given
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(true);
		//Mockito.when(emailVerificationService.scheduleEmailConfirmation(Mockito.any(User.class))).thenThrow(); doesn't work
		Mockito.doThrow(EmailNotificationServiceException.class)
			.when(emailVerificationService)
			.scheduleEmailConfirmation(Mockito.any(User.class));
		
		/*Mockito.doNothing().when(emailVerificationService)
			.scheduleEmailConfirmation(Mockito.any(User.class));*/
		
		String firstName = "mostafa";
		
		//when and then
		Assertions.assertThrows(UserServiceException.class,
					() -> userService.createUser(firstName, lastName, email,
							password, repeatedPassword),
					"UserServiceException should have been thrown");
		
		Mockito.verify(emailVerificationService, Mockito.times(1))
			.scheduleEmailConfirmation(Mockito.any(User.class));;
	}
	
	@Test
	void testCreateUser_whenCreate_thenCallRealMethod() {
		
		//given
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(true);
		
		Mockito.doCallRealMethod()
			.when(emailVerificationService)
			.scheduleEmailConfirmation(Mockito.any(User.class));
		
		String firstName = "mostafa";
		
		//when
		userService.createUser(firstName, lastName, email, password, repeatedPassword);
		
		//then
		Mockito.verify(emailVerificationService, Mockito.times(1))
			.scheduleEmailConfirmation(Mockito.any(User.class));
	}
	
}
