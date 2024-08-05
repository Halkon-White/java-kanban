package tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subsList = new ArrayList<>();
    // эпик хранит список из ID его подзадач, при создании подзадачи ее ID заносится в список


    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);

    }

    public ArrayList<Integer> getSubsList() {
        return subsList;
    }

    public void setSubsList(ArrayList<Integer> subsList) {
        this.subsList = subsList;
    }

    @Override
    public String toString() {
        return "tracker.model.Epic{" + super.toString() +
                "subsList=" + subsList +
                '}';
    }
}


