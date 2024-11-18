package org.poach3r.frontend

import org.poach3r.Expr
import org.poach3r.Stmt
import org.poach3r.Token
import org.poach3r.errors.ResolvingError
import java.util.Stack

class Resolver(
    private val interpreter: Interpreter
) : Expr.Visitor<Any>, Stmt.Visitor<Any> {
    private enum class FunctionType {
        NONE,
        BLOCK,
        FUNCTION,
        METHOD,
        INITIALIZER
    }

    private enum class ClassType {
        NONE,
        CLASS,
        SUBCLASS
    }

    private var currentClass = ClassType.NONE
    private var currentFunction = FunctionType.NONE
    private val scopes = Stack<HashMap<String, Boolean>>()

    override fun visitBlockStmt(stmt: Stmt.Companion.Block): Any {
        val enclosingFunction = currentFunction
        currentFunction = FunctionType.BLOCK

        beginScope()
        resolve(stmt.statements)
        endScope()

        currentFunction = enclosingFunction

        return 0
    }

    override fun visitClassStmt(stmt: Stmt.Companion.Class): Any {
        val enclosingClass = currentClass
        currentClass = ClassType.CLASS

        declare(stmt.name)
        define(stmt.name)

        stmt.superclass?.let {
            if (stmt.name.lexeme == it.name.lexeme)
                throw ResolvingError(
                    line = stmt.name.line,
                    msg = "A class cannot inherit from itself."
                )

            currentClass = ClassType.SUBCLASS
            resolve(it)
            beginScope()
            scopes.peek().put("super", true)
        }

        beginScope()
        scopes.peek().put("this", true)

        stmt.methods.forEach {
            resolveFunction(
                function = it,
                type = if (it.name!!.lexeme == "init")
                    FunctionType.INITIALIZER
                else
                    FunctionType.METHOD
            )
        }

        endScope()

        stmt.superclass?.let {
            endScope()
        }

        currentClass = enclosingClass

        return 0
    }

    override fun visitVarStmt(stmt: Stmt.Companion.Var): Any {
        declare(stmt.name)
        resolve(stmt.initializer)
        define(stmt.name)
        return 0
    }

    override fun visitVarExpr(expr: Expr.Companion.Var): Any {
        if (!scopes.isEmpty && scopes.peek()[expr.name.lexeme] == false)
            throw ResolvingError(
                line = expr.name.line,
                msg = "Cannot read local variable in it's own initializer."
            )

        resolveLocal(expr, expr.name)

        return 0
    }

    override fun visitAssignExpr(expr: Expr.Companion.Assign): Any {
        resolve(expr.value)
        resolveLocal(expr, expr.name)
        return 0
    }

    override fun visitFunctionStmt(stmt: Stmt.Companion.Function): Any {
        if (stmt.name != null) {
            declare(stmt.name)
            define(stmt.name)
        }

        resolveFunction(stmt, FunctionType.FUNCTION)
        return 0
    }

    override fun visitExpressionStmt(stmt: Stmt.Companion.Expression): Any {
        resolve(stmt.expr)
        return 0
    }

    override fun visitIfStmt(stmt: Stmt.Companion.If): Any {
        beginScope()
        resolve(stmt.condition)
        resolve(stmt.thenBranch)

        if (stmt.elseBranch != null)
            resolve(stmt.elseBranch)

        endScope()

        return 0
    }

    override fun visitWhileStmt(stmt: Stmt.Companion.While): Any {
        beginScope()
        resolve(stmt.condition)
        resolve(stmt.body)
        endScope()

        return 0
    }

    override fun visitForStmt(stmt: Stmt.Companion.For): Any {
        beginScope()
        stmt.initializer?.let {
            resolve(it)
        }
        resolve(stmt.condition)
        resolve(stmt.incrementer)
        resolve(stmt.body)
        endScope()

        return 0
    }

    override fun visitCallExpr(expr: Expr.Companion.Call): Any {
        resolve(expr.callee)

        expr.arguments.forEach {
            resolve(it)
        }

        return 0
    }

    override fun visitGetExpr(expr: Expr.Companion.Get): Any {
        resolve(expr.obj)

        return 0
    }

    override fun visitSetExpr(expr: Expr.Companion.Set): Any {
        resolve(expr.value)
        resolve(expr.obj)

        return 0
    }

    override fun visitSuperExpr(expr: Expr.Companion.Super): Any {
        if (currentClass == ClassType.NONE)
            throw ResolvingError(expr.keyword.line, "Cannot use 'super' outside of a class.")

        if (currentClass != ClassType.SUBCLASS)
            throw ResolvingError(expr.keyword.line, "Cannot use 'super' in a class with no superclass.")

        resolveLocal(expr, expr.keyword)

        return 0
    }

    override fun visitThisExpr(expr: Expr.Companion.This): Any {
        if (currentClass == ClassType.NONE)
            throw ResolvingError(expr.keyword.line, "Can't use 'this' outside of a class.")

        resolveLocal(expr, expr.keyword)

        return 0
    }

    override fun visitGroupingExpr(expr: Expr.Companion.Grouping): Any {
        resolve(expr.expr)
        return 0
    }

    override fun visitLiteralExpr(expr: Expr.Companion.Literal): Any {
        return 0
    }

    override fun visitLogicalExpr(expr: Expr.Companion.Logical): Any {
        resolve(expr.left)
        resolve(expr.right)
        return 0
    }

    override fun visitUnaryExpr(expr: Expr.Companion.Unary): Any {
        resolve(expr.right)
        return 0
    }

    override fun visitBinaryExpr(expr: Expr.Companion.Binary): Any {
        resolve(expr.left)
        resolve(expr.right)
        return 0
    }

    override fun visitBreakStmt(stmt: Stmt.Companion.Break): Any {
        if (currentFunction != FunctionType.BLOCK)
            throw ResolvingError(
                line = stmt.token.line,
                msg = "Cannot break outside of a loop."
            )

        return 0
    }

    override fun visitPrintStmt(stmt: Stmt.Companion.Print): Any {
        TODO("Not yet implemented")
    }

    override fun visitReturnStmt(stmt: Stmt.Companion.Return): Any {
        if (currentFunction == FunctionType.INITIALIZER)
            throw ResolvingError(
                line = stmt.keyword.line,
                msg = "Cannot return from an initializer."
            )

        resolve(stmt.value)

        return 0
    }

    fun resolve(statements: List<Stmt>) {
        statements.forEach { resolve(it) }
    }

    private fun resolve(stmt: Stmt) {
        stmt.accept(this)
    }

    private fun resolve(expr: Expr) {
        expr.accept(this)
    }

    private fun beginScope() {
        scopes.push(HashMap<String, Boolean>())
    }

    private fun endScope() {
        scopes.pop()
    }

    private fun declare(name: Token) {
        if (scopes.isEmpty)
            return

        val scope = scopes.peek()

        scope.put(
            name.lexeme,
            false
        )
    }

    private fun define(name: Token) {
        if (scopes.isEmpty)
            return

        scopes.peek().put(
            name.lexeme,
            true
        )
    }

    private fun resolveLocal(expr: Expr, name: Token) {
        for (i in scopes.size - 1 downTo 0) {
            if (scopes[i].containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size - 1 - i)
                return
            }
        }
    }

    private fun resolveFunction(function: Stmt.Companion.Function, type: FunctionType) {
        val enclosingFunction = currentFunction
        currentFunction = type

        beginScope()

        function.parameters.forEach {
            declare(it)
            define(it)
        }

        resolve(function.body)
        endScope()

        currentFunction = enclosingFunction
    }
}