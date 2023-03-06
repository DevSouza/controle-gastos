import { ChevronDownIcon, ChevronUpIcon } from '@heroicons/react/outline';
import React, { useEffect, useRef, useState } from 'react';
import { useForm, Controller, ControllerRenderProps, FieldValues } from 'react-hook-form';
import { useOutsideAlert } from '../../../hooks/useOutsideAlert';

interface Option {
    value: string;
    label: string;
}

interface Props {
    name: string;
    control: any;
    options: Option[] | undefined;
    label: string;
    render?: boolean;
    emptyMessage?: string;
}

export const Select = ({ options, name, label, render = true, control, emptyMessage = "Vazio" }: Props) => {
    const [isOpen, setIsOpen] = useState(false);
    const ref = useRef(null);

    useOutsideAlert({ 
        ref, 
        onEvent: () => {
            setIsOpen(false);
        }
    });

    const toggle = () => setIsOpen(!isOpen);
    const selectOption = (option: Option, field: ControllerRenderProps<FieldValues, string>) => {
        setIsOpen(false);
        field.onChange(option.value);
    };

    return (
        <div className={`mb-3 ${!render && 'hidden'}`} ref={ref}>
            <div className="flex justify-between items-center pb-1">
                <label className="block text-gray-900 pr-4" htmlFor={name}>
                    {label}
                </label>
            </div>

            <div>
                <Controller
                    render={({ field }) => (
                        <div className="relative">
                            <div
                                className={`grid grid-cols-[1fr_60px] pl-3 appearance-none w-full bg-white border border-gray-300 cursor-pointer min-h-[3rem] rounded leading-tight ${isOpen && 'border-green-400 border-[2px]'}`}
                                onClick={toggle}
                            >
                                <span className="flex items-center">
                                    { options && field.value && options.filter(item => item.value===field.value)[0]?.label }
                                </span>
                                <div className="pointer-events-none text-gray-700 flex justify-center items-center">
                                    <ChevronDownIcon className="w-5" />
                                </div>
                            </div>
                            {isOpen && (
                                <div className="absolute bg-white w-full shadow-md border rounded-md py-1 z-50 mb-3">
                                    {options && options.length > 0 ? (
                                            options.map(option => (
                                                <div
                                                    key={option.value}
                                                    className={`cursor-pointer p-4 ${option.value === field.value ? 'font-bold bg-green-200' : 'hover:bg-slate-100'}`}
                                                    onClick={() => selectOption(option, field)}
                                                >
                                                    {option.label}
                                                </div>
                                            ))
                                    ) : (
                                        <div className={`cursor-not-allowed p-4`}>
                                            { emptyMessage }
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>
                    )}
                    control={control}
                    name={name}
                />

                {control.getFieldState(name).error && (
                    <p className="text-red-500 text-xs italic">{control.getFieldState(name).error?.message}</p>
                )}
            </div>
        </div>
    );
}
