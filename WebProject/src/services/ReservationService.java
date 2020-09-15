package services;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Apartment;
import beans.Gender;
import beans.Reservation;
import beans.ReservationStatus;
import beans.Role;
import beans.User;
import config.PathConfig;

@Path("/reservation")
public class ReservationService {

	@Context
	ServletContext ctx;

	@Context
	HttpServletRequest request;

	@Path("")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllReservations() throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.ADMIN)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		return Response.status(Response.Status.OK).entity(reservations).build();
	}

	@Path("/guest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGuestsReservations() throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.GUEST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		reservations = reservations.stream().filter(reservation -> reservation.getGuestId().equals(loggedUser.getId()))
				.collect(Collectors.toCollection(ArrayList::new));

		return Response.status(Response.Status.OK).entity(reservations).build();
	}

	@Path("/{id}/guest/cancel")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelReservation(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.GUEST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		for (Reservation reservation : reservations) {
			if (reservation.getId().equals(id) && reservation.getGuestId().equals(loggedUser.getId())
					&& (reservation.getStatus() == ReservationStatus.ACCEPTED
							|| reservation.getStatus() == ReservationStatus.CREATED)) {
				reservation.setStatus(ReservationStatus.CANCELED);
				writeReservations(reservations);
				return Response.status(Response.Status.OK).entity(reservation).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@Path("/filter/host")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostReservations(@QueryParam("staus") ReservationStatus status)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		reservations = reservations.stream().filter(reservation -> {
			try {
				return getApartmentById(reservation.getApartmentId()).getHostId().equals(loggedUser.getId());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}).collect(Collectors.toCollection(ArrayList::new));
		if (status != null)
			reservations = reservations.stream().filter(reservation -> reservation.getStatus() == status)
					.collect(Collectors.toCollection(ArrayList::new));

		return Response.status(Response.Status.OK).entity(reservations).build();
	}
	
	@Path("/host")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHostReservations()
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		reservations = reservations.stream().filter(reservation -> {
			try {
				return getApartmentById(reservation.getApartmentId()).getHostId().equals(loggedUser.getId());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}).collect(Collectors.toCollection(ArrayList::new));
		

		return Response.status(Response.Status.OK).entity(reservations).build();
	}
	
	@Path("/host/search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchHostReservations(@QueryParam("username")String username, @QueryParam("status")String status)
			throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		reservations = reservations.stream().filter(reservation -> {
			try {
				return getApartmentById(reservation.getApartmentId()).getHostId().equals(loggedUser.getId());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}).collect(Collectors.toCollection(ArrayList::new));
		
		if(username != "" && username != null) {
			User user = getUserByusername(username);
			
			reservations = reservations.stream()
					.filter(reservation -> reservation.getGuestId().equals(user.getId()))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		if(status != "" && status != null) {
			reservations = reservations.stream()
					.filter(reservation -> reservation.getStatus().toString().equals(status))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		

		return Response.status(Response.Status.OK).entity(reservations).build();
	}

	@Path("/{id}/host/accept")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptReservation(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		for (Reservation reservation : reservations) {
			if (getApartmentById(reservation.getApartmentId()).getHostId().equals(loggedUser.getId())
					&& reservation.getStatus() == ReservationStatus.CREATED) {
				reservation.setStatus(ReservationStatus.ACCEPTED);
				writeReservations(reservations);
				return Response.status(Response.Status.OK).entity(reservation).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@Path("/{id}/host/reject")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response rejectReservation(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Reservation> reservations = readReservations();
		for (Reservation reservation : reservations) {
			if (getApartmentById(reservation.getApartmentId()).getHostId().equals(loggedUser.getId())
					&& (reservation.getStatus() == ReservationStatus.CREATED
							|| reservation.getStatus() == ReservationStatus.ACCEPTED)) {
				reservation.setStatus(ReservationStatus.REJECTED);
				writeReservations(reservations);
				return Response.status(Response.Status.OK).entity(reservation).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@Path("/{id}/host/end")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response endReservation(@PathParam("id") String id)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.HOST)
			return Response.status(Response.Status.FORBIDDEN).build();

		Calendar c = Calendar.getInstance();

		ArrayList<Reservation> reservations = readReservations();
		for (Reservation reservation : reservations) {
			c.setTime(reservation.getStartDate());
			c.add(Calendar.DATE, reservation.getNights());
			Date endDate = c.getTime();
			if (getApartmentById(reservation.getApartmentId()).getHostId().equals(loggedUser.getId())
					&& endDate.after(new Date())) {
				reservation.setStatus(ReservationStatus.ENDED);
				writeReservations(reservations);
				return Response.status(Response.Status.OK).entity(reservation).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@Path("")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addReservation(Reservation reservation)
			throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.GUEST)
			return Response.status(Response.Status.FORBIDDEN).build();

		Apartment apartment = getApartmentById(reservation.getApartmentId());
		if (apartment == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		// Check if all days is available
		for (int i = 1; i <= reservation.getNights(); i++) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(reservation.getStartDate());
			c1.add(Calendar.DATE, i);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			boolean available = apartment.getDates().stream().anyMatch(date -> {
				Calendar c2 = Calendar.getInstance();
				c2.setTime(date);
				c2.set(Calendar.HOUR_OF_DAY, 0);
				c2.set(Calendar.MINUTE, 0);
				c2.set(Calendar.SECOND, 0);
				c2.set(Calendar.MILLISECOND, 0);
				return c1.compareTo(c2) == 0;
			});
			if (!available)
				return Response.status(Response.Status.BAD_REQUEST).entity("Date is not available").build();
		}

		for (int i = 1; i <= reservation.getNights(); i++) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(reservation.getStartDate());
			c1.add(Calendar.DATE, i);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			ArrayList<Date> newDates = apartment.getDates().stream().filter(date -> {
				Calendar c2 = Calendar.getInstance();
				c2.setTime(date);
				c2.set(Calendar.HOUR_OF_DAY, 0);
				c2.set(Calendar.MINUTE, 0);
				c2.set(Calendar.SECOND, 0);
				c2.set(Calendar.MILLISECOND, 0);
				return c1.compareTo(c2) != 0;
			}).collect(Collectors.toCollection(ArrayList::new));
			apartment.setDates(newDates);
		}

		// Calculate Bonus
		double bonus = 1;
		boolean weekend = false;
		ArrayList<Date> holidays = readHolidays();
		for (int i = 1; i <= reservation.getNights(); i++) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(reservation.getStartDate());
			c1.add(Calendar.DATE, i);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);

			if (c1.get(Calendar.DAY_OF_WEEK) == 1 || c1.get(Calendar.DAY_OF_WEEK) > 5)
				weekend = true;

			boolean include = holidays.stream().anyMatch(date -> {
				Calendar c2 = Calendar.getInstance();
				c2.setTime(date);
				c2.set(Calendar.HOUR_OF_DAY, 0);
				c2.set(Calendar.MINUTE, 0);
				c2.set(Calendar.SECOND, 0);
				c2.set(Calendar.MILLISECOND, 0);
				return c1.compareTo(c2) == 0;
			});
			if (include)
				bonus += 0.05;
			if (weekend)
				bonus -= 0.1;
		}
		reservation.setPrice(apartment.getPrice() * reservation.getNights() * bonus);
		reservation.setStatus(ReservationStatus.CREATED);
		ArrayList<Reservation> reservations = readReservations();
		reservations.add(reservation);
		writeReservations(reservations);

		ArrayList<Apartment> apartments = readApartments();
		for (Apartment a : apartments) {
			if (a.getId().equals(apartment.getId())) {
				a.setDates(apartment.getDates());
				writeApartments(apartments);
			}
		}

		return Response.status(Response.Status.CREATED).entity(reservation).build();
	}
	
	//@SuppressWarnings("unlikely-arg-type")
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchReservation(@QueryParam("username")String username, @QueryParam("status")String status) throws JsonParseException, JsonMappingException, NoSuchAlgorithmException, IOException {
		ArrayList<Reservation> reservations = readReservations();
	
		if(username != "" && username != null) {
			User user = getUserByusername(username);
			
			reservations = reservations.stream()
					.filter(reservation -> reservation.getGuestId().equals(user.getId()))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		if(status != "" && status != null) {
			reservations = reservations.stream()
					.filter(reservation -> reservation.getStatus().toString().equals(status))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		return Response.status(Response.Status.OK).entity(reservations).build();
	}
	
	public User getUserByusername(String username)
			throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException {
		for (User u : readUsers()) {
			if (u.getUsername().contentEquals(username))
				return u;
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

	private Apartment getApartmentById(String id) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Apartment> apartments = readApartments();
		for (Apartment apartment : apartments) {
			if (apartment.getId().equals(id))
				return apartment;
		}
		return null;
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

	private ArrayList<Date> readHolidays() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File holidaysFile = new File(ctx.getRealPath(".") + PathConfig.HOLIDAYS_FILE);
		boolean created = holidaysFile.createNewFile();
		if (created)
			mapper.writeValue(holidaysFile, new ArrayList<Date>());
		return mapper.readValue(holidaysFile, new TypeReference<ArrayList<Date>>() {
		});
	}
	
	private ArrayList<User> readUsers() throws IOException, NoSuchAlgorithmException {
		ObjectMapper mapper = new ObjectMapper();
		File userFile = new File(ctx.getRealPath(".") + PathConfig.USERS_FILE);
		boolean created = userFile.createNewFile();
		if (created) {
			ArrayList<User> admins = new ArrayList<User>();
			admins.add(new User("admin", "admin", "Admin FirstName", "Admin LastName", Gender.MALE, Role.ADMIN));
			mapper.writeValue(userFile, admins);
		}

		return mapper.readValue(userFile, new TypeReference<ArrayList<User>>() {
		});
	}

}
