package concurrent;

import static java.lang.System.*;
import pt.ua.concurrent.*;

public class TestTerminationPolicy
{
   static final int step = 100;

   public static void main(String[] args)
   {
      if (args.length != 1)
      {
         err.println("Usage: TestTerminationPolicy (debug | propagate | ignore | ignore-debug)");
         exit(1);
      }
      /*
      out.print("Using \"TestTerminationPolicy");
      for(int i = 0; i < args.length; i++)
         out.print(" "+args[i]);
      out.println("\"");
      out.println();
      */

      CThread t1 = new CThread(new CRunnable() {public void arun() {CThread.pause((int)(Math.random()*1000)); throw new NullPointerException();} });
      if (args[0].equalsIgnoreCase("debug"))
         t1.setTerminationPolicy(CThread.TerminationPolicy.DEBUG);
      else if (args[0].equalsIgnoreCase("propagate"))
         t1.setTerminationPolicy(CThread.TerminationPolicy.PROPAGATE);
      else if (args[0].equalsIgnoreCase("ignore"))
         t1.setTerminationPolicy(CThread.TerminationPolicy.IGNORE);
      else if (args[0].equalsIgnoreCase("ignore-debug"))
         t1.setTerminationPolicy(CThread.TerminationPolicy.IGNORE_DEBUG);
      else
      {
         err.println("ERROR: invalid argument \""+args[0]+"\"");
         exit(2);
      }
      t1.start();
      try
      {
         int t = 5;
         out.println("Waiting "+t+" seconds...");
         for(int i = 1; i <= t*1000; i+=step)
         {
            out.println("[main] iteration "+i);
            CThread.pause(step);
         }
         t1.ajoin();
      }
      catch(ThreadInterruptedException e)
      {
         out.println("Main interrupted!!!");
         if (t1.terminationPolicy() == CThread.TerminationPolicy.PROPAGATE)
            t1.sourceException().printStackTrace();
      }
      out.print("Child thread ");
      if (t1.failed())
         out.println("failed!");
      else
         out.println("succeeded!");
      out.println();
      out.println("Main terminated!!!");
   }
}

