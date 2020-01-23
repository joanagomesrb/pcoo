package concurrent.support;

import static java.lang.System.*;
import java.util.Stack;
import pt.ua.concurrent.*;

public class ActorStack<T> extends Actor
{
   public ActorStack(Stack<T> s)
   {
      super();
      assert s != null: "Null reference to stack";
      this.stack = s;
      start();
   }

   protected final Stack<T> stack;

   public void push(T elem)
   {
      pushFuture(elem); // lazy
   }

   public Future pushFuture(final T elem)
   {
      final Future result = new Future(false);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final T e = elem;
         final Future future = result;
         public void execute()
         {
            out.println("push: "+elem);
            s.push(e);
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }

   public void pop()
   {
      popFuture(); // lazy
   }

   public Future popFuture()
   {
      final Future result = new Future(false);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final Future future = result;
         public boolean concurrentPrecondition() { return !s.isEmpty(); }
         public void execute()
         {
            s.pop();
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }

   public T top()
   {
      Future<T> future = topFuture();
      return future.result(); // eager
   }

   public Future<T> topFuture()
   {
      final Future<T> result = new Future<T>(true);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final Future<T> future = result;
         public boolean concurrentPrecondition() { return !s.isEmpty(); }
         public void execute()
         {
            future.setResult(s.peek());
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }

   public T topOut()
   {
      Future<T> future = topOutFuture();
      return future.result(); // eager
   }

   public Future<T> topOutFuture()
   {
      final Future<T> result = new Future<T>(true);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final Future<T> future = result;
         public boolean concurrentPrecondition() { return !s.isEmpty(); }
         public void execute()
         {
            future.setResult(s.peek());
            s.pop();
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }

   public boolean isEmpty()
   {
      Future<Boolean> future = isEmptyFuture();
      return future.result(); // eager
   }

   public Future<Boolean> isEmptyFuture()
   {
      final Future<Boolean> result = new Future<Boolean>(true);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final Future<Boolean> future = result;
         public void execute()
         {
            future.setResult(s.isEmpty());
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }

   public int size()
   {
      Future<Integer> future = sizeFuture();
      return future.result(); // eager
   }

   public Future<Integer> sizeFuture()
   {
      final Future<Integer> result = new Future<Integer>(true);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final Future<Integer> future = result;
         public void execute()
         {
            future.setResult(s.size());
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }

   public void clear()
   {
      clearFuture(); // lazy
   }

   public Future clearFuture()
   {
      final Future result = new Future(false);
      Routine r = new Routine()
      {
         final Stack<T> s = stack;
         final Future future = result;
         public void execute()
         {
            s.clear();
            futureDone(future);
         }
      };
      inPendingRoutine(r);
      return result;
   }
}

