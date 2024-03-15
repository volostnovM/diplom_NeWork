package ru.netology.neworknetology.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Formatter {
    fun formatPostDate(input: String): String {
        val postDate = ZonedDateTime.parse(input, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault())
        val fullDate = DateTimeFormatter.ofPattern("dd MMMM yyyy в HH:mm")
            .format(postDate)
        val formattedDate = DateTimeFormatter.ofPattern("d MMMM в HH:mm")
            .format(postDate)
        val formattedMinutes = DateTimeFormatter.ofPattern("в HH:mm")
            .format(postDate)
        val currentTime = LocalDateTime.now()
        when (currentTime.year - postDate.year) {
            0 -> {
                return when (currentTime.dayOfMonth - postDate.dayOfMonth) {
                    0 -> {
                        "Сегодня $formattedMinutes"
                    }

                    1 -> {
                        "Вчера $formattedMinutes"
                    }

                    else -> {
                        formattedDate
                    }
                }
            }

            else -> {
                return fullDate
            }

        }
    }


    fun formatCount(count: Int): String? {
        when (count) {
            in 0..999 -> return count.toString()
            in 1000..9999 -> {
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                val result = df.format(count / 1000.0)
                return result.toString() + "K"
            }

            in 10000..999999 -> {
                val df = DecimalFormat("#")
                df.roundingMode = RoundingMode.DOWN
                val result = df.format(count / 1000.0)
                return result.toString() + "K"
            }

            in 1000000..999999999 -> {
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                val result = df.format(count / 1000000.0)
                return result.toString() + "M"
            }
        }
        return null
    }

    fun formatEventDate(input: String): String {
        val eventDate = ZonedDateTime.parse(input, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val fullDate = DateTimeFormatter.ofPattern("dd MMMM yyyy в HH:mm")
            .format(eventDate)
        val formattedDate = DateTimeFormatter.ofPattern("d MMMM в HH:mm")
            .format(eventDate)
        val currentTime = LocalDateTime.now()
        return when (currentTime.year - eventDate.year) {
            0 -> {
                formattedDate
            }

            else -> {
                return fullDate
            }

        }
    }
}