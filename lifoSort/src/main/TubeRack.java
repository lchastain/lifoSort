import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;

// Models a 'rack' of TestTubes with contents
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

    public TubeRack(ItemColor[][] rackContent, ArrayList<String> movesUpToThisTableau) {
        super();
        this.rackContent = rackContent;
        this.tubeCount = rackContent.length;
        this.tubeCapacity = rackContent[0].length;
        loadRack();
        previousMoves = new ArrayList<>(movesUpToThisTableau);
        possibleMoves = new ArrayList<>();
        findPossibilities(); // The very first tableau will always have (tubeCount-2) possibilities.

        rackNumber = 1;
        // Right now we are not yet fully constructed (but will be, eventually).
        // The point is, this rack has not yet been added to the list, so when calculating
        //    what our rack number will be, we need to take one more than the current size
        //    of the rack list.
        if (LifoSort.rackList != null) rackNumber = LifoSort.rackList.size() + 1;

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

        String theMove = "From tube " + sourceTube.tubeNumber +
                ", moved " + sourceValue + " (#" +
                ((tubeCapacity + 1) - (sourceTube.size())) +
                ") to tube " + aTube.tubeNumber + " (#" +
                (tubeCapacity - aTube.size()) + ")";

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

    void explorePossibilities() {
        // loop thru the possibleMoves list
        showRack();
        System.out.println();
        System.out.println("Looking at each of " + possibleMoves.size() + " possible moves.");

        for (PossibleMove possibleMove : possibleMoves) {
            // This call is not needed to explore the first possibility, but it does no
            //   harm and it is definitely needed as a reset before moving on to later ones.
            loadRack();

            System.out.println("Considering: " + possibleMove);
            sourceValue = possibleMove.theItem;
            sourceTubeIndex = possibleMove.fromTubeIndex;
            explorePossibility();
        }


    }

    // This method takes a possible move and then explores follow-on moves in a depth-first approach.
    void explorePossibility() {
        int numberOfMoves = previousMoves.size();
        currentRun = new ArrayList<>(previousMoves);
        while (!sorted()) {  // Either we find a solution or bail out after determining that the possibility does not pan out.
            if (moveOne()) { // If we did move one, that move was reported by popAndPush()
                // Keep track of how many moves are in the current possibility -
                numberOfMoves++; // vs getting the current size of the currentRun list.

//                // Print out the move we just made -
//                System.out.println(currentRun.get(numberOfMoves - 1)); // From tube x, moved <color & slot> to tube y <slot>

//                System.out.println("Resulting in a new rack: ");
//                showRack();
//                System.out.println("------- after " + numberOfMoves + " moves --------------\n");

                // Get the new state of the rack, to (possibly) be used to create a new rack that starts at this point.
                ItemColor[][] newTableau = getCurrentTableau();
//                System.out.println("The new tableau: ");
//                LifoSort.showTableau(newTableau);

                // Now, before we add a new TubeRack to our worklist, verify that we don't already have one
                //   with the exact same tableau already in the list.
                boolean beenThere = false;
                // The 'beenThere' var name is more accurate than 'doneThat'; it may already be on the list but not yet processed.
                for (TubeRack tubeRack : LifoSort.rackList) {
                    if (Arrays.deepEquals(newTableau, tubeRack.rackContent)) {
                        beenThere = true;
                        break;
                    }
                }
                if (!beenThere) {
//                    ArrayList<String> compositeListOfMoves = new ArrayList<>(previousMoves);
//                    compositeListOfMoves.addAll(currentRun);
                    LifoSort.rackList.add(new TubeRack(newTableau, currentRun));
                }

            } else {
                sourceTubeIndex++;
            }

            if (sourceTubeIndex + 1 >= tubeCount) {
                // we need to reset the rack and explore the next possibility.

                showMoves();
                System.out.println("This possibility sequence has no further available moves.\n");
                return;
            } else {
                try {
                    sourceValue = get(sourceTubeIndex).peek();
                } catch (EmptyStackException ese) { // We tried to look at a value from an empty tube; ignore and go on.
                    //ese.printStackTrace();
                    return;
                }
            }
        } // end while
        showMoves();
        System.out.println("The rack is completely sorted!");
        System.out.println("Started: " + LifoSort.localDateTime);
        System.out.println("Ended: " + LocalDateTime.now());
        System.exit(0);  // All Sorted!
    } // end explorePossibility

    private void showMoves() {
        // Print out what we did to get to this point -
        int moveNum = 1;
        for (String s : currentRun) {
            System.out.print(moveNum++ + ".  ");
            System.out.println(s);
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
                if (destTube.empty()) {
                    // Create a new PossibleMove and add it to the list.
                    // But first verify that we don't already have a move of this item to an empty tube, because there is
                    // no difference when two different tube destinations are both 'empty'; disallow a second one.
                    if (emptyTubeIndex < 0) emptyTubeIndex = j; // Take the first empty.  The other one will be ignored.
                    if (j == emptyTubeIndex) { // Otherwise it is some other empty tube
                        // Ideally there would never be more than two empties, anyway.
                        PossibleMove possibleMove = new PossibleMove(sourceItem, i, fromSlot, j, tubeCapacity);
                        possibleMoves.add(possibleMove);
                    }
                } else {
                    ItemColor nextItem = destTube.peek();
                    if (sourceItem.equals(nextItem)) { // Create a new PossibleMove and add it to the list.
                        // This is not the same math as after an actual move; this move is only being noted at this point.
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

    // This method will either make one single move, or return a false.  It does not consult the list of possible
    //   moves because it may be looking 'deeper' than the top level and the first move is the only one that is in the
    //   possibles for this rack.
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
//                    String theMove = "Did not move " + sourceValue + " (#" +
//                            ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
//                            " from tube " + (sourceTubeIndex + 1) +
//                            ") to tube " + (i + 1) + " because the source tube is homogenous " +
//                            "and the destination tube is empty.";
//                    System.out.println(theMove);
                    return false;
                }
            } else {
                ItemColor nextValue = aTube.peek();
                if (sourceValue.equals(nextValue)) {  // Otherwise it is a disallowed move.

                    // When both source and destination tubes are homogenous (and we already know they are the same
                    // color), disallow the move from the greater content tube to the lesser content tube.
                    if (sourceTube.homogenous() && aTube.homogenous()) {
                        if (sourceTube.size() > aTube.size()) {
                            String theMove = "Did not move " + sourceValue + " (#" +
                                    ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
                                    " from tube " + (sourceTubeIndex + 1) +
                                    ") to tube " + (i + 1) +
                                    " because the directionality is illogical.  The reverse of this move will be allowed. ";
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
//                        String theMove = "Did not move " + sourceValue + " (#" +
//                            ((tubeCapacity + 1) - get(sourceTubeIndex).size()) +
//                            " from tube " + (sourceTubeIndex + 1) +
//                            ") to tube " + (i + 1) + " because the destination tube does not have enough room for " +
//                            itemsToMove + " items.";
//                        System.out.println(theMove);
                        return false;
                    }
                    //-------------------------------------------------------------------------------------------

                    return popAndPush(aTube);
//                } else {
                    // We don't need a 'continue' from here, because we are already at the bottom of the 'for' loop.
                    // But what it means is - the candidate destination tube was neither full nor empty, but it did
                    // not have as its top value, the same value that we want to push.
                    // Therefore it is disqualified as a potential place to put the sourceValue.
                    // The only reason we have an 'else' at all is for debug/troubleshooting.  When the
                    // app is fully functional, this section may be disabled.
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

    // A rack is sorted when every tube has the same content within itself, or is empty.
    // No need to check if the non-empty ones are full; there are not enough available slots to allow that case and
    //   yet still show all tubes sorted.
    boolean sorted() {
        for (TestTube<ItemColor> aTube : this) {
            if (aTube.empty()) continue; // Do not ever check to see if an empty tube is sorted.
            if (!aTube.sorted()) return false;
        }
        return true;
    }

}
