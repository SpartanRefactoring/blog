package il.org.spartan.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.java.Token.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method")
//
public class TokenizerTest {
  static Token getToken(final String s, final int i) throws IOException {
    final RawTokenizer j = new RawTokenizer(new StringReader(s));
    for (int ¢ = 0; ¢ < i - 1; ++¢)
      j.next();
    return j.next();
  }

  static String getTokenText(final String s, final int i) throws IOException {
    final RawTokenizer j = new RawTokenizer(new StringReader(s));
    for (int ¢ = 0; ¢ < i; ++¢)
      j.next();
    return j.text();
  }

  private static Token firstToken(final String ¢) throws IOException {
    return getToken(¢, 1);
  }

  private static String firstTokenText(final String ¢) throws IOException {
    return getTokenText(¢, 1);
  }

  private static Token secondToken(final String ¢) throws IOException {
    return getToken(¢, 2);
  }

  private static String secondTokenText(final String ¢) throws IOException {
    return getTokenText(¢, 2);
  }

  final StringTokenizer t = new StringTokenizer("");

  @Test public void annotation() throws IOException {
    final String text = "@interfac";
    azzert.assertThat("", firstToken(text), is(ANNOTATION));
    azzert.assertThat("", firstTokenText(text), is(text));
    azzert.assertThat("", firstToken("@interface__"), is(ANNOTATION));
    azzert.assertThat("", firstToken("@__interface__"), is(ANNOTATION));
  }

  @Test public void at_intreface() throws IOException {
    final String text = "@interface";
    azzert.assertThat("", firstToken(text), is(AT_INTERFACE));
    azzert.assertThat("", firstTokenText(text), is(text));
  }

  @Test public void block_comment_empty() throws IOException {
    azzert.assertThat("", firstToken("/**/"), is(EMPTY_BLOCK_COMMENT));
  }

  @Test public void block_comment_keyword() throws IOException {
    final String text = "/* a */ public\n" + //
        "public";
    reset(text);
    azzert.assertThat("", t.next(), is(BLOCK_COMMENT));
    azzert.assertThat("", t.next(), is(SPACE));
    azzert.assertThat("", t.next(), is(__public));
  }

