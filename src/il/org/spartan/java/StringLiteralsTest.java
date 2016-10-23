package il.org.spartan.java;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class StringLiteralsTest {
  @Nullable static Token toToken(@NotNull final String s) {
    try {
      final RawTokenizer J = new RawTokenizer(new StringReader(s));
      final Token $ = J.next();
      azzert.that(J.next(), is(Token.EOF));
      return $;
    } catch (@NotNull final IOException E) {
      return Token.EOF;
    }
  }

  @Test public void test_simple_literal() {
    azzert.that(toToken("\"abcd\""), is(Token.STRING_LITERAL));
  }
}
