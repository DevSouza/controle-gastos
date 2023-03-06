import { Button } from './Form/Button/Button';
import { toast } from 'react-toastify';
import { http } from '../services/api';
import { Dialog } from 'primereact/dialog';
import { useEffect, useState } from "react";
import { AxiosError } from "axios";
import { Movement } from "../services/hooks/useMovement";
import { dateToString } from '../services/utils/DataUtils';

type DeleteMovementModalProps = {
    movement: Movement | undefined;
    setMovement: Function;
    onClose: () => void;
};

export const DeleteMovementModal = ({ onClose, movement, setMovement }: DeleteMovementModalProps) => {
    const [movements, setMovements] = useState<Movement[]>([]);
    const handleDelete = async (type : "ALL" | "THIS") => {
        try {
            await http.delete(`/movements/${movement?.movementId}`, {
                data: {
                   type
                }
            });

            toast.success("Movimento Excluido com sucesso.");
            handleClose();
        } catch (error) {
            const err = error as AxiosError;
            const status = err.response?.status;
      
            if (status && status === 401) return;
      
            if (status && status >= 500) {
              toast.error(
                "Instabilidade com o servidor detectada, tente novamente mais tarde!"
              );
            } else {
              toast.error("Erro ao excluir movimentação!");
            }
        }
    };

    const handleClose = () => {
        setMovement();
        onClose();
    }

    const checkDependent = async () => {
        if(movement?.movementId) {
            try {
                const response = await http.get(`/movements/${movement.movementId}/check-dependent`);
                setMovements(response.data.movements);
            } catch (_) {
                console.log('ERROR ', _);
                handleClose();
            }
        }
    }

    useEffect(() => {
        checkDependent();
    }, [movement]);

    return (
        <Dialog visible={!!movement} onHide={() => onClose()} closable={false} header="Excluir Movimento">
            <form className="px-5 pt-5 sm:p-0 sm:pt-0">
                <div className="grid grid-cols-1 gap-1 w-full max-w-md">
                    <p className="py-3">Você tem certeza que deseja excluir o movimento <strong>{movement?.name}?</strong></p>


                    {movements && (
                        <div className="shadow-md p-5 rounded-md border">
                            <h3 className="text-lg text-red-500 font-bold">Atenção</h3>
                            <p className="py-3">
                                O movimento que você está tentando excluir faz parte de uma serie de movimentos!
                            </p>

                            <table className="w-full">
                                <tbody className="grid grid-cols-1 gap-2">
                                        {movements?.map(item => (
                                            <tr className="grid grid-cols-2">
                                                <td>{item.name} {item.movementId === movement?.movementId &&(<label className="text-gray-300 italic">Selecionado</label>)}</td>
                                                
                                                <td className="text-center">
                                                    {item.paidAt ? (
                                                        <label className="bg-green-500 py-1 px-3 text-white font-bold rounded-md">Pago</label>
                                                    ) : (
                                                        dateToString(item.dueAt)
                                                    )}
                                                </td>
                                            </tr>
                                        ))}
                                </tbody>
                            </table>
                        </div>
                    )}

                    <div className="grid grid-cols-1 gap-3 mb-2">
                        {movements && (
                            <Button type="button" className="bg-transparent text-red-500 border-solid border-[2px] border-red-100 hover:border-red-500" onClick={() => handleDelete("ALL")}>Excluir Todos</Button>
                        )}
                        <Button type="button" className="bg-transparent text-red-500 border-solid border-[2px] border-red-100 hover:border-red-500" onClick={() => handleDelete("THIS")}>{(movements && movements.length > 1) ? 'Excluir apenas esse' : 'Excluir movimento'}</Button>
                        
                        <Button type="button" className="bg-transparent text-white bg-red-500" onClick={() => handleClose()}>Cancelar</Button>
                    </div>

                </div>
            </form>
        </Dialog>
    );
}