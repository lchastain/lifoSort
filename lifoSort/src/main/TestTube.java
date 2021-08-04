import java.util.Stack;

public class TestTube<ItemColor> extends Stack<ItemColor>
{
    int maxCapacity; // 1 or greater; typical is 4.
    // 12 colors per each of 4 slots in a tube = 12x12x12x12 (3456) possible content variants, per tube.

    /** Creates an empty Stack. **/
    public TestTube(int capacity)
    {
        maxCapacity = capacity;
    }

    // Check to see if the tube is full.
    // We shouldn't need the 'greater than' part of the test, but it is the safer approach.
    boolean full() {
        return this.size() >= maxCapacity;
    }

    @Override
    public ItemColor push(ItemColor item) {
        if(canPush(item)) {
            return super.push(item);
        } else {
            return null;
        }
    }

    private boolean canPush(ItemColor theItem) {
        if(this.empty()) return true;
        if(this.size() < maxCapacity) {
            ItemColor topItem = this.peek();
            return theItem.equals(topItem);
        }
        return false;
    }

    void setContent(ItemColor[] contentValues) {
        if(contentValues != null) {
            int theLength = contentValues.length;
            if(theLength > maxCapacity) {
                try {
                    throw new Exception("TestTube initialization exceeds capacity!");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(contentValues.length);
                }
            }
            int maxIndex = theLength-1;

            for(int i=maxIndex; i>=0; i--) {
                if(contentValues[i] == null) break; // No data should follow a null.
                addElement(contentValues[i]);
//                addElement(contentValues[3]);
//                addElement(contentValues[2]);
//                addElement(contentValues[1]);
//                addElement(contentValues[0]);
            }
        }
    }

    boolean sorted() {
        if(size() < maxCapacity) return false; // Only a 'full' tube can be considered to be sorted.
        ItemColor topValue = elementAt(0);
        for(int i=1; i<maxCapacity; i++) {
            if (topValue != elementAt(i)) return false;
        }
        return true;
    }

}