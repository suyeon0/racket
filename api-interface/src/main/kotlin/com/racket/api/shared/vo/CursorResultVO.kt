package com.racket.api.shared.vo

class CursorResultVO<T>(val values: List<T>, val hasNext: Boolean)