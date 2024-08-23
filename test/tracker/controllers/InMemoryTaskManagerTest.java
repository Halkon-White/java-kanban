package tracker.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Status.IN_PROGRESS;
import static tracker.model.Status.NEW;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
    }

    //объект Epic нельзя добавить в самого себя в виде подзадачи в любом случае, т.к. метод добавления подзадач принимает только объекты подзадач
    //объект Subtask нельзя сделать своим же эпиком в любом случае, т.к. метод добавления эпиков принимает только объекты эпики
    //написать такие тесты не представляется возможным

    @Test
    void addNewTask_ShouldSaveTask() {
        //prepare
        Task task = new Task("t1", "t1.1", 1, NEW);
        Task expectedTask = new Task("t1", "t1.1", 1, NEW);
        //do
        Task actualTask = taskManager.addNewTask(task);

        //check
        Assertions.assertNotNull(actualTask);
        Assertions.assertNotNull(actualTask.getId());
        Assertions.assertEquals(expectedTask, actualTask);
    }

    @Test
    void updateTask_ShouldUpdateTaskWithSpecifiedId() {
        //prepare
        Task task = new Task("t1", "t1.1", 1, NEW);
        Task savedTask = taskManager.addNewTask(task);
        Task updTask = new Task("t1 upd", "t1.1 upd", savedTask.getId(), IN_PROGRESS);
        Task expectedUpdTask = new Task("t1 upd", "t1.1 upd", savedTask.getId(), IN_PROGRESS);
        //do
        Task actualUpdTask = taskManager.updateTask(updTask);
        //check
        Assertions.assertEquals(expectedUpdTask, actualUpdTask);
    }


    @Test
    void getTaskList_ShouldReturnArrayList() {
        //prepare
        Task task1 = new Task("t1", "t1.1", 1, NEW);
        Task task2 = new Task("t2", "t2.1", 2, IN_PROGRESS);

        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task1);
        expectedTaskList.add(task2);
        //do
        Task savedTask1 = taskManager.addNewTask(task1);
        Task savedTask2 = taskManager.addNewTask(task2);
        ArrayList<Task> actualTaskList = taskManager.getTaskList();
        //check
        Assertions.assertEquals(expectedTaskList, actualTaskList);
    }

    @Test
    void addNewTask_DoesNotChangingAddedTask() {
        //prepare
        Task task = new Task("t1", "t1.1", NEW);
        Task expectedTask = new Task("t1", "t1.1", 1, NEW);
        //do
        Task actualTask = taskManager.addNewTask(task);

        //check
        Assertions.assertEquals(expectedTask.getName(), actualTask.getName());
        Assertions.assertEquals(expectedTask.getDescription(), actualTask.getDescription());
        Assertions.assertEquals(expectedTask.getId(), actualTask.getId());
        Assertions.assertEquals(expectedTask.getStatus(), actualTask.getStatus());
    }

    @Test
    void getById_ShouldGetById() {
        //prepare
        Task task = new Task("t1", "t1.1", NEW);
        Task expectedTask = new Task("t1", "t1.1", 1, NEW);
        Epic epic = new Epic("e1", "e1.1", 2);
        Epic expectedEpic = new Epic("e1", "e1.1", 2);
        Subtask subtask = new Subtask("s1", "s1.1", 3, NEW, epic.getId());
        Subtask expectedSubtask = new Subtask("s1", "s1.1", 3, NEW, epic.getId());
        //do
        Task savedTask = taskManager.addNewTask(task);
        Epic savedEpic = taskManager.addNewEpic(epic);
        Subtask savedSubtask = taskManager.addNewSubtask(subtask);
        Task getTask = taskManager.getTaskById(savedTask.getId());
        Epic getEpic = taskManager.getEpicById(savedEpic.getId());
        Subtask getSubtask = taskManager.getSubtaskById(savedSubtask.getId());
        //check
        Assertions.assertNotNull(getTask);
        Assertions.assertNotNull(getEpic);
        Assertions.assertNotNull(getSubtask);

        Assertions.assertEquals(expectedTask, getTask);
        Assertions.assertEquals(expectedEpic, getEpic);
        Assertions.assertEquals(expectedSubtask, getSubtask);

    }

    @Test
    void addNewEpic_ShouldSaveEpic() {
        //prepare
        Epic epic = new Epic("e1", "e1.1", 1);
        Epic expectedEpic = new Epic("e1", "e1.1", 1);

        //do
        Epic actualEpic = taskManager.addNewEpic(epic);
        //check
        Assertions.assertNotNull(actualEpic);
        Assertions.assertNotNull(actualEpic.getId());
        Assertions.assertEquals(expectedEpic, actualEpic);
    }

    @Test
    void addNewEpicAndSubtask_ShouldSaveEpicAndSubtask() {
        //prepare
        Epic epic = new Epic("e1", "e1.1", 1);
        Epic expectedEpic = new Epic("e1", "e1.1", 1);
        Subtask subtask = new Subtask("s1", "s1.1", 2, NEW, epic.getId());
        Subtask expectedSubtask = new Subtask("s1", "s1.1", 2, NEW, epic.getId());
        //do
        Epic actualEpic = taskManager.addNewEpic(epic);
        Subtask actualSubtask = taskManager.addNewSubtask(subtask);

        //check
        Assertions.assertNotNull(actualSubtask);
        Assertions.assertNotNull(actualSubtask.getId());
        Assertions.assertEquals(expectedSubtask, actualSubtask);
        Assertions.assertNotNull(actualEpic);
        Assertions.assertNotNull(actualEpic.getId());
        Assertions.assertEquals(expectedEpic, actualEpic);
    }
    @Test
    void Managers_ShouldSetTaskManagerAndHistoryManager() {
        //prepare
        InMemoryHistoryManager ExpectedHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager ExpectedTaskManager = new InMemoryTaskManager(ExpectedHistoryManager);


        //do
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        //check
        Assertions.assertNotNull(taskManager);
        Assertions.assertNotNull(historyManager);
        Assertions.assertEquals(ExpectedTaskManager, taskManager);
        Assertions.assertEquals(ExpectedHistoryManager, historyManager);

    }

    @Test
    void HistoryManager_ShouldAddTasksToHistoryAndDoNotTakeLaterUpdates() {
        //prepare
        HistoryManager historyManager = taskManager.getHistoryManager();
        Task task = new Task("t1", "t1.1", NEW);
        Task savedTask = taskManager.addNewTask(task);

        Task updTask = new Task("t1 upd", "t1.1 upd", savedTask.getId(), IN_PROGRESS);
        Task expectedUpdTask = new Task("t1 upd", "t1.1 upd", savedTask.getId(), IN_PROGRESS);
        //do
        Task gotTask = taskManager.getTaskById(savedTask.getId());
        List<Task> history = historyManager.getHistory();
        Task actualUpdTask = taskManager.updateTask(updTask);
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(gotTask);
        //check

        Assertions.assertNotNull(historyManager.getHistory());
        Assertions.assertEquals(expectedHistory, history);
        Assertions.assertNotEquals(actualUpdTask.getName(), history.getFirst().getName());

    }

    @Test
    void TaskWithSetIDAndGeneratedIdShouldNotConflict() {
        //prepare
        Task task1 = new Task("t1", "t1.1", NEW);
        Task task2 = new Task("t2", "t2.1", 1, NEW);
        Task expectedTask2 = new Task("t2", "t2.1", 1, NEW);
        //do
        Task savedTask1 = taskManager.addNewTask(task1);
        Task savedTask2 = taskManager.addNewTask(task2);
        //check
        Assertions.assertEquals(savedTask1.getId(), expectedTask2.getId());
        Assertions.assertNotEquals(expectedTask2, savedTask2);


    }


}

