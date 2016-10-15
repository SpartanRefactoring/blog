/**
 *
 */
package il.org.spartan.java;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class SignatureAnalyzerTest {
  @Test public void createFile() {
    assert null != SignatureAnalyzer.ofFile("Data/file");
  }

  @Test public void createFileStaticType() {
    final SignatureAnalyzer __ = SignatureAnalyzer.ofFile("Data/file");
    azzert.that(__.getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createFileType() {
    azzert.that(SignatureAnalyzer.ofFile("Data/file").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createReaderStaticType() {
    final SignatureAnalyzer __ = SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n"));
    azzert.that(__.getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createReaderType() {
    azzert.that(SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createString() {
    assert null != SignatureAnalyzer.ofString("Hello, World!\n");
  }

  @Test public void createStringStaticType() {
    final SignatureAnalyzer __ = SignatureAnalyzer.ofString("Hello, World!\n");
    azzert.that(__.getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createStringType() {
    azzert.that(SignatureAnalyzer.ofString("Hello, World!\n").getClass(), is(SignatureAnalyzer.class));
  }
}
