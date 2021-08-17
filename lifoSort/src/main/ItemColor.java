import java.awt.*;

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
    private final Color theRGB;

    ItemColor(String s) {
        itemColor = s;
        switch (itemColor) {
            case "Lime Green":
                //   https://html-color.codes/green   Yellow-green
                theRGB = new Color(154, 205, 50);
                break;
            case "Sky Blue": // https://html-color.codes/blue   deepskyblue
                theRGB = new Color(0, 191, 255);
                break;
            case "Green": //   https://html-color.codes/green   Spanish Green
                theRGB = new Color(0, 145, 80);
                break;
            case "Purple": // https://html-color.codes/purple   darkviolet
                theRGB = new Color(148, 0, 211);
                break;
            case "Brown": // https://html-color.codes/brown   saddlebrown
                theRGB = new Color(139,69,19);
                break;
            case "Gray":
                theRGB = Color.GRAY;
                break;
            case "Red":
                theRGB = Color.RED;
                break;
            case "Orange": // https://html-color.codes/orange   darkorange
                theRGB = new Color(255, 140, 0);
                break;
            case "Pale Green": //   https://html-color.codes/green   springgreen
                theRGB = new Color(0,255,127);
                break;
            case "Yellow":
                theRGB = Color.YELLOW;
                break;
            case "Pink": //  https://html-color.codes/pink   Rose Bonbon
                theRGB = new Color(249,66,158);
                break;
            case "Blue":
                theRGB = Color.BLUE;
                break;
            default:
                theRGB = new Color(0,0,0); // Black
        }
    }

    // This is the name formatted for data storage (no spaces)
    String getItemColorName() {
        return itemColor.replaceAll("\\s", "");
    }

    Color getColor() {
        return theRGB;
    }

    // This is the pretty-printed version of the item color.
    @Override
    public String toString() {
        return getItemColorName();
    }

}
