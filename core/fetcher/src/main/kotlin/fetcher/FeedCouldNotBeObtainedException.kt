package fetcher

open class FeedCouldNotBeObtainedException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message) {
    init {
        if (cause != null) initCause(cause)
    }

    final override fun initCause(cause: Throwable?): Throwable {
        return super.initCause(cause)
    }
}

class FeedNotFoundException(source: String, cause: Throwable? = null) :
    FeedCouldNotBeObtainedException("Not found at source: $source", cause)

class FeedInvalidException(message: String, cause: Throwable? = null) :
    FeedCouldNotBeObtainedException(message, cause)

class FeedTemporaryUnavailableException(source: String, cause: Throwable? = null) :
    FeedCouldNotBeObtainedException("Temporary unavailable resource: $source", cause)
