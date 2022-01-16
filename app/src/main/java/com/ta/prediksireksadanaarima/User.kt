package com.ta.prediksireksadanaarima

/* Kotlin data/model classes that map the JSON response, we could also add Moshi
 * annotations to help the compiler with the mappings on a production app */
data class User(
    val email: String,
    val phone: String
)