package src;

import static java.lang.System.*;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.*;
import pt.ua.concurrent.*;

/*
 * Instances of this class are immutable after all rooms registered
 */
public class FloorPlan
{
   public FloorPlan(String pathToMap)
   {
      assert Labyrinth.validMapFile(pathToMap): "ERROR: invalid map file \""+pathToMap+"\"";

      roadSymbols = new char[] {taxi_square, pick_up_people_place, coffee, library};
      System.out.println("Hello world 3");
      readFile(pathToMap);
   }

   public boolean containsSymbol(char symbol)
   {
      boolean result = false;
      for(int l = 0; !result && l < numLines; l++)
         for(int c = 0; !result && c < numColumns && map[l][c] != '\u0000'; c++)
            result = map[l][c] == symbol;
      return result;
   }

   public boolean isReservedSymbol(char symbol)
   {
      return symbol == taxi_square || symbol == pick_up_people_place || symbol == coffee || symbol == library;
   }

   public boolean validRoomIDSymbol(char symbol)
   {
      return symbol != ' ' && symbol != '\u0000' && !isReservedSymbol(symbol) && containsSymbol(symbol);
   }

   public void registerRoom(IRoomPlan room)
   {
      assert room != null;
      assert !room.registeredToFloor();
      assert validRoomIDSymbol(room.IDSymbol());

      Position[] ps = findSymbolPositions(room.IDSymbol());
      out.print(room.description()+" symbol positions: ");
      for(int i = 0; i < ps.length; i++)
      {
         if (i > 0)
            out.print(",");
         out.print(ps[i]);
      }
      out.println();

      TemporaryPositionsRegister reg = new TemporaryPositionsRegister();
      for(int i = 0; i < ps.length; i++)
      {
         if (i == 0)
         {
            reg.minLin = reg.maxLin = ps[i].line();
            reg.minCol = reg.maxCol = ps[i].column();
         }
         else
         {
            if (reg.minLin > ps[i].line())
               reg.minLin = ps[i].line();
            else if (reg.maxLin < ps[i].line())
               reg.maxLin = ps[i].line();
            if (reg.minCol > ps[i].column())
               reg.minCol = ps[i].column();
            else if (reg.maxCol < ps[i].column())
               reg.maxCol = ps[i].column();
         }
         fillPosition(room, ps[i].line()+1, ps[i].column(), taxi_square, reg);
         fillPosition(room, ps[i].line()-1, ps[i].column(), pick_up_people_place, reg);
         fillPosition(room, ps[i].line()-1, ps[i].column(), coffee, reg);
         fillPosition(room, ps[i].line()-1, ps[i].column(), library, reg);
      }

      out.print(room.description()+" bounding box: ("+reg.minLin+","+reg.minCol+") to ("+reg.maxLin+","+reg.maxCol+")");
      out.print(room.description()+" entry positions: ");
      for(int i = 0; i < reg.entries.length; i++)
      {
         if (i > 0)
            out.print(",");
         out.print(reg.entries[i]);
      }
      out.println();
      out.print(room.description()+" exit positions: ");
      for(int i = 0; i < reg.exits.length; i++)
      {
         if (i > 0)
            out.print(",");
         out.print(reg.exits[i]);
      }
      out.println();

      room.setBoundingBox(reg.minLin, reg.maxLin, reg.minCol, reg.maxCol);
      room.setEntryPositions(reg.entries);
      room.setExitPositions(reg.exits);
   }

   public Position[] findSymbolPositions(char symbol)
   {
      assert symbol != '\u0000';

      Position[] result = null;

      int n = 0;
      for(int l = 0; l < numLines; l++)
         for(int c = 0; c < numColumns && map[l][c] != '\u0000'; c++)
            if (map[l][c] == symbol)
              n++;
      result = new Position[n];
      n = 0;
      for(int l = 0; l < numLines; l++)
         for(int c = 0; c < numColumns && map[l][c] != '\u0000'; c++)
            if (map[l][c] == symbol)
               result[n++] = new Position(l, c);

      return result;
   }

