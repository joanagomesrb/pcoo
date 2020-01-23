package src;

import static java.lang.System.*;
import java.awt.Color;
import java.awt.Point;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;


public class People extends Thread{

    private int id;
    private Position[] pick_up_people_place_point;
    private static char taxi_square;
    private static char pick_up_people_place;
    private static Labyrinth labyrinth;

    People(int id, Position[] pick_up_people_place_point, char[] extraSymbols){
        assert id > 0;
        assert pick_up_people_place_point != null;

        this.id = id;
        this.pick_up_people_place_point = pick_up_people_place_point;
        this.taxi_square = extraSymbols[0];
        this.pick_up_people_place = extraSymbols[1];
        this.labyrinth = CityMap.getlabyrinth();

        

        //this.labyrinth.board.draw(new ImageGelem("resources/person.png", labyrinth.board, 100), lin, col, 1);

    }

}