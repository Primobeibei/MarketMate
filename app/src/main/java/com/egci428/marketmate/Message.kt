package com.egci428.marketmate

class Message(val id: String, val timeStamp: String, val message: String, val checked: String, val price: String, val status: Boolean) {
    constructor(): this("","","","","",false)
}

class LogMessage(val id: String, val timeStamp: String, val message: String, val date: String, val uri: String) {
    constructor(): this("","","","","")
}

class BudgetMessage(val budget: Int) {
    constructor(): this(0)
}