package superapp.dal;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import superapp.data.UserEntity;
import superapp.data.UserId;
import superapp.data.UserRole;
import superapp.logic.UserQueries;
import superapp.restAPI.UserBoundary;

@Service
public class UserServiceDB implements UserQueries {
	private UserCRUD userCRUD;

	private Log logger = LogFactory.getLog(UserServiceDB.class);

	@Autowired
	public void setUsersCRUD(UserCRUD usersCRUD) {
		this.userCRUD = usersCRUD;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {

		UserEntity entity = this.toEntity(user);

		// check for validations
		validateUser(user);

		entity = this.userCRUD.save(entity);
		return this.toBoundary(entity);
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		UserEntity existing = this.userCRUD.findById(new UserId(userSuperApp, userEmail))
				.orElseThrow(() -> new RuntimeException(
						"could not find  user in " + userSuperApp + " with the email : " + userEmail));

		return this.toBoundary(existing);
	}

	@Override
	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update) {

		UserId userId = new UserId(userSuperApp, userEmail);

		UserEntity existing = this.userCRUD.findById(userId).orElseThrow(() -> new RuntimeException(
				"could not find  user for update by the Keywords : " + userSuperApp + " / " + userEmail));

		if (update.getRole() != null) {
			existing.setRole(UserRole.valueOf(update.getRole()));
		}

		if (update.getUsername() != null) {
			existing.setUsername(update.getUsername());
		}

		if (update.getAvatar() != null) {
			existing.setAvatar(update.getAvatar());
		}

		existing = this.userCRUD.save(existing);
		return this.toBoundary(existing);

	}

	@Override
	public List<UserBoundary> getAllUsers() {

		return this.userCRUD.findAll().stream() // Stream<MiniAppCommandEntity>
				.map(this::toBoundary) // Stream<MiniAppCommandBoundary>
				.toList(); // List<MiniAppCommandBoundary>
	}

	private UserBoundary toBoundary(UserEntity entity) {

		UserBoundary boundary = new UserBoundary();

		boundary.setUserId(new UserId(entity.getUserId().getSuperapp(), entity.getUserId().getEmail()));
		boundary.setRole(entity.getRole().name());
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());

		return boundary;
	}

	private UserEntity toEntity(UserBoundary boundary) {

		UserEntity entity = new UserEntity();

		if (boundary.getUserId() != null) {
			entity.setUserId(boundary.getUserId());
		} else {
			//
		}

		if (boundary.getRole() != null) { // updated here ???
			entity.setRole(UserRole.valueOf(boundary.getRole()));
		} else {
			entity.setRole(null);
		}

		if (boundary.getUsername() != null) {
			entity.setUsername(boundary.getUsername());
		} else {
			//
		}

		if (boundary.getAvatar() != null) {
			entity.setAvatar(boundary.getAvatar());
		} else {
			//
		}

		return entity;
	}

	@Override
	public void deleteAllUsers() {
		this.userCRUD.deleteAll();
	}

	private void validateUser(UserBoundary user) {

		if (!isValidEmail(user.getUserId().getEmail())) {
			throw new IllegalArgumentException(user.getUserId().getEmail() + " is invalid email address");
		}

		if (isNullOrEmpty(user.getUsername())) {
			throw new IllegalArgumentException("Username must be provided and not be empty");
		}

		if (isNullOrEmpty(user.getAvatar())) {
			throw new IllegalArgumentException("Avatar must be provided and not be empty");
		}

		if (!isValidUserRole(user.getRole())) {
			throw new IllegalArgumentException("Invalid user role, must be one of: MINIAPP_USER, SUPERAPP_USER, ADMIN");
		}

	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches();
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	private boolean isValidUserRole(String role) {
		try {
			UserRole.valueOf(role);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public List<UserBoundary> getAllUsersWithPagination(int page, int size) {

		return this.userCRUD.findAll(PageRequest.of(page, size, Direction.ASC, "userId")).stream().map(this::toBoundary)
				.toList();

	}

	@Override
	public UserBoundary getUserById(UserId user) {

		return this.userCRUD.findById(user).map(this::toBoundary).get();

	}

}
