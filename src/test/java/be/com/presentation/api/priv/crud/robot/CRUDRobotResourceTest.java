package be.com.presentation.api.priv.crud.robot;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import be.com.business.robot.RobotBean;
import be.com.business.robot.RobotBeanService;
import be.com.data.Robot;
import be.com.helpers.OperationResult;
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
public class CRUDRobotResourceTest
{
    private RobotBean correctRobotBean;
    private RobotBean copyRobotBean;
    private RobotBean deletedRobotBean;
    private int[] legSequence = new int[] {0, 3, 1, 2};

    @Mock
    private RobotBeanService robotBeanService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Principal principal;

    @InjectMocks
    CRUDRobotResource crudRobotResource;

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

//        when(securityContext.isSecure()).thenReturn(true);

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
    public void getExistentRobot() throws Exception
    {
        RobotBean robotBean = crudRobotResource.getRobot("10", securityContext);
        assertThat("We can get existent robot", robotBean.getName(), equalTo(correctRobotBean.getName()));
    }

    @Test
    public void getNonexistentRobot() throws Exception
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        crudRobotResource.getRobot("-1", securityContext);
    }

    @Test
    public void deleteExistentRobot()
    {
        Response response = crudRobotResource.deleteRobot("10");

        assertThat("We can delete existent robot", response.getStatus(), equalTo(204));
    }

    @Test
    public void deleteNonexistentRobot()
    {
        Response response = crudRobotResource.deleteRobot("-1");

        assertThat("We can't delete robot that doesn't exist", response.getStatus(), equalTo(404));
    }

    @Test
    public void postNonexistentRobot() throws Exception
    {
        Response response = crudRobotResource.postRobot(correctRobotBean);

        assertThat("We can add correct robot", response.getStatus(), equalTo(204));
    }

    @Test
    public void postExistentRobot() throws Exception
    {
        Response response = crudRobotResource.postRobot(copyRobotBean);

        assertThat("We can't add robot that already exists", response.getStatus(), equalTo(400));
    }

    @Test
    public void putExistentRobot() throws Exception
    {
        Response response = crudRobotResource.putRobot(correctRobotBean.getId(), correctRobotBean);

        assertThat("We can update robot that exists", response.getStatus(), equalTo(204));
    }

    @Test
    public void putNonexistentRobot() throws Exception
    {
        Response response = crudRobotResource.putRobot(correctRobotBean.getId(), deletedRobotBean);

        assertThat("We can't update robot that doesn't exist", response.getStatus(), equalTo(404));
    }

    @Test
    public void putNullAsRobot() throws Exception
    {
        exceptionRule.expect(WebApplicationException.class);
        exceptionRule.expectMessage("404");

        Response response = crudRobotResource.putRobot("", null);
    }

}

