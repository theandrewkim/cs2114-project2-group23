package rentals;
/**
 * The public contract for a chain of linked entries. LinkedChain implements
 * this interface, and because it extends Iterable, any ChainInterface can be
 * walked with a for-each loop.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 * @param <T>
 *            the type of entries stored in the chain
 */

public interface ChainInterface<T> extends Iterable<T>
{
    /** Adds newEntry to the end of the chain. @return true if added */
    boolean add(T newEntry);

    /** Removes and returns the first entry, or null if the chain is empty. */
    T remove();

    /** Removes one occurrence of anEntry. @return true if it was found and removed */
    boolean remove(T anEntry);

    /** @return true if the chain contains anEntry */
    boolean contains(T anEntry);

    /** @return the number of times anEntry appears in the chain */
    int count(T anEntry);

    /** @return the number of entries in the chain */
    int size();

    /** @return true if the chain has no entries */
    boolean isEmpty();

    /** Removes all entries from the chain. */
    void clear();

    /** @return the entry at the given index (0-based) */
    T get(int index);
}