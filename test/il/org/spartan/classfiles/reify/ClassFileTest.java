package il.org.spartan.classfiles.reify;

import static il.org.spartan.azzert.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ClassInfo.*;
import il.org.spartan.external.*;
import il.org.spartan.files.visitors.*;
import il.org.spartan.files.visitors.FileSystemVisitor.*;
import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;
import il.org.spartan.utils.*;

@SuppressWarnings("static-method") public class ClassFileTest {
  private static final String GOOD_FILE_PATH = "bin/il/ac/technion/cs/ssdl/classfiles/reify/ClassFileTest.class";
  private static final String BAD_FILE_PATH = "asdfasdfaafasdfas";

  @Test public void allAccessLevels() {
    final TypedEntity[] methods = ClassInfo.make(AllAccessLevels.class).methods;
    assert methods[0].isPrivate();
    assert methods[1].isDefault();
    assert methods[2].isProtected();
    assert methods[3].isPublic();
  }

  @Test public void allAccessLevelsNegative() {
    final TypedEntity[] methods = ClassInfo.make(AllAccessLevels.class).methods;
    assert !methods[1].isPrivate();
    assert !methods[2].isPrivate();
    assert !methods[3].isPrivate();
    assert !methods[0].isDefault();
    assert !methods[2].isDefault();
    assert !methods[3].isDefault();
    assert !methods[0].isProtected();
    assert !methods[1].isProtected();
    assert !methods[3].isProtected();
    assert !methods[0].isPublic();
    assert !methods[1].isPublic();
    assert !methods[2].isPublic();
  }

  @Test public void allPrimitiveFieldsArePrimitive() {
    for (final TypedEntity f : ClassInfo.make(AllPrimitiveFields.class).fields)
      assert f.type.isPrimitive();
  }

  @Test public void allPrimitiveFieldsAttributes() {
    for (final FlaggedEntity ¢ : ClassInfo.make(AllPrimitiveFields.class).fields)
      azzert.that(¢.attributes.length, is(0));
  }

  @Test public void allPrimitiveFieldsCount() {
    azzert.that(ClassInfo.make(AllPrimitiveFields.class).fields.length, is(7));
  }

  @Test public void allPrimitiveFieldsModifiers() {
    for (final FlaggedEntity ¢ : ClassInfo.make(AllPrimitiveFields.class).fields)
      azzert.that(¢.flags, is(Modifier.FINAL | Modifier.PUBLIC));
  }

  @Test public void allPrimitiveFieldsNames() {
    final NamedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    azzert.that(fields[0].name, is("firstField"));
    azzert.that(fields[1].name, is("secondField"));
    azzert.that(fields[2].name, is("thirdField"));
    azzert.that(fields[3].name, is("fourthField"));
    azzert.that(fields[4].name, is("fifthField"));
    azzert.that(fields[5].name, is("sixthField"));
  }

  @Test public void allPrimitiveFieldsTypes() {
    final TypedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    azzert.that(fields[0].type + "", is("byte"));
    azzert.that(fields[1].type + "", is("short"));
    azzert.that(fields[2].type + "", is("long"));
    azzert.that(fields[3].type + "", is("float"));
    azzert.that(fields[4].type + "", is("double"));
    azzert.that(fields[5].type + "", is("char"));
    azzert.that(fields[6].type + "", is("boolean"));
  }

  @Test public void annotationCounts() {
    final ClassInfo __ = ClassInfo.make(Annotation.class);
    azzert.that(__.constructors.length, is(0));
    azzert.that(__.methods.length, is(1));
    azzert.that(__.fields.length, is(0));
    azzert.that(__.initializers.length, is(0));
  }

  @Test public void annotationFlags() {
    azzert.that(ClassInfo.make(Annotation.class).flags & ~(Modifier.ABSTRACT | Modifier.INTERFACE | FlaggedEntity.ANNOTATION), is(0));
  }

  @Test public void annotationIsAnnotation() {
    assert ClassInfo.make(Annotation.class).isAnnotation();
  }

  @Test public void annotationIsDeprecated() {
    assert !ClassInfo.make(Annotation.class).isDeprecated();
  }

  @Test public void annotationIsEnum() {
    assert !ClassInfo.make(Annotation.class).isEnum();
  }

