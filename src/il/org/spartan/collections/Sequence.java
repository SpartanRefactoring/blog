package il.org.spartan.collections;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** An immutable collection of elements. Once construction is completed no new
 * elements can be added to the collections, nor can elements be removed from
 * the collection. The {@link #indexOf(Object)} and {@link #has(Object)} methods
 * assume that the collection does not contain null elements
 * @param <E> Type of elements in the sequence */
public class Sequence<E> implements Iterable<E>, Serializable {
  private static final long serialVersionUID = 8259437798546027307L;

  @NotNull public static <T> Sequence<T> make(@NotNull final Sequence<T> ts, final int begin) {
    return make(ts.es, begin, ts.end);
  }

  @NotNull public static <T> Sequence<T> make(final T[] ts, final int begin, final int end) {
    return new Sequence<>(ts, begin, end);
  }

  @NotNull public static <T> Sequence<T> upcast(@SuppressWarnings("unused") final Class<T> __, @NotNull final Sequence<? extends T> src) {
    return make(src.es, src.begin, src.end);
  }

  private final E[] es;
  private final int begin;
  private final int end;

  public Sequence() {
    this(null, 0, 0);
  }

  public Sequence(@NotNull final E... es) {
    this(es, 0, es.length);
  }

  protected Sequence(final E[] es, final int begin, final int end) {
    this.begin = begin;
    this.end = end;
    this.es = es;
  }

  public E get(final int index) {
    return es[index + begin];
  }

  public boolean has(final E ¢) {
    return indexOf(¢) >= 0;
  }

  public int indexOf(final E e) {
    for (int ¢ = begin; ¢ < end; ++¢)
      if (es[¢].equals(e))
        return ¢ - begin;
    return -1;
  }

  @NotNull @Override public Iterator<E> iterator() {
    return ArrayIterator.make(es, begin, end);
  }

  public int size() {
    return end - begin;
  }

  @NotNull @Override public String toString() {
    return Stringify.it(this);
  }
}
