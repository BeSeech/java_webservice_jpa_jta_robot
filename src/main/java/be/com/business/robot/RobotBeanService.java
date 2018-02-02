package be.com.business.robot;

import be.com.data.Robot;
import be.com.data.RobotCRUDService;
import be.com.helpers.OperationResult;

import javax.ejb.*;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashMap;

@Singleton
public class RobotBeanService
{
    private HashMap<String, RobotBean> robotBeanMap = new HashMap<String, RobotBean>();

    @EJB
    RobotCRUDService robotCRUDService;

    @PersistenceContext(unitName = "H2_PU")
    private EntityManager entityManager;

    private boolean isRobotBeanInCash(String id)
    {
        return (robotBeanMap.getOrDefault(id, null) != null);
    }

    public RobotBean getRobotBean(String id)
    {
        if (!isRobotBeanInCash(id)) {
            RobotBean robotBean = robotCRUDService.getRobotBean(id);
            if (robotBean != null) {
                robotBeanMap.put(robotBean.getId(), robotBean);
            }
        }

        return robotBeanMap.getOrDefault(id, null);
    }

    public OperationResult deleteRobot(String id)
    {
        OperationResult or = robotCRUDService.deleteRobot(id);

        if (!or.isOk()) {
            return or;
        }

        RobotBean result = robotBeanMap.remove(id);
        if (result == null) {
            return OperationResult.error("Robot is not found");
        }
        return OperationResult.ok();
    }

    public OperationResult addRobotBean(RobotBean robotBean) throws Exception
    {
        if (isRobotBeanInCash(robotBean.getId())) {
            return OperationResult.error("Robot with this id already exists");
        }

        OperationResult or = robotCRUDService.insertRobotBean(robotBean);

        if (!or.isOk()) {
            return or;
        }

        robotBeanMap.put(robotBean.getId(), robotBean);

        return OperationResult.ok();
    }

    public OperationResult updateRobotBean(RobotBean robotBean) throws Exception
    {
        if (!isRobotBeanInCash(robotBean.getId())) {
            return OperationResult.error("Robot is not found");
        }
        OperationResult or = robotCRUDService.updateRobotBean(robotBean);
        if (!or.isOk()) {
            return or;
        }

        robotBeanMap.put(robotBean.getId(), robotBean);
        return OperationResult.ok();
    }
}
