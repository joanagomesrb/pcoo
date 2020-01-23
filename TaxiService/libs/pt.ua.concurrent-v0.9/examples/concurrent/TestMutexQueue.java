package concurrent;

import static java.lang.System.*;

import java.util.Queue;
import java.util.LinkedList;
import pt.ua.concurrent.*;
import concurrent.support.*;

public class TestMutexQueue
{
   static int numSingleProducers = 6;
   static int numSingleConsumers = 4;
   static int queueMaxSize = 10;

   public static void main(String[] args)
   {
      String[] defaultArgs = {"6", "4", "10"};
      if (args.length != 0 && args.length != 3)
      {
         err.println("Usage: TestMutexQueue [<num-producers> <num-consumers> <queue-max-size>]");
         exit(1);
      }
      else if (args.length == 0)
      {
         args=defaultArgs;
         out.print("Using \"TestMutexQueue");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      numSingleProducers = Integer.parseInt(args[0]);
      numSingleConsumers = Integer.parseInt(args[1]);
      queueMaxSize = Integer.parseInt(args[2]);

      MutexQueue<Integer> sq = new MutexQueue<Integer>(new BoundedQueue<Integer>(new LinkedList<Integer>(), queueMaxSize));

      SingleProducer[] singleProducers = new SingleProducer[numSingleProducers];
      for(int i = 0; i < numSingleProducers; i++)
      {
         singleProducers[i] = new SingleProducer();
         singleProducers[i].set("single-producer #"+(i+1), sq);
      }
      SingleConsumer[] singleConsumers = new SingleConsumer[numSingleConsumers];
      for(int i = 0; i < numSingleConsumers; i++)
      {
         singleConsumers[i] = new SingleConsumer();
         singleConsumers[i].setDaemon(true);
         singleConsumers[i].set("single-consumer #"+(i+1), sq);
      }
      for(int i = 0; i < numSingleProducers; i++)
         singleProducers[i].start();
      for(int i = 0; i < numSingleConsumers; i++)
         singleConsumers[i].start();

      for(int i = 0; i < numSingleProducers; i++)
         singleProducers[i].ajoin();

      while(!sq.isEmpty())
         CThread.yield();

      CThread.pause(100);
   }

   static abstract class SQThread extends CThread
   {
      public void set(String id, MutexQueue<Integer> sq)
      {
         assert id != null && id.length() > 0;
         assert sq != null;

         this.id = id;
         this.sq = sq;
         finished = false;
      }

      public void terminate()
      {
         finished = true;
      }

      public final double maxPause = 100.0;

      public abstract void theRun();

      public void arun()
      {
         assert id != null && id.length() > 0;
         assert sq != null;

         while(!finished)
         {
            theRun();
            pause((int)(Math.random()*maxPause));
         }
      }

      protected String id = null;
      protected MutexQueue<Integer> sq = null;
      protected boolean finished = false;
   }

   static class SingleProducer extends SQThread
   {
      public void theRun()
      {
         count++;
         out.println("["+id+"]: producing unique value - "+count);
         sq.in(count);
         if (count == 100)
            terminate();
      }

      protected int count = 0;
   }

   static class SingleConsumer extends SQThread
   {
      public void theRun()
      {
         out.println("["+id+"]: consuming unique value - "+sq.peekAndOut());
      }
   }

}
