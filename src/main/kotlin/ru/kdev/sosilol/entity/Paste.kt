package ru.kdev.sosilol.entity

import jakarta.persistence.*

@Entity
@Table(name = "pastes")
open class Paste(
    @Id
    open val id: String = "",
    @Column(columnDefinition="TEXT")
    open val code: String = "",
)