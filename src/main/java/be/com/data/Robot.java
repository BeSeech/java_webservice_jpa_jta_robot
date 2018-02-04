package be.com.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "ROBOT", schema = "PUBLIC")
@NamedQueries({
        @NamedQuery(
                name = "Robot.findById",
                query = "select o from Robot o where o.id=:id"
        )
})
@XmlRootElement
public class Robot implements Serializable
{
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEG_SEQUENCE")
    private String legSequence;

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

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLegSequence()
    {
        return legSequence;
    }

    public void setLegSequence(String legSequence)
    {
        this.legSequence = legSequence;
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
        if (!(other instanceof Robot))
            return false;
        Robot otherRobot = (Robot)other;
        return this.toString().equals(otherRobot.toString());
    }
}
