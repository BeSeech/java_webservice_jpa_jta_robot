package be.com.business.robot;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class LegsTest
{
    private Legs legs;
    private int[] legSequence;

    @Before
    public void init()
    {
        legSequence = new int[] {0, 3, 1, 2};
        legs = new Legs(legSequence);
    }

    @Test
    public void initialStateIsCorrect()
    {
        assertThat("Initial state is correct", legs.isOk(), equalTo(true));
    }

    @Test
    public void weCanMoveOneLegForward()
    {
        boolean result = legs.move(legSequence[0], true);

        assertThat("moveForward() result is true", result, equalTo(true));
    }

    @Test
    public void weCanMoveOneLegBackward()
    {
        boolean result = legs.move(legSequence[legSequence.length-1], false);

        assertThat("moveBackward() result is true", result, equalTo(true));
    }

    @Test
    public void weCanMoveAllLegsBackward()
    {
        for (int i = 0; i< legSequence.length; i++)
        {
            legs.move(legSequence[i], false);
        }

        assertThat("After moving all legs backward we have correct state", legs.isOk(), equalTo(true));
    }

    @Test
    public void legsCountIsCorrect()
    {
        assertThat("Legs count is correct", legs.getLength(), equalTo(legSequence.length));
    }

    @Test
    public void legsSequenceIsCorrect()
    {
        assertThat("Legs sequence is correct", legs.getSequence(), equalTo(legSequence));
    }

    @Test
    public void weCantMoveWithIncorrectLegsSequence()
    {
        for (int i = 0; i< legSequence.length; i++)
        {
            legs.move(i, true);
        }

        assertThat("After moving all legs (incorrect sequence) we have incorrect state", legs.isOk(), equalTo(false));
    }

    @Test
    public void weCanMoveAllLegsForward()
    {
        for (int i = 0; i< legSequence.length; i++)
        {
            legs.move(legSequence[i], true);
        }

        assertThat("After moving all legs forward we have correct state", legs.isOk(), equalTo(true));
    }


    @Test
    public void weCanResetToInitialState()
    {
        for (int i = 0; i< legSequence.length; i++)
        {
            legs.move(i, true);
        }

        legs.reset();

        assertThat("State after reset is correct", legs.isOk(), equalTo(true));
    }
}
