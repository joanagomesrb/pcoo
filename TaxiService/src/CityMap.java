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
    protected final FloorPlan floorPlan;
    public static char[] extraSymbols;
    public static char taxi_square;
    public static char pick_up_people_place;
    public static char coffee;
    public static char library;

    public CityMap(String map_file, char[] extraSymbols, Gelem[] gelems){

        assert map_file != null;
        assert Labyrinth.validMapFile(map_file): "ERROR: invalid map file \""+ map_file +"\"";


        this.map_file = map_file;
        this.extraSymbols = extraSymbols;


        floorPlan = new FloorPlan(map_file);
        
        LabyrinthGelem.setShowRoadBoundaries();

        //labyrinth = new Labyrinth(map_file, extraSymbols);

        taxi_square = extraSymbols[0];
        pick_up_people_place = extraSymbols[1];
        coffee = extraSymbols[2];
        library = extraSymbols[3];

        Labyrinth.setWindowName("City");
        labyrinth.setNumberOfLayers(2);
        labyrinth = new Labyrinth(floorPlan.exportMap(), floorPlan.roadSymbols(), Global.N, true);
        //labyrinth.attachGelemToRoadSymbol(taxi_square, new ImageGelem("resources/taxi.jpg", labyrinth.board, 90, Global.N, Global.N));
        labyrinth.attachGelemToRoadSymbol(pick_up_people_place, new ImageGelem("resources/person.png", labyrinth.board, 90, Global.N, Global.N));
        labyrinth.attachGelemToRoadSymbol(coffee, new ImageGelem("resources/coffee.jpg", labyrinth.board, 90, Global.N, Global.N));
        labyrinth.attachGelemToRoadSymbol(library, new ImageGelem("resources/library.png", labyrinth.board, 90, Global.N, Global.N));
    
       
       

      

    }


    
    public static Labyrinth getlabyrinth() {
        assert labyrinth != null;
        
        return labyrinth;
    }
}