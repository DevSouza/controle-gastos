import { ReactNode } from "react";
import { FieldError, Controller } from "react-hook-form";
import { InputNumber as InputPrimeReact } from "primereact/inputnumber";

type InputTextProps = {
    label: string;
    name: string;
    error?: FieldError;
    option? : ReactNode;
    max?: number | undefined;
    min?: number | undefined;
    control: any;
}

const InputBase = ({ label, error, control, name, option, max, min } : InputTextProps) => {
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
                    <InputPrimeReact max={max} min={min}  value={field.value} onValueChange={(e : any) => field.onChange(e.value)} inputClassName="max-w-full" className="max-w-full" mode="decimal" minFractionDigits={2} maxFractionDigits={2} />
                )} />
            </div>
            { !!error && (
                <p className="text-red-500 text-xs italic">{ error.message }</p>
            )}
        </div>
    );
}

export const InputNumber = InputBase;