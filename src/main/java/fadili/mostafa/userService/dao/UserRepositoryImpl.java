package fadili.mostafa.userService.dao;

import java.util.HashMap;
import java.util.Map;

import fadili.mostafa.userService.model.User;

public class UserRepositoryImpl implements UserRepository {

	
	Map<String, User> database = new HashMap<>();
	@Override
	public boolean save(User user) {
		
		boolean saved = false;
		if (!database.containsKey(user.getId())) {
			
			database.put(user.getId(), user);
			saved = true;
		}
		
		return saved;
	}

}
