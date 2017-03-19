// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static il.org.spartan.fapi.azzert.*;
import static org.junit.Assert.assertEquals;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.fapi.*;
import il.org.spartan.statistics.*;
import il.org.spartan.streotypes.*;

@TestCase @SuppressWarnings("static-method") public class StatisticsTest {
  @Test public void testAverage() {
    @NotNull final RealStatistics s = new RealStatistics();
    s.record(1);
    s.record(3);
    s.record(2);
    assertEquals(2.0, s.mean(), 1e-7);
  }

  @Test public void testOneCount() {
    @NotNull final RealStatistics s = new RealStatistics();
    s.record(1);
    azzert.that(s.n(), is(1));
  }

  @Test public void testStdTwoValues() {
    @NotNull final RealStatistics s = new RealStatistics();
    s.record(8);
    s.record(12);
    assertEquals(2.0, s.sd(), 1e-7);
  }

  @Test public void testTwoValues() {
    @NotNull final RealStatistics s = new RealStatistics();
    s.record(1);
    s.record(3);
    assertEquals(2.0, s.n(), 1e-7);
  }

  @Test public void testTwoValuesAverage() {
    @NotNull final RealStatistics s = new RealStatistics();
    s.record(4);
    s.record(6);
    assertEquals(5.0, s.mean(), 1e-7);
  }

  @Test public void testTwoValuesSecondMoment() {
    @NotNull final RealStatistics s = new RealStatistics();
    s.record(4);
    s.record(6);
    assertEquals(52.0, s.sum2(), 1e-7);
  }

  @Test public void testZeroCount() {
    azzert.that(new RealStatistics().n(), is(0));
  }
}
