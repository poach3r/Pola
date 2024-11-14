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
    private val peek: Token
        get() = tokens[current]
    private val previous: Token
        get() {
            if(current == 0)
                return peek

            return tokens[current - 1]
        }
    private val isAtEnd: Boolean
        get() = peek.type == EOF
    private val expression: Expr
        get() = assignment()

    fun parse(tokens: List<Token>): List<Stmt> {
        val statements = ArrayList<Stmt>()
        this.tokens = tokens
        current = 0

        while(!isAtEnd)
            statements.add(declaration())

        return statements
    }

    private fun declaration(): Stmt {
        if(match(IMPORT))
            return import()

        if(match(CLASS))
            return classDeclaration()

        if(match(FUN))
            return function("function")

        if(match(VAR))
            return varDeclaration(true)

        if(match(VAL))
            return varDeclaration(false)

        return statement()
    }

    private fun statement(): Stmt {
        if(match(PRINT))
            return printStatement()

        if(match(RETURN))
            return returnStatement()

        if(match(LEFT_BRACE))
            return Stmt.Companion.Block(block())

        if(match(IF))
            return ifStatement()

        if(match(WHILE))
            return whileStatement()

        if(match(FOR))
            return forStatement()

        if(match(BREAK))
            return breakStatement()

        return expressionStatement()
    }

    // import NAME as NAME
    private fun import(): Stmt {
        var internalName = expression

        consume(AS, "Expected 'as' after library name.")

        val givenName = consume(IDENTIFIER, "Expected name after 'as'.")

        return Stmt.Companion.Var(
            name = givenName,
            mutable = false,
            initializer = internalName
        )
    }

    // fun NAME (PARAM, PARAM) { BODY }
    private fun function(kind: String): Stmt.Companion.Function {
        // fun NAME
        var name: Token? =
            if(match(IDENTIFIER))
                previous
            else
                null

        // ..NAME (
        consume(LEFT_PAREN, "Expected '(' after $kind name.")

        // ..(PARAM, PARAM
        val parameters = ArrayList<Token>()
        if(!check(RIGHT_PAREN)) {
            do {
                parameters.add(consume(IDENTIFIER, "Expected parameter name."))
            } while(match(COMMA))
        }

        // ..PARAM, PARAM)
        consume(RIGHT_PAREN, "Expected ')' after parameters.")

        // ..) {
        consume(LEFT_BRACE, "Expected '{' before $kind body.")

        return Stmt.Companion.Function(
            name = name,
            parameters = parameters,
            body = block() // ..BODY }
        )
    }

    // var NAME = VALUE
    private fun varDeclaration(mutable: Boolean): Stmt.Companion.Var {
        // var NAME
        val name = consume(IDENTIFIER, "Expected variable name.")

        // ..NAME =
        consume(EQUAL, "Cannot declare variable '${name.lexeme}' without an initializer.")

        return Stmt.Companion.Var(
            name = name,
            initializer = expression, // ..= VALUE
            mutable = mutable
        )
    }

    // class NAME { BODY }
    private fun classDeclaration(): Stmt {
        // class NAME
        val name = consume(IDENTIFIER, "Expected class name.")

        var superclass: Expr.Companion.Var? = null
        if(match(INHERITS)) {
            consume(IDENTIFIER, "Expected superclass name after '<'.")
            superclass = Expr.Companion.Var(previous)
        }

        // ..NAME {
        consume(LEFT_BRACE, "Expected '{' before class body.")

        // ..{ BODY
        val methods = ArrayList<Stmt.Companion.Function>()
        while(!check(RIGHT_BRACE) && !isAtEnd)
            methods.add(function("method"))

        // ..BODY }
        consume(RIGHT_BRACE, "Expected '}' after class body.")

        return Stmt.Companion.Class(name, superclass, methods)
    }

    // print VALUE
    private fun printStatement(): Stmt {
        return Stmt.Companion.Print(expression)
    }

    // return VALUE
    private fun returnStatement(): Stmt {
        return Stmt.Companion.Return(
            keyword = previous,
            value = expression
        )
    }

    private fun expressionStatement(): Stmt {
        return Stmt.Companion.Expression(expression)
    }

    // if (CONDITION) THEN else THEN
    private fun ifStatement(): Stmt {
        // if (
        consume(LEFT_PAREN, "Expected '(' after if.")

        // ..(CONDITION
        val condition = expression

        // ..CONDITION)
        consume(RIGHT_PAREN, "Expected ')' after if.")

        return Stmt.Companion.If(
            condition = condition,
            thenBranch =  statement(), // ..) THEN
            elseBranch = if(match(ELSE)) statement() else null) // ..else THEN
    }

    private fun whileStatement(): Stmt {
        consume(LEFT_PAREN, "Expected '(' after while.")
        val condition = expression
        consume(RIGHT_PAREN, "Expected ')' after condition.")

        return Stmt.Companion.While(condition, statement())
    }

    private fun forStatement(): Stmt {
        consume(LEFT_PAREN, "Expected '(' after for.")

        val initializer: Stmt.Companion.Var? =
            if (match(SEMICOLON))
                null
            else
                varDeclaration(true)

        consume(SEMICOLON, "Expected ';' after initializer.")

        val condition = expression

        consume(SEMICOLON, "Expected ';' after loop condition.")

        val incrementer = expression

        consume(RIGHT_PAREN, "Expected ')' after for clauses.")

        return Stmt.Companion.For(
            initializer = initializer,
            condition = condition,
            incrementer = incrementer,
            body = statement(),
        )
    }

    private fun breakStatement(): Stmt {
        return Stmt.Companion.Break(peek)
    }

    // { STATEMENTS }
    private fun block(): List<Stmt> {
        val statements: ArrayList<Stmt> = arrayListOf()

        while(!check(RIGHT_BRACE) && !isAtEnd) { // { STATEMENTS
            statements.add(declaration())
        }

        consume(RIGHT_BRACE, "Expected '}' after block.") // ..STATEMENTS }
        return statements
    }

    // NAME = VALUE
    private fun assignment(): Expr {
        var expr = or()

        if(match(EQUAL)) { // NAME =
            if (expr is Expr.Companion.Var)
                return Expr.Companion.Assign(
                    name = expr.name,
                    value = assignment() // ..= VALUE
                )

            if(expr is Expr.Companion.Get)
                return Expr.Companion.Set(
                    name = expr.name,
                    value = assignment(),
                    obj = expr.obj
                )

            throw ParsingError(previous.line, "Invalid assignment target.")
        }

        return expr
    }

    // VALUE or VALUE
    // VALUE | VALUE
    private fun or(): Expr {
        var expr = and()

        while(match(OR)) {
            expr = Expr.Companion.Logical(
                operator = previous, // VALUE |
                left = expr, // VALUE
                right = and() // ..| VALUE
            )
        }

        return expr
    }

    // VALUE and VALUE
    // VALUE & VALUE
    private fun and(): Expr {
        var expr = equality()

        while(match(AND)) {
            expr = Expr.Companion.Logical(
                operator = previous, // VALUE &
                left = expr, // VALUE
                right = equality() // ..& VALUE
            )
        }

        return expr
    }

    private fun equality(): Expr {
        var expr = comparison()

        while(match(BANG_EQUAL, EQUAL_EQUAL)) {
            expr = Expr.Companion.Binary(
                operator = previous,
                left = expr,
                right = comparison()
            )
        }

        return expr
    }

    private fun comparison(): Expr {
        var expr = term()

        while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            expr = Expr.Companion.Binary(
                operator = previous,
                left = expr,
                right = term()
            )
        }

        return expr
    }

    private fun term(): Expr {
        var expr = factor()

        while(match(MINUS, PLUS)) {
            expr = Expr.Companion.Binary(
                operator = previous,
                left = expr,
                right = factor()
            )
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while(match(SLASH, STAR, MODULO)) {
            expr = Expr.Companion.Binary(
                operator = previous,
                left = expr,
                right = unary()
            )
        }

        return expr
    }

    private fun unary(): Expr {
        if(match(BANG, MINUS, PLUS_PLUS, MINUS_MINUS)) {
            return Expr.Companion.Unary(
                operator = previous,
                right = unary()
            )
        }

        return call()
    }

    private fun call(): Expr {
        var expr = primary()

        while(true) {
            if(match(LEFT_PAREN))
                expr = finishCall(expr)
            else if(match(DOT))
                expr = Expr.Companion.Get(
                    obj = expr,
                    name = consume(IDENTIFIER, "Expected property name after '.'.")
                )
            else
                break
        }

        return expr
    }

    private fun primary(): Expr {
        if(match(FALSE))
            return Expr.Companion.Literal(false)

        if(match(TRUE))
            return Expr.Companion.Literal(true)

        if(match(NUMBER, STRING))
            return Expr.Companion.Literal(previous.literal)

        if(match(SUPER)) {
            val keyword = previous
            consume(DOT, "Expected '.' after 'super'")

            return Expr.Companion.Super(
                keyword = keyword,
                method = consume(IDENTIFIER, "Expected superclass method name.")
            )
        }

        if(match(IDENTIFIER))
            return Expr.Companion.Var(previous)

        if(match(LEFT_PAREN)) {
            var expr = expression
            consume(RIGHT_PAREN, "Expected ')' after expression.")
            return Expr.Companion.Grouping(expr)
        }

        if(match(THIS))
            return Expr.Companion.This(previous)

        throw ParsingError(peek.line, "Expected expression but got '${peek.lexeme}.")
    }

    private fun match(vararg types: TokenType): Boolean {
        types.map { check(it) }.filter { it }.forEach {
            advance()
            return true
        }

        return false
    }

    private fun check(type: TokenType): Boolean {
        if(isAtEnd)
            return false

        return peek.type == type
    }

    private fun advance(): Token {
        if (!isAtEnd)
            current++

        return previous
    }

    private fun consume(type: TokenType, message: String): Token {
        if(check(type))
            return advance()

        throw ParsingError(
            line = peek.line,
            msg = message
        )
    }

    private fun finishCall(callee: Expr): Expr {
        val arguments = ArrayList<Stmt>()

        if(!check(RIGHT_PAREN)) {
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
}