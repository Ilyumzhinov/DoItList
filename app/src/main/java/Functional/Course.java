package Functional;

public class Course
{
    private String Name;

    // Name should be smaller than 50 characters long
    // Returns true if set was successful
    public boolean setName(String xName)
    {
        if (xName.trim().equals(""))
            return false;

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