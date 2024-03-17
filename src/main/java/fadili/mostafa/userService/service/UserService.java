package fadili.mostafa.userService.service;

import fadili.mostafa.userService.model.User;

public interface UserService {

	User createUser(String firsName, String lastName, String email, String password, String repeatedPassword);
}
