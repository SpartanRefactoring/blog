package il.org.spartan.classfiles.reify;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ConstantPool.*;
import il.org.spartan.collections.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An in memory representation of a class file.
 * @author Yossi Gil */
public final class ClassInfo extends ConstantPoolEntity {
  @Nullable
  public static ClassInfo make(final Class<?> ¢) {
    return make(CLASSFILES.open(¢));
  }

  @Nullable
  @SuppressWarnings("synthetic-access") public static AttributedEntity make(final DataInputStream ¢) {
    return new Builder(¢).go();
  }

  @Nullable
  public static ClassInfo make(final File ¢) {
    return new Builder(¢).go();
  }

  @Nullable
  public static ClassInfo make(final InputStream ¢) {
    return new Builder(¢).go();
  }

  @Nullable
  public static ConstantPoolEntity make(@NotNull final String fileName) {
    return new Builder(fileName).go();
  }

  private static void addLinkComponents(@NotNull final int[] target, @Nullable final int[] addition) {
    if (addition != null)
      for (int ¢ = 0; ¢ < target.length; ++¢)
        target[¢] += addition[¢];
  }

  @NotNull
  private static ConstructorInfo asConstructor(final TypedEntity ¢) {
    return new ConstructorInfo(¢);
  }

  private static FieldInfo[] asFields(@NotNull final TypedEntity[] fields) {
    final List<FieldInfo> $ = new ArrayList<>();
    for (final TypedEntity ¢ : fields)
      if (!¢.isSynthetic())
        $.add(new FieldInfo(¢));
    return $.toArray(new FieldInfo[$.size()]);
  }

  @NotNull
  private static InitializerInfo asInitializer(final TypedEntity ¢) {
    return new InitializerInfo(¢);
  }

  @NotNull
  private static MethodInfo asMethod(final TypedEntity ¢) {
    return new MethodInfo(¢);
  }

