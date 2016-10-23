/**
 *
 */
package il.org.spartan.classfiles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

/** @author Yossi Gil
 * @since 11 November 2011 */
public class RobustReader {
  @Nullable
  protected static DataInputStream asDataInputStream(@Nullable final InputStream ¢) {
    return ¢ == null ? null : new DataInputStream(¢);
  }

  @Nullable
  protected static FileInputStream asFileInputStream(@Nullable final File f) {
    if (f == null)
      return null;
    try {
      return new FileInputStream(f);
    } catch (@NotNull final FileNotFoundException e) {
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
  public RobustReader(@NotNull final String fileName) {
    this(new File(fileName));
  }

  public final void close() {
    if (inner != null)
      try {
        inner.close();
      } catch (@NotNull final IOException e) {
        recordError(e);
      }
  }

  public final boolean hasErrors() {
    return !errors.isEmpty();
  }

  public final double readDouble() {
    if (inner == null)
      return 0;
    try {
      return inner.readDouble();
    } catch (@NotNull final IOException e) {
      return recordError(e);
    }
  }

  public final float readFloat() {
    if (inner == null)
      return 0;
    try {
      return inner.readFloat();
    } catch (@NotNull final IOException e) {
      return recordError(e);
    }
  }

  public final int readInt() {
    if (inner == null)
      return 0;
    try {
      return inner.readInt();
    } catch (@NotNull final IOException e) {
      return recordError(e);
    }
  }

  public final long readLong() {
    if (inner == null)
      return 0;
    try {
      return inner.readLong();
    } catch (@NotNull final IOException e) {
      return recordError(e);
    }
  }

  public final int readUnsignedByte() {
    if (inner == null)
      return 0;
    try {
      return inner.readUnsignedByte();
    } catch (@NotNull final IOException e) {
      return recordError(e);
    }
  }

  public final int readUnsignedShort() {
    if (inner == null)
      return 0;
    try {
      return inner.readUnsignedShort();
    } catch (@NotNull final IOException e) {
      return recordError(e);
    }
  }

  public final String readUTF() {
    if (inner == null)
      return "";
    try {
      return inner.readUTF();
    } catch (@NotNull final IOException e) {
      recordError(e);
      return null;
    }
  }

  public final void skipBytes(final int i) {
    for (int ¢ = 0; ¢ < i; ++¢)
      readUnsignedByte();
  }

  @NotNull
  protected final byte[] readBytes(@NotNull final byte[] $) {
    if (inner == null)
      return new byte[0];
    for (int left = $.length; left > 0;)
      left -= readBytes($, $.length - left, left);
    return $;
  }

  /** @param ioException */
  protected int recordError(final Exception ¢) {
    errors.add(¢);
    return 0;
  }

  int readBytes(@NotNull final byte[] bs, final int offset, final int howMany) {
    try {
      return inner.read(bs, offset, howMany);
    } catch (@NotNull final IOException e) {
      recordError(e);
      return howMany;
    }
  }
}
