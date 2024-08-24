package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private HistoryManager historyManager;
   public InMemoryTaskManager (HistoryManager historyManager) {
       this.historyManager = historyManager;
   }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void setHistoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return id == that.id && Objects.equals(taskMap, that.taskMap) && Objects.equals(epicMap, that.epicMap) && Objects.equals(subtaskMap, that.subtaskMap) && Objects.equals(historyManager, that.historyManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskMap, epicMap, subtaskMap, historyManager);
    }

    // методы для задач
   @Override
   public ArrayList<Task> getTaskList() {
       return new ArrayList<>(taskMap.values());
    }

    @Override
    public void removeAllTasks() {
        taskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            Task task = taskMap.get(id);
            historyManager.addTaskToHistory(task);
            return taskMap.get(id);
        } else {
            System.out.println("Задачи с таким id не существует.");
            return null;
        }
    }

    @Override
    public Task addNewTask(Task newTask) {
        id++;
        int newId = id;
        newTask.setId(newId);
        taskMap.put(newTask.getId(), newTask);
        System.out.println("Задача с id " + newTask.getId() + " добавлена в менеджер.");
        return newTask;
    }

    @Override
    public Task updateTask(Task updatedTask) {
        Integer taskId = updatedTask.getId();

        if (taskMap.containsKey(taskId)) {
            taskMap.put(taskId, updatedTask);
            return updatedTask;
        } else {
            System.out.println("Задачи с таким id не существует.");
            return null;
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            System.out.println("Задача с id " + id + " удалена из списка задач.");
        } else {
            System.out.println("Задачи с таким id не существует.");
        }
    }

    // методы для подзадач
    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtaskMap.values()) { // Необходимо очистить эпики от подзадач
            Epic epicOfSub = epicMap.get(subtask.getEpicId());
            ArrayList<Integer> subtasksOfEpic = epicOfSub.getSubsList();
            subtasksOfEpic.remove(subtask.getId());
            checkEpicStatus(subtask.getEpicId());
        }
        subtaskMap.clear();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            historyManager.addTaskToHistory(subtask);
            return subtaskMap.get(id);
        } else {
            System.out.println("Подзадачи с таким id не существует.");
            return null;
        }
    }

    @Override
    public Subtask addNewSubtask(Subtask newSubtask) {
        if (epicMap.containsKey(newSubtask.getEpicId())) {
            id++;
            int newId = id;
            newSubtask.setId(newId);
            Epic epicOfSub = epicMap.get(newSubtask.getEpicId());
            ArrayList<Integer> subtasksOfEpic = epicOfSub.getSubsList();
            subtasksOfEpic.add(newSubtask.getId()); // Заносим ID новой подзадачи в список подзадач эпика
            subtaskMap.put(newSubtask.getId(), newSubtask);
            checkEpicStatus(epicOfSub.getId());//при добавлении подзадачи необходимо проверить эпик на статус
            System.out.println("Подзадача с id " + newSubtask.getId() + " добавлена в менеджер.");
            return newSubtask;
        }
        else {
            System.out.println("Эпика, которому принадлежит подзадача, нет в менеджере.");
            return null;
        }
    }


    @Override
    public Subtask updateSubtask(Subtask updatedSubtask) {
        Integer taskId = updatedSubtask.getId();

        if (subtaskMap.containsKey(taskId)) {
            subtaskMap.put(taskId, updatedSubtask);
            checkEpicStatus(updatedSubtask.getEpicId());
            return updatedSubtask;
        } else {
            System.out.println("Подзадачи с таким id не существует.");
            return null;
        }

    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            Epic epic = epicMap.get(subtask.getEpicId()); //Убираем ID подзадачи из списка подзадач эпика
            ArrayList<Integer> subsList = epic.getSubsList();
            subsList.remove(subtask.getId());
            subtaskMap.remove(id);
            checkEpicStatus(subtask.getEpicId());
            System.out.println("Подзадача с id " + id + " удалена из списка подзадач.");
        } else {
            System.out.println("Подзадачи с таким id не существует.");
        }
    }

    // методы для эпиков
    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void removeAllEpics() {
        subtaskMap.clear(); // если удаляются все эпики, удаляются и все их подзадачи
        epicMap.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            historyManager.addTaskToHistory(epic);
            return epicMap.get(id);
        } else {
            System.out.println("Эпика с таким id не существует.");
            return null;
        }
    }

    @Override
    public Epic addNewEpic(Epic newEpic) {
        id++;
        int newId = id;
        newEpic.setId(newId);
        epicMap.put(newEpic.getId(), newEpic);
        System.out.println("Эпик с id " + newEpic.getId() + " добавлен в менеджер.");
        return newEpic;
    }

    @Override
    public Epic updateEpic(Epic updatedEpic) {
        Integer epicId = updatedEpic.getId();

        if (epicMap.containsKey(epicId)) {
            epicMap.put(epicId, updatedEpic);
            checkEpicStatus(epicId);
            return updatedEpic;
        } else {
            System.out.println("Эпика с таким id не существует.");
            return null;
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            ArrayList<Integer> subsList = epic.getSubsList();
            for (int subId : subsList) {
                subtaskMap.remove(subId);
            }
            epicMap.remove(id);

            System.out.println("Эпик с id " + id + " удален из списка эпиков.");
        } else {
            System.out.println("Эпика с таким id не существует.");
        }
    }

    @Override
    public ArrayList <Integer> getSubtasksOfEpic(int epicId) {
        if (epicMap.containsKey(epicId)) {
            Epic epic = epicMap.get(epicId);
            return epic.getSubsList();
        } else {
            System.out.println("Эпика с таким id не существует.");
            return null;
        }
    }


// приватные методы
    private void checkEpicStatus(int epicId) {
        Epic epic = epicMap.get(epicId);
        ArrayList<Integer> subsList = epic.getSubsList();
        if (subsList.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
            }
        int n = 0;
        int inProgress = 0;
        int done = 0;
        for (int subId : subsList) {
            Subtask subtask = subtaskMap.get(subId);
            Status status = subtask.getStatus();
            switch (status) {
                case NEW:
                    n++;
                    break;
                case IN_PROGRESS:
                    inProgress++;
                    break;
                case DONE:
                    done++;
                default:
                    System.out.println("Такой статус подзадачи не существует.");
            }
        }
        if (n == subsList.size()) {
            epic.setStatus(Status.NEW);
        }
        if (inProgress > 0) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        if (done == subsList.size()) {
            epic.setStatus(Status.DONE);
        }
    }


}


