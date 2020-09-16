package repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Amenity;
import config.PathConfig;

public class AmenityRepository {
	
	public static ArrayList<Amenity> readAmenities(String absolutPath) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File amenitiesFile = new File(absolutPath + PathConfig.AMENITIES_FILE);
		boolean created = amenitiesFile.createNewFile();
		if (created)
			mapper.writeValue(amenitiesFile, new ArrayList<Amenity>());
		return mapper.readValue(amenitiesFile, new TypeReference<ArrayList<Amenity>>() {
		});
	}

	public static void writeAmenities(String absolutPath, ArrayList<Amenity> amenities) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File amenitiesFile = new File(absolutPath + PathConfig.AMENITIES_FILE);
		amenitiesFile.createNewFile();
		mapper.writeValue(amenitiesFile, amenities);
	}

	public static Amenity getAmenityById(String absolutPath, String id) throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Amenity> amenities = readAmenities(absolutPath);
		for (Amenity amenity : amenities) {
			if (amenity.getId().equals(id))
				return amenity;
		}
		return null;
	}

}