   public Position[] randomShortestPath(Position src, Position dst)
   {
      assert src != null && isRoad(src);
      assert dst != null && isRoad(dst);

      Position[] result = null;

      int[][] distances = new int[numLines][numColumns];
      for(int l = 0; l < numLines; l++)
         Arrays.fill(distances[l], -1);
      
      fillDistances(distances, src.line(), src.column(), 0);
      int minDist = distances[dst.line()][dst.column()];
      if (minDist >= 0)
      {
         result = new Position[minDist];
         Position p = dst;
         int d;
         for(d = minDist-1; d > 0; d--)
         {
            result[d] = p;
            int l, c;
            do
            {
               l = p.line();
               c = p.column();
               switch(Utils.randomInteger(4))
               {
                  case 0: l++; break;
                  case 1: l--; break;
                  case 2: c++; break;
                  case 3: c--; break;
               }
            }
            while(!(l >= 0 && l < numLines && c >= 0 && c < numColumns && isRoad(l, c)) ||
                  distances[l][c] != d);
            p = new Position(l, c);
         }
         result[d] = p;
      }

      return result;
   }

   public Position[] randomShortestPath(IRoomPlan room, Position src, Position dst)
   {
      assert room != null;
      assert room.registeredToFloor();
      assert src != null && isRoad(src);
      assert dst != null && isRoad(dst);

      Position[] result = null;

      int[][] distances = new int[numLines][numColumns];
      for(int l = 0; l < numLines; l++)
         Arrays.fill(distances[l], -1);

      /*
      out.print("[randomShortestPath] roadSymbols:");
      for(int i = 0; i < roadSymbols.length; i++)
         out.print(" "+roadSymbols[i]);
      out.println();
      */

      fillDistances(room, distances, src.line(), src.column(), 0, true);
      int minDist = distances[dst.line()][dst.column()];
      /*
      for(int l = 0; l < numLines; l++)
      {
         for(int c = 0; c < numColumns && map[l][c] != '\u0000'; c++)
            out.printf("%3d", distances[l][c]);
         out.println();
      }
      out.println("[randomShortestPath] "+src+" <-> "+dst+" minDist: "+minDist);
      */
      if (minDist >= 0)
      {
         result = new Position[minDist];
         Position p = dst;
         int d;
         for(d = minDist-1; d > 0; d--)
         {
            result[d] = p;
            int l, c;
            do
            {
               l = p.line();
               c = p.column();
               switch(Utils.randomInteger(4))
               {
                  case 0: l++; break;
                  case 1: l--; break;
                  case 2: c++; break;
                  case 3: c--; break;
               }
            }
            while(!(l >= 0 && l < numLines && c >= 0 && c < numColumns && room.inside(l, c)) ||
                  distances[l][c] != d);
            p = new Position(l, c);
         }
         result[d] = p;
      }

      return result;
   }

   /*
    * A randomly choosen IRoomPlan IDSymbol location
    */
   public Position randomRoomPosition(IRoomPlan room)
   {
      assert room != null;
      assert room.registeredToFloor();

      int lin, col;
      do
      {
         lin = Utils.randomInteger(room.minLin(), room.maxLin());
         col = Utils.randomInteger(room.minCol(), room.maxCol());
      }
      while(map[lin][col] != room.IDSymbol());

      return new Position(lin, col);
   }

   public String[] exportMap()
   {
      String[] result = new String[numLines];
      for(int l = 0; l < numLines; l++)
         result[l] = new String(map[l]);
      return result;
   }

   /*
    * All distances in map
    */
   protected void fillDistances(int[][] distances, int line, int column, int d)
   {
      if (line >= 0 && line < numLines && column >= 0 && column < numColumns && isRoad(line, column))
      {
         if (distances[line][column] == -1 || distances[line][column] > d)
         {
            distances[line][column] = d;
            fillDistances(distances, line+1, column, d+1);
            fillDistances(distances, line-1, column, d+1);
            fillDistances(distances, line, column+1, d+1);
            fillDistances(distances, line, column-1, d+1);
         }
      }
   }

   /*
    * All distances through a specific room in mao 
    */
   protected void fillDistances(IRoomPlan room, int[][] distances, int line, int column, int d, boolean isSrc)
   {
      if (line >= 0 && line < numLines && column >= 0 && column < numColumns && isRoad(line, column))
      {
         if (distances[line][column] == -1 || distances[line][column] > d)
         {
            distances[line][column] = d;
            if (room.inside(line, column) || isSrc)
            {
               fillDistances(room, distances, line+1, column, d+1, false);
               fillDistances(room, distances, line-1, column, d+1, false);
               fillDistances(room, distances, line, column+1, d+1, false);
               fillDistances(room, distances, line, column-1, d+1, false);
            }
         }
      }
   }

   public boolean roadSymbolRegistered(char symbol)
   {
      assert symbol != ' ' && symbol != '\u0000';

      boolean result = false;
      for(int i = 0; !result && i < roadSymbols.length; i++)
         result = symbol == roadSymbols[i];
      return result;
   }

