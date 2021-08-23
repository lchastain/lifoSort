import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;

// Models a 'rack' of TestTubes with contents
// This is not the graphical class; it is the one used in finding the puzzle solution.
public class TubeRack extends ArrayList<TestTube<ItemColor>> {
    final int tubeCapacity;
    final int tubeCount;
    final ItemColor[][] rackContent;
    ArrayList<PossibleMove> possibleMoves;
    Integer sourceTubeIndex = 0;  // Since this var stays 'under the hood', we can use zero-based Vector indexing.
    ItemColor sourceValue;        // The top item in the current source tube
    ArrayList<String> previousMoves;
    ArrayList<String> currentRun;
    int rackNumber;
    PossibleMove trialMove;
    TubeRack spawnedRack;  // The most recent new rack that this rack has created during possibility exploration.

    public TubeRack(ItemColor[][] rackContent, ArrayList<String> movesUpToThisTableau) {
        super();
        this.rackContent = rackContent;
        this.tubeCount = rackContent.length;
        this.tubeCapacity = rackContent[0].length;
        loadRack();
        previousMoves = new ArrayList<>(movesUpToThisTableau);
        possibleMoves = new ArrayList<>();

        // Determine our rack 'number' from the size of the rack list.
        rackNumber = (LifoSort.rackList == null) ? 1: LifoSort.rackList.size() + 1;

        findPossibilities(); // The very first tableau will always have (tubeCount-2) possibilities.
//        System.out.println("Found " + possibleMoves.size() + " possible moves in TubeRack #" + rackNumber);
//        for (PossibleMove possibleMove : possibleMoves) {
//            System.out.println(possibleMove.toString());
//        }
//        System.out.println();
    } // end constructor

    // The logic prior to this method being called is believed to have avoided any situation that would be disallowed
    //   by the program rules so that at this point only a successful move is expected.
    //   So - no need to check for content before doing a pop(), or worry that we would exceed capacity from a push().
    // However, we DO need to first verify that this exact move has not already been made during the current run and
    //   if it has, do not do it again.  This will prevent this variant of an infinite loop.
    boolean popAndPush(TestTube<ItemColor> aTube) {
        TestTube<ItemColor> sourceTube = get(sourceTubeIndex);

        int fromSlot = tubeCapacity + 1 - sourceTube.size();
        int toSlot = tubeCapacity - aTube.size();
        trialMove = new PossibleMove(sourceValue, sourceTubeIndex, fromSlot, aTube.tubeNumber-1, toSlot );
//        String theMove = "Move " + sourceValue + " from tube " + sourceTube.tubeNumber +
//                " (slot " + fromSlot + ") to tube " + aTube.tubeNumber + " (slot " + toSlot + ")";

        String theMove = "Move " + trialMove;  // implicit .toString()

        if (currentRun.contains(theMove)) {
            return false;
        }

        sourceTube.pop(); // We already know the value; this is just to get it off the stack.
        ItemColor theResult = aTube.push(sourceValue);
        if (theResult == null) { // This is what we do NOT expect, but handling it nevertheless.
            System.out.println("The move failed!  Exiting the app.");
            System.exit(1); // A fail exit.  This may help during dev in case we go off the rails during recursions.
        }
        if (aTube.sorted()) theMove += " - Sorted!";
        currentRun.add(theMove);

        // After a successful move, the source tube index must be reset.  In some cases it will
        // already be at zero, but it's more efficient to just set it whether it needs it or not.
        sourceTubeIndex = 0;

        return true;
    } // end popAndPush

    // loop thru the possibleMoves list
    void explorePossibilities() {
        int possibilityNum = 0;
        int possibilityCount = possibleMoves.size();
        if (possibilityCount > 0) {
            System.out.print("Looking at each of " + possibilityCount + " possible moves");
        } else {
            System.out.print("There are no possible moves");
        }
        if (previousMoves.size() > 0) {
            System.out.println(" after this sequence: ");
            showMoves();
        } else {
            System.out.println(".");
        }

        for (PossibleMove possibleMove : possibleMoves) {
            possibilityNum++;
            if(possibleMove.explored) {
                System.out.println("Possible move #" + possibilityNum + " has already been explored (" + possibleMove + ")");
                continue;
            }
            System.out.println("Considering possible move #" + possibilityNum + " of " + possibilityCount + ": " + possibleMove);

            sourceValue = possibleMove.theItem;
            sourceTubeIndex = possibleMove.fromTubeIndex;
            if (possibilityNum > 1) loadRack(); // Rack needs reset before each new possibility is explored.
            explorePossibility();
            possibleMove.explored = true; // This can be relevant even in the same rack, when two or more moves can be made in different orders.
            if(sorted()) return; // This happens when run from the graphical user setup, because otherwise the app would have ended upon rack sorted.
        }
    } // end explorePossibilities


