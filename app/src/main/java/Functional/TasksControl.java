package Functional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksControl implements Serializable
{
    private final static List<Task> Tasks = new ArrayList<>();

    // Checks before adding a course
    // Returns the added course if successful
    public Task addTask(String name, Course course, SimpleDateFormat start, SimpleDateFormat deadline, String detail)
    {
        // Try to set the values
        Task task = new Task(name, course, start, deadline, detail);

        // Try to add to the list
        Tasks.add(task);

        return task;
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
}
