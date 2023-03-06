import { ArrowLeftIcon } from "@heroicons/react/outline";
import { Link } from "react-router-dom";

export function NotFound() {
    return (
        <div className="h-screen w-screen flex items-center justify-center flex-col gap-3">
            <h2 className="text-gray-900 text-5xl text-center">404</h2>
            <h1 className="text-gray-600 text-3xl text-center">Pagina não encontrada</h1>
            <Link to="/" className="text-blue-500 text-2xl hover:underline animate-pulse flex">
                <ArrowLeftIcon className="w-8 h-8 animate"/>
                Voltar
            </Link>
        </div>
    );
}