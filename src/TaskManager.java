import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int id = 0;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    // методы для задач
    ArrayList<Task> getTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskMap.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    void removeAllTasks() {
        taskMap.clear();
        System.out.println("Все задачи удалены.");
    }

    Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            return taskMap.get(id);
        } else {
            System.out.println("Задачи с таким id не существует.");
            return null;
        }
    }

    Task addNewTask(Task newTask) {
        id++;
        int newId = id;
        newTask.setId(newId);
        taskMap.put(newTask.getId(), newTask);
        System.out.println("Задача с id " + newTask.getId() + " добавлена в менеджер.");
        return newTask;
    }

    Task updateTask(Task updatedTask) {
        Integer taskId = updatedTask.getId();

        if (taskMap.containsKey(taskId)) {
            taskMap.put(taskId, updatedTask);
            return updatedTask;
        } else {
            System.out.println("Задачи с таким id не существует.");
            return null;
        }
    }

    void removeTaskById(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            System.out.println("Задача с id " + id + " удалена из списка задач.");
        } else {
            System.out.println("Задачи с таким id не существует.");
        }
    }

    // методы для подзадач
    ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> tasks = new ArrayList<>();
        for (Subtask task : subtaskMap.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    void removeAllSubtasks() {
        for (Subtask subtask : subtaskMap.values()) { // Необходимо очистить эпики от подзадач
            Epic epicOfSub = epicMap.get(subtask.getEpicId());
            ArrayList<Integer> subtasksOfEpic = epicOfSub.getSubsList();
            subtasksOfEpic.remove(subtask.getId());
            checkEpicStatus(subtask.getEpicId());
        }
        subtaskMap.clear();
        System.out.println("Все подзадачи удалены.");
    }

    Subtask getSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            return subtaskMap.get(id);
        } else {
            System.out.println("Подзадачи с таким id не существует.");
            return null;
        }
    }

    Subtask addNewSubtask(Subtask newSubtask) {
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


    Subtask updateSubtask(Subtask updatedSubtask) {
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

    void removeSubtaskById(int id) {
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
    ArrayList<Epic> getEpicList() {
        ArrayList<Epic> tasks = new ArrayList<>();
        for (Epic task : epicMap.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    void removeAllEpics() {
        subtaskMap.clear(); // если удаляются все эпики, удаляются и все их подзадачи
        epicMap.clear();
        System.out.println("Все эпики удалены.");
    }

    Epic getEpicById(int id) {
        if (epicMap.containsKey(id)) {
            return epicMap.get(id);
        } else {
            System.out.println("Эпика с таким id не существует.");
            return null;
        }
    }

    Epic addNewEpic(Epic newEpic) {
        id++;
        int newId = id;
        newEpic.setId(newId);
        epicMap.put(newEpic.getId(), newEpic);
        System.out.println("Эпик с id " + newEpic.getId() + " добавлен в менеджер.");
        return newEpic;
    }

    Epic updateEpic(Epic updatedEpic) {
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

    void removeEpicById(int id) {
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

    ArrayList <Integer> getSubtasksOfEpic(int epicId) {
        if (epicMap.containsKey(epicId)) {
            Epic epic = epicMap.get(epicId);
            return epic.getSubsList();
        } else {
            System.out.println("Эпика с таким id не существует.");
            return null;
        }
    }

    void checkEpicStatus(int epicId) {
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


