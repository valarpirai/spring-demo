//package com.example.demo
//
//class Test {
//}
//
//fun main(args: Array<String>) {
//    val str = "<label>\n" +
//            "Text to match:\n" +
//            "<input type=\"text\" name=\"textToMatch\" value='<a href=\"google.com?ticket_id=#Ticket Id\"'><button>Match</button>\n" +
//            "</label>"
//
//    val regex = "href=([\"'])(.*?)\\1".toRegex()
//
//    val TicketId = 1000
//    val placeHolder = "#Ticket Id"
//
//    val matches = regex.findAll(str)
//    var output = str
//    matches.iterator().forEach { matchResult: MatchResult ->
//        println(matchResult.value)
//        if (matchResult.value.contains(placeHolder)) {
//            val resolvedValue = matchResult.value.replace(placeHolder, TicketId.toString())
//            output = output.replace(matchResult.value, resolvedValue)
//        }
//    }
//    println(output)
//}