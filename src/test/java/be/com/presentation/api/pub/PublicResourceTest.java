package be.com.presentation.api.pub;

import be.com.presentation.api.pub.robot.RobotResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PublicResourceTest
{
    @Mock
    private RobotResource robotResource;

    @InjectMocks
    private PublicResource publicResource;

    @Test
    public void getRobotResource()
    {
        RobotResource result = publicResource.getRobotResource();

        assertThat("getRobotResource() returned appropriate RobotResource", result, equalTo(robotResource));
    }

}
