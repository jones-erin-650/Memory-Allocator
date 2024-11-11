import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Mallocator {

    public static void main(String[] args) throws Exception {
        String memoryInputFile = "Minput.data";
        String processInputFile = "Pinput.data";


//      Contains the memory data from Minput.data
//      memorySlots[0]: addresses of start of a free memory slot
//      memorySlots[1]: addresses of end of a free memory slot
//      memorySlots[2]: 0 for unallocated 1 for allocated
        LinkedList<int[]> memorySlots = readProcessInput(memoryInputFile);

//      Contains the process data from Pinput.data
//      processBlock[0]: process ID
//      processBlock[1]: size of process
//      processBlock[2]: 0 for unallocated 1 for allocated
        LinkedList<int[]> processList = readProcessInput(processInputFile);

        System.out.println("processList: \n" + processList);

//        writeToOutput(Algorithms.FF, processList);

        List<String[]> hardcodedOutput = new ArrayList<>();
        hardcodedOutput.add(new String[]{"100", "310", "2"});
        hardcodedOutput.add(new String[]{"600", "790", "1"});
        hardcodedOutput.add(new String[]{"1500", "1705", "3"});


    }

    public static class MemorySlot {
        int start;
        int end;
        int size;

        MemorySlot(int start, int end) {
            this.start = start;
            this.end = end;
            this.size = end - start;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "MemorySlot{" +
                    "start=" + start +
                    ", end=" + end +
                    ", size=" + size +
                    '}';
        }
    }

    // Structure to represent a process (ID, size)
    public static class Process {
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

    public enum Algorithms {
        FF,
        WF,
        BF
    }

    public static LinkedList<int[]> firstFit(LinkedList<int[]> processList, LinkedList<int[]> memorySlots) {
        return new LinkedList<>();
    }

    public static LinkedList<int[]> worstFit(LinkedList<int[]> processList, LinkedList<int[]> memorySlots) {
        return new LinkedList<>();
    }

    public static LinkedList<int[]> bestFit(LinkedList<int[]> processList, LinkedList<int[]> memorySlots) {
        return new LinkedList<>();
    }

    public static LinkedList<MemorySlot> readMemoryInput(String memoryInput) {
        InputStream inputStream = Mallocator.class.getResourceAsStream(memoryInput);
        Scanner scanner = new Scanner(inputStream);

//        First line is always the number of memoryblocks
        int numberOfMemorySlots = Integer.parseInt(scanner.nextLine());

        LinkedList<MemorySlot> memorySlots = new LinkedList<>();

//        Fill the memory list with individual memorySlots
        for (int i = 0; i < numberOfMemorySlots; i++) {
//              Split the line by the spaces
            String line = scanner.nextLine();
//            Doing it with another scanner because doing line.split would require changing the variable's type a lot and that's not ideal
//            TODO: having another scanner isn't ideal, causes more overhead
            Scanner lineParser = new Scanner(line);

            int startAddress = lineParser.nextInt();
            int endAddress = lineParser.nextInt();

//            Starts off unallocated
            MemorySlot memorySlot = new MemorySlot(startAddress, endAddress);
            System.out.println(memorySlot);

            memorySlots.add(memorySlot);

            lineParser.close();
        }
        scanner.close();
        return memorySlots;


    }

    public static LinkedList<Process> readProcessInput(String processInput) {
        InputStream inputStream = Mallocator.class.getResourceAsStream(processInput);
        Scanner scanner = new Scanner(inputStream);

//        First line is always the number of processes
        int numberOfProcesses = Integer.parseInt(scanner.nextLine());

        LinkedList<Process> processList = new LinkedList<>();

//        Fill the process list with individual processes
        for (int i = 0; i < numberOfProcesses; i++) {
//              Split the line by the spaces
            String line = scanner.nextLine();
//            Doing it with another scanner because doing line.split would require changing the variable's type a lot and that's not ideal
//            TODO: having another scanner isn't ideal, causes more overhead
            Scanner lineParser = new Scanner(line);

            int processId = lineParser.nextInt();
            int processSize = lineParser.nextInt();

//            Starts off unallocated
            Process process = new Process(processId, processSize);
            System.out.println(process);

            processList.add(process);

            lineParser.close();
        }
        scanner.close();
        return processList;
    }

    

    public static void writeToFile(Algorithms algorithm, List<String[]> outputData) throws Exception {
        File outputFile = new File(algorithm.toString() + "output.data");

        PrintWriter writer = new PrintWriter(outputFile);

        outputData.forEach(writer::println);

        writer.close();
    }
}
