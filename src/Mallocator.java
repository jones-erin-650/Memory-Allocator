import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
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

    public static LinkedList<int[]> readMemoryInput(String memoryInput) {
        InputStream inputStream = Mallocator.class.getResourceAsStream(memoryInput);
        Scanner scanner = new Scanner(inputStream);

//        First line is always the number of memoryblocks
        int numberOfMemoryBlocks = Integer.parseInt(scanner.nextLine());

        LinkedList<int[]> memorySlots = new LinkedList<>();

//        Fill the memory list with individual memorySlots
        for (int i = 0; i < numberOfMemoryBlocks; i++) {
//              Split the line by the spaces
            String line = scanner.nextLine();
//            Doing it with another scanner because doing line.split would require changing the variable's type a lot and that's not ideal
//            TODO: having another scanner isn't ideal, causes more overhead
            Scanner lineParser = new Scanner(line);

            int startAddress = lineParser.nextInt();
            int endAddress = lineParser.nextInt();

//            Starts off unallocated
            int[] memoryBlock = {startAddress, endAddress, 0};
            System.out.println("MemorySlot" + memoryBlock[0] + ": " + Arrays.toString(memoryBlock));

            memorySlots.add(memoryBlock);

            lineParser.close();
        }
        scanner.close();
        return memorySlots;


    }

    public static LinkedList<int[]> readProcessInput(String processInput) {
        InputStream inputStream = Mallocator.class.getResourceAsStream(processInput);
        Scanner scanner = new Scanner(inputStream);

//        First line is always the number of processes
        int numberOfProcesses = Integer.parseInt(scanner.nextLine());

        LinkedList<int[]> processList = new LinkedList<>();

//        Fill the process list with individual processBlocks
        for (int i = 0; i < numberOfProcesses; i++) {
//              Split the line by the spaces
            String line = scanner.nextLine();
//            Doing it with another scanner because doing line.split would require changing the variable's type a lot and that's not ideal
//            TODO: having another scanner isn't ideal, causes more overhead
            Scanner lineParser = new Scanner(line);

            int processId = lineParser.nextInt();
            int processSize = lineParser.nextInt();

//            Starts off unallocated
            int[] processBlock = {processId, processSize, 0};
            System.out.println("ProcessBlock" + processBlock[0] + ": " + Arrays.toString(processBlock));

            processList.add(processBlock);

            lineParser.close();
        }
        scanner.close();
        return processList;
    }

    public static void writeToOutput(Algorithms algorithm, List<String[]> outputData) throws Exception {
        File outputFile = new File(algorithm.toString() + "output.data");

        PrintWriter writer = new PrintWriter(outputFile);

        outputData.forEach(writer::println);

        writer.close();
    }
}
