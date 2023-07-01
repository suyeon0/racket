package com.racket.api.shared.vo

class CursorResult<T>(val values: List<T>, val hasNext: Boolean)