package org.poach3r.backend

import org.poach3r.Token
import org.poach3r.TokenType
import org.poach3r.TokenType.*
import org.poach3r.errors.ParsingError

class Scanner {
    private var tokens: ArrayList<Token> = arrayListOf()
    private var start: Int = 0
    private var current: Int = 0
    private var line: Int = 1
    private var source: String = ""
    private var isAtEnd: Boolean = false
        get() = current >= source.length
    private var keywords: HashMap<String, TokenType> = hashMapOf(
        "and" to AND,
        "else" to ELSE,
        "false" to FALSE,
        "true" to TRUE,
        "for" to FOR,
        //"foreach" to FOREACH,
        "fun" to FUN,
        "if" to IF,
        "or" to OR,
        //"print" to PRINT,
        "return" to RETURN,
        "var" to VAR,
        "val" to VAL,
        "while" to WHILE,
        "break" to BREAK,
        "class" to CLASS,
        "this" to THIS,
        "super" to SUPER
    )

    fun scanTokens(source: String): List<Token> {
        start = 0
        current = 0
        line = 1
        tokens = arrayListOf()
        this.source = source

        while (!isAtEnd) {
            start = current
            scanToken()
        }

        tokens.add(
            Token(
                type = EOF,
                lexeme = "",
                literal = 0,
                line = line
            )
        )
        return tokens
    }

    private fun scanToken() {
        var c = advance()
        when (c) {
            ' ', '\r', '\t' -> return // Comments don't need to be parsed
            '\n' -> line++
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '!' -> addToken(
                    if(match('=')) BANG_EQUAL
                    else BANG
                )
            '=' -> addToken(if(match('=')) EQUAL_EQUAL else EQUAL)
            '>' -> addToken(
                    if(match('=')) GREATER_EQUAL
                    else GREATER
                )
            '<' -> addToken(
                    if(match('=')) LESS_EQUAL
                    else if(match('-')) LEFT_ARROW
                    else LESS
                )
            '-' -> addToken(
                    if(match('-')) MINUS_MINUS
                    else if(match('>')) RIGHT_ARROW
                    else MINUS
                )
            '+' -> addToken(if(match('+')) PLUS_PLUS else PLUS)
            '%' -> addToken(MODULO)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '/' -> addToken(SLASH)
            '&' -> addToken(AND)
            '|' -> addToken(OR)
            '"' -> string()
            '#' -> {
                while (peek() != '\n' && !isAtEnd)
                    advance()
            }
            '[' -> addToken(LEFT_BRACKET)
            ']' -> addToken(RIGHT_BRACKET)
            else -> {
                if(isDigit(c))
                    number()

                else if (isAlpha(c))
                    identifier()

                else
                    throw ParsingError(line, "Unexpected character '$c'")
            }
        }
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(
            type = type,
            literal = false
        )
    }

    private fun addToken(type: TokenType, literal: Any) {
        tokens.add(
            Token(
                type = type,
                lexeme = source.substring(start, current),
                literal = literal,
                line = line
            )
        )
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd)
            return false

        if (source[current] != expected)
            return false

        current++
        return true
    }

    private fun peek(): Char {
        if (isAtEnd)
            return ' '

        return source[current]
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd) {
            if (peek() == '\n') 
                line++
            
            advance()
        }

        if (isAtEnd)
            throw ParsingError(line, "Unterminated string.")

        advance()

        addToken(
            type = STRING,
            literal = source.substring(start + 1, current - 1)
        )
    }

    private fun isDigit(c: Char): Boolean {
        return c >= '0' && c <= '9';
    }

    private fun number() {
        while (isDigit(peek()))
            advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(
            type = NUMBER,
            literal = source.substring(start, current).toDouble()
        )
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length)
            return ' ';

        return source[current + 1]
    }

    private fun identifier() {
        while (isAlphaNumeric(peek()))
            advance();

        addToken(keywords[source.substring(start, current)] ?: IDENTIFIER);
    }

    private fun isAlpha(c: Char): Boolean {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c);
    }
}
