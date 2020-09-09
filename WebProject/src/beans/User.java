package beans;

import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class User {

	private String id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Gender gender;
	private Role role;
	private boolean active;

	public User() {}
	
	public User(String username, String password, String firstName, String lastName, Gender gender,
			Role role) throws NoSuchAlgorithmException {
		super();
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes());
		
		this.id = UUID.randomUUID().toString();
		this.username = username;
		this.password = new String(messageDigest.digest());
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.role = role;
		this.active = true;
	}
	
	public User(String id, String username, String password, String firstName, String lastName, Gender gender,
			Role role, boolean active) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.role = role;
		this.active = active;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", gender=" + gender + ", role=" + role + ", isActive=" + active + "]";
	}
	
	
	
}
