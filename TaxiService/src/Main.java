package src;

import static java.lang.System.*;
import java.awt.Color;
import java.awt.Point;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

public class Main{

    public static void main(String[] args){

        // variables
        String map = Global.MAP;
        char taxi_square = 'S';
        char pick_up_people_place = 'P';
        

        char[] extraSymbols= {
            taxi_square,
            pick_up_people_place
        };

        Gelem[] gelems = {
            new StringGelem("" + taxi_square, Color.yellow),
            new StringGelem("" + pick_up_people_place, Color.black)        
        };
    


        CityMap cityMap = new CityMap(map, extraSymbols, gelems);
        
        Position[] taxi_quare_point = CityMap.getlabyrinth().roadSymbolPositions(taxi_square);
        Position[] pick_up_people_place_point = cityMap.getlabyrinth().roadSymbolPositions(pick_up_people_place);
        

    }
}