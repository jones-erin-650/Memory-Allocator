import java.awt.image.AreaAveragingScaleFilter;
import java.util.LinkedList;
import java.util.List;

// This is necessary to keep the Processes read only. This way we can just add allocated processes to a response rather than edit the original Process

public class AlgorithmResponse {
    LinkedList<MemorySlot> outputMemory = new LinkedList<>();
    LinkedList<Process> outputProcesses = new LinkedList<>();

    public AlgorithmResponse(LinkedList<MemorySlot> outputMemory, LinkedList<Process> outputProcesses) {
        this.outputMemory = outputMemory;
        this.outputProcesses = outputProcesses;
    }

    public AlgorithmResponse() {

    }

    public LinkedList<MemorySlot> getOutputMemory() {
        return outputMemory;
    }

    public LinkedList<Process> getOutputProcesses() {
        return outputProcesses;
    }

    @Override
    public String toString() {
        return "AlgorithmResponse{" +
                "outputMemory=" + outputMemory +
                ", outputProcesses=" + outputProcesses +
                '}';
    }
}