import { useEffect, useState } from 'react';
import { Button } from '../components/Form/Button/Button';
import Table from '../components/Table/Table';
import { Template } from '../components/Template/Template';
import { useAuth } from '../contexts/AuthContext';
import { http } from '../services/api';

type Summary = {
    expense?: string;
    revenue?: string;
    result?: string;
}

export function Dashboard() {
    const [summary, setSummary] = useState<Summary>({})

    useEffect(() => {
        findSummary()
    }, []);
    const findSummary = async  () => {
        const result = await http.get('/movements/summary');
        setSummary({
            expense: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(result.data.expense * -1),
            revenue: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(result.data.revenue),
            result: new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(result.data.result),
        })
    }
    
    return (
        <Template>
            <div className="px-2 sm:px-0 grid grid-flow-row-dense grid-cols-12 gap-3">
                <div className="p-3 rounded-md border-gray-200 border-2 col-span-12">
                    <h3 className="text-gray-700 text-3xl font-medium">Resumo</h3>

                    <div className="grid sm:grid-cols-3 gap-5 sm:max-w-md">
                        <div className="grid grid-cols-1">
                            <label className="text-sm">Receita</label>
                            <strong className="text-green-500">{summary.revenue}</strong>
                        </div>

                        <div className="grid grid-cols-1">
                            <label className="text-sm">Despesa</label>
                            <strong className="text-red-500">{summary.expense}</strong>
                        </div>

                        <div className="grid grid-cols-1">
                            <label className="text-sm">Resultado</label>
                            <strong>{summary.result}</strong>
                        </div>
                    </div>
                </div>
            </div>
        </Template>
    );
}