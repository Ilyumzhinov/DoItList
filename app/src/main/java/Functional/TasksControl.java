package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TasksControl implements Serializable
{
    private List<Task> Tasks = new ArrayList<>();

    // Returns the added task if successful
    public Task addTask(String name, String detail, Course course, LocalDateTime deadline, Long timeEst)
    {
        // Try to set the values
        Task task = new Task();

        task.setName(name);
        task.setDetail(detail);
        task.setCourse(course);
        task.setDeadline(deadline);
        task.setDateAdded(LocalDateTime.now());
        task.setTimeGoal(timeEst);
        task.setStatusFinished(false);

        // Try to add to the list
        Tasks.add(task);

        return task;
    }

    public Task updateTask(Task xTask, String name, String detail, Course course, LocalDateTime deadline, Long timeEst)
    {
        // Try to set the values
        xTask.setName(name);
        xTask.setDetail(detail);
        xTask.setCourse(course);
        xTask.setDeadline(deadline);
        xTask.setTimeGoal(timeEst);

        return xTask;
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

    public List<Task> getTasksForDate(LocalDateTime xDate)
    {
        List<Task> tasks = new ArrayList<>();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (Task iTask : Tasks)
            if (fmt.format(iTask.getDeadline()).equals(fmt.format(xDate)))
                tasks.add(iTask);

        return tasks;
    }

    public Long getTotalTimeRemainEst(List<Task> xTassk)
    {
        Long totalMinutes = 0L;

        for (Task iTask : xTassk)
        {
            totalMinutes += iTask.getTimeRemainEst();
        }

        return totalMinutes;
    }

    public Task getTaskAt(Integer index) {return Tasks.get(index);}

    public int getTaskIndexFor(Task xTask)
    {
        return Tasks.indexOf(xTask);
    }
}
