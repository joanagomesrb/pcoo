package concurrent;

import static java.lang.System.*;

import pt.ua.concurrent.*;

public class TestDynamicBarrier
{
   public static void main(String[] args)
   {
      String[] defaultArgs = {"10"};
      if (args.length > 1)
      {
         err.println("Usage: TestDynamicBarrier [<num-threads>]");
         exit(1);
      }
      else if (args.length == 0)
      {
         args=defaultArgs;
         out.print("Using \"TestDynamicBarrier");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      int numThreads = 0;
      try
      {
         numThreads = Integer.parseInt(args[0]);
      }
      catch(NumberFormatException e)
      {
         err.println("ERROR: invalid number of thread \""+args[0]+"\"");
         exit(2);
      }
      if (numThreads < 0)
      {
         err.println("ERROR: invalid number of thread \""+args[0]+"\"");
         exit(2);
      }
      final DynamicBarrier barrier = new DynamicBarrier();
      Runnable thread = () -> {
         barrier.signIn();
         CThread.pause((int)(100+Math.random()*500));
         out.println(""+CThread.currentThread()+" waiting on barrier #1...");
         barrier.await();
         out.println(""+CThread.currentThread()+" released from barrier #1...");
         CThread.pause((int)(100+Math.random()*500));
         out.println(""+CThread.currentThread()+" waiting on barrier #2...");
         barrier.await();
         out.println(""+CThread.currentThread()+" released from barrier #2...");
      };
      barrier.signIn();
      for(int i = 0; i < numThreads; i++)
         new CThread(thread).start();
      out.println(""+CThread.currentThread()+" waiting on barrier #1...");
      barrier.await();
      out.println(""+CThread.currentThread()+" released from barrier #1...");
      out.println(""+CThread.currentThread()+" waiting on barrier #2...");
      barrier.await();
      out.println(""+CThread.currentThread()+" released from barrier #2...");
   }
}

