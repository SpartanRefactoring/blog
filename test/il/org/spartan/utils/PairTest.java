package il.org.spartan.utils;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method") public class PairTest {
  @Test public void testSymmetry() {
    final Pair<Integer, Integer> p1 = new NamedPair("a", Integer.valueOf(1001), Integer.valueOf(-5017));
    final Pair<Integer, Integer> p2 = new Pair<>(Integer.valueOf(1001), Integer.valueOf(-5017));
    assertEquals(p1.equals(p2), p2.equals(p1));
  }

  @SuppressWarnings("all") public static class NamedPair extends Pair<Integer, Integer> {
    public final String name;

    public NamedPair(final String name, final Integer a, final Integer b) {
      super(a, b);
      this.name = name;
    }

    public boolean equals(final Pair<Integer, Integer> o) {
      return o == this || o != null && o instanceof NamedPair && equals((NamedPair) o);
    }

    @Override public String toString() {
      return name + super.toString();
    }

    private boolean equals(final NamedPair that) {
      return first.equals(that.first) && second.equals(that.second) && name.equals(that.name);
    }
  }
}
