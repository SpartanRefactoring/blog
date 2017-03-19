package il.org.spartan.java;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class SignatureFilter extends CodeOnlyFilter {
  private static boolean usefulKeyword(@NotNull final Token ¢) {
    switch (¢) {
      default:
        return false;
      case AT_INTERFACE:
      case __abstract:
      case __class:
      case __enum:
      case __interface:
      case __new:
        return true;
    }
  }

  /** @param ¢ a token */
  private static boolean usefulPucntuation(@NotNull final Token ¢) {
    switch (¢) {
      default:
        return false;
      case EQ:
      case LBRACE:
      case LBRACK:
      case LPAREN:
      case RBRACE:
      case RBRACK:
      case RPAREN:
      case SEMICOLON:
        return true;
    }
  }

  @Override protected boolean ok(@NotNull final Token ¢) {
    switch (¢.kind) {
      case KEYWORD:
        return usefulKeyword(¢);
      case PUNCTUATION:
        return usefulPucntuation(¢);
      default:
        return false;
    }
  }
}
