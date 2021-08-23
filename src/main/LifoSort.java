import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

// This app models a set (rack) of test tubes containing colored balls,
//   and then proceeds to sort them according to color, with some specific sorting rules such as a ball may
//   only move to another tube that is either empty, or the top ball in the non-full destination tube is the same color.

public class LifoSort {
    static boolean debug;
    static boolean textOnly;
    static int tubeCount;
    static ArrayList<TubeRack> rackList;
    static TubeRack currentRack;
    static LocalDateTime localDateTime;

    static {
        // Defined in the java command, not the command-line arguments to the app.  Ex:   java -Ddebug LifoSort
        debug = (System.getProperty("debug") != null);

        if (debug) System.out.println("Debugging printouts on by Java startup flag.");
    } // end static


    // Description:  Prints the input parameter.
    //   Does a 'flush' so that statements are not printed out
    //   of order, when mixed with exceptions.
    public static void debug(String s) {
        if (debug) {
            System.out.println(s);
            System.out.flush();
        } // end if
    } // end debug


    private static ItemColor[][] setup() {
        // Running in debug mode can add ~1 minute for every 8 minutes of a non-debug run.
        // Times noted below are for non-debug runs, but the amount of console output also has an effect.
        //
//        return setup646();  // 13 seconds
//        return setup647();  //  7 minutes and 18 seconds
//        return setup648();  // 19 seconds
//        return setup649();  //  4 minutes and 5 seconds
        return setup651();  // 21 minutes and 30 seconds
//        return setup652();  // 31 seconds
//        return setup653();  // 37 Seconds
    }

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

