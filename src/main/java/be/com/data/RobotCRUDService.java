package be.com.data;

import be.com.business.robot.RobotBean;
import be.com.helpers.IntArrayTransformer;
import be.com.helpers.OperationResult;
import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;

@Stateless
public class RobotCRUDService
{
    @PersistenceContext(unitName = "H2_PU")
    private EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(RobotCRUDService.class.getPackage().getName());

    public static Robot getRobot(RobotBean robotBean)
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

    public OperationResult insertRobotBean(RobotBean robotBean)
    {
        if (getRobotBean(robotBean.getId()) != null) {
            return OperationResult.error("Robot with this id already exists");
        }
        try {
            Robot robot = getRobot(robotBean);
            entityManager.persist(robot);
            entityManager.flush();
        }
        catch (Exception ex)
        {
            return OperationResult.error("Error in insertRobotBean(): "+ex.getMessage());
        }
        return OperationResult.ok();
    }

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

    public List<Robot> getRobotsFromDB()
    {
        List<Robot> robotList = null;
        try {
            robotList = entityManager.createNamedQuery("Robot.findAllRobots", Robot.class).getResultList();
        }
        catch (Exception ex) {
            return null;
        }
            return robotList;
    }


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
        if (robot == null) {
            return OperationResult.error("Robot with this id doesn't exist");
        }

        entityManager.remove(robot);
        entityManager.flush();

        return OperationResult.ok();
    }

    @Remove
    public void finished()
    {}
}
