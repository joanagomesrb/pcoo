package concurrent;

import static pt.ua.concurrent.Console.*;

public class TestConsole
{
   public static void main(String[] args)
   {
      for(int c = 0; c < colors.length; c++)
         println(colors[c], "["+c+"] Colorful text!");

      print("RGB: ");
      setColor(RED);
      print("red");
      resetColor();
      print(",");
      setColor(GREEN);
      print("green");
      resetColor();
      print(",");
      setColor(BLUE);
      print("blue");
      resetColor();
      println(".");

      print("RGB: ");
      print(RED, "red");
      print(",");
      print(GREEN, "green");
      print(",");
      print(BLUE, "blue");
      println(".");
   }
}