  @Test public void annotationIsSynthetic() {
    assert !ClassInfo.make(Annotation.class).isSynthetic();
  }

  @Test public void arrayField() {
    final TypedEntity field = ClassInfo.make(ArrayField.class).fields[0];
    azzert.that(field.name, is("fieldName"));
    azzert.that(field.descriptor, is("[[[[I"));
    azzert.that(field.type + "", is("int[][][][]"));
  }

  @Test public void badFile() {
    assert new File(BAD_FILE_PATH) != null;
  }

  @Test(expected = FileNotFoundException.class) public void badFileInputStream() throws FileNotFoundException {
    assert new FileInputStream(new File(BAD_FILE_PATH)) != null;
  }

  @Test public void badInputStream() {
    azzert.isNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void byteCodeCount() {
    class __ {
      // empty
    }
    assert ClassInfo.make(__.class).methods[0].getCode().codes.length > 0;
  }

  @Test public void classInfoFromBadPath() {
    azzert.isNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void classInfoFromBadPathCLASSFILE() {
    azzert.isNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void classWithCapitalL() {
    final TypedEntity field = ClassInfo.make(ClassWithCapitalL.class).fields[0];
    azzert.that(field.name, is("fieldName"));
    azzert.that(field.descriptor, is("Lil/ac/technion/cs/ssdl/classfiles/reify/ClassFileTest$ClassWithCapitalL;"));
    azzert.that(field.type + "", is("il.org.spartan.classfiles.reify.ClassFileTest$ClassWithCapitalL"));
  }

  @Test public void codeAttributeExists() {
    class __ {
    }
    assert ClassInfo.make(__.class).methods[0].getCode() != null;
  }

  @Test public void codeAttributeMaxLocals() {
    class __ {
    }
    azzert.that(ClassInfo.make(__.class).methods[0].getCode().maxLocals, is(1));
  }

  @Test public void codeAttributeMaxStack() {
    class __ {
    }
    azzert.that(ClassInfo.make(__.class).methods[0].getCode().maxStack, is(0));
  }

  @Test public void complexClassConstructorCounts() {
    azzert.that(ClassInfo.make(LargeClass.class).constructorsCount(), is(5));
  }

  @Test public void complexClassCounts() {
    azzert.that(ClassInfo.make(LargeClass.class).initializersCount(), is(1));
  }

  @Test public void complexClassFieldsCounts() {
    azzert.that(ClassInfo.make(LargeClass.class).fieldsCount(), is(6));
  }

  @Test public void complexClassFieldsInterfaceCounts() {
    azzert.that(ClassInfo.make(LargeClass.class).interfacesCount(), is(3));
  }

  @Test public void complexClassFileAbstraction() {
    azzert.that(ClassInfo.make(LargeClass.class).abstraction(), is(Abstraction.PLAIN));
  }

  @Test public void complexClassFileKind() {
    azzert.that(ClassInfo.make(LargeClass.class).kind(), is(Kind.CLASS));
  }

  @Test public void complexClassMethodCounts() {
    azzert.that(ClassInfo.make(LargeClass.class).methodCount(), is(8));
  }

  @Test public void complexClassMethodNames() {
    final MethodInfo[] ms = ClassInfo.make(LargeClass.class).methods;
    int i = 0;
    azzert.that(ms[i++].name, is("method0"));
    azzert.that(ms[i++].name, is("method1"));
    azzert.that(ms[i++].name, is("method2"));
    azzert.that(ms[i++].name, is("method3"));
    azzert.that(ms[i++].name, is("method4"));
    azzert.that(ms[i++].name, is("method5"));
    azzert.that(ms[i++].name, is("method6"));
    azzert.that(ms[i++].name, is("method7"));
  }

  @Test public void complexMethod() {
    azzert.that(ClassInfo.make(ComplexMethod.class).methods[0].type + "", is("java.lang.Object[][][] (java.lang.Void, java.lang.Object, int, float, double[][])"));
  }

  @Test public void dataInputStreamFromPath() throws FileNotFoundException {
    assert new DataInputStream(new FileInputStream(new File(GOOD_FILE_PATH))) != null;
  }

  @Test public void deprecatedClassIsDeprecated() {
    assert ClassInfo.make(DeprecatedClass.class).isDeprecated();
    assert ClassInfo.make(DeprecatedClass.class).constructors[0].isDeprecated();
  }

  @Test public void deprecatedClassIsSynthetic() {
    assert !ClassInfo.make(DeprecatedClass.class).isSynthetic();
    assert !ClassInfo.make(DeprecatedClass.class).constructors[0].isSynthetic();
  }

  @Test public void emptlyClassInteface() {
    final ClassInfo __ = ClassInfo.make(EmptyClass.class);
    assert !__.isAbstract();
    assert !__.isInterface();
    assert __.isClass();
  }

  @Test public void emptyClassConstructorExceptions() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].exceptions.length, is(0));
  }

