package rss

open class RssCouldNotBeObtainedException (
    message: String? = null,
    cause: Throwable? = null
): Exception(message) {
    init {
        if (cause != null) initCause(cause)
    }

    final override fun initCause(cause: Throwable?): Throwable {
        return super.initCause(cause)
    }
}

class RssNotFoundException(source: String, cause: Throwable? = null):
    RssCouldNotBeObtainedException("Not found at source: $source", cause)


class RssInvalidException(message: String, cause: Throwable? = null):
    RssCouldNotBeObtainedException(message, cause)


class RssTemporaryUnavailableException(source: String, cause: Throwable? = null):
    RssCouldNotBeObtainedException("Temporary unavailable resource: $source", cause)
