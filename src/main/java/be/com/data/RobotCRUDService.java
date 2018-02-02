package be.com.data;

import be.com.business.robot.RobotBean;
import be.com.helpers.IntArrayTransformer;

public class RobotCRUDService
{


    public static Robot getRobot(RobotBean robotBean)
    {
        Robot robot = new Robot();
        merge(robotBean, robot);
        return robot;
    }

    public static void merge(RobotBean fromRobotBean, Robot toRobot)
    {
        toRobot.setId(fromRobotBean.getId());
        toRobot.setName(fromRobotBean.getName());
        toRobot.setLegSequence(IntArrayTransformer.getString(fromRobotBean.getLegSequence(), ",", "", ""));
    }

    public static void merge(Robot fromRobot, RobotBean toRobotBean) throws Exception
    {
        toRobotBean.setId(fromRobot.getId());
        toRobotBean.setName(fromRobot.getName());
        toRobotBean.setLegSequence(IntArrayTransformer.getArray(fromRobot.getLegSequence(), ",", "", ""));
    }

    public static RobotBean getRobotBean(Robot robot) throws Exception
    {
        RobotBean robotBean = new RobotBean();
        merge(robot, robotBean);
        return robotBean;
    }
}
