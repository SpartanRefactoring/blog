package il.org.spartan.collections;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.___.*;
import org.jetbrains.annotations.NotNull;

/** An unsorted set of integers supporting the basic set operations:
 * {@link #add(int)}, {@link #contains(int)} and {@link #remove(int)}.
 * <p>
 * Implementation is using a linear hash table s, with quardratic probes (i.e.,
 * +1, +3, +6, +10, ... modulo the table size). Array capacity is always a power
 * of two, and is doubled when the load goes above {@link #MAX_LOAD}; capacity
 * is halved when the load drops below {@value #MIN_LOAD}. When the fraction of
 * removed keys goes below {@link #REMOVE_LOAD}, the table is rehashed.
 * @author Yossi Gil
 * @since December 2010 */
public class integers {
  public static final float MAX_LOAD = 0.75f;
  public static final float MIN_LOAD = 0.25f;
  public static final float REMOVE_LOAD = 0.20f;
  public static final int MIN_CAPACITY = 4;

  /** [[SuppressWarningsSpartan]] */
  static int hash(final int ¢) {
    int $ = ¢ ^ ¢ >>> 20 ^ ¢ >>> 12;
    return $ ^ $ >>> 7 ^ $ >>> 4;
  }

  private static int roundUp(final int ¢) {
    int $ = 1;
    while ($ < ¢)
      $ <<= 1;
    return $;
  }

  protected int[] data;
  private boolean[] occupied;
  private boolean[] placeholder;
  private int size;
  private int removed;

  /** Instantiate this class */
  public integers() {
    this(MIN_CAPACITY);
  }

  /** Instantiate this class, using a given size for the hash table.
   * @param initialCapacity suggests a hash table size, will be rounded up to
   *        the next power of two. */
  public integers(final int initialCapacity) {
    reset(Math.max(MIN_CAPACITY, roundUp(initialCapacity)));
  }

