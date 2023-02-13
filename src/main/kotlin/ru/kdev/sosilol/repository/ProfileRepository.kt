package ru.kdev.sosilol.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.kdev.sosilol.entity.Profile

@Repository
@Transactional
interface ProfileRepository : CrudRepository<Profile, Long>