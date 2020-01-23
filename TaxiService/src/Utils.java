package src;

import pt.ua.concurrent.Metronome;
import pt.ua.concurrent.CThread;

public class Utils
{
   /*
    * returns a random value in interval [0;max[ (opened interval in max)
    */
   public static int randomInteger(int max)
   {
      return (int)(Math.random()*(double)max);
   }

   /*
    * returns a random value in interval [min;max] (closed interval in max)
    */
   public static int randomInteger(int min, int max)
   {
      return min + (int)(Math.random()*(double)(max-min+1));
   }

   /*
    * random sleep time
    */
   public static void randomPause()
   {
      int numSyncs = randomInteger(Global.MIN_RANDOM_PAUSE, Global.MAX_RANDOM_PAUSE);
      for(int i = 0; i < numSyncs; i++)
         Global.metronome.sync();
   }

}

