package services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Apartment;
import beans.Comment;
import beans.Reservation;
import beans.Role;
import beans.User;
import config.PathConfig;
import repository.CommentRepository;

@Path("/comment")
public class CommentService {

	@Context
	ServletContext ctx;

	@Context
	HttpServletRequest request;

	@Path("/apartment")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComment( Comment comment)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.GUEST)
			return Response.status(Response.Status.FORBIDDEN).build();

		Apartment apartment = getApartmentById(comment.getApartmentId());
		if (apartment == null) {
			System.out.println(comment.getApartmentId());
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		ArrayList<Reservation> reservations = readReservations();
		boolean included = reservations.stream().anyMatch(reservation -> reservation.getApartmentId().equals(comment.getApartmentId()));
		if (!included)
			return Response.status(Response.Status.BAD_REQUEST).build();

		ArrayList<Apartment> apartments = readApartments();
		for (Apartment a : apartments) {
			if (a.getId().equals(comment.getApartmentId())) {
				a.getCommentsId().add(comment.getId());
				writeApartments(apartments);
			}
		}

		ArrayList<Comment> comments = CommentRepository.readComments(ctx.getRealPath("."));
		comments.add(comment);
		CommentRepository.writeComments(ctx.getRealPath("."),comments);

		return Response.status(Response.Status.CREATED).build();

	}

	@Path("/apartment/{id}/admin-host")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommentsForApartmentAdminHost(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() == Role.GUEST)
			return Response.status(Response.Status.FORBIDDEN).build();

		Apartment apartment = getApartmentById(id);
		if (apartment == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		ArrayList<Comment> comments = CommentRepository.readComments(ctx.getRealPath("."));
		comments = comments.stream().filter(comment -> comment.getApartmentId().equals(id))
				.collect(Collectors.toCollection(ArrayList::new));
		return Response.status(Response.Status.OK).entity(comments).build();
	}

	@Path("/apartment/{id}/guest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommentsForApartmentGuest(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.GUEST)
			return Response.status(Response.Status.FORBIDDEN).build();

		Apartment apartment = getApartmentById(id);
		if (apartment == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		ArrayList<Comment> comments = CommentRepository.readComments(ctx.getRealPath("."));
		comments = comments.stream().filter(comment -> comment.getApartmentId().equals(id) && comment.isApproved())
				.collect(Collectors.toCollection(ArrayList::new));
		return Response.status(Response.Status.OK).entity(comments).build();
	}

	@Path("/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveComment(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();
		
		ArrayList<Comment> comments = CommentRepository.readComments(ctx.getRealPath("."));
		for (Comment comment : comments) {
			if (comment.getId().equals(id)) {
				comment.setApproved(true);
				CommentRepository.writeComments(ctx.getRealPath("."),comments);
				return Response.status(Response.Status.OK).entity(comment).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	

	private ArrayList<Apartment> readApartments() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File apartmentsFile = new File(ctx.getRealPath(".") + PathConfig.APARTMENTS_FILE);
		boolean created = apartmentsFile.createNewFile();
		if (created)
			mapper.writeValue(apartmentsFile, new ArrayList<Apartment>());
		return mapper.readValue(apartmentsFile, new TypeReference<ArrayList<Apartment>>() {
		});
	}

	private void writeApartments(ArrayList<Apartment> apartments) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File apartmentsFile = new File(ctx.getRealPath(".") + PathConfig.APARTMENTS_FILE);
		apartmentsFile.createNewFile();
		mapper.writeValue(apartmentsFile, apartments);
	}

	private Apartment getApartmentById(String id) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Apartment> apartments = readApartments();
		for (Apartment apartment : apartments) {
			if (apartment.getId().equals(id))
				return apartment;
		}
		return null;
	}

	private ArrayList<Reservation> readReservations() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File reservationsFile = new File(ctx.getRealPath(".") + PathConfig.RESERVATIONS_FILE);
		boolean created = reservationsFile.createNewFile();
		if (created)
			mapper.writeValue(reservationsFile, new ArrayList<Reservation>());
		return mapper.readValue(reservationsFile, new TypeReference<ArrayList<Reservation>>() {
		});
	}

	private void writeReservations(ArrayList<Reservation> reservations) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File reservationsFile = new File(ctx.getRealPath(".") + PathConfig.RESERVATIONS_FILE);
		reservationsFile.createNewFile();
		mapper.writeValue(reservationsFile, reservations);
	}
}
