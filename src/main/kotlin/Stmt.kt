package org.poach3r

abstract class Stmt {
    interface Visitor<R> {
        fun visitExpressionStmt(stmt: Expression): R
        fun visitPrintStmt(stmt: Print): R
        fun visitVarStmt(stmt: Var): R
        fun visitBlockStmt(stmt: Block): R
        fun visitIfStmt(stmt: If): R
        fun visitWhileStmt(stmt: While): R
        fun visitForStmt(stmt: For): R
        fun visitBreakStmt(stmt: Break): R
        fun visitFunctionStmt(stmt: Function): R
        fun visitReturnStmt(stmt: Return): R
        fun visitClassStmt(stmt: Class): R
    }

    abstract fun <R> accept(visitor: Stmt.Visitor<R>): R

    companion object {
        data class Expression(
            val expr: Expr
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitExpressionStmt(this)
            }
        }

        data class Print(
            val expr: Expr
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitPrintStmt(this)
            }
        }

        data class Var(
            val name: Token,
            val initializer: Expr,
            val mutable: Boolean
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitVarStmt(this)
            }
        }

        data class Block(
            val statements: List<Stmt>
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitBlockStmt(this)
            }
        }

        data class If(
            val condition: Expr,
            val thenBranch: Stmt,
            val elseBranch: Stmt?
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitIfStmt(this)
            }
        }

        data class While(
            val condition: Expr,
            val body: Stmt
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitWhileStmt(this)
            }
        }

        data class For(
            val initializer: Var?,
            val condition: Expr,
            val incrementer: Expr,
            val body: Stmt
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitForStmt(this)
            }
        }

        data class Break(
            val token: Token
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitBreakStmt(this)
            }
        }

        data class Function(
            val name: Token?,
            val parameters: List<Token>,
            val body: List<Stmt>
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitFunctionStmt(this)
            }
        }

        data class Return(
            val keyword: Token,
            val value: Expr
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitReturnStmt(this)
            }
        }

        data class Class(
            val name: Token,
            val superclass: Expr.Companion.Var?,
            val methods: List<Function>,
        ): Stmt() {
            override fun <R> accept(visitor: Visitor<R>): R {
                return visitor.visitClassStmt(this)
            }
        }
    }
}