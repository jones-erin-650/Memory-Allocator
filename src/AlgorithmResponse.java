import java.util.LinkedList;
import java.util.List;

// This is necessary to keep the Processes read only. This way we can just add allocated processes to a response rather than edit the original Process

public class AlgorithmResponse {
    LinkedList<MemorySlot> outputMemory;
    LinkedList<Process> outputProcesses;
    List<Integer> allocatedProcesses;
    List<Integer> unallocatedProcesses;

    public AlgorithmResponse(LinkedList<MemorySlot> outputMemory, List<Integer> allocatedProcesses, List<Integer> unallocatedProcesses) {
        this.outputMemory = outputMemory;
        this.allocatedProcesses = allocatedProcesses;
        this.unallocatedProcesses = unallocatedProcesses;
    }

    public LinkedList<MemorySlot> getOutputMemory() {
        return outputMemory;
    }

    public List<Integer> getAllocatedProcesses() {
        return allocatedProcesses;
    }

    public List<Integer> getUnallocatedProcesses() {
        return unallocatedProcesses;
    }
}