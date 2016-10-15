/**
 *
 */
package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.xy.*;

/** @author Yossi Gil
 * @since Feb 26, 2012 */
public class Binner {
  private static int[] reverse(final int[] $) {
    for (int i = 0, j = $.length - 1; i < j; i++, j--) {
      final int temp = $[i];
      $[i] = $[j];
      $[j] = temp;
    }
    return $;
  }

  private final int[] from;
  private final int[] to;
  public final int n;
  public final int m;
  private final int[] map;

  public Binner(final int binSize, final int... is) {
    map = new int[n = is.length];
    final IntsArray fromStack = new IntsArray(), toStack = new IntsArray();
    main: for (int t = n; t >= 0; t--)
      for (int f = t - 1, s = 0; f >= 0; f--)
        if (f == 0 || (s += is[f]) >= binSize) {
          fromStack.push(f);
          toStack.push(t);
          t = f + 1;
          continue main;
        }
    from = reverse(fromStack.toArray());
    to = reverse(toStack.toArray());
    ___.sure(from.length == to.length);
    m = from.length;
    for (int i = 0; i < from.length; i++)
      for (int j = from[i]; j < to[i]; j++)
        map[j] = i;
  }

  public double[] bin(final double[] ds) {
    ___.require(n == ds.length);
    final double[] $ = new double[m];
    for (int i = 0; i < m; i++)
      for (int j = from[i]; j < to[i]; j++)
        $[i] += ds[j];
    return $;
  }

  public int bin(final int j) {
    return map[j];
  }

  public int[] bin(final int[] is) {
    ___.require(n == is.length);
    final int[] $ = new int[m];
    for (int i = 0; i < m; i++)
      for (int j = from[i]; j < to[i]; j++)
        $[i] += is[j];
    return $;
  }

  public int[][] bin(final int[][] is) {
    final int[][] $ = new int[is.length][];
    for (int i = 0; i < $.length; i++)
      $[i] = bin(is[i]);
    return $;
  }

  public double unbin(final double d) {
    return unbin((int) (d + 0.5));
  }

  public double[] unbin(final double[] ds) {
    final double[] $ = new double[ds.length];
    for (int i = 0; i < ds.length; i++)
      $[i] = unbin(ds[i]);
    return $;
  }

  public double unbin(final int i) {
    return (from[i] + to[i] - 1) / 2.;
  }

