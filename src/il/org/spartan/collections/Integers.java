/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.collections;

import org.eclipse.jdt.annotation.*;

/** An unsorted set of integers supporting the basic set operations:
 * {@link #add} , {@link #contains} and {@link #remove}.
 * <p>
 * Implementation is with a linear hash table s, with quadratic probes (i.e.,
 * +1, +3, +6, +10, +15, ... modulo the table size). Array capacity is always a
 * power of two, and is doubled when the load goes above {@link #MAX_LOAD};
 * capacity is halved when the load drops below {@value #MIN_LOAD}. When the
 * fraction of removed keys goes below {@link #REMOVE_LOAD}, the table is
 * rehashed.
 * @author Yossi Gil
 * @since December 2010 */
public class Integers {
  /** */
  public static final float MAX_LOAD = 0.75f;
  /** */
  public static final int MIN_CAPACITY = 4;
  /** */
  public static final float MIN_LOAD = 0.25f;
  /** */
  public static final float REMOVE_LOAD = 0.20f;

  static int hash(final int i) {
    final int $ = i ^ i >>> 12 ^ i >>> 20;
    return $ ^ $ >>> 4 ^ $ >>> 7;
  }

  private static int roundUp(final int i) {
    int $ = 1;
    while ($ < i)
      $ <<= 1;
    return $;
  }

  private boolean[] occupied;
  private boolean[] placeholder;
  private int removed;
  private int size;
  protected int[] data;

  /** Instantiates this class */
  public Integers() {
    this(MIN_CAPACITY);
  }

  /** Instantiate this class, using a given size for the hash table.
   * @param initialCapacity suggests a hash table size, will be rounded up to
   *        the next power of two. */
  public Integers(final int initialCapacity) {
    final int capacity = Math.max(MIN_CAPACITY, roundUp(initialCapacity));
    data = new int @NonNull [capacity];
    occupied = new boolean @NonNull [capacity];
    placeholder = new boolean @NonNull [capacity];
    size = removed = 0;
    subclassReset(capacity);
  }

  /** Add an integer to the set, if it is not already there.
   * @param n an arbitrary integer
   * @return <code><b>this</b></code> */
  public Integers add(final int n) {
    final int i = find(n);
    assert i >= -1 && i < capacity();
    if (i < 0)
      return this;
    data[i] = n;
    occupied[i] = true;
    if (++size > MAX_LOAD * capacity())
      rehash(data.length << 1);
    return this;
  }

  /** Add an array of integers to this set, if they are not already in it.
   * @param is an arbitrary array of integers; ; must not be
   *        <code><b>null</b></code>.
   * @return <code><b>this</b></code> */
  public Integers add(final int... is) {
    for (final int n : is)
      add(n);
    return this;
  }

  /** What's the underlying table size?
   * @return the hash table size (always a power of two) */
  public int capacity() {
    return data.length;
  }

  /** Remove all elements from this set, preserving capacity.
   * @return <code><b>this</b></code> */
  public Integers clear() {
    return reset(capacity());
  }

  /** Determine whether a given value is in this set.
   * @param i an arbitrary integer
   * @return <code><b>true</b></code> if, and only if, the parameter is
   *         contained in this set. */
  public boolean contains(final int i) {
    return location(i) >= 0;
  }

  /** Check whether an array of integers is contained in this set.
   * @param is an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, all elements in the array
   *         are contained in this set */
  public boolean contains(final int... is) {
    for (final int n : is)
      if (!contains(n))
        return false;
    return true;
  }

  /** Check whether this object is disjoint from an array of integers
   * @param is an array of of integers; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, this object is disjoint
   *         from the set of elements in the parameter */
  public boolean disjoint(final int... is) {
    for (final int n : is)
      if (contains(n))
        return false;
    return true;
  }

  /** What are all values stored in this object?
   * @return an array of all elements in this set. */
  public int[] entries() {
    final int @NonNull [] $ = new int @NonNull [size];
    for (int i = 0, j = 0; i < capacity(); ++i)
      if (occupied[i] && !placeholder[i])
        $[j++] = data[i];
    return $;
  }

  /** Find the index in the hash table into which the parameter could be
   * inserted.
   * @param n some integer
   * @return -1 if the parameter is in the table already, otherwise, the index
   *         at which it could be safely inserted. */
  protected int find(final int n) {
    int $ = -1;
    for (int i = hash(n), t = 0;; i += ++t) {
      i &= data.length - 1;
      if (placeholder[i] || !occupied[i])
        $ = $ < 0 ? i : $;
      if (!occupied[i])
        return $;
      if (n == data[i])
        return -1;
    }
  }

  /** Find the index in the hash table of the parameter
   * @param i some integer
   * @return index of the element if the parameter is in the table, otherwise,
   *         -1; */
  private int location(final int i) {
    for (int $ = hash(i), t = 0;; $ += ++t) {
      $ &= data.length - 1;
      if (!occupied[$])
        return -1;
      if (placeholder[$])
        continue;
      if (i == data[$])
        return $;
    }
  }

  /** Recreate the table, inserting all elements in it afresh.
   * @return <code><b>this</b></code> */
  public Integers rehash() {
    return rehash(capacity());
  }

  /** resize internal storage to the specified capacity, which must be a power
   * of two.
   * @param newCapacity new initialCapacity for the internal array
   * @return <code><b>this</b></code> */
  protected Integers rehash(final int newCapacity) {
    assert (newCapacity & newCapacity - 1) == 0;
    assert newCapacity >= MIN_CAPACITY;
    return reset(newCapacity).add(entries());
  }

  /** Remove an element from this set, it is in it
   * @param n some integer to be removed from the set
   * @return <code><b>this</b></code> */
  public Integers remove(final int n) {
    final int i = location(n);
    assert i >= -1 && i < capacity();
    if (i < 0)
      return this;
    assert occupied[i] && n == data[i];
    placeholder[i] = true;
    return --size < MIN_LOAD * capacity() && capacity() > MIN_CAPACITY ? rehash(data.length >> 1)
        : ++removed > REMOVE_LOAD * capacity() ? rehash() : this;
  }

  /** Remove an array of integers to this set, if they are in it.
   * @param is an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>this</b></code> */
  public Integers remove(final int... is) {
    for (final int n : is)
      remove(n);
    return this;
  }

  final protected Integers reset(final int capacity) {
    data = new int @NonNull [capacity];
    occupied = new boolean @NonNull [capacity];
    placeholder = new boolean @NonNull [capacity];
    size = removed = 0;
    subclassReset(capacity);
    return this;
  }

  /** How many elements are there in this set?
   * @return the number of values in the set. */
  public int size() {
    return size;
  }

  /** @param capacity new hash table size */
  protected void subclassReset(final int capacity) {
    //
  }
}
