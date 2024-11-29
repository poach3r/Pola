package org.poach3r.backend

import org.poach3r.Token
import org.poach3r.TokenType
import org.poach3r.TokenType.*
import org.poach3r.errors.ParsingError

class Scanner {
    // Immutable map of keywords: this pretty much just grants better performance and thread safety [better in the long run basically]
    private val keywords = mapOf(
        "and" to AND,
        "else" to ELSE,
        "false" to FALSE,
        "true" to TRUE,
        "for" to FOR,
        "fun" to FUN,
        "if" to IF,
        "or" to OR,
        "return" to RETURN,
        "var" to VAR,
        "val" to VAL,
        "while" to WHILE,
        "break" to BREAK,
        "class" to CLASS,
        "this" to THIS,
        "super" to SUPER,
        "inherits" to INHERITS
    )

    // Scanning state encapsulated in method to prevent state pollution
    fun scanTokens(source: String): List<Token> {
        val state = ScannerState(source)
        return state.scan()
    }

    // Inner class to manage scanning state and process
    private inner class ScannerState(private val source: String) {
        private val tokens = mutableListOf<Token>()
        private var start = 0
        private var current = 0
        private var line = 1

        private val isAtEnd: Boolean
            get() = current >= source.length

        fun scan(): List<Token> {
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
            when (val c = advance()) {
                ' ', '\r', '\t' -> return
                '\n' -> line++
                '(' -> addToken(LEFT_PAREN)
                ')' -> addToken(RIGHT_PAREN)
                '{' -> addToken(LEFT_BRACE)
                '}' -> addToken(RIGHT_BRACE)
                ',' -> addToken(COMMA)
                '.' -> addToken(DOT)
                '[' -> addToken(LEFT_BRACKET)
                ']' -> addToken(RIGHT_BRACKET)
                '%' -> addToken(MODULO)
                ';' -> addToken(SEMICOLON)
                '*' -> addToken(STAR)
                '/' -> addToken(SLASH)
                '&' -> addToken(AND)
                '|' -> addToken(OR)
                '#' -> skipComment()
                '"' -> processString()
                '!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
                '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
                '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
                '<' -> processLessOperator()
                '-' -> processMinusOperator()
                '+' -> addToken(if (match('+')) PLUS_PLUS else PLUS)
                else -> when {
                    c.isDigit() -> processNumber()
                    c.isLetter() || c == '_' -> processIdentifier()
                    else -> throw ParsingError(line, "Unexpected character '$c'")
                }
            }
        }

        private fun processLessOperator() = addToken(
            when {
                match('=') -> LESS_EQUAL
                match('-') -> LEFT_ARROW
                else -> LESS
            }
        )

        private fun processMinusOperator() = addToken(
            when {
                match('-') -> MINUS_MINUS
                match('>') -> RIGHT_ARROW
                else -> MINUS
            }
        )

        private fun skipComment() {
            while (peek() != '\n' && !isAtEnd) advance()
        }

        private fun processString() {
            while (peek() != '"' && !isAtEnd) {
                if (peek() == '\n') line++
                advance()
            }

            if (isAtEnd) throw ParsingError(line, "Unterminated string.")

            advance() // Consume closing quote
            addToken(
                type = STRING,
                literal = source.substring(start + 1, current - 1)
            )
        }

        private fun processNumber() {
            while (peek().isDigit()) advance()

            // Handle decimal part
            if (peek() == '.' && peekNext().isDigit()) {
                advance() // Consume decimal point
                while (peek().isDigit()) advance()
            }

            addToken(
                type = NUMBER,
                literal = source.substring(start, current).toDouble()
            )
        }

        private fun processIdentifier() {
            while (peek().isLetterOrDigit() || peek() == '_') advance()

            val text = source.substring(start, current)
            val type = keywords[text] ?: IDENTIFIER
            addToken(type)
        }

        private fun advance(): Char = source[current++]

        private fun match(expected: Char): Boolean {
            if (isAtEnd || source[current] != expected) return false
            current++
            return true
        }

        private fun peek(): Char =
            if (isAtEnd) ' ' else source[current]

        private fun peekNext(): Char =
            if (current + 1 >= source.length) ' '
            else source[current + 1]

        private fun addToken(type: TokenType, literal: Any? = null) {
            tokens.add(
                Token(
                    type = type,
                    lexeme = source.substring(start, current),
                    literal = literal ?: false,
                    line = line
                )
            )
        }
    }
}