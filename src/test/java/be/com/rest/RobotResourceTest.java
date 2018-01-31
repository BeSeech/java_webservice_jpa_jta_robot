package be.com.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import be.com.bean.RobotBean;
import be.com.bean.RobotBeanService;
import be.com.helpers.OperationResult;
import org.junit.Rule;
import org.junit.rules.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class RobotResourceTest
{
    private String NOT_FOUND = "HTTP 404 Not Found";
    private RobotBean correctRobotBean;
    private RobotBean copyRobotBean;
    private RobotBean deletedRobotBean;

    @Mock
    private RobotBeanService robotBeanService;

    @InjectMocks
    RobotResource robotResource;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initRobot()
    {
        correctRobotBean = new RobotBean();
        correctRobotBean.setName("TDrone");
        correctRobotBean.setId("10");
        copyRobotBean = new RobotBean();
        copyRobotBean.setName("TDrone");
        copyRobotBean.setId("10");
        deletedRobotBean = new RobotBean();
        deletedRobotBean.setName("The old TDrone");
        deletedRobotBean.setId("100");

        when(robotBeanService.getRobot("10")).thenReturn(correctRobotBean);
        when(robotBeanService.getRobot("-1")).thenReturn(null);
        when(robotBeanService.deleteRobot("10")).thenReturn(OperationResult.ok());
        when(robotBeanService.deleteRobot("-1")).thenReturn(OperationResult.error("Robot is not found"));
        when(robotBeanService.addRobot(correctRobotBean)).thenReturn(OperationResult.ok());
        when(robotBeanService.addRobot(copyRobotBean)).thenReturn(OperationResult.error("Robot already exists"));
        when(robotBeanService.updateRobot(correctRobotBean)).thenReturn(OperationResult.ok());
        when(robotBeanService.updateRobot(deletedRobotBean)).thenReturn(OperationResult.error("Robot is not found"));
    }

    @Test
    public void getExistentRobot()
    {
        RobotBean robot = robotResource.getRobot("10");

        assertThat("We can get existent robot", robot.getName(), equalTo(correctRobotBean.getName()));
    }

    @Test
    public void getNonexistentRobot()
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        robotResource.getRobot("-1");
    }

    @Test
    public void deleteExistentRobot()
    {
        Response response = robotResource.deleteRobot("10");

        assertThat("We can delete existent robot", response.getStatus(), equalTo(204));
    }

    @Test
    public void deleteNonexistentRobot()
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = robotResource.deleteRobot("-1");
    }

    @Test
    public void postNonexistentRobot()
    {
        Response response = robotResource.postRobot(correctRobotBean);

        assertThat("We can add correct robot", response.getStatus(), equalTo(204));
    }

    @Test
    public void postExistentRobot()
    {
        Response response = robotResource.postRobot(copyRobotBean);

        assertThat("We can't add robot that already exists", response.getStatus(), equalTo(400));
    }

    @Test
    public void putExistentRobot()
    {
        Response response = robotResource.putRobot(correctRobotBean);

        assertThat("We can update robot that exists", response.getStatus(), equalTo(204));
    }

    @Test
    public void putNonexistentRobot()
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = robotResource.putRobot(deletedRobotBean);
    }

    @Test
    public void putNullAsRobot()
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = robotResource.putRobot(null);
    }

}

