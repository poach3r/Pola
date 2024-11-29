package org.poach3r.backend

import org.poach3r.Expr
import org.poach3r.Stmt
import org.poach3r.Token
import org.poach3r.TokenType
import org.poach3r.TokenType.*
import org.poach3r.errors.ParsingError

class Parser {
    private var current: Int = 0
    private var tokens: List<Token> = arrayListOf()

    private val peek: Token get() = tokens[current]
    private val previous: Token get() = if (current == 0) peek else tokens[current - 1]
    private val isAtEnd: Boolean get() = peek.type == EOF

    fun parse(tokens: List<Token>): List<Stmt> {
        val statements = ArrayList<Stmt>()
        this.tokens = tokens
        current = 0

        while (!isAtEnd) {
            statements.add(declaration())
        }

        return statements
    }

    private fun declaration(): Stmt = when {
        match(CLASS) -> classDeclaration()
        match(FUN) -> function("function")
        match(VAR) -> varDeclaration(true)
        match(VAL) -> varDeclaration(false)
        else -> statement()
    }

    private fun statement(): Stmt = when {
        match(PRINT) -> printStatement()
        match(RETURN) -> returnStatement()
        match(LEFT_BRACE) -> Stmt.Companion.Block(block())
        match(IF) -> ifStatement()
        match(WHILE) -> whileStatement()
        match(FOR) -> forStatement()
        match(BREAK) -> breakStatement()
        else -> expressionStatement()
    }

    private fun function(kind: String): Stmt.Companion.Function {
        val name = if (match(IDENTIFIER)) previous else null

        consume(LEFT_PAREN, "Expected '(' after $kind name.")

        val parameters = mutableListOf<Token>()
        if (!check(RIGHT_PAREN)) {
            do {
                parameters.add(consume(IDENTIFIER, "Expected parameter name."))
            } while (match(COMMA))
        }

        consume(RIGHT_PAREN, "Expected ')' after parameters.")
        consume(LEFT_BRACE, "Expected '{' before $kind body.")

        return Stmt.Companion.Function(
            name = name,
            parameters = parameters,
            body = block()
        )
    }

    private fun varDeclaration(mutable: Boolean): Stmt.Companion.Var {
        val name = consume(IDENTIFIER, "Expected variable name.")
        consume(EQUAL, "Cannot declare variable '${name.lexeme}' without an initializer.")

        return Stmt.Companion.Var(
            name = name,
            initializer = expression,
            mutable = mutable
        )
    }

    private fun classDeclaration(): Stmt {
        val name = consume(IDENTIFIER, "Expected class name.")

        val superclass = if (match(INHERITS)) {
            consume(IDENTIFIER, "Expected superclass name after '<'.")
            Expr.Companion.Var(previous)
        } else null

        consume(LEFT_BRACE, "Expected '{' before class body.")

        val methods = mutableListOf<Stmt.Companion.Function>()
        while (!check(RIGHT_BRACE) && !isAtEnd) {
            methods.add(function("method"))
        }

        consume(RIGHT_BRACE, "Expected '}' after class body.")

        return Stmt.Companion.Class(name, superclass, methods)
    }

    private val expression: Expr get() = assignment()

    private fun assignment(): Expr {
        val expr = or()

        return when (expr) {
            is Expr.Companion.Var -> {
                if (match(EQUAL)) {
                    Expr.Companion.Assign(
                        name = expr.name,
                        value = assignment()
                    )
                } else expr
            }
            is Expr.Companion.Get -> {
                if (match(EQUAL)) {
                    Expr.Companion.Set(
                        name = expr.name,
                        value = assignment(),
                        obj = expr.obj
                    )
                } else expr
            }
            else -> expr.also {
                if (match(EQUAL)) throw ParsingError(previous.line, "Invalid assignment target.")
            }
        }
    }

    // Parsing methods for different precedence levels
    private fun or(): Expr = buildBinaryExpression(::and, OR) {
        Expr.Companion.Logical(operator = previous, left = it, right = and())
    }

    private fun and(): Expr = buildBinaryExpression(::equality, AND) {
        Expr.Companion.Logical(operator = previous, left = it, right = equality())
    }

    private fun equality(): Expr = buildBinaryExpression(::comparison, BANG_EQUAL, EQUAL_EQUAL)
    private fun comparison(): Expr = buildBinaryExpression(::term, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)
    private fun term(): Expr = buildBinaryExpression(::factor, MINUS, PLUS)
    private fun factor(): Expr = buildBinaryExpression(::unary, SLASH, STAR, MODULO)

