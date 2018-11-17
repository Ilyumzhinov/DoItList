package Functional;

import java.io.Serializable;

public class Course implements Serializable
{
    private String name;
    private Integer associatedColor;
    private Course parent;

    // Name should be smaller than 50 characters long
    // Returns true if set was successful
    public boolean setName(String xName)
    {
        if (xName.trim().equals(""))
            return false;

        if (xName.length() > 50)
            return false;

        this.name = xName;

        return true;
    }

    // Check if we do not try to assign the course as its own parent
    public void setParent(Course xParent)
    {
        if (!this.getFullScope().equals(xParent.getFullScope())
                && !this.getName().equals(xParent.getName()))
            this.parent = xParent;
    }

    public void setAssociatedColor(Integer xColor)
    {
        this.associatedColor = xColor;
    }

    public String getName()
    {
        if (this.name == null)
            return "";

        return this.name;
    }

    public Integer getAssociatedColor()
    {
        return associatedColor;
    }

    public Course getParent()
    {
        return this.parent;
    }

    public String getFullScope()
    {
        return getFullScopeOf(this);
    }

    // A recursive function that browses through the hierarchy of parents and fetches their names into a single string
    // Example:
    // CNIT35500
    // Homework < CNIT35500
    // Individual < Homework < CNIT35500
    private String getFullScopeOf(Course xCourse)
    {
        if (xCourse.getParent() == null)
            return xCourse.getName();

        return xCourse.getName() + " < " + getFullScopeOf(xCourse.getParent());
    }

    public String getScopeOfParent()
    {
        if (this.getParent() == null)
            return "";

        return getFullScopeOf(this.getParent());
    }

    public Boolean compareParentScopeWith(String xScope)
    {
        String parentScope = getScopeOfParent();

        if (parentScope == xScope)
            return true;
        else if (parentScope == null)
            return false;
        else if (xScope == null)
            return false;

        return parentScope.equals(xScope);
    }

    public Boolean isNull()
    {
        return name == null;
    }

    @Override
    public String toString()
    {
        return getFullScope();
    }
}