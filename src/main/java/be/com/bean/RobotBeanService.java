package be.com.bean;

import be.com.helpers.OperationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
            return OperationResult.error("Robot is not found");
        }
        return OperationResult.ok();
    }

    public OperationResult addRobot(RobotBean robotBean)
    {
        if (getRobot(robotBean.getId()) != null) {
            return OperationResult.error("Robot with this id already exists");
        }
        robotBeanMap.put(robotBean.getId(), robotBean);
        return OperationResult.ok();
    }
}