    private fun buildBinaryExpression(
        nextLevel: () -> Expr,
        vararg operators: TokenType,
        customBuilder: ((Expr) -> Expr)? = null
    ): Expr {
        var expr = nextLevel()

        while (match(*operators)) {
            expr = customBuilder?.invoke(expr) ?: Expr.Companion.Binary(
                operator = previous,
                left = expr,
                right = nextLevel()
            )
        }

        return expr
    }

    private fun unary(): Expr =
        if (match(BANG, MINUS, PLUS_PLUS, MINUS_MINUS)) {
            Expr.Companion.Unary(
                operator = previous,
                right = unary()
            )
        } else call()

    private fun call(): Expr {
        var expr = primary()

        while (true) {
            expr = when {
                match(LEFT_PAREN) -> finishCall(expr)
                match(DOT) -> Expr.Companion.Get(
                    obj = expr,
                    name = consume(IDENTIFIER, "Expected property name after '.'.")
                )
                else -> break
            }
        }

        return expr
    }

    private fun primary(): Expr = when {
        match(FALSE) -> Expr.Companion.Literal(false)
        match(TRUE) -> Expr.Companion.Literal(true)
        match(NUMBER, STRING) -> Expr.Companion.Literal(previous.literal)
        match(SUPER) -> {
            val keyword = previous
            consume(DOT, "Expected '.' after 'super'")
            Expr.Companion.Super(
                keyword = keyword,
                method = consume(IDENTIFIER, "Expected superclass method name.")
            )
        }
        match(IDENTIFIER) -> Expr.Companion.Var(previous)
        match(LEFT_PAREN) -> {
            val expr = expression
            consume(RIGHT_PAREN, "Expected ')' after expression.")
            Expr.Companion.Grouping(expr)
        }
        match(THIS) -> Expr.Companion.This(previous)
        else -> throw ParsingError(peek.line, "Expected expression but got '${peek.lexeme}.")
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean =
        !isAtEnd && peek.type == type

    private fun advance(): Token {
        if (!isAtEnd) current++
        return previous
    }

    private fun consume(type: TokenType, message: String): Token =
        if (check(type)) advance()
        else throw ParsingError(peek.line, message)

    private fun finishCall(callee: Expr): Expr {
        val arguments = mutableListOf<Stmt>()

        if (!check(RIGHT_PAREN)) {
            do {
                arguments.add(declaration())
            } while (match(COMMA))
        }

        return Expr.Companion.Call(
            callee = callee,
            paren = consume(RIGHT_PAREN, "Expected ')' after arguments."),
            arguments = arguments
        )
    }

    private fun block(): List<Stmt> {
        val statements = mutableListOf<Stmt>()

        while (!check(RIGHT_BRACE) && !isAtEnd) {
            statements.add(declaration())
        }

        consume(RIGHT_BRACE, "Expected '}' after block.")
        return statements
    }

    // Statement methods
    private fun printStatement(): Stmt =
        Stmt.Companion.Print(expression)

    private fun returnStatement(): Stmt =
        Stmt.Companion.Return(keyword = previous, value = expression)

    private fun expressionStatement(): Stmt =
        Stmt.Companion.Expression(expression)

    private fun ifStatement(): Stmt {
        consume(LEFT_PAREN, "Expected '(' after if.")
        val condition = expression
        consume(RIGHT_PAREN, "Expected ')' after if.")

        return Stmt.Companion.If(
            condition = condition,
            thenBranch = statement(),
            elseBranch = if (match(ELSE)) statement() else null
        )
    }

    private fun whileStatement(): Stmt {
        consume(LEFT_PAREN, "Expected '(' after while.")
        val condition = expression
        consume(RIGHT_PAREN, "Expected ')' after condition.")

        return Stmt.Companion.While(condition, statement())
    }

    private fun forStatement(): Stmt {
        consume(LEFT_PAREN, "Expected '(' after for.")

        val initializer = if (match(SEMICOLON)) null else varDeclaration(true)
        consume(SEMICOLON, "Expected ';' after initializer.")

        val condition = expression
        consume(SEMICOLON, "Expected ';' after loop condition.")

        val incrementer = expression
        consume(RIGHT_PAREN, "Expected ')' after for clauses.")

        return Stmt.Companion.For(
            initializer = initializer,
            condition = condition,
            incrementer = incrementer,
            body = statement()
        )
    }

    private fun breakStatement(): Stmt =
        Stmt.Companion.Break(peek)
}