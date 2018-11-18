package cnit35500_group_whccai.doitlist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Functional.CoursesControl;
import Functional.TasksControl;

public class SaveTasks extends AppCompatActivity {

    private File path;
    private File tasksFile;
    private File coursesFile;

    // Constructor
    public void createSaveTasks(Context context) {
        path = context.getFilesDir();
        tasksFile = new File(path, "tasksLocal");
        coursesFile = new File(path, "coursesLocal");
    }

    // Attempts to save CoursesControl to coursesLocal file
    public void saveFile(CoursesControl mCourses) {
        try {
            FileOutputStream stream = new FileOutputStream(coursesFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(stream);
            objectOut.writeObject(mCourses);
            objectOut.close();
            stream.close();
        } catch (Exception e) {
            System.out.println("Error saving courses object");
            e.printStackTrace();
            return;
        }
        System.out.println("Courses object saved successfully");
    }

    // Attempts to save TasksControl to tasksLocal file
    public void saveFile(TasksControl mTasks) {
        try {
            FileOutputStream stream = new FileOutputStream(tasksFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(stream);
            objectOut.writeObject(mTasks);
            objectOut.close();
            stream.close();
        } catch (Exception e) {
            System.out.println("Error saving tasks object");
            e.printStackTrace();
            return;
        }
        System.out.println("Tasks object saved successfully");
    }

    // Attempts to open coursesLocal and returns the associated object
    public CoursesControl openCoursesFile() {
        try {
            FileInputStream stream = new FileInputStream(coursesFile);
            ObjectInputStream objectIn = new ObjectInputStream(stream);
            CoursesControl result = (CoursesControl) objectIn.readObject();
            objectIn.close();
            System.out.println("Courses loaded successfully");
            return result;
        } catch (Exception e) {
            System.out.println("Error reading file");
            e.printStackTrace();
            return null;
        }
    }

    // Attempts to open tasksLocal and returns the associated object
    public TasksControl openTasksFile() {
        try {
                FileInputStream stream = new FileInputStream(tasksFile);
                ObjectInputStream objectIn = new ObjectInputStream(stream);
                TasksControl result = (TasksControl) objectIn.readObject();
                objectIn.close();
                System.out.println("Tasks loaded sucessfully");
                return result;
        } catch (Exception e) {
                System.out.println("Error reading file");
                e.printStackTrace();
                return null;
        }
    }

    public String getCoursesFileName() {
        return coursesFile.getName();
    }

    public String getCoursesFilePath() {
        return coursesFile.getPath();
    }

    public String getTasksFileName() {
        return tasksFile.getName();
    }

    public String getTasksFilePath() {
        return tasksFile.getPath();
    }
}
