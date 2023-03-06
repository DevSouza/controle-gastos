import { QueryClientProvider } from 'react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AuthProvider } from './contexts/AuthContext';
import { ThemeContextProvider } from './contexts/ThemeContext';
import { Router } from './Router';

import { ReactQueryDevtools } from 'react-query/devtools';
import { queryClient } from './services/queryClient';

import { addLocale } from 'primereact/api';

function App() {
    addLocale('pt', {
        firstDayOfWeek: 0,
        dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
        dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
        dayNamesMin: ['D','S','T','Q','Q','S','S'],
        monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
        monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'],
        today: 'Hoje',
        clear: 'Limpar'
    });

    return (
        <>
            <QueryClientProvider client={queryClient}>
                <ThemeContextProvider>
                    <AuthProvider>
                        <Router />
                    </AuthProvider>
                </ThemeContextProvider>

                {false  && (<ReactQueryDevtools />)}
                <ToastContainer />
            </QueryClientProvider>
        </>
    );
}

export default App;