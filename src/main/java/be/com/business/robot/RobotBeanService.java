package be.com.business.robot;

import be.com.helpers.OperationResult;

import javax.ejb.Stateless;
import java.util.HashMap;

@Stateless
public class RobotBeanService
{
    private HashMap<String, RobotBean> robotBeanMap = new HashMap<String, RobotBean>();

    public RobotBean getRobot(String id)
    {
        return robotBeanMap.getOrDefault(id, null);
    }

    public OperationResult deleteRobot(String id)
    {
        RobotBean result = robotBeanMap.remove(id);
        if (result == null) {
            return OperationResult.error("robot is not found");
        }
        return OperationResult.ok();
    }

    public OperationResult addRobot(RobotBean robotBean)
    {
        if (getRobot(robotBean.getId()) != null) {
            return OperationResult.error("robot with this id already exists");
        }
        robotBeanMap.put(robotBean.getId(), robotBean);
        return OperationResult.ok();
    }

    public OperationResult updateRobot(RobotBean robotBean)
    {
        if (getRobot(robotBean.getId()) == null) {
            return OperationResult.error("robot is not found");
        }
        robotBeanMap.put(robotBean.getId(), robotBean);
        return OperationResult.ok();
    }
}
