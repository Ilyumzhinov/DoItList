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

    // Get arrays
    public List<String> getScopeArrayOf(Boolean ofParent)
    {
        List<String> scope;

        if (ofParent)
            scope = getScopeArrayOfParent();
        else
            scope = getFullScopeArray(this);

        return scope;
    }

    // A recursive function that surfaces through the hierarchy of parents and fetches their names into an array of strings
    // Example:
    // CNIT35500
    // Homework < CNIT35500
    // Individual < Homework < CNIT35500
    private List<String> getFullScopeArray(Course xCourse)
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
//        Quicker way
//        if (xCourse.getParent() == null)
//            return xCourse.getName();
//
//        return xCourse.getName() + " < " + getFullScopeOf(xCourse.getParent());

        final String dividerStr = " < ";
        final StringBuilder fullScope = new StringBuilder();
        String finalStr;

        for (String iName : getFullScopeArray(xCourse))
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
    private List<String> getScopeArrayOfParent()
    {
        if (this.getParent() == null)
            return new ArrayList<>();

        return getFullScopeArray(this.getParent());
    }

    private String getScopeStrOfParent()
    {
        if (this.getParent() == null)
            return "";

        return getFullScopeStrOf(this.getParent());
    }

    public Boolean compareParentScopeWith(List<String> xScope)
    {
        List<String> parentScope = getScopeArrayOfParent();

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

    // Get Colors
    public List<Integer> getColorAsArray(Boolean ofParent)
    {
        List<Integer> scope;

        if (ofParent)
            scope = getColorArrayOfParent();
        else
            scope = getAllColorsArrayOf(this);

        return scope;
    }

    private List<Integer> getAllColorsArrayOf(Course xCourse)
    {
        final List<Integer> list = new ArrayList<>();

        if (xCourse.getParent() == null)
        {
            list.add(xCourse.getAssociatedColor());
            return list;
        }

        list.add(xCourse.getAssociatedColor());

        list.addAll(getAllColorsArrayOf(xCourse.getParent()));

        return list;
    }

    private List<Integer> getColorArrayOfParent()
    {
        if (this.getParent() == null)
            return null;

        return getAllColorsArrayOf(this.getParent());
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