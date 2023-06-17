package superapp.data;

import java.util.Objects;

public class TargetObject {
	ObjectId objectId;

	public TargetObject() {
	}

	public TargetObject(ObjectId objectId) {
		super();
		this.objectId = objectId;
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TargetObject other = (TargetObject) obj;
		return Objects.equals(objectId, other.objectId);
	}

	@Override
	public String toString() {
		return "TargetObject [objectId=" + objectId + "]";
	}

}
