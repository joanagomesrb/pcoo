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


public class Taxi extends Thread {

    private int id;
    private Position[] pick_up_people_place_point;
    private static char taxi;
    private static Gelem taxi_g;
    private static char pick_up_people_place;
    private static char coffee;
    private static char library;
    private static Labyrinth labyrinth;
    protected Position pos;
    private Position[] taxi_square_point;
    private Map positions;

    Taxi(int id, Controller controller, Position[] taxi_square, Map positions, char[] extraSymbols){

        assert id > 0;

        this.id = id;
        this.taxi_square_point = taxi_square;
        this.taxi = extraSymbols[0];
        this.pick_up_people_place = extraSymbols[1];
        this.coffee = extraSymbols[2];
        this.library = extraSymbols[3];
        this.labyrinth = CityMap.getlabyrinth();
        this.positions = positions;
        this.taxi_g = new ImageGelem("resources/taxi.jpg", this.labyrinth.board, 80, Global.N, Global.N);

        // go pick up a person
        out.println("Taxi #" + id + " started its day work!");
 
        // taxi day's work beginning:
        //pos = CityMap.taxi_square().randomEntryPosition();
        //draw(pos);

        //go(CityMap.taxi_square())

        //this.labyrinth.board.draw(new ImageGelem("resources/person.png", labyrinth.board, 100), lin, col, 1);

    }

    @Override
    public void run() {
        //searchPath(0, this.taxi_square_point[0].y, this.taxi_square_point[0].x, this.positions);
        //Position[] path;
        //path = this.labyrinth.floorPlan.randomShortestPath(pos, dest);
        //this.labyrinth.board.move(this.taxi, Global.N*pos.line()+i*l, Global.N*pos.column()+i*c, 2, Global.N*pos.line()+(i+1)*l, Global.N*pos.column()+(i+1)*c, 2);
        out.println("Hello Taxi1!");
        // get current position of symbol
        //out.println((this.labyrinth.symbolPositions(this.taxi))[0]);
        Position currentPos = (this.labyrinth.symbolPositions(this.taxi))[0];
        out.println(currentPos);
        Position nextPos = new Position(3,1);
        //out.println(gelemLayer(this.taxi_g, currentPos.line(), currentPos.column()));
        go(currentPos, nextPos);
    }

    public void go(Position now, Position then){
        assert now != null;
        assert then != null;

        if ((this.labyrinth.isRoad(then.line(), then.column())) && this.labyrinth.validPosition(then.line(), then.column())){
            draw(then);
            out.println("Moving!");

        }
        //erase(now);
        //Position thise = new Position(3,2);
        //erase(then);
        //draw(thise);
    }

    public void searchPath(int distance, int lin, int col, Map markedPositions) {
        if(this.labyrinth.validPosition(lin, col)){
            out.println("Hello Taxi2");
        }

    }

    private void erase(Position pos){
      assert pos != null;

      this.labyrinth.board.erase(this.taxi_g, Global.N*pos.line(), Global.N*pos.column(), 1);
   }


    private void draw(Position pos){
        assert pos != null;
        
        this.labyrinth.board.draw(this.taxi_g, Global.N*pos.line(), Global.N*pos.column(), 1);
    }

}