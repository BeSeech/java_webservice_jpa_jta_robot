package be.com.data;

import be.com.business.robot.RobotBean;
import be.com.helpers.IntArrayTransformer;
import be.com.helpers.OperationResult;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

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
        //logger.debug("We in getRobotBean(" + robot.toString() + ")");

        RobotBean robotBean = new RobotBean();
        //logger.debug("We're going to try merge");
        if (merge(robot, robotBean)) {
            //logger.debug("The trying to merge returned false");
            return robotBean;
        }
        //logger.debug("The trying to merge returned false");
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
        //logger.debug("We in merge( from:" + fromRobot.toString() +"; to: RobotBean )");
        try {
            //logger.debug("We're going to toRobotBean.setId("+fromRobot.getId()+")");
            toRobotBean.setId(fromRobot.getId());
            //logger.debug("We're going to toRobotBean.setName("+fromRobot.getName()+")");
            toRobotBean.setName(fromRobot.getName());
            //logger.debug("We're going to toRobotBean.setLegSequence("+fromRobot.getLegSequence()+")");
            toRobotBean.setLegSequence(IntArrayTransformer.getArray(fromRobot.getLegSequence(), ",", "", ""));
        }
        catch (Exception ex) {
            //logger.debug("As the result we have an exception:"+ex.getMessage()+")");
            return false;
        }
        //logger.debug("The merge is done");
        return true;
    }

    public OperationResult insertRobotBean(RobotBean robotBean)
    {
        if (getRobotBean(robotBean.getId()) != null) {
            return OperationResult.error("Robot with this id already exists");
        }
        try {
            Robot robot = getRobot(robotBean);
            logger.debug("Saving to DB Robot: " + robot.toString());
            entityManager.persist(robot);
            entityManager.flush();
        }
        catch (Exception ex)
        {
            logger.error("Error in insertRobotBean(): "+ex);
            return OperationResult.error("Error in insertRobotBean(): "+ex.getMessage());
        }
        return OperationResult.ok();
    }

    private Robot getRobotFromDB(String id)
    {
        //logger.debug("We in getRobotFromDB(" + id + ")");
        Robot robot = null;
        try {
            robot = entityManager.createNamedQuery("Robot.findById", Robot.class).setParameter("id", id).getSingleResult();
            //logger.debug("Try to get robot with id: " + id);
        }
        catch (Exception ex) {
            //logger.debug("Try to get robot with id lead to exception: " + ex.getMessage());
            return null;
        }
        //logger.debug("Try to get robot with id result: " + robot.toString());
        return robot;
    }

    public RobotBean getRobotBean(String id)
    {
        //logger.debug("Try to get robot with id: " + id);
        Robot robot = getRobotFromDB(id);
        if (robot == null) {
            //logger.debug("Try to get robot with id result: null");
            return null;
        }
        //logger.debug("Try to get robot with id result: " + robot.toString());
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
