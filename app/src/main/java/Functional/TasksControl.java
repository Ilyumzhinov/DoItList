package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TasksControl implements Serializable
{
    private final static List<Task> Tasks = new ArrayList<>();

    // Checks before adding a course
    // Returns the added course if successful
    public Task addTask(String name, String detail, Course course, LocalDateTime deadline, Long timeEst, Boolean highlight)
    {
        // Try to set the values
        Task task = new Task();

        task.setName(name);
        task.setDetail(detail);
        task.setCourse(course);
        task.setDeadline(deadline);
        task.setDateAdded(LocalDateTime.now());
        task.setTimeEst(timeEst);
        task.setTimeSpent(0L);
        task.setHighlight(highlight);

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

    public Task getTaskAt(Integer index) {return Tasks.get(index);}
}