    // This method makes a 'possible' move and then explores follow-on moves in a depth-first approach.
    void explorePossibility() {
        int numberOfMoves = previousMoves.size();
        currentRun = new ArrayList<>(); // The list of moves that are being made/considered, starting with 'this' one.
        while (!sorted()) {  // Either we find a solution or bail out after determining that the possibility does not pan out.
            if (moveOne()) { // If we did move one, that move was added to the currentRun list by popAndPush()
                // Keep track of how many moves (from the very start) are in the current possibility checkout sequence -
                numberOfMoves++;  // This will be the count of previous moves plus the moves in the current run.

                if(spawnedRack != null) {
                    spawnedRack.eliminatePossibility(trialMove);
                }


                // Print out the move we just made (which changed the tableau that this rack is holding)
                System.out.println("  trying " + numberOfMoves + ".  " + trialMove); // Move color from tube x (slot) to tube y (slot)

                // Get the new tableau of this rack.
                ItemColor[][] newTableau = getCurrentTableau();

                // Verify that we don't already have a rack with this same new tableau.
                for (TubeRack tubeRack : LifoSort.rackList) {  // Search the existing rack list
                    if (Arrays.deepEquals(newTableau, tubeRack.rackContent)) {
                        System.out.println("  attempted move #" + numberOfMoves + " of rack #" + rackNumber + " results in a duplicate tableau of rack number " + tubeRack.rackNumber);
                        // This possibility sequence would follow a circular path; ending it here by not allowing a
                        // duplicate rack to be added to the overall list.
                        return;
                    }
                }
                // Make a composite list of all previous moves plus the ones made during this possibility checkout.
                ArrayList<String> combinedRun = new ArrayList<>(previousMoves);
                combinedRun.addAll(currentRun);

                // Make a new rack from the new tableau, using the composite list as its 'previous moves' that led to
                //  its initial layout.  Then add the new rack to the overall list of racks, to eventually be examined.
                TubeRack tr = new TubeRack(newTableau, combinedRun);
                System.out.println("  created tube rack #" + tr.rackNumber + " with " + tr.possibleMoves.size() + " possible moves");
                spawnedRack = tr;
                LifoSort.rackList.add(tr);

            } else { // We were unable to move the ItemColor from the current tube, so move on to the next tube.
                sourceTubeIndex++;
            }

            if (sourceTubeIndex + 1 >= tubeCount) { // If we have no more tubes to consider -
                // then we need to reset the rack and explore the next possibility.

                //showMoves();
                System.out.println("This possibility sequence has no further available moves.");
                return;
            } else { // Get the next ItemColor to be considered.
                try {
                    sourceValue = get(sourceTubeIndex).peek();
                } catch (EmptyStackException ese) { // We tried to look at a value from an empty tube; ignore and go on.
                    //ese.printStackTrace();
                    return;
                }
            }
        } // end while

        // If we fell out of the 'while' loop and landed here then the solution has been found!
        System.out.println("\nThe rack is completely sorted!");
        showMoves();
        System.out.println("Started: " + LifoSort.localDateTime);
        System.out.println("Ended: " + LocalDateTime.now());
        long seconds = ChronoUnit.SECONDS.between(LifoSort.localDateTime, LocalDateTime.now());
        if (seconds > 60) {
            long minutes = seconds / 60;
            seconds = seconds % 60;
            System.out.println("Elapsed: " + minutes + " minutes and " + seconds + " seconds.");
        } else {
            System.out.println("Elapsed: " + seconds + " seconds.");
        }
        if (LifoSort.textOnly) System.exit(0);  // All Sorted!
    } // end explorePossibility

