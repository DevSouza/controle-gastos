package br.com.controlefinanceiro.backend.requests;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoryDeleteRequestBody {
    
    private UUID toCategoryId;
}
