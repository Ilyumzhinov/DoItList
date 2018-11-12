package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TasksControl implements Serializable
{
    private final static List<Task> Tasks = new ArrayList<>();

    // Returns the added task if successful
    public Task addTask(String name, String detail, Course course, LocalDateTime deadline, Integer timeEst, Boolean highlight)
    {
        // Try to set the values
        Task task = new Task();

        task.setName(name);
        task.setDetail(detail);
        task.setCourse(course);
        task.setDeadline(deadline);
        task.setDateAdded(LocalDateTime.now());
        task.setTimeEst(timeEst);
        task.setHighlight(highlight);
        task.setStatusFinished(false);

        // Try to add to the list
        Tasks.add(task);

        return task;
    }

    // Remove a task with a name
    // True if successful removal
    public boolean removeTask(Task xTask)
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

    // Get an array of all tasks
    public Task[] getTasks()
    {
        return Tasks.toArray(new Task[0]);
    }

    public Task getTaskAt(Integer index) {return Tasks.get(index);}
}
