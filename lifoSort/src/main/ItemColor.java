
enum ItemColor {
    LIMEGREEN("Lime Green"),
    SKYBLUE("Sky Blue"),
    GREEN("Green"),
    PURPLE("Purple"),
    BROWN("Brown"),
    GRAY("Gray"),
    RED("Red"),
    ORANGE("Orange"),
    PALEGREEN("Pale Green"),
    YELLOW("Yellow"),
    PINK("Pink"),
    BLUE("Blue");

    private final String itemColor;

    ItemColor(String s) {
        itemColor = s;
    }

    // This is the name formatted for data storage (no spaces)
    String getItemColor() {
        return itemColor.replaceAll("\\s", "");
    }

    // This is the pretty-printed version of the item color.
    @Override
    public String toString() {
        return getItemColor();
    }

}
