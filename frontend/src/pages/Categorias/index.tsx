import Table from "../../components/Table/Table";

import { Template } from "../../components/Template/Template";
import { useState } from "react";
import { CreateCategoryModal } from "../../components/CreateCategory";
import { Button } from "../../components/Form/Button/Button";

import { Category, useCategories } from "../../services/hooks/useCategories";
import { PlusIcon, TrashIcon } from "@heroicons/react/outline";
import { DeleteCategoryModal } from "../../components/DeleteCategory";
import { PencilAltIcon } from "@heroicons/react/solid";

const wait = () => new Promise((resolve) => setTimeout(resolve, 1000));

export function Categorias() {
    const [isOpen, setIsOpen] = useState(false);
    const { data, refetch } = useCategories({});
    const listReceita = data?.categories.filter(item => item.type === "REVENUE");
    const listDespesa = data?.categories.filter(item => item.type === "EXPENSE");

    const [category, setCategory] = useState<Category>();
    const [categoryDelete, setCategoryDelete] = useState<Category>();
    
    const handleOnClose = async () => {
        setIsOpen(false);
        refetch();
    }

    return (
        <Template title="Categorias">
            <main className="h-full sm:col-span-3">
                <Button className="mb-3 bg-green-400 text-white" onClick={() => setIsOpen(true)}><PlusIcon className='w-5 h-5'/>Nova Categoria</Button>
                <CreateCategoryModal isOpen={isOpen} category={category} setCategory={setCategory} onClose={handleOnClose}/>

                <DeleteCategoryModal category={categoryDelete} setCategory={setCategoryDelete} onClose={handleOnClose} />

                <div className="w-full bg-white shadow-sm rounded-md p-3">
                    <table className="w-full">
                        <thead>
                            <tr className="grid grid-cols-1 sm:grid-cols-4 p-3 rounded-md">
                                <th></th>
                                <th className="hidden sm:block">%</th>
                                <th className="hidden sm:block">$</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody className="grid grid-cols-1 items-center justify-center">
                            
                            <tr className="p-3 rounded-md text-xl text-green-500 font-bold">
                                Receita (+)
                            </tr>
                            {listReceita && listReceita.length > 0 ? (listReceita.map(item => (
                                    <tr key={item.categoryId} className="grid grid-cols-2 sm:grid-cols-4 p-3 rounded-md hover:bg-slate-200">
                                        <td className="font-medium">{item.name}</td>
                                        <td className="hidden sm:block text-slate-500 text-center">{item.limitMaxPercentage}</td>
                                        <td className="hidden sm:block text-slate-500 text-center">
                                            {item.limitMaxValue}
                                        </td>
                                        <td className="flex gap-5 justify-end">
                                            <div className="flex gap-10">
                                                <a type='button' className="cursor-pointer" onClick={() => {
                                                        setCategory(item);
                                                        setIsOpen(true);
                                                    }}>
                                                    <PencilAltIcon className="w-5 fill-green-500" />
                                                </a>

                                                <a type='button' className="cursor-pointer" onClick={() => setCategoryDelete(item)}>
                                                    <TrashIcon className="w-5 text-red-500" />
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                ))) : (
                                    <tr className="grid grid-cols-2 sm:grid-cols-3 p-3 rounded-md hover:bg-slate-200">
                                        <td>
                                            Nenhuma Receita
                                        </td>
                                    </tr>
                                )
                            }

                            <tr className="p-3 rounded-md text-xl text-red-500 font-bold mt-3">
                                Despesa (+)
                            </tr>

                            {listDespesa && listDespesa.length > 0 ? (listDespesa.map(item => (
                                    <tr key={item.categoryId} className="grid grid-cols-2 sm:grid-cols-4 p-3 rounded-md hover:bg-slate-200">
                                        <td className="font-medium">{item.name}</td>
                                        <td className="hidden sm:block text-slate-500 text-center">{item.limitMaxPercentage}</td>
                                        <td className="hidden sm:block text-slate-500 text-center">
                                            {item.limitMaxValue}
                                        </td>
                                        <td className="flex gap-5 justify-end">
                                            <div className="flex gap-10">
                                                <a type='button' className="cursor-pointer" onClick={() => {
                                                        setCategory(item);
                                                        setIsOpen(true);
                                                    }}>
                                                    <PencilAltIcon className="w-5 fill-green-500" />
                                                </a>

                                                <a type='button' className="cursor-pointer" onClick={() => setCategoryDelete(item)}>
                                                    <TrashIcon className="w-5 text-red-500" />
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                ))) : (
                                    <tr className="grid grid-cols-2 sm:grid-cols-3 p-3 rounded-md hover:bg-slate-200">
                                        <td>
                                            Nenhuma Despesa
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </main>
        </Template>
    );
}