package tracker.controllers;

import tracker.model.Status;
import tracker.model.Task;
import tracker.model.Subtask;
import tracker.model.Epic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(history, that.history);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(history);
    }

    @Override
    public void addTaskToHistory(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);

    }
}
