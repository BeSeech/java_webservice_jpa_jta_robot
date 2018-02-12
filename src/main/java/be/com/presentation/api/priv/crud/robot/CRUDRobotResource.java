package be.com.presentation.api.priv.crud.robot;

import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
import be.com.data.RobotCRUDService;
import be.com.helpers.OperationResult;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Singleton
public class CRUDRobotResource
{
    @EJB
    private RobotBeanService robotBeanService;

    private static final Logger logger = Logger.getLogger(RobotCRUDService.class.getPackage().getName());

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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RobotBean getRobot(@PathParam("id") String id, @Context SecurityContext securityContext) throws Exception
    {
        if (!securityContext.isSecure()) {
            throw new WebApplicationException(401);
        }

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
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), or.getErrorMessage()).build();
    }
}