  public XYSeries unbin(final XYSeries s) {
    return new XYSeries(unbin(s.x), s.y, s.dy);
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    @Test public void binLongRandomArray() {
      final Random r = new Random(0);
      final int[] is = new int[1000];
      for (int i = 0; i < is.length; i++)
        is[i] = r.nextInt(5);
      final Binner b = new Binner(4, is);
      checkBinner(b);
      for (int i = 0; i < b.from.length; i++) {
        int s = 0;
        for (int j = b.from[i]; j < b.to[i]; j++)
          s += is[j];
        assert i == 0 || s >= 4;
      }
    }

    @Test public void oneLength() {
      final Binner b = new Binner(1, 1);
      azzert.that(b.from.length, is(1));
      azzert.that(b.to.length, is(1));
      azzert.that(b.from[0], is(0));
      azzert.that(b.to[0], is(1));
    }

    @Test public void singleIntervalOfTwo() {
      final Binner b = new Binner(10, 5, 5);
      checkBinner(b);
    }

    @Test public void singleIntervalOfTwoFrom() {
      final Binner b = new Binner(10, 5, 5);
      azzert.that(b.from[0], is(0));
    }

    @Test public void singleIntervalOfTwoMap() {
      final Binner b = new Binner(10, 5, 5);
      azzert.that(b.bin(0), is(0));
      azzert.that(b.bin(1), is(0));
    }

    @Test public void singleIntervalOfTwoSize() {
      final Binner b = new Binner(10, 5, 5);
      azzert.that(b.n, is(2));
    }

    @Test public void singleIntervalOfTwoTo() {
      final Binner b = new Binner(10, 5, 5);
      azzert.that(b.to[0], is(2));
    }

    @Test public void singleIntervalOfTwoUnmap() {
      final Binner b = new Binner(10, 5, 5);
      Assert.assertEquals(0.5, b.unbin(0), 1E-12);
    }

    @Test public void singleIntervalUndershootSizeTwo() {
      final Binner b = new Binner(10, 3, 5);
      checkBinner(b);
    }

    @Test public void singleIntervalUndershootSizeTwoFrom() {
      final Binner b = new Binner(10, 3, 5);
      azzert.that(b.from[0], is(0));
    }

    @Test public void singleIntervalUndershootSizeTwoLengthFromLength() {
      final Binner b = new Binner(10, 3, 5);
      azzert.that(b.from.length, is(1));
    }

    @Test public void singleIntervalUndershootSizeTwoLengthToLength() {
      final Binner b = new Binner(10, 3, 5);
      azzert.that(b.to.length, is(1));
    }

    @Test public void singleIntervalUndershootSizeTwoTo() {
      final Binner b = new Binner(10, 3, 5);
      azzert.that(b.to[0], is(2));
    }

    @Test public void trivial() {
      final Binner b = new Binner(1, 1, 1, 1, 1, 1);
      checkBinner(b);
    }

    @Test public void twoIntervalsSizedTwo() {
      final Binner b = new Binner(10, 3, 5, 5, 5);
      azzert.that(b.from[0], is(0));
      azzert.that(b.to[0], is(2));
      azzert.that(b.from[1], is(2));
      azzert.that(b.to[1], is(4));
    }

    @Test public void twoIntervalsSizedTwoBin() {
      final int[] a = new Binner(10, 3, 5, 5, 5).bin(new int[] { 3, 9, 7, 13 });
      azzert.that(a.length, is(2));
      azzert.that(a[0], is(12));
      azzert.that(a[1], is(20));
    }

    @Test public void twoIntervalsSizedTwoBinMatrix() {
      final int[][] a = new Binner(10, 3, 5, 5, 5).bin(new int[][] { //
          new int[] { 3, 9, 7, 13 }, //
          new int[] { 4, 2, 5, 4 }, //
          new int[] { 19, 11, 24, 12 } //
      });
      azzert.that(a.length, is(3));
      azzert.that(a[0][0], is(12));
      azzert.that(a[0][1], is(20));
      azzert.that(a[1][0], is(6));
      azzert.that(a[1][1], is(9));
      azzert.that(a[2][0], is(30));
      azzert.that(a[2][1], is(36));
    }

    @Test public void twoIntervalsSizedTwoDoublesBin() {
      final double[] a = new Binner(10, 3, 5, 5, 5).bin(new double[] { 3, 9, 7, 13 });
      azzert.that(a.length, is(2));
      assertEquals(12.0, a[0], 1E-10);
      assertEquals(20.0, a[1], 1E-10);
    }

    @Test public void twoLength() {
      final Binner b = new Binner(1, 1, 1);
      azzert.that(b.from.length, is(2));
      azzert.that(b.to.length, is(2));
    }

    @Test public void twoSingletonIntervalsFirstInterval() {
      final Binner b = new Binner(1, 1, 1);
      azzert.that(b.to[0], is(1));
      azzert.that(b.from[0], is(0));
    }

    @Test public void twoSingletonIntervalsSecondFrom() {
      final Binner b = new Binner(1, 1, 1);
      azzert.that(b.from[1], is(1));
    }

    @Test public void twoSingletonIntervalsSecondTo() {
      final Binner b = new Binner(1, 1, 1);
      azzert.that(b.to[1], is(2));
    }

    @Test public void veryLongBinner() {
      final Binner b = new Binner(3, 1, 2, 3, 3, 2, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 0);
      checkBinner(b);
    }

    @Test public void zeroLength() {
      final Binner b = new Binner(1);
      azzert.that(b.from.length, is(0));
      azzert.that(b.to.length, is(0));
    }

    private void checkBinner(final Binner b) {
      startAtZero(b);
      endsAtEnd(b, b.n);
      sortedFrom(b);
      sortedTo(b);
      nonZeroInterval(b);
      consecutive(b);
      checkMap(b);
      checkInverseMap(b);
    }

    private void checkInverseMap(final Binner b) {
      for (int i = 0; i < b.from.length - 1; i++)
        for (int j = b.from[i]; j < b.to[i]; j++) {
          final double u = b.unbin(b.bin(j));
          assert u >= b.from[i];
          assert u < b.to[i];
          assertEquals((b.to[i] + b.from[i] - 1) / 2., u, 1E-12);
        }
    }

    private void checkMap(final Binner b) {
      for (int i = 0; i < b.from.length - 1; i++)
        for (int j = b.from[i]; j < b.to[i]; j++) {
          final int t1 = i;
          azzert.that(b.bin(j), is(t1));
        }
    }

    private void consecutive(final Binner b) {
      for (int i = 1; i < b.to.length; i++)
        azzert.that(b.from[i], is(b.to[i - 1]));
    }

    private void endsAtEnd(final Binner b, final int n) {
      azzert.that(b.from.length, is(b.to.length));
      assert n == 0 || b.to.length > 0;
      azzert.that(b.to[b.to.length - 1], is(n));
    }

    private void nonZeroInterval(final Binner b) {
      for (int i = 0; i < b.to.length; i++)
        assert b.to[i] > b.from[i];
    }

    private void sortedFrom(final Binner b) {
      for (int i = 0; i < b.from.length - 1; i++)
        assert b.from[i] < b.from[i + 1];
    }

    private void sortedTo(final Binner b) {
      for (int i = 0; i < b.to.length - 1; i++)
        assert b.to[i] < b.to[i + 1];
    }

    private void startAtZero(final Binner b) {
      azzert.that(b.from[0], is(0));
    }
  }
}
