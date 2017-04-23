package il.org.spartan.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.java.Token.*;

import java.io.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method")
//
public class TokenizerTest {
  @Nullable static Token getToken(@NotNull final String s, final int i) throws IOException {
    @NotNull final RawTokenizer $ = new RawTokenizer(new StringReader(s));
    for (int ¢ = 0; ¢ < i - 1; ++¢)
      $.next();
    return $.next();
  }

  @NotNull static String getTokenText(@NotNull final String s, final int i) throws IOException {
    @NotNull final RawTokenizer $ = new RawTokenizer(new StringReader(s));
    for (int ¢ = 0; ¢ < i; ++¢)
      $.next();
    return $.text();
  }

  @Nullable private static Token firstToken(@NotNull final String ¢) throws IOException {
    return getToken(¢, 1);
  }

  @NotNull private static String firstTokenText(@NotNull final String ¢) throws IOException {
    return getTokenText(¢, 1);
  }

  @Nullable private static Token secondToken(@NotNull final String ¢) throws IOException {
    return getToken(¢, 2);
  }

  @NotNull private static String secondTokenText(@NotNull final String ¢) throws IOException {
    return getTokenText(¢, 2);
  }

  final StringTokenizer t = new StringTokenizer("");

  @Test public void annotation() throws IOException {
    @NotNull final String text = "@interfac";
    azzert.that(firstToken(text), is(ANNOTATION));
    azzert.that(firstTokenText(text), is(text));
    azzert.that(firstToken("@interface__"), is(ANNOTATION));
    azzert.that(firstToken("@__interface__"), is(ANNOTATION));
  }

  @Test public void at_intreface() throws IOException {
    @NotNull final String text = "@interface";
    azzert.that(firstToken(text), is(AT_INTERFACE));
    azzert.that(firstTokenText(text), is(text));
  }

  @Test public void block_comment_empty() throws IOException {
    azzert.that(firstToken("/**/"), is(EMPTY_BLOCK_COMMENT));
  }

  @Test public void block_comment_keyword() throws IOException {
    reset("/* a */ public\n" + "public");
    azzert.that(t.next(), is(BLOCK_COMMENT));
    azzert.that(t.next(), is(SPACE));
    azzert.that(t.next(), is(__public));
  }

