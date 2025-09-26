package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {

    List<Token> findAllValidIsFalseOrRevokedIsFalseByUsuario_Id(Long id);

    Optional<Token> findByToken(String jwtToken);

}
