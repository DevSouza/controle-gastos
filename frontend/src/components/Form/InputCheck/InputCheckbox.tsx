interface InputTextProps extends React.DetailedHTMLProps<React.InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>{
    label: string;
    id: string;
}

export function InputCheckbox({ id, label, ...props } : InputTextProps) {
    return (
        <div className="mb-6 flex items-center">
            <input id={id} type="checkbox"/>
            <label htmlFor={id} className="ml-2 text-sm text-gray-900">{ label }</label>
        </div>
    );
}