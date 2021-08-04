import java.util.ArrayList;

// Models a 'rack' of TestTubes
public class TubeRack extends ArrayList<TestTube<ItemColor>> {
    int tubeCapacity;
    int tubeCount;
    //    Integer[][] initialRackContent;
    ItemColor[][] initialRackContent;


    public TubeRack(ItemColor[][] initialRackContent) {
        super();
        this.initialRackContent = initialRackContent;
        this.tubeCount = initialRackContent.length;
        this.tubeCapacity = initialRackContent[0].length;
    }

    // The logic prior to this method being called is believed to have avoided any situation that would be disallowed
    //   by the program rules so that at this point only a successful move is expected.
    //   So - no need to check for content before doing a pop(), or worry that we would exceed capacity from a push().
    boolean doMove(TestTube<ItemColor> aTube, int destTubeIndex) {
        TestTube<ItemColor> sourceTube = get(LifoSort.sourceTubeIndex);
        sourceTube.pop(); // We already have the value; this is just to get it off the stack.
        ItemColor theResult = aTube.push(LifoSort.sourceValue);
        if (theResult == null) { // This is what we do NOT expect, but handling it nevertheless.
            System.out.println("The move failed!");
            return false;
        }
        int destTubePosition = (tubeCapacity + 1) - get(destTubeIndex).size();
        String theMove = "From tube " + (LifoSort.sourceTubeIndex + 1) +
                ", moved " + LifoSort.sourceValue + " (#" +
                ((tubeCapacity + 1) - (sourceTube.size() + 1)) +
                ") to tube " + (destTubeIndex + 1) + " (#" +
                destTubePosition + ")";
        if(get(destTubeIndex).sorted()) theMove += " - Sorted!";
        LifoSort.currentRun.add(theMove);

        // After a successful move, the source tube index must be reset.  In some cases it will
        // already be at zero, but it's more efficient to just set it whether it needs it or not.
        LifoSort.sourceTubeIndex = 0;

        return true;
    }

    // This method gives us the starting point, to which we will need to return after every
    //   iteration of the recursive solution algorithm.
    void loadRack() {
        // Set (or reset) the content of each tube -
        TestTube<ItemColor> tmpTube;
//        for (int i = 0; i < tubeCount; i++) {
//            tmpTube = new TestTube<>(tubeCapacity);
//            ItemColor[] tubeContent = initialRackContent[i];
//            tmpTube.setContent(tubeContent);
//            add(tmpTube);
//        }
        for(ItemColor[] tubeContent: initialRackContent) {
            tmpTube = new TestTube<>(tubeCapacity);
            tmpTube.setContent(tubeContent);
            add(tmpTube);
        }
    } // end loadRack

    boolean moveOne() {
        for (int i = 0; i < tubeCount; i++) { // We need the index number as well as the iteration functionality.
            if (i == LifoSort.sourceTubeIndex) continue;  // Cannot move an item to the same tube that it is coming from.
            TestTube<ItemColor> aTube = get(i);
            if (aTube.full()) continue;
            if (aTube.empty()) {
                return doMove(aTube, i);  // We only expect to see a 'true' here.
            } else {
                ItemColor nextValue = aTube.peek();
                if (LifoSort.sourceValue.equals(nextValue)) {
                    return doMove(aTube, i);  // We only expect to see a 'true' here.
                } else {
                    // We don't need a 'continue' from here, because we are already at the bottom of the 'for' loop.
                    // But what it means is - the candidate destination tube was neither full nor empty, but it did
                    // not have as its top value, the same value that we want to push.
                    // Therefore it is disqualified as a potential place to put the sourceValue.
                    // The only reason we have an 'else' at all is for debug/troubleshooting.  When the
                    // app is fully functional, this section may be disabled.
                    String theMove = "From tube " + (LifoSort.sourceTubeIndex + 1) +
                            ", did not move " + LifoSort.sourceValue + " (#" +
                            ((tubeCapacity + 1) - get(LifoSort.sourceTubeIndex).size()) +
                            ") to tube " + (i + 1) + " because it contains " + nextValue + " in its #" +
                            ((tubeCapacity + 1) - get(i).size()) + " position.";
                    System.out.println(theMove);
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
    boolean sorted() {
        for (TestTube<ItemColor> aTube : this) {
            if (aTube.empty()) continue; // Do not ever check to see if an empty tube is sorted.
            if (!aTube.sorted()) return false;
        }
        return true;
    }

}
