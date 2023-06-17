package superapp.data;

import java.util.Objects;
import java.util.UUID;

public class CommandId {
	private String superapp;
	private String miniapp;
	private String internalCommandId;

	public CommandId() {
	};

	public CommandId(String superapp, String miniapp, String internalCommandId) {
		super();
		this.superapp = superapp;
		this.miniapp = miniapp;
		this.internalCommandId = internalCommandId;
	}

	public CommandId(String superapp, String miniapp) {
		super();
		this.superapp = superapp;
		this.miniapp = miniapp;
		this.internalCommandId = UUID.randomUUID() + "";
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getMiniapp() {
		return miniapp;
	}

	public void setMiniapp(String miniapp) {
		this.miniapp = miniapp;
	}

	public String getInternalCommandId() {
		return internalCommandId;
	}

	public void setInternalCommandId(String internalCommandId) {
		this.internalCommandId = internalCommandId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(internalCommandId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandId other = (CommandId) obj;
		return Objects.equals(internalCommandId, other.internalCommandId);
	}

	@Override
	public String toString() {
		return "CommandId [superapp=" + superapp + ", miniapp=" + miniapp + ", internalCommandId=" + internalCommandId
				+ "]";
	}

}
