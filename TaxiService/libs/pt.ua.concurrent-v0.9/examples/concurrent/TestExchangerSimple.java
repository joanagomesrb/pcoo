package concurrent;

import static java.lang.System.*;

import java.util.Scanner;
import pt.ua.concurrent.*;

public class TestExchangerSimple
{
   public static void main(String[] args)
   {
      String[] defaultArgs = {"4"};
      if (args.length > 1)
      {
         err.println("Usage: TestExchangerSimple [<N>]");
         exit(1);
      }
      else if (args.length == 0)
      {
         args=defaultArgs;
         out.print("Using \"TestExchangerSimple");
         for(int i = 0; i < args.length; i++)
            out.print(" "+args[i]);
         out.println("\" as default");
         out.println("");
      }

      int N = Integer.parseInt(args[0]);
      Exchanger<String> ex = new Exchanger<String>();
      ExchangerThread exT1 = new ExchangerThread(ex, N);
      ExchangerThread exT2 = new ExchangerThread(ex, N);
      exT1.start();
      exT2.start();
   }

   static class ExchangerThread extends CThread
   {
      public ExchangerThread(Exchanger<String> ex, int N)
      {
         assert ex != null;

         this.ex = ex;
         this.N = N;
      }

      public void arun()
      {
         String text;
         for(int i = 0; i < N; i++)
         {
            text = "["+currentThreadID()+"] Mensagem número "+(i+1);
            out.println("Exchange: \""+text+"\" ... ");
            ex.exchange(text);
            pause((int)(Math.random()*5));
         }
      }

      private final Exchanger<String> ex;
      private final int N;
   }
}
