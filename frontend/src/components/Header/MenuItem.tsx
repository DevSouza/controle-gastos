import { Disclosure } from "@headlessui/react";
import { Link, useLocation, useNavigate } from "react-router-dom";

type MenuItemProps = {
    to: string;
    name: string;
}

export function MenuItem({ to, name }: MenuItemProps){
    const { pathname } = useLocation();

    const current = pathname.startsWith(to);

    return (
        <Link
            to={to}
            className={` ${current ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'} px-3 py-2 rounded-md text-sm font-medium`}
            aria-current={current ? 'page' : undefined}
        >
            { name }
        </Link>
    );
}