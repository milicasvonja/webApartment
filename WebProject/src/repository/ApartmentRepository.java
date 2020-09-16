package repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Apartment;
import config.PathConfig;

public class ApartmentRepository {
	
	public static ArrayList<Apartment> readApartments(String absolutPath) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File apartmentsFile = new File(absolutPath + PathConfig.APARTMENTS_FILE);
		boolean created = apartmentsFile.createNewFile();
		if (created)
			mapper.writeValue(apartmentsFile, new ArrayList<Apartment>());
		return mapper.readValue(apartmentsFile, new TypeReference<ArrayList<Apartment>>() {
		});
	}

	public static void writeApartments(String absolutPath, ArrayList<Apartment> apartments) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File apartmentsFile = new File(absolutPath + PathConfig.APARTMENTS_FILE);
		System.out.println(apartmentsFile.getAbsolutePath() + " " + apartmentsFile.toString());
		apartmentsFile.createNewFile();
		mapper.writeValue(apartmentsFile, apartments);
	}
	
	public static Apartment getApartmentById(String absolutPath, String id) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Apartment> apartments = readApartments(absolutPath);
		for (Apartment apartment : apartments) {
			if (apartment.getId().equals(id))
				return apartment;
		}
		return null;
	}

}
