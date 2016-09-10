package il.org.spartan.classfiles;

import static il.org.spartan.utils.___.*;

import java.io.*;

/** A collection of functions representing the translation of class names to
 * path and vice versa.
 * @author Yossi Gil
 * @since 12/07/2007 */
public enum Filename {
  ;
  /** The '.' character, used in separation of fully qualified class names. */
  public static final char DOT = '.';

  /** Obtain the relative path name of a class
   * @param c an arbitrary class
   * @return the relative path name, with respect to JAVA packages directory
   *         system, of the parameter */
  public static String class2path(final Class<?> c) {
    return class2path(c.getCanonicalName());
  }

  /** obtain the relative path name of the class of a given object
   * @param o an arbitrary object
   * @return relative path of this class */
  public static Object class2path(final Object o) {
    return class2path(o.getClass());
  }

  /** Obtain the relative path name of a class
   * @param className the fully qualified name of a class
   * @return the relative path name, with respect to JAVA packages directory
   *         system, of the parameter */
  public static String class2path(final String className) {
    return className.replace(DOT, File.separatorChar);
  }

  public static String filePart(final String fileName) {
    nonnull(fileName);
    System.out.println("Replacing " + fileName);
    final String normalizedName = fileName.replaceAll("\\/", "\\");
    System.out.println("normalizedName = " + normalizedName);
    return normalizedName.substring(normalizedName.lastIndexOf('\\') + 1);
  }

  public static String getTrailer(final String s) {
    return s.substring(s.lastIndexOf('.') + 1);
  }

  /** Obtain the leading portion of a fully qualified class name.
   * @param name a class name to process
   * @return the longest prefix of name that is followed by a {@link #DOT}, or
   *         the empty {@link String} if no such prefix exists.
   * @see #tailPart(String) */
  public static String headPart(final String name) {
    final int index = name.lastIndexOf(DOT);
    return index < 0 ? "" : name.substring(0, index);
  }

  public static boolean isAllInner(final String name) {
    return isInner(name) && !tailPart(name).matches(".*[$][0-9].*");
  }

  /** determine whether a class is anonymous
   * @param name a class name to process
   * @return <code><b>true</b></code> <em>iff</em>the class is an anonymous one,
   *         i.e., defined in the context of a <code><b>new</b></code>
   *         expression. */
  public static boolean isAnonymous(final String name) {
    return trailerPart(name).matches("[0-9]+");
  }

  /** determine whether a class is inner
   * @param name a class name to process
   * @return <code><b>true</b></code> <em>iff</em>the class is an inner class,
   *         i.e., defined within another class. */
  public static boolean isInner(final String name) {
    return trailerPart(name).matches("[A-Za-z__$].*");
  }

  /** determine whether a class is local
   * @param name a class name to process
   * @return <code><b>true</b></code> <em>iff</em>the class is a local one,
   *         i.e., defined within a function. */
  public static boolean isLocal(final String name) {
    return trailerPart(name).matches("[0-9][A-Za-z__$].*");
  }

  public static String name2Canonical(final String name) {
    for (String before = name, after;; before = after) {
      after = before.replaceFirst("\\$([a-zA-Z__][a-zA-Z0-9__$]*)$", ".$1");
      if (after.equals(before))
        return after;
    }
  }

  /** Convert a relative file name into a class name.
   * @param path a relative path name, with respect to JAVA packages directory
   *        system
   * @return the fully qualified name of the class residing in this file. */
  public static String path2class(final String path) {
    return path.replaceAll("\\.class$", "").replace('/', DOT).replace('\\', DOT);
  }

  /** Convert a file name as found in the file system, to a class name.
   * @param path a file name to be converted
   * @param root the root of the packages directory
   * @return the fully qualified name of the class residing in this file. */
  public static String path2class(final String path, final String root) {
    return path2class(removeRoot(path, root));
  }

  /** Convert a relative directory name into a package name.
   * @param path a relative path name, with respect to JAVA packages directory
   *        system
   * @return the fully qualified name of the class residing in this file. */
  public static String path2package(final String path) {
    return path.replaceAll("[\\/]", ".");
  }

  /** Convert an absolute directory name as found in the file system, to a class
   * name.
   * @param path a file name to be converted
   * @param root the root of the packages directory
   * @return the fully qualified name of the class residing in this file. */
  public static String path2package(final String path, final String root) {
    return path2package(removeRoot(path, root));
  }

  /** Trim the root portion of an absolute path
   * @param path the absolute path to be trimmed
   * @param root the root portion to be trimmed from the path
   * @return path, but without the root portion */
  public static String removeRoot(final String path, final String root) {
    return path.replace(root + File.separator, "");
  }

  /** Obtain the last portion of a fully qualified class name.
   * @param name a class name to process
   * @return that portion of the name that follows the last occurrence of the
   *         {@link #DOT}, or the entire name, if name does not contain this
   *         character.
   * @see #tailPart(String) */
  public static String tailPart(final String name) {
    final int index = name.lastIndexOf(DOT);
    return index < 0 ? name : name.substring(index + 1);
  }

  public static String trailerPart(final String name) {
    final String tail = tailPart(name);
    final int index = tail.lastIndexOf('$');
    return index < 1 ? "" : tail.substring(index + 1);
  }
}
