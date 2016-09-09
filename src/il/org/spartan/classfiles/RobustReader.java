/**
 *
 */
package il.org.spartan.classfiles;

import java.io.*;
import java.util.*;

/** @author Yossi Gil
 * @since 11 November 2011 */
public class RobustReader {
  protected static DataInputStream asDataInputStream(final InputStream is) {
    return is == null ? null : new DataInputStream(is);
  }

  protected static FileInputStream asFileInputStream(final File f) {
    if (f == null)
      return null;
    try {
      return new FileInputStream(f);
    } catch (final FileNotFoundException e) {
      return null;
    }
  }

  private final List<Exception> errors = new ArrayList<>();
  /** The stream from which the remainder of this class file can be read. */
  private final DataInputStream inner;

  public RobustReader(final DataInputStream inner) {
    this.inner = inner;
  }

  /** Instantiate {@link RobustReader} from a given {@link File}
   * @param f an arbitrary file object */
  public RobustReader(final File f) {
    this(asFileInputStream(f));
  }

  /** Instantiate {@link RobustReader}.
   * @param is */
  public RobustReader(final InputStream is) {
    this(asDataInputStream(is));
  }

  /** Instantiate {@link RobustReader}.
   * @param fileName an arbitrary file name */
  public RobustReader(final String fileName) {
    this(new File(fileName));
  }

  public final void close() {
    if (inner != null)
      try {
        inner.close();
      } catch (final IOException e) {
        recordError(e);
      }
  }

  public final boolean hasErrors() {
    return errors.size() != 0;
  }

  public final double readDouble() {
    if (inner == null)
      return 0;
    try {
      return inner.readDouble();
    } catch (final IOException e) {
      return recordError(e);
    }
  }

  public final float readFloat() {
    if (inner == null)
      return 0;
    try {
      return inner.readFloat();
    } catch (final IOException e) {
      return recordError(e);
    }
  }

  public final int readInt() {
    if (inner == null)
      return 0;
    try {
      return inner.readInt();
    } catch (final IOException e) {
      return recordError(e);
    }
  }

  public final long readLong() {
    if (inner == null)
      return 0;
    try {
      return inner.readLong();
    } catch (final IOException e) {
      return recordError(e);
    }
  }

  public final int readUnsignedByte() {
    if (inner == null)
      return 0;
    try {
      return inner.readUnsignedByte();
    } catch (final IOException e) {
      return recordError(e);
    }
  }

  public final int readUnsignedShort() {
    if (inner == null)
      return 0;
    try {
      return inner.readUnsignedShort();
    } catch (final IOException e) {
      return recordError(e);
    }
  }

  public final String readUTF() {
    if (inner == null)
      return "";
    try {
      return inner.readUTF();
    } catch (final IOException e) {
      recordError(e);
      return null;
    }
  }

  public final void skipBytes(final int n) {
    for (int i = 0; i < n; i++)
      readUnsignedByte();
  }

  protected final byte[] readBytes(final byte[] $) {
    if (inner == null)
      return new byte[0];
    for (int left = $.length; left > 0;)
      left -= readBytes($, $.length - left, left);
    return $;
  }

  /** @param ioException */
  protected int recordError(final Exception e) {
    errors.add(e);
    return 0;
  }

  int readBytes(final byte[] b, final int offset, final int howMany) {
    try {
      return inner.read(b, offset, howMany);
    } catch (final IOException e) {
      recordError(e);
      return howMany;
    }
  }
}
