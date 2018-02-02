package be.com.business.robot;

import be.com.data.Robot;
import be.com.helpers.OperationResult;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.HashMap;

@Singleton
public class RobotBeanService
{
    private HashMap<String, RobotBean> robotBeanMap = new HashMap<String, RobotBean>();

    @PersistenceContext(unitName = "H2_PU")
    private EntityManager entityManager;

    public RobotBean getRobot(String id) throws Exception
    {
        Robot robot = null;
        try {
            robot = entityManager.createNamedQuery("Robot.findById", Robot.class).setParameter("id", id).getSingleResult();
        }
        catch (Exception ex)
        {
        }
        if (robot != null)
        {
            RobotBean robotBean = new RobotBean();
            robotBean.setId(robot.getId());
            robotBean.setName(robot.getName());
            robotBean.setLegSequence(new int[] {0, 3, 2, 1});
            robotBeanMap.put(robotBean.getId(), robotBean);
        }

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

    @Transactional
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OperationResult addRobot(RobotBean robotBean) throws Exception
    {
        if (getRobot(robotBean.getId()) != null) {
            return OperationResult.error("Robot with this id already exists");
        }

        Robot robot = new Robot();
        robot.setId(robotBean.getId());
        robot.setLegSequence("0, 3, 1, 2");
        robot.setName(robotBean.getName());
        entityManager.persist(robot);
        //entityManager.flush();

        robotBeanMap.put(robotBean.getId(), robotBean);

        return OperationResult.ok();
    }

    public OperationResult updateRobot(RobotBean robotBean) throws Exception
    {
        if (getRobot(robotBean.getId()) == null) {
            return OperationResult.error("Robot is not found");
        }
        robotBeanMap.put(robotBean.getId(), robotBean);
        return OperationResult.ok();
    }
}
