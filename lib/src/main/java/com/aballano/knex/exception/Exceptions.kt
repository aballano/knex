package com.aballano.knex.exception

open class KnexRendererException(detailMessage: String) : RuntimeException(detailMessage)

class KnexRendererNotFoundException(detailMessage: String) : KnexRendererException(detailMessage)
