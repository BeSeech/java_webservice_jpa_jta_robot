package be.com.presentation.robot;

import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
import be.com.helpers.OperationResult;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static java.lang.Integer.parseInt;

@Path("/")
public class RobotResource
{
    @EJB
    private RobotBeanService robotBeanService;

    @POST
    @Path("/secure/")
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
    @Path("/secure/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putRobot(@PathParam("id") String id, RobotBean robotBean) throws Exception
    {
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }
        robotBean.setId(id);
        OperationResult or = robotBeanService.updateRobotBean(robotBean);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), or.getErrorMessage()).build();
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
    @Path("/secure/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RobotBean getRobot(@PathParam("id") String id, @Context SecurityContext securityContext) throws Exception
    {
        if (!securityContext.isSecure())
            throw new WebApplicationException(401);

        RobotBean robotBean = robotBeanService.getRobotBean(id);
        if (robotBean == null) {
            throw new WebApplicationException(404);
        }

        return robotBean;
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/secure/{id}")
    public Response deleteRobot(@PathParam("id") String id)
    {
        OperationResult or = robotBeanService.deleteRobot(id);
        if (or.isOk()) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), or.getErrorMessage()).build();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/do")
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
    @Path("/{id}/reset")
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