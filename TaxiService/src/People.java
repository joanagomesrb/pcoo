package src;

import static java.lang.System.*;
import java.awt.Color;
import java.awt.Point;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

import java.util.Map;
import java.util.TreeMap;


public class People extends Thread implements PeopleInterface{

    private int id;
    private Position[] pick_up_people_place_point;
    private static char taxi_square;
    private static char person;
    private static char coffee;
    private static char library;
    private static Labyrinth labyrinth;
    private Gelem person_g;
    public  Position currentPosition;

    People(int id, Controller controller, Position[] pick_up_people_place_point, Map positions, char[] extraSymbols){
        assert id > 0;
        assert pick_up_people_place_point != null;

        this.id = id;
        this.pick_up_people_place_point = pick_up_people_place_point;
        this.taxi_square = extraSymbols[0];
        this.person = extraSymbols[1];
        this.coffee = extraSymbols[2];
        this.library = extraSymbols[3];
        this.labyrinth = CityMap.getlabyrinth();
        this.person_g = new ImageGelem("resources/person.png", this.labyrinth.board, 80, Global.N, Global.N);


        //this.currentPosition = startPosition;
        


        //this.labyrinth.board.draw(new ImageGelem("resources/person.png", labyrinth.board, 100), lin, col, 1);

    }

    @Override
    public void run() {
        out.println("Person #" + id + " started its day!");
        //Position currentPos = (this.labyrinth.symbolPositions(this.person))[0];
        Position tmp = (this.labyrinth.symbolPositions(this.person))[0];
        this.currentPosition = tmp;
        draw(this.currentPosition);

    }

    private void draw(Position pos){
        assert pos != null;
        
        this.labyrinth.board.draw(this.person_g, Global.N*pos.line(), Global.N*pos.column(), 1);
    }

    public int getID(){
        return this.id;
    }

    public Position[] pick_up_people_place_point(){
        return this.pick_up_people_place_point;
    }
}