import { Navigate, Outlet, Route, Routes } from "react-router-dom";
import { DefaultLayout } from "./components/Layouts/DefaultLayout";
import { useAuth } from "./contexts/AuthContext";
import { Categorias } from "./pages/Categorias";
import { Dashboard } from "./pages/Dashboard";
import { ForgotPassword } from "./pages/ForgotPassword";
import { Lancamentos } from "./pages/Lancamentos";
import { NotFound } from "./pages/NotFound";
import { DRE } from "./pages/Report/Dre";
import { Settings } from "./pages/Settings";
import { SignIn } from "./pages/SignIn";
import { SignUp } from "./pages/SignUp";
import { setupInterceptorClient } from "./services/api";

const OutletRoute = ({ redirect, to }: { redirect: boolean, to: string }) => (!redirect ? <Outlet /> : <Navigate to={to} />);

export function Router() {
    const { isAuthenticated, signOut } = useAuth();
    setupInterceptorClient(signOut);

    return (
        <Routes>
            <Route element={<OutletRoute redirect={isAuthenticated} to="/dashboard" />}>
                <Route index element={<SignIn />} />
                <Route path="/signin" element={<SignIn />} />
                <Route path="/signup" element={<SignUp />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
            </Route>

            <Route element={<OutletRoute redirect={!isAuthenticated} to="/" />}>
                <Route path="/" element={<DefaultLayout />}>
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/categorias" element={<Categorias />} />
                    <Route path="/lancamentos" element={<Lancamentos />} />
                    <Route path="/dre" element={<DRE />} />
                    <Route path="/settings/*" element={<Settings />} />
                </Route>
            </Route>

            <Route path="*" element={<NotFound />} />
        </Routes>
    );
}