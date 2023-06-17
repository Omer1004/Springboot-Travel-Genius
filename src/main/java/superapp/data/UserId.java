package superapp.data;

import java.util.Objects;

public class UserId {
	private String superapp;
	private String email;

	public UserId() {
	}

	public UserId(String superapp, String email) {
		super();
		this.superapp = superapp;
		this.email = email;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserId other = (UserId) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "UserId [superapp=" + superapp + ", email=" + email + "]";
	}

}
