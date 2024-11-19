public class Process {
    int id;
    int size;
    boolean allocated;

    Process(int id, int size) {
        this.id = id;
        this.size = size;
        this.allocated = false;
    }

    Process(int id, int size, boolean allocated) {
        this.id = id;
        this.size = size;
        this.allocated = allocated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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