   public void registerRoadSymbol(char symbol)
   {
      assert symbol != ' ' && symbol != '\u0000';

      if (!roadSymbolRegistered(symbol))
      {
         char[] rs = new char[roadSymbols.length + 1];
         arraycopy(roadSymbols, 0, rs, 0, roadSymbols.length);
         rs[roadSymbols.length] = symbol;
         roadSymbols = rs;
      }
   }

   public boolean isRoad(int line, int column)
   {
      assert line >= 0 && line < numLines;
      assert column >= 0 && column < numColumns;

      char symbol = map[line][column];
      boolean result = false;
      for(int i = 0; !result && i < roadSymbols.length; i++)
         result = symbol == roadSymbols[i];
      return result;
   }

   public boolean isRoad(Position pos)
   {
      assert pos != null;

      return isRoad(pos.line(), pos.column());
   }

   public char[] roadSymbols()
   {
      return roadSymbols;
   }

   @Override
   public String toString()
   {
      String result = "";
      for(int l = 0; l < numLines; l++)
      {
         for(int c = 0; c < numColumns && map[l][c] != '\u0000'; c++)
            result += map[l][c];
         result += '\n';
      }
      return result;
   }

   protected void readFile(String pathToMap)
   {
      numLines = 0;
      numColumns = 0;
      try
      {
         File fin = new File(pathToMap);
         Scanner scin = new Scanner(fin);
         while(scin.hasNextLine())
         {
            String ln = scin.nextLine();
            if (numColumns < ln.length())
               numColumns = ln.length();
            numLines++;
         }
         scin.close();
         map = new char[numLines][numColumns]; // initialized to '\u0000'
         scin = new Scanner(fin);
         int l = 0;
         while(scin.hasNextLine())
         {
            String ln = scin.nextLine();
            ln.getChars(0, ln.length(), map[l], 0);
            l++;
         }
         scin.close();
      }
      catch(IOException e)
      {
         map = null;
         assert false;
      }
   }

   protected void fillPosition(IRoomPlan room, int line, int column, char lastPos, TemporaryPositionsRegister reg)
   {
      if (line >= 0 && line < numLines && column >= 0 && column < numColumns)
      {
         boolean checkBoundaries = true;
         // find contiguous entry/exit symbols:
         if (map[line][column] == room.entrySymbol())
         {
            Position[] ps = new Position[reg.entries.length + 1];
            arraycopy(reg.entries, 0, ps, 0, reg.entries.length);
            ps[reg.entries.length] = new Position(line, column);
            reg.entries = ps;
            map[line][column] = lastPos;
         }
         else if (map[line][column] == room.exitSymbol())
         {
            Position[] ps = new Position[reg.exits.length + 1];
            arraycopy(reg.exits, 0, ps, 0, reg.exits.length);
            ps[reg.exits.length] = new Position(line, column);
            reg.exits = ps;
            switch(lastPos)
            {
               case taxi_square: map[line][column] = pick_up_people_place; break;
               case pick_up_people_place: map[line][column] = taxi_square; break;
               case coffee: map[line][column] = library; break;
               case library: map[line][column] = coffee; break;
            }
         }
         else if (room.isRoad(map[line][column]) && (map[line][column] != room.IDSymbol()))
         {
            if (map[line][column] == ' ')
               map[line][column] = room.IDSymbol();
            else
               room.registerResourcePosition(map[line][column], line, column);
            fillPosition(room, line+1, column, taxi_square, reg);
            fillPosition(room, line-1, column, pick_up_people_place, reg);
            fillPosition(room, line, column+1,  coffee, reg);
            fillPosition(room, line, column-1,  library, reg);
         }
         else
            checkBoundaries = false;

         if(checkBoundaries) // includes entry & exit locations!
         {
            if (reg.minLin > line)
               reg.minLin = line;
            else if (reg.maxLin < line)
               reg.maxLin = line;
            if (reg.minCol > column)
               reg.minCol = column;
            else if (reg.maxCol < column)
               reg.maxCol = column;
         }
      }
   }

   protected char[][] map = null;
   protected int numLines = 0;
   protected int numColumns = 0;
   protected char[] roadSymbols;

   public final static char taxi_square = 'S';
   public final static char pick_up_people_place = 'P';
   public final static char coffee = 'C';
   public final static char library = 'L';

   protected class TemporaryPositionsRegister
   {
      Position[] entries = new Position[0];
      Position[] exits = new Position[0];
      int minLin, maxLin;
      int minCol, maxCol;
   }
}

