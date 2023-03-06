import { Outlet } from "react-router-dom";
import { Header } from "../Header/Header";
import { Template } from "../Template/Template";

export function DefaultLayout() {
  return (
    <div>
      <Header />
      <Outlet /> 
    </div>
  )
}