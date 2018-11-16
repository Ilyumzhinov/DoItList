package Functional;

import android.graphics.Color;

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
    public void setparent(Course xParente)
    {
        if (!this.getFullScope(this).equals(xParente.getFullScope(xParente))
                && !this.getName().equals(xParente.getName()))

        this.parent = xParente;
    }

    public void setAssociatedColor(Integer xColor)
    {
        this.associatedColor = xColor;
    }

    public String getName()
    {
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

    // A recursive function that browses through the hierarchy of parents and fetches their names into a single string
    public String getFullScope(Course xCourse)
    {
        // Example:
        // Individual / Homework / CNIT35500
        // Homework / CNIT35500
        // CNIT35500

        if (xCourse.getParent() == null)
            return xCourse.getName();

        return xCourse.getName() + " / " + getFullScope(xCourse.getParent());
    }

    @Override
    public String toString()
    {
        return getFullScope(this);
    }
}