package com.example.demo.service

object RequestContext : ThreadLocal<MutableMap<String, Any?>>() {

    private final const val LOG_REQUEST_END_POINT: String = "EndPoint"

    override fun initialValue(): MutableMap<String, Any?> {
        return mutableMapOf()
    }

    fun getEndPoint(): String {
        val context = RequestContext.get()
        return context[LOG_REQUEST_END_POINT]?.toString() ?: ""
    }

    fun setEndPoint(endPoint: String) {
        val context = RequestContext.get()
        context[LOG_REQUEST_END_POINT] = endPoint
    }

    fun clear() {
        get().clear()
    }
}