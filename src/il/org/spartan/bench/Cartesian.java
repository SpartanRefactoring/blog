package il.org.spartan.bench;

import static il.org.spartan.utils.____.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.utils.*;

/**
 *
 */
/** @author Yossi Gil
 * @since 29/05/2011 */
public class Cartesian {
  public static <S> Pair<Integer, S>[] makeObliqueProduct(final int[] is, final S[] ss) {
    return makeObliqueProduct(Box.it(is), ss);
  }

  public static <S, T> Triple<Integer, S, T>[] makeObliqueProduct(final int[] is, final S[] ss, final T[] ts) {
    return makeObliqueProduct(Box.it(is), ss, ts);
  }

  public static <R, S> Pair<R, S>[] makeObliqueProduct(final R[] rs, final S[] ss) {
    final Pair<R, S>[] $ = Pair.makePairs(rs.length, ss.length);
    int n = 0;
    for (int sum = 2; sum < rs.length + ss.length; sum++)
      for (int i = 1; i <= Math.min(sum, rs.length); i++) {
        final int j = sum - i;
        if (j < 1 || j > ss.length)
          continue;
        positive(i);
        positive(j);
        sure(i <= rs.length);
        sure(j <= ss.length);
        sure(i < sum);
        sure(j < sum);
        nonnegative(n);
        sure(n < $.length);
        $[n++] = new Pair<>(rs[i - 1], ss[j - 1]);
      }
    ____.sure(n == $.length);
    return $;
  }

  public static <R, S, T> Triple<R, S, T>[] makeObliqueProduct(final R[] rs, final S[] ss, final T[] ts) {
    final Triple<R, S, T>[] $ = Triple.makeTriples(rs.length, ss.length, ts.length);
    int n = 0;
    for (int sum = 3; sum <= rs.length + ss.length + ts.length; sum++)
      for (int i = 1; i <= Math.min(sum, rs.length); i++)
        for (int j = sum - i - 1; j >= 1; j--) {
          if (j > ss.length)
            continue;
          final int k = sum - i - j;
          if (k < 1 || k > ts.length)
            continue;
          positive(i);
          positive(j);
          positive(k);
          sure(i <= rs.length);
          sure(j <= ss.length);
          sure(k <= ts.length);
          sure(i < sum);
          sure(j < sum);
          sure(k < sum);
          nonnegative(n);
          sure(n < $.length);
          $[n++] = Triple.make(rs[i - 1], ss[j - 1], ts[k - 1]);
        }
    sure(n <= $.length);
    sure(n >= $.length);
    sure(n == $.length);
    return $;
  }

  public static <S> Pair<Integer, S>[] makeProduct(final int[] is, final S[] ss) {
    return makeProduct(Box.it(is), ss);
  }

  public static <S, T> Triple<Integer, S, T>[] makeProduct(final int[] is, final S[] ss, final T[] ts) {
    return makeProduct(Box.it(is), ss, ts);
  }

  public static <R, S> Pair<R, S>[] makeProduct(final R[] rs, final S[] ss) {
    final Pair<R, S>[] $ = Pair.makePairs(rs.length * ss.length);
    int n = 0;
    for (final R r : rs)
      for (final S s : ss)
        $[n++] = new Pair<>(r, s);
    return $;
  }

  public static <R, S, T> Triple<R, S, T>[] makeProduct(final R[] rs, final S[] ss, final T[] ts) {
    final Triple<R, S, T>[] $ = Triple.makeTriples(rs.length, ss.length, ts.length);
    int n = 0;
    for (final R r : rs)
      for (final S s : ss)
        for (final T t : ts)
          $[n++] = Triple.make(r, s, t);
    sure(n == $.length);
    return $;
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void makeArray000() {
      final int n = makeObliqueProduct(new Object[0], new Object[0], new Object[0]).length;
      assertEquals(0, n);
    }

    @Test public void makeArray111() {
      final int n = makeObliqueProduct(new Object[1], new Object[1], new Object[1]).length;
      assertEquals(1, n);
    }

    @Test public void makeArray3_13_19() {
      final int n = makeObliqueProduct(new Object[3], new Object[13], new Object[19]).length;
      assertEquals(3 * 13 * 18, n);
    }
  }
}
