package br.com.controlefinanceiro.backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controlefinanceiro.backend.models.EmailModel;

public interface EmailRepository extends JpaRepository<EmailModel, UUID> {

}
