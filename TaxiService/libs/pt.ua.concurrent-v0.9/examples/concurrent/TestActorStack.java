package concurrent;

import static java.lang.System.*;
import java.util.Stack;
import pt.ua.concurrent.*;
import concurrent.support.*;

public class TestActorStack
{
   public static void main(String[] args)
   {
      final ActorStack<Integer> actor = new ActorStack<Integer>(new Stack<Integer>());

      new CThread(new CRunnable() {
         public void arun() {
            int v = actor.top();
            out.println("Top: "+v);
         }
      }).start();

      new CThread(new CRunnable() {
         public void arun() {
            CThread.pause(2000);
            out.println("start push");
            for(int i = 0; i < 10; i++)
            {
               actor.push(i);
               //Future f = actor.pushFuture(i);
               //f.done(); // eager procedure!
            }
            out.println("finish push");
         }
      }).start();
      for(int i = 0; i < 10; i++)
      {
         out.println(actor.topOut()); // to avoid external synchronization need
      }
      actor.terminate();
   }
}

