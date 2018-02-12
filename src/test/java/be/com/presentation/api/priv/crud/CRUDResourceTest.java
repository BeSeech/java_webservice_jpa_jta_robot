package be.com.presentation.api.priv.crud.robot;

import be.com.presentation.api.priv.crud.CRUDResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CRUDResourceTest
{
    @Mock
    private CRUDRobotResource crudRobotResource;

    @InjectMocks
    private CRUDResource crudResource;

    @Test
    public void getRobotResource()
    {
        CRUDRobotResource result = crudResource.getCRUDRobotResource();

        assertThat("getCRUDRobotResource() returned appropriate CRUDRobotResource", result, equalTo(crudRobotResource));
    }

}
