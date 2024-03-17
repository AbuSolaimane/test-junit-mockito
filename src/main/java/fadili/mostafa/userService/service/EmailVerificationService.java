package fadili.mostafa.userService.service;

import fadili.mostafa.userService.model.User;

public interface EmailVerificationService {

	void scheduleEmailConfirmation(User user);
}
