package be.com.presentation.api.pub;

import be.com.presentation.api.pub.robot.RobotResource;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.Path;

@Singleton
public class PublicResource
{
    private static final Logger logger = Logger.getLogger(PublicResource.class.getPackage().getName());

    @EJB
    RobotResource robotResource;

    @Path("/robots")
    public RobotResource getRobotResource()
    {
        return robotResource;
    }

}
