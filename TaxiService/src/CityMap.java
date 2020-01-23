package src;

import static java.lang.System.*;
import java.awt.Color;
import java.awt.Point;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

public class CityMap{

    public static String map_file;
    public static Labyrinth labyrinth = null;
    public static char[] extraSymbols;
    public static char taxi_square;
    public static char pick_up_people_place;

    public CityMap(String map_file, char[] extraSymbols, Gelem[] gelems){

        assert map_file != null;

        this.map_file = map_file;
        this.extraSymbols = extraSymbols;
        
        LabyrinthGelem.setShowRoadBoundaries();

        labyrinth = new Labyrinth(map_file, extraSymbols);

        taxi_square = extraSymbols[0];
        pick_up_people_place = extraSymbols[1];

        for (int i = 0; i < extraSymbols.length; i++) {
            labyrinth.attachGelemToRoadSymbol(extraSymbols[i], gelems[i]);
        }       

    }

    public static Labyrinth getlabyrinth() {
        assert labyrinth != null;
        
        return labyrinth;
    }
}