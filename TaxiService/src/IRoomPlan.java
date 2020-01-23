package src;

import static java.lang.System.*;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

/*
 * Instances of this class are immutable after being registered in FloorPlan (registerRoom);
 */
public interface IRoomPlan
{
   public boolean registeredToFloor();
   public void addToFloor(FloorPlan floorPlan);
   public String description();
   public char IDSymbol();
   public char entrySymbol();
   public char exitSymbol();
   public boolean isRoad(char symbol);
   public void registerResourcePosition(char symbol, int line, int column);
   public void setBoundingBox(int minLin, int maxLin, int minCol, int maxCol);
   public int minLin();
   public int maxLin();
   public int minCol();
   public int maxCol();
   public int numLins();
   public int numCols();
   public boolean inside(Position pos);
   public boolean inside(int line, int column);
   public void setEntryPositions(Position[] pos);
   public void setExitPositions(Position[] pos);
   public Position randomEntryPosition();
   public Position randomExitPosition();
}

