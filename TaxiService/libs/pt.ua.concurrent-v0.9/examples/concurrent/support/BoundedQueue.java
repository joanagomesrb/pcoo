package concurrent.support;

import static java.lang.System.*;

import java.util.Queue;

public class BoundedQueue<T>
{
   public BoundedQueue(Queue<T> queue, int maxSize)
   {
      assert queue != null;
      assert maxSize >= 0;

      this.queue = queue;
      this.maxSize = maxSize;
   }

   public void in(T e)
   {
      assert !isFull();

      queue.add(e);
   }

   public void out()
   {
      assert !isEmpty();

      queue.remove();
   }

   public T peek()
   {
      assert !isEmpty();

      return queue.peek();
   }

   public int size()
   {
      return queue.size();
   }

   public boolean isEmpty()
   {
      return queue.isEmpty();
   }

   public boolean isFull()
   {
      return size() == maxSize;
   }

   public void clear()
   {
      queue.clear();
   }

   protected final Queue<T> queue;
   protected final int maxSize;
}

