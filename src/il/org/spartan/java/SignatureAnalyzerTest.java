/**
 *
 */
package il.org.spartan.java;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class SignatureAnalyzerTest {
  @Test public void createFile() {
    assertNotNull(SignatureAnalyzer.ofFile("Data/file"));
  }

  @Test public void createFileStaticType() {
    final SignatureAnalyzer _ = SignatureAnalyzer.ofFile("Data/file");
    assertEquals(SignatureAnalyzer.class, _.getClass());
  }

  @Test public void createFileType() {
    assertEquals(SignatureAnalyzer.class, SignatureAnalyzer.ofFile("Data/file").getClass());
  }

  @Test public void createReaderStaticType() {
    final SignatureAnalyzer _ = SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n"));
    assertEquals(SignatureAnalyzer.class, _.getClass());
  }

  @Test public void createReaderType() {
    assertEquals(SignatureAnalyzer.class, SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass());
  }

  @Test public void createString() {
    assertNotNull(SignatureAnalyzer.ofString("Hello, World!\n"));
  }

  @Test public void createStringStaticType() {
    final SignatureAnalyzer _ = SignatureAnalyzer.ofString("Hello, World!\n");
    assertEquals(SignatureAnalyzer.class, _.getClass());
  }

  @Test public void createStringType() {
    assertEquals(SignatureAnalyzer.class, SignatureAnalyzer.ofString("Hello, World!\n").getClass());
  }
}