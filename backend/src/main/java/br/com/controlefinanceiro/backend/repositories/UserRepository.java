package br.com.controlefinanceiro.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controlefinanceiro.backend.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
//    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "from UserModel where lower(username) = lower(:username)")
    Optional<UserModel> findByUsername(@Param("username") String username);
    
    @Query(value = "from UserModel where lower(email) = lower(:email)")
    Optional<UserModel> findByEmail(@Param("email") String email);
    
//    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findById(UUID userId);
}
