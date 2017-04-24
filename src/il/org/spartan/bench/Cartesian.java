package il.org.spartan.bench;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.___.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** @author Yossi Gil
 * @since 29/05/2011 */
public class Cartesian {
  @NotNull public static <S> Pair<Integer, S>[] makeObliqueProduct(@NotNull final int[] is, @NotNull final S[] ss) {
    return makeObliqueProduct(box.it(is), ss);
  }

  @NotNull public static <S, T> Triple<Integer, S, T>[] makeObliqueProduct(@NotNull final int[] is, @NotNull final S[] ss, @NotNull final T[] ts) {
    return makeObliqueProduct(box.it(is), ss, ts);
  }

  @NotNull public static <R, S> Pair<R, S>[] makeObliqueProduct(@NotNull final R[] rs, @NotNull final S[] ss) {
    @NotNull final Pair<R, S>[] $ = Pair.makePairs(rs.length, ss.length);
    int n = 0;
    for (int sum = 2; sum < rs.length + ss.length; ++sum)
      for (int i = 1; i <= Math.min(sum, rs.length); ++i) {
        final int j = sum - i;
        if (j < 1 || j > ss.length)
          continue;
        ___.positive(i);
        ___.positive(j);
        ___.sure(i <= rs.length);
        ___.sure(j <= ss.length);
        ___.sure(i < sum);
        ___.sure(j < sum);
        ___.nonnegative(n);
        ___.sure(n < $.length);
        $[n++] = new Pair<>(rs[i - 1], ss[j - 1]);
      }
    ___.sure(n == $.length);
    return $;
  }

  @NotNull public static <R, S, T> Triple<R, S, T>[] makeObliqueProduct(@NotNull final R[] rs, @NotNull final S[] ss, @NotNull final T[] ts) {
    @NotNull final Triple<R, S, T>[] $ = Triple.makeTriples(rs.length, ss.length, ts.length);
    int n = 0;
    for (int sum = 3; sum <= rs.length + ss.length + ts.length; ++sum)
      for (int i = 1; i <= Math.min(sum, rs.length); ++i)
        for (int j = sum - i - 1; j >= 1; --j) {
          if (j > ss.length)
            continue;
          final int k = sum - i - j;
          if (k < 1 || k > ts.length)
            continue;
          ___.positive(i);
          ___.positive(j);
          ___.positive(k);
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

  @NotNull public static <S> Pair<Integer, S>[] makeProduct(@NotNull final int[] is, @NotNull final S[] ss) {
    return makeProduct(box.it(is), ss);
  }

  @NotNull public static <S, T> Triple<Integer, S, T>[] makeProduct(@NotNull final int[] is, @NotNull final S[] ss, @NotNull final T[] ts) {
    return makeProduct(box.it(is), ss, ts);
  }

  @NotNull public static <R, S> Pair<R, S>[] makeProduct(@NotNull final R[] rs, @NotNull final S[] ss) {
    @NotNull final Pair<R, S>[] $ = Pair.makePairs(rs.length * ss.length);
    int n = 0;
    for (final R r : rs)
      for (final S ¢ : ss)
        $[n++] = new Pair<>(r, ¢);
    return $;
  }

  @NotNull public static <R, S, T> Triple<R, S, T>[] makeProduct(@NotNull final R[] rs, @NotNull final S[] ss, @NotNull final T[] ts) {
    @NotNull final Triple<R, S, T>[] $ = Triple.makeTriples(rs.length, ss.length, ts.length);
    int n = 0;
    for (final R r : rs)
      for (final S s : ss)
        for (final T ¢ : ts)
          $[n++] = Triple.make(r, s, ¢);
    sure(n == $.length);
    return $;
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void makeArray000() {
      azzert.that(makeObliqueProduct(new Object[0], new Object[0], new Object[0]).length, is(0));
    }

    @Test public void makeArray111() {
      azzert.that(makeObliqueProduct(new Object[1], new Object[1], new Object[1]).length, is(1));
    }

    @Test public void makeArray3_13_19() {
      azzert.that(makeObliqueProduct(new Object[3], new Object[13], new Object[19]).length, is(702));
    }
  }
}
