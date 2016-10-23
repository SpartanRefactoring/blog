package il.org.spartan.bench;

import org.jetbrains.annotations.*;

import il.org.spartan.bench.operations.*;

public class Bencher extends LogBook.Mutable {
  private static final long serialVersionUID = 1L;
  private transient Operation after;

  public Bencher(final Object initiator) {
    super(initiator);
  }

  public void afterEachGo(final Operation ¢) {
    after = ¢;
  }

  @NotNull @Override public LogBook clear() {
    super.clear();
    current.clear();
    dotter.clear();
    return this;
  }

  public void go(final Bencheon ¢) {
    BenchingPolicy.go(this, ¢);
    BenchingPolicy.after(after);
  }

  public void go(final long size, final NamedOperation o) {
    BenchingPolicy.go(this, size, o);
    BenchingPolicy.after(after);
  }

  public void go(final String name, final long l, final Operation o) {
    BenchingPolicy.go(this, name, l, o);
    BenchingPolicy.after(after);
  }
}