package beans;

import java.util.UUID;

public class Amenity {

	private String id;
	private String name;
	
	public Amenity() {}

	public Amenity(String name) {
		super();
		this.id = UUID.randomUUID().toString();
		this.name = name;
	}
	
	public Amenity(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
