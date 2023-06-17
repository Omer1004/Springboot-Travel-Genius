package superapp.data;

import java.util.Objects;
import java.util.UUID;

public class ObjectId {
	private String superapp;
	private String internalObjectId;

	public ObjectId() {
	};

	public ObjectId(String superapp, String internalObjectId) {
		super();
		this.superapp = superapp;
		this.internalObjectId = internalObjectId;
	}

	public ObjectId(String superapp) {
		this.superapp = superapp;
		this.internalObjectId = "" + UUID.randomUUID();
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(internalObjectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectId other = (ObjectId) obj;
		return Objects.equals(internalObjectId, other.internalObjectId);
	}

	@Override
	public String toString() {
		return "ObjectId [superapp:" + superapp + ", internalObjectId=" + internalObjectId + "]";
	}

}