  @Test public void block_comment_single_line() throws IOException {
    azzert.assertThat("", firstToken("/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void block_comment_two_lines() throws IOException {
    final String text = "/* first Line \n Second Line */";
    azzert.assertThat("", getToken(text, 1), is(PARTIAL_BLOCK_COMMENT));
    azzert.assertThat("", getToken(text, 2), is(NL_BLOCK_COMMENT));
    azzert.assertThat("", getToken(text, 3), is(BLOCK_COMMENT));
    azzert.assertThat("", getTokenText(text, 1), is("/* first Line "));
    azzert.assertThat("", getTokenText(text, 2), is("\n"));
    azzert.assertThat("", getTokenText(text, 3), is(" Second Line */"));
  }

  @Test public void character_literal_of_quote() throws IOException {
    azzert.assertThat("", firstTokenText("'\\''"), is("'\\''"));
  }

  @Test public void character_literal_of_quote_kind() throws IOException {
    azzert.assertThat("", firstToken("'\''"), is(CHARACTER_LITERAL));
  }

  @Test public void character_literal_with_double_backslash() throws IOException {
    azzert.assertThat("", firstTokenText("'\\\\'"), is("'\\\\'"));
  }

  @Test public void character_literal_with_quote_text() throws IOException {
    azzert.assertThat("", firstTokenText("'\\a'"), is("'\\a'"));
  }

  @Test public void character_literal_with_tab() throws IOException {
    azzert.assertThat("", firstTokenText("'\t'"), is("'\t'"));
  }

  @Test public void character_literal_with_triple_backslash() throws IOException {
    azzert.assertThat("", firstTokenText("'\\\\\\''"), is("'\\\\\\''"));
  }

  @Test public void doc_comment_keyword() throws IOException {
    final String text = "/**\n" + //
        "* A suite of metrics over Java code.\n" + //
        "* \n" + //
        "* @author Yossi Gil <yogi@cs.technion.ac.il> 21/04/2007\n" + //
        "*/\n" + //
        "public";
    reset(text);
    azzert.assertThat("", t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(NL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(NL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(NL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(NL_DOC_COMMENT));
    azzert.assertThat("", t.next(), is(DOC_COMMENT));
    azzert.assertThat("", t.next(), is(NL));
    azzert.assertThat("", t.next(), is(__public));
  }

  @Test public void doc_comment_single_line() throws IOException {
    azzert.assertThat("", firstToken("/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void doc_comment_two_lines() throws IOException {
    final String text = "/** first Line \n Second Line */";
    azzert.assertThat("", getToken(text, 1), is(PARTIAL_DOC_COMMENT));
    azzert.assertThat("", getToken(text, 2), is(NL_DOC_COMMENT));
    azzert.assertThat("", getToken(text, 3), is(DOC_COMMENT));
    azzert.assertThat("", getTokenText(text, 1), is("/** first Line "));
    azzert.assertThat("", getTokenText(text, 2), is("\n"));
    azzert.assertThat("", getTokenText(text, 3), is(" Second Line */"));
  }

  @Test public void empty_character_literal() throws IOException {
    azzert.assertThat("", firstTokenText("''"), is("''"));
  }

  @Test public void empty_string_empty_string() throws IOException {
    final String text = "\"\"\"\"";
    azzert.assertThat("", getToken(text, 1), is(STRING_LITERAL));
    azzert.assertThat("", getToken(text, 2), is(STRING_LITERAL));
    azzert.assertThat("", getTokenText(text, 1), is("\"\""));
    azzert.assertThat("", getTokenText(text, 2), is("\"\""));
  }

  @Test public void empty_string_id() throws IOException {
    final String text = "\"\"abcd";
    azzert.assertThat("", getToken(text, 1), is(STRING_LITERAL));
    azzert.assertThat("", getToken(text, 2), is(IDENTIFIER));
    azzert.assertThat("", getTokenText(text, 1), is("\"\""));
    azzert.assertThat("", getTokenText(text, 2), is("abcd"));
  }

  @Test public void empty_string_literal() throws IOException {
    azzert.assertThat("", firstTokenText("''"), is("''"));
  }

  @Test public void eof() throws IOException {
    azzert.assertThat("", firstToken(""), is(EOF));
  }

  @Test public void eof_terminated_character_literal() throws IOException {
    azzert.assertThat("", firstToken("'m"), is(UNTERMINATED_CHARACTER_LITERAL));
  }

  @Test public void eof_terminated_string_literal() throws IOException {
    azzert.assertThat("", firstToken("\"m"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void eof_terminated_string_literal_empty() throws IOException {
    azzert.assertThat("", firstToken("\""), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void eof_terminated_string_literal_text() throws IOException {
    azzert.assertThat("", firstToken("\"m"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void id() throws IOException {
    azzert.assertThat("", firstToken("m"), is(IDENTIFIER));
  }

  @Test public void id_space() throws IOException {
    azzert.assertThat("", firstToken("m "), is(IDENTIFIER));
  }

  @Test public void id_space_id() throws IOException {
    final String text = "id1 id2";
    azzert.assertThat("", getToken(text, 1), is(IDENTIFIER));
    azzert.assertThat("", getToken(text, 2), is(SPACE));
    azzert.assertThat("", getToken(text, 3), is(IDENTIFIER));
    azzert.assertThat("", getTokenText(text, 1), is("id1"));
    azzert.assertThat("", getTokenText(text, 2), is(" "));
    azzert.assertThat("", getTokenText(text, 3), is("id2"));
  }

  @Test public void line_comment() throws IOException {
    azzert.assertThat("", firstToken("// Comment\n"), is(LINE_COMMENT));
  }

  @Test public void line_comment_eof() throws IOException {
    azzert.assertThat("", firstToken("// Comment"), is(LINE_COMMENT));
  }

  @Test public void long_character_literal() throws IOException {
    azzert.assertThat("", firstToken("'masfasdfasdfas'"), is(CHARACTER_LITERAL));
  }

  @Test public void long_character_literal_text() throws IOException {
    azzert.assertThat("", firstTokenText("'masfasdfasdfas'"), is("'masfasdfasdfas'"));
  }

  @Test public void long_string_literal() throws IOException {
    azzert.assertThat("", firstToken("\"masfasdfasdfas\""), is(STRING_LITERAL));
  }

  @Test public void long_string_literal_text() throws IOException {
    azzert.assertThat("", firstTokenText("'masfasdfasdfas'"), is("'masfasdfasdfas'"));
  }

  @Test public void nl_string_space_id_popen_integer() throws IOException {
    final String text = "\n\"\" abcd(12";
    azzert.assertThat("", getToken(text, 1), is(NL));
    azzert.assertThat("", getToken(text, 2), is(STRING_LITERAL));
    azzert.assertThat("", getToken(text, 3), is(SPACE));
    azzert.assertThat("", getToken(text, 4), is(IDENTIFIER));
  }

  /* escaped \ */
  @Test public void no_esc_block_comment() throws IOException {
    azzert.assertThat("", firstToken("/* block Comment \\*/"), is(BLOCK_COMMENT));
  }

  /* /* No nested comment */
  @Test public void no_nested_block_comment() throws IOException {
    azzert.assertThat("", firstToken("/*/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void no_nested_block_comment_text() throws IOException {
    azzert.assertThat("", firstTokenText("/*/* block Comment */"), is("/*/* block Comment */"));
  }

  @Test public void no_nested_doc_comment_text_firstToken() throws IOException {
    azzert.assertThat("", firstTokenText("/**/** doc Comment */"), is("/**/"));
  }

  @Test public void no_nested_doc_comment_text_secondToken() throws IOException {
    azzert.assertThat("", secondTokenText("/**//** doc Comment */"), is("/** doc Comment */"));
  }

  @Test public void no_nested_doc_commentFirstToken() throws IOException {
    azzert.assertThat("", firstToken("/**/** doc Comment */"), is(EMPTY_BLOCK_COMMENT));
  }

  @Test public void no_nested_doc_commentSecondToken() throws IOException {
    azzert.assertThat("", secondToken("/**/** doc Comment */"), is(MULT));
  }

  // ===================================
  @Test public void one_char_string_literal() throws IOException {
    azzert.assertThat("", firstToken("\"m\""), is(STRING_LITERAL));
  }

  @Test public void short_doc_comment_keyword() throws IOException {
    final String text = "/** a */ public\n" + //
        "public";
    reset(text);
    azzert.assertThat("", t.next(), is(DOC_COMMENT));
    azzert.assertThat("", t.next(), is(SPACE));
    azzert.assertThat("", t.next(), is(__public));
  }

  @Test public void simple_character_literal() throws IOException {
    azzert.assertThat("", firstToken("'m'"), is(CHARACTER_LITERAL));
  }

  @Test public void simple_character_literal_text() throws IOException {
    azzert.assertThat("", firstTokenText("'m'"), is("'m'"));
  }

  @Test public void simple_string_literal_text() throws IOException {
    azzert.assertThat("", firstTokenText("\"abc m'\""), is("\"abc m'\""));
  }

  @Test public void something_after_emtpy_comment() throws IOException {
    azzert.assertThat("", secondToken("/**/something"), is(IDENTIFIER));
  }

  @Test public void space_id_space() throws IOException {
    azzert.assertThat("", getToken(" m ", 2), is(IDENTIFIER));
  }

  @Test public void string_id_string() throws IOException {
    final String text = "\"str1\"xid\"str2\"";
    azzert.assertThat("", getToken(text, 1), is(STRING_LITERAL));
    azzert.assertThat("", getToken(text, 2), is(IDENTIFIER));
    azzert.assertThat("", getToken(text, 3), is(STRING_LITERAL));
    azzert.assertThat("", getTokenText(text, 1), is("\"str1\""));
    azzert.assertThat("", getTokenText(text, 2), is("xid"));
    azzert.assertThat("", getTokenText(text, 3), is("\"str2\""));
  }

  @Test public void string_keyword() throws IOException {
    final String text = "\" \"public";
    reset(text);
    azzert.assertThat("", t.next(), is(STRING_LITERAL));
    azzert.assertThat("", t.text(), is("\" \""));
    azzert.assertThat("", t.next(), is(__public));
    azzert.assertThat("", t.text(), is("public"));
  }

  @Test public void string_literal_of_quote() throws IOException {
    azzert.assertThat("", firstTokenText("'\\''"), is("'\\''"));
  }

  @Test public void string_literal_of_quote_kind() throws IOException {
    azzert.assertThat("", firstToken("\"\\\"\""), is(STRING_LITERAL));
  }

  @Test public void string_literal_with_double_backslash() throws IOException {
    azzert.assertThat("", firstTokenText("'\\\\'"), is("'\\\\'"));
  }

  @Test public void string_literal_with_quote_text() throws IOException {
    azzert.assertThat("", firstTokenText("\"\\\"\""), is("\"\\\"\""));
  }

  @Test public void string_literal_with_tab() throws IOException {
    azzert.assertThat("", firstTokenText("'\t'"), is("'\t'"));
  }

  @Test public void string_literal_with_triple_backslash() throws IOException {
    azzert.assertThat("", firstTokenText("'\\\\\\''"), is("'\\\\\\''"));
  }

  @Test public void string_space_keyword() throws IOException {
    final String text = "\" \" public\n";
    reset(text);
    azzert.assertThat("", t.next(), is(STRING_LITERAL));
    azzert.assertThat("", t.next(), is(SPACE));
    azzert.assertThat("", t.next(), is(__public));
  }

  @Test public void unterminated_block_comment() throws IOException {
    azzert.assertThat("", firstToken("/*/* block Comment"), is(UNTERMINATED_BLOCK_COMMENT));
  }

  @Test public void unterminated_block_comment_text() throws IOException {
    azzert.assertThat("", firstTokenText("/*/* block Comment"), is("/*/* block Comment"));
  }

  @Test public void unterminated_character_literal() throws IOException {
    azzert.assertThat("", UNTERMINATED_CHARACTER_LITERAL, is(firstToken("'m\n")));
  }

  @Test public void unterminated_doc_comment() throws IOException {
    azzert.assertThat("", firstToken("/** doc Comment"), is(UNTERMINATED_DOC_COMMENT));
  }

  @Test public void unterminated_doc_comment_text() throws IOException {
    azzert.assertThat("", firstTokenText("/*/* block Comment"), is("/*/* block Comment"));
  }

  @Test public void unterminated_string_literal() throws IOException {
    azzert.assertThat("", firstToken("\"masfasdfasdf\n"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void unterminated_string_literal_text() throws IOException {
    azzert.assertThat("", firstTokenText("\"mabc\n"), is("\"mabc"));
  }

  void reset(final String text) {
    t.reset(text);
  }

  public static class StringTokenizer {
    private final RawTokenizer inner;

    public StringTokenizer(final String text) {
      inner = new RawTokenizer(new StringReader(text));
    }

    public Token next() throws IOException {
      return inner.next();
    }

    public void reset(final String text) {
      inner.yyreset(new StringReader(text));
    }

    public String text() {
      return inner.text();
    }
  }
}