  private static int codeSize(@NotNull final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity ¢ : es)
      $ += ¢.codeSize();
    return $;
  }

  private static int cyclomaticComplexity(@NotNull final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity ¢ : es)
      $ += ¢.cyclomaticComplexity();
    return $;
  }

  private static int instructionCount(@NotNull final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity ¢ : es)
      $ += ¢.instructionCount();
    return $;
  }

  private static boolean isConstructor(@NotNull final TypedEntity ¢) {
    return "<init>".equals(¢.name);
  }

  private static boolean isInitializer(@NotNull final TypedEntity ¢) {
    return "<clinit>".equals(¢.name);
  }

  private static boolean isMethod(@NotNull final TypedEntity ¢) {
    return !¢.name.startsWith("<");
  }

  private static ConstructorInfo[] selectConstructors(@NotNull final TypedEntity[] executables) {
    final List<ConstructorInfo> $ = new ArrayList<>();
    for (final TypedEntity ¢ : executables)
      if (isConstructor(¢) && !¢.isSynthetic())
        $.add(asConstructor(¢));
    return $.toArray(new ConstructorInfo[$.size()]);
  }

  private static InitializerInfo[] selectInitializers(@NotNull final TypedEntity[] executables) {
    final List<InitializerInfo> $ = new ArrayList<>();
    for (final TypedEntity ¢ : executables)
      if (isInitializer(¢) && !¢.isSynthetic())
        $.add(asInitializer(¢));
    return $.toArray(new InitializerInfo[$.size()]);
  }

  private static MethodInfo[] selectMethods(@NotNull final TypedEntity[] executables) {
    final List<MethodInfo> $ = new ArrayList<>();
    for (final TypedEntity ¢ : executables)
      if (isMethod(¢) && !¢.isSynthetic())
        $.add(asMethod(¢));
    return $.toArray(new MethodInfo[$.size()]);
  }

  private static int throwCount(@NotNull final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity ¢ : es)
      $ += ¢.throwCount();
    return $;
  }

  private String containingJar;
  private String classFileName;
  final Map<String, Integer> refsToStatics = new HashMap<>();
  public final String superClass;
  public final ClassConstant[] interfaces;
  @NotNull
  public final FieldInfo[] fields;
  @NotNull
  public final MethodInfo[] methods;
  @NotNull
  public final ConstructorInfo[] constructors;
  @NotNull
  public final InitializerInfo[] initializers;
  @NotNull
  public final String source;
  public final String packeageName;
  public final String shortName;

  ClassInfo(@NotNull final Builder b) {
    super(b);
    packeageName = b.packageName;
    shortName = b.shortName;
    superClass = b.superClass;
    interfaces = b.interfaces;
    fields = asFields(b.fields);
    methods = selectMethods(b.executables);
    initializers = selectInitializers(b.executables);
    constructors = selectConstructors(b.executables);
    source = findSource();
  }

  @NotNull
  public Abstraction abstraction() {
    return Abstraction.abstraction(this);
  }

  @Attribute public int abstractMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (¢.isAbstract())
        ++$;
    return $;
  }

  @Attribute public int accessedPublicConstructorsCount() {
    int $ = 0;
    for (final ConstructorInfo ¢ : constructors)
      if (¢.isPublic() && isAccessed(¢))
        ++$;
    return $;
  }

  @Attribute public int accessedPublicFieldsCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isPublic() && isAccessed(¢))
        ++$;
    return $;
  }

  @Attribute public int accessedPublicMethodsCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (¢.isPublic() && isAccessed(¢))
        ++$;
    return $;
  }

  public void addRefToStatic(final String staticFieldOrMethod) {
    MapUtil.addToValue(refsToStatics, staticFieldOrMethod, 1);
  }

  @Attribute public int codeSize() {
    return codeSize(methods) + codeSize(constructors) + codeSize(initializers);
  }

  @Attribute public int concreteMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (!¢.isAbstract())
        ++$;
    return $;
  }

  @Attribute public int constructorsCount() {
    return constructors.length;
  }

  @Attribute public int cyclomaticComplexity() {
    return cyclomaticComplexity(methods);
  }

  @Attribute public int defaultConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo ¢ : constructors)
      if (¢.isDefault())
        ++$;
    return $;
  }

  @Attribute public int defaultFieldCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isDefault())
        ++$;
    return $;
  }

  @Attribute public int defaultMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (¢.isDefault())
        ++$;
    return $;
  }

  @Override public boolean equals(@Nullable final Object o) {
    if (o == null || o.getClass() != getClass())
      return false;
    final ClassInfo other = (ClassInfo) o;
    return other.name().equals(name()) && other.fieldsCount() == fieldsCount() && other.methodCount() == methodCount()
        && other.constructorsCount() == constructorsCount() && other.referencedClasses() == referencedClasses()
        && other.referencedDoubles() == referencedDoubles() && other.referencedInts() == referencedInts()
        && other.referencedFloats() == referencedFloats() && other.referencedStrings() == referencedStrings()
        && other.referencedLongs() == referencedLongs() && other.codeSize() == codeSize();
  }

  @Attribute public int fieldsCount() {
    return fields.length;
  }

  public String getClassFileName() {
    return classFileName;
  }

  public String getContainingJar() {
    return containingJar;
  }

  @Override public int hashCode() {
    return name().hashCode();
  }

  @Attribute public int initializersCount() {
    return initializers.length;
  }

  @Attribute public int instructionCount() {
    return instructionCount(methods) + instructionCount(constructors) + instructionCount(initializers);
  }

  @Attribute public int interfacesCount() {
    return interfaces.length;
  }

  /** Returns true if this {@code Class} object represents an annotation type.
   * Note that if this method returns true, {@link #isInterface()} would also
   * return true, as all annotation types are also interfaces.
   * @return {@code true} if this class object represents an annotation type;
   *         {@code false} otherwise
   * @since 1.5 */
  public boolean isAnnotation() {
    return (flags & ANNOTATION) != 0;
  }

  public boolean isClass() {
    return !isInterface() && !isEnum() && !isAnnotation();
  }

  public boolean isEnum() {
    return (flags & ENUM) != 0;
  }

  /** Determines if the specified {@code Class} object represents an interface
   * type.
   * @return {@code true} if this object represents an interface; {@code false}
   *         otherwise. */
  public boolean isInterface() {
    return Modifier.isInterface(flags);
  }

  /** Returns {@code true} if this class is a synthetic class; returns
   * {@code false} otherwise.
   * @return {@code true} if and only if this class is a synthetic class as
   *         defined by the Java Language Specification.
   * @since 1.5 */
  @Override public boolean isSynthetic() {
    return (flags & SYNTHETIC) != 0;
  }

  @Nullable
  public Kind kind() {
    return Kind.kind(this);
  }

  @Attribute public int lackOfCohesion() {
    int emptyIntersect = 0;
    int nonEmptyIntersect = 0;
    for (final MethodInfo m1 : methods)
      for (final MethodInfo m2 : methods) {
        if (m1.hashCode() >= m2.hashCode())
          continue;
        final Set<String> s2 = m2.instanceVariables();
        s2.retainAll(m1.instanceVariables());
        boolean found = false;
        for (final String ¢ : s2)
          if (¢.startsWith(name + ":")) {
            ++nonEmptyIntersect;
            found = true;
            break;
          }
        if (!found)
          ++emptyIntersect;
      }
    return Math.max(emptyIntersect - nonEmptyIntersect, 0);
  }

  @Attribute public int methodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (!¢.isObjectMethod())
        ++$;
    return $;
  }

  @Attribute public int privateConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo ¢ : constructors)
      if (¢.isPrivate())
        ++$;
    return $;
  }

  @Attribute public int privateFieldCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isPrivate())
        ++$;
    return $;
  }

  @Attribute public int privateMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (¢.isPrivate())
        ++$;
    return $;
  }

  @Attribute public int protectedConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo ¢ : constructors)
      if (¢.isProtected())
        ++$;
    return $;
  }

  @Attribute public int protectedFieldCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isProtected())
        ++$;
    return $;
  }

  @Attribute public int protectedMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (!¢.isObjectMethod() && ¢.isProtected())
        ++$;
    return $;
  }

  @Attribute public int publicConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo ¢ : constructors)
      if (¢.isPublic())
        ++$;
    return $;
  }

  @Attribute public int publicFieldCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isPublic())
        ++$;
    return $;
  }

  @Attribute public int publicMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (!¢.isObjectMethod() && ¢.isPublic())
        ++$;
    return $;
  }

  @Attribute public int publicNonStaticFieldCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isPublic() && !¢.isStatic())
        ++$;
    return $;
  }

  @Attribute public int publicNonStaticMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (!¢.isObjectMethod() && ¢.isPublic() && !¢.isStatic())
        ++$;
    return $;
  }

  @Attribute public int publicStaticFieldCount() {
    int $ = 0;
    for (final FieldInfo ¢ : fields)
      if (¢.isPublic() && ¢.isStatic())
        ++$;
    return $;
  }

  @Attribute public int publicStaticMethodCount() {
    int $ = 0;
    for (final MethodInfo ¢ : methods)
      if (!¢.isObjectMethod() && ¢.isPublic() && ¢.isStatic())
        ++$;
    return $;
  }

  @Attribute public int referencedClasses() {
    return constantPool.getReferencedClasses().length;
  }

  @Attribute public int referencedDoubles() {
    return constantPool.getReferencedDoubles().length;
  }

  @Attribute public int referencedFloats() {
    return constantPool.getReferencedFloats().length;
  }

  @Attribute public int referencedInts() {
    return constantPool.getReferencedInts().length;
  }

  @Attribute public int referencedLongs() {
    return constantPool.getReferencedLongs().length;
  }

  @Attribute public int referencedStrings() {
    return constantPool.getReferencedStrings().length;
  }

  @NotNull
  public int[] referencesToClass(final String className) {
    final int[] $ = new int[LinkComponents.values().length];
    for (final MethodInfo ¢ : methods)
      addLinkComponents($, ¢.referencesToClass(className));
    for (final ConstructorInfo ¢ : constructors)
      addLinkComponents($, ¢.referencesToClass(className));
    for (final InitializerInfo ¢ : initializers)
      addLinkComponents($, ¢.referencesToClass(className));
    if (superClass.equals(className))
      ++$[LinkComponents.SuperClass.ordinal()];
    for (final ClassConstant i : interfaces)
      if (i.getClassName().equals(className))
        ++$[LinkComponents.SuperInterface.ordinal()];
    for (final FieldInfo ¢ : fields)
      if ((¢.type + "").equals(className))
        ++$[LinkComponents.FieldDeclaration.ordinal()];
    return $;
  }

  @Attribute public int responseForClass() {
    final String[] referencedMethods = constantPool.getReferencedMethods();
    int $ = 0;
    for (final String ¢ : referencedMethods)
      if (!¢.startsWith(name + ":"))
        ++$;
    return $ + methodCount();
  }

  public void setClassFileName(final String fileName) {
    classFileName = fileName;
  }

  public void setContainingJar(final String jar) {
    containingJar = jar;
  }

  @Attribute public int throwCount() {
    return throwCount(methods) + throwCount(constructors) + throwCount(initializers);
  }

  @NotNull
  private String findSource() {
    final AttributeInfo a = findAttribute("SourceFile");
    return a == null ? "" : a.reader(constantPool).readStringConstant();
  }

  private boolean isAccessed(final TypedEntity e) {
    for (final ExecutableEntity ¢ : methods)
      if (¢.isAccessed(e, name))
        return true;
    for (final ExecutableEntity ¢ : constructors)
      if (¢.isAccessed(e, name))
        return true;
    for (final ExecutableEntity ¢ : initializers)
      if (¢.isAccessed(e, name))
        return true;
    return false;
  }

  public enum Abstraction {
    ABSTRACT, FINAL, PLAIN;
    @NotNull
    public static Abstraction abstraction(@NotNull final ClassInfo ¢) {
      return ¢.isAbstract() ? ABSTRACT : ¢.isFinal() ? FINAL : PLAIN;
    }
  }

  /** @author Yossi Gil
   * @since 21 November 2011 */
  public static class Builder extends RobustReader {
    /** Magic number identifying Java class files */
    static final int MAGIC = 0xCAFEBABE;
    final int majorVersion;
    final int minorVersion;
    @NotNull
    final ConstantPool constantPool;
    final int accessFlags;
    final AttributeInfo[] attributes;
    final TypedEntity[] executables;
    final TypedEntity[] fields;
    final ClassConstant[] interfaces;
    final String superClass;
    final String name;
    final String shortName;
    final String packageName;

    /** Instantiate this class from a given file name.
     * @param c a non-<code><b>null</b></code> representing a valid
     *        {@link Class} object */
    public Builder(final Class<?> c) {
      this(CLASSFILES.open(c));
    }

    /** Instantiate this class from a given file name.
     * @param f a non-<code><b>null</b></code> representing a valid
     *        <tt>.class</tt> file name @ * in case the file could not be opened
     *        or processed */
    public Builder(final File f) {
      this(asFileInputStream(f));
    }

    /** Instantiate this class from a given input stream
     * @param is a non-<code><b>null</b></code> representing an input stream
     *        containing a valid <tt>.class</tt> @ * in case the file could not
     *        be opened or processed */
    public Builder(final InputStream is) {
      this(asDataInputStream(is));
    }

    /** Instantiate this class from a given file name.
     * @param fileName a non-<code><b>null</b></code> representing a valid
     *        <tt>.class</tt> file name */
    public Builder(@NotNull final String fileName) {
      this(new File(fileName));
    }

    /** Extracts the constant pool from the specified data stream of a class
     * file.
     * @param s a non-<code><b>null</b></code> providing a stream of a class
     *        file starting at the first byte. @ * in case of reading errors or
     *        invalid class file. */
    private Builder(final DataInputStream s) {
      super(s);
      if (readInt() != MAGIC)
        recordError(new Exception("Not a class file: bad magic number."));
      minorVersion = readUnsignedShort();
      majorVersion = readUnsignedShort();
      constantPool = new ConstantPool(this);
      final ConstantPoolReader r = new ConstantPoolReader(s, constantPool);
      accessFlags = readUnsignedShort();
      final int classIndex = readUnsignedShort();
      name = r.readClassName(classIndex);
      shortName = r.classShortName(classIndex);
      packageName = r.classPackage(classIndex);
      superClass = r.readClassName();
      interfaces = r.readClasses();
      fields = r.readMembers();
      executables = r.readMembers();
      attributes = r.readAttributes();
    }

    @NotNull
    public String[] getReferencedClasses() {
      return constantPool.getReferencedClasses();
    }

    public double[] getReferencedDoubles() {
      return constantPool.getReferencedDoubles();
    }

    public float[] getReferencedFloats() {
      return constantPool.getReferencedFloats();
    }

    public int[] getReferencedInts() {
      return constantPool.getReferencedInts();
    }

    public long[] getReferencedLongs() {
      return constantPool.getReferencedLongs();
    }

    @NotNull
    public String[] getReferencedStrings() {
      return constantPool.getReferencedStrings();
    }

    @Nullable
    public ClassInfo go() {
      try {
        close();
      } catch (@NotNull final Throwable e1) {
        e1.printStackTrace();
      }
      if (hasErrors())
        return null;
      try {
        return new ClassInfo(this);
      } catch (@NotNull final CorruptClassFile e) {
        return null;
      }
    }
  }

  public enum Kind {
    INTERFACE, ANNOTATION, CLASS, ENUM;
    public static Kind kind(@NotNull final ClassInfo ¢) {
      return ¢.isInterface() ? Kind.INTERFACE : ¢.isClass() ? Kind.CLASS : ¢.isEnum() ? Kind.ENUM : ¢.isAnnotation() ? Kind.ANNOTATION : null;
    }
  }

  public enum LinkComponents {
    SuperClass, SuperInterface, FieldDeclaration, MethodDeclaration, Instantiation, FieldAccess, StaticFieldAccess, MethodInvocation, StaticMethodInvocation
  }
}
