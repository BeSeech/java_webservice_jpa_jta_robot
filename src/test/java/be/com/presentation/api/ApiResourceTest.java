package be.com.presentation.api;

import be.com.presentation.api.priv.PrivateResource;
import be.com.presentation.api.pub.PublicResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApiResourceTest
{

    @Mock
    private PublicResource publicResource;

    @Mock
    private PrivateResource privateResource;

    @InjectMocks
    ApiResource apiResource;

    @Test
    public void getPublicResource()
    {
        PublicResource result = apiResource.getPublicResource();

        assertThat("getPublicResource() returned appropriate PublicResource", result, equalTo(publicResource));
    }

    @Test
    public void getPrivatedResource()
    {
        PrivateResource result = apiResource.getPrivateResource();

        assertThat("getPrivateResource() returned appropriate PrivateResource", result, equalTo(privateResource));
    }

}
