package il.org.spartan.collections;

/** @param <T> Type of encoded objects
 * @param <U> An upper bound on the type of encoded objects */
public interface Container<U, T extends U> {
  /**
   * @param u  an arbitrary object
   * @return  <code><b>true</b</code> <em>if and only if</em> the parameter is contained in the receiver. 
   */
  boolean contains(final U u);

  /**
   * Which objects are contained in this object?
   * @return  an  {@link Iterable}  over the set of all objects for which {@link #contains}  returns true. 
   */
  Iterable<? extends T> elements();

  /**
   * How many elements are represented by this object?
   * @return  the size of the set defined by  {@link #elements()}  
   */
  int size();
}
