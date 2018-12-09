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

    // Remove a course with a scope
    // True if successful removal
    public void removeCourse(String xName, List<String> xParentScope)
    {
        Course courses = null;

        // Check if a course with the scope and name exists
        for (Course iCourse : Courses)
        {
            if (iCourse.compareParentScopeWith(xParentScope))
                if (iCourse.getName().equals(xName))
                    courses = iCourse;
        }

        // Try to remove
        if (courses != null)
            Courses.remove(courses);
    }

    // Todo: remove test
    public void removeCourses()
    {
        Courses.removeAll(Courses);
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

    public List<String> getEmptyCourseScope()
    {
        return new ArrayList<>();
    }

    // Get an array of all courses
    public List<Course> getCourses()
    {
        return Courses;
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