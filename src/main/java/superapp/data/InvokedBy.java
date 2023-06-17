package superapp.data;

import java.util.Objects;

public class InvokedBy {
	private UserId userId;

	public InvokedBy() {
	}

	public InvokedBy(UserId userId) {
		super();
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvokedBy other = (InvokedBy) obj;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "InvokedBy [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
