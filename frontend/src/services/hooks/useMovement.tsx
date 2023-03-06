import { useQuery, UseQueryOptions, UseQueryResult } from "react-query";
import { http } from "../api";
import { Category } from "./useCategories";

export type Movement = {
    movementId: string;
    name: string;
    amount: number;
    dueAt: string;
    paidAt: string;
    category: Category;
}

type GetMovementsParams = {
    start: Date,
    end: Date,
}

type GetMovementsResponse = {
    balance: string; // SALDO
	revenue: string; // RECEITA
	expense: string; // DESPESA
	
	predictedBalance: string; // SALDO PREVISTO
	predictedRevenue: string; // RECEITA PREVISTA
	predictedExpense: string; // DESPESA PREVISTA	
    totalCount: number;
    movements: Movement[];
}

export async function getMovements(start: Date, end: Date): Promise<GetMovementsResponse> {
    const startStr = start.toISOString().split('T')[0];
    const endStr = end.toISOString().split('T')[0];

    const { data } = await http.get(`/movements?start=${startStr}&end=${endStr}`);

    const movements = data.movements.map((item : any) => ({
        movementId: item.movementId,
        name: item.name,
        category: item.category,
        amount: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.category.type === 'EXPENSE' ? item.amount * -1 : item.amount),
        dueAt: item.dueAt == null ? undefined : new Date(item.dueAt).toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: 'long',
            year: 'numeric'
        }),
        paidAt: item.paidAt == null ? undefined : new Date(item.paidAt).toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: 'long',
            year: 'numeric'
        }),
    }));

    return {
        balance: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(data.balance),
        expense: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(data.expense),
        revenue: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(data.revenue),
        predictedBalance: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(data.predictedBalance),
        predictedExpense: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(data.predictedExpense),
        predictedRevenue: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(data.predictedRevenue),
        movements,
        totalCount: movements.length
    };
}

export function useMovements(params: GetMovementsParams, options: UseQueryOptions | any) {
    return useQuery(['movements'], () => getMovements(params.start, params.end),{
        staleTime: 1000 * 60 * 10, // 10 minutes
        ...options
    }) as UseQueryResult<GetMovementsResponse, unknown>;
}