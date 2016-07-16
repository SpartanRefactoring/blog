/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static org.junit.Assert.*;
import il.org.spartan.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * No values in an 'enum' used as name space for a collection of 'static'
 * functions.
 *
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 */
public enum Iterables {
  //
  ;
  /**
   * Counts the number of items in an {@link Iterable}.
   *
   * @param <T> some arbitrary type
   * @param ts some iterable over items whose type is the type parameter
   * @return the number of items the given iterable yields.
   */
  public static <T> int count(final @Nullable Iterable<T> ts) {
    int $ = 0;
    if (ts != null)
      for (final @Nullable T t : ts)
        $ += as.bit(t != null);
    return $;
  }
  /**
   * @param <T> JD
   * @return <code><b>true</b></code> <i>iff</i> the receive is empty
   */
  public static <T> PureIterable.Sized<T> empty() {
    return as.nonNullIterable();
  }
  /**
   * @param os JD
   * @return TODO document return type
   */
  public static boolean isEmpty(final Iterable<?> os) {
    for (final Object name2 : os)
      if (name2 != null)
        return false;
    return true;
  }
  /**
   * TODO Javadoc(2016): automatically generated for method
   * <code>singleton</code>
   *
   * @param <T> JD
   * @param t JD
   * @return PureIterable.Sized<T> TODO Javadoc(2016) automatically generated
   *         for returned value of method <code>singleton</code>
   */
  public static <T> PureIterable.Sized<T> singleton(final T t) {
    return as.nonNullIterable(t);
  }
  /**
   * wraps a value in a singleton iterator form
   *
   * @param <T> JD
   * @param $ JD
   * @return the parameter, but in a singleton iterator form
   */
  public static <T> PureIterator<T> singletonIterator(final T $) {
    return singleton($).iterator();
  }

  /**
   * A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   *
   * @author Yossi Gil
   * @since 2014-05-31
   */
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
  @SuppressWarnings({ "static-method", "javadoc" })//
  public static class TEST {
    @Test public void containsDegenerate() {
      assertFalse(contains("Hello"));
    }
    @Test public void containseturnsFalseTypical() {
      assertFalse(contains("Hello", null, "x", "y", null, "z", "w", "u", "v"));
    }
    @Test public void containsSimple() {
      assertTrue("", contains("Hello", "e"));
    }
    @Test public void containsTypical() {
      assertTrue("", contains("Hello", "a", "b", "c", "d", "e", "f"));
    }
    @Test public void containsWithNulls() {
      assertTrue("", contains("Hello", null, "a", "b", null, "c", "d", "e", "f", null));
    }
    @Test public void countDoesNotIncludeNull() {
      assertEquals(3, count(as.iterable(null, "One", null, "Two", null, "Three")));
    }
    @Test public void countEmpty() {
      assertEquals(0, count(Iterables.<String> empty()));
    }
    @Test public void countSingleton() {
      assertEquals(1, count(singleton(new Object())));
    }
    @Test public void countThree() {
      assertEquals(3, count(as.iterable("One", "Two", "Three")));
    }
  }
}
