public class MemorySlot {
    int start;
    int end;
    int size;
    // This way we can just see if there's a process in the MemorySlot using the class itself
    int processID;

    MemorySlot(int start, int end) {
        this.start = start;
        this.end = end;
        this.size = end - start;
        // Starts off as 0 to show that there's no process in it
        this.processID = 0;
    }

    MemorySlot(int start, int end, int processID) {
        this.start = start;
        this.end = end;
        this.size = end - start;
        this.processID = processID;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getSize() {
        return size;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    @Override
    public String toString() {
        return "MemorySlot{" +
                "start=" + start +
                ", end=" + end +
                ", size=" + size +
                ", processID=" + processID +
                '}';
    }
}