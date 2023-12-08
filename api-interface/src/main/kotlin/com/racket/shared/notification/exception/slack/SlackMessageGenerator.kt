package com.racket.shared.notification.exception.slack

import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors.joining
import kotlin.collections.HashMap

@Component
class SlackMessageGenerator(
) {

    companion object {
        private const val EMPTY_BODY_MESSAGE = "{BODY IS EMPTY}"
        private const val EXCEPTION_MESSAGE_FORMAT = "%s.%s:%d - %s"
    }

    fun generate(request: ContentCachingRequestWrapper, exception: Exception): String {
        return try {
            val currentTime = LocalDateTime.now().toString()
            val exceptionMessage = this.extractExceptionMessage(exception)

            val requestURI = request.requestURI
            val method = request.method

            val requestIP = request.remoteAddr
            val userAgent = request.getHeader("User-Agent")

            val headers = this.extractHeaders(request)
            val body = this.getBody(request)

            this.makeSlackMessage(
                currentTime = currentTime,
                exceptionMessage = exceptionMessage,
                requestURI = requestURI,
                method = method,
                requestIP = requestIP,
                userAgent = userAgent,
                headers = headers,
                body = body
            )
        } catch (e: Exception) {
            throw RuntimeException("슬랙 메세지 추출 오류!", e)
        }
    }

    private fun makeSlackMessage(
        currentTime: String,
        exceptionMessage: String,
        requestURI: String,
        requestIP: String,
        userAgent: String,
        method: String,
        headers: String,
        body: String
    ): String {
        return "${currentTime}\n" +
            "*Error*\n $exceptionMessage\n" +
            "*Request URL*\n $requestURI\n" +
            "*Request METHOD*\n $method\n" +
            "*Request IP*\n $requestIP\n" +
            "*Request User-Agent*\n $userAgent\n" +
            "*Request HEADERS*\n $headers\n" +
            "*Request BODY*\n $body\n"
    }

    private fun extractExceptionMessage(e: Exception): String {
        val stackTrace = e.stackTrace[0]
        val className = stackTrace.className
        val lineNumber = stackTrace.lineNumber
        val methodName = stackTrace.methodName
        val message = e.message

        return if (Objects.isNull(message)) {
            Arrays.stream(e.stackTrace)
                .map(StackTraceElement::toString)
                .collect(joining("\n"));
        } else {
            String.format(EXCEPTION_MESSAGE_FORMAT, className, methodName, lineNumber, message);
        }
    }

    private fun extractHeaders(request: ContentCachingRequestWrapper): String {
        val headerNames = request.headerNames

        val map: MutableMap<String, String> = HashMap()
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            map[headerName] = request.getHeader(headerName)
        }
        return map.entries.joinToString { (key, value): Map.Entry<String, String> -> "$key:$value" }
    }

    private fun getBody(request: ContentCachingRequestWrapper): String {
        val body = String(request.contentAsByteArray)
        return body.ifEmpty {
            EMPTY_BODY_MESSAGE
        }
    }


}