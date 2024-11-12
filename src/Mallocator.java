import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Mallocator {

    public static void main(String[] args) throws Exception {
        String memoryInputFile = "Minput.data";
        String processInputFile = "Pinput.data";

        PrintWriter writer = new PrintWriter(new FileWriter("FFoutput.data"));
        writer.println("please work :(");



//      Contains the memory data from Minput.data
//      memorySlots[0]: addresses of start of a free memory slot
//      memorySlots[1]: addresses of end of a free memory slot
//      memorySlots[2]: 0 for unallocated 1 for allocated
        LinkedList<MemorySlot> memoryList = readMemoryInput(memoryInputFile);
        System.out.println("memoryList: \n" + memoryList);

//      Contains the process data from Pinput.data
//      processBlock[0]: process ID
//      processBlock[1]: size of process
//      processBlock[2]: 0 for unallocated 1 for allocated
        LinkedList<Process> processList = readProcessInput(processInputFile);

        System.out.println("processList: \n" + processList);

//        writeToOutput(Algorithms.FF, processList);

        LinkedList<Process> hardcodedProcesses = new LinkedList<>();

        LinkedList<MemorySlot> hardcodedOutput = new LinkedList<>();
        hardcodedOutput.add(new MemorySlot(100, 310, 2));
        hardcodedOutput.add(new MemorySlot(600, 790, 1));
        hardcodedOutput.add(new MemorySlot(1500, 1705, 3));

        System.out.println("hardcodedOutput: " + hardcodedOutput);

        generateOutputLog(Algorithms.FF, hardcodedOutput, processList);



    }

    public static class MemorySlot {
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
                    ", processID=" + processID +
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

    public static void generateOutputLog(Algorithms algorithm, LinkedList<MemorySlot> allocatedMemory, LinkedList<Process> processes) {
        try {
            File outputFile = new File(algorithm.toString() + "output.data");
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            // This will be used to keep track of what processes are allocated and unallocated for the last line of the output log
            List<Integer> allocatedProcesses = new ArrayList<>();
            List<Integer> unallocatedProcesses = new ArrayList<>();

            // Loop through the allocatedMemory and write the information
            for (MemorySlot memorySlot : allocatedMemory) {
                System.out.println("memorySlot to be written to file: \n" + memorySlot);
                String test = (memorySlot.getStart() + " " + memorySlot.getEnd() + " " + memorySlot.getProcessID());
                System.out.println("String to be written to file: \n" + test);

                writer.println(test);
            }

//            // Find the processes that aren't allocated
//            for (int i = 0; i < processes.size(); i++) {
//                Process currentProcess = processes.get(i);
//
//                if(!currentProcess.isAllocated()) {
//                    unallocatedProcesses.add(currentProcess.getId());
//                } else {
//                    allocatedProcesses.add(currentProcess.id);
//                }
//            }

            // Write -unallocated processes or -0 if they're all allocated

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeToFile(Algorithms algorithm, List<String[]> outputData) throws Exception {
        File outputFile = new File(algorithm.toString() + "output.data");

        PrintWriter writer = new PrintWriter(outputFile);

        outputData.forEach(writer::println);

        writer.close();
    }
}
