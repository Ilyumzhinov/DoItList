package cnit35500_group_whccai.doitlist;

import Functional.CoursesControl;
import Functional.TasksControl;

// Show that a fragment has a notifiable element like a RecyclerView that needs to be refreshed
public interface NotifiableFragment
{
    void notifyFragment(TasksControl xTasks, CoursesControl xCourses);
}
