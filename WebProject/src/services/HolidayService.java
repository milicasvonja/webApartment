package services;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Reservation;
import beans.Role;
import beans.User;
import config.PathConfig;

@Path("/holiday")
public class HolidayService {

	@Context
	ServletContext ctx;

	@Context
	HttpServletRequest request;

	@Path("")
	@POST
	public Response addHoliday(Date date) throws JsonParseException, JsonMappingException, IOException {
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if (loggedUser.getRole() != Role.ADMIN)
			return Response.status(Response.Status.FORBIDDEN).build();

		ArrayList<Date> holidays = readHolidays();
		holidays.add(date);
		writeHolidays(holidays);
		return Response.status(Response.Status.OK).entity(date).build();
	}

	@Path("")
	@GET
	public Response getHolidays() throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Date> holidays = readHolidays();
		return Response.status(Response.Status.OK).entity(holidays).build();
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

	private void writeHolidays(ArrayList<Date> holidays) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File holidaysFile = new File(ctx.getRealPath(".") + PathConfig.HOLIDAYS_FILE);
		holidaysFile.createNewFile();
		mapper.writeValue(holidaysFile, holidays);
	}

}
