package fadili.mostafa.userService.service;

import fadili.mostafa.userService.model.User;

public class EmailVerificationServiceImpl implements EmailVerificationService {

	@Override
	public void scheduleEmailConfirmation(User user) {
		
		//add email to the queue to be sent to the user
		System.out.println("hello");
	}

}
