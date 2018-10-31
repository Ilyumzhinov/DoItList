import java.util.List;

public class CoursesControl
{
    private List<Course> Courses;

    public boolean AddCourse(String xName)
    {
        Course course = new Course();

        // Try to set the name
        if (!SetNameCheck(course, xName))
            return false;

        // Try to add to the list
        return Courses.add(course);
    }

    public boolean RemoveCourse(String xName)
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

    // True if successful set
    private boolean SetNameCheck(Course xCourse, String xName)
    {
        // Check if a course with the name already exists
        for (Course iCourse : Courses)
        {
            if (xName.equals(iCourse.getName()))
               return false;
        }

        // Check if name satisfies other Set requirements
        return xCourse.setName(xName);
    }
}
