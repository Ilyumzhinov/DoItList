package Functional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

public class TasksControl implements Serializable
{
    private final static List<Task> Tasks = new ArrayList<>();

    // Checks before adding a course
    // Returns the added course if successful
    public Task addTask(String name, String detail, Course course, Date deadline, Long timeEst, Boolean highlight)
    {
        // Try to set the values
        Task task = new Task();

        task.setName(name);
        task.setDetail(detail);
        task.setCourse(course);
        task.setDeadline(deadline);
        task.setTimeEst(timeEst);
        task.setTimeSpent(0L);
        task.setHighlight(false);

        // Try to add to the list
        if (!Tasks.add(task))
            return task;

        return null;
    }

    // Remove a course with a name
    // True if successful removal
    public boolean removeCourse(Task xTask)
    {
        Task task = null;

        // Check if a course with the name already exists
        for (Task iTask : Tasks)
        {
            if (xTask.equals(iTask))
                task = iTask;
        }

        // Try to remove
        if (task == null)
            return false;
        else
            return Tasks.remove(task);
    }

    // Get an array of all courses
    public Task[] getTasks()
    {
        return Tasks.toArray(new Task[0]);
    }

    public Task getTaskAt(Integer index) {return Tasks.get(index);}
}
