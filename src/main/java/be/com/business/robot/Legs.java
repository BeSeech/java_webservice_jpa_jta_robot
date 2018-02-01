package be.com.business.robot;

import java.util.Vector;

class Legs
{
    private int getLegIndexById(int legId)
    {
        for (int i = 0; i < legs.size(); i++)
        {
            if (legs.get(i).getId() == legId) {
                return i;
            }
        }
        return -1;
    }

    public int[] getSequence()
    {
        int[] result = new int[legs.size()];
        for (int i = 0; i < legs.size(); i++)
        {
            result[i] = legs.get(i).getId();
        }
        return result;
    }

    private int getDistance(Leg leg1, Leg  leg2)
    {
        if  ( (leg1.getPosition() == Position.Normal) && ((leg2.getPosition() == Position.Backward)))
            return 1;
        if  ( (leg1.getPosition() == Position.Backward) && ((leg2.getPosition() == Position.Normal)))
            return 1;
        if  ( (leg1.getPosition() == Position.Normal) && ((leg2.getPosition() == Position.Forward)))
            return 1;
        if  ( (leg1.getPosition() == Position.Forward) && ((leg2.getPosition() == Position.Normal)))
            return 1;
        if  ( (leg1.getPosition() == Position.Normal) && ((leg2.getPosition() == Position.Normal)))
            return 0;
        if  ( (leg1.getPosition() == Position.Backward) && ((leg2.getPosition() == Position.Backward)))
            return 0;
        if  ( (leg1.getPosition() == Position.Forward) && ((leg2.getPosition() == Position.Forward)))
            return 0;
        return 2;
    }

    public Boolean isOk()
    {
        Leg current = null;
        Leg next = null;
        Leg prior = null;

        for (int i = 0; i < legs.size(); i++) {
            current = legs.get(i);
            next = (( i+1 ) < legs.size()) ? legs.get(i + 1) : null;
            if ( (next != null) && (getDistance(current, next) > 1))
            {
                return false;
            }

            if ((prior != null) && (next != null))
            {
                if ( (prior.getPosition() != current.getPosition()) && (current.getPosition() != next.getPosition()) )
                {
                    return false;
                }
            }
            prior = current;
        }
        return true;
    }

    public void reset()
    {
        for (int i = 0; i < legs.size(); i++)
        {
            legs.get(i).setPosition(Position.Normal);
        }
    }

    public int getLength()
    {
        return legs.size();
    }


    public boolean move(int legId, boolean isForward)
    {
        if (!isOk())
            return false;

        int index = getLegIndexById(legId);
        Leg leg = legs.get(index);

        switch (leg.position)
        {
            case Normal:
                leg.setPosition(isForward ? Position.Forward : Position.Backward);
                break;
            case Forward:
                leg.setPosition(isForward ? Position.Forward : Position.Normal);
                break;
            case Backward:
                leg.setPosition(isForward ? Position.Normal : Position.Backward);
                break;
        }

        checkForFullStep();
        return isOk();
    }

    private void checkForFullStep()
    {
        boolean samePosition = true;
        for (int i = 1; i < legs.size(); i++)
        {
            if (legs.get(i-1).position != legs.get(i).position)
            {
                samePosition = false;
                break;
            }
        }
        if (samePosition)
            reset();
    }

    enum Position
    {
        Forward, Backward, Normal
    }

    class Leg
    {
        private int id;
        private Position position;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public Position getPosition()
        {
            return position;
        }

        public void setPosition(Position position)
        {
            this.position = position;
        }

        public Leg(int id)
        {
            setId(id);
            setPosition(Position.Normal);
        }
    }

    private Vector<Leg> legs;

    public Legs(int[] legSequence)
    {
        legs = new Vector<>();
        for (int i = 0;
             i < legSequence.length;
             i++) {
            legs.add(new Leg(legSequence[i]));
        }
    }
}
