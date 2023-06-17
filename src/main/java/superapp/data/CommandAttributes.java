package superapp.data;

import java.util.Objects;

public class CommandAttributes {

	private CommandSubAttribute key1;

	public CommandAttributes() {
	};

	public CommandAttributes(CommandSubAttribute key1) {
		super();
		this.key1 = key1;
	}

	public CommandSubAttribute getKey1() {
		return key1;
	}

	public void setKey1(CommandSubAttribute key1) {
		this.key1 = key1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key1);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandAttributes other = (CommandAttributes) obj;
		return Objects.equals(key1, other.key1);
	}

	@Override
	public String toString() {
		return "commandAttributes [key1=" + key1 + "]";
	}

}
