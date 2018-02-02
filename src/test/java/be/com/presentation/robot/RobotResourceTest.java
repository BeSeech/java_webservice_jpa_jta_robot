package be.com.presentation.robot;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
import be.com.helpers.OperationResult;
import org.junit.Rule;
import org.junit.rules.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.containsString;
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
    private int[] legSequence = new int[] {0, 3, 1, 2};

    @Mock
    private RobotBeanService robotBeanService;

    @InjectMocks
    RobotResource robotResource;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initRobot() throws Exception
    {
        correctRobotBean = new RobotBean();
        correctRobotBean.setName("TDrone");
        correctRobotBean.setId("10");
        correctRobotBean.setLegSequence(legSequence);
        copyRobotBean = new RobotBean();
        copyRobotBean.setName("TDrone");
        copyRobotBean.setId("10");
        deletedRobotBean = new RobotBean();
        deletedRobotBean.setName("The old TDrone");
        deletedRobotBean.setId("100");

        when(robotBeanService.getRobotBean("10")).thenReturn(correctRobotBean);
        when(robotBeanService.getRobotBean("-1")).thenReturn(null);
        when(robotBeanService.deleteRobot("10")).thenReturn(OperationResult.ok());
        when(robotBeanService.deleteRobot("-1")).thenReturn(OperationResult.error("robot is not found"));
        when(robotBeanService.addRobotBean(correctRobotBean)).thenReturn(OperationResult.ok());
        when(robotBeanService.addRobotBean(copyRobotBean)).thenReturn(OperationResult.error("robot already exists"));
        when(robotBeanService.updateRobotBean(correctRobotBean)).thenReturn(OperationResult.ok());
        when(robotBeanService.updateRobotBean(deletedRobotBean)).thenReturn(OperationResult.error("robot is not found"));
    }

    @Test
    public void canMakeTrueStepForward() throws Exception
    {
        RobotAction action = new RobotAction(ActionType.StepForward, 0);
        String result = robotResource.Do("10", action);

        assertThat("Robot make step forward", result, containsString("Info"));
    }

    @Test
    public void canMakeTrueStepBackward() throws Exception
    {
        RobotAction action = new RobotAction(ActionType.StepBackward, 0);
        String result = robotResource.Do("10", action);

        assertThat("Robot make step backward", result, containsString("Info"));
    }

    @Test
    public void cantMakeStepByNonexistentRobot() throws Exception
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");
        RobotAction action = new RobotAction(ActionType.StepBackward, 0);

        robotResource.Do("-10", action);
    }

    @Test
    public void canResetRobotState() throws Exception
    {
        Response response = robotResource.reset("10");

        assertThat("We can reset state of existent robot", response.getStatus(), equalTo(204));
    }

    @Test
    public void cantResetNonexistentRobotState() throws Exception
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = robotResource.reset("-1");
    }

    @Test
    public void getExistentRobot() throws Exception
    {
        RobotBean robot = robotResource.getRobot("10");

        assertThat("We can get existent robot", robot.getName(), equalTo(correctRobotBean.getName()));
    }

    @Test
    public void getNonexistentRobot() throws Exception
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
    public void postNonexistentRobot() throws Exception
    {
        Response response = robotResource.postRobot(correctRobotBean);

        assertThat("We can add correct robot", response.getStatus(), equalTo(204));
    }

    @Test
    public void postExistentRobot() throws Exception
    {
        Response response = robotResource.postRobot(copyRobotBean);

        assertThat("We can't add robot that already exists", response.getStatus(), equalTo(400));
    }

    @Test
    public void putExistentRobot() throws Exception
    {
        Response response = robotResource.putRobot(correctRobotBean);

        assertThat("We can update robot that exists", response.getStatus(), equalTo(204));
    }

    @Test
    public void putNonexistentRobot() throws Exception
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = robotResource.putRobot(deletedRobotBean);
    }

    @Test
    public void putNullAsRobot() throws Exception
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = robotResource.putRobot(null);
    }

}

