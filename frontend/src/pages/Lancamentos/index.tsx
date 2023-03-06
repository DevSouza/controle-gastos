import { Template } from "../../components/Template/Template";
import { useEffect, useState } from "react";
import { Button } from "../../components/Form/Button/Button";

import { Movement, useMovements } from '../../services/hooks/useMovement';
import { CreateMovementModal } from '../../components/CreateMovement';
import { Calendar } from 'primereact/calendar';
import { ThumbUpIcon, ThumbDownIcon } from '@heroicons/react/solid';
import { http } from '../../services/api';
import { toast } from 'react-toastify';
import { TrashIcon } from "@heroicons/react/outline";
import { DeleteMovementModal } from "../../components/DeleteMovement";

export function Lancamentos() {
    const [date, setDate] = useState(new Date()); 
    const start = new Date(date.getFullYear(), date.getMonth(), 1);
    const end = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    const { data, refetch } = useMovements({ start, end }, {});

    const [movementDelete, setMovementDelete] = useState<Movement>();

    useEffect(() => {
        refetch()
    }, [date]);

    const [isOpen, setIsOpen] = useState(false);
    const handleClose = () => {
        setIsOpen(false);
        refetch();
    }

    const handlePayOrCancelPay= async ( item: Movement ) => {
        try {
            if(item.paidAt === undefined) {
                const response = await http.patch(`/movements/${item.movementId}/pay`);
                toast.success("Pagamento Realizado.");
                
            } else {
                const response = await http.patch(`/movements/${item.movementId}/cancel-pay`);
                toast.success("Cancelamento Realizado.");
            }
            
            refetch();
        } catch (err) {
            toast.error("Erro ao cancelar/realizar pagamento.");
        }
    }

    return (
        <Template title="Lançamentos">

            <main className="h-full sm:col-span-3">
                <div className="grid grid-cols-2 sm:grid-cols-3 justify-center items-center mb-10">
                    <div>
                        <Button onClick={() => setIsOpen(true)}>Novo Movimento</Button>
                    </div>

                    <div className="flex items-center justify-center">
                        <Calendar view="month" dateFormat="MM/yy" value={date} inputClassName="text-center" locale="pt" onChange={(e) => setDate(e.value as Date)}/>
                    </div>
                </div>

                <CreateMovementModal isOpen={isOpen} onClose={() => handleClose()} />
            
                <DeleteMovementModal movement={movementDelete} onClose={() => handleClose()} setMovement={setMovementDelete} />

                <div className="w-full bg-white shadow-sm rounded-md p-3">
                    <table className="w-full">
                        <tbody className="grid grid-cols-1 items-center justify-center">
                            {data && data.totalCount > 0 ? (data?.movements.map(item => (
                                    <tr key={item.movementId} className="grid grid-cols-1 sm:grid-cols-3 p-3 rounded-md hover:bg-slate-200">
                                        <td className="font-medium">{item.name}</td>
                                        <td className="hidden sm:block text-slate-500">{item.category.name}</td>
                                        <td className="flex gap-5 justify-between sm:justify-end">
                                            {item.amount}

                                            <div className="flex gap-5 ">
                                                <a type='button' className="cursor-pointer" onClick={() => handlePayOrCancelPay(item)}>
                                                    {item.paidAt === undefined ? (
                                                        <ThumbDownIcon className="w-5" />
                                                    ) : (
                                                        <ThumbUpIcon className="w-5 fill-green-500" />
                                                    )}
                                                </a>

                                                <a type='button' className="cursor-pointer" onClick={() => setMovementDelete(item)}>
                                                    <TrashIcon className="w-5 text-red-500" />
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                ))) : (
                                    <tr className="grid grid-cols-2 sm:grid-cols-3 p-3 rounded-md hover:bg-slate-200">
                                        <td>
                                            Nenhuma movimentação
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>

                <div className="flex flex-col items-end my-5 text-sm">
                    <div className="w-full md:w-80 grid grid-cols-2 gap-3 border-y-2 py-3">
                        <span>Receita Realizada</span>
                        <span className="text-right text-green-500">{data?.revenue}</span>
                        
                        <span>Receita Prevista</span>
                        <span className="text-right text-gray-400">{data?.predictedRevenue}</span>
                        
                        <span>Despesa Realizada</span>
                        <span className="text-right text-red-500">{data?.expense}</span>
                        
                        <span>Despesa Prevista</span>
                        <span className="text-right text-gray-400">{data?.predictedExpense}</span>
                    </div>
                    <div className="w-full md:w-80 grid grid-cols-2 gap-3 py-3">
                        <span>Saldo</span>
                        <span className="text-right text-blue-500">{data?.balance}</span>
                        
                        <span>Previsto</span>
                        <span className="text-right text-gray-400">{data?.predictedBalance}</span>
                    </div>
                </div>
            </main>
        </Template>
    );
}