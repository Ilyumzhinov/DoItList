package Functional;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable
{
    private String name;
    private Integer associatedColor;
    private Course parent;

    private static final String cBaseName = "Main";

    public Course()
    {
        // Default values
        associatedColor = Color.parseColor("#c5c6c7");
    }

    // Name should be smaller than 50 characters long
    // Returns true if set was successful
    public void setName(String xName) throws Exception
    {
        if (xName.trim().equals(""))
            throw new Exception("Enter name!");

        if (xName.length() > 50)
            throw new Exception("Name exceeds max length of 50");

        if (xName.equals(cBaseName) && getScopeStrOf(false).equals(cBaseName))
            throw new Exception("Name matches system name");

        this.name = xName;
    }

    // Check if we do not try to assign the course as its own parent
    public void setParent(Course xParent)
    {
        if (!this.getScopeStrOf(false).equals(xParent.getScopeStrOf(false))
                && !this.getName().equals(xParent.getName()))
            this.parent = xParent;
    }

    public void setAssociatedColor(Integer xColor) throws Exception
    {
        if (xColor == -1)
            throw new Exception("Invalid color");

        this.associatedColor = xColor;
    }

    public String getName()
    {
        if (this.isNull())
            return cBaseName;

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
    // CNIT 35500
    // Homework < CNIT 35500
    // Individual < Homework < CNIT 35500
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
                return list;

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
            return cBaseName;
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

    public Boolean compareScopeWith(List<String> xScope, Boolean ofParent)
    {
        List<String> scope = getScopeStrArrayOf(ofParent);

        if (xScope == null)
            return false;

        if (scope.isEmpty() && this.isNull())
            return false;
        else if (scope.isEmpty() && xScope.isEmpty())
            return true;
        else if (scope.size() != xScope.size())
            return false;

        int i = 0;
        while (i < scope.size())
        {
            if (!scope.get(i).equals(xScope.get(i)))
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