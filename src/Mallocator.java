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
        int datasets = 3;
        int chosenDataset = 0;
        File outputDir = new File("outputs");
        File inputDir = new File("inputs");
        String memoryInputFile = "Minput" + chosenDataset + ".data";
        String processInputFile = "Pinput" + chosenDataset + ".data";
        LinkedList<MemorySlot> memoryList = readMemoryInput(memoryInputFile);
        LinkedList<Process> processList = readProcessInput(processInputFile);
        LinkedList<MemorySlot> ffOutput = bestFit(processList, memoryList);
        generateOutputLog(Algorithms.FF, ffOutput, processList, chosenDataset, outputDir);




//        for (int i = 0; i < datasets; i++) {
//            System.out.println("\nRunning Algorithms on dataset " + i + "...\n");
//
//            String memoryInputFile = "Minput" + i + ".data";
//            String processInputFile = "Pinput" + i + ".data";
//
//            // TODO: This is a very stupid way of doing this
//            // Reinstantiated because the processList is not read only
//            LinkedList<MemorySlot> memoryList = readMemoryInput(memoryInputFile);
//            System.out.println("...memoryList " + i + ":");
//            System.out.println("    " + memoryList + "...");
//
//            LinkedList<Process> processList = readProcessInput(processInputFile);
//            System.out.println("...processList " + i + ":");
//            System.out.println("    " + processList + "...");
//
//            LinkedList<MemorySlot> ffOutput = firstFit(processList, memoryList);
//            generateOutputLog(Algorithms.FF, ffOutput, processList, i, outputDir);
//
//            memoryList = readMemoryInput(memoryInputFile);
//            processList = readProcessInput(processInputFile);
//
//            LinkedList<MemorySlot> bfOutput = bestFit(processList, memoryList);
//            generateOutputLog(Algorithms.BF, ffOutput, processList, i, outputDir);
//
//            memoryList = readMemoryInput(memoryInputFile);
//            processList = readProcessInput(processInputFile);
//
//            LinkedList<MemorySlot> wfOutput = worstFit(processList, memoryList);
//            generateOutputLog(Algorithms.WF, ffOutput, processList, i, outputDir);
//
//        }

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
                    ", size=" + size +
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

    public enum Algorithms {
        FF,
        WF,
        BF
    }

    // This is necessary to keep the Processes read only. This way we can just add allocated processes to a response rather than edit the original Process
    public static class AlgorithmResponse {
        LinkedList<MemorySlot> outputMemory;
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

    public static LinkedList<MemorySlot> firstFit(LinkedList<Process> processList, LinkedList<MemorySlot> memoryList) {
        LinkedList<MemorySlot> outputMemory = new LinkedList<>();
        int debugIndex = 0;

        // Loop through the Processes
        for (Process process : processList) {


            System.out.println("\nProcess " + process.getId() + " " + process);
            // Instantiate currentProcess information to be easier to read
            int processID = process.getId();
            int processSize = process.getSize();

            // This needs to be a fori loop because it memoryList changes size
            for (int j = 0; j < memoryList.size(); j++) {
                MemorySlot memorySlot = memoryList.get(j);
                System.out.println("MemorySlot " + j + " " + memorySlot);
                int memoryStart = memorySlot.getStart();
                int memoryEnd = memorySlot.getEnd();
                int memorySize = memorySlot.getSize();

                // Inner for loop to check if the current process can fit in any MemorySlot

                // If currentProcess' size is less than the memorySlot's size then allocate it
                // If the memorySlot is already allocated then skip it
                if (processSize < memorySize && memorySlot.getProcessID() == 0) {
                    System.out.println("...Allocated Process " + processID + " to MemorySlot " + j + "...");
                    // Mark currentProcess as allocated for the output log's logic
                    process.setAllocated(true);

                    // Create a new memorySlot to be added to the output list
                    // Change the size of the allocated process
                    int outputMemoryEnd = memoryStart + processSize;

                    MemorySlot outputMemorySlot = new MemorySlot(memoryStart, outputMemoryEnd, processID);

                    // Add the new memorySlot to the new List
                    outputMemory.add(outputMemorySlot);

                    // Take the remaining space and create a new memorySlot to be added to the list
                    int remainingSpace = memoryEnd - outputMemoryEnd;
                    if (remainingSpace > 0) {
                        // Create a new MemorySlot for the remaining space
                        MemorySlot remainingMemory = new MemorySlot(outputMemoryEnd, outputMemoryEnd + remainingSpace, 0);
                        // Replace the current space with that new space so it's in the right location and the old space doesn't get used
                        memoryList.set(j, remainingMemory);
                    }

                    // This ensures that one process won't be allocated to multiple slots
                    break;

                }

            }
            System.out.println("--OutputMemory: " + outputMemory + "--");
            debugIndex++;
        }


        return outputMemory;
    }

    public static LinkedList<MemorySlot> worstFit(LinkedList<Process> processList, LinkedList<MemorySlot> memoryList) {
        return new LinkedList<>();
    }

    public static LinkedList<MemorySlot> bestFit(LinkedList<Process> processList, LinkedList<MemorySlot> memoryList) {
        LinkedList<MemorySlot> outputMemory = new LinkedList<>();
        List<Integer> allocatedProcesses = new ArrayList<>();
        List<Integer> unallocatedProcesses = new ArrayList<>();
        int debugIndex = 0;

        // Loop through the Processes
        for (Process process : processList) {

            System.out.println("\nProcess " + process.getId() + " " + process);

            // Instantiate currentProcess information to be easier to read
            int processID = process.getId();
            int processSize = process.getSize();

            // Will hold the memorySlot to be accessed outside the inner loop
            MemorySlot smallestPossibleSlot = new MemorySlot(-1, 0);
            int smallestMemoryIndex = -1;

            // This needs to be a fori loop because it memoryList changes size
            for (int j = 0; j < memoryList.size(); j++) {
                MemorySlot memorySlot = memoryList.get(j);
                System.out.println("MemorySlot " + j + " " + memorySlot);
                int memoryStart = memorySlot.getStart();
                int memoryEnd = memorySlot.getEnd();
                int memorySize = memorySlot.getSize();

                if(memorySlot.getProcessID() != 0) {
                    break;
                }
                // If there's nothing assigned to smallestPossibleSlot then assign the first memory slot that fits the process
                if(smallestPossibleSlot.getSize() == -1 && processSize < memorySize) {
                    smallestPossibleSlot = memorySlot;
                    smallestMemoryIndex = j;
                } else if(memorySize < smallestPossibleSlot.getSize()) {
                    smallestPossibleSlot = memorySlot;
                    smallestMemoryIndex = j;
                }
            }

            // Allocate the process only after all the memory slots have been checked
            if (smallestPossibleSlot.getSize() != -1) {
                int memoryStart = smallestPossibleSlot.getStart();
                int memoryEnd = smallestPossibleSlot.getEnd();
                int memorySize = smallestPossibleSlot.getSize();

                System.out.println("...Allocated Process " + processID + " to MemorySlot " + smallestMemoryIndex + "...");
                // Mark currentProcess as allocated for the output log's logic
                process.setAllocated(true);

                // Create a new memorySlot to be added to the output list
                // Change the size of the allocated process
                int outputMemoryEnd = memoryStart + processSize;

                MemorySlot outputMemorySlot = new MemorySlot(memoryStart, outputMemoryEnd, processID);

                // Add the new memorySlot to the new List
                outputMemory.add(outputMemorySlot);

                // Take the remaining space and create a new memorySlot to be added to the list
                int remainingSpace = memoryEnd - outputMemoryEnd;
                if (remainingSpace > 0) {
                    // Create a new MemorySlot for the remaining space
                    MemorySlot remainingMemory = new MemorySlot(outputMemoryEnd, outputMemoryEnd + remainingSpace, 0);
                    // Replace the current space with that new space so it's in the right location and the old space doesn't get used
                    memoryList.set(smallestMemoryIndex, remainingMemory);
                }
            }



                System.out.println("--OutputMemory: " + outputMemory + "--");
            debugIndex++;
        }
        return outputMemory;
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
//            System.out.println(memorySlot);

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
//            System.out.println(process);

            processList.add(process);

            lineParser.close();
        }
        scanner.close();
        return processList;
    }

    // TODO: the algorithms should all return a response that has the memoryOutput AND a list of allocated processes, this way the processList doesn't have to be written to and it can be reused
    public static void generateOutputLog(Algorithms algorithm, LinkedList<MemorySlot> allocatedMemory, LinkedList<Process> processes, int fileIndex, File outputDir) {
        try {
            // Putting the output in a folder
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            File outputFile = new File(outputDir, algorithm.toString() + "output" + fileIndex + ".data");

            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

            // This will be used to keep track of what processes are allocated and unallocated for the last line of the output log
            List<Integer> allocatedProcesses = new ArrayList<>();
            List<Integer> unallocatedProcesses = new ArrayList<>();

            // Loop through the allocatedMemory and write the information
            for (MemorySlot memorySlot : allocatedMemory) {
//                System.out.println("memorySlot to be written to file: \n" + memorySlot);
                String test = (memorySlot.getStart() + " " + memorySlot.getEnd() + " " + memorySlot.getProcessID());
//                System.out.println("String to be written to file: \n" + test);

                writer.println(test);
            }

            // Find the processes that aren't allocated
            for (Process currentProcess : processes) {
                if (!currentProcess.isAllocated()) {
                    unallocatedProcesses.add(currentProcess.getId());
                } else {
                    allocatedProcesses.add(currentProcess.id);
                }
            }

            // Write -unallocated processes or -0 if they're all allocated
            
            // ALl processes are allocated
            if (allocatedProcesses.size() == processes.size()) {
                writer.println("-0");
            } else {
                writer.print("-");
                for (int i = 0; i < unallocatedProcesses.size(); i++) {
                    if (i==0) {
                        writer.print(unallocatedProcesses.get(i));
                    } else {
                        writer.print("," +  unallocatedProcesses.get(i));
                    }
                }
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
