import java.util.ArrayList;

// This app models a set (rack) of test tubes containing colored balls,
//   and then proceeds to sort them according to color, with some specific sorting rules such as a ball may
//   only move to another tube that is either empty, or the top ball in the non-full tube is the same color.

public class LifoSort {
    static int tubeCount;

    static Integer sourceTubeIndex;  // Since this var stays 'under the hood', we can use zero-based Vector indexing.
    static ItemColor sourceValue;
    static TubeRack tubeRack;
    static ArrayList<String> currentRun;

    static ArrayList<TubeRack> rackList;

    // This sets the starting point for the solution to follow -
    //   Two of the tubes will be completely empty.
    //   The rest of the tubes will be at their maximum capacity; no more and no less.
    private static ItemColor[][] setup() {
        rackList = new ArrayList<>();
        // Set the initial data.  This might eventually come from a file, or user input (but that would be quite tedious).
        tubeCount = 14;
        int tubeCapacity = 4;
        ItemColor[][] initialTableau = new ItemColor[tubeCount][tubeCapacity];
        initialTableau[0] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.SKYBLUE, ItemColor.GREEN, ItemColor.PURPLE};
        initialTableau[1] = new ItemColor[]{ItemColor.BROWN, ItemColor.GRAY, ItemColor.RED, ItemColor.ORANGE};
        initialTableau[2] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.GRAY, ItemColor.GRAY, ItemColor.PURPLE};
        initialTableau[3] = new ItemColor[]{ItemColor.BROWN, ItemColor.GREEN, ItemColor.ORANGE, ItemColor.ORANGE};
        initialTableau[4] = new ItemColor[]{ItemColor.YELLOW, ItemColor.PINK, ItemColor.BLUE, ItemColor.LIMEGREEN};
        initialTableau[5] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.ORANGE};
        initialTableau[6] = new ItemColor[]{ItemColor.YELLOW, ItemColor.BROWN, ItemColor.RED, ItemColor.GREEN};
        initialTableau[7] = new ItemColor[]{ItemColor.PURPLE, ItemColor.GREEN, ItemColor.LIMEGREEN, ItemColor.PINK};
        initialTableau[8] = new ItemColor[]{ItemColor.PINK, ItemColor.BLUE, ItemColor.YELLOW, ItemColor.GRAY};
        initialTableau[9] = new ItemColor[]{ItemColor.PURPLE, ItemColor.RED, ItemColor.PALEGREEN, ItemColor.LIMEGREEN};
        initialTableau[10] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.YELLOW, ItemColor.BLUE, ItemColor.BROWN};
        initialTableau[11] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.PINK, ItemColor.PALEGREEN, ItemColor.RED};
        initialTableau[12] = new ItemColor[4];
        initialTableau[13] = new ItemColor[4];

        return initialTableau;
    }

    public static void main(String[] args) {
        setup();

        // Create a rack (list) to hold the tubes
        tubeRack = new TubeRack(setup());
        rackList.add((TubeRack) tubeRack.clone());

        // A bit of output for the user, so it's obvious that the app has started 'thinking' about how to get this all sorted out.
        System.out.println("The number of different colors is: " + (tubeCount-2));
        tubeRack.showRack(); // Useful during dev and debug; later this output may be disabled.
        System.out.println("---------------------\n");

        for(TubeRack tubeRack: rackList) {

            // Set the starting positions
            sourceTubeIndex = 0;  // Start on the 'left'.
            sourceValue = tubeRack.get(sourceTubeIndex).peek();

            int numberOfMoves = 0;
            currentRun = new ArrayList<>();
            while (!tubeRack.sorted()) {
                if (tubeRack.moveOne()) {
                    System.out.println(currentRun.get(currentRun.size() - 1));
                    numberOfMoves++;
                    tubeRack.showRack(); // Useful during dev and debug; later this line will be disabled.
                    System.out.println("------- after " + numberOfMoves + " moves --------------\n");
                } else {
                    sourceTubeIndex++;
                }
                if (sourceTubeIndex + 1 > tubeCount) {
                    // we need to start the next trial...
                    System.out.println("End of the line after " + numberOfMoves + " moves!");
                    int moveNum = 1;
                    for (String s : currentRun) {
                        System.out.print(moveNum++ + ".  ");
                        System.out.println(s);
                    }
                    System.exit(numberOfMoves);
                } else {
                    sourceValue = tubeRack.get(sourceTubeIndex).peek();
                }
            } // end while
        }

    } // end main
}