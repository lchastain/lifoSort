// A Possibility is a possible(legal) move that may be performed in a given tableau/TubeRack.

public class PossibleMove {
    ItemColor theItem;

    // For these values, we are using array indexes to indicate which Tube in a Rack, but we
    //   use the term 'slot' to indicate the position of an item inside a tube, considering the
    //   item at the 'top' of a full tube to be in slot #1, and going up in value from there as you
    //   go further down into the tube.  The slot numbers are tied to positions of the items in the
    //   tube and do not depend on how many items are in the tube, so non-full tubes will not have
    //   an item in slot 1, and higher number slots of non-full tubes may also be empty.
    int fromTubeIndex;
    int fromSlot;
    int toTubeIndex;
    int toSlot;

    boolean explored;     // Has this move been 'made', or not?
    // If the move were to be 'made', it would result in a new tableau.     // MOVE THIS TO WHERE A NEW TUBERACK IS CREATED.
    //  and the corresponding TubeRack will have a whole new list of possible moves.

    PossibleMove(ItemColor theItem, int fromTubeIndex, int fromSlot, int toTubeIndex, int toSlot) {
        this.theItem = theItem;
        this.fromTubeIndex = fromTubeIndex;
        this.fromSlot = fromSlot;
        this.toTubeIndex = toTubeIndex;
        this.toSlot = toSlot;
    }

    @Override
    public String toString() {
        String theAnsr;
        theAnsr = theItem + " from tube " + (fromTubeIndex+1) + " (slot " + fromSlot +
                ") to tube " + (toTubeIndex+1) + " (slot " + toSlot + ")";
        return theAnsr;
    }
}
