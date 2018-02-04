package be.com.business.robot;

import be.com.helpers.OperationResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Stateless
public class RobotBean
{
    enum Direction
    {
        Forward, Backward, Unknown
    }

    private String id;
    private String name;

    @JsonIgnore
    private Legs legs;
    @JsonIgnore
    private double position = 0;
    @JsonIgnore
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

    @JsonIgnore
    public String getState()
    {
        if (isOk()) {
            return "[Info] " + String.format("robot position is %.2f m", position);
        }
        return "[Error] " + "robot is broken";
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
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
    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE).toString();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof RobotBean))
            return false;
        RobotBean otherRobot = (RobotBean)other;
        return this.toString().equals(otherRobot.toString());
    }
}
