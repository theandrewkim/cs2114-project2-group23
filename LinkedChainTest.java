package rentals;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Tests for LinkedChain, including the Group 23 changes: add() appends at the
 * tail, the tail reference stays correct after removals, and the chain can be
 * walked with an iterator / for-each loop.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class LinkedChainTest
        extends student.TestCase
{
    // ~ Fields ................................................................

    private LinkedChain<String> empty;
    private LinkedChain<String> abc; // "a", "b", "c" added in that order

    // ~ Setup .................................................................

    /**
     * Creates an empty chain and a chain holding "a", "b", "c".
     */
    public void setUp()
    {
        empty = new LinkedChain<String>();
        abc = new LinkedChain<String>();
        abc.add("a");
        abc.add("b");
        abc.add("c");
    }

    // ~ Constructor / size ....................................................

    /**
     * A new chain is empty.
     */
    public void testConstructor()
    {
        assertEquals(0, empty.size());
        assertTrue(empty.isEmpty());
        assertEquals("", empty.toString());
    }


    /**
     * size() and isEmpty() track adds and removes.
     */
    public void testSizeAndIsEmpty()
    {
        assertEquals(3, abc.size());
        assertFalse(abc.isEmpty());
        abc.remove();
        abc.remove();
        abc.remove();
        assertEquals(0, abc.size());
        assertTrue(abc.isEmpty());
    }

    // ~ add ...................................................................

    /**
     * add() appends to the end, so entries keep insertion order.
     */
    public void testAddKeepsInsertionOrder()
    {
        assertTrue(empty.add("x"));
        assertTrue(empty.add("y"));
        assertEquals("x", empty.get(0));
        assertEquals("y", empty.get(1));
        assertEquals("a", abc.get(0));
        assertEquals("b", abc.get(1));
        assertEquals("c", abc.get(2));
    }


    /**
     * add(null) throws and leaves the chain unchanged.
     */
    public void testAddNull()
    {
        Exception thrown = null;
        try
        {
            abc.add(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(3, abc.size());
    }

    // ~ remove() ..............................................................

    /**
     * remove() takes entries from the front.
     */
    public void testRemoveFirst()
    {
        assertEquals("a", abc.remove());
        assertEquals("b", abc.remove());
        assertEquals(1, abc.size());
        assertEquals("c", abc.get(0));
    }


    /**
     * remove() on an empty chain returns null.
     */
    public void testRemoveFirstEmpty()
    {
        assertNull(empty.remove());
        assertEquals(0, empty.size());
    }


    /**
     * Emptying the chain with remove() resets the tail, so a later add()
     * works.
     */
    public void testRemoveFirstUntilEmptyThenAdd()
    {
        abc.remove();
        abc.remove();
        abc.remove();
        abc.add("d");
        assertEquals(1, abc.size());
        assertEquals("d", abc.get(0));
        abc.add("e");
        assertEquals("e", abc.get(1));
    }

    // ~ remove(T) .............................................................

    /**
     * Removing the head entry.
     */
    public void testRemoveEntryHead()
    {
        assertTrue(abc.remove("a"));
        assertEquals(2, abc.size());
        assertEquals("b", abc.get(0));
        assertEquals("c", abc.get(1));
    }


    /**
     * Removing a middle entry.
     */
    public void testRemoveEntryMiddle()
    {
        assertTrue(abc.remove("b"));
        assertEquals(2, abc.size());
        assertEquals("a", abc.get(0));
        assertEquals("c", abc.get(1));
    }


    /**
     * Removing the tail entry moves the tail back, so the next add() goes
     * right after the new last entry.
     */
    public void testRemoveEntryTailThenAdd()
    {
        assertTrue(abc.remove("c"));
        assertEquals(2, abc.size());
        abc.add("d");
        assertEquals(3, abc.size());
        assertEquals("a", abc.get(0));
        assertEquals("b", abc.get(1));
        assertEquals("d", abc.get(2));
    }


    /**
     * Removing the only entry, then adding again.
     */
    public void testRemoveEntryOnlyThenAdd()
    {
        empty.add("x");
        assertTrue(empty.remove("x"));
        assertTrue(empty.isEmpty());
        empty.add("y");
        assertEquals(1, empty.size());
        assertEquals("y", empty.get(0));
    }


    /**
     * Only the first of several equal entries is removed.
     */
    public void testRemoveEntryDuplicate()
    {
        abc.add("a");
        assertTrue(abc.remove("a"));
        assertEquals(3, abc.size());
        assertEquals("b", abc.get(0));
        assertEquals("a", abc.get(2));
    }


    /**
     * remove(T) returns false for missing entries, null and empty chains.
     */
    public void testRemoveEntryNotFound()
    {
        assertFalse(abc.remove("z"));
        assertFalse(abc.remove(null));
        assertFalse(empty.remove("a"));
        assertEquals(3, abc.size());
    }

    // ~ contains / count ......................................................

    /**
     * contains() finds entries anywhere in the chain.
     */
    public void testContains()
    {
        assertTrue(abc.contains("a"));
        assertTrue(abc.contains("c"));
        assertFalse(abc.contains("z"));
        assertFalse(abc.contains(null));
        assertFalse(empty.contains("a"));
    }


    /**
     * count() counts every equal entry.
     */
    public void testCount()
    {
        abc.add("a");
        abc.add("a");
        assertEquals(3, abc.count("a"));
        assertEquals(1, abc.count("b"));
        assertEquals(0, abc.count("z"));
        assertEquals(0, abc.count(null));
        assertEquals(0, empty.count("a"));
    }

    // ~ clear .................................................................

    /**
     * clear() empties the chain, and it can be used again afterwards.
     */
    public void testClear()
    {
        abc.clear();
        assertEquals(0, abc.size());
        assertTrue(abc.isEmpty());
        assertNull(abc.remove());
        abc.add("x");
        assertEquals(1, abc.size());
        assertEquals("x", abc.get(0));
    }

    // ~ get ...................................................................

    /**
     * get() rejects indexes below 0.
     */
    public void testGetNegativeIndex()
    {
        Exception thrown = null;
        try
        {
            abc.get(-1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IndexOutOfBoundsException);
    }


    /**
     * get() rejects index == size.
     */
    public void testGetIndexTooLarge()
    {
        Exception thrown = null;
        try
        {
            abc.get(3);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IndexOutOfBoundsException);
    }

    // ~ iterator ..............................................................

    /**
     * A for-each loop visits entries head to tail.
     */
    public void testForEachOrder()
    {
        StringBuilder visited = new StringBuilder();
        for (String entry : abc)
        {
            visited.append(entry);
        }
        assertEquals("abc", visited.toString());
    }


    /**
     * The iterator reports when it runs out, and next() then throws.
     */
    public void testIteratorExhausted()
    {
        Iterator<String> iter = abc.iterator();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());
        assertFalse(iter.hasNext());

        Exception thrown = null;
        try
        {
            iter.next();
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof NoSuchElementException);
    }


    /**
     * An empty chain's iterator has nothing to visit.
     */
    public void testIteratorEmpty()
    {
        assertFalse(empty.iterator().hasNext());
    }

    // ~ toArray ...............................................................

    /**
     * toArray() copies entries head to tail.
     */
    public void testToArray()
    {
        String[] values = abc.toArray(new String[3]);
        assertEquals("a", values[0]);
        assertEquals("b", values[1]);
        assertEquals("c", values[2]);

        String[] bigger = abc.toArray(new String[5]);
        assertEquals("c", bigger[2]);
        assertNull(bigger[3]);
    }


    /**
     * toArray() rejects a null array.
     */
    public void testToArrayNull()
    {
        Exception thrown = null;
        try
        {
            abc.toArray(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * toArray() rejects an array that is too small.
     */
    public void testToArrayTooSmall()
    {
        Exception thrown = null;
        try
        {
            abc.toArray(new String[2]);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ toString ..............................................................

    /**
     * toString() lists one entry per line, head to tail.
     */
    public void testToString()
    {
        assertEquals("a\nb\nc", abc.toString());
        empty.add("only");
        assertEquals("only", empty.toString());
    }
}
