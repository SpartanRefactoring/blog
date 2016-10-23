/**
 *
 */
package il.org.spartan.classfiles.reify;

import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since 26 November 2011 */
public class CodeEntity {
  public int maxStack;
  public int maxLocals;
  public final byte[] codes;
  @NotNull
  public final SimplifiedCode simplifiedCode;

  public CodeEntity(final int maxStack, final int maxLocals, final byte[] codes) {
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
    this.codes = codes;
    simplifiedCode = new SimplifiedCode(codes);
  }

  public int cyclomaticComplexity() {
    return simplifiedCode.cyclomaticComplexity();
  }

  public int instructionsCount() {
    return simplifiedCode.instructionsCount();
  }

  public int throwCount() {
    return simplifiedCode.throwCount();
  }
}
