package superapp.data;

import org.springframework.data.mongodb.core.mapping.Document;
//import lombok.Data;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.ToString;

import java.util.Objects;

import org.springframework.data.annotation.Id;

@Document(collection = "Users")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString

public class UserEntity {

	@Id
	public UserId userId;
	private UserRole role;
	private String username;
	private String avatar;

	public UserEntity() {
	}

	public UserEntity(UserId userId, UserRole role, String username, String avatar) {
		this();
		this.userId = userId;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {

		this.userId = userId;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}
}
