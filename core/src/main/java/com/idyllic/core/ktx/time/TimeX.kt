package com.idyllic.core.ktx.time

import java.util.*


inline val Calendar.date: Date
    get() = Date(this.timeInMillis)

inline val Date.calendar: Calendar
    get() = Calendar.getInstance().apply { time = this@calendar }

