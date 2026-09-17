package com.danilo.conductorexample.domain.model

data class Platform(
    val id: String,
    val name: String
) {
    init {
        require(id.isNotBlank()) { "Platform ID cannot be blank" }
        require(name.isNotBlank()) { "Platform name cannot be blank" }
    }
}
