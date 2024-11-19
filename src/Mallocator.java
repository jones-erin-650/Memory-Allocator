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

        for (int i = 0; i < datasets; i++) {
            System.out.println("\n...Performing algorithms on dataset " + i + "...\n");

            File outputDir = new File("outputs");

            File inputDir = new File(".", "inputs");

            String memoryInputFile = new File(inputDir, "Minput" + i + ".data").getPath();
            String processInputFile = new File(inputDir, "Pinput" + i + ".data").getPath();

            LinkedList<MemorySlot> memoryList = readMemoryInput(memoryInputFile);
            LinkedList<Process> processList = readProcessInput(processInputFile);

            System.out.println("\n--Performing firstFit on dataset " + i + "--");
            performAlgorithm(Algorithms.FF, processList, memoryList, i, outputDir);
            System.out.println("\n--Performing bestFit on dataset " + i + "--");
            performAlgorithm(Algorithms.BF, processList, memoryList, i, outputDir);
            System.out.println("\n--Performing worstFit on dataset " + i + "--");
            performAlgorithm(Algorithms.WF, processList, memoryList, i, outputDir);
        }
    }

    public static AlgorithmResponse firstFit(LinkedList<Process> referencedProcesses, LinkedList<MemorySlot> memoryList) {
        // Dereference the processes so the original data is read only
        LinkedList<Process> dereferencedProcesses = referencedProcesses;

        LinkedList<MemorySlot> outputMemory = new LinkedList<>();

        // Loop through the Processes
        for (Process process : dereferencedProcesses) {

            System.out.println("\nProcess " + process.getId() + " " + process);
            // Instantiate currentProcess information to be easier to read
            int processID = process.getId();
            int processSize = process.getSize();

            // This needs to be a fori loop because it memoryList changes size
            for (int j = 0; j < memoryList.size(); j++) {
                MemorySlot memorySlot = memoryList.get(j);
                System.out.println("    MemorySlot " + j + " " + memorySlot);
                int memoryStart = memorySlot.getStart();
                int memoryEnd = memorySlot.getEnd();
                int memorySize = memorySlot.getSize();

                // Inner for loop to check if the current process can fit in any MemorySlot

                // If currentProcess' size is less than the memorySlot's size then allocate it
                // If the memorySlot is already allocated then skip it
                if (processSize < memorySize && memorySlot.getProcessID() == 0) {
                    System.out.println("    ...Allocated Process " + processID + " to MemorySlot " + j + "...");
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
            System.out.println("OutputMemory: " + outputMemory);
        }

        System.out.println("\n..FirstFit Algorithm Complete..\n");
        System.out.println("Final OutputMemory: " + outputMemory + "--");
        return new AlgorithmResponse(outputMemory, dereferencedProcesses);
    }

    public static AlgorithmResponse worstFit(LinkedList<Process> processList, LinkedList<MemorySlot> memoryList) {
        return new AlgorithmResponse();
    }

    public static AlgorithmResponse bestFit(LinkedList<Process> referencedProcesses, LinkedList<MemorySlot> memoryList) {
        LinkedList<MemorySlot> outputMemory = new LinkedList<>();
        LinkedList<Process> dereferencedProcesses = referencedProcesses;

        int debugIndex = 0;

        // Loop through the Processes
        for (Process process : dereferencedProcesses) {

            System.out.println("\nProcess " + process.getId() + " " + process);

            // Instantiate currentProcess information to be easier to read
            int processID = process.getId();
            int processSize = process.getSize();

            // Will hold the memorySlot to be accessed outside the inner loop
            MemorySlot smallestPossibleSlot = null;
            int smallestMemoryIndex = -1;

            // This needs to be a fori loop because it memoryList changes size
            for (int j = 0; j < memoryList.size(); j++) {
                MemorySlot memorySlot = memoryList.get(j);
                System.out.println("    MemorySlot " + j + " " + memorySlot);
                int memoryStart = memorySlot.getStart();
                int memoryEnd = memorySlot.getEnd();
                int memorySize = memorySlot.getSize();

                if(memorySlot.getProcessID() != 0) {
                    continue;
                }
                // If there's nothing assigned to smallestPossibleSlot then assign the first memory slot that fits the process
                if(smallestPossibleSlot == null && processSize < memorySize) {
                    smallestPossibleSlot = memorySlot;
                    smallestMemoryIndex = j;
                } else if(processSize <= memorySize && memorySize < smallestPossibleSlot.getSize()) {
                    smallestPossibleSlot = memorySlot;
                    smallestMemoryIndex = j;
                }
            }

            // Allocate the process only after all the memory slots have been checked
            if (smallestPossibleSlot != null) {
                int memoryStart = smallestPossibleSlot.getStart();
                int memoryEnd = smallestPossibleSlot.getEnd();
                int memorySize = smallestPossibleSlot.getSize();

                System.out.println("    ...Allocated Process " + processID + " to MemorySlot " + smallestMemoryIndex + "...");
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



                System.out.println("OutputMemory: " + outputMemory);
            debugIndex++;
        }
        return new AlgorithmResponse(outputMemory, dereferencedProcesses);
    }

    public static LinkedList<MemorySlot> readMemoryInput(String memoryInput) throws FileNotFoundException {
        File memoryFile = new File(memoryInput);
        Scanner scanner = new Scanner(memoryFile);

//        First line is always the number of memoryblocks
        int numberOfMemorySlots = Integer.parseInt(scanner.nextLine());

        LinkedList<MemorySlot> memorySlots = new LinkedList<>();

//        Fill the memory list with individual memorySlots
        for (int i = 0; i < numberOfMemorySlots; i++) {
//              Split the line by the spaces
            String line = scanner.nextLine();

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

    public static LinkedList<Process> readProcessInput(String processInput) throws FileNotFoundException {
       File processFile = new File(processInput);
       Scanner scanner = new Scanner(processFile);

//        First line is always the number of processes
        int numberOfProcesses = Integer.parseInt(scanner.nextLine());

        LinkedList<Process> processList = new LinkedList<>();

//        Fill the process list with individual processes
        for (int i = 0; i < numberOfProcesses; i++) {
//              Split the line by the spaces
            String line = scanner.nextLine();
//            Doing it with another scanner because doing line.split would require changing the variable's type a lot and that's not ideal

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

    public static void generateOutputLog(Algorithms algorithm, AlgorithmResponse inputData, int fileIndex, File outputDir) {
        try {
            LinkedList<MemorySlot> allocatedMemory = inputData.getOutputMemory();
            LinkedList<Process> processes = inputData.getOutputProcesses();

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
                String outputString = (memorySlot.getStart() + " " + memorySlot.getEnd() + " " + memorySlot.getProcessID());
//                System.out.println("String to be written to file: \n" + test);

                writer.println(outputString);
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

    public static void performAlgorithm(Algorithms algorithm, LinkedList<Process> processList, LinkedList<MemorySlot> memoryList, int fileIndex, File outputDir) {
        AlgorithmResponse response = null;

        switch(algorithm) {
            case FF:
                response = firstFit(processList, memoryList);
                break;
            case BF:
                response = bestFit(processList, memoryList);
            case WF:
                response = worstFit(processList, memoryList);
        }

        generateOutputLog(algorithm, response, fileIndex, outputDir);
    }
}
