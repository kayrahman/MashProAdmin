package com.nkr.bazarano.util

import android.util.Log
import java.security.SecureRandom
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Helperutils {

    companion object {
        fun formattedDateTime(dateTime: String?): String {
            var date = "29/05/19"

            try {

                var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                format.timeZone = TimeZone.getTimeZone("Etc/UTC")
                val newDate = format.parse(dateTime)

                format = SimpleDateFormat("h:mma  d MMM yy")
                date = format.format(newDate)

                return date

            } catch (e: ParseException) {
                e.printStackTrace()
                return "29/05/2019"
            }


        }
        fun formattedDate(dateTime: String?): String {

            var date: String

            //var format = SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z")
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("Etc/UTC")
            val newDate = format.parse(dateTime)

            format = SimpleDateFormat("d MMM yy")
            date = format.format(newDate)

            Log.d("Formatted_time", date)

            return date
        }
        fun formattedTime(dateTime: String?): String {


            //var format = SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z")
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("Etc/UTC")
            val newDate = format.parse(dateTime)

            format = SimpleDateFormat("h:mma")
            var date = format.format(newDate)

            Log.d("Formatted_time", date)

            return date


        }
        fun capitalizeFirstLetters(name: String): String {

            val strArray = name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val builder = StringBuilder()
            for (s in strArray) {
                val cap = s.substring(0, 1).toUpperCase() + s.substring(1)
                builder.append("$cap ")
            }

            return builder.toString()
        }
        fun getCalendarTime(): String {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z")
            format.timeZone = cal.timeZone
            return format.format(cal.time)
        }
        fun generateOrderCode(codeLength: Int): String? {
            val chars =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray()
            val sb = java.lang.StringBuilder()
            val random: Random = SecureRandom()
            for (i in 0 until codeLength) {
                val c = chars[random.nextInt(chars.size)]
                sb.append(c)
            }
            val output = sb.toString()
            println(output)
            return output
        }

    }

}