package il.org.spartan.bench;

import il.org.spartan.utils.*;
import nano.ly.*;

public class AbstractRunRecord {
  protected int runs;
  protected long grossTime;
  protected long netTime;

  public long grossTime() {
    return grossTime;
  }

  public long netTime() {
    return netTime;
  }

  public int runs() {
    return runs;
  }

  @Override public String toString() {
    return String.format("runs=%d, netTime=%s, grossTime=%s, efficacy=%s", //
        Box.it(runs), //
        Unit.formatNanoseconds(netTime), //
        Unit.formatNanoseconds(grossTime), //
        Unit.formatRelative(netTime, grossTime));
  }
}
