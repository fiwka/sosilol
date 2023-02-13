package ru.kdev.sosilol.entity

import jakarta.persistence.*

@Entity
@Table(name = "profiles")
open class Profile(
    @Id
    open val id: Long = 0,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = true)
    open val pastes: MutableList<Paste> = arrayListOf()
)