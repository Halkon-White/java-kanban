public class Subtask extends Task {
    private int epicId; // подзадача создается под эпик, ей должен быть известен ID эпика

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + super.toString() +
                "epicId=" + epicId +
                '}';
    }
}

