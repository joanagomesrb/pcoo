package concurrent.support;

import static java.lang.System.*;

import pt.ua.concurrent.Mutex;
import pt.ua.concurrent.MutexCV;

public class MutexQueue<T>
{
   public MutexQueue(BoundedQueue<T> queue)
   {
      assert queue != null;

      this.queue = queue;
      intra = new Mutex();
      isEmptyCV = intra.newCV();
      isFullCV = intra.newCV();
   }

   public void in(T e)
   {
      intra.lock();
      try
      {
         while(queue.isFull())
            isFullCV.await();
         queue.in(e);
         isEmptyCV.broadcast();
      }
      finally
      {
         intra.unlock();
      }
   }

   public void out()
   {
      intra.lock();
      try
      {
         while(queue.isEmpty())
            isEmptyCV.await();
         queue.out();
         isFullCV.broadcast();
      }
      finally
      {
         intra.unlock();
      }
   }

   public T peek()
   {
      T result;
      intra.lock();
      try
      {
         while(queue.isEmpty())
            isEmptyCV.await();
         result = queue.peek();
      }
      finally
      {
         intra.unlock();
      }
      return result;
   }

   public T peekAndOut()
   {
      T result;
      intra.lock();
      try
      {
         while(queue.isEmpty())
            isEmptyCV.await();
         result = queue.peek();
         queue.out();
         isFullCV.broadcast();
      }
      finally
      {
         intra.unlock();
      }
      return result;
   }

   public void clear()
   {
      intra.lock();
      try
      {
         queue.clear();
      }
      finally
      {
         intra.unlock();
      }
   }

   public int size()
   {
      int result;
      intra.lock();
      try
      {
         result = queue.size();
      }
      finally
      {
         intra.unlock();
      }
      return result;
   }

   public boolean isEmpty()
   {
      boolean result;
      intra.lock();
      try
      {
         result = queue.isEmpty();
      }
      finally
      {
         intra.unlock();
      }
      return result;
   }

   public boolean isFull()
   {
      boolean result;
      intra.lock();
      try
      {
         result = queue.isFull();
      }
      finally
      {
         intra.unlock();
      }
      return result;
   }

   public void interruptWaitingThreads()
   {
      intra.interruptWaitingThreads();
   }

   protected final BoundedQueue<T> queue;
   protected Mutex intra;
   protected MutexCV isEmptyCV;
   protected MutexCV isFullCV;
}

