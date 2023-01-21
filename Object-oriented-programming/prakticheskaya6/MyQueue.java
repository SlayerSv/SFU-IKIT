package prakticheskaya6;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Custom class that implements the class {@link Queue}, uses generics
 * and represents the Queue data structure (FIFO). Holds data in a generic array {@code T[] array}.
 * Capacity of the array is restricted by a constant {@code MAX_ARRAY_SIZE}.
 * Implements most of the abstract methods from {@link Collection} and {@link Queue}, and some
 * custom methods for easier usage of the queue.
 * @see Collection
 * @see Queue
 */
public class MyQueue<T> implements Queue<T> {

  /**Maximal length of the array, size of the queue will be restricted by this number.*/
  static final int MAX_ARRAY_SIZE = 10;

  /**Generic array that will represent the queue and will hold all the queue elements in itself.*/
  private T[] array;

  /**Custom constructor that initializes generic array with data type defined in the class.*/
  @SuppressWarnings("unchecked")
  public MyQueue() {
    array = (T[]) new Object[MAX_ARRAY_SIZE];
  }
  
  /**
   * Uses {@code isEmpty()} to determine whether the array is empty, then displays the result
   * to a user.
   */
  public void emptyCheck() {
    if (isEmpty()) {
      System.out.println("\nThe queue is empty!");
    } else {
      System.out.println("\nThe queue is NOT empty!");
    }
  }

  /**
   * Implementation of the abstract method, checks whether the array is empty by looking at it`s
   * first element. The function assumes that the array is properly sorted by having no {@code null}
   * elements before non-{@code null} elements.
   */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /**Checks if the array is full by comparing value returned from {@code size()} to a constant
   * {@code MAX_ARRAY_SIZE}.
   */
  public boolean isFull() {
    return size() == MAX_ARRAY_SIZE;
  }

