package repository;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Gender;
import beans.Role;
import beans.User;
import config.PathConfig;

public class UserRepository {
	

	
	public static ArrayList<User> readUsers(String absPath) throws IOException, NoSuchAlgorithmException {
		createFilesDirectory(absPath);		
		ObjectMapper mapper = new ObjectMapper();
		File userFile = new File(absPath + PathConfig.USERS_FILE);
		System.out.println("abs putanja");
		System.out.println(absPath);
		boolean created = userFile.createNewFile();
		if (created) {
			ArrayList<User> admins = new ArrayList<User>();
			admins.add(new User("admin", "admin", "Admin FirstName", "Admin LastName", Gender.MALE, Role.ADMIN));
			mapper.writeValue(userFile, admins);
		}
		return mapper.readValue(userFile, new TypeReference<ArrayList<User>>() {
		});
	}

	public static void writeUsers(String absPath,ArrayList<User> users) throws IOException {
		createFilesDirectory(absPath);
		ObjectMapper mapper = new ObjectMapper();
		File userFile = new File(absPath + PathConfig.USERS_FILE);
		userFile.createNewFile();
		mapper.writeValue(userFile, users);
	}

	public static User getUserById(String absPath ,String id)
			throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException {
		for (User u : readUsers(absPath)) {
			if (u.getId().contentEquals(id))
				return u;
		}
		return null;
	}
	
	private static void createFilesDirectory(String absPath) {
		File directory = new File(absPath + PathConfig.FILES);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	}

}
