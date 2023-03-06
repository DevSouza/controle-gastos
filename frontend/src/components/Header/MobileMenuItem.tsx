import { Disclosure } from "@headlessui/react";
import { Link, useLocation, useNavigate } from "react-router-dom";

type MobileMenuItemProps = {
    to: string;
    name: string;
}

export function MobileMenuItem({ to, name }: MobileMenuItemProps){
    const { pathname } = useLocation();

    const current = pathname.startsWith(to);

    return (
        <Disclosure.Button
            as={Link}
            to={to}
            className={`${current ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'} block px-3 py-2 rounded-md text-base font-medium`}
            aria-current={current ? 'page' : undefined}
        >
            { name }
        </Disclosure.Button>
    );
}