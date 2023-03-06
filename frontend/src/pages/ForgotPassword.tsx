import { Link } from "react-router-dom";
import { InputCheckbox } from "../components/Form/InputCheck/InputCheckbox";
import { InputText } from "../components/Form/InputText/InputText";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { SubmitHandler, useForm } from "react-hook-form";

import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AxiosError } from "axios";
import { Button } from "../components/Form/Button/Button";
import { useState } from "react";
import { http } from "../services/api";

type ForgotPasswordFormData = {
  email: string;
};

const forgotPasswordFormShema = yup.object().shape({
  email: yup
    .string()
    .required("E-mail obrigatório")
    .min(4, "E-mail deve conter no mínimo quatro caracteres")
    .max(60, "E-mail deve conter no máximo sessenta caracteres"),
});

export function ForgotPassword() {
  const { register, handleSubmit, formState, control, setError, getValues } =
    useForm<ForgotPasswordFormData>({
      resolver: yupResolver(forgotPasswordFormShema),
    });
  const { errors, isSubmitting } = formState;
  const [finished, setFinished] = useState(false);

  const handleSignUp: SubmitHandler<ForgotPasswordFormData> = async ({
    email,
  }) => {
    try {
      await http.post(
        "/auth/forgot-password",
        { email }
      );
      setFinished(true);
    } catch (err) {
      const error = err as AxiosError;
      const { inner } = error.response?.data as any;
      if (inner) {
        inner.forEach((item: any) =>
          setError(item.path, {
            type: "manual",
            message: item.message,
          })
        );
      } else if(error.response?.status === 404) {
        toast.error("Email não cadastrado na plataforma");
      } else {
        toast.error("Erro ao Recuperar senha");
      }
    }
  };

  return (
    <div className="min-h-screen min-w-screen flex justify-center items-center flex-col gap-5 my-5">
      <h1 className="text-green-500 font-bold text-4xl">Kraken.</h1>

      { finished ? (
        <div className="bg-white drop-shadow-md rounded-md p-5 flex justify-center items-stretch flex-col max-w-sm">
          <h2 className="text-gray-900 text-xl">Senha Recuperada com sucesso? 🔑</h2>
          <p className="text-gray-600 mt-1 mb-4">
            Enviamos uma senha temporária para o 
            email: <strong>{ getValues().email }</strong> <br />

            <Link to="/" className="text-green-500 hover:brightness-90">
              Ir para página login
            </Link>
          </p>

        </div>
      ) : (
        <form
          className="bg-white drop-shadow-md rounded-md p-5 flex justify-center items-stretch flex-col max-w-sm "
          onSubmit={handleSubmit(handleSignUp)}
        >
          <h2 className="text-gray-900 text-xl">Esqueceu sua senha? 🔑</h2>
          <p className="text-gray-600 mt-1 mb-4">
            Insira seu e-mail abaixo para receber uma senha temporária!
          </p>

          <InputText
            label="Email"
            placeholder="john@example.com"
            id="email"
            type="email"
            error={errors.email}
            name="email"
            control={control}
          />
          
          <Button type="submit" loading={isSubmitting}>
            Recuperar senha
          </Button>

          <span className="before:h-[1px] after:h-[1px] before:w-full after:w-full before:bg-gray-400 after:bg-gray-400 before:opacity-30 after:opacity-30 flex items-center gap-2 justify-between flex-row mt-5 mb-3 text-gray-400">
            Ou
          </span>

          <span className="text-center text-gray-400">
            Novo em nossa plataforma?
            <Link
              to="/signup"
              className="text-green-500 ml-1 hover:brightness-75"
            >
              Crie uma conta
            </Link>
          </span>

          <span className="before:h-[1px] after:h-[1px] before:w-full after:w-full before:bg-gray-400 after:bg-gray-400 before:opacity-30 after:opacity-30 flex items-center gap-2 justify-between flex-row mt-5 mb-3 text-gray-400">
            Ou
          </span>
          <span className="text-center text-gray-400">
            Já tem uma conta?
            <Link
              to="/"
              className="text-green-500 ml-1 hover:brightness-75"
            >
              Faça o login
            </Link>
          </span>
        </form>
      )}
    </div>
  );
}
