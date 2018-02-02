package be.com.data;

import be.com.business.robot.RobotBean;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RobotCRUDServiceTest
{
    private Robot standardRobot;
    private RobotBean standardRobotBean;

    @Before
    public void init() throws Exception
    {
        standardRobot = new Robot();
        standardRobot.setName("TestRobotName");
        standardRobot.setId("TestRobotId");
        standardRobot.setLegSequence("0,3,2,1");

        standardRobotBean = new RobotBean();
        standardRobotBean.setName(standardRobot.getName());
        standardRobotBean.setId(standardRobot.getId());
        standardRobotBean.setLegSequence(new int[] {0,3,2,1});
    }

    @Test
    public void weCanGetRobotFromRobotBean()
    {
        Robot robot = RobotCRUDService.getRobot(standardRobotBean);

        assertThat("robot is equal to standardRobot", robot, equalTo(standardRobot));
    }

    @Test
    public void weCanGetRobotBeanFromRobot() throws Exception
    {
        RobotBean robotBean = RobotCRUDService.getRobotBean(standardRobot);

        assertThat("robotBean is equal to standardRobotBean", robotBean, equalTo(standardRobotBean));
    }

    @Test
    public void weCanMergeRobotBeanToRobot()
    {
        Robot robot = new Robot();

        RobotCRUDService.merge(standardRobotBean, robot);

        assertThat("merged robot is equal to standardRobot", robot, equalTo(standardRobot));
    }

    @Test
    public void weCanMergeRobotToRobotBean() throws Exception
    {
        RobotBean robotBean = new RobotBean();

        RobotCRUDService.merge(standardRobot, robotBean);

        assertThat("merged robotBean is equal to standardRobotBean", robotBean, equalTo(standardRobotBean));
    }
}
