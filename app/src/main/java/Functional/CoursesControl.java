package Functional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoursesControl implements Serializable
{
    private List<Course> Courses = new ArrayList<>();

    // Check before adding a course
    // Return the added course if successful
    public Course addCourse(String xName, Integer xAssociatedColor)
    {
        Course course = addCourse(xName, xAssociatedColor, null);

        return course;
    }

    // Overload a method for adding a course to add parent as well
    // Return the added course if successful
    public Course addCourse(String xName, Integer xAssociatedColor, Course xParent)
    {
        Course course = new Course();

        if (xParent != null)
            course.setParent(xParent);

        // Try to set the name
        if (!setNameCheck(course, xName))
            return null;

        if (course.setAssociatedColor(xAssociatedColor) == null)
            return null;

        // Try to add to the list
        Courses.add(course);

        return course;
    }

    // Remove a course with a name
    // True if successful removal
    public boolean removeCourse(String xName)
    {
        Course course = null;

        // Check if a course with the name already exists
        for (Course iCourse : Courses)
        {
            if (xName.equals(iCourse.getName()))
                course = iCourse;
        }

        // Try to remove
        if (course == null)
            return false;
        else
            return Courses.remove(course);
    }

    // Get an array of all courses
    public Course[] getCourses()
    {
        return Courses.toArray(new Course[0]);
    }

    // True if successful set
    private boolean setNameCheck(Course xCourse, String xName)
    {
        Course[] localCourses = getCoursesAtScope(xCourse.getScopeArrayOf(true));

        if (localCourses == null)
        localCourses = new Course[0];

        // Check if a course with the name already exists
        for (Course iCourse : localCourses)
        {
            if (xName.equals(iCourse.getName()))
                return false;
        }

        // Check if name satisfies other Set requirements
        return xCourse.setName(xName);
    }

    public Course getCourseWithName(String name)
    {
        for (Course iCourse : Courses)
            if (iCourse.getName().equals(name))
                return iCourse;

        return null;
    }

    public Course[] getCoursesAtScope(List<String> scope)
    {
        List<Course> courses = new ArrayList<>();

        for (Course iCourse : Courses)
            if (iCourse.compareParentScopeWith(scope))
                courses.add(iCourse);

        if (courses.size() > 0)
            return courses.toArray(new Course[0]);
        else
            return null;
    }
}