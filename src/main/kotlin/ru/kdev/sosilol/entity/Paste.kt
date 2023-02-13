package ru.kdev.sosilol.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "pastes")
open class Paste(
    @Id
    open val id: String = "",
    open val code: String = "",
)