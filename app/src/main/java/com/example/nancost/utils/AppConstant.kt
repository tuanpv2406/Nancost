package com.example.nancost.utils

interface AppConstant {
    annotation class Enum {
        companion object {
            const val HAS_LOGGED_IN = "HAS_LOGGED_IN"
            const val NEW_PRICE = "NEW_PRICE"
            const val USER_UID = "USER_UID"
        }
    }
}