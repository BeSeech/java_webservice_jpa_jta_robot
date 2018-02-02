package be.com.data;

import be.com.business.robot.RobotBean;
import be.com.helpers.IntArrayTransformer;
import be.com.helpers.OperationResult;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Singleton
public class RobotCRUDService
{
    @PersistenceContext(unitName = "H2_PU")
    private EntityManager entityManager;

    public static Robot getRobotFromDB(RobotBean robotBean)
    {
        Robot robot = new Robot();
        merge(robotBean, robot);
        return robot;
    }

    public static RobotBean getRobotBean(Robot robot)
    {
        RobotBean robotBean = new RobotBean();
        if (merge(robot, robotBean)) {
            return robotBean;
        }
        return null;
    }

    public static boolean merge(RobotBean fromRobotBean, Robot toRobot)
    {
        toRobot.setId(fromRobotBean.getId());
        toRobot.setName(fromRobotBean.getName());
        toRobot.setLegSequence(IntArrayTransformer.getString(fromRobotBean.getLegSequence(), ",", "", ""));
        return true;
    }

    public static boolean merge(Robot fromRobot, RobotBean toRobotBean)
    {
        try {
            toRobotBean.setId(fromRobot.getId());
            toRobotBean.setName(fromRobot.getName());
            toRobotBean.setLegSequence(IntArrayTransformer.getArray(fromRobot.getLegSequence(), ",", "", ""));
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Transactional
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OperationResult insertRobotBean(RobotBean robotBean)
    {
        if (getRobotBean(robotBean.getId()) != null) {
            return OperationResult.error("Robot with this id already exists");
        }

        Robot robot = getRobotFromDB(robotBean);
        entityManager.persist(robot);
        entityManager.flush();

        return OperationResult.ok();
    }

    @Transactional
    private Robot getRobotFromDB(String id)
    {
        Robot robot = null;
        try {
            robot = entityManager.createNamedQuery("Robot.findById", Robot.class).setParameter("id", id).getSingleResult();
        }
        catch (Exception ex) {
            return null;
        }
        return robot;
    }

    @Transactional
    public RobotBean getRobotBean(String id)
    {
        Robot robot = getRobotFromDB(id);
        if (robot == null) {
            return null;
        }
        return getRobotBean(robot);
    }

    @Transactional
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OperationResult updateRobotBean(RobotBean robotBean)
    {
        Robot robot = getRobotFromDB(robotBean.getId());

        if (robot == null) {
            return OperationResult.error("Robot with this id doesn't exists");
        }

        merge(robotBean, robot);
        entityManager.merge(robot);
        entityManager.flush();

        return OperationResult.ok();
    }

    @Transactional
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OperationResult deleteRobot(String id)
    {
        Robot robot = getRobotFromDB(id);
        if (robot == null)
        {
            return OperationResult.error("Robot with this id doesn't exist");
        }

        entityManager.remove(robot);
        entityManager.flush();

        return OperationResult.ok();
    }
}
