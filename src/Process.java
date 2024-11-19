public class Process {
    int id;
    int size;
    boolean allocated;

    Process(int id, int size) {
        this.id = id;
        this.size = size;
        this.allocated = false;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", size=" + size +
                ", allocated=" + allocated +
                '}';
    }
}