import * as yup from "yup";
import { SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from "./Form/Button/Button";
import { InputText } from "./Form/InputText/InputText";
import { useCategories } from "../services/hooks/useCategories";

import { Dialog } from 'primereact/dialog';
import { InputSwitch } from 'primereact/inputswitch';
import { Controller } from "react-hook-form";
import { InputCalendar } from "./Form/InputCalendar/InputCalendar";
import { http } from "../services/api";
import { useAuth } from "../contexts/AuthContext";
import { toast } from "react-toastify";
import { InputNumber } from "./Form/InputNumber/InputNumber";
import { useEffect, useState } from "react";
import { Select } from "./Form/Select/Select";

type CreateCategoryModalProps = {
    isOpen: boolean;
    onClose: () => void;
};

type MovementFormData = {
    name: string;
    dueAt: Date;
    userId: string;
    categoryId: string;
    frequency?: string;
    repeat: boolean;
    installments: number;
    amount: number;
};

const frequencies = [
    { label: 'Diario', value: 'DAILY' },
    { label: 'Semanal', value: 'WEEKLY' },
    { label: 'Mensal', value: 'MONTHLY' },
    { label: 'Anual', value: 'YEARLY' },
]

const categoryFormSchema = yup.object().shape({
    name: yup
        .string()
        .required("Nome é obrigatório")
        .min(4, "Nome deve conter no mínimo quatro caracteres")
        .max(60, "Nome deve conter no máximo sessenta caracteres"),
    dueAt: yup
        .string()
        .required("Vencimento é obrigatório"),
    categoryId: yup
        .string()
        .required("Categoria é obrigatório"),
    amount: yup
    .number()
    .moreThan(0, 'Valor tem que ser maior que zero!')
    .required("Valor é obrigatório"),
});

export const CreateMovementModal = ({ isOpen, onClose }: CreateCategoryModalProps) => {
    const { user } = useAuth();
    const [typeFilter, setTypeFilter] = useState('EXPENSE');
    const { data } = useCategories({});
    const categories = data?.categories.filter((item) => item.type === typeFilter);
    
    const { handleSubmit, formState, reset, control, watch, setValue } = useForm<MovementFormData>({
        defaultValues: {
            repeat: false,
            amount: 0.00,
            installments: 1,
            name: '',
        },
        resolver: yupResolver(categoryFormSchema)
    });
    const { errors, isSubmitting } = formState;

    useEffect(() => setValue('categoryId', ''), [typeFilter]);
    
    const repeat = watch('repeat');

    const handleCreateMovement: SubmitHandler<MovementFormData> = async (data) => {
        try {
            await http.post('/movements', {
                userId: user?.userId,
                name: data.name,
                dueAt: new Date(data.dueAt).toISOString().split('T')[0],
                categoryId: data.categoryId,
                fixed: false,
                frequency: data.frequency,
                installments: data.installments,
                amount: data.amount
            });

            toast.success("Movimentação Adicionada com sucesso.");
            handleClose();
        } catch (err) {
            toast.error("Erro ao cadastrar movimentação.");
            console.log(err);
        }
    };

    const handleClose = () => {
        reset();
        onClose();
    }

    return (
        <Dialog visible={isOpen} onHide={() => onClose()} closable={false} header={typeFilter === 'EXPENSE' ? 'Nova Despesa' : 'Nova Receita'}>

            <form className="grid grid-cols-1 gap-1 w-full max-w-md" onSubmit={handleSubmit(handleCreateMovement)}>
                <div className="grid grid-cols-2 gap-3">
                    <InputNumber
                        label="Valor"
                        error={errors.amount}
                        name="amount"
                        control={control} />

                    <InputCalendar
                        label={repeat ? 'Primeiro Vencimento' : 'Vencimento'}
                        error={errors.dueAt}
                        name="dueAt"
                        control={control}
                    />

                </div>

                <InputText
                    label="Nome"
                    placeholder="Nome"
                    id="name"
                    type="text"
                    error={errors.name}
                    name="name"
                    control={control}
                />

                <div className="grid grid-cols-2 gap-3">
                    <Button 
                        type="button"
                        className={`${typeFilter === 'EXPENSE' ? 'bg-red-500 text-white':'bg-transparent text-red-500'} border-red-500 border-2 py-5`}
                        onClick={() => setTypeFilter('EXPENSE')}>
                        Despesa (-)
                    </Button>
                    <Button 
                        type="button"
                        className={`${typeFilter === 'REVENUE' ? 'bg-green-500 text-white':'bg-transparent text-green-500'} border-green-500 border-2 py-5`}
                        onClick={() => setTypeFilter('REVENUE')}>
                            Receita (+)
                    </Button>
                </div>

                <Select
                    label="Categoria"
                    control={control}
                    options={categories?.map(item => ({ label: item.name, value: item.categoryId }))}
                    name="categoryId"
                />

                <div className="flex items-center gap-3 mb-2">
                    <Controller name="repeat" control={control} render={({ field, fieldState }) => (
                        <InputSwitch id={field.name} checked={field.value} onChange={(e) => field.onChange(e.value)} />
                    )} />
                    <label className="font-semibold">Repetir?</label>
                </div>

                <div className={`grid grid-cols-2 gap-3 ${repeat ? '' : 'hidden'}`}>
                    <Select
                        label="Frequencia"
                        name="frequency"
                        control={control}
                        options={frequencies}
                    />

                    <InputText
                        label="Ocorrencias"
                        placeholder="Ocorrencias"
                        id="installments"
                        type="text"
                        error={errors.installments}
                        name="installments"
                        control={control}
                    />
                </div>
                <div className="flex justify-between mt-3">
                    <Button type="button" className="bg-transparent text-red-500 border-solid border-[2px] border-red-500" onClick={() => handleClose()}>Cancelar</Button>
                    <Button>Salvar</Button>
                </div>
            </form>
        </Dialog>
    );
}