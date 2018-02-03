package be.com.presentation.robot;

import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
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
    public Response postRobot(RobotBean newRobotBean) throws Exception
    {
        OperationResult or = robotBeanService.addRobotBean(newRobotBean);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), or.getErrorMessage()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putRobot(RobotBean robotBean) throws Exception
    {
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }
        OperationResult or = robotBeanService.updateRobotBean(robotBean);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        throw new WebApplicationException(404);
    }

    @GET
    @Path("/{id}/state")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRobotState(@PathParam("id") String id) throws Exception
    {
        RobotBean robotBean = robotBeanService.getRobotBean(id);
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }
        return robotBean.getState();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RobotBean getRobot(@PathParam("id") String id) throws Exception
    {
        RobotBean robotBean = robotBeanService.getRobotBean(id);
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

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/Do")
    public String Do(@PathParam("id") String id, RobotAction action) throws Exception
    {
        RobotBean robotBean = robotBeanService.getRobotBean(id);
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }

        boolean isForward = action.getActionType() == ActionType.StepForward;
        OperationResult or = robotBean.makeStep(action.getLegId(), isForward);
        if (or.isOk()) {
            return "[Info] " + or.getCommentMessage();
        }
        return "[Error] " + or.getErrorMessage();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}/Reset")
    public Response reset(@PathParam("id") String id) throws Exception
    {
        RobotBean robotBean = robotBeanService.getRobotBean(id);
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }

        robotBean.reset();

        return Response.noContent().build();
    }
}