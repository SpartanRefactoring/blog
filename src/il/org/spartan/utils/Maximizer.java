/**
 *
 */
package il.org.spartan.utils;

/** @author Yossi Gil
 * @since Mar 6, 2012
 * @param <T> an arbitrary type */
public class Maximizer<T> {
  private double max = Double.NaN;
  private T value = null;

  public double max() {
    return max;
  }

  public Maximizer<T> next(final T t, final double next) {
    if (Double.isNaN(max) || next > max) {
      max = next;
      value = t;
    }
    return this;
  }

  public T value() {
    return value;
  }
}
