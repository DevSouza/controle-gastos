import { Link, redirect, useNavigate } from "react-router-dom";
import { InputCheckbox } from "../components/Form/InputCheck/InputCheckbox";
import { InputText } from "../components/Form/InputText/InputText";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { SubmitHandler, useForm } from "react-hook-form";

import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AxiosError } from "axios";
import { Button } from "../components/Form/Button/Button";
import { http } from "../services/api";

type SignUpFormData = {
  username: string;
  fullName: string;
  email: string;
  password: string;
};

const signUpFormShema = yup.object().shape({
  username: yup
    .string()
    .required("Username obrigatório")
    .min(4, "Username deve conter no mínimo quatro caracteres")
    .max(60, "Username deve conter no máximo sessenta caracteres"),
  fullName: yup
    .string()
    .required("Nome Completo obrigatório")
    .min(3, "Nome completo deve conter no mínimo três caracteres")
    .max(60, "Nome completo deve conter no máximo sessenta caracteres"),
  email: yup
    .string()
    .required("E-mail obrigatório")
    .min(4, "E-mail deve conter no mínimo quatro caracteres")
    .max(60, "E-mail deve conter no máximo sessenta caracteres"),
  password: yup
    .string()
    .required("Senha obrigatória")
    .min(6, "Senha deve conter no mínimo seis caracteres")
    .max(20, "Senha deve conter no máximo vinte caracteres"),
});

export function SignUp() {
  const { register, handleSubmit, formState, control, setError } =
    useForm<SignUpFormData>({
      resolver: yupResolver(signUpFormShema),
    });
  const navigate = useNavigate();
  const { errors, isSubmitting } = formState;

  const handleSignUp: SubmitHandler<SignUpFormData> = async ({
    username,
    fullName,
    email,
    password,
  }) => {
    try {
      console.log({ username, fullName, email, password });
      const responseAuth = await http.post(
        "/auth/signup",
        {
          username,
          fullName,
          email,
          password,
        }
      );
      toast.success("Cadastro finalizado! vou redirecionar você para o login.", {
        onClose: () => navigate('/'),
        autoClose: 2000,
      });
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
      } else {
        toast.error("Erro ao cadastrar perfil");
      }
    }
  };

  return (
    <div className="min-h-screen min-w-screen flex justify-center items-center flex-col gap-5 my-5">
      <h1 className="text-green-500 font-bold text-4xl">Kraken.</h1>

      <form
        className="bg-white drop-shadow-md rounded-md p-5 flex justify-center items-stretch flex-col max-w-sm "
        onSubmit={handleSubmit(handleSignUp)}
      >
        <h2 className="text-gray-900 text-xl">A aventura começa aqui 🚀</h2>
        <p className="text-gray-600 mt-1 mb-4">
          Torne o gerenciamento do seu aplicativo fácil e divertido!
        </p>

        <InputText
          label="Username"
          placeholder="john123"
          id="username"
          type="text"
          error={errors.username}
          name="username"
          control={control}
        />
        <InputText
          label="Nome"
          placeholder="John doe"
          id="fullname"
          type="text"
          error={errors.fullName}
          name="fullName"
          control={control}
        />
        <InputText
          label="Email"
          placeholder="john@example.com"
          id="email"
          type="email"
          error={errors.email}
          name="email"
          control={control}
        />
        <InputText
          label="Senha"
          placeholder="Senha"
          id="password"
          type="password"
          error={errors.password}
          name="password"
          control={control}
        />
        <InputCheckbox label="Eu concordo com a política de privacidade e os termos" id="terms" />

        <Button type="submit" loading={isSubmitting}>
          Criar Conta
        </Button>

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
    </div>
  );
}
