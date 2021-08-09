import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

// This app models a set (rack) of test tubes containing colored balls,
//   and then proceeds to sort them according to color, with some specific sorting rules such as a ball may
//   only move to another tube that is either empty, or the top ball in the non-full destination tube is the same color.

public class LifoSort {
    static int tubeCount;
    static ArrayList<TubeRack> rackList;
    static TubeRack currentRack;
    static LocalDateTime localDateTime = LocalDateTime.now();

    private static ItemColor[][] setup() {
//        return setup646();
//        return setup647();
        return setup649();
    }

    // This sets the starting point for the solution to follow -
    //   Two of the tubes will be completely empty.
    //   The rest of the tubes will be at their maximum capacity; no more and no less.
    private static ItemColor[][] setup646() {
        // Set the initial data.  This might eventually come from a file, or user input (but that would be quite tedious).
        tubeCount = 11;
        int tubeCapacity = 4;
        ItemColor[][] initialTableau = new ItemColor[tubeCount][tubeCapacity];
        initialTableau[0] = new ItemColor[]{ItemColor.PURPLE, ItemColor.RED, ItemColor.PURPLE, ItemColor.PALEGREEN};
        initialTableau[1] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.PINK, ItemColor.PINK, ItemColor.BLUE};
        initialTableau[2] = new ItemColor[]{ItemColor.GRAY, ItemColor.RED, ItemColor.GRAY, ItemColor.PURPLE};
        initialTableau[3] = new ItemColor[]{ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.PALEGREEN, ItemColor.RED};
        initialTableau[4] = new ItemColor[]{ItemColor.ORANGE, ItemColor.LIMEGREEN, ItemColor.LIMEGREEN, ItemColor.PALEGREEN};
        initialTableau[5] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.RED, ItemColor.SKYBLUE, ItemColor.LIMEGREEN};
        initialTableau[6] = new ItemColor[]{ItemColor.PURPLE, ItemColor.PINK, ItemColor.ORANGE, ItemColor.SKYBLUE};
        initialTableau[7] = new ItemColor[]{ItemColor.ORANGE, ItemColor.GRAY, ItemColor.BLUE, ItemColor.PINK};
        initialTableau[8] = new ItemColor[]{ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.ORANGE, ItemColor.GRAY};
        initialTableau[9] = new ItemColor[4];
        initialTableau[10] = new ItemColor[4];

        return initialTableau;
    }
    private static ItemColor[][] setup647() {
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
    private static ItemColor[][] setup649() {
        // Set the initial data.  This might eventually come from a file, or user input (but that would be quite tedious).
        tubeCount = 14;
        int tubeCapacity = 4;
        ItemColor[][] initialTableau = new ItemColor[tubeCount][tubeCapacity];
        initialTableau[0] = new ItemColor[]{ItemColor.ORANGE, ItemColor.LIMEGREEN, ItemColor.GREEN, ItemColor.PALEGREEN};
        initialTableau[1] = new ItemColor[]{ItemColor.RED, ItemColor.PURPLE, ItemColor.YELLOW, ItemColor.PALEGREEN};
        initialTableau[2] = new ItemColor[]{ItemColor.PURPLE, ItemColor.PINK, ItemColor.PINK, ItemColor.RED};
        initialTableau[3] = new ItemColor[]{ItemColor.GREEN, ItemColor.YELLOW, ItemColor.LIMEGREEN, ItemColor.GRAY};
        initialTableau[4] = new ItemColor[]{ItemColor.ORANGE, ItemColor.BROWN, ItemColor.PALEGREEN, ItemColor.GRAY};
        initialTableau[5] = new ItemColor[]{ItemColor.PURPLE, ItemColor.ORANGE, ItemColor.BLUE, ItemColor.BLUE};
        initialTableau[6] = new ItemColor[]{ItemColor.GREEN, ItemColor.GREEN, ItemColor.SKYBLUE, ItemColor.GRAY};
        initialTableau[7] = new ItemColor[]{ItemColor.PURPLE, ItemColor.BLUE, ItemColor.YELLOW, ItemColor.BROWN};
        initialTableau[8] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.PALEGREEN, ItemColor.SKYBLUE, ItemColor.ORANGE};
        initialTableau[9] = new ItemColor[]{ItemColor.PINK, ItemColor.GRAY, ItemColor.RED, ItemColor.BLUE};
        initialTableau[10] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.RED, ItemColor.BROWN, ItemColor.LIMEGREEN};
        initialTableau[11] = new ItemColor[]{ItemColor.BROWN, ItemColor.LIMEGREEN, ItemColor.YELLOW, ItemColor.PINK};
        initialTableau[12] = new ItemColor[4];
        initialTableau[13] = new ItemColor[4];

        return initialTableau;
    }

    static void showTableau(ItemColor[][] theTableau) {
        for (ItemColor[] aTube : theTableau) {
            String tubeString = Arrays.asList (aTube).toString();

            tubeString = tubeString.replaceAll(", null", "");
            tubeString = tubeString.replaceAll("null", "");
            System.out.println(tubeString);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        ItemColor[][] firstTableau = setup();

//        System.out.println("The initial tableau: ");
//        showTableau(firstTableau);

        // Create a rack to hold the test tubes
        TubeRack firstRack = new TubeRack(firstTableau, new ArrayList<>());

        // Create a list of racks and add in the first one
        rackList = new ArrayList<>();
        rackList.add(firstRack);

        // Keep an indexer and a reference to the TubeRack that is currently being processed.
        currentRack = firstRack;
        int currentRackIndex = 0;


        while(true) {
            System.out.println("Exploring possibilities for rack # " + (currentRackIndex+1) + " of " + rackList.size());
            currentRack.explorePossibilities();
            currentRackIndex++;

            if(currentRackIndex == rackList.size()) {
                System.out.println("All possibilities have been explored, no solution found!");
                System.exit(0);
            } else {
                currentRack = rackList.get(currentRackIndex);
            }
        }

    } // end main

} // end LifoSort