  @Test public void block_comment_single_line() throws IOException {
    azzert.that(firstToken("/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void block_comment_two_lines() throws IOException {
    @NotNull final String text = "/* first Line \n Second Line */";
    azzert.that(getToken(text, 1), is(PARTIAL_BLOCK_COMMENT));
    azzert.that(getToken(text, 2), is(NL_BLOCK_COMMENT));
    azzert.that(getToken(text, 3), is(BLOCK_COMMENT));
    azzert.that(getTokenText(text, 1), is("/* first Line "));
    azzert.that(getTokenText(text, 2), is("\n"));
    azzert.that(getTokenText(text, 3), is(" Second Line */"));
  }

  @Test public void character_literal_of_quote() throws IOException {
    azzert.that(firstTokenText("'\\''"), is("'\\''"));
  }

  @Test public void character_literal_of_quote_kind() throws IOException {
    azzert.that(firstToken("'\''"), is(CHARACTER_LITERAL));
  }

  @Test public void character_literal_with_double_backslash() throws IOException {
    azzert.that(firstTokenText("'\\\\'"), is("'\\\\'"));
  }

  @Test public void character_literal_with_quote_text() throws IOException {
    azzert.that(firstTokenText("'\\a'"), is("'\\a'"));
  }

  @Test public void character_literal_with_tab() throws IOException {
    azzert.that(firstTokenText("'\t'"), is("'\t'"));
  }

  @Test public void character_literal_with_triple_backslash() throws IOException {
    azzert.that(firstTokenText("'\\\\\\''"), is("'\\\\\\''"));
  }

  @Test public void doc_comment_keyword() throws IOException {
    reset("/**\n" + "* A suite of metrics over Java code.\n" + "* \n" + "* @author Yossi Gil <yogi@cs.technion.ac.il> 21/04/2007\n" + "*/\n"
        + "public");
    azzert.that(t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.that(t.next(), is(NL_DOC_COMMENT));
    azzert.that(t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.that(t.next(), is(NL_DOC_COMMENT));
    azzert.that(t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.that(t.next(), is(NL_DOC_COMMENT));
    azzert.that(t.next(), is(PARTIAL_DOC_COMMENT));
    azzert.that(t.next(), is(NL_DOC_COMMENT));
    azzert.that(t.next(), is(DOC_COMMENT));
    azzert.that(t.next(), is(NL));
    azzert.that(t.next(), is(__public));
  }

  @Test public void doc_comment_single_line() throws IOException {
    azzert.that(firstToken("/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void doc_comment_two_lines() throws IOException {
    @NotNull final String text = "/** first Line \n Second Line */";
    azzert.that(getToken(text, 1), is(PARTIAL_DOC_COMMENT));
    azzert.that(getToken(text, 2), is(NL_DOC_COMMENT));
    azzert.that(getToken(text, 3), is(DOC_COMMENT));
    azzert.that(getTokenText(text, 1), is("/** first Line "));
    azzert.that(getTokenText(text, 2), is("\n"));
    azzert.that(getTokenText(text, 3), is(" Second Line */"));
  }

  @Test public void empty_character_literal() throws IOException {
    azzert.that(firstTokenText("''"), is("''"));
  }

  @Test public void empty_string_empty_string() throws IOException {
    @NotNull final String text = "\"\"\"\"";
    azzert.that(getToken(text, 1), is(STRING_LITERAL));
    azzert.that(getToken(text, 2), is(STRING_LITERAL));
    azzert.that(getTokenText(text, 1), is("\"\""));
    azzert.that(getTokenText(text, 2), is("\"\""));
  }

  @Test public void empty_string_id() throws IOException {
    @NotNull final String text = "\"\"abcd";
    azzert.that(getToken(text, 1), is(STRING_LITERAL));
    azzert.that(getToken(text, 2), is(IDENTIFIER));
    azzert.that(getTokenText(text, 1), is("\"\""));
    azzert.that(getTokenText(text, 2), is("abcd"));
  }

  @Test public void empty_string_literal() throws IOException {
    azzert.that(firstTokenText("''"), is("''"));
  }

  @Test public void eof() throws IOException {
    azzert.that(firstToken(""), is(EOF));
  }

  @Test public void eof_terminated_character_literal() throws IOException {
    azzert.that(firstToken("'m"), is(UNTERMINATED_CHARACTER_LITERAL));
  }

  @Test public void eof_terminated_string_literal() throws IOException {
    azzert.that(firstToken("\"m"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void eof_terminated_string_literal_empty() throws IOException {
    azzert.that(firstToken("\""), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void eof_terminated_string_literal_text() throws IOException {
    azzert.that(firstToken("\"m"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void id() throws IOException {
    azzert.that(firstToken("m"), is(IDENTIFIER));
  }

  @Test public void id_space() throws IOException {
    azzert.that(firstToken("m "), is(IDENTIFIER));
  }

  @Test public void id_space_id() throws IOException {
    @NotNull final String text = "id1 id2";
    azzert.that(getToken(text, 1), is(IDENTIFIER));
    azzert.that(getToken(text, 2), is(SPACE));
    azzert.that(getToken(text, 3), is(IDENTIFIER));
    azzert.that(getTokenText(text, 1), is("id1"));
    azzert.that(getTokenText(text, 2), is(" "));
    azzert.that(getTokenText(text, 3), is("id2"));
  }

  @Test public void line_comment() throws IOException {
    azzert.that(firstToken("// Comment\n"), is(LINE_COMMENT));
  }

  @Test public void line_comment_eof() throws IOException {
    azzert.that(firstToken("// Comment"), is(LINE_COMMENT));
  }

  @Test public void long_character_literal() throws IOException {
    azzert.that(firstToken("'masfasdfasdfas'"), is(CHARACTER_LITERAL));
  }

  @Test public void long_character_literal_text() throws IOException {
    azzert.that(firstTokenText("'masfasdfasdfas'"), is("'masfasdfasdfas'"));
  }

  @Test public void long_string_literal() throws IOException {
    azzert.that(firstToken("\"masfasdfasdfas\""), is(STRING_LITERAL));
  }

  @Test public void long_string_literal_text() throws IOException {
    azzert.that(firstTokenText("'masfasdfasdfas'"), is("'masfasdfasdfas'"));
  }

  @Test public void nl_string_space_id_popen_integer() throws IOException {
    @NotNull final String text = "\n\"\" abcd(12";
    azzert.that(getToken(text, 1), is(NL));
    azzert.that(getToken(text, 2), is(STRING_LITERAL));
    azzert.that(getToken(text, 3), is(SPACE));
    azzert.that(getToken(text, 4), is(IDENTIFIER));
  }

  /* escaped \ */
  @Test public void no_esc_block_comment() throws IOException {
    azzert.that(firstToken("/* block Comment \\*/"), is(BLOCK_COMMENT));
  }

  /* /* No nested comment */
  @Test public void no_nested_block_comment() throws IOException {
    azzert.that(firstToken("/*/* block Comment */"), is(BLOCK_COMMENT));
  }

  @Test public void no_nested_block_comment_text() throws IOException {
    azzert.that(firstTokenText("/*/* block Comment */"), is("/*/* block Comment */"));
  }

  @Test public void no_nested_doc_comment_text_firstToken() throws IOException {
    azzert.that(firstTokenText("/**/** doc Comment */"), is("/**/"));
  }

  @Test public void no_nested_doc_comment_text_secondToken() throws IOException {
    azzert.that(secondTokenText("/**//** doc Comment */"), is("/** doc Comment */"));
  }

  @Test public void no_nested_doc_commentFirstToken() throws IOException {
    azzert.that(firstToken("/**/** doc Comment */"), is(EMPTY_BLOCK_COMMENT));
  }

  @Test public void no_nested_doc_commentSecondToken() throws IOException {
    azzert.that(secondToken("/**/** doc Comment */"), is(MULT));
  }

  // ===================================
  @Test public void one_char_string_literal() throws IOException {
    azzert.that(firstToken("\"m\""), is(STRING_LITERAL));
  }

  @Test public void short_doc_comment_keyword() throws IOException {
    reset("/** a */ public\n" + "public");
    azzert.that(t.next(), is(DOC_COMMENT));
    azzert.that(t.next(), is(SPACE));
    azzert.that(t.next(), is(__public));
  }

  @Test public void simple_character_literal() throws IOException {
    azzert.that(firstToken("'m'"), is(CHARACTER_LITERAL));
  }

  @Test public void simple_character_literal_text() throws IOException {
    azzert.that(firstTokenText("'m'"), is("'m'"));
  }

  @Test public void simple_string_literal_text() throws IOException {
    azzert.that(firstTokenText("\"abc m'\""), is("\"abc m'\""));
  }

  @Test public void something_after_emtpy_comment() throws IOException {
    azzert.that(secondToken("/**/something"), is(IDENTIFIER));
  }

  @Test public void space_id_space() throws IOException {
    azzert.that(getToken(" m ", 2), is(IDENTIFIER));
  }

  @Test public void string_id_string() throws IOException {
    @NotNull final String text = "\"str1\"xid\"str2\"";
    azzert.that(getToken(text, 1), is(STRING_LITERAL));
    azzert.that(getToken(text, 2), is(IDENTIFIER));
    azzert.that(getToken(text, 3), is(STRING_LITERAL));
    azzert.that(getTokenText(text, 1), is("\"str1\""));
    azzert.that(getTokenText(text, 2), is("xid"));
    azzert.that(getTokenText(text, 3), is("\"str2\""));
  }

  @Test public void string_keyword() throws IOException {
    @NotNull final String text = "\" \"public";
    reset(text);
    azzert.that(t.next(), is(STRING_LITERAL));
    azzert.that(t.text(), is("\" \""));
    azzert.that(t.next(), is(__public));
    azzert.that(t.text(), is("public"));
  }

  @Test public void string_literal_of_quote() throws IOException {
    azzert.that(firstTokenText("'\\''"), is("'\\''"));
  }

  @Test public void string_literal_of_quote_kind() throws IOException {
    azzert.that(firstToken("\"\\\"\""), is(STRING_LITERAL));
  }

  @Test public void string_literal_with_double_backslash() throws IOException {
    azzert.that(firstTokenText("'\\\\'"), is("'\\\\'"));
  }

  @Test public void string_literal_with_quote_text() throws IOException {
    azzert.that(firstTokenText("\"\\\"\""), is("\"\\\"\""));
  }

  @Test public void string_literal_with_tab() throws IOException {
    azzert.that(firstTokenText("'\t'"), is("'\t'"));
  }

  @Test public void string_literal_with_triple_backslash() throws IOException {
    azzert.that(firstTokenText("'\\\\\\''"), is("'\\\\\\''"));
  }

  @Test public void string_space_keyword() throws IOException {
    reset("\" \" public\n");
    azzert.that(t.next(), is(STRING_LITERAL));
    azzert.that(t.next(), is(SPACE));
    azzert.that(t.next(), is(__public));
  }

  @Test public void unterminated_block_comment() throws IOException {
    azzert.that(firstToken("/*/* block Comment"), is(UNTERMINATED_BLOCK_COMMENT));
  }

  @Test public void unterminated_block_comment_text() throws IOException {
    azzert.that(firstTokenText("/*/* block Comment"), is("/*/* block Comment"));
  }

  @Test public void unterminated_character_literal() throws IOException {
    azzert.that(UNTERMINATED_CHARACTER_LITERAL, is(firstToken("'m\n")));
  }

  @Test public void unterminated_doc_comment() throws IOException {
    azzert.that(firstToken("/** doc Comment"), is(UNTERMINATED_DOC_COMMENT));
  }

  @Test public void unterminated_doc_comment_text() throws IOException {
    azzert.that(firstTokenText("/*/* block Comment"), is("/*/* block Comment"));
  }

  @Test public void unterminated_string_literal() throws IOException {
    azzert.that(firstToken("\"masfasdfasdf\n"), is(UNTERMINATED_STRING_LITERAL));
  }

  @Test public void unterminated_string_literal_text() throws IOException {
    azzert.that(firstTokenText("\"mabc\n"), is("\"mabc"));
  }

  void reset(@NotNull final String text) {
    t.reset(text);
  }

  public static class StringTokenizer {
    @NotNull private final RawTokenizer inner;

    public StringTokenizer(@NotNull final String text) {
      inner = new RawTokenizer(new StringReader(text));
    }

    @Nullable public Token next() throws IOException {
      return inner.next();
    }

    public void reset(@NotNull final String text) {
      inner.yyreset(new StringReader(text));
    }

    @NotNull public String text() {
      return inner.text();
    }
  }
}
