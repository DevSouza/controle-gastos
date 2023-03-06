package br.com.controlefinanceiro.backend.enuns;

public enum TypeRole {
	
	GLOBAL_USER, // Role, nivel user que somente o GOD_USER pode criar/atualizar
	GLOBAL_COMPANY,// Role, nivel company que somente o GOD_USER pode criar/atualizar
	PER_COMPANY; // Um função criada por uma empresa cadastrada.
	
}
