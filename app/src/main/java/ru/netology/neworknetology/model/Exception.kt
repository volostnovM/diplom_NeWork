package ru.netology.neworknetology.model

import java.io.IOException
import java.sql.SQLException

sealed class AppError(var code: Int, val info: String) : RuntimeException()
class ApiException(code: Int, info: String) : AppError(code, info)
class ForbiddenException(code: Int, info: String) : AppError(code, info)

object NetworkException : AppError(code = -1, info = "No_Network_Exception")
object UnknownException : AppError(code = -2, info = "Unknown_Exception")
object DbErrorException : AppError(code = -3, info = "DbError_Exception")







//sealed class AppError(): RuntimeException() {
//    companion object {
//        fun from(e: Throwable): AppError = when (e) {
//            is ForbiddenError -> e
//            is AppError -> e
//            is SQLException -> DbError
//            is IOException -> NetworkError
//            else -> UnknownError
//        }
//    }
//}
//
//class ApiError(val code: Int, message: String): AppError()
//class ForbiddenError(val code: Int, message: String): AppError()
//
//
//object NetworkError : AppError()
//object UnknownError: AppError()
//object DbError: AppError()
////object CustomForbiddenError: AppError()