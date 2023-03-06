import { Disclosure } from "@headlessui/react";
import { LogoutIcon, UserIcon } from "@heroicons/react/outline";
import { useAuth } from "../../contexts/AuthContext";
import { MobileMenuItem } from "./MobileMenuItem";

export function MobileMenu() {
    const { user, signOut } = useAuth();

    return (
        <Disclosure.Panel className="md:hidden">
            <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
                <MobileMenuItem to="/dashboard" name="Visão Geral" />
                <MobileMenuItem to="/lancamentos" name="Lançamentos" />
                <MobileMenuItem to="/dre" name="DRE" />
                <MobileMenuItem to="/categorias" name="Categorias" />
            </div>
            <div className="pt-4 pb-3 border-t border-gray-700">
                <div className="flex items-center px-5">
                    <div className="flex-shrink-0">
                        <div className="flex items-center w-7 h-7 rounded-md bg-slate-50 p-1">
                            <UserIcon className="fill-slate-900 border-slate-900 stroke-slate-900"/>
                        </div>
                    </div>
                    <div className="ml-3">
                        <div className="text-base font-medium leading-none text-white">{user?.username}</div>
                        <div className="text-sm font-medium leading-none text-gray-400">{user?.email}</div>
                    </div>
                    
                </div>
                <div className="mt-3 px-2 space-y-1">
                    <MobileMenuItem to="/settings" name="Editar Perfil" />
                    <Disclosure.Button
                        className="flex items-center gap-1 px-3 py-2 rounded-md text-base font-medium text-red-500 hover:bg-gray-700 w-full"
                        onClick={() => signOut()}>
                        <LogoutIcon width={16}/>
                        Deslogar
                    </Disclosure.Button>
                </div>
            </div>
        </Disclosure.Panel>
    )
}