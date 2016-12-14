package il.org.spartan.statistics;

import static il.org.spartan.statistics.Skewness.*;
import static il.org.spartan.statistics.Sum.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility public enum Mean {
  ;
  public static double destructiveMoment(final int i, @NotNull final double... ds) {
    return sum(i, shift(ds)) / ds.length;
  }

  public static double mean(@NotNull final double... ¢) {
    return sum(¢) / ¢.length;
  }

  public static double moment(final int i, @NotNull final double... ds) {
    return destructiveMoment(i, ds.clone());
  }

  @NotNull public static double[] shift(@NotNull final double... $) {
    final double mean = mean($);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] -= mean;
    return $;
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void moment1() {
      @NotNull final double vs[] = { 1, 2, 3, 4, 5 };
      Assert.assertEquals(1, moment(0, vs), 1E-8);
      Assert.assertEquals(15, sum(1, vs), 1E-8);
      shift(vs);
      Assert.assertEquals(0, moment(1, vs), 1E-8);
      Assert.assertEquals(2.0, moment(2, vs), 1E-8);
      Assert.assertEquals(0.0, moment(3, vs), 1E-8);
      Assert.assertEquals(6.8, moment(4, vs), 1E-8);
      Assert.assertEquals(0.0, skewness(vs), 1E-8);
    }
  }
}
