package network

open class NetworkException(message: String? = null, cause: Throwable? = null) : Exception(message) {
    init {
        if (cause != null) initCause(cause)
    }

    final override fun initCause(cause: Throwable?): Throwable {
        return super.initCause(cause)
    }
}

class SourceNotFoundException(source: String, cause: Throwable? = null) :
    NetworkException("Not found: $source", cause)

class ServerErrorException(message: String?, cause: Throwable?) :
    NetworkException(message, cause)
