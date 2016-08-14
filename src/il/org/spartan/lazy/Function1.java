/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;
/** A {@link FunctionalInterface} encapsulating a function with one argument
 * @param <A> type of the argument
 * @param <T> type of the function result
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
@FunctionalInterface public interface Function1<T, A> {
  /** Encapsulated function
   * @param ¢ JD
   * @throws Exception in case the function failed
   * @return the result of applying the function to the argument */
  T ϑ(A ¢) throws Exception;
}