package concurrent;

import static java.lang.System.*;
import pt.ua.concurrent.*;

public class TestTimeout extends CObject
{
   public static void main(String[] args)
   {
      String[] defaultArgs = {"ms", "1000", "200", "500"};
      if (args.length == 0)
      {
         args=defaultArgs;
         out.println("Usage: TestTimeout [ms | ns] <timeout1> ...  (default is in milliseconds)");
         out.println();
         out.print("Using \"TestTimeout");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      boolean milliseconds = true;
      int i = 0;
      if (args[0].equalsIgnoreCase("ns"))
      {
         milliseconds = false;
         i++;
      }
      if (args[0].equalsIgnoreCase("ms"))
      {
         milliseconds = true;
         i++;
      }

      TestTimeout obj = new TestTimeout();
      long t = 0L;

      for(; i < args.length; i++)
      {
         boolean result;
         long timeout = Long.parseLong(args[i]);
         if (milliseconds)
         {
            t = System.currentTimeMillis();
            result = obj.justWait(timeout);
         }
         else
         {
            t = System.nanoTime();
            result = obj.justWait(timeout / 1000000L, (int)(timeout % 1000000L));
         }
         if(!result)
         {
            if (milliseconds)
               out.println("TIMEOUT: "+(System.currentTimeMillis()-t)+" milliseconds ellapsed!");
            else
               out.println("TIMEOUT: "+(System.nanoTime()-t)+" nanosseconds ellapsed!");
         }
         else
            out.println("ERROR: timeout bug (could it be a spurious wakeup?)");
      }
   }

   public synchronized boolean justWait(long timeout)
   {
      return await(timeout);
   }

   public synchronized boolean justWait(long millis, int nano)
   {
      return await(millis, nano);
   }
}

