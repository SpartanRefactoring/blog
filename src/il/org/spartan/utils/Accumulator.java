/**
 *
 */
package il.org.spartan.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

public abstract class Accumulator {
  static final int asInteger(final boolean b) {
    return As.binary(b);
  }

  static final int asInteger(final Object o) {
    return As.binary(o);
  }

  /** @param s
   * @return */
  static final int asInteger(final String s) {
    return As.binary(s);
  }

  protected int value = 0;
  public int weight = 1;
  protected final String name;

  public Accumulator() {
    this("");
  }

  public Accumulator(final String name) {
    this.name = name;
  }

  public final void add(final boolean b) {
    add(asInteger(b));
  }

  public void add(final int v) {
    value += weight * transform(v);
  }

  public final void add(final String s) {
    add(asInteger(s));
  }

  public String name() {
    return name;
  }

  public int value() {
    return value;
  }

  abstract protected int transform(int v);

  final int weight() {
    return weight;
  }

  final void weight(final int w) {
    weight = w;
  }

  /** A simple counter class.
   * @author Itay Maman, The Technion
   * @since Jul 30, 2007 */
  public static class Counter extends Accumulator {
    public Counter() {
      super();
    }

    public Counter(final String name) {
      super(name);
    }

    public void add() {
      add(1);
    }

    public int next() {
      add();
      return value();
    }

    @Override public String toString() {
      return value + "";
    }

    @Override protected int transform(final int v) {
      return v == 0 ? 0 : 1;
    }

    @SuppressWarnings("static-method") public static class TEST {
      @Test public void booleanAdds() {
        final Counter c = new Counter();
        azzert.that(c.value(), is(0));
        c.add(true);
        azzert.that(c.value(), is(1));
        c.add(false);
        azzert.that(c.value(), is(1));
        c.add(false);
        azzert.that(c.value(), is(1));
        c.add(true);
        azzert.that(c.value(), is(2));
        c.add(true);
        azzert.that(c.value(), is(3));
      }

      @Test public void emptyAdds() {
        final Counter c = new Counter();
        for (int i = 0; i < 19; i++)
          c.add();
        azzert.that(c.value(), is(19));
      }
    }
  }

  public static class Last extends Accumulator {
    /** Instantiate {@link Last}. */
    public Last() {
      super();
    }

    /** Instantiate {@link Last}.
     * @param name JD */
    public Last(final String name) {
      super(name);
    }

    @Override public void add(final int v) {
      value = v;
    }

    @Override protected int transform(final int v) {
      return v;
    }

    @SuppressWarnings("static-method") public static class TEST {
      @SuppressWarnings("static-access") @Test public void booleanAdds() {
        final Last c = new Last();
        azzert.that(Accumulator.asInteger(false), is(0));
        azzert.that(c.value(), is(0));
        c.add(true);
        azzert.that(c.value(), is(1));
        azzert.that(Accumulator.asInteger(false), is(0));
        c.add(false);
        azzert.that(c.value(), is(0));
        c.add(false);
        azzert.that(c.value(), is(0));
        c.add(true);
        azzert.that(c.value(), is(1));
        c.add(true);
        azzert.that(c.value(), is(1));
      }

      @Test public void emptyAdds() {
        final Last c = new Last();
        for (int i = 0; i < 19; i++)
          c.add(i);
        c.add(11);
        azzert.that(c.value(), is(11));
      }
    }
  }

  public static class Squarer extends Accumulator {
    @Override protected int transform(final int v) {
      return v * v;
    }
  }

  public static class Summer extends Accumulator {
    /** Instantiate {@link Summer}. */
    public Summer() {
      // Empty
    }

    /** Instantiate {@link Summer}.
     * @param name name of this instance */
    public Summer(final String name) {
      super(name);
    }

    @Override protected int transform(final int v) {
      return v;
    }
  }
}