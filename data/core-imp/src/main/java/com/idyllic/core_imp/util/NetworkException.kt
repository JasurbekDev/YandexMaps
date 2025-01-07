package com.idyllic.core_imp.util


class ServerException(message: String) : Exception(message)

class NetworkException(message: String) : Exception(message)
class GlobalException(message: String, val statusCode: Int) :
    Exception("$message code = $statusCode")

class TokenWrongException(message: String) : Exception(message)
class NeedPassChangeException(message: String) : Exception(message)
class AppUpdateException(massage: String) : Exception(massage)
class BlockUserException(massage: String) : Exception(massage)
class InfoForUser(massage: String) : Exception(massage)
class InternalServerError(massage: String) : Exception(massage)
class TechnicalBreakServerError(massage: String) : Exception(massage)
class DdosServerError(massage: String) : Exception(massage)
class ErrorToServerError(message: String) : Exception(message)
class TimeoutError(message: String) : Exception(message)
class RefreshTokenExpired(message: String) : Exception(message)
class DeviceNotActivated(message: String) : Exception(message)