package be.com.business.robot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RobotBeanTest
{
    private RobotBean robotBean = new RobotBean();
    private char decimalSep;

    private void moveOneSequenceForward(int[] legSequence)
    {
        for (int i = 0;
             i < legSequence.length;
             i++) {
            robotBean.makeStep(legSequence[i], true);
        }
    }

    private void moveOneSequenceBackward(int[] legSequence)
    {
        for (int i = legSequence.length - 1;
             i >= 0;
             i--) {
            robotBean.makeStep(legSequence[i], false);
        }
    }


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init() throws Exception
    {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols( Locale.getDefault());
        decimalSep = dfs.getDecimalSeparator();

        robotBean.setLegSequence(new int[]{1, 2, 3, 4});
        robotBean.setName("TDrone");
    }


    @Test
    public void weCanSetAndGetName() throws Exception
    {
        robotBean.setName("A");

        assertThat("Robot name is exactly what we set", robotBean.getName(), equalTo("A"));
    }

    @Test
    public void weCantSetEmptyName() throws Exception
    {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage("Empty name is forbidden");

        robotBean.setName("");
    }

    @Test
    public void weCanSetLeqSequence() throws Exception
    {
        robotBean.setLegSequence(new int[]{1, 2, 3, 4});

        assertThat("We can set and get the same leg sequence value", robotBean.getLegSequence(), equalTo(new int[]{1, 2, 3, 4}));
    }

    @Test
    public void weCantSetEmptyLeqSequence() throws Exception
    {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage("Empty leg sequence is forbidden");

        robotBean.setLegSequence(new int[]{});
    }

    @Test
    public void newPositionIs0()
    {
        assertThat("New robot position is 0", robotBean.getPosition(), equalTo(0.0));
    }

    @Test
    public void robotHasAValidInitialState()
    {
        assertThat("Robot has a state", robotBean.getState(), equalTo(getInitialStateString()));
    }

    @Test
    public void weCanMoveForwardOneMeter()
    {
        robotBean.reset();

        moveOneSequenceForward(robotBean.getLegSequence());

        assertThat("Position after moving forward is 1 m", robotBean.getPosition(), equalTo(1.0));
    }

    @Test
    public void weCanMoveBackwardOneMeter()
    {
        robotBean.reset();

        moveOneSequenceBackward(robotBean.getLegSequence());

        assertThat("Position after moving backward is -1 m", robotBean.getPosition(), equalTo(-1.0));
    }


    @Test
    public void weCanMoveForwardTenMeters()
    {
        robotBean.reset();
        for (int meters = 0;
             meters < 10;
             meters++) {
            moveOneSequenceForward(robotBean.getLegSequence());
        }

        assertThat("Position after moving forward ten times is 10 m", robotBean.getPosition(), equalTo(10.0));
    }

    @Test
    public void weCanMoveBackwardTenMeters()
    {
        robotBean.reset();
        for (int meters = 0;
             meters < 10;
             meters++) {
            moveOneSequenceBackward(robotBean.getLegSequence());
        }

        assertThat("Position after moving backward ten times is -10 m", robotBean.getPosition(), equalTo(-10.0));
    }

    @Test
    public void weCanResetRobotToInitialState()
    {
        moveOneSequenceForward(robotBean.getLegSequence());
        robotBean.reset();

        assertThat("Robot has initial position after reset", robotBean.getState(), equalTo(getInitialStateString()));
    }

    private Object getInitialStateString()
    {
        return String.format("[Info] Robot position is 0%s00 m", decimalSep);
    }

    @Test
    public void afterWrongLegSequenceTheRobotIsBroken()
    {
        robotBean.reset();
        robotBean.makeStep(robotBean.getLegSequence()[0], true);
        robotBean.makeStep(robotBean.getLegSequence()[2], true);

        assertThat("After wrong leg sequence the robot is broken", robotBean.getState(), containsString("[Error]"));
    }

    @Test
    public void weCanMoveHalfDistanceForwardAndHalfDistanceBackward()
    {
        int stepsCount = robotBean.getLegSequence().length / 2;
        int[] halfLegSequence = Arrays.copyOfRange(robotBean.getLegSequence(), 0, stepsCount);

        moveOneSequenceForward(halfLegSequence);
        moveOneSequenceBackward(halfLegSequence);

        assertThat("Robot returned in initial position", robotBean.getState(), equalTo(getInitialStateString()));
    }
}
