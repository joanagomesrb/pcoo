package concurrent;

import static java.lang.System.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import pt.ua.concurrent.CThread;
import pt.ua.concurrent.CRunnable;
import pt.ua.concurrent.Metronome;

public class TestMetronome
{
   /* Usage:
    * java -ea TestMetronome <clock-period> <n-clients> <clients-random.max> <n-iter>
    *    clock-period: sync point period
    *    n-clients: number of client tasks
    *    clients-random-max: max random delay [ms]
    *    n-iter: number of iterations
    */
   public static void main(String[] args)
   {
      String[] defaultArgs = {"500", "4", "400", "20"};
      if (args.length != 0 && args.length != 4)
      {
         out.println("Usage: java -ea TestMetronome <ms-clock-period> <n-clients> <clients-random.max> <n-iter>");
         exit(1);
      }
      else if (args.length == 0)
      {
         args=defaultArgs;
         out.print("Using \"TestMetronome");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      long clockPeriod = Integer.parseInt(args[0]);
      int numClients = Integer.parseInt(args[1]);
      int randomMax = Integer.parseInt(args[2]);
      int numIter = Integer.parseInt(args[3]);

      final Metronome metronome = new Metronome(clockPeriod);

      CThread[] tasks = new CThread[numClients];
      for(int i = 0; i < numClients; i++)
         tasks[i] = new CThread(new ClientTask(metronome, randomMax, numIter));
      for(int i = 0; i < numClients; i++)
         tasks[i].start();
      for(int i = 0; i < numClients; i++)
         tasks[i].ajoin();
      metronome.terminate();
   }

   static class ClientTask extends CRunnable
   {
      static AtomicInteger count = new AtomicInteger(1);

      ClientTask(Metronome metronome, int randomMax, int numIter) {
         this.metronome = metronome;
         this.randomMax = randomMax;
         this.numIter = numIter;
         id=count.get();
         count.incrementAndGet();
      }

      public void arun()
      {
         for(int i = 0; i < numIter; i++)
         {
            counter = metronome.sync();
            out.printf("ID #%02d [ITER #%03d] - counter=%03d\n", id, (i+1), counter);
            CThread.pause(1+(int)(Math.random()*randomMax));
         }
      }

      long counter;
      final Metronome metronome;
      final int randomMax;
      final int numIter;
      final int id;
   }
}

