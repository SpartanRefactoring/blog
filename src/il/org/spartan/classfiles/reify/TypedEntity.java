package il.org.spartan.classfiles.reify;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spatan.iteration.*;

public class TypedEntity extends ConstantPoolEntity {
  @Nullable public static TypeInfo decode(@NotNull final String descriptor) {
    return decodeSingleType(Iterables.make(descriptor.toCharArray()).iterator());
  }

  static TypeInfo[] decodeArguments(@NotNull final CharIterator rest) {
    @NotNull final List<TypeInfo> $ = new ArrayList<>();
    for (char first;;)
      switch (first = rest.next()) {
        case ')':
          return $.toArray(new TypeInfo[$.size()]);
        default:
          $.add(decodeSingleType(first, rest));
      }
  }

  private static String decodeReferenceType(@NotNull final CharIterator i) {
    @NotNull final StringBuilder $ = new StringBuilder();
    for (char ¢; i.hasNext();)
      switch (¢ = i.next()) {
        case ';':
          return $ + "";
        case '/':
          $.append('.');
          break;
        default:
          $.append(¢);
          break;
      }
    return null;
  }

  private static TypeInfo decodeSingleType(final char first, @NotNull final CharIterator rest) {
    switch (first) {
      case 'B':
        return TypeInfo.makePrimitiveType("byte");
      case 'C':
        return TypeInfo.makePrimitiveType("char");
      case 'D':
        return TypeInfo.makePrimitiveType("double");
      case 'F':
        return TypeInfo.makePrimitiveType("float");
      case 'I':
        return TypeInfo.makePrimitiveType("int");
      case 'J':
        return TypeInfo.makePrimitiveType("long");
      case 'S':
        return TypeInfo.makePrimitiveType("short");
      case 'V':
        return TypeInfo.makePrimitiveType("void");
      case 'Z':
        return TypeInfo.makePrimitiveType("boolean");
      case '(':
        @NotNull final TypeInfo[] $ = decodeArguments(rest);
        return TypeInfo.makeMethodType(decodeSingleType(rest), $);
      case 'L':
        return TypeInfo.makeReferenceType(decodeReferenceType(rest));
      case '[':
        return TypeInfo.makeArrayOf(decodeSingleType(rest));
      default:
        return null;
    }
  }

  @Nullable private static TypeInfo decodeSingleType(@NotNull final CharIterator ¢) {
    return decodeSingleType(¢.next(), ¢);
  }

  public final TypeInfo type;
  public final String descriptor;

  public TypedEntity(final ConstantPool constantPool, final int flags, final String name, @NotNull final String descriptor,
      final AttributeInfo[] attributes) {
    this(constantPool, flags, name, decode(descriptor), descriptor, attributes);
  }

  public TypedEntity(final ConstantPool constantPool, final int flags, final String name, final TypeInfo type, final String descriptor,
      final AttributeInfo[] attributes) {
    super(constantPool, flags, name, attributes);
    this.descriptor = descriptor;
    this.type = type;
  }
}