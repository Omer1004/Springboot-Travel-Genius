package superapp.data;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeMap;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

@CompoundIndexes({ @CompoundIndex(name = "location_2dsphere", def = "{'location': '2dsphere'}") })

@Document(collection = "Object")
public class SuperAppObjectEntity {

	@Id
	private ObjectId objectId;
	private String type;
	private String alias;
	private Boolean active;
	private Date creationTimestamp;
	@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	private Point location;
	private CreatedBy createdBy;
	private Map<String, Object> objectDetails;

	@DBRef(lazy = true)
	private Set<SuperAppObjectEntity> parents;

	@DBRef(lazy = true)
	private Set<SuperAppObjectEntity> children;

	public SuperAppObjectEntity() {
		this.objectDetails = new TreeMap<>();
		this.parents = new HashSet<>();
		this.children = new HashSet<>();
	}

	public SuperAppObjectEntity(ObjectId objectId, String type, String alias, boolean active, Date creationTimestamp,
			Point location, CreatedBy createdBy, Map<String, Object> objectDetails) {
		super();
		this.objectId = objectId;
		this.type = type;
		this.alias = alias;
		this.active = active;
		this.creationTimestamp = new Date();
		this.location = location;
		this.createdBy = createdBy;
		this.objectDetails = objectDetails;
		this.parents = new HashSet<>();
		this.children = new HashSet<>();
	}

	@Id
	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public double getLat() {
		return this.location.getY();
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(double lng, double lat) {
		this.location = new Point(lng, lat);
	}

	public double getLng() {
		return this.location.getX();
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	public Set<SuperAppObjectEntity> getParents() {
		return parents;
	}

	public void setParents(Set<SuperAppObjectEntity> parents) {
		this.parents = parents;
	}

	public Set<SuperAppObjectEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<SuperAppObjectEntity> children) {
		this.children = children;
	}

	public void addParent(SuperAppObjectEntity parent) {
		this.parents.add(parent);
	}

	public void removeParent(SuperAppObjectEntity parent) {
		this.parents.remove(parent);
	}

	public void addChild(SuperAppObjectEntity child) {
		this.children.add(child);
	}

	public void removeChild(SuperAppObjectEntity child) {
		this.children.remove(child);
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SuperAppObjectEntity other = (SuperAppObjectEntity) obj;
		return Objects.equals(objectId, other.objectId);
	}

	@Override
	public String toString() {
		return "SuperAppObjectEntity [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", active="
				+ active + ", creationTimestamp=" + creationTimestamp + ", location=" + location + ", createdBy="
				+ createdBy + ", objectDetails=" + objectDetails + "]";
	}

}
