package be.com.rest;

import be.com.bean.robot.RobotBean;
import be.com.bean.robot.RobotBeanService;
import be.com.helpers.OperationResult;

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
    public Response putRobot(RobotBean robotBean)
    {
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }
        OperationResult or = robotBeanService.updateRobot(robotBean);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        throw new WebApplicationException(404);
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