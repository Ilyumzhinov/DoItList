package Functional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoursesControl implements Serializable
{
    private List<Course> Courses = new ArrayList<>();

    // Check before adding a course
    // Return the added course if successful
    public Course addCourse(String xName, Integer xAssociatedColor) throws Exception
    {
        return addCourse(xName, xAssociatedColor, null);
    }

    // Overload a method for adding a course to add parent as well
    // Return the added course if successful
    public Course addCourse(String xName, Integer xAssociatedColor, Course xParent) throws Exception
    {
        Course course = new Course();

        if (xParent != null)
            course.setParent(xParent);

        // Try to set the name
        setNameCheck(course, xName);

        course.setAssociatedColor(xAssociatedColor);

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

    private Course emptyCourse;

    public void addEmptyCourse()
    {
        if (emptyCourse == null)
        {
            emptyCourse = new Course();
            Courses.add(0, emptyCourse);
        }
    }

    public Course getEmptyCourse()
    {
        return this.emptyCourse;
    }

    public void removeEmptyCourse()
    {
        Courses.remove(emptyCourse);

        emptyCourse = null;
    }

    // Get an array of all courses
    public Course[] getCourses()
    {
        return Courses.toArray(new Course[0]);
    }

    // Get index of course
    // Match is based on scope match
    public Integer getIndexOfCourse(Course xCourse)
    {
        int i = -1;

        if (xCourse == null)
            return i;

        for (Course iCourse : Courses)
        {
            i++;
            if (iCourse.getScopeStrOf(false).equals(xCourse.getScopeStrOf(false)))
                return i;
        }

        return -1;
    }

    // True if successful set
    private void setNameCheck(Course xCourse, String xName) throws Exception
    {
        List<Course> localCourses = getCoursesAtScope(xCourse.getScopeStrArrayOf(true));

        if (localCourses == null)
            localCourses = new ArrayList<>();

        // Check if a course with the name already exists
        for (Course iCourse : localCourses)
        {
            if (xName.equals(iCourse.getName()))
                throw new Exception("Name already exists");
        }

        // Check if name satisfies other Set requirements
        xCourse.setName(xName);
    }

    // Return an array of all courses that are located at the same scope
    public List<Course> getCoursesAtScope(List<String> scope)
    {
        List<Course> courses = new ArrayList<>();

        for (Course iCourse : Courses)
            if (iCourse.compareParentScopeWith(scope))
                courses.add(iCourse);

        if (!courses.isEmpty())
            return courses;
        else
            return null;
    }
}