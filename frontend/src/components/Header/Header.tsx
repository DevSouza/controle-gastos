import { Fragment } from 'react'
import { Disclosure, Menu, Transition } from '@headlessui/react'
import { BellIcon, LogoutIcon, MenuIcon, UserIcon, XIcon } from '@heroicons/react/outline'
import { useAuth } from '../../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { MobileMenu } from './MobileMenu';
import { MenuItem } from './MenuItem';

function classNames(...classes: any[]) {
    return classes.filter(Boolean).join(' ')
}

export function Header() {
    const { user, signOut } = useAuth();
    const navigate = useNavigate();

    return (
        <Disclosure as="nav" className="bg-gray-800">
            {({ open }) => (
                <>
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                        <div className="flex items-center justify-between h-16">
                            <div className="flex items-center">
                                <div className="flex-shrink-0">
                                    <h1 className="text-green-500 font-bold text-2xl">Kraken.</h1>
                                </div>
                                <div className="hidden md:block">
                                    <div className="ml-10 flex items-baseline space-x-4">
                                        <MenuItem to="/dashboard" name="Visão Geral" />
                                        <MenuItem to="/lancamentos" name="Movimentações" />
                                        <MenuItem to="/dre" name="DRE" />
                                        <MenuItem to="/categorias" name="Categorias" />
                                    </div>
                                </div>
                            </div>
                            <div className="hidden md:block">
                                <div className="ml-4 flex items-center md:ml-6">
                                    {/* Profile dropdown */}
                                    <Menu as="div" className="ml-3 relative">
                                        <div>
                                            <Menu.Button className="flex items-center w-7 h-7 rounded-md bg-slate-50 p-1">
                                                <span className="sr-only">Abrir menu do usuário</span>
                                                <UserIcon className="fill-slate-900 border-slate-900 stroke-slate-900"/>
                                            </Menu.Button>
                                        </div>
                                        <Transition
                                            as={Fragment}
                                            enter="transition ease-out duration-100"
                                            enterFrom="transform opacity-0 scale-95"
                                            enterTo="transform opacity-100 scale-100"
                                            leave="transition ease-in duration-75"
                                            leaveFrom="transform opacity-100 scale-100"
                                            leaveTo="transform opacity-0 scale-95"
                                        >
                                            <Menu.Items className="origin-top-right absolute right-0 mt-2 w-max min-w-[10rem] flex flex-col items-stretch justify-center rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 focus:outline-none">
                                                <Menu.Item>
                                                    {({ active }) => (
                                                        <button className={`text-left block px-4 py-2 text-sm text-gray-700 ${active && 'bg-gray-200'}`} onClick={() => navigate('/settings')}>
                                                            Editar Perfil
                                                        </button>
                                                    )}
                                                </Menu.Item>
                                                <hr />
                                                <Menu.Item>
                                                    {({ active }) => (
                                                        <a href={`${import.meta.env.VITE_GATEWAY_URL}/swagger-ui/index.html`} className={`text-left block px-4 py-2 text-sm text-gray-700 ${active && 'bg-gray-200'}`}>
                                                            Api Docs
                                                        </a>
                                                    )}
                                                </Menu.Item>
                                                <hr />
                                                <Menu.Item>
                                                    {({ active }) => (
                                                        <button className={`flex items-center gap-1 px-4 py-2 text-sm text-red-500 ${active && 'bg-gray-200'}`} onClick={() => signOut()}>
                                                            <LogoutIcon width={16}/>
                                                            Deslogar
                                                        </button>
                                                    )}
                                                </Menu.Item>
                                            </Menu.Items>
                                        </Transition>
                                    </Menu>
                                </div>
                            </div>
                            <div className="-mr-2 flex md:hidden">
                                {/* Mobile menu button */}
                                <Disclosure.Button className="bg-gray-800 inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white">
                                    <span className="sr-only">Abrir menu do usuário</span>
                                    {open ? (
                                        <XIcon className="block h-6 w-6" aria-hidden="true" />
                                    ) : (
                                        <MenuIcon className="block h-6 w-6" aria-hidden="true" />
                                    )}
                                </Disclosure.Button>
                            </div>
                        </div>
                    </div>

                    <MobileMenu />
                </>
            )}
        </Disclosure>
    );
}