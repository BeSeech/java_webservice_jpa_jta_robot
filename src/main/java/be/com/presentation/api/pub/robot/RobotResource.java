package be.com.presentation.api.pub.robot;

import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
import be.com.helpers.OperationResult;
import be.com.presentation.helpers.ActionType;
import be.com.presentation.helpers.RobotAction;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
public class RobotResource
{
    @EJB
    private RobotBeanService robotBeanService;

    private static final Logger logger = Logger.getLogger(RobotResource.class.getPackage().getName());

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
