package be.com.presentation.api.pub.robot;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
import be.com.helpers.OperationResult;
import be.com.presentation.helpers.ActionType;
import be.com.presentation.helpers.RobotAction;
import org.junit.Ignore;
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

import java.security.Principal;

@RunWith(MockitoJUnitRunner.class)
public class RobotResourceTest
{
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

        when(robotBeanService.getRobotBean("10")).thenReturn(correctRobotBean);
        when(robotBeanService.getRobotBean("-1")).thenReturn(null);
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

}

