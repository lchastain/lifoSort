import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class TestTube<ItemColor> extends Stack<ItemColor>
{
    int maxCapacity; // 0 or greater; typical is 4.
    // 12 colors per each of 4 slots in a tube = 12x12x12x12 (3456) possible content variants, per tube.

    int tubeNumber; // Where is this tube in terms of position in the Rack?  (all Tubes will be in a Rack).

    /** Creates an empty Stack. **/
    public TestTube(int maxCapacity, int tubeNumber)
    {
        this.maxCapacity = maxCapacity;
        this.tubeNumber = tubeNumber;
    }

    private boolean canPush(ItemColor theItem) {
        if(this.empty()) return true;
        if(this.size() < maxCapacity) {
            ItemColor topItem = this.peek();
            return theItem.equals(topItem);
        }
        return false;
    }

    // Check to see if the tube is full.
    // We shouldn't need the 'greater than' part of the test, but it is the safer approach.
    boolean full() {
        return this.size() >= maxCapacity;
    }

    // Are all the items in this tube the same Color?
    boolean homogenous() {
        // The answer for an empty tube could probably go either way, but we'll say 'no' for an empty.
        if(empty()) return false; // Thinking that for empties, this would not ever be called anyway.

        ItemColor topValue = elementAt(0);
        for(int i=1; i<size(); i++) {
            if (topValue != elementAt(i)) return false;
        }
        return true;

    } // end homogenous


    // A potentially deeper 'peek' into the stack.
    //   depth is 1-based, with 1 at the top of the stack.
    ItemColor peek(int depth) {
        if(empty()) return peek(); // Let the base class throw the Exception.

        for(int i=this.size()-1; i>=0; i--) {
            if(this.size()-i == depth) {
                return elementAt(i);
            }
        }
        return null;
    }

    @Override
    public ItemColor push(ItemColor item) {
        if(canPush(item)) {
            return super.push(item);
        } else {
            return null;
        }
    }

    int getTubeItemCount(ItemColor[] theArray) {
        int theCount = 0;
        for (ItemColor itemColor : theArray) {
            if (itemColor != null) theCount++;
        }
        return theCount;
    }

    void setContent(ItemColor[] contentValues) {
        if(contentValues != null) {
            int theSize = getTubeItemCount(contentValues);
            if(theSize > maxCapacity) {
                try {
                    throw new Exception("TestTube initialization exceeds capacity!");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(contentValues.length);
                }
            }
            int maxIndex = theSize-1;

            for(int i=maxIndex; i>=0; i--) {
                if(contentValues[i] == null) break; // No data should follow a null.
                addElement(contentValues[i]);
            }
        }
    }

    boolean sorted() {
        if(size() < maxCapacity) return false; // Only a 'full' tube can be considered to be sorted.
        return homogenous();
    }

}