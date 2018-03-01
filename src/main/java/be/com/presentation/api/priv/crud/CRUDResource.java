package be.com.presentation.api.priv.crud;

import be.com.presentation.api.priv.crud.robot.CRUDRobotResource;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.Path;

@Singleton
public class CRUDResource
{
    private static final Logger logger = Logger.getLogger(CRUDResource.class.getPackage().getName());

    @EJB
    CRUDRobotResource CRUDRobotResource;

    @Path("/robots")
    public CRUDRobotResource getCRUDRobotResource()
    {
        return CRUDRobotResource;
    }

}
