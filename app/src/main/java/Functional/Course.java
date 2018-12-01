package Functional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        if (!this.getScopeStrOf(false).equals(xParent.getScopeStrOf(false))
                && !this.getName().equals(xParent.getName()))
            this.parent = xParent;
    }

    public Integer setAssociatedColor(Integer xColor)
    {
        if (xColor == -1)
            return null;

        return this.associatedColor = xColor;
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

    // Get arrays
    public List<String> getScopeStrArrayOf(Boolean ofParent)
    {
        List<String> scope;

        if (ofParent)
            if (this.getParent() == null)
                scope = new ArrayList<>();
            else
                scope = getFullScopeStrArray(this.getParent());
        else
            scope = getFullScopeStrArray(this);

        return scope;
    }

    // A recursive function that surfaces through the hierarchy of parents and fetches their names into an array of strings
    // Example:
    // CNIT35500
    // Homework < CNIT35500
    // Individual < Homework < CNIT35500
    private List<String> getFullScopeStrArray(Course xCourse)
    {
        final List<String> list = new ArrayList<>();

        if (xCourse.getParent() == null)
        {
            if (xCourse.isNull())
                return new ArrayList<>();

            list.add(xCourse.getName());
            return list;
        }

        list.add(xCourse.getName());

        list.addAll(getFullScopeStrArray(xCourse.getParent()));

        return list;
    }

    public List<Course> getScopeArrayOf(Boolean ofParent)
    {
        List<Course> scope;

        if (ofParent)
            if (this.getParent() == null)
                scope = new ArrayList<>();
            else
                scope = getFullScopeArray(this.getParent());
        else
            scope = getFullScopeArray(this);

        return scope;
    }

    private List<Course> getFullScopeArray(Course xCourse)
    {
        final List<Course> list = new ArrayList<>();

        if (xCourse.getParent() == null)
        {
            if (xCourse.isNull())
                return new ArrayList<>();

            list.add(xCourse);
            return list;
        }

        list.add(xCourse);

        list.addAll(getFullScopeArray(xCourse.getParent()));

        return list;
    }
    //

    // Get fetched strings
    public String getScopeStrOf(Boolean ofParent)
    {
        String scope;

        if (ofParent)
            scope = getScopeStrOfParent();
        else

            scope = getFullScopeStrOf(this);


        return scope;
    }

    private String getFullScopeStrOf(Course xCourse)
    {
        final String dividerStr = " < ";
        final StringBuilder fullScope = new StringBuilder();
        String finalStr;

        for (String iName : getFullScopeStrArray(xCourse))
        {
            fullScope.append(iName);
            fullScope.append(dividerStr);
        }

        try
        {
            finalStr = fullScope.substring(0, fullScope.length() - dividerStr.length());
        } catch (Exception c)
        {
            return "";
        }

        return finalStr;
    }
    //

    // Get parent
    private String getScopeStrOfParent()
    {
        if (this.getParent() == null)
            return "";

        return getFullScopeStrOf(this.getParent());
    }

    public Boolean compareParentScopeWith(List<String> xScope)
    {
        List<String> parentScope = getScopeStrArrayOf(true);

        if (parentScope == null && xScope == null)
            return true;
        else if (parentScope == null || xScope == null)
            return false;
        else if (parentScope.isEmpty() && xScope.isEmpty())
            return true;
        else if (parentScope.size() != xScope.size())
            return false;

        int i = 0;
        while (i < parentScope.size())
        {
            if (!parentScope.get(i).equals(xScope.get(i)))
                return false;

            i++;
        }

        return true;
    }
    //

    public Boolean isNull()
    {
        return name == null;
    }

    @Override
    public String toString()
    {
        return getScopeStrOf(false);
    }
}