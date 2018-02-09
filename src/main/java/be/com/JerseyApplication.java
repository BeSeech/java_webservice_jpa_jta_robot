package be.com;

import be.com.presentation.robot.RobotResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class JerseyApplication extends ResourceConfig
{
    public JerseyApplication()
    {
        super();
        register(RobotResource.class);
        register(JsonProvider.class);
        register(JacksonFeature.class);

    }
}
