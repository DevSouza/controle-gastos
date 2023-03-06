package br.com.controlefinanceiro.backend.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.controlefinanceiro.backend.dtos.MovementDTO;
import br.com.controlefinanceiro.backend.enuns.TypeCategory;
import br.com.controlefinanceiro.backend.models.MovementModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementGetResponseBody {
    
	private BigDecimal balance; // SALDO
	private BigDecimal revenue; // RECEITA
	private BigDecimal expense; // DESPESA
	
	private BigDecimal predictedBalance; // SALDO PREVISTO
	private BigDecimal predictedRevenue; // RECEITA PREVISTA
	private BigDecimal predictedExpense; // DESPESA PREVISTA	
//	private BigDecimal previousBalance; // BALANCO PREVIO
	
    private List<MovementDTO> movements;
    
    public static MovementGetResponseBody toMovementGetResponseBody(List<MovementModel> movements) {
    	BigDecimal balance = BigDecimal.ZERO;
    	BigDecimal revenue = BigDecimal.ZERO;
    	BigDecimal expense = BigDecimal.ZERO;
    	
    	BigDecimal predictedBalance = BigDecimal.ZERO; // SALDO PREVISTO
    	BigDecimal predictedRevenue = BigDecimal.ZERO; // RECEITA PREVISTA
    	BigDecimal predictedExpense = BigDecimal.ZERO; // DESPESA PREVISTA
    	
    	List<MovementDTO> movementsDTO = new ArrayList<>();
    	for (MovementModel movement : movements) {
			var dto = movement.toMovementDTO();
			revenue = dto.getCategory().getType().equals(TypeCategory.REVENUE) && dto.getPaidAt() != null ? revenue.add(dto.getAmount()): revenue;
			expense = dto.getCategory().getType().equals(TypeCategory.EXPENSE) && dto.getPaidAt() != null ? expense.add(dto.getAmount()): expense;
			
			predictedRevenue = dto.getCategory().getType().equals(TypeCategory.REVENUE) ? predictedRevenue.add(dto.getAmount()): predictedRevenue;
			predictedExpense = dto.getCategory().getType().equals(TypeCategory.EXPENSE) ? predictedExpense.add(dto.getAmount()): predictedExpense;
			
			
			if(dto.getCategory().getType().equals(TypeCategory.REVENUE) && dto.getPaidAt() != null) {
				balance = balance.add(dto.getAmount());
			} else if(dto.getCategory().getType().equals(TypeCategory.EXPENSE) && dto.getPaidAt() != null) {
				balance = balance.subtract(dto.getAmount());
			}
			
			if(dto.getCategory().getType().equals(TypeCategory.REVENUE)) {
				predictedBalance = predictedBalance.add(dto.getAmount());
			} else if(dto.getCategory().getType().equals(TypeCategory.EXPENSE)) {
				predictedBalance = predictedBalance.subtract(dto.getAmount());
			}
			
			movementsDTO.add(dto);
		}
    	
    	var data = MovementGetResponseBody.builder()
    			.movements(movementsDTO)
    			.balance(balance)
    			.revenue(revenue)
    			.expense(expense.multiply(BigDecimal.valueOf(-1)))

    			.predictedBalance(predictedBalance)
    			.predictedRevenue(predictedRevenue)
    			.predictedExpense(predictedExpense.multiply(BigDecimal.valueOf(-1)))
    			.build();
    	
    	return data;
    }
    
}
