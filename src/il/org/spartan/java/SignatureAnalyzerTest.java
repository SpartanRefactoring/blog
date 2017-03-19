package il.org.spartan.java;

import static il.org.spartan.fapi.azzert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.fapi.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class SignatureAnalyzerTest {
  @Test public void createFile() {
    assert SignatureAnalyzer.ofFile("Data/file") != null;
  }

  @Test public void createFileStaticType() {
    azzert.that(SignatureAnalyzer.ofFile("Data/file").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createFileType() {
    azzert.that(SignatureAnalyzer.ofFile("Data/file").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createReaderStaticType() {
    azzert.that(SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createReaderType() {
    azzert.that(SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createString() {
    assert SignatureAnalyzer.ofString("Hello, World!\n") != null;
  }

  @Test public void createStringStaticType() {
    azzert.that(SignatureAnalyzer.ofString("Hello, World!\n").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createStringType() {
    azzert.that(SignatureAnalyzer.ofString("Hello, World!\n").getClass(), is(SignatureAnalyzer.class));
  }
}
