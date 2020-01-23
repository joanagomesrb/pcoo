package concurrent;

import static java.lang.System.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import pt.ua.concurrent.CThread;
import pt.ua.concurrent.CRunnable;
import pt.ua.concurrent.TriggeredBarrier;

public class TestTriggeredBarrier
{
   /* Usage:
    * java -ea TestTriggeredBarrier <clock-period> <n-clients> <clients-random.max> <n-iter>
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
         out.println("Usage: java -ea TestTriggeredBarrier <clock-period> <n-clients> <clients-random.max> <n-iter>");
         exit(1);
      }
      else if (args.length == 0)
      {
         args=defaultArgs;
         out.print("Using \"TestTriggeredBarrier");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      final TriggeredBarrier triggeredBarrier = new TriggeredBarrier();
      ScheduledExecutorService clock = Executors.newScheduledThreadPool(1);

      long clockPeriod = Integer.parseInt(args[0]);
      int numClients = Integer.parseInt(args[1]);
      int randomMax = Integer.parseInt(args[2]);
      int numIter = Integer.parseInt(args[3]);

      CThread[] tasks = new CThread[numClients];
      clock.scheduleAtFixedRate(new Runnable() {public void run() {triggeredBarrier.release();} }, clockPeriod, clockPeriod, TimeUnit.MILLISECONDS);
      for(int i = 0; i < numClients; i++)
         tasks[i] = new CThread(new ClientTask(triggeredBarrier, randomMax, numIter));
      for(int i = 0; i < numClients; i++)
         tasks[i].start();
      for(int i = 0; i < numClients; i++)
         tasks[i].ajoin();
      clock.shutdown();
   }

   static class ClientTask extends CRunnable
   {
      static AtomicInteger count = new AtomicInteger(1);

      ClientTask(TriggeredBarrier triggeredBarrier, int randomMax, int numIter) {
         this.triggeredBarrier = triggeredBarrier;
         this.randomMax = randomMax;
         this.numIter = numIter;
         id=count.get();
         count.incrementAndGet();
      }

      public void arun()
      {
         for(int i = 0; i < numIter; i++)
         {
            counter = triggeredBarrier.awaitCount();
            out.printf("ID #%02d [ITER #%03d] - counter=%03d\n", id, (i+1), counter);
            CThread.pause(1+(int)(Math.random()*randomMax));
         }
      }

      long counter;
      final TriggeredBarrier triggeredBarrier;
      final int randomMax;
      final int numIter;
      final int id;
   }
}

