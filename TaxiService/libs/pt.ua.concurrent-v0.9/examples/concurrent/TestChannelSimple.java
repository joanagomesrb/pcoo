package concurrent;

import static java.lang.System.*;

import java.util.Scanner;
import pt.ua.concurrent.*;

public class TestChannelSimple
{
   public static void main(String[] args)
   {
      String[] defaultArgs = {"10"};
      if (args.length > 1)
      {
         err.println("Usage: TestChannelSimple [<N>]");
         exit(1);
      }
      else if (args.length == 0)
      {
         args=defaultArgs;
         out.print("Using \"TestChannelSimple");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      int N = Integer.parseInt(args[0]);
      Channel<String> ch = new Channel<String>();
      Sender sender = new Sender(ch, N);
      Receiver receiver = new Receiver(ch, N);
      sender.start();
      receiver.start();
   }

   static class Sender extends CThread
   {
      public Sender(Channel<String> ch, int N)
      {
         assert ch != null;

         this.ch = ch;
         this.N = N;
      }

      public void arun()
      {
         String text;
         for(int i = 0; i < N; i++)
         {
            text = "Menssagem número "+(i+1);
            out.println("Send: \""+text+"\" ... ");
            ch.put(text);
            pause((int)(Math.random()*5));
         }
      }

      private final Channel<String> ch;
      private final int N;
   }

   static class Receiver extends CThread
   {
      public Receiver(Channel<String> ch, int N)
      {
         assert ch != null;

         this.ch = ch;
         this.N = N;
      }

      public void arun()
      {
         String text;
         for(int i = 0; i < N; i++)
         {
            text = ch.take();
            out.println("Received: \""+text+"\" ... ");
            pause((int)(Math.random()*5));
         }
      }

      private final Channel<String> ch;
      private final int N;
   }
}
