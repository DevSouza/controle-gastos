import { UserIcon, LockClosedIcon } from "@heroicons/react/outline";
import { Link, Route, Routes } from "react-router-dom";
import { Template } from "../../components/Template/Template";
import { Security } from "./Security";
import { Account } from "./Account";

export function Settings() {
    return (
        <Template title="Configurações da conta">
            <div className="grid grid-cols-1 sm:grid-cols-4">
                <div className="col-span-1 flex flex-col items-start gap-3 px-5 sm:p-0">
                    <Link
                        to="account"
                        className="flex justify-center items-center text-gray-600 gap-3 hover:text-green-500">
                        <UserIcon className="h-6 w-6" aria-hidden="true" />
                        <span>Perfil</span>
                    </Link>

                    <Link
                        to="change-password"
                        className="flex justify-center items-center text-gray-600 gap-3 hover:text-green-500">
                        <LockClosedIcon className="h-6 w-6" aria-hidden="true" />
                        <span>Segurança</span>
                    </Link>
                </div>

                <main className="h-full sm:col-span-3">
                    <Routes>
                        <Route index element={<Account />} />
                        <Route path="account" element={<Account />} />
                        <Route path="change-password" element={<Security />} />
                    </Routes>
                </main>
            </div>
        </Template>
    );
}