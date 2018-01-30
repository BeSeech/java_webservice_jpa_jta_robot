package be.com.rest;

import be.com.bean.RobotBean;
import be.com.bean.RobotBeanService;
import be.com.helpers.OperationResult;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.lang.Integer.parseInt;

@Path("/")
public class RobotResource
{
    @EJB
    private RobotBeanService robotBeanService;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postRobot(RobotBean newRobotBean)
    {
        OperationResult or = robotBeanService.addRobot(newRobotBean);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Already exists").build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putRobot(RobotBean newRobotBean)
    {
        return Response.status(100).build();
//        return Response.ok(newRobotBean, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RobotBean getRobot(@PathParam("id") String id)
    {
        RobotBean robotBean = robotBeanService.getRobot(id);
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }
        return robotBean;
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public Response deleteRobot(@PathParam("id") String id)
    {
        OperationResult or = robotBeanService.deleteRobot(id);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        throw new WebApplicationException(404);
    }

}