package il.org.spartan.collections;

import org.eclipse.jdt.annotation.Nullable;

import il.org.spartan.__;

/** A generic wrapper classes which can store and retrieve values of any type.
 * @author Yossi Gil
 * @since 2015-08-02
 * @param <T> JD */
public class Wrapper<T> {
  protected @Nullable T inner;

  @Override public String toString() {
    return inner == null ? "null" : __.cantBeNull(inner.toString());
  }
  @Override public int hashCode() {
    return inner == null ? 0 : inner.hashCode();
  }
  @Override public final boolean equals(@Nullable final Object o) {
    return super.equals(o) || o != null && getClass() == o.getClass() && equals((Wrapper<?>) o);
  }
  @SuppressWarnings("unchecked") //
  @Override public Wrapper<T> clone() throws CloneNotSupportedException {
    return (Wrapper<T>) __.cantBeNull(super.clone());
  }
  /** @param w JD
   * @return <code><b>true</b></code> <i>iff</i> method <code>equals</code>
   *         returns <code><b>true</b></code> for the wrapped objects. */
  public boolean equals(final Wrapper<?> w) {
    return inner == null ? w.inner == null : inner.equals(w.inner);
  }
  /** Instantiates this class
   * @param inner JD */
  public Wrapper(final @Nullable T inner) {
    this.inner = inner;
  }
  /** Instantiates this class */
  public Wrapper() {
    this(null);
  }
  /** @return the value wrapped in this object. */
  public T get() {
    return inner;
  }
}