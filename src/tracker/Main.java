package tracker;

import tracker.controllers.InMemoryHistoryManager;
import tracker.controllers.InMemoryTaskManager;

import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main {

    public static void main(String[] args) {



        TaskManager taskManager = Managers.getDefault();

        // Создайте две задачи
        taskManager.addNewTask(new Task("Помыть пол", "Задача 1", 123, Status.NEW));

        taskManager.addNewTask(new Task("Поесть", "Задача 2", 125, Status.NEW)); // Создайте эпик с двумя подзадачами

        taskManager.addNewEpic(new Epic("Прибраться", "Эпик 1", 1213));

        taskManager.addNewSubtask(new Subtask("Пропылесосить", "Подзадача 1", 4444, Status.NEW, 3));

        taskManager.addNewSubtask(new Subtask("Вытереть пыль", "Подзадача 2", 2322, Status.NEW, 3));
        // Создайте эпик с одной подзадачей
        taskManager.addNewEpic(new Epic("Отдохнуть", "Эпик 2", 11313));

        taskManager.addNewSubtask(new Subtask("Поспать", "Подзадача 1", 34, Status.NEW, 6));
        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..)
    System.out.println(taskManager.getTaskList());
    System.out.println(taskManager.getSubtaskList());
    System.out.println(taskManager.getEpicList()); // Измените статусы созданных объектов, распечатайте их.
    taskManager.updateSubtask(new Subtask("Отправить код на проверку", "Подзадача 2", 5, Status.IN_PROGRESS, 3));
    Epic epic = taskManager.getEpicById(3);
        System.out.println(epic.getStatus());

    taskManager.removeSubtaskById(5);
    System.out.println(epic.getStatus());
    taskManager.removeEpicById(3);
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
    taskManager.removeTaskById(2);
        System.out.println(taskManager.getTaskList());
    }

}