        /* End of the run:
Exploring possibilities for rack # 6308 of 18111
Looking at each of 6 possible moves after this sequence:
1.  From tube 3, move Gray (#1) to tube 10 (#4)
2.  From tube 5, move Orange (#1) to tube 11 (#4)
3.  From tube 2, move LimeGreen (#1) to tube 5 (#1)
4.  From tube 8, move Orange (#1) to tube 11 (#3)
5.  From tube 8, move Gray (#2) to tube 10 (#3)
6.  From tube 4, move Blue (#1) to tube 8 (#2)
7.  From tube 9, move Blue (#1) to tube 8 (#1)
8.  From tube 4, move SkyBlue (#2) to tube 9 (#1)
9.  From tube 6, move PaleGreen (#1) to tube 4 (#2)
10.  From tube 3, move Red (#2) to tube 6 (#1)
11.  From tube 3, move Gray (#3) to tube 10 (#2)
12.  From tube 1, move Purple (#1) to tube 3 (#3)
13.  From tube 7, move Purple (#1) to tube 3 (#2)
14.  From tube 7, move Pink (#2) to tube 2 (#1)
15.  From tube 7, move Orange (#3) to tube 11 (#2)
16.  From tube 9, move SkyBlue (#1) to tube 7 (#3)
17.  From tube 9, move SkyBlue (#2) to tube 7 (#2)
18.  From tube 9, move Orange (#3) to tube 11 (#1) - Sorted!
19.  From tube 9, move Gray (#4) to tube 10 (#1) - Sorted!
20.  From tube 1, move Red (#2) to tube 9 (#4)
21.  From tube 1, move Purple (#3) to tube 3 (#1) - Sorted!
22.  From tube 1, move PaleGreen (#4) to tube 4 (#1)
23.  From tube 6, move Red (#1) to tube 1 (#4)
24.  From tube 6, move Red (#2) to tube 1 (#3)
25.  From tube 6, move SkyBlue (#3) to tube 7 (#1) - Sorted!
26.  From tube 5, move LimeGreen (#1) to tube 6 (#3)
27.  From tube 5, move LimeGreen (#2) to tube 6 (#2)
28.  From tube 5, move LimeGreen (#3) to tube 6 (#1) - Sorted!
29.  From tube 4, move PaleGreen (#1) to tube 5 (#3)
30.  From tube 4, move PaleGreen (#2) to tube 5 (#2)
31.  From tube 4, move PaleGreen (#3) to tube 5 (#1) - Sorted!
32.  From tube 4, move Red (#4) to tube 1 (#2)
Considering possible move #1 of 6: Red from tube 1 slot 2 to tube 4 slot 4
Trying 33.  From tube 2, move Pink (#1) to tube 4 (#4)
Attempted move #33 of rack #6308 results in a duplicate tableau of rack number 6309
Considering possible move #2 of 6: Red from tube 1 slot 2 to tube 9 slot 3
Trying 33.  From tube 2, move Pink (#1) to tube 4 (#4)
Attempted move #33 of rack #6308 results in a duplicate tableau of rack number 6309
Considering possible move #3 of 6: Pink from tube 2 slot 1 to tube 4 slot 4
Trying 33.  From tube 2, move Pink (#1) to tube 4 (#4)
Attempted move #33 of rack #6308 results in a duplicate tableau of rack number 6309
Considering possible move #4 of 6: Blue from tube 8 slot 1 to tube 4 slot 4
Trying 33.  From tube 8, move Blue (#1) to tube 4 (#4)
Trying 34.  From tube 4, move Blue (#4) to tube 8 (#1)
Attempted move #34 of rack #6308 results in a duplicate tableau of rack number 6308
Considering possible move #5 of 6: Red from tube 9 slot 4 to tube 1 slot 1
Trying 33.  From tube 9, move Red (#4) to tube 1 (#1) - Sorted!
Trying 34.  From tube 2, move Pink (#1) to tube 4 (#4)
Trying 35.  From tube 2, move Pink (#2) to tube 4 (#3)
Trying 36.  From tube 2, move Pink (#3) to tube 4 (#2)
Trying 37.  From tube 8, move Blue (#1) to tube 2 (#3)
Trying 38.  From tube 8, move Blue (#2) to tube 2 (#2)
Trying 39.  From tube 8, move Blue (#3) to tube 2 (#1) - Sorted!
Trying 40.  From tube 8, move Pink (#4) to tube 4 (#1) - Sorted!

The rack is completely sorted!
1.  From tube 3, move Gray (#1) to tube 10 (#4)
2.  From tube 5, move Orange (#1) to tube 11 (#4)
3.  From tube 2, move LimeGreen (#1) to tube 5 (#1)
4.  From tube 8, move Orange (#1) to tube 11 (#3)
5.  From tube 8, move Gray (#2) to tube 10 (#3)
6.  From tube 4, move Blue (#1) to tube 8 (#2)
7.  From tube 9, move Blue (#1) to tube 8 (#1)
8.  From tube 4, move SkyBlue (#2) to tube 9 (#1)
9.  From tube 6, move PaleGreen (#1) to tube 4 (#2)
10.  From tube 3, move Red (#2) to tube 6 (#1)
11.  From tube 3, move Gray (#3) to tube 10 (#2)
12.  From tube 1, move Purple (#1) to tube 3 (#3)
13.  From tube 7, move Purple (#1) to tube 3 (#2)
14.  From tube 7, move Pink (#2) to tube 2 (#1)
15.  From tube 7, move Orange (#3) to tube 11 (#2)
16.  From tube 9, move SkyBlue (#1) to tube 7 (#3)
17.  From tube 9, move SkyBlue (#2) to tube 7 (#2)
18.  From tube 9, move Orange (#3) to tube 11 (#1) - Sorted!
19.  From tube 9, move Gray (#4) to tube 10 (#1) - Sorted!
20.  From tube 1, move Red (#2) to tube 9 (#4)
21.  From tube 1, move Purple (#3) to tube 3 (#1) - Sorted!
22.  From tube 1, move PaleGreen (#4) to tube 4 (#1)
23.  From tube 6, move Red (#1) to tube 1 (#4)
24.  From tube 6, move Red (#2) to tube 1 (#3)
25.  From tube 6, move SkyBlue (#3) to tube 7 (#1) - Sorted!
26.  From tube 5, move LimeGreen (#1) to tube 6 (#3)
27.  From tube 5, move LimeGreen (#2) to tube 6 (#2)
28.  From tube 5, move LimeGreen (#3) to tube 6 (#1) - Sorted!
29.  From tube 4, move PaleGreen (#1) to tube 5 (#3)
30.  From tube 4, move PaleGreen (#2) to tube 5 (#2)
31.  From tube 4, move PaleGreen (#3) to tube 5 (#1) - Sorted!
32.  From tube 4, move Red (#4) to tube 1 (#2)
33.  From tube 9, move Red (#4) to tube 1 (#1) - Sorted!
34.  From tube 2, move Pink (#1) to tube 4 (#4)
35.  From tube 2, move Pink (#2) to tube 4 (#3)
36.  From tube 2, move Pink (#3) to tube 4 (#2)
37.  From tube 8, move Blue (#1) to tube 2 (#3)
38.  From tube 8, move Blue (#2) to tube 2 (#2)
39.  From tube 8, move Blue (#3) to tube 2 (#1) - Sorted!
40.  From tube 8, move Pink (#4) to tube 4 (#1) - Sorted!
Started: 2021-08-18T09:52:30.314722100
Ended: 2021-08-18T09:52:44.266467500
Elapsed: 13 seconds.

         */
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
        /*
        End of the run:
Exploring possibilities for rack # 46592 of 61396
Looking at each of 3 possible moves after this sequence:
1.  From tube 5, move Yellow (#1) to tube 13 (#4)
2.  From tube 12, move PaleGreen (#1) to tube 14 (#4)
3.  From tube 12, move Pink (#2) to tube 5 (#1)
4.  From tube 3, move PaleGreen (#1) to tube 12 (#2)
5.  From tube 7, move Yellow (#1) to tube 13 (#3)
6.  From tube 2, move Brown (#1) to tube 7 (#1)
7.  From tube 2, move Gray (#2) to tube 3 (#1)
8.  From tube 12, move PaleGreen (#2) to tube 14 (#3)
9.  From tube 12, move PaleGreen (#3) to tube 14 (#2)
10.  From tube 12, move Red (#4) to tube 2 (#2)
11.  From tube 5, move Pink (#1) to tube 12 (#4)
12.  From tube 5, move Pink (#2) to tube 12 (#3)
13.  From tube 9, move Pink (#1) to tube 12 (#2)
14.  From tube 9, move Blue (#2) to tube 5 (#2)
15.  From tube 9, move Yellow (#3) to tube 13 (#2)
16.  From tube 3, move Gray (#1) to tube 9 (#3)
17.  From tube 3, move Gray (#2) to tube 9 (#2)
18.  From tube 3, move Gray (#3) to tube 9 (#1) - Sorted!
19.  From tube 8, move Purple (#1) to tube 3 (#3)
20.  From tube 10, move Purple (#1) to tube 3 (#2)
21.  From tube 10, move Red (#2) to tube 2 (#1)
22.  From tube 10, move PaleGreen (#3) to tube 14 (#1) - Sorted!
23.  From tube 1, move LimeGreen (#1) to tube 10 (#3)
24.  From tube 11, move SkyBlue (#1) to tube 1 (#1)
25.  From tube 11, move Yellow (#2) to tube 13 (#1) - Sorted!
26.  From tube 11, move Blue (#3) to tube 5 (#1)
27.  From tube 4, move Brown (#1) to tube 11 (#3)
28.  From tube 8, move Green (#2) to tube 4 (#1)
29.  From tube 7, move Brown (#1) to tube 11 (#2)
30.  From tube 7, move Brown (#2) to tube 11 (#1) - Sorted!
31.  From tube 8, move LimeGreen (#3) to tube 10 (#2)
32.  From tube 8, move Pink (#4) to tube 12 (#1) - Sorted!
33.  From tube 5, move Blue (#1) to tube 8 (#4)
34.  From tube 5, move Blue (#2) to tube 8 (#3)
35.  From tube 5, move Blue (#3) to tube 8 (#2)
36.  From tube 5, move LimeGreen (#4) to tube 10 (#1) - Sorted!
37.  From tube 1, move SkyBlue (#1) to tube 5 (#4)
38.  From tube 1, move SkyBlue (#2) to tube 5 (#3)
39.  From tube 4, move Green (#1) to tube 1 (#2)
40.  From tube 4, move Green (#2) to tube 1 (#1)
41.  From tube 6, move SkyBlue (#1) to tube 5 (#2)
42.  From tube 6, move Blue (#2) to tube 8 (#1) - Sorted!
43.  From tube 6, move SkyBlue (#3) to tube 5 (#1) - Sorted!
44.  From tube 6, move Orange (#4) to tube 4 (#2)
45.  From tube 1, move Green (#1) to tube 6 (#4)
46.  From tube 1, move Green (#2) to tube 6 (#3)
47.  From tube 1, move Green (#3) to tube 6 (#2)
48.  From tube 1, move Purple (#4) to tube 3 (#1) - Sorted!
49.  From tube 7, move Red (#3) to tube 1 (#4)
Considering possible move #1 of 3: Red from tube 2 slot 1 to tube 1 slot 3
Trying 50.  From tube 2, move Red (#1) to tube 1 (#3)
Attempted move #50 of rack #46592 results in a duplicate tableau of rack number 46593
Considering possible move #2 of 3: Green from tube 6 slot 2 to tube 7 slot 3
Trying 50.  From tube 7, move Green (#4) to tube 6 (#1) - Sorted!
Trying 51.  From tube 2, move Red (#1) to tube 1 (#3)
Trying 52.  From tube 2, move Red (#2) to tube 1 (#2)
Trying 53.  From tube 2, move Red (#3) to tube 1 (#1) - Sorted!
Trying 54.  From tube 2, move Orange (#4) to tube 4 (#1) - Sorted!

The rack is completely sorted!
1.  From tube 5, move Yellow (#1) to tube 13 (#4)
2.  From tube 12, move PaleGreen (#1) to tube 14 (#4)
3.  From tube 12, move Pink (#2) to tube 5 (#1)
4.  From tube 3, move PaleGreen (#1) to tube 12 (#2)
5.  From tube 7, move Yellow (#1) to tube 13 (#3)
6.  From tube 2, move Brown (#1) to tube 7 (#1)
7.  From tube 2, move Gray (#2) to tube 3 (#1)
8.  From tube 12, move PaleGreen (#2) to tube 14 (#3)
9.  From tube 12, move PaleGreen (#3) to tube 14 (#2)
10.  From tube 12, move Red (#4) to tube 2 (#2)
11.  From tube 5, move Pink (#1) to tube 12 (#4)
12.  From tube 5, move Pink (#2) to tube 12 (#3)
13.  From tube 9, move Pink (#1) to tube 12 (#2)
14.  From tube 9, move Blue (#2) to tube 5 (#2)
15.  From tube 9, move Yellow (#3) to tube 13 (#2)
16.  From tube 3, move Gray (#1) to tube 9 (#3)
17.  From tube 3, move Gray (#2) to tube 9 (#2)
18.  From tube 3, move Gray (#3) to tube 9 (#1) - Sorted!
19.  From tube 8, move Purple (#1) to tube 3 (#3)
20.  From tube 10, move Purple (#1) to tube 3 (#2)
21.  From tube 10, move Red (#2) to tube 2 (#1)
22.  From tube 10, move PaleGreen (#3) to tube 14 (#1) - Sorted!
23.  From tube 1, move LimeGreen (#1) to tube 10 (#3)
24.  From tube 11, move SkyBlue (#1) to tube 1 (#1)
25.  From tube 11, move Yellow (#2) to tube 13 (#1) - Sorted!
26.  From tube 11, move Blue (#3) to tube 5 (#1)
27.  From tube 4, move Brown (#1) to tube 11 (#3)
28.  From tube 8, move Green (#2) to tube 4 (#1)
29.  From tube 7, move Brown (#1) to tube 11 (#2)
30.  From tube 7, move Brown (#2) to tube 11 (#1) - Sorted!
31.  From tube 8, move LimeGreen (#3) to tube 10 (#2)
32.  From tube 8, move Pink (#4) to tube 12 (#1) - Sorted!
33.  From tube 5, move Blue (#1) to tube 8 (#4)
34.  From tube 5, move Blue (#2) to tube 8 (#3)
35.  From tube 5, move Blue (#3) to tube 8 (#2)
36.  From tube 5, move LimeGreen (#4) to tube 10 (#1) - Sorted!
37.  From tube 1, move SkyBlue (#1) to tube 5 (#4)
38.  From tube 1, move SkyBlue (#2) to tube 5 (#3)
39.  From tube 4, move Green (#1) to tube 1 (#2)
40.  From tube 4, move Green (#2) to tube 1 (#1)
41.  From tube 6, move SkyBlue (#1) to tube 5 (#2)
42.  From tube 6, move Blue (#2) to tube 8 (#1) - Sorted!
43.  From tube 6, move SkyBlue (#3) to tube 5 (#1) - Sorted!
44.  From tube 6, move Orange (#4) to tube 4 (#2)
45.  From tube 1, move Green (#1) to tube 6 (#4)
46.  From tube 1, move Green (#2) to tube 6 (#3)
47.  From tube 1, move Green (#3) to tube 6 (#2)
48.  From tube 1, move Purple (#4) to tube 3 (#1) - Sorted!
49.  From tube 7, move Red (#3) to tube 1 (#4)
50.  From tube 7, move Green (#4) to tube 6 (#1) - Sorted!
51.  From tube 2, move Red (#1) to tube 1 (#3)
52.  From tube 2, move Red (#2) to tube 1 (#2)
53.  From tube 2, move Red (#3) to tube 1 (#1) - Sorted!
54.  From tube 2, move Orange (#4) to tube 4 (#1) - Sorted!
Started: 2021-08-18T09:56:02.366879100
Ended: 2021-08-18T10:04:07.169477800
Elapsed: 8 and 4 seconds.

         */
    }

    private static ItemColor[][] setup648() {
        ItemColor[][] initialTableau = new ItemColor[11][4];
        initialTableau[0] = new ItemColor[]{ItemColor.PURPLE, ItemColor.PALEGREEN, ItemColor.LIMEGREEN, ItemColor.BLUE};
        initialTableau[1] = new ItemColor[]{ItemColor.PINK, ItemColor.PINK, ItemColor.RED, ItemColor.LIMEGREEN};
        initialTableau[2] = new ItemColor[]{ItemColor.BLUE, ItemColor.PALEGREEN, ItemColor.BLUE, ItemColor.SKYBLUE};
        initialTableau[3] = new ItemColor[]{ItemColor.GRAY, ItemColor.BLUE, ItemColor.RED, ItemColor.PINK};
        initialTableau[4] = new ItemColor[]{ItemColor.GRAY, ItemColor.ORANGE, ItemColor.SKYBLUE, ItemColor.PALEGREEN};
        initialTableau[5] = new ItemColor[]{ItemColor.ORANGE, ItemColor.PALEGREEN, ItemColor.ORANGE, ItemColor.RED};
        initialTableau[6] = new ItemColor[]{ItemColor.PINK, ItemColor.GRAY, ItemColor.PURPLE, ItemColor.PURPLE};
        initialTableau[7] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.LIMEGREEN, ItemColor.PURPLE, ItemColor.RED};
        initialTableau[8] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.ORANGE, ItemColor.SKYBLUE, ItemColor.GRAY};
        initialTableau[9] = new ItemColor[4];
        initialTableau[10] = new ItemColor[4];

        return initialTableau;
    }

    private static ItemColor[][] setup649() { // 4 minutes and 29 seconds
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

    private static ItemColor[][] setup651() {  // 24 minutes and 16 seconds
        ItemColor[][] initialTableau = new ItemColor[14][4];
        initialTableau[0] = new ItemColor[]{ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.ORANGE, ItemColor.RED};
        initialTableau[1] = new ItemColor[]{ItemColor.GRAY, ItemColor.GREEN, ItemColor.PALEGREEN, ItemColor.SKYBLUE};
        initialTableau[2] = new ItemColor[]{ItemColor.RED, ItemColor.SKYBLUE, ItemColor.PALEGREEN, ItemColor.ORANGE};
        initialTableau[3] = new ItemColor[]{ItemColor.PINK, ItemColor.BROWN, ItemColor.BLUE, ItemColor.PINK};
        initialTableau[4] = new ItemColor[]{ItemColor.PURPLE, ItemColor.PURPLE, ItemColor.BLUE, ItemColor.PALEGREEN};
        initialTableau[5] = new ItemColor[]{ItemColor.BROWN, ItemColor.PALEGREEN, ItemColor.PINK, ItemColor.PURPLE};
        initialTableau[6] = new ItemColor[]{ItemColor.YELLOW, ItemColor.YELLOW, ItemColor.BROWN, ItemColor.GREEN};
        initialTableau[7] = new ItemColor[]{ItemColor.RED, ItemColor.LIMEGREEN, ItemColor.LIMEGREEN, ItemColor.GRAY};
        initialTableau[8] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.ORANGE, ItemColor.RED, ItemColor.SKYBLUE};
        initialTableau[9] = new ItemColor[]{ItemColor.PINK, ItemColor.BROWN, ItemColor.GREEN, ItemColor.GRAY};
        initialTableau[10] = new ItemColor[]{ItemColor.PURPLE, ItemColor.LIMEGREEN, ItemColor.YELLOW, ItemColor.BLUE};
        initialTableau[11] = new ItemColor[]{ItemColor.ORANGE, ItemColor.GRAY, ItemColor.YELLOW, ItemColor.GREEN};
        initialTableau[12] = new ItemColor[4];
        initialTableau[13] = new ItemColor[4];

        return initialTableau;

        /* The end of the run:
Exploring possibilities for rack # 60264 of 135089
Looking at each of 4 possible moves after this sequence:
1.  Move Pink from tube 10 slot 1 to tube 13 slot 4)
2.  Move Blue from tube 1 slot 1 to tube 14 slot 4)
3.  Move Pink from tube 4 slot 1 to tube 13 slot 3)
4.  Move Brown from tube 4 slot 2 to tube 10 slot 1)
5.  Move Blue from tube 4 slot 3 to tube 14 slot 3)
6.  Move Pink from tube 4 slot 4 to tube 13 slot 2)
7.  Move Red from tube 3 slot 1 to tube 4 slot 4)
8.  Move SkyBlue from tube 1 slot 2 to tube 3 slot 1)
9.  Move Orange from tube 12 slot 1 to tube 1 slot 2)
10.  Move Gray from tube 2 slot 1 to tube 12 slot 1)
11.  Move Red from tube 8 slot 1 to tube 4 slot 3)
12.  Move LimeGreen from tube 9 slot 1 to tube 8 slot 1)
13.  Move Orange from tube 9 slot 2 to tube 1 slot 1)
14.  Move Red from tube 4 slot 3 to tube 9 slot 2)
15.  Move Red from tube 4 slot 4 to tube 9 slot 1)
16.  Move Brown from tube 6 slot 1 to tube 4 slot 4)
17.  Move Brown from tube 10 slot 1 to tube 4 slot 3)
18.  Move Brown from tube 10 slot 2 to tube 4 slot 2)
19.  Move Green from tube 10 slot 3 to tube 2 slot 1)
20.  Move Gray from tube 12 slot 1 to tube 10 slot 3)
21.  Move Gray from tube 12 slot 2 to tube 10 slot 2)
22.  Move Yellow from tube 7 slot 1 to tube 12 slot 2)
23.  Move Yellow from tube 7 slot 2 to tube 12 slot 1)
24.  Move Brown from tube 7 slot 3 to tube 4 slot 1) - Sorted!
25.  Move Green from tube 2 slot 1 to tube 7 slot 3)
26.  Move Green from tube 2 slot 2 to tube 7 slot 2)
27.  Move PaleGreen from tube 2 slot 3 to tube 6 slot 1)
28.  Move SkyBlue from tube 3 slot 1 to tube 2 slot 3)
29.  Move SkyBlue from tube 3 slot 2 to tube 2 slot 2)
30.  Move PaleGreen from tube 6 slot 1 to tube 3 slot 2)
31.  Move PaleGreen from tube 6 slot 2 to tube 3 slot 1)
32.  Move Pink from tube 6 slot 3 to tube 13 slot 1) - Sorted!
33.  Move Purple from tube 5 slot 1 to tube 6 slot 3)
34.  Move Purple from tube 5 slot 2 to tube 6 slot 2)
35.  Move Blue from tube 5 slot 3 to tube 14 slot 2)
36.  Move PaleGreen from tube 3 slot 1 to tube 5 slot 3)
37.  Move PaleGreen from tube 3 slot 2 to tube 5 slot 2)
38.  Move PaleGreen from tube 3 slot 3 to tube 5 slot 1) - Sorted!
39.  Move Orange from tube 1 slot 1 to tube 3 slot 3)
40.  Move Orange from tube 1 slot 2 to tube 3 slot 2)
41.  Move Orange from tube 1 slot 3 to tube 3 slot 1) - Sorted!
42.  Move Red from tube 9 slot 1 to tube 1 slot 3)
43.  Move Red from tube 9 slot 2 to tube 1 slot 2)
44.  Move Red from tube 9 slot 3 to tube 1 slot 1) - Sorted!
45.  Move SkyBlue from tube 9 slot 4 to tube 2 slot 1) - Sorted!
46.  Move LimeGreen from tube 8 slot 1 to tube 9 slot 4)
47.  Move LimeGreen from tube 8 slot 2 to tube 9 slot 3)
48.  Move LimeGreen from tube 8 slot 3 to tube 9 slot 2)
49.  Move Gray from tube 8 slot 4 to tube 10 slot 1) - Sorted!
50.  Move Yellow from tube 12 slot 1 to tube 8 slot 4)
51.  Move Yellow from tube 12 slot 2 to tube 8 slot 3)
52.  Move Yellow from tube 12 slot 3 to tube 8 slot 2)
53.  Move Purple from tube 11 slot 1 to tube 6 slot 1) - Sorted!
54.  Move LimeGreen from tube 11 slot 2 to tube 9 slot 1) - Sorted!
55.  Move Yellow from tube 11 slot 3 to tube 8 slot 1) - Sorted!
56.  Move Blue from tube 11 slot 4 to tube 14 slot 1) - Sorted!
Considering possible move #1 of 4: Green from tube 7 slot 2 to tube 11 slot 4
  did not move Green (#2 from tube 7) to tube 11 because the source tube is homogenous and the destination tube is empty.
  did not move Yellow (#1 from tube 8) to tube 11 because the source tube is homogenous and the destination tube is empty.
  did not move LimeGreen (#1 from tube 9) to tube 11 because the source tube is homogenous and the destination tube is empty.
  did not move Gray (#1 from tube 10) to tube 11 because the source tube is homogenous and the destination tube is empty.
Considering possible move #2 of 4: Green from tube 7 slot 2 to tube 12 slot 3
  did not move Green (#2 from tube 7) to tube 11 because the source tube is homogenous and the destination tube is empty.
  did not move Yellow (#1 from tube 8) to tube 11 because the source tube is homogenous and the destination tube is empty.
  did not move LimeGreen (#1 from tube 9) to tube 11 because the source tube is homogenous and the destination tube is empty.
  did not move Gray (#1 from tube 10) to tube 11 because the source tube is homogenous and the destination tube is empty.
Considering possible move #3 of 4: Green from tube 12 slot 4 to tube 7 slot 1
Trying 57.  Move Green from tube 12 slot 4 to tube 7 slot 1) - Sorted!

The rack is completely sorted!
1.  Move Pink from tube 10 slot 1 to tube 13 slot 4)
2.  Move Blue from tube 1 slot 1 to tube 14 slot 4)
3.  Move Pink from tube 4 slot 1 to tube 13 slot 3)
4.  Move Brown from tube 4 slot 2 to tube 10 slot 1)
5.  Move Blue from tube 4 slot 3 to tube 14 slot 3)
6.  Move Pink from tube 4 slot 4 to tube 13 slot 2)
7.  Move Red from tube 3 slot 1 to tube 4 slot 4)
8.  Move SkyBlue from tube 1 slot 2 to tube 3 slot 1)
9.  Move Orange from tube 12 slot 1 to tube 1 slot 2)
10.  Move Gray from tube 2 slot 1 to tube 12 slot 1)
11.  Move Red from tube 8 slot 1 to tube 4 slot 3)
12.  Move LimeGreen from tube 9 slot 1 to tube 8 slot 1)
13.  Move Orange from tube 9 slot 2 to tube 1 slot 1)
14.  Move Red from tube 4 slot 3 to tube 9 slot 2)
15.  Move Red from tube 4 slot 4 to tube 9 slot 1)
16.  Move Brown from tube 6 slot 1 to tube 4 slot 4)
17.  Move Brown from tube 10 slot 1 to tube 4 slot 3)
18.  Move Brown from tube 10 slot 2 to tube 4 slot 2)
19.  Move Green from tube 10 slot 3 to tube 2 slot 1)
20.  Move Gray from tube 12 slot 1 to tube 10 slot 3)
21.  Move Gray from tube 12 slot 2 to tube 10 slot 2)
22.  Move Yellow from tube 7 slot 1 to tube 12 slot 2)
23.  Move Yellow from tube 7 slot 2 to tube 12 slot 1)
24.  Move Brown from tube 7 slot 3 to tube 4 slot 1) - Sorted!
25.  Move Green from tube 2 slot 1 to tube 7 slot 3)
26.  Move Green from tube 2 slot 2 to tube 7 slot 2)
27.  Move PaleGreen from tube 2 slot 3 to tube 6 slot 1)
28.  Move SkyBlue from tube 3 slot 1 to tube 2 slot 3)
29.  Move SkyBlue from tube 3 slot 2 to tube 2 slot 2)
30.  Move PaleGreen from tube 6 slot 1 to tube 3 slot 2)
31.  Move PaleGreen from tube 6 slot 2 to tube 3 slot 1)
32.  Move Pink from tube 6 slot 3 to tube 13 slot 1) - Sorted!
33.  Move Purple from tube 5 slot 1 to tube 6 slot 3)
34.  Move Purple from tube 5 slot 2 to tube 6 slot 2)
35.  Move Blue from tube 5 slot 3 to tube 14 slot 2)
36.  Move PaleGreen from tube 3 slot 1 to tube 5 slot 3)
37.  Move PaleGreen from tube 3 slot 2 to tube 5 slot 2)
38.  Move PaleGreen from tube 3 slot 3 to tube 5 slot 1) - Sorted!
39.  Move Orange from tube 1 slot 1 to tube 3 slot 3)
40.  Move Orange from tube 1 slot 2 to tube 3 slot 2)
41.  Move Orange from tube 1 slot 3 to tube 3 slot 1) - Sorted!
42.  Move Red from tube 9 slot 1 to tube 1 slot 3)
43.  Move Red from tube 9 slot 2 to tube 1 slot 2)
44.  Move Red from tube 9 slot 3 to tube 1 slot 1) - Sorted!
45.  Move SkyBlue from tube 9 slot 4 to tube 2 slot 1) - Sorted!
46.  Move LimeGreen from tube 8 slot 1 to tube 9 slot 4)
47.  Move LimeGreen from tube 8 slot 2 to tube 9 slot 3)
48.  Move LimeGreen from tube 8 slot 3 to tube 9 slot 2)
49.  Move Gray from tube 8 slot 4 to tube 10 slot 1) - Sorted!
50.  Move Yellow from tube 12 slot 1 to tube 8 slot 4)
51.  Move Yellow from tube 12 slot 2 to tube 8 slot 3)
52.  Move Yellow from tube 12 slot 3 to tube 8 slot 2)
53.  Move Purple from tube 11 slot 1 to tube 6 slot 1) - Sorted!
54.  Move LimeGreen from tube 11 slot 2 to tube 9 slot 1) - Sorted!
55.  Move Yellow from tube 11 slot 3 to tube 8 slot 1) - Sorted!
56.  Move Blue from tube 11 slot 4 to tube 14 slot 1) - Sorted!
57.  Move Green from tube 12 slot 4 to tube 7 slot 1) - Sorted!
Started: 2021-08-20T07:19:24.360097400
Ended: 2021-08-20T07:43:40.986506800
Elapsed: 24 minutes and 16 seconds.
         */
    }

    private static ItemColor[][] setup652() {
        ItemColor[][] initialTableau = new ItemColor[11][4];
        initialTableau[0] = new ItemColor[]{ItemColor.RED, ItemColor.PURPLE, ItemColor.SKYBLUE, ItemColor.SKYBLUE};
        initialTableau[1] = new ItemColor[]{ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.BLUE, ItemColor.PALEGREEN};
        initialTableau[2] = new ItemColor[]{ItemColor.PINK, ItemColor.PALEGREEN, ItemColor.ORANGE, ItemColor.LIMEGREEN};
        initialTableau[3] = new ItemColor[]{ItemColor.PINK, ItemColor.ORANGE, ItemColor.GRAY, ItemColor.GRAY};
        initialTableau[4] = new ItemColor[]{ItemColor.BLUE, ItemColor.BLUE, ItemColor.RED, ItemColor.RED};
        initialTableau[5] = new ItemColor[]{ItemColor.PINK, ItemColor.LIMEGREEN, ItemColor.GRAY, ItemColor.PURPLE};
        initialTableau[6] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.SKYBLUE, ItemColor.RED, ItemColor.PURPLE};
        initialTableau[7] = new ItemColor[]{ItemColor.ORANGE, ItemColor.PALEGREEN, ItemColor.PINK, ItemColor.ORANGE};
        initialTableau[8] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.GRAY, ItemColor.LIMEGREEN, ItemColor.PURPLE};
        initialTableau[9] = new ItemColor[4];
        initialTableau[10] = new ItemColor[4];

        return initialTableau;
        /* end of the run:
Exploring possibilities for rack # 10678 of 21542
Looking at each of 6 possible moves after this sequence:
1.  Move Blue from tube 2 slot 1 to tube 10 slot 4)
2.  Move Pink from tube 3 slot 1 to tube 11 slot 4)
3.  Move PaleGreen from tube 9 slot 1 to tube 3 slot 1)
4.  Move Pink from tube 4 slot 1 to tube 11 slot 3)
5.  Move Blue from tube 5 slot 1 to tube 10 slot 3)
6.  Move Blue from tube 5 slot 2 to tube 10 slot 2)
7.  Move Red from tube 1 slot 1 to tube 5 slot 2)
8.  Move Pink from tube 6 slot 1 to tube 11 slot 2)
9.  Move LimeGreen from tube 7 slot 1 to tube 6 slot 1)
10.  Move SkyBlue from tube 2 slot 2 to tube 7 slot 1)
11.  Move Blue from tube 2 slot 3 to tube 10 slot 1) - Sorted!
12.  Move PaleGreen from tube 3 slot 1 to tube 2 slot 3)
13.  Move Orange from tube 8 slot 1 to tube 4 slot 1)
14.  Move PaleGreen from tube 3 slot 2 to tube 2 slot 2)
15.  Move Orange from tube 4 slot 1 to tube 3 slot 2)
16.  Move Orange from tube 4 slot 2 to tube 3 slot 1)
17.  Move PaleGreen from tube 8 slot 2 to tube 2 slot 1) - Sorted!
18.  Move Pink from tube 8 slot 3 to tube 11 slot 1) - Sorted!
19.  Move Orange from tube 3 slot 1 to tube 8 slot 3)
20.  Move Orange from tube 3 slot 2 to tube 8 slot 2)
21.  Move Orange from tube 3 slot 3 to tube 8 slot 1) - Sorted!
22.  Move LimeGreen from tube 6 slot 1 to tube 3 slot 3)
23.  Move Gray from tube 9 slot 2 to tube 4 slot 2)
24.  Move LimeGreen from tube 6 slot 2 to tube 3 slot 2)
25.  Move Gray from tube 6 slot 3 to tube 4 slot 1) - Sorted!
26.  Move LimeGreen from tube 9 slot 3 to tube 3 slot 1) - Sorted!
Considering possible move #1 of 6: Purple from tube 1 slot 2 to tube 6 slot 3
Trying 27.  Move Purple from tube 1 slot 2 to tube 6 slot 3)
Attempted move #27 of rack #10678 results in a duplicate tableau of rack number 10679
Considering possible move #2 of 6: Purple from tube 1 slot 2 to tube 9 slot 3
Trying 27.  Move Purple from tube 1 slot 2 to tube 6 slot 3)
Attempted move #27 of rack #10678 results in a duplicate tableau of rack number 10679
Considering possible move #3 of 6: Purple from tube 6 slot 4 to tube 1 slot 1
Trying 27.  Move Purple from tube 6 slot 4 to tube 1 slot 1)
Attempted move #27 of rack #10678 results in a duplicate tableau of rack number 21542
Considering possible move #4 of 6: Purple from tube 6 slot 4 to tube 9 slot 3
Trying 27.  Move Purple from tube 6 slot 4 to tube 1 slot 1)
Attempted move #27 of rack #10678 results in a duplicate tableau of rack number 21542
Considering possible move #5 of 6: Purple from tube 9 slot 4 to tube 1 slot 1
Trying 27.  Move Purple from tube 9 slot 4 to tube 1 slot 1)
Trying 28.  Move Purple from tube 1 slot 1 to tube 6 slot 3)
Trying 29.  Move Purple from tube 1 slot 2 to tube 6 slot 2)
  did not move SkyBlue (#3 from tube 1) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move PaleGreen (#1 from tube 2) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move LimeGreen (#1 from tube 3) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Gray (#1 from tube 4) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Red (#2 from tube 5) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Purple (#2 from tube 6) to tube 9 because the source tube is homogenous and the destination tube is empty.
Trying 30.  Move SkyBlue from tube 7 slot 1 to tube 1 slot 2)
  did not move SkyBlue (#2 from tube 1) to tube 7 because the destination tube does not have enough room for 3 items.
  did not move PaleGreen (#1 from tube 2) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move LimeGreen (#1 from tube 3) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Gray (#1 from tube 4) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Red (#2 from tube 5) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Purple (#2 from tube 6) to tube 9 because the source tube is homogenous and the destination tube is empty.
Trying 31.  Move SkyBlue from tube 7 slot 2 to tube 1 slot 1) - Sorted!
  did not move SkyBlue (#1 from tube 1) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move PaleGreen (#1 from tube 2) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move LimeGreen (#1 from tube 3) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Gray (#1 from tube 4) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Red (#2 from tube 5) to tube 7 because the destination tube does not have enough room for 3 items.
  did not move Purple (#2 from tube 6) to tube 9 because the source tube is homogenous and the destination tube is empty.
Trying 32.  Move Red from tube 7 slot 3 to tube 5 slot 1) - Sorted!
  did not move SkyBlue (#1 from tube 1) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move PaleGreen (#1 from tube 2) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move LimeGreen (#1 from tube 3) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Gray (#1 from tube 4) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Red (#1 from tube 5) to tube 9 because the source tube is homogenous and the destination tube is empty.
  did not move Purple (#2 from tube 6) to tube 7 because the directionality is illogical.
Trying 33.  Move Purple from tube 7 slot 4 to tube 6 slot 1) - Sorted!

The rack is completely sorted!
1.  Move Blue from tube 2 slot 1 to tube 10 slot 4)
2.  Move Pink from tube 3 slot 1 to tube 11 slot 4)
3.  Move PaleGreen from tube 9 slot 1 to tube 3 slot 1)
4.  Move Pink from tube 4 slot 1 to tube 11 slot 3)
5.  Move Blue from tube 5 slot 1 to tube 10 slot 3)
6.  Move Blue from tube 5 slot 2 to tube 10 slot 2)
7.  Move Red from tube 1 slot 1 to tube 5 slot 2)
8.  Move Pink from tube 6 slot 1 to tube 11 slot 2)
9.  Move LimeGreen from tube 7 slot 1 to tube 6 slot 1)
10.  Move SkyBlue from tube 2 slot 2 to tube 7 slot 1)
11.  Move Blue from tube 2 slot 3 to tube 10 slot 1) - Sorted!
12.  Move PaleGreen from tube 3 slot 1 to tube 2 slot 3)
13.  Move Orange from tube 8 slot 1 to tube 4 slot 1)
14.  Move PaleGreen from tube 3 slot 2 to tube 2 slot 2)
15.  Move Orange from tube 4 slot 1 to tube 3 slot 2)
16.  Move Orange from tube 4 slot 2 to tube 3 slot 1)
17.  Move PaleGreen from tube 8 slot 2 to tube 2 slot 1) - Sorted!
18.  Move Pink from tube 8 slot 3 to tube 11 slot 1) - Sorted!
19.  Move Orange from tube 3 slot 1 to tube 8 slot 3)
20.  Move Orange from tube 3 slot 2 to tube 8 slot 2)
21.  Move Orange from tube 3 slot 3 to tube 8 slot 1) - Sorted!
22.  Move LimeGreen from tube 6 slot 1 to tube 3 slot 3)
23.  Move Gray from tube 9 slot 2 to tube 4 slot 2)
24.  Move LimeGreen from tube 6 slot 2 to tube 3 slot 2)
25.  Move Gray from tube 6 slot 3 to tube 4 slot 1) - Sorted!
26.  Move LimeGreen from tube 9 slot 3 to tube 3 slot 1) - Sorted!
27.  Move Purple from tube 9 slot 4 to tube 1 slot 1)
28.  Move Purple from tube 1 slot 1 to tube 6 slot 3)
29.  Move Purple from tube 1 slot 2 to tube 6 slot 2)
30.  Move SkyBlue from tube 7 slot 1 to tube 1 slot 2)
31.  Move SkyBlue from tube 7 slot 2 to tube 1 slot 1) - Sorted!
32.  Move Red from tube 7 slot 3 to tube 5 slot 1) - Sorted!
33.  Move Purple from tube 7 slot 4 to tube 6 slot 1) - Sorted!
Started: 2021-08-20T07:07:45.784475400
Ended: 2021-08-20T07:08:16.973387500
Elapsed: 31 seconds.
         */
    }

    private static ItemColor[][] setup653() {
        ItemColor[][] initialTableau = new ItemColor[14][4];
        initialTableau[0] = new ItemColor[]{ItemColor.RED, ItemColor.PINK, ItemColor.PALEGREEN, ItemColor.ORANGE};
        initialTableau[1] = new ItemColor[]{ItemColor.BLUE, ItemColor.RED, ItemColor.PURPLE, ItemColor.PALEGREEN};
        initialTableau[2] = new ItemColor[]{ItemColor.GRAY, ItemColor.PURPLE, ItemColor.PINK, ItemColor.GREEN};
        initialTableau[3] = new ItemColor[]{ItemColor.ORANGE, ItemColor.BROWN, ItemColor.YELLOW, ItemColor.LIMEGREEN};
        initialTableau[4] = new ItemColor[]{ItemColor.BROWN, ItemColor.YELLOW, ItemColor.ORANGE, ItemColor.BROWN};
        initialTableau[5] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.RED, ItemColor.BROWN, ItemColor.GREEN};
        initialTableau[6] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.BLUE, ItemColor.SKYBLUE, ItemColor.LIMEGREEN};
        initialTableau[7] = new ItemColor[]{ItemColor.GREEN, ItemColor.RED, ItemColor.YELLOW, ItemColor.PURPLE};
        initialTableau[8] = new ItemColor[]{ItemColor.BLUE, ItemColor.PINK, ItemColor.YELLOW, ItemColor.PURPLE};
        initialTableau[9] = new ItemColor[]{ItemColor.PALEGREEN, ItemColor.GRAY, ItemColor.GRAY, ItemColor.GREEN};
        initialTableau[10] = new ItemColor[]{ItemColor.SKYBLUE, ItemColor.ORANGE, ItemColor.PINK, ItemColor.GRAY};
        initialTableau[11] = new ItemColor[]{ItemColor.LIMEGREEN, ItemColor.SKYBLUE, ItemColor.BLUE, ItemColor.PALEGREEN};
        initialTableau[12] = new ItemColor[4];
        initialTableau[13] = new ItemColor[4];

        return initialTableau;
    }



    static void showTableau(ItemColor[][] theTableau) {
        for (ItemColor[] aTube : theTableau) {
            String tubeString = Arrays.asList(aTube).toString();

            tubeString = tubeString.replaceAll(", null", "");
            tubeString = tubeString.replaceAll("null", "");
            System.out.println(tubeString);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        //---------------------------------------------------------------
        // Evaluate input parameters, if any.
        //---------------------------------------------------------------
        debug("Evaluating parameters");
        if (args.length > 0)
            System.out.println("Number of args: " + args.length);

        for (String startupFlag : args) { // Cycling thru them this way, position is irrelevant.
            if (startupFlag.equals("-debug")) {
                // This could be redundant, if the java option was used to turn on debug.
                if (!debug) System.out.println("Debugging printouts on by command-line option.");
                debug = true;
            } else if (startupFlag.equals("-text")) {
                textOnly = true;
                System.out.println("Running in text-only mode.");
            } else {
                System.out.println("Parameter not handled: [" + startupFlag + "]");
            } // end if/else
        } // end for i


        if (textOnly) {
            ItemColor[][] firstTableau = setup();
            //System.out.println("The initial tableau: ");
            //showTableau(firstTableau);
            if (TubeRackPanel.validateTableau(firstTableau)) {
                solvePuzzle(firstTableau);
            }
        } else {
            JFrame theFrame = new JFrame("Ball Sort Puzzle");

            TubeRackPanel theTubeRack = new TubeRackPanel();
            theFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
            });

            // Needed to override the 'metal' L&F for Swing components.
            String laf = UIManager.getSystemLookAndFeelClassName();
            try {
                UIManager.setLookAndFeel(laf);
            } catch (Exception ignored) {
            }    // end try/catch
            SwingUtilities.updateComponentTreeUI(theTubeRack);

            theFrame.getContentPane().add(theTubeRack, "Center");
            theFrame.pack();
            theFrame.setSize(new Dimension(650, 640));
            theFrame.setVisible(true);
//            theFrame.setResizable(false);
            theFrame.setLocationRelativeTo(null);
        }

    } // end main

    static void solvePuzzle(ItemColor[][] firstTableau) {
        localDateTime = LocalDateTime.now();
        System.out.println("Started the Ball Sort Puzzle solution finder at: " + localDateTime);
        System.out.println("\nThe initial tableau: ");
        showTableau(firstTableau);

        TubeRack firstRack = new TubeRack(firstTableau, new ArrayList<>());

        // Create a list of racks and add in the first one
        rackList = new ArrayList<>();
        rackList.add(firstRack);

        // Keep an indexer and a reference to the TubeRack that is currently being processed.
        currentRack = firstRack;
        int currentRackIndex = 0;


        while (true) {
            System.out.println("\nExploring possibilities for rack # " + (currentRackIndex + 1) + " of " + rackList.size());
            currentRack.explorePossibilities();
            if (currentRack.sorted()) break; // Needed by graphical mode; text mode ends the app upon fully sorting a rack.
            currentRackIndex++;

            if (currentRackIndex == rackList.size()) {
                System.out.println("All possibilities have been explored, no solution found!");
                System.out.println("Started: " + LifoSort.localDateTime);
                System.out.println("Ended: " + LocalDateTime.now());
                if (textOnly) System.exit(0);
                else break;
            } else {
                currentRack = rackList.get(currentRackIndex);
            }
        }
    }

} // end LifoSort