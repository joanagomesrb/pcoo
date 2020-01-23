package concurrent;

import static java.lang.System.*;
import pt.ua.concurrent.*;

public class TestEvent
{
   public static void main(String[] args)
   {
      final Event ev = new Event(false);
      Runnable thr_main = () -> {
         out.println("[thr_main] waiting true:");
         ev.await(true);
         out.println("[thr_main] waiting false:");
         ev.await(false);
         out.println("[thr_main] done.");
      };

      CThread thread1 = new CThread(thr_main);
      thread1.start();
      out.println("[main] setting event:");
      ev.set();
      out.println("[main] reseting event:");
      ev.reset();
      out.println("[main] waiting thread termination:");
      thread1.ajoin();
      out.println("[main] done.");
   }
}

