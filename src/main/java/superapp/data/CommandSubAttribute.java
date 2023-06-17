package superapp.data;

public class CommandSubAttribute {

	private String key1Subkey1;

	public CommandSubAttribute(String key1Subkey1) {
		super();
		this.key1Subkey1 = key1Subkey1;
	}

	public CommandSubAttribute() {
	}

	public String getKey1Subkey1() {
		return key1Subkey1;
	}

	public void setKey1Subkey1(String key1Subkey1) {
		this.key1Subkey1 = key1Subkey1;
	}

	@Override
	public String toString() {
		return "CommandSubAttribute [key1Subkey1=" + key1Subkey1 + "]";
	}

}
