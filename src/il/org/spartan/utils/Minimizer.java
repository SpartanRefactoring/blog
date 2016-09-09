/**
 *
 */
package il.org.spartan.utils;

/** @author Yossi Gil
 * @since Mar 6, 2012
 * @param <T> an arbitrary type */
public class Minimizer<T> {
  private double min = Double.NaN;
  private T value = null;
  private int index = 0;
  private int maxIndex = -1;

  public int index() {
    return maxIndex;
  }

  public double min() {
    return min;
  }

  public Minimizer<T> next(final T t, final double next) {
    ____.nonnull(t);
    if (Double.isNaN(min) || next < min) {
      min = next;
      value = t;
      maxIndex = index;
    }
    index++;
    return this;
  }

  public T value() {
    ____.nonnull(value);
    return value;
  }
}
