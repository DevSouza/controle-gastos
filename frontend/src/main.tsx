import ReactDOM from 'react-dom/client'
import { BrowserRouter as Router } from 'react-router-dom'
import App from './App'

import "primereact/resources/themes/lara-light-indigo/theme.css";   //theme
import "primereact/resources/primereact.min.css";                   //core css
import "primeicons/primeicons.css";                                 //icons
import "primereact/resources/themes/tailwind-light/theme.css";      // Tailwind Theme

import "./index.css";

ReactDOM
.createRoot(document.getElementById('root') as HTMLElement)
.render(
    <Router>
        <App/>
    </Router>
)
