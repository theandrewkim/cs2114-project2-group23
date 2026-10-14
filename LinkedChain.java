package rentals;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic chain of linked nodes. Use it as the storage inside your own
 * classes. This class owns storage only: nodes, the head and tail references,
 * and the count. It contains no application logic. A class that needs a
 * collection should hold a LinkedChain field and call its methods.
 *
 * Group 23 changes from the CS 2114 starter: add() now appends at the END of
 * the chain using a tail reference, so entries come back (from get(),
 * toArray(), iteration and toString()) in the order they were added. The class
 * implements ChainInterface, which extends Iterable, so callers can use a
 * for-each loop.
 *
 * Time complexity, where n is the number of entries: add O(1), remove() O(1),
 * remove(T) O(n), clear O(1), get O(n), contains O(n), count O(n), size O(1),
 * isEmpty O(1), toArray O(n), iterator O(1) to create and O(n) for a full pass.
 *
 * @author CS 2114 Staff
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker (modifications)
 * @version Oct 14, 2026
 * @param <T>
 *            the type of entries stored in the chain
 */
public class LinkedChain<T>
        implements ChainInterface<T>
{
    // ~ Fields ................................................................

    private Node<T> head; // first node, or null when empty
    private Node<T> tail; // last node, or null when empty
    private int size; // number of nodes in the chain

    // ~ Constructors ..........................................................

    /**
     * Creates an empty chain.
     */
    public LinkedChain()
    {
        head = null;
        tail = null;
        size = 0;
    }

    // ~ Public Methods ........................................................

    /**
     * Adds a new entry to the end of this chain.
     *
     * @param newEntry
     *            the object to be added; must not be null
     * @return true
     * @throws IllegalArgumentException
     *             if newEntry is null
     */
    @Override
    public boolean add(T newEntry)
    {
        requireNonNull(newEntry);
        Node<T> newNode = new Node<T>(newEntry);
        if (head == null)
        {
            head = newNode;
        }
        else
        {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
        return true;
    }


    /**
     * Removes and returns the first entry in this chain, if possible.
     *
     * @return the removed entry, or null if the chain is empty
     */
    @Override
    public T remove()
    {
        if (head == null)
        {
            return null;
        }
        T result = head.data;
        head = head.next;
        if (head == null)
        {
            tail = null;
        }
        size--;
        return result;
    }


    /**
     * Removes the first occurrence of a given entry from this chain. If the
     * last node is removed, tail moves back to the new last node.
     *
     * @param anEntry
     *            the entry to be removed
     * @return true if the removal was successful, or false if not
     */
    @Override
    public boolean remove(T anEntry)
    {
        if (anEntry == null || head == null)
        {
            return false;
        }
        if (anEntry.equals(head.data))
        {
            remove();
            return true;
        }
        Node<T> previous = head;
        Node<T> current = head.next;
        while (current != null)
        {
            if (anEntry.equals(current.data))
            {
                previous.next = current.next;
                if (current == tail)
                {
                    tail = previous;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }


    /**
     * Removes all entries from this chain.
     */
    @Override
    public void clear()
    {
        head = null;
        tail = null;
        size = 0;
    }


    /**
     * Gets the entry at the given position without removing it.
     *
     * @param index
     *            the zero-based position
     * @return the entry at that position
     * @throws IndexOutOfBoundsException
     *             if index is negative or index is greater than or equal to
     *             size()
     */
    @Override
    public T get(int index)
    {
        if (index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++)
        {
            current = current.next;
        }
        return current.data;
    }


    /**
     * Tests whether this chain contains a given entry.
     *
     * @param anEntry
     *            the entry to locate
     * @return true if the chain contains anEntry, or false if not
     */
    @Override
    public boolean contains(T anEntry)
    {
        return count(anEntry) > 0;
    }


    /**
     * Counts the number of times a given entry appears in this chain.
     *
     * @param anEntry
     *            the entry to be counted
     * @return the number of times anEntry appears in the chain
     */
    @Override
    public int count(T anEntry)
    {
        if (anEntry == null)
        {
            return 0;
        }
        int frequency = 0;
        Node<T> current = head;
        while (current != null)
        {
            if (anEntry.equals(current.data))
            {
                frequency++;
            }
            current = current.next;
        }
        return frequency;
    }


    /**
     * Gets the number of entries currently in this chain.
     *
     * @return the integer number of entries in the chain
     */
    @Override
    public int size()
    {
        return size;
    }


    /**
     * Sees whether this chain is empty.
     *
     * @return true if the chain has no entries, or false if not
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }


    /**
     * Copies every entry in this chain into the given array, first entry first.
     *
     * @param values
     *            an array of the correct type to fill; it must have length at
     *            least size()
     * @return the array containing all entries in the chain
     * @throws IllegalArgumentException
     *             if values is null or too small
     */
    public T[] toArray(T[] values)
    {
        if (values == null)
        {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (values.length < size)
        {
            throw new IllegalArgumentException(
                    "Array of length " + values.length + " is too small for "
                            + size + " entries");
        }
        int index = 0;
        Node<T> current = head;
        while (current != null)
        {
            values[index] = current.data;
            index++;
            current = current.next;
        }
        return values;
    }


    /**
     * Returns an iterator that walks the chain from head to tail.
     *
     * @return an iterator over the entries in this chain
     */
    @Override
    public Iterator<T> iterator()
    {
        return new ChainIterator();
    }


    /**
     * Returns every entry's toString(), one per line, head to tail. An empty
     * chain gives an empty string.
     *
     * @return a string representation of this chain
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null)
        {
            sb.append(current.data);
            if (current.next != null)
            {
                sb.append("\n");
            }
            current = current.next;
        }
        return sb.toString();
    }

    // ~ Private Methods .......................................................

    /**
     * Rejects null entries.
     *
     * @param entry
     *            the entry to check
     * @throws IllegalArgumentException
     *             if entry is null
     */
    private void requireNonNull(T entry)
    {
        if (entry == null)
        {
            throw new IllegalArgumentException("Cannot add null to chain");
        }
    }

    // ~ Nested Classes ........................................................

    /**
     * Walks the chain from head to tail, one node per call to next().
     */
    private class ChainIterator
            implements Iterator<T>
    {
        private Node<T> nextNode; // node whose data next() returns

        /**
         * Creates an iterator positioned at the head of the chain.
         */
        private ChainIterator()
        {
            nextNode = head;
        }


        /**
         * @return true if there is another entry to visit
         */
        @Override
        public boolean hasNext()
        {
            return nextNode != null;
        }


        /**
         * @return the next entry in the chain
         * @throws NoSuchElementException
         *             if every entry has already been visited
         */
        @Override
        public T next()
        {
            if (nextNode == null)
            {
                throw new NoSuchElementException("No more entries");
            }
            T result = nextNode.data;
            nextNode = nextNode.next;
            return result;
        }
    }


    /**
     * A node in the chain. Holds one entry and a link to the next node.
     *
     * @param <E>
     *            the type of data held in the node
     */
    private static class Node<E>
    {
        private E data; // entry stored in this node
        private Node<E> next; // link to the next node, or null

        /**
         * Creates a node with the given data and no next node.
         *
         * @param dataPortion
         *            the entry to store
         */
        private Node(E dataPortion)
        {
            this(dataPortion, null);
        }


        /**
         * Creates a node with the given data and next link.
         *
         * @param dataPortion
         *            the entry to store
         * @param nextNode
         *            the node that follows this one
         */
        private Node(E dataPortion, Node<E> nextNode)
        {
            data = dataPortion;
            next = nextNode;
        }
    }
}
