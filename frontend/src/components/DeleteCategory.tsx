import * as yup from "yup";
import { Button } from './Form/Button/Button';
import { SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import { toast } from 'react-toastify';
import { http } from '../services/api';
import { Dialog } from 'primereact/dialog';
import { Category, useCategories } from "../services/hooks/useCategories";
import { useEffect, useState } from "react";
import { Select } from "./Form/Select/Select";
import { AxiosError } from "axios";

type CreateCategoryModalProps = {
    category: Category | undefined;
    setCategory: Function;
    onClose: () => void;
};

type CategoryFormData = {
    toCategoryId: string
};

const categoryFormSchema = yup.object().shape({});

export const DeleteCategoryModal = ({ onClose, category, setCategory }: CreateCategoryModalProps) => {
    const [needMigration, setNeedMigration] = useState(false);
    const { data, refetch } = useCategories({});
    const categoriesFiltered = data?.categories.filter((item) => (item.categoryId !== category?.categoryId && item.type === category?.type));
    const { handleSubmit, formState, reset, control, watch } =
        useForm<CategoryFormData>({
            resolver: yupResolver(categoryFormSchema),
            defaultValues: {
                toCategoryId: ''
            }
        });
    const toCategoryId = watch('toCategoryId')
    const { errors, isSubmitting } = formState;
    const handleDeleteCategory: SubmitHandler<CategoryFormData> = async ({
        toCategoryId
    }) => {
        try {
            await http.delete(`/categories/${category?.categoryId}`, {
                data: {
                   toCategoryId 
                }
            });

            toast.success("Categoria Excluida com sucesso.");
            handleClose();
        } catch (error) {
            const err = error as AxiosError;
            const status = err.response?.status;
      
            if (status && status === 401) return;
      
            if (status && status === 409) {
              toast.error("Tipo de categoria não confere!");
            } else if (status && status >= 500) {
              toast.error(
                "Instabilidade com o servidor detectada, tente novamente mais tarde!"
              );
            } else {
              toast.error("Erro ao excluir categoria!");
            }
        }
    };

    const handleClose = () => {
        setNeedMigration(false)
        setCategory();
        onClose();
        reset();
    }
    

    useEffect(() => {
        if(category?.categoryId) {
            http.get(`/categories/${category.categoryId}/check-dependent`)
                .then(({ data }) => setNeedMigration(data.needMigration))
                .catch(() => handleClose());
        }
    }, [category]);

    return (
        <Dialog visible={!!category} onHide={() => onClose()} closable={false} header="Excluir Categoria">
            <form
                className="px-5 pt-5 sm:p-0 sm:pt-0"
                onSubmit={handleSubmit(handleDeleteCategory)}
            >
                <div className="grid grid-cols-1 gap-1 w-full max-w-md">
                    <p className="py-3">Você deseja realmente excluir a categoria <strong>{category?.name}</strong></p>


                    <p className={`text-red-500 ${!needMigration && 'hidden'}`}>Essa categoria já foi atrelada a uma movimentação, é necessario informar uma nova categoria.</p>

                    <Select
                        label="Categoria"
                        control={control}
                        options={categoriesFiltered?.map((item) => ({ label: item.name, value: item.categoryId }))}
                        render={needMigration}
                        name="toCategoryId"
                    />

                    <div className="flex justify-between mt-3">
                        <Button type="button" className="bg-transparent text-red-500 border-solid border-[2px] border-red-500" onClick={() => handleClose()}>Cancelar</Button>
                        <Button type="submit" loading={isSubmitting} disabled={needMigration && toCategoryId === ''}>
                            Excluir
                        </Button>
                    </div>
                </div>
            </form>
        </Dialog>
    );
}