  /**
   * Function for checking the size of the array, meaning any non-{@code null} elements.
   * Runs for loop in the array and increments {@code arraySize} every time it finds
   * a non-{@code null} element.
   * @return Amount of the non-{@code null} elements in the array.
   */
  @Override
  public int size() {
    int arraySize = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        arraySize++;
      }
    }
    return arraySize;
  }
  
  /**
   * Adds a new element, passed to this function as an argument, to the end of the array.
   * Runs {@code isFull()} to check if the array has space for the new element. Checks that
   * the passed object is not null.
   * 
   * <p>Determines the index of the new element by running {@code size()}
   * (function assumes that the array is properly sorted and has no null elements between regular
   * elements). Catches most the exceptions that can occur, including {@link ClassCastException}
   * if the passed object has the wrong type for the array.
   * @param newElement Passed object that will be added to the array.
   * @return {@code true} if the new element was added, {@code false} if it was not.
   * @throws IllegalArgumentException if the passed object is null.
   * @throws IllegalStateException if the array has no space for the new element.
   */
  @Override
  public boolean add(T newElement) {
    try {
      if (newElement == null) {
        throw new IllegalArgumentException("Can`t add null elements!");
      }
      if (isFull()) {
        throw new IllegalStateException("The queue is full! Can`t add new elements!");
      }
      int indexOfNewElement = size();
      array[indexOfNewElement] = newElement;
      System.out.println("\n New element has been added to the tail of the queue!");
      return true;
    } catch (IllegalStateException | ClassCastException | IllegalArgumentException e) {
      System.out.println(e);
      return false;
    }
  }

  /**
   * Adds a new element, passed to this function as an argument, to the end of the array.
   * Runs {@code isFull()} to check if the array has space for the new element. Checks that
   * the passed object is not null. Differs from {@code add()} only in that it does not
   * throw exceptions when the checks fail, it just notifies the user and returns false.
   * 
   * <p>Determines the index of the new element by running {@code size()}
   * (function assumes that the array is properly sorted and has no null elements between regular
   * elements). Catches {@link ClassCastException} if the passed object has the wrong type
   * for the array.
   * @param newElement Passed object that will be added to the array.
   * @return {@code true} if the new element was added, {@code false} if it was not.
   */
  @Override
  public boolean offer(T newElement) {
    if (newElement == null) {
      System.out.println("Can`t add null elements!");
      return false;
    }
    if (isFull()) {
      System.out.println("\nThe queue is full! Can`t add new elements!");
      return false;
    }
    int indexOfNewElement = size();
    try {
      array[indexOfNewElement] = newElement;
      return true;
    } catch (ClassCastException e) {
      System.out.println(e);
      return false;
    }
  }

  /**
   * Retrieves and removes the head of this queue, represented by the first element of the array.
   * Function assumes that the array is properly sorted and has no {@code null} elements before
   * non-{@code null} elements. After the head was removed from the array, runs {@code sortQueue()}
   * to sort the array. Throws and catches {@link IllegalStateException} if the array is empty.
   * @return First element of the array (head of the queue) or {@code null} if the array is empty.
   * @throws IllegalStateException If the array is empty.
   */
  @Override
  public T remove() {
    try {
      if (isEmpty()) {
        throw new IllegalStateException("Can`t remove elements because the queue is empty!");
      }
      T head = array[0];
      array[0] = null;
      System.out.println("\nThe element at the head of the queue has been removed!");
      if (!isEmpty()) {
        sortQueue();
      }
      return head;
    } catch (IllegalStateException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Removes a single object from the array, if it matches the object passed to this function.
   * Checks that the passed object is not {@code null} and that the array is not empty.
   * 
   * <p>Runs for loop on the array and {@code object.equals()} on its elements to check
   * if the objects match. If they are then the function removes that element from the array
   * and breaks the loop. So only one element will be removed, even if the array contains
   * more than one matching element.
   * 
   * <p>After that runs {@code sortQueue()} to sort the array so it does not have {@code null}
   * elements before non-{@code null} elements.
   * @param object Element that matches this object will be removed from the array.
   * @return {@code true} if an element was removed, {@code false} if not.
   */
  @Override
  public boolean remove(Object object) {
    boolean isModified = false;
    if (object == null) {
      System.out.println("\nError: Passed object is null");
    } else if (isEmpty()) {
      System.out.println("\nCan`t remove elements, because the queue is empty!");
    } else {
      int size = size();
      for (int i = 0; i < size; i++) {
        if (object.equals(array[i])) {
          array[i] = null;
          isModified = true;
          if (!isEmpty()) {
            sortQueue();
          }
          break;
        }
      }
      System.out.println(isModified ? "\nThe element has been deleted!" : "\nNo such element!");
    }
    return isModified;
  }

  /**
   * Retrieves and removes the head of this queue, represented by the first element of the array.
   * Differs from {@code remove()} only in that it does not throw exceptions when the checks fail.
   * Function assumes that the array is properly sorted and has no {@code null} elements before
   * non-{@code null} elements. After the head was removed from the array, runs {@code sortQueue()}
   * to sort the array.
   * @return First element of the array (head of the queue) or {@code null} if the array is empty.
   */
  @Override
  public T poll() {
    if (isEmpty()) {
      System.out.println("\nCan`t remove elements because the queue is empty!");
      return null;
    }
    T head = array[0];
    array[0] = null;
    if (!isEmpty()) {
      sortQueue();
    }
    return head;
  }

  /**
   * Uses {@code peek()} to get the head of the queue, returns it to user and prints it 
   * on screen. In case the array is empty returns {@code null} and notifies the user.
   * @return The head of the queue or {@code null} if the queue is empty.
   */
  public T getHead() {
    if (isEmpty()) {
      System.out.println("\nCan`t get the head of the queue because it is empty!");
      return null;
    }
    T head = peek();
    System.out.println("\nThe head of the queue is '" + head + "'");
    return head;
  }

  /**
   * Gets and returns the head of queue represented by the first element of the array. Function
   * assumes that the array is properly sorted with no {@code null} elements before non-{@code null}
   * elements. In case the array is empty throws {@link IllegalStateException} and returns null.
   * @return The head of queue represented by the first element of the array or {@code null} if the
   *      array is empty.
   * @throws IllegalStateException If the array is empty.
   */
  @Override
  public T element() {
    try {
      if (array[0] == null) {
        throw new IllegalStateException("Can`t get the head of the queue because it is empty");
      }
      return (T) array[0];
    } catch (IllegalStateException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Gets and returns the head of queue represented by the first element of the array. Function
   * assumes that the array is properly sorted with no {@code null} elements before non-{@code null}
   * elements. Differs from {@code element()} only in that it does not throw exception if the array
   * is empty.
   * @return The head of queue represented by the first element of the array or {@code null} if the
   *      array is empty.
   */
  @Override
  public T peek() {
    return (T) array[0];
  }

  /**
   * Creates a copy of the element at the tail of the queue (last array element), retrieved by the
   * {@code getTail()}, then uses {@code offer()} to add this copy to the end of the same array.
   * 
   * <p>Runs {@code isEmpty()} and {@code isFull()} to make sure that this operation is possible.
   * If the queue is full or empty, then the function terminates and notifies the user.
   */
  public void duplicateTail() {
    if (isEmpty()) {
      System.out.println("\nCan`t duplicate the tail because the queue is empty!");
      return;
    }
    if (isFull()) {
      System.out.println("\nCan`t duplicate the tail because the queue is full!");
      return;
    }
    T tail = getTail();
    offer(tail);
    System.out.println("\nThe tail has been duplicated");
  }

  /**
   * Gets and returns the element at the tail of the queue (last non-{@code null} element in the
   * array). To determine the index of the last non-{@code null} element in the array runs
   * {@code size()} and substracts '1' from it.
   * 
   * <p>Checks if the array is empty by running {@code isEmpty()}, if it is then notifies the user
   * and returns null.
   * @return The tail of the queue represented by the last non-{@code null} element of the array,
   *      or {@code null} of the array is empty.
   */
  public T getTail() {
    if (isEmpty()) {
      System.out.println("\nThe queue is empty!");
      return null;
    }
    int indexOfTail = size() - 1;
    T tail = array[indexOfTail];
    return tail;
  }

  /**
   * Prints the queue to the user. Runs {@code isEmpty()} to check if the array is empty, if it is,
   * then aborts and notifies the user. Runs for loop to iterate through all elements of the array
   * and displays them on the screen. Assumes that the array is properly sorted.
   */
  public void printQueue() {
    if (isEmpty()) {
      System.out.println("\nCan`t print the queue because it is empty!");
      return;
    }
    System.out.print("\nCurrent queue: [");
    for (int i = 0; i < size(); i++) {
      if (i != size() - 1) {
        System.out.print(array[i] + ", ");
      } else {
        System.out.println(array[i] + "]");
      }
    }
  }

  /**
   * Sorts the queue, by making sure that there are no {@code null} elements in the array before the
   * non-{@code null} elements.
   * 
   * <p><b>Many functions in this class rely on this sorting, otherwise they will not properly work,
   * so this function must be always executed after removing elements and other operations that can
   * result in {@code null} elements in the array to be before non-{@code null} elements.</b>
   */
  public void sortQueue() {
    if (isEmpty()) {
      System.out.println("\nCan`t sort the queue because it is empty!");
      return;
    }
    for (int i = 0; i < array.length; i++) {
      if (array[i] == null) {
        for (int j = i + 1; j < array.length; j++) {
          if (array[j] != null) {
            array[i] = array[j];
            array[j] = null;
            break;
          }
        }
      }
    }
  }

  /**
   * Checks if the queue has element that matches the object passed to this function as an argument.
   * Runs for loop on all non-{@code null} elements and uses {@code object.equals(objects)} to check
   * if the objects match. Assumes that the array is properly sorted.
   * 
   * <p>Can`t check for {@code null} objects. Can`t check if the array is empty. Throws exceptions.
   * @param object The object to which all of the elements of the array will be compared.
   * @return {@code true} if the array has an element that matches the passed object, {@code false}
   *      if it does not have matching object.
   * @throws IllegalArgumentException If the passed object is null.
   * @throws IllegalStateException If the array is empty.
   */
  @Override
  public boolean contains(Object object) {
    try {
      if (object == null) {
        throw new IllegalArgumentException("\nCan`t check for null objects!");
      }
      if (isEmpty()) {
        throw new IllegalStateException("\nThe queue is empty!");
      }
      boolean hasElement = false;
      for (int i = 0; i < size(); i++) {
        if (array[i].equals(object)) {
          hasElement = true;
          break;
        }
      }
      return hasElement;
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println(e);
      return false;
    }
  }

  /**
   * Clears the queue by turning all of the elements in the array to {@code null}. If the queue is
   * already empty aborts and notifies the user.
   */
  @Override
  public void clear() {
    if (isEmpty()) {
      System.out.println("\nThe queue is already empty!");
      return;
    }
    for (int i = 0; i < array.length; i++) {
      array[i] = null;
    }
    System.out.println("\nThe queue has been cleared!");
  }

  @Override
  public Iterator<T> iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] toArray() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }
}