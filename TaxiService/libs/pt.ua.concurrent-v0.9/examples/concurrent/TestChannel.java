package concurrent;

import static java.lang.System.*;

import java.util.Scanner;
import pt.ua.concurrent.*;

public class TestChannel
{
   public static void main(String[] args)
   {
      Channel<String> ch = new Channel<String>();
      Sender sender = new Sender(ch);
      Receiver receiver = new Receiver(ch);
      sender.start();
      receiver.start();
      sender.ajoin();
      ch.interruptWaitingThreads();
   }

   static class Sender extends CThread
   {
      public final static Scanner scin = new Scanner(System.in);

      public Sender(Channel<String> ch)
      {
         assert ch != null;

         this.ch = ch;
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
               ch.put(text);
            }
         }
      }

      private Channel<String> ch;
   }

   static class Receiver extends CThread
   {
      public Receiver(Channel<String> ch)
      {
         assert ch != null;

         this.ch = ch;
      }

      public void arun()
      {
         boolean finished = false;
         while(!finished)
         {
            try
            {
               String text = ch.take();
               out.println("Received: \""+text+"\" ... ");
            }
            catch(ThreadInterruptedException e)
            {
               finished = true;
            }
         }
      }

      private Channel<String> ch;
   }
}
