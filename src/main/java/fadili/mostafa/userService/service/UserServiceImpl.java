package fadili.mostafa.userService.service;

import java.util.UUID;

import fadili.mostafa.userService.dao.UserRepository;
import fadili.mostafa.userService.exception.UserServiceException;
import fadili.mostafa.userService.model.User;

public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final EmailVerificationService emailVerificationService;
	
	public UserServiceImpl(UserRepository userRepository, EmailVerificationService emailVerificationService) {
		super();
		this.userRepository = userRepository;
		this.emailVerificationService = emailVerificationService;
	}

	@Override
	public User createUser(String firsName, String lastName,
			String email, String password, String repeatedPassword) {
		
		if (firsName == null || firsName.length() == 0)
			throw new IllegalArgumentException("firstName shouldn't be null");
		
		if (lastName == null || lastName.length() == 0)
			throw new IllegalArgumentException("lastName shouldn't be null");
		
		User user = new User(UUID.randomUUID().toString(), firsName, lastName, email);
		
		boolean isUserCreated;
		try {
			isUserCreated = userRepository.save(user);
		} catch (RuntimeException ex) {
			
			throw new UserServiceException(ex.getMessage());
		}
		
		if (!isUserCreated) {
			throw new UserServiceException("you couldn't create the user");
		}
		
		try {
			emailVerificationService.scheduleEmailConfirmation(user);
		} catch (RuntimeException ex) {
			
			throw new UserServiceException(ex.getMessage());
		}
		
		return user;
	}

}
