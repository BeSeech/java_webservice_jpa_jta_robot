package be.com.business.robot;

import be.com.helpers.OperationResult;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.xml.bind.annotation.XmlRootElement;

@Stateless
@XmlRootElement
public class RobotBean
{
    enum Direction
    {
        Forward, Backward, Unknown
    }

    private String id;
    private String name;
    private double position = 0;
    private Legs legs;
    private boolean isBroken;

    private boolean isBroken()
    {
        return isBroken;
    }

    private void setBroken(boolean broken)
    {
        isBroken = broken;
    }

    private double getStepDistance()
    {
        return 1.0 / legs.getLength();
    }

    public RobotBean()
    {
    }

    @PostConstruct
    private void init()
    {
        reset();
    }

    private boolean isOk()
    {
        return !isBroken;
    }

    private void resetErrorState()
    {
        setBroken(false);
    }

    private void setPosition(double newPosition)
    {
        position = newPosition;
    }

    public String getState()
    {
        if (isOk()) {
            return "[Info] " + String.format("robot position is %.2f m", position);
        }
        return "[Error] " + "robot is broken";
    }

    public int[] getLegSequence()
    {
        return legs.getSequence();
    }

    public void setLegSequence(int[] legSequence) throws Exception
    {
        if (legSequence.length < 1) {
            throw new Exception("Empty leg sequence is forbidden");
        }
        this.legs = new Legs(legSequence);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name) throws Exception
    {
        if (name == "") {
            throw new Exception("Empty name is forbidden");
        }
        this.name = name;
    }

    public void reset()
    {
        resetErrorState();
        if (legs != null) {
            legs.reset();
        }
        setPosition(0);
    }

    public double getPosition()
    {
        if ((position - Math.floor(position)) < 0.01) {
            setPosition(Math.floor(position));
        }
        return position;
    }

    public OperationResult makeStep(int legId, boolean isForward)
    {
        if (!isOk()) {
            return OperationResult.error("Robot doesn't trust you anymore");
        }

        if (!legs.move(legId, isForward))
        {
            setBroken(true);
            return OperationResult.error("Robot is confused by his legs");
        }

        if (isForward) {
            setPosition(getPosition() + getStepDistance());
            return OperationResult.ok("Robot is moving forward");
        }
        else
        {
            setPosition(getPosition() - getStepDistance());
            return OperationResult.ok("Robot is moving backward");
        }
    }

}
