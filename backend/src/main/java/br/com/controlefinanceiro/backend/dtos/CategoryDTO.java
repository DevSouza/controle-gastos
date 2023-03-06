package br.com.controlefinanceiro.backend.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID categoryId;
    private String name;
    private TypeCategory type;
    private UUID createdBy;
    private UUID updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
