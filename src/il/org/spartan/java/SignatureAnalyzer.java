/**
 *
 */
package il.org.spartan.java;

import java.io.*;

import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since 19 November 2011 */
public class SignatureAnalyzer {
  @NotNull
  public static SignatureAnalyzer ofFile(final String fileName) {
    ___.______unused(fileName);
    return new SignatureAnalyzer();
  }

  @NotNull
  public static SignatureAnalyzer ofReader(final StringReader ¢) {
    ___.______unused(¢);
    return new SignatureAnalyzer();
  }

  @NotNull
  public static SignatureAnalyzer ofString(final String ¢) {
    ___.______unused(¢);
    return new SignatureAnalyzer();
  }
}
