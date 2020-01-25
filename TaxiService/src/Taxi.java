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
    private static char person;
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
        this.person = extraSymbols[1];
        this.coffee = extraSymbols[2];
        this.library = extraSymbols[3];
        this.labyrinth = CityMap.getlabyrinth();
        this.positions = positions;
        this.taxi_g = new ImageGelem("resources/taxi.jpg", this.labyrinth.board, 80, Global.N, Global.N);

        // go pick up a person
 
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
        out.println("Taxi #" + id + " started its day work!");
        Position currentPos = (this.labyrinth.symbolPositions(this.taxi))[0];
        draw(currentPos);

        // go pick up a person
        goPickUp(currentPos);
        
        //Postion pick_up_people_place_point = new Position();
        // get current position of symbol
        //out.println((this.labyrinth.symbolPositions(this.taxi))[0]);
        //Position nextPos = new Position(3,1);
        //out.println(gelemLayer(this.taxi_g, currentPos.line(), currentPos.column()));
        //go(currentPos, nextPos);
    }

    public void goPickUp(Position now){
        assert now != null;

        Position personPos = (this.labyrinth.symbolPositions(this.person))[0];
        go(now, personPos);
    }

    public void go(Position now, Position then){
        assert now != null;
        assert then != null;

        if ((this.labyrinth.isRoad(then.line(), then.column())) && this.labyrinth.validPosition(then.line(), then.column())){
            searchPath(now, then);
            
            //draw(then);
            out.println("Moving!");

        }
        //erase(now);
        //Position thise = new Position(3,2);
        //erase(then);
        //draw(thise);
    }

    
    public void searchPath(Position now, Position then){
        assert now != null;
        assert then != null;
        
        int xN = now.line();
        int yN = now.column();
        int xT = then.line();
        int yT = then.column();

        out.println("now " + now);
        out.println("then " + then);
        /*out.println("yN " + yN);
        out.println("yT " + yT);*/
        
        boolean arrived = false;
        Position move = now;
        //Position newnow;

        int i = 0;
        while(!arrived && !move.isEqual(then)){
            if (xN == xT){
                if (yN == yT){
                    arrived = true;
                }else if (yN < yT){
                    erase(now);
                    move = goRight(now);
                    draw(move);
                    now = move;
                }else if (yN > yT){
                    erase(now);
                    move = goLeft(now);
                    draw(now);
                    now = move;
                }
            }else if (xN < xT){
                erase(now);
                move = goDown(now);
                draw(now);
                now = move;
            }else if (xN > xT){
                erase(now);
                move = goUp(now);
                draw(now);
                now = move;
            }
        }
    }

    private Position goRight(Position now){

        int yN = now.column();
        Position move = new Position(now.line(), yN+1);

        return move;
    }
    private Position goLeft(Position now){

        int yN = now.column();
        Position move = new Position(now.line(), yN-1);

        return move;
    }
    private Position goDown(Position now){
        
        int xN = now.line();
        Position move = new Position(xN-1, now.column());

        return move;
    }
    private Position goUp(Position now){
         
        int xN = now.line();
        Position move = new Position(xN+1, now.column());

        return move;
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