  @Test public void emptyClassDefaultConstructorAttributesLength() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].attributes.length, is(1));
  }

  @Test public void emptyClassDefaultConstructorAttributesName() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].attributes[0].name, is("Code"));
  }

  @Test public void emptyClassDefaultConstructorFlags() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].flags, is(Modifier.PUBLIC));
  }

  @Test public void emptyClassDefaultConstructorgFlags() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].flags, is(Modifier.PUBLIC));
  }

  @Test public void emptyClassDefaultConstructorIsSynthetic() {
    assert !ClassInfo.make(EmptyClass.class).constructors[0].isSynthetic();
  }

  @Test public void emptyClassDefaultConstructorName() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].name, is(""));
  }

  @Test public void emptyClassDefaultConstructorType() {
    azzert.that(ClassInfo.make(EmptyClass.class).constructors[0].type + "", is("()"));
  }

  @Test public void emptyClassDescriptor() {
    final TypedEntity constructor = ClassInfo.make(EmptyClass.class).constructors[0];
    azzert.that(constructor.name, is(""));
    azzert.that(constructor.descriptor, is("()V"));
  }

  @Test public void emptyClassDGetConstructors() {
    assert ClassInfo.make(EmptyClass.class).constructors != null;
  }

  @Test public void emptyClassFlags() {
    azzert.that(ClassInfo.make(EmptyClass.class).flags, is(Modifier.PUBLIC | Modifier.SYNCHRONIZED));
  }

  @Test public void emptyClassIsDeprecated() {
    assert !ClassInfo.make(EmptyClass.class).isDeprecated();
    assert !ClassInfo.make(EmptyClass.class).constructors[0].isDeprecated();
  }

  @Test public void emptyClassNFields() {
    azzert.that(ClassInfo.make(EmptyClass.class).fields.length, is(0));
  }

  @Test public void emptyClassNInterfaces() {
    azzert.that(ClassInfo.make(EmptyClass.class).interfaces.length, is(0));
  }

  @Test public void emptyClassNMethods() {
    azzert.that(ClassInfo.make(EmptyClass.class).methods.length, is(0));
  }

  @Test public void emptyClassOneMethodNFields() {
    azzert.that(ClassInfo.make(EmptyClassOneMethod.class).fields.length, is(0));
  }

  @Test public void emptyClassOneMethodNMethods() {
    azzert.that(ClassInfo.make(EmptyClassOneMethod.class).methods.length, is(1));
  }

  @Test public void enumClassFile() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assert !__.isAbstract();
    assert !__.isInterface();
    assert !__.isStatic();
  }

  @Test public void enumClassFileIsAnnotation() {
    assert !ClassInfo.make(Enum.class).isAnnotation();
  }

  @Test public void enumClassFileIsEnum() {
    assert ClassInfo.make(Enum.class).isEnum();
  }

  @Test public void enumConstructorIsSynthetic() {
    assert ClassInfo.make(Enum.class).constructors[0].isSynthetic();
  }

  @Test public void enumConstructorsCounts() {
    azzert.that(ClassInfo.make(Enum.class).constructors.length, is(1));
  }

  @Test public void enumIsDeprecated() {
    assert !ClassInfo.make(Enum.class).isDeprecated();
    assert !ClassInfo.make(Enum.class).constructors[0].isDeprecated();
  }

  @Test public void enumIsSynthetic() {
    assert !ClassInfo.make(Enum.class).isSynthetic();
  }

  @Test public void enumMethod() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    azzert.that(__.methods.length, is(1));
    azzert.that(__.constructors.length, is(0));
    azzert.that(__.initializers.length, is(0));
    assert __.methods[0].isAbstract();
    assert __.methods[0].isPublic();
  }

  @Test public void enumMethodCounts() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    azzert.that(__.methods.length, is(2));
    azzert.that(__.initializers.length, is(0));
  }

  @Test public void enumMethodName() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    azzert.that(__.methods[0].name, is("myMethod"));
    azzert.that(__.methods[1].name, is("values"));
    assert __.methods[1].isSynthetic();
  }

  @Test public void fileFromPath() {
    assert new File(GOOD_FILE_PATH) != null;
  }

  @Test public void findSelf() {
    assert this.getClass() != null;
  }

  @Test public void InitializerConstructors() {
    final NamedEntity[] ctros = ClassInfo.make(Initializer.class).constructors;
    azzert.that(ctros.length, is(2));
    azzert.that(ctros[0].name, is(""));
    azzert.that(ctros[1].name, is(""));
  }

  @Test public void inputStreamFromPath() throws FileNotFoundException {
    assert new FileInputStream(new File(GOOD_FILE_PATH)) != null;
  }

  @Test public void interfaceClassFile() {
    final ClassInfo __ = ClassInfo.make(Interface.class);
    assert __.isAbstract();
    assert __.isInterface();
  }

  @Test public void interfaceIsDeprecated() {
    assert !ClassInfo.make(Interface.class).isDeprecated();
  }

  @Test public void interfaceIsSynthetic() {
    assert !ClassInfo.make(Interface.class).isSynthetic();
  }

  @Test public void interfaceMethod() {
    final ClassInfo __ = ClassInfo.make(Interface.class);
    azzert.that(__.methods.length, is(1));
    azzert.that(__.constructors.length, is(0));
    azzert.that(__.initializers.length, is(0));
    assert __.methods[0].isAbstract();
    assert __.methods[0].isPublic();
  }

  @Test public void interfaceSuperClassName() {
    azzert.that(ClassInfo.make(Serializable.class).superClass, is(Object.class.getCanonicalName()));
  }

  @Test public void intFieldName() {
    final TypedEntity field = ClassInfo.make(IntField.class).fields[0];
    azzert.that(field.name, is("fieldName"));
    azzert.that(field.descriptor, is("I"));
    azzert.that(field.type + "", is("int"));
  }

  @Test public void isClass() {
    assert !ClassInfo.make(Annotation.class).isClass();
    assert !ClassInfo.make(Enum.class).isClass();
    assert !ClassInfo.make(Interface.class).isClass();
    assert ClassInfo.make(EmptyClass.class).isClass();
  }

  @Test public void meFlags() {
    azzert.that(ClassInfo.make(this.getClass()).flags, is(Modifier.PUBLIC | Modifier.SYNCHRONIZED));
  }

  @Test public void meHasCorrectInteface() {
    azzert.that(ClassInfo.make(this.getClass()).interfaces[0] + "", is(Serializable.class.getCanonicalName()));
  }

  @Test public void meHasOneInteface() {
    azzert.that(ClassInfo.make(this.getClass()).interfaces.length, is(1));
  }

  @Test public void meName() {
    azzert.that(ClassInfo.make(this.getClass()).name, is(this.getClass().getCanonicalName()));
  }

  @Test public void meSuperClassName() {
    azzert.that(ClassInfo.make(this.getClass()).superClass, is(Object.class.getCanonicalName()));
  }

  @Test public void methodExceptionsLength() {
    class __ {
    }
    azzert.that(ClassInfo.make(__.class).methods[0].exceptions.length, is(2));
  }

  @Test public void methodExceptionsName() {
    class __ {
    }
    azzert.that(ClassInfo.make(__.class).methods[0].exceptions[0] + "", is(IOException.class.getName()));
    azzert.that(ClassInfo.make(__.class).methods[0].exceptions[1] + "", is(ArrayIndexOutOfBoundsException.class.getName()));
  }

  @Test public void methodWithParameters() {
    final TypedEntity method = ClassInfo.make(MethodWithParametrs.class).methods[0];
    azzert.that(method.name, is("method"));
    azzert.that(method.descriptor, is("(III)Ljava/lang/Integer;"));
  }

  @Test public void nameOneField() {
    azzert.that(ClassInfo.make(OneField.class).fields[0].name, is("fieldName"));
  }

  @Test public void namesTwoAnnotatedFields() {
    final NamedEntity[] fields = ClassInfo.make(TwoAnnotatedFields.class).fields;
    azzert.that(fields[0].name, is("firstField"));
    azzert.that(fields[1].name, is("secondField"));
  }

  @Test public void namesTwoFields() {
    final NamedEntity[] fields = ClassInfo.make(TwoFields.class).fields;
    azzert.that(fields[0].name, is("firstField"));
    azzert.that(fields[1].name, is("secondField"));
  }

  @Test public void nameTwoMethods() {
    final ClassInfo __ = ClassInfo.make(TwoMethods.class);
    azzert.that(__.constructors[0].name, is(""));
    azzert.that(__.methods[0].name, is("firstMethod"));
    azzert.that(__.methods[1].name, is("secondMethod"));
  }

  @Test public void noByteCodeEmptyClassConstructor() {
    azzert.isNull(ClassInfo.make(EmptyClass.class).constructors[0].code);
  }

  @Test public void noByteCodeEmptyConstructor() {
    class __ {
    }
    azzert.isNull(ClassInfo.make(__.class).constructors[0].code);
  }

  @Test public void noByteCodeEmptyConstructorCallingSuper() {
    class __ {
    }
    azzert.isNull(ClassInfo.make(__.class).constructors[0].code);
  }

  @Test public void noExceptionsCount() {
    class __ {
    }
    azzert.that(ClassInfo.make(__.class).methods[0].exceptions.length, is(2));
  }

  @Test public void objecClassCreate() {
    assert ClassInfo.make(Object.class) != null;
  }

  @Test public void objectFieldName() {
    final TypedEntity field = ClassInfo.make(ObjectField.class).fields[0];
    azzert.that(field.name, is("fieldName"));
    azzert.that(field.descriptor, is("Ljava/lang/Object;"));
    azzert.that(field.type + "", is("java.lang.Object"));
  }

  @Test public void objectSuperClassName() {
    azzert.isNull(ClassInfo.make(Object.class).superClass);
  }

  @Test public void omeMethoExceptions() {
    assert !ClassInfo.make(Annotation.class).isSynthetic();
  }

  @Test public void oneFieldFieldCount() {
    azzert.that(ClassInfo.make(OneField.class).fields.length, is(1));
  }

  @Test public void oneFieldFieldIsDeperecated() {
    assert ClassInfo.make(OneField.class).fields[0].isDeprecated();
  }

  @Test public void oneFieldFieldIsSynthetic() {
    assert !ClassInfo.make(OneField.class).fields[0].isSynthetic();
  }

  @Test public void oneFieldFlags() {
    azzert.that(ClassInfo.make(OneField.class).fields[0].flags, is(Modifier.FINAL | Modifier.PUBLIC | FlaggedEntity.DEPRECATED));
  }

  @Test public void oneFieldIsDeperecated() {
    assert !ClassInfo.make(OneField.class).isDeprecated();
  }

  @Test public void oneFieldIsSynthetic() {
    assert !ClassInfo.make(OneField.class).isSynthetic();
  }

  @Test public void oneMethodFlags() {
    azzert.that(ClassInfo.make(OneMethod.class).methods[0].flags, is(Modifier.FINAL | Modifier.PUBLIC));
  }

  @Test public void oneMethodLengths() {
    final ClassInfo __ = ClassInfo.make(OneMethod.class);
    azzert.that(__.constructors.length, is(1));
    azzert.that(__.methods.length, is(1));
    azzert.that(__.fields.length, is(0));
    azzert.that(__.initializers.length, is(0));
  }

  @Test public void oneMethodName() {
    final ClassInfo __ = ClassInfo.make(OneMethod.class);
    azzert.that(__.constructors[0].name, is(""));
    azzert.that(__.methods[0].name, is("methodName"));
  }

  @Test public void oneMethodSize() {
    azzert.that(ClassInfo.make(OneMethod.class).methods.length, is(1));
  }

  @Test public void oneStaticMethodSize() {
    azzert.that(ClassInfo.make(OneMethod.class).methods.length, is(1));
  }

  @Test public void openBadPath() {
    Assert.assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void openEmptyClass() {
    assert ClassInfo.make(EmptyClass.class) != null;
  }

  @Test public void openPath() {
    assert ClassInfo.make(GOOD_FILE_PATH) != null;
  }

  @Test public void openPathCLASSFILES() {
    assert ClassInfo.make(this.getClass()) != null;
  }

  @Test public void openPathCLASSFILESBadPath() {
    azzert.isNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void openSelf() {
    assert CLASSFILES.open(this.getClass()) != null;
  }

  @Test public void openSelfNoErrors() {
    final Builder b = new ClassInfo.Builder(this.getClass());
    b.go();
    assert !b.hasErrors();
  }

  @Test public void parseAllPathMethods() {
    final Parser p = new Parser();
    p.parse(CLASSPATH.asArray());
    System.out.println(p);
  }

  @Test public void parseAllProjectMethods() {
    final Parser p = new Parser();
    p.parse(".");
    System.out.println(p);
  }

  @Test public void parseAllWorspace() {
    final Parser p = new Parser();
    p.parse("..");
    System.out.println(p);
  }

  @Test public void parseByteCodesSelfFirstMethod() {
    assert ClassInfo.make(this.getClass()).methods[0].getCode().instructionsCount() > 0;
  }

  @Test public void parseByteCodesTwoMethods() {
    class __ {
      boolean ____(final int __) {
        return __ % 1 == 0 ? ____(__ * __) : ____(__ % 7) != ______(__ * __ % 3);
      }

      boolean ______(final int __) {
        return __ == 1 == ____(__);
      }
    }
    assert ClassInfo.make(__.class).methods[0].getCode().instructionsCount() > ClassInfo.make(__.class).methods[1].getCode().instructionsCount();
  }

  @Test public void parseComplexMethod() {
    class ClassWithComplexMethod {
    }
    ClassInfo.make(ClassWithComplexMethod.class).methods[0].getCode().instructionsCount();
  }

  @Test public void parseSelfAllMethods() {
    for (final MethodInfo ¢ : ClassInfo.make(this.getClass()).methods)
      ¢.getCode().instructionsCount();
  }

  @Test public void parseTableSwitch() {
    class __ {
    }
    azzert.that(ClassInfo.make(__.class).methods[0].name, is("tableSwitch"));
    assert ClassInfo.make(__.class).methods[0].getCode().instructionsCount() > 20;
  }

  @Test public void readFromDataInputStreamFromPath() throws IOException {
    final DataInputStream f = new DataInputStream(new FileInputStream(new File(GOOD_FILE_PATH)));
    azzert.that(f.readInt(), is(ClassInfo.Builder.MAGIC));
    f.close();
  }

  @Test public void readFromNoExceptionsReaderFromBadPath() {
    azzert.that(new RobustReader(BAD_FILE_PATH).readInt(), is(0));
  }

  @Test public void readFromNoExceptionsReaderFromGoodPath() {
    azzert.that(new RobustReader(GOOD_FILE_PATH).readInt(), is(ClassInfo.Builder.MAGIC));
  }

  @Test public void selfMakeNotNull() {
    assert ClassInfo.make(this.getClass()) != null;
  }

  @Test public void selfSource() {
    azzert.that(ClassInfo.make(this.getClass()).source, is(this.getClass().getSimpleName() + ".java"));
  }

  @Test public void selfSourceNotNul() {
    assert ClassInfo.make(this.getClass()).source != null;
  }

  @Test public void singleStaticInitilizerLengths() {
    final ClassInfo __ = ClassInfo.make(SingleStaticInitializer.class);
    azzert.that(__.constructors.length, is(1));
    azzert.that(__.methods.length, is(0));
    azzert.that(__.fields.length, is(0));
    azzert.that(__.initializers.length, is(1));
  }

  @Test public void staticInitializerIsPrivate() {
    assert ClassInfo.make(StaticInitializer.class).initializers[0].isDefault();
  }

  @Test public void staticInitializerIsStatic() {
    assert ClassInfo.make(StaticInitializer.class).initializers[0].isStatic();
  }

  @Test public void staticInitializerNames() {
    final ClassInfo __ = ClassInfo.make(StaticInitializer.class);
    azzert.that(__.methods.length, is(0));
    azzert.that(__.initializers[0].name, is(""));
    azzert.that(__.constructors[0].name, is(""));
    azzert.that(__.constructors[1].name, is(""));
  }

  @Test public void staticInitializerType() {
    azzert.that(ClassInfo.make(StaticInitializer.class).initializers[0].type + "", is("()"));
  }

  @Test public void twoFieldsAreNotPrimtive() {
    for (final TypedEntity f : ClassInfo.make(TwoFields.class).fields)
      assert !f.type.isPrimitive();
  }

  @Test public void twoFieldsFlags() {
    final FlaggedEntity[] fields = ClassInfo.make(TwoFields.class).fields;
    azzert.that(fields[0].flags, is(Modifier.FINAL | Modifier.STATIC));
    azzert.that(fields[1].flags, is(Modifier.FINAL | Modifier.PRIVATE));
  }

  @Test public void twoMethodLengths() {
    final ClassInfo __ = ClassInfo.make(OneMethod.class);
    azzert.that(__.constructors.length, is(1));
    azzert.that(__.methods.length, is(1));
    azzert.that(__.fields.length, is(0));
    azzert.that(__.initializers.length, is(0));
  }

  @Test public void twoMethodsCount() {
    azzert.that(ClassInfo.make(TwoMethods.class).methods.length, is(2));
  }

  @Test public void twoMethodsFlags() {
    final FlaggedEntity[] methods = ClassInfo.make(TwoMethods.class).methods;
    azzert.that(methods[0].flags, is(Modifier.ABSTRACT));
    azzert.that(methods[1].flags, is(Modifier.FINAL | Modifier.PUBLIC | Modifier.STATIC));
  }

  @Test public void voidMethod() {
    azzert.that(ClassInfo.make(VoidMethod.class).methods[0].descriptor, is("()V"));
    azzert.that(ClassInfo.make(VoidMethod.class).methods[0].type + "", is("void ()"));
  }

  public abstract static class AllAccessLevels {
    public abstract void f4();

    protected abstract void f3();

    void f2() {
      f1();
    }

    private void f1() {
      f1();
    }
  }

  public static class AllPrimitiveFields {
    public final byte firstField = (byte) hashCode();
    public final short secondField = (short) hashCode();
    public final long thirdField = (byte) hashCode();
    public final float fourthField = hashCode();
    public final double fifthField = hashCode();
    public final char sixthField = toString().charAt(0);
    public final boolean seventhField = hashCode() > 100;
  }

  public static class ArrayField {
    int[][][][] fieldName;
  }

  public static class ClassWithCapitalL {
    ClassWithCapitalL fieldName = new ClassWithCapitalL();
  }

  public static class ComplexMethod {
    public final Object[][][] m(final Void v, final Object a, final int b, final float c, final double[][] dss) {
      ___.unused(v, a, Box.it(b), Box.it(c));
      return new Object[dss.length][][];
    }
  }

  @Deprecated public static class DeprecatedClass {
    @Deprecated DeprecatedClass() {
    }
  }

  public static class EmptyClass {
    // Empty
  }

  public static class EmptyClassOneMethod {
    void empty() throws IOException {
      throw new IOException();
    }
  }

  public abstract static class Initializer {
    {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }

    Initializer() {
    }

    Initializer(final String s) {
      System.getenv(s);
    }
  }

  public static class IntField {
    int fieldName = 5;
  }

  public abstract static class MethodWithParametrs {
    public MethodWithParametrs() {
    }

    public final Integer method(final int a, final int b, final int c) {
      return Integer.valueOf(a + b + c + hashCode());
    }
  }

  public static class ObjectField {
    Object fieldName = new Object();
  }

  public static class OneField {
    @Deprecated public final String fieldName = null;
  }

  public static class OneMethod {
    public final Object methodName() {
      return this;
    }
  }

  public static class OneStaticMethod {
    public static String methodName() {
      return null;
    }
  }

  public static class Parser {
    int nFiles;
    int nMethods;
    int nCodes;
    int nInstructions;
    Stopper s = new Stopper();

    /** [[SuppressWarningsSpartan]] */
    @Override public String toString() {
      final long stop = s.time();
      return String.format(
          "Processed %s files, consisting of %s methods with %s code blocks\n" + "comprising a total of %s bytecode instructions in %s.",
          Unit.INTEGER.format(nFiles), Unit.INTEGER.format(nMethods), Unit.INTEGER.format(nCodes), Unit.INTEGER.format(nInstructions),
          Unit.NANOSECONDS.format(stop)) + "";
    }

    void parse(final ClassInfo ¢) {
      parse(¢.methods);
    }

    void parse(final MethodInfo[] is) {
      ++nFiles;
      for (final MethodInfo ¢ : is)
        parseMethod(¢);
    }

    void parse(final String... path) {
      try {
        new ClassFilesVisitor(path, new FileOnlyAction() {
          File zipFile;

          @Override public void visitFile(final File f) {
            try {
              parse(ClassInfo.make(f));
            } catch (final RuntimeException e) {
              System.out.println("\n** " + f + ": " + e);
            }
          }

          @Override public void visitZip(final File zip) {
            zipFile = zip;
          }

          @Override public void visitZipEntry(final String entryName, final InputStream s) {
            try {
              parse(ClassInfo.make(s));
            } catch (final RuntimeException e) {
              System.out.println(zipFile + ":" + entryName);
            }
          }
        }).go();
      } catch (final IOException e) {
        e.printStackTrace();
      } catch (final StopTraversal e) {
        e.printStackTrace();
      }
    }

    private void parse(final CodeEntity ¢) {
      ++nCodes;
      nInstructions += ¢.instructionsCount();
    }

    private void parseMethod(final MethodInfo ¢) {
      ++nMethods;
      if (¢.getCode() != null)
        parse(¢.getCode());
    }
  }

  public abstract static class SingleStaticInitializer {
    static {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }
  }

  public abstract static class StaticInitializer {
    static {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }

    StaticInitializer() {
    }

    StaticInitializer(final String s) {
      System.getenv(s);
    }
  }

  public static class TwoAnnotatedFields {
    @External @Deprecated private static final String firstField = null;
    @Deprecated final String secondField = null;
  }

  public static class TwoFields {
    static final String firstField = null;
    private final String secondField = null;

    public String getSecondField() {
      return secondField;
    }
  }

  public abstract static class TwoMethods {
    public static String secondMethod() {
      return null;
    }

    abstract String firstMethod(int a, int b);
  }

  public static class VoidMethod {
    public final void voidMethod() {
      voidMethod();
    }
  }

  @interface Annotation {
    int f();
  }

  enum Enum {
    ;
    public void myMethod() {
      myMethod();
    }
  }

  interface Interface {
    void method0();
  }

  static class LargeClass implements Serializable, Cloneable, Interface {
    private static final long serialVersionUID = 1L;
    private static int field1;
    static Random field4 = new Random();
    static {
      field4 = new Random(field4.nextLong());
      for (int ¢ = 1; ¢ < 10; ++¢)
        field4.nextDouble();
    }
    static Random field5 = new Random(field4.nextLong() + method3());
    static {
      for (int ¢ = 1; ¢ < 20; ++¢)
        field5.nextDouble();
    }
    static Random field6 = new Random(field5.nextLong());
    static {
      for (int ¢ = 1; ¢ < 20; ++¢)
        field4 = new Random(field6.nextLong());
    }

    static int method3() {
      return field5.nextInt();
    }

    static int method4() {
      return field5.nextInt();
    }

    static int method5() {
      return field5.nextInt();
    }

    static int method6() {
      return field5.nextInt();
    }

    static int method7() {
      return field6.nextInt();
    }

    private final int field2;
    private final int field3;

    public LargeClass() {
      this(12);
    }

    public LargeClass(final byte a) {
      this(12 + a);
    }

    public LargeClass(final double d) {
      field1 = 1;
      field2 = (int) d;
      field3 = field2 + 3;
    }

    public LargeClass(final int field1, final int field2, final int field3) {
      LargeClass.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    LargeClass(final int a) {
      field1 = field4.nextInt() + a;
      field2 = field5.nextInt() + field1;
      field3 = field6.nextInt() + field2;
    }

    @Override public void method0() {
      method1();
    }

    int method1() {
      return method2(11);
    }

    private int method2(final int ¢) {
      return ¢ * ¢ + field3;
    }
  }
}
