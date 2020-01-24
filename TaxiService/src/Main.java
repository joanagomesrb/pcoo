package src;

import static java.lang.System.*;
import java.awt.Color;
import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;

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
        char coffee = 'C';
        char library = 'L';

        int period = Global.DEFAULT_PERIOD;
        Global.metronome = new Metronome(period);
        

        char[] extraSymbols= {
            taxi_square,
            pick_up_people_place,
            coffee,
            library
        };

        Gelem[] gelems = {
            new StringGelem("" + taxi_square, Color.yellow),
            new StringGelem("" + pick_up_people_place, Color.black),
            new StringGelem("" + coffee, Color.black),
            new StringGelem("" + library, Color.black)        
        };
    


        CityMap cityMap = new CityMap(map, extraSymbols, gelems);
        Controller controller = new Controller();
        
        int i = Global.NR_TAXI;
        //Taxi taxi = new Taxi(i, extraSymbols);

        Map positions = new TreeMap<>();
        Position[] taxi_square_point = cityMap.getlabyrinth().roadSymbolPositions(taxi_square);
        Taxi taxi = new Taxi(i, controller, taxi_square_point, positions, extraSymbols);
        taxi.start();
        


       /* Position[] taxi_quare_point = CityMap.getlabyrinth().roadSymbolPositions(taxi_square);
        Position[] pick_up_people_place_point = cityMap.getlabyrinth().roadSymbolPositions(pick_up_people_place);
        
        for (int i = 0; i < Global.NR_PEOPLE; i++){
            Map markedPositionsPeople = new TreeMap<>();
            People people = new People(i+1, pick_up_people_place_point, markedPositionsPeople, extraSymbols);
            labyrinth.board.draw(new ImageGelem("resources/person.png", labyrinth.board, 100), lin, col, 1);
        
            //people.start();

        }*/

    }
}