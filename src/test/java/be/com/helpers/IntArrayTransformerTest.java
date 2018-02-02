package be.com.helpers;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntArrayTransformerTest
{
    private int[] legSequence = new int[] {0, 3, 2, 1};
    private String legSequenceInString = "{0,3,2,1}";

    @Test
    public void arrayToString()
    {
        String result = IntArrayTransformer.getString(legSequence, ",", "{", "}");

        assertThat("int[] {0, 3, 2, 1} toString is \"{0,3,2,1}\"", result, equalTo(legSequenceInString));
    }

    @Test
    public void arrayFromString()
    {
        int[] intArr = IntArrayTransformer.getArray(legSequenceInString, ",", "{", "}");

        assertThat("\"{0,3,2,1}\" to int[] is {0, 3, 2, 1}", intArr, equalTo(legSequence));
    }
}
