import java.util.ArrayList;
import java.util.Scanner;

// This app models a set (rack) of test tubes containing colored balls,
//   and then proceeds to sort them according to color, with some specific sorting rules such as a ball may
//   only move to another tube that is either empty, or the top ball in the non-full tube is the same color.

public class LifoSort {
    static int tubeCount = 14;

    static Integer sourceTubeIndex;  // Since this var stays 'under the hood', we can use zero-based Vector indexing.
    static ItemColor sourceValue;
    static ItemColor[][] initialRackContent;
    static TubeRack tubeRack;
    static ArrayList<String> currentRun;

    private static void setup() {
        // Set the initial data.  This might eventually come from a file, or user input (but that would be quite tedious).
        int tubeCapacity = 4;
        initialRackContent = new ItemColor[tubeCount][tubeCapacity];
        initialRackContent[0] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.SKYBLUE, ItemColor.GREEN, ItemColor.PURPLE};
        initialRackContent[1] = new ItemColor[]{ItemColor.BROWN, ItemColor.GRAY, ItemColor.RED, ItemColor.ORANGE};
        initialRackContent[2] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.GRAY, ItemColor.GRAY, ItemColor.PURPLE};
        initialRackContent[3] = new ItemColor[]{ItemColor.BROWN, ItemColor.GREEN, ItemColor.ORANGE, ItemColor.ORANGE};
        initialRackContent[4] = new ItemColor[]{ItemColor.YELLOW, ItemColor.PINK, ItemColor.BLUE, ItemColor.LIMEGREEN};
        initialRackContent[5] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.ORANGE};
        initialRackContent[6] = new ItemColor[]{ItemColor.YELLOW, ItemColor.BROWN, ItemColor.RED, ItemColor.GREEN};
        initialRackContent[7] = new ItemColor[]{ItemColor.PURPLE, ItemColor.GREEN, ItemColor.LIMEGREEN, ItemColor.PINK};
        initialRackContent[8] = new ItemColor[]{ItemColor.PINK, ItemColor.BLUE, ItemColor.YELLOW, ItemColor.GRAY};
        initialRackContent[9] = new ItemColor[]{ItemColor.PURPLE, ItemColor.RED, ItemColor.PALEGREEN, ItemColor.LIMEGREEN};
        initialRackContent[10] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.YELLOW, ItemColor.BLUE, ItemColor.BROWN};
        initialRackContent[11] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.PINK, ItemColor.PALEGREEN, ItemColor.RED};
        initialRackContent[12] = new ItemColor[4];
        initialRackContent[13] = new ItemColor[4];
    }

    public static void main(String[] args) {
        setup();

        // Create a rack (list) to hold the tubes
        tubeRack = new TubeRack(initialRackContent);

        // A bit of output for the user, so it's obvious that the app has started 'thinking' about how to get this all sorted out.
        System.out.println("The number of different colors is: " + (tubeCount-2));

        // We start with several assumptions (not going to idiot-proof the initial setup):
        //   Not one tube will already be sorted.
        //   Two of the tubes will be completely empty.
        //   The rest of the tubes will be at their maximum capacity; no more and no less.
        tubeRack.loadRack(); // Load the tube rack with the initial problem set.
        tubeRack.showRack(); // Useful during dev and debug; later this line will be disabled.
        System.out.println("---------------------\n");

        // Set the starting positions
        sourceTubeIndex = 0;  // Start on the 'left'.
        sourceValue = tubeRack.get(sourceTubeIndex).peek();

//            tubeRack.loadRack();
//            System.out.println("The rack of tubes is sorted: " + tubeRack.sorted());
        int numberOfMoves = 0;
        currentRun = new ArrayList<>();
        while(!tubeRack.sorted()) {
            if(tubeRack.moveOne()) {
                numberOfMoves++;
                tubeRack.showRack(); // Useful during dev and debug; later this line will be disabled.
                System.out.println("------- after " + numberOfMoves + " moves --------------\n");
            } else {
                sourceTubeIndex++;
            }
            if(sourceTubeIndex+1 > tubeCount) {
                // we need to start the next trial...
                System.out.println("End of the line after " + numberOfMoves + " moves!");
                int moveNum = 1;
                for(String s: currentRun) {
                    System.out.print(moveNum++ + ".  ");
                    System.out.println(s);
                }
                System.exit(numberOfMoves);
            } else {
                sourceValue = tubeRack.get(sourceTubeIndex).peek();
            }
        } // end while

    } // end main
}