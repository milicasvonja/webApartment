package services;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("")
public class TestController {

	
	@Context
	ServletContext ctx;
	
	@Path("/test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "TESTT";
	}
}
