package il.org.spartan.reflection;

import java.lang.reflect.*;

import il.org.spartan.utils.*;

public class ClassPredicates {
  public static boolean hasMutableFields(final Class<?> c) {
    for (final Field ¢ : c.getDeclaredFields())
      if (!Modifier.isFinal(¢.getModifiers()))
        return true;
    final Class<?> parent = c.getSuperclass();
    return parent != null && hasMutableFields(parent);
  }

  public static boolean isImmutable(final Class<?> ¢) {
    return !¢.isEnum() && nFields(¢) > 0 && !hasMutableFields(¢);
  }

  public static int nFields(final Class<?> c) {
    int $ = 0;
    for (final Field ¢ : c.getDeclaredFields())
      $ += As.binary(!¢.isSynthetic() && !Modifier.isStatic(¢.getModifiers()));
    final Class<?> parent = c.getSuperclass();
    return $ + (parent == null ? 0 : nFields(parent));
  }
}
