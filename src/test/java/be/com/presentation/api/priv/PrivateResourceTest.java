package be.com.presentation.api.priv;

import be.com.presentation.api.priv.crud.CRUDResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PrivateResourceTest
{
    @Mock
    private CRUDResource crudResource;

    @InjectMocks
    private PrivateResource privateResource;

    @Test
    public void getCRUDResource()
    {
        CRUDResource result = privateResource.getCRUDResource();

        assertThat("getCRUDResource() returned appropriate CRUDResource", result, equalTo(crudResource));
    }
}
