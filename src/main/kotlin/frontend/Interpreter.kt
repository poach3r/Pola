package org.poach3r.frontend

import org.poach3r.Expr
import org.poach3r.Stmt
import org.poach3r.Token
import org.poach3r.TokenType.*
import org.poach3r.errors.BreakError
import org.poach3r.errors.ReturnError
import org.poach3r.errors.RuntimeError
import org.poach3r.frontend.classes.PForeignClass
import org.poach3r.frontend.functions.Import
import org.poach3r.frontend.functions.PFunction

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Any> {
    val globals = Environment().apply {
        this.define(
            name = "import",
            value = Import(),
            mutable = false
        )
        this.define(
            name = "string",
            value = org.poach3r.frontend.classes.String(),
            mutable = false
        )
    }
    private var environment = globals
    private val locals = HashMap<Expr, Int>()

    fun interpret(stmts: List<Stmt>) {
        stmts.forEach {
            execute(it)
        }
    }

    private fun execute(stmt: Stmt): Any {
        return stmt.accept(this)
    }

    override fun visitExpressionStmt(stmt: Stmt.Companion.Expression): Any {
        return evaluate(stmt.expr)
    }

    override fun visitFunctionStmt(stmt: Stmt.Companion.Function): Any {
        if (stmt.name == null) {
            return PFunction(
                declaration = stmt,
                closure = environment,
                isInitializer = false
            )
        }

        return environment.define(
            name = stmt.name.lexeme,
            mutable = false,
            value = PFunction(
                declaration = stmt,
                closure = environment,
                isInitializer = false
            )
        )
    }

    override fun visitPrintStmt(stmt: Stmt.Companion.Print): Any {
        val value = stringify(evaluate(stmt.expr))
        println(value)
        return value
    }

    override fun visitVarStmt(stmt: Stmt.Companion.Var): Any {
        val value = evaluate(stmt.initializer)
        environment.define(stmt.name, stmt.mutable, value)
        return value
    }

    override fun visitBlockStmt(stmt: Stmt.Companion.Block): Any {
        executeBlock(stmt.statements, Environment(environment))
        return 0
    }

    override fun visitClassStmt(stmt: Stmt.Companion.Class): Any {
        var superclass: Any? = null
        stmt.superclass?.let {
            superclass = evaluate(stmt.superclass)
            if (superclass !is PForeignClass)
                throw RuntimeError(
                    line = stmt.superclass.name.line,
                    msg = "Superclass '$superclass' is not a class."
                )
        }

        environment.define(
            name = stmt.name.lexeme,
            mutable = true,
            value = 0
        )

        stmt.superclass?.let {
            environment = Environment(environment).apply {
                this.define(
                    name = "super",
                    value = superclass!!,
                    mutable = false
                )
            }
        }

        environment.assign(
            token = stmt.name,
            value = PForeignClass(
                name = stmt.name.lexeme,
                superclass = superclass as PForeignClass?,
                methods = HashMap<String, PCallable>().apply {
                    stmt.methods.forEach {
                        this.put(
                            it.name!!.lexeme, PFunction(
                                declaration = it,
                                closure = environment,
                                isInitializer = it.name.lexeme == "init"
                            )
                        )
                    }
                }
            )
        )

        stmt.superclass?.let {
            environment.enclosing?.let {
                environment = it
                return 0
            }

            throw RuntimeError(
                stmt.name.line,
                "Failed to find enclosing environment for superclass '${it.name.lexeme}'. Contact a developer!'"
            )
        }

        return 0
    }

    override fun visitIfStmt(stmt: Stmt.Companion.If): Any {
        val previous = this.environment
        try {
            this.environment = Environment(previous)

            if (isTruthy(evaluate(stmt.condition))) {
                execute(stmt.thenBranch)
            } else if (stmt.elseBranch != null) {
                execute(stmt.elseBranch)
            }
        } finally {
            this.environment = previous
        }

        return 0
    }

    override fun visitWhileStmt(stmt: Stmt.Companion.While): Any {
        val previous = this.environment
        try {
            this.environment = Environment(previous)

            while (isTruthy(evaluate(stmt.condition))) {
                execute(stmt.body)
            }
        } finally {
            this.environment = previous
        }

        return 0
    }

    override fun visitForStmt(stmt: Stmt.Companion.For): Any {
        val previous = this.environment
        try {
            this.environment = Environment(previous)

            stmt.initializer?.let {
                execute(it)
            }

            while (isTruthy(evaluate(stmt.condition))) {
                execute(stmt.body)
                evaluate(stmt.incrementer)
            }
        } finally {
            this.environment = previous
        }

        return 0
    }

    override fun visitBreakStmt(stmt: Stmt.Companion.Break): Any {
        throw BreakError()
    }

    override fun visitReturnStmt(stmt: Stmt.Companion.Return): Any {
        throw ReturnError(evaluate(stmt.value))
    }

    override fun visitAssignExpr(expr: Expr.Companion.Assign): Any {
        val value = evaluate(expr.value)
        val distance = locals.get(expr)

        if (distance != null)
            environment.assignAt(
                distance,
                expr.name,
                value,
                false
            )
        else
            globals.assign(expr.name, value)

        return value
    }

    override fun visitBinaryExpr(expr: Expr.Companion.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        when (expr.operator.type) {
            PLUS -> {
                if (left is Double && right is Double)
                    return left + right
//
//                if(left is String && right is String)
//                    return left + right

                return createString(stringify(left) + stringify(right))

//                throw RuntimeError(
//                    msg = "Operands '$right' and '$left' must be numbers or strings."
//                )
            }

            MINUS -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double - right as Double
            }

            SLASH -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double / right as Double
            }

            STAR -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double * right as Double
            }

            MODULO -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double % right as Double
            }

            GREATER -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double > right as Double
            }

            GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double >= right as Double
            }

            LESS -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) < right as Double
            } // idk why this needs parentheses
            LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                return left as Double <= right as Double
            }

            BANG_EQUAL -> {
                val l =
                    if(left is PInstance)
                        left.get()
                    else
                        left
                val r =
                    if(right is PInstance)
                        right.get()
                    else
                        right

                return l != r
            }
            EQUAL_EQUAL -> {
                val l =
                    if(left is PInstance)
                        left.get()
                    else
                        left
                val r =
                    if(right is PInstance)
                        right.get()
                    else
                        right

                return l == r
            }

            else -> return false
        }

        return false
    }

    override fun visitGroupingExpr(expr: Expr.Companion.Grouping): Any {
        return evaluate(expr.expr)
    }

    override fun visitLiteralExpr(expr: Expr.Companion.Literal): Any {
        if (expr.value is String)
            return createString(expr.value)

        return expr.value
    }

    override fun visitLogicalExpr(expr: Expr.Companion.Logical): Any {
        val left = evaluate(expr.left)

        if (expr.operator.type == OR) {
            if (isTruthy(left))
                return left
        } else if (!isTruthy(left))
            return left

        return evaluate(expr.right)
    }

    override fun visitUnaryExpr(expr: Expr.Companion.Unary): Any {
        val right = evaluate(expr.right)

        when (expr.operator.type) {
            MINUS -> {
                checkNumberOperand(expr.operator, right)
                return -(right as Double)
            }

            BANG -> return !isTruthy(right)
            PLUS_PLUS -> {
                checkNumberOperand(expr.operator, right)
                return (right as Double) + 1
            }

            MINUS_MINUS -> {
                checkNumberOperand(expr.operator, right)
                return (right as Double) - 1
            }

            else -> return 0
        }

        return 0 // unreachable
    }

    /**
     * Gets a variable based on distance.
     * @return The desired variable.
     */
    override fun visitVarExpr(expr: Expr.Companion.Var): Any {
        return lookupVar(expr.name, expr)
    }

    /**
     * Calls native and foreign functions.
     * @return Whatever is returned by the function call
     */
    override fun visitCallExpr(expr: Expr.Companion.Call): Any {
        val callee = evaluate(expr.callee)

        if (callee !is PCallable) {
            throw RuntimeError(
                line = expr.paren.line,
                msg = "Cannot call '$callee' as it is not a function."
            )
        }

        // get function arguments
        val arguments = ArrayList<Any>().apply {
            // if the function is a method
            if (expr.callee is Expr.Companion.Get) {
                val result = evaluate(expr.callee.obj)
                if (result is PInstance) {
                    try {
                        // get the literal value of the class and pass it as an argument
                        this.add(result.get("__literalValue"))
                    } catch (_: RuntimeError) {
                    } // if its not a native class then it'll throw a RuntimeError
                }
            }

            // add the actual arguments
            expr.arguments.map { execute(it) }.forEach(this::add)
        }

        // check if the provided arguments match the expected ones
        if (arguments.size != callee.arity && callee.arity != -1)
            throw RuntimeError(expr.paren.line, "Expected ${callee.arity} arguments but got ${arguments.size}.")

        // call the function
        try {
            return callee.call(this, arguments)
        } catch (e: RuntimeError) {
            throw RuntimeError( // this allows errors thrown while calling to have vague line numbers
                line = expr.paren.line,
                msg = e.msg
            )
        }
    }

    override fun visitGetExpr(expr: Expr.Companion.Get): Any {
        val obj = evaluate(expr.obj)

        if (obj !is PInstance)
            throw RuntimeError(
                expr.name.line,
                "Attempted to access non-existent property '${expr.name.lexeme}' of '$obj'."
            )

        return obj.get(expr.name)
    }

    override fun visitSetExpr(expr: Expr.Companion.Set): Any {
        val obj = evaluate(expr.obj)

        if (obj !is PInstance)
            throw RuntimeError(
                expr.name.line,
                "Attempted to access field '${expr.name.lexeme}' of a non-instance object."
            )

        obj.set(expr.name, evaluate(expr.value))

        return 0
    }

    override fun visitSuperExpr(expr: Expr.Companion.Super): Any {
        val distance = locals.get(expr)!!
        val superclass = environment.getAt(distance, "super") as PForeignClass

        superclass.findMethod(expr.method.lexeme)?.let {
            return (it as PFunction).bind(environment.getAt(distance - 1, "this") as PInstance)
        }

        throw RuntimeError(
            line = expr.method.line,
            msg = "Failed to find method '${expr.method.lexeme}' in superclass '${superclass.name}'."
        )
    }

    override fun visitThisExpr(expr: Expr.Companion.This): Any {
        return lookupVar(expr.keyword, expr)
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

    private fun isTruthy(obj: Any): Boolean {
        if (obj is Boolean)
            return obj

        return false
    }

    private fun checkNumberOperand(operator: Token, operand: Any) {
        if (operand is Double)
            return

        throw RuntimeError(
            line = operator.line,
            msg = "Operands '$operand' must be a number"
        )
    }

    private fun checkNumberOperands(operator: Token, left: Any, right: Any) {
        if (left is Double && right is Double)
            return

        throw RuntimeError(
            line = operator.line,
            msg = "Operands '$left' and '$right' must be numbers"
        )
    }

    fun stringify(obj: Any): String {
        if (obj is Double) {
            var text = obj.toString()

            if (text.endsWith(".0"))
                text = text.substring(0, text.length - 2)

            return text
        }

        if (obj is String)
            return obj.replace("\\n", "\n")

        return obj.toString()
    }

    fun executeBlock(statements: List<Stmt>, environment: Environment) {
        val previous = this.environment
        try {
            this.environment = environment
            statements.forEach {
                execute(it)
            }
        } catch (_: BreakError) {
            return
        } finally {
            this.environment = previous
        }
    }

    fun resolve(expr: Expr, depth: Int) {
        locals.put(expr, depth)
    }

    private fun lookupVar(name: Token, expr: Expr): Any {
        val distance = locals[expr]

        if (distance == null)
            return environment.get(name)

        return environment.getAt(distance, name.lexeme)!!
    }

    fun createString(text: String): Any {
        return (globals.variables.get("string")!!.value as org.poach3r.frontend.classes.String).call(this, listOf(text))
    }
}