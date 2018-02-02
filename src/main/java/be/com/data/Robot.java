package be.com.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ROBOT", schema = "PUBLIC")
@NamedQueries({
        @NamedQuery(
                name = "Robot.findById",
                query = "select o from Robot o where o.id=:id"
        )
})
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
}
