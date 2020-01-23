package src;

import static java.lang.System.*;

import java.awt.Color;
import java.awt.Point;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

public class Global{

    // map_file
    public static String MAP = "resources/map_file_1.txt";

    // number of taxis
    public static final int NR_TAXI = 1;
    
    // number of people
    public static final int NR_PEOPLE = 1;

    public static final int N = 4;
    public static int DEFAULT_PERIOD = 10; // 10 ms
    public static Metronome metronome;
    
    public static int MIN_RANDOM_PAUSE = 20; // # syncs
    public static int MAX_RANDOM_PAUSE = 40; // # syncs
 
    public static int MIN_SPEED = 1;
    public static int MAX_SPEED = 3;
}