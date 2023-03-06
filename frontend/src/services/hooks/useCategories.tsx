import { useQuery, UseQueryOptions, UseQueryResult } from "react-query";
import { http } from "../api";

export type Category = {
    categoryId: string;
    name: string;
    type: 'EXPENSE' | 'REVENUE';
    limitMaxPercentage: number;
    limitMaxValue: number;
    createdAt: string;
    createdBy: string;
}

type GetCategoriesResponse = {
    totalCount: number;
    categories: Category[];
}

export async function getCategories(): Promise<GetCategoriesResponse> {
    const { data } = await http.get('/categories');

    const categories = data.map((category : any) => ({
        categoryId: category.categoryId,
        name: category.name,
        type: category.type,
        createdAt: new Date(category.createdAt).toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: 'long',
            year: 'numeric'
        }),
        limitMaxPercentage: category.limitMaxPercentage !== 0 ? category.limitMaxPercentage : undefined,
        limitMaxValue: category.limitMaxValue === undefined || category.limitMaxValue === 0 ? undefined : category.limitMaxValue,
    }));

    return {
        categories,
        totalCount: categories.length
    };
}

export function useCategories(options: UseQueryOptions | any) {
    return useQuery(['categories'], () => getCategories(),{
        staleTime: 1000 * 60 * 10, // 10 minutes
        ...options
    }) as UseQueryResult<GetCategoriesResponse, unknown>;
}