// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.collections;

import static il.org.spartan.utils.___.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** A mutable map, associating an <code><b>int</b></code> value with each value
 * of an enumerated type.
 * @param <E> an <code><b>enum</b></code> type, whose values are to be
 *        associated with <code><b>int</b></code> values by this map.
 * @see ImmutableEnumIntMap
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Instantiable @Classical @Canopy public class MutableEnumIntMap<E extends Enum<E>> implements EnumIntMap<E> {
  /** Suite metric values are stored internally here. */
  @NotNull private final int[] implementation;

  /** Initialize this class, with a map associating a zero with each of the
   * enumerated type values.
   * @param dummy some arbitrary, non-<code><b>null</b></code> value of type
   *        <code>E</code>, used for figuring out the number of distinct
   *        enumerated values in the type <code>E</code>. An elegant way of
   *        passing such a dummy value is by fetching the first enumerated
   *        value, as follows
   *
   *        <pre>
   *        MutableIntMap&lt;E&gt; mutableMap = new MutableIntMap&lt;E&gt;(E.values()[0]);
   *        </pre>
  */
  public MutableEnumIntMap(@NotNull final E dummy) {
    nonnull(dummy);
    this.implementation = new int[dummy.getClass().getEnumConstants().length];
  }

  /** Add to the value associated with a specific <code><b>enum</b></code>
   * value.
   * @param e some non-<code><b>null</b></code> value of type <code>E</code>.
   * @param value what to add to the value associated with this enumerated type
   *        value. */
  public void add(@NotNull final E e, final int value) {
    nonnull(e);
    implementation[e.ordinal()] += value;
  }

  @NotNull public ImmutableEnumIntMap<E> asImmutable() {
    return new ImmutableEnumIntMap<>(implementation);
  }

  @Override public int get(@NotNull final E ¢) {
    return implementation[¢.ordinal()];
  }

  /** Increment the value associated with a specific <code><b>enum</b></code>
   * value.
   * @param ¢ some non-<code><b>null</b></code> value of type <code>E</code>. */
  public void increment(@NotNull final E ¢) {
    nonnull(¢);
    ++implementation[¢.ordinal()];
  }

  /** Set the value associated with a specific <code><b>enum</b></code> value.
   * @param e some non-<code><b>null</b></code> value of type <code>E</code>.
   * @param value new value to be associated with <code>e</code>. */
  public void set(@NotNull final E e, final int value) {
    nonnull(e);
    implementation[e.ordinal()] = value;
  }
}
