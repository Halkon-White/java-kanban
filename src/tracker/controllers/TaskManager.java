package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    HistoryManager getHistoryManager();


    // методы для задач
    ArrayList<Task> getTaskList();

    void removeAllTasks();

    Task getTaskById(int id);

    Task addNewTask(Task newTask);

    Task updateTask(Task updatedTask);

    void removeTaskById(int id);

    // методы для подзадач
    ArrayList<Subtask> getSubtaskList();

    void removeAllSubtasks();

    Subtask getSubtaskById(int id);

    Subtask addNewSubtask(Subtask newSubtask);

    Subtask updateSubtask(Subtask updatedSubtask);

    void removeSubtaskById(int id);

    // методы для эпиков
    ArrayList<Epic> getEpicList();

    void removeAllEpics();

    Epic getEpicById(int id);

    Epic addNewEpic(Epic newEpic);

    Epic updateEpic(Epic updatedEpic);

    void removeEpicById(int id);

    ArrayList<Integer> getSubtasksOfEpic(int epicId);


}
