import { ReactNode } from "react";
import { FieldError, Controller } from "react-hook-form";
import { Calendar } from "primereact/calendar";
import { DeepRequired, FieldErrorsImpl, Merge } from "react-hook-form/dist/types";

type InputTextProps = {
    label: string;
    name: string;
    error?: Merge<FieldError, FieldErrorsImpl<DeepRequired<Date>>>;
    option? : ReactNode;
    control: any;
}

const InputBase = ({ label, error, control, name, option} : InputTextProps) => {
    return (
        <div className="mb-3">
            <div className="flex justify-between items-center pb-1">
                <label className="block text-gray-900 pr-4" htmlFor={name}>
                    {label}
                </label>

                { option }
            </div>
            <div>
                <Controller name={name} control={control} render={({ field }) => (
                    <Calendar 
                        id={field.name}
                        value={field.value}
                        onChange={(e) => field.onChange(e.value)} 
                        dateFormat="dd/mm/yy"
                        mask="99/99/9999"
                        locale="pt"
                    />
                )} />
            </div>
            { !!error && (
                <p className="text-red-500 text-xs italic">{ error.message }</p>
            )}
        </div>
    );
}

export const InputCalendar = InputBase;