  /** Add an integer to the set, if it is not already there.
   * @param n an arbitrary integer
   * @return <code><b>this</b>/code> */
  @NotNull
  public integers add(final int n) {
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
   * @return <code><b>this</b>/code> */
  @NotNull
  public integers add(@NotNull final int... is) {
    for (final int ¢ : is)
      add(¢);
    return this;
  }

  /** What's the underlying table size?
   * @return the hash table size (always a power of two) */
  public int capacity() {
    return data.length;
  }

  /** Remove all elements from this set, preserving capacity.
   * @return <code><b>this</b>/code> */
  @NotNull
  public integers clear() {
    return reset(capacity());
  }

  /** Determine whether a given value is in this set.
   * @param ¢ an arbitrary integer
   * @return <code><b>true</b></code> if, and only if, the parameter is
   *         contained in this set. */
  public boolean contains(final int ¢) {
    return location(¢) >= 0;
  }

  /** Check whether an array of integers is contained in this set.
   * @param is an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, all elements in the array
   *         are contained in this set */
  public boolean contains(@NotNull final int... is) {
    for (final int ¢ : is)
      if (!contains(¢))
        return false;
    return true;
  }

  /** Check whether this object is disjoint from an array of integers
   * @param is an array of of integers; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, this object is disjoint
   *         from the set of elements in the parameter */
  public boolean disjoint(@NotNull final int... is) {
    for (final int ¢ : is)
      if (contains(¢))
        return false;
    return true;
  }

  /** What are all values stored in this object?
   * @return an array of all elements in this set. */
  @NotNull
  public int[] entries() {
    final int[] $ = new int[size];
    for (int ¢ = 0, j = 0; ¢ < capacity(); ++¢)
      if (occupied[¢] && !placeholder[¢])
        $[j++] = data[¢];
    return $;
  }

  /** Recreate the table, inserting all elements in it afresh.
   * @return <code><b>this</b>/code> */
  @NotNull
  public integers rehash() {
    return rehash(capacity());
  }

  /** Remove an element from this set, it is in it
   * @param n some integer to be removed from the set
   * @return <code><b>this</b>/code> */
  @NotNull
  public integers remove(final int n) {
    final int i = location(n);
    assert i >= -1 && i < capacity();
    if (i < 0)
      return this;
    assert occupied[i] && data[i] == n;
    placeholder[i] = true;
    return --size < MIN_LOAD * capacity() && capacity() > MIN_CAPACITY ? rehash(data.length >> 1)
        : ++removed > REMOVE_LOAD * capacity() ? rehash() : this;
  }

  /** Remove an array of integers to this set, if they are in it.
   * @param is an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>this</b>/code> */
  @NotNull
  public integers remove(@NotNull final int... is) {
    for (final int ¢ : is)
      remove(¢);
    return this;
  }

  /** How many elements are there in this set?
   * @return the number of values in the set. */
  public int size() {
    return size;
  }

  /** Find the index in the hash table into which the parameter could be
   * inserted.
   * @param i some integer
   * @return -1 if the parameter is in the table already, otherwise, the index
   *         at which it could be safely inserted. */
  protected int find(final int i) {
    for (int $ = -1, ¢ = hash(i), t = 0;; ¢ += ++t) {
      ¢ &= data.length - 1;
      if (placeholder[¢] || !occupied[¢])
        $ = $ < 0 ? ¢ : $;
      if (!occupied[¢])
        return $;
      if (data[¢] == i)
        return -1;
    }
  }

  /** resize internal storage to the specified capacity, which must be a power
   * of two.
   * @param newCapacity new initialCapacity for the internal array
   * @return <code><b>this</b>/code> */
  @NotNull
  protected integers rehash(final int newCapacity) {
    assert (newCapacity & newCapacity - 1) == 0;
    assert newCapacity >= MIN_CAPACITY;
    return reset(newCapacity).add(entries());
  }

  @NotNull
  protected final integers reset(final int capacity) {
    data = new int[capacity];
    occupied = new boolean[capacity];
    placeholder = new boolean[capacity];
    size = removed = 0;
    subclassReset(capacity);
    return this;
  }

  /** @param capacity new hash table size */
  protected void subclassReset(final int capacity) {
    //
  }

  /** Find the index in the hash table of the parameter
   * @param i some integer
   * @return index of the element if the parameter is in the table, otherwise,
   *         -1; */
  private int location(final int i) {
    for (int $ = hash(i), ¢ = 0;; $ += ++¢) {
      $ &= data.length - 1;
      if (!occupied[$])
        return -1;
      if (placeholder[$])
        continue;
      if (data[$] == i)
        return $;
    }
  }

  @SuppressWarnings({ "synthetic-access", "boxing" }) //
  public final class INVARIANT implements Invariantable {
    @Override public void check() {
      azzert.that(size, lessThanOrEqualTo(capacity()));
      azzert.that(capacity(), greaterThanOrEqualTo(MIN_CAPACITY));
      azzert.that(placeholder.length, lessThanOrEqualTo(capacity()));
      azzert.that(occupied.length, lessThanOrEqualTo(capacity()));
      azzert.that(data.length, lessThanOrEqualTo(capacity()));
      azzert.that(size, lessThanOrEqualTo((int) (MAX_LOAD * capacity())));
      azzert.that(size, greaterThanOrEqualTo((int) (MIN_LOAD * capacity())));
      azzert.that(removed, lessThanOrEqualTo((int) (REMOVE_LOAD * capacity())));
      azzert.that(removed, comparesEqualTo(count(placeholder)));
      azzert.that(size, comparesEqualTo(count(occupied) - removed));
      for (int ¢ = 0; ¢ < capacity(); ++¢)
        if (placeholder[¢])
          assert occupied[¢];
    }

    private int count(@NotNull final boolean bs[]) {
      int $ = 0;
      for (final boolean ¢ : bs)
        $ += as.bit(¢);
      return $;
    }
  }
}
