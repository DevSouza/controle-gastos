import * as yup from "yup";
import { useAuth } from '../contexts/AuthContext';
import { Button } from './Form/Button/Button';
import { InputText } from './Form/InputText/InputText';
import { SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import { toast } from 'react-toastify';
import { http } from '../services/api';
import { Dialog } from 'primereact/dialog';
import { InputNumber } from "./Form/InputNumber/InputNumber";
import { Category } from "../services/hooks/useCategories";
import { useEffect } from "react";
import { Select } from "./Form/Select/Select";

type CreateCategoryModalProps = {
    isOpen: boolean;
    category: Category | undefined;
    setCategory: Function;
    onClose: () => void;
};

type CategoryFormData = {
    categoryId: string,
    name: string;
    createdBy: string;
    type: string;
    limitMaxPercentage: number;
    limitMaxValue: number;
};

const categoryFormSchema = yup.object().shape({
    name: yup
        .string()
        .required("Nome é obrigatório")
        .min(4, "Nome deve conter no mínimo quatro caracteres")
        .max(60, "Nome deve conter no máximo sessenta caracteres"),
    
    type: yup
        .string()
        .required("Tipo é obrigatório"),

});

const tipos = [
    { label: 'Receita (+)', value: 'REVENUE' },
    { label: 'Despesa (-)', value: 'EXPENSE' }
]

export const CreateCategoryModal = ({ isOpen, onClose, category, setCategory }: CreateCategoryModalProps) => {
    const { user } = useAuth();
    const { handleSubmit, formState, reset, control, watch, setValue} =
        useForm<CategoryFormData>({
            resolver: yupResolver(categoryFormSchema),
            defaultValues: {
                name: '',
                type: 'EXPENSE',
                limitMaxPercentage: 0.00,
                limitMaxValue: 0.00,
            }
        });
    const { errors, isSubmitting } = formState;
    const typeCategory = watch('type');

    const handleCreateCategory: SubmitHandler<CategoryFormData> = async ({
        categoryId, name, type, limitMaxPercentage, limitMaxValue
    }) => {
        try {
            if(categoryId) {
                await http.put(`/categories/${categoryId}`,
                    {
                        name,
                        limitMaxPercentage,
                        limitMaxValue,
                    }
                );
                toast.success("Categoria Atualizada com sucesso.");
            } else {
                await http.post(`/categories`,
                    {
                        name,
                        userId: user?.userId,
                        type,
                        limitMaxPercentage,
                        limitMaxValue,
                    }
                );
                toast.success("Categoria Adicionada com sucesso.");
            }

            handleClose();
        } catch (err) {
            toast.error("Erro ao cadastrar categoria.");
        }
    };

    const handleClose = () => {
        setCategory();
        onClose();
        reset();
    }
    

    useEffect(() => {
        if(category !== undefined){
            const fields = Object.getOwnPropertyNames(category) as ("name" | "type" | "limitMaxPercentage" | "limitMaxValue" | "createdBy")[];
            fields.forEach(item => setValue(item, category[item]));
        }
    }, [category]);

    return (
        <Dialog visible={isOpen} onHide={() => onClose()} closable={false} header="Nova Categoria">
            <form
                className="px-5 pt-5 sm:p-0 sm:pt-0"
                onSubmit={handleSubmit(handleCreateCategory)}
            >
                <div className="grid grid-cols-1 gap-1 w-full max-w-md">
                    <Select
                        render={!category?.categoryId}
                        control={control}
                        name="type"
                        
                        label="type"
                        options={tipos}
                    />

                    <InputText
                        label="Nome"
                        placeholder="Mercado, Farmacia, Passeio..."
                        id="name"
                        type="text"
                        error={errors.name}
                        name="name"
                        control={control}
                    />

                    <div className={`grid grid-cols-2 gap-3 ${typeCategory === 'REVENUE' && 'hidden'}`}>
                        <InputNumber
                            label="Limite %"
                            error={errors.limitMaxPercentage}
                            name="limitMaxPercentage"
                            control={control}
                            max={100}
                            min={0} />

                        <InputNumber
                            label="Valor maximo"
                            error={errors.limitMaxValue}
                            name="limitMaxValue"
                            control={control}
                            min={0} />
                    </div>

                    <div className="flex justify-between mt-3">
                        <Button type="button" className="bg-transparent text-red-500 border-solid border-[2px] border-red-500" onClick={() => handleClose()}>Cancelar</Button>
                        <Button type="submit" loading={isSubmitting}>
                            Salvar Categoria
                        </Button>
                    </div>
                </div>
            </form>
        </Dialog>
    );
}