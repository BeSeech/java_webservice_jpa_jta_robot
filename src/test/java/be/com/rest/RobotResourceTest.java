package be.com.rest;


import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import be.com.bean.RobotBean;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class RobotResourceTest extends JerseyTest
{

    private RobotBean robotBean;

    @Override
    protected Application configure() {
        return new ResourceConfig(RobotResource.class);
    }


    @Before
    public void initRobot()
    {
        robotBean = new RobotBean();
        robotBean.setName("TDrone");
    }

    @Test
    public void testGetRobot() {


        final RobotBean responseRobot = target().path("/10").request().get(RobotBean.class);


        assertThat("Robot:Get has a correct name ",responseRobot.getName(), equalTo(robotBean.getName()));
    }

    @Test
    public void testPostRobot()
    {
        Entity<RobotBean> entity = Entity.entity(robotBean, MediaType.APPLICATION_JSON_TYPE);


        final RobotBean newRobotBean = target().path("/").request().post(entity, RobotBean.class);


        assertThat("Robot:Post is working", robotBean.getName(), equalTo(newRobotBean.getName()));
    }

    @Test
    public void testPutRobot()
    {

        Entity<RobotBean> entity = Entity.entity(robotBean, MediaType.APPLICATION_JSON_TYPE);

        final RobotBean newRobotBean = target().path("/10").request().put(entity, RobotBean.class);

        assertThat("Robot:Put is working", robotBean.getName(), equalTo(newRobotBean.getName()));
    }

    @Test
    public void testDeleteRobot()
    {


        final String responseMsg = target().path("/10").request().delete(String.class);


        assertThat("Robot:Delete is working", responseMsg, equalTo("Status: Ok"));
    }

}

