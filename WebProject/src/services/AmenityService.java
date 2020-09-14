package services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Amenity;
import beans.Apartment;
import beans.Role;
import beans.User;
import config.PathConfig;
import repository.AmenityRepository;
import repository.ApartmentRepository;

@Path("/amenity")
public class AmenityService {

	@Context
	ServletContext ctx;

	@Context
	HttpServletRequest request;

	@Path("")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAmenity(Amenity amenity) throws JsonGenerationException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.ADMIN)
			return Response.status(Response.Status.FORBIDDEN).build();

		Amenity a = new Amenity(amenity.getName());
		ArrayList<Amenity> amenities = AmenityRepository.readAmenities(ctx.getRealPath("."));
		amenities.add(a);
		AmenityRepository.writeAmenities(ctx.getRealPath("."),amenities);
		return Response.status(Response.Status.OK).entity(a).build();
	}

	@Path("")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAmenities() throws JsonGenerationException, JsonMappingException, IOException {
		ArrayList<Amenity> amenities = AmenityRepository.readAmenities(ctx.getRealPath("."));
		return Response.status(Response.Status.OK).entity(amenities).build();
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAmenity(@PathParam("id")String id) throws JsonGenerationException, JsonMappingException, IOException {
		Amenity amenity = AmenityRepository.getAmenityById(ctx.getRealPath("."),id);
		return Response.status(Response.Status.OK).entity(amenity).build();
	}

	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editAmenity(@PathParam("id") String id, Amenity amenity) throws IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.ADMIN)
			return Response.status(Response.Status.FORBIDDEN).build();
		ArrayList<Amenity> amenities = AmenityRepository.readAmenities(ctx.getRealPath("."));
		for (Amenity a : amenities) {
			if (a.getId().equals(id)) {
				a.setName(amenity.getName());
				AmenityRepository.writeAmenities(ctx.getRealPath("."),amenities);
				return Response.status(Response.Status.OK).entity(a).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();

	}

	@Path("/{id}")
	@DELETE
	public Response deleteAmenity(@PathParam("id") String id)
			throws JsonGenerationException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.ADMIN)
			return Response.status(Response.Status.FORBIDDEN).build();

		Amenity amenity = AmenityRepository.getAmenityById(ctx.getRealPath("."),id);
		if (amenity == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		ArrayList<Amenity> amenities = AmenityRepository.readAmenities(ctx.getRealPath("."));
		amenities = amenities.stream().filter(a -> !a.getId().equals(id))
				.collect(Collectors.toCollection(ArrayList::new));
		AmenityRepository.writeAmenities(ctx.getRealPath("."),amenities);

		ArrayList<Apartment> apartments = ApartmentRepository.readApartments(ctx.getRealPath("."));
		apartments.forEach(apartment -> apartment.setAmenitiesId(apartment.getAmenitiesId().stream()
				.filter(amenityId -> !amenityId.equals(id)).collect(Collectors.toCollection(ArrayList::new))));
		ApartmentRepository.writeApartments(ctx.getRealPath("."),apartments);

		return Response.status(Response.Status.OK).build();
	}

	

	

	
}
