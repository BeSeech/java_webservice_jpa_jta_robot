package be.com.rest;

import be.com.bean.RobotBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RobotResource
{
    private RobotBean robotBean = new RobotBean();

    @PostConstruct
    public void init()
    {
        robotBean.setName("TDrone");
        robotBean.setId("10");
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postRobot(RobotBean newRobotBean)
    {
        return Response.ok(newRobotBean, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putRobot(RobotBean newRobotBean)
    {
        return Response.ok(newRobotBean, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public RobotBean getRobot(@PathParam("id") String id)
    {
        if (id.equals(robotBean.getId()))
            return robotBean;
        else
            return new RobotBean();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String deleteRobot(@PathParam("id") String id)
    {
        if (id.equals(robotBean.getId()))
            return "Status: Ok";
        else
            return "Status: Error";
    }

}
