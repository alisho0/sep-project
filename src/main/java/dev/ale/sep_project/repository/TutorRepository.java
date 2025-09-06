package dev.ale.sep_project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Tutor;

public interface TutorRepository extends CrudRepository<Tutor, Long> {
    public Optional<Tutor> findByDni(String dni);
}
