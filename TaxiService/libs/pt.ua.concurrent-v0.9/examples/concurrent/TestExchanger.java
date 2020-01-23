package concurrent;

import static java.lang.System.*;

import java.util.Scanner;
import pt.ua.concurrent.*;

public class TestExchanger
{
   public static void main(String[] args)
   {
      Exchanger<String> ex = new Exchanger<String>();
      Sender sender = new Sender(ex);
      Receiver receiver = new Receiver(ex);
      //receiver.setDaemon(true);
      sender.start();
      receiver.start();
      sender.ajoin();
      ex.interruptWaitingThreads();
   }

   static class Sender extends CThread
   {
      public final static Scanner scin = new Scanner(System.in);

      public Sender(Exchanger<String> ex)
      {
         assert ex != null;

         this.ex = ex;
      }

      public void arun()
      {
         boolean finished = false;
         while(!finished)
         {
            out.print("Text to send: ");
            String text = null;
            if (scin.hasNextLine())
               text = scin.nextLine();
            finished = text == null || text.length() == 0;
            if (!finished)
            {
               out.println("Send: \""+text+"\" ... ");
               ex.exchange(text);
            }
         }
      }

      private final Exchanger<String> ex;
   }

   static class Receiver extends CThread
   {
      public Receiver(Exchanger<String> ex)
      {
         assert ex != null;

         this.ex = ex;
      }

      public void arun()
      {
         boolean finished = false;
         while(!finished)
         {
            try
            {
               String text = ex.exchange(null);
               out.println("Received: \""+text+"\" ... ");
            }
            catch(ThreadInterruptedException e)
            {
               finished = true;
            }
         }
      }

      private final Exchanger<String> ex;
   }
}
