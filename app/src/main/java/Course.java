public class Course
{
    private int ID = 0;
    private String Name;

    // Allows to assign ID only once
    public boolean setID(int xID)
    {
        if (ID == 0)
            return false;

        ID = xID;
        return true;
    }

    public int getID()
    {
        return ID;
    }

    // Name should be smaller than 50 characters long
    public boolean setName(String xName)
    {
        if (xName.length() > 50)
            return false;

        Name = xName;

        return true;
    }

    public String getName()
    {
        return Name;
    }
}