package be.com.bean;

import javax.ejb.Stateless;
import javax.xml.bind.annotation.XmlRootElement;

@Stateless
@XmlRootElement
public class RobotBean
{
    private String name;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    private String id;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