    private void eliminatePossibility(PossibleMove possibilityToEliminate) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            PossibleMove possibleMove = possibleMoves.get(i);
            //System.out.println("Comparing " + possibleMove + " to " + possibilityToEliminate);
            if (possibilityToEliminate.toString().equals(possibleMove.toString())) {
                possibleMove.explored = true;
                System.out.println("  marked possible move #" + (i+1) + " of rack number " + rackNumber + " as already explored.");
                return;
            }
        }
    }

    private void showMoves() {
        // Print out what we did to get to this point -
        int moveNum = 1;
        for (String s : previousMoves) {
            System.out.print(moveNum++ + ".  ");
            System.out.println(s);
        }
        if (currentRun != null) {
            for (String s : currentRun) {
                System.out.print(moveNum++ + ".  ");
                System.out.println(s);
            }
        }
    }

    // Cycle through all the tubes in this rack using one loop nested inside another, to find every
    // possible single move that could be made from the current tableau.  Keep the 'legal' ones in our list.
    // These possibilities are only 1-deep; they do not continue on to the move after one that is found.
    // Once a possibility is selected for exploring, that is when the processing goes 'deeper'; all the
    // way to the 'bottom', so that is why we need to retain the different starting points at every level;
    // otherwise we would just repeat the first path down, once for each possible move that was found.
    void findPossibilities() {
        int emptyTubeIndex = -1;  // Initialize

        for (int i = 0; i < tubeCount; i++) {  // We will use this index for considering a source Tube
            TestTube<ItemColor> sourceTube = get(i);  // Get the source tube
            if (sourceTube.empty()) continue;  // Cannot move an item from an empty tube.
            if (sourceTube.sorted()) continue;  // Connot move an item from a sorted tube.
            ItemColor sourceItem = sourceTube.peek(); // Get the source item
            int fromSlot = (tubeCapacity + 1) - sourceTube.size();

            for (int j = 0; j < tubeCount; j++) { // We will use this index for considering a destination Tube.
                if (i == j) continue;  // Cannot move an item to the same tube that it is coming from.
                TestTube<ItemColor> destTube = get(j);
                if (destTube.full()) continue;
                if (destTube.empty()) { // The destination tube is currently empty.
                    // Create a new PossibleMove and add it to the list.
                    // But first verify that we don't already have a move of this item to an empty tube, because there is
                    // no difference when two different tube destinations are both 'empty'; disallow a second one.
                    if (emptyTubeIndex < 0) emptyTubeIndex = j; // Take the first empty.  The other one will be ignored.
                    if (j == emptyTubeIndex) { // Otherwise it is the other empty tube; there are never more than two.
                        PossibleMove possibleMove = new PossibleMove(sourceItem, i, fromSlot, j, tubeCapacity);
                        possibleMoves.add(possibleMove);
                    }
                } else {
                    ItemColor nextItem = destTube.peek();
                    if (sourceItem.equals(nextItem)) { // Create a new PossibleMove and add it to the list.
                        // This is not the same math as the trials that take place after a move while looking
                        // deeper; this move is hypothetical for this rack at this point, whereas those moves are
                        // 'made' while determining whether or not they ultimately lead to a solution, and each
                        // subsequent one looks at a different tableau/rack.
                        int destSlot = tubeCapacity - destTube.size();
                        PossibleMove possibleMove = new PossibleMove(sourceItem, i, fromSlot, j, destSlot);
                        possibleMoves.add(possibleMove);
                    }
                }

            } // end for i  (outer loop, of sources)
        } // end for j  (inner loop, of destinations)
    } // end findPossibilities


    // Extract the current tableau from this TubeRack.
    // Called after a move has been made, prior to constructing a follow-on TubeRack.
    private ItemColor[][] getCurrentTableau() {
        ItemColor[][] currentTableau = new ItemColor[tubeCount][tubeCapacity];
        for (int i = 0; i < tubeCount; i++) {
            TestTube<ItemColor> aTube = get(i);
            currentTableau[i] = new ItemColor[tubeCapacity];
            for (int j = 0; j < aTube.size(); j++) {
                currentTableau[i][aTube.size() - 1 - j] = aTube.elementAt(j);
            }
        }
        return currentTableau;
    }


    // Set the content of each tube in the rack -
    void loadRack() {
        int tubeNumber = 1;
        clear();
        TestTube<ItemColor> tmpTube;
        for (ItemColor[] tubeContent : rackContent) {
            tmpTube = new TestTube<>(tubeCapacity, tubeNumber++);
            tmpTube.setContent(tubeContent);
            add(tmpTube);
        }
    } // end loadRack

    // This method will either make one single move and return true, or return a false.  It does not consult the list
    //   of possible moves because it may be looking 'deeper' than the top level and the first move is the only one
    //   that is in the possibles for this rack.
    // We come here with a sourceTubeIndex and a sourceValue that have already been set in explorePossibility().
    boolean moveOne() {
        int destinationRemainingCapacity;
        TestTube<ItemColor> sourceTube = get(sourceTubeIndex);
        for (int i = 0; i < tubeCount; i++) { // We need the index number as well as the iteration functionality.
            if (i == sourceTubeIndex) continue;  // Cannot move an item to the same tube that it is coming from.
            TestTube<ItemColor> aTube = get(i);
            if (aTube.full()) continue;  // Cannot move an item to a Tube that is already full.
            if (aTube.empty()) {
                if (!sourceTube.homogenous()) {
                    return popAndPush(aTube);
                } else {
                    String theMove = "  did not move " + sourceValue + " (#" +
                            ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
                            " from tube " + (sourceTubeIndex + 1) +
                            ") to tube " + (i + 1) + " because the source tube is homogenous " +
                            "and the destination tube is empty.";
                    System.out.println(theMove);
                    return false;
                }
            } else {
                ItemColor nextValue = aTube.peek();
                if (sourceValue.equals(nextValue)) {  // Otherwise it is a disallowed move.

                    // When both source and destination tubes are homogenous (and we already know they are the same
                    // color as each other), disallow the move from the greater content tube to the lesser content tube.
                    if (sourceTube.homogenous() && aTube.homogenous()) {
                        if (sourceTube.size() > aTube.size()) {
                            String theMove = "  did not move " + sourceValue + " (#" +
                                    ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
                                    " from tube " + (sourceTubeIndex + 1) +
                                    ") to tube " + (i + 1) +
                                    " because the directionality is illogical.";
                            System.out.println(theMove);
                            return false;
                        }
                    }


                    // When there is more than one of the same color at the top of the source tube, we disallow
                    //   the move to the destination tube unless there is room enough there for ALL of them.
                    //-------------------------------------------------------------------------------------------
                    destinationRemainingCapacity = aTube.maxCapacity - aTube.size();

                    // Find out how many of the sourceValue color are directly below the sourceValue.
                    int depth = 1;
                    int itemsToMove = 0;
                    while (sourceTube.peek(depth++) == sourceValue) {
                        itemsToMove++;
                    }
                    if (itemsToMove > destinationRemainingCapacity) {
                        String theMove = "  did not move " + sourceValue + " (#" +
                                ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
                                " from tube " + (sourceTubeIndex + 1) +
                                ") to tube " + (i + 1) + " because the destination tube does not have enough room for " +
                                itemsToMove + " items.";
                        System.out.println(theMove);
                        return false;
                    }
                    //-------------------------------------------------------------------------------------------

                    return popAndPush(aTube);
//                } else {  // We don't need an 'else/continue' here, because we are already at the bottom of the loop.
                    // But what it means is - the candidate destination tube was neither full nor empty, but it did
                    // not have as its top value, the same value that we want to push.
                    // Therefore it is disqualified as a potential place to put the sourceValue.
                    // The only reason this section is here is for debug/troubleshooting, so you may find it disabled.
                    // Also - this 'did not move' notification is different from all the others because it is a move
                    //   that the game itself does not allow; it does not follow 'the rules'.
//                    String theMove = "Did not move " + sourceValue + " (#" +
//                            ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
//                            " from tube " + (sourceTubeIndex + 1) +
//                            ") to tube " + (i + 1) + " because the top of that tube contains " + nextValue + " (in the #" +
//                            ((tubeCapacity + 1) - get(i).size()) + " slot).";
                    //System.out.println(theMove);
                }
            }

        } // end for
        return false;
    }

    void showRack() {
        // Visual check of the current state (we don't want to use 'pop()'; that would empty the tubes).
        // But a simple 'toString()' shows the data in Vector order, so we need to reference the elements
        // in reverse order to show them in the same order that they would be popped from the Stack.
        for (TestTube<ItemColor> aTube : this) {
            //System.out.print(aTube.toString() + "\t\t");
            System.out.print("[");
            if (!aTube.empty()) {
                for (int i = aTube.size() - 1; i > 0; i--) {
                    System.out.print(aTube.elementAt(i));
                    System.out.print(", ");
                }
                System.out.print(aTube.elementAt(0));
            }
            System.out.println("]");
        }
    }

    // A rack is sorted when every tube is full and has the same content within itself, or is empty.
    boolean sorted() {
        for (TestTube<ItemColor> aTube : this) {
            if (aTube.empty()) continue; // Do not ever check to see if an empty tube is sorted.
            if (!aTube.sorted()) return false;
        }
        return true;
    }

}
