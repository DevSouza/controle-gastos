import { forwardRef, ForwardRefRenderFunction, ReactNode } from "react";
import { FieldError } from "react-hook-form";
import { Link } from "react-router-dom";

interface InputTextProps extends React.DetailedHTMLProps<React.InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>{
    label: string;
    type: "password" | "text" | "email";
    name: string;
    error?: FieldError;
    option? : ReactNode;
    control: any;
}

const InputBase: ForwardRefRenderFunction<HTMLInputElement, InputTextProps> = ({ id, label, type, error,control,name, option,...props} : InputTextProps, ref) => {
    
    

    return (
        <div className="mb-3">
            <div className="flex justify-between items-center pb-1">
                <label className="block text-gray-900 pr-4" htmlFor={id}>
                    {label}
                </label>

                { option }
            </div>
            <div>
                
                <input 
                    className="text-lg appearance-none border-[1px] border-gray-400 rounded-md w-full py-3 px-4 text-gray-900 leading-tight focus:outline-none placeholder-focus-move focus:border-green-500"
                    id={id}
                    type={type}
                    ref={ref}
                    {...control.register(name)}
                    {...props} />
            </div>
            { !!error && (
                <p className="text-red-500 text-xs italic">{ error.message }</p>
            )}
        </div>
    );
}

export const InputText = forwardRef(InputBase);