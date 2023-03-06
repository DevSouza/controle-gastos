import { Link, useLocation, useNavigate, useRoutes } from "react-router-dom";
import { InputCheckbox } from "../components/Form/InputCheck/InputCheckbox";
import { InputText } from "../components/Form/InputText/InputText";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { SubmitHandler, useForm } from "react-hook-form";
import { useAuth } from "../contexts/AuthContext";

import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Button } from "../components/Form/Button/Button";

type SignInFormData = {
  username: string;
  password: string;
};

const signInFormSchema = yup.object().shape({
  username: yup
    .string()
    .required("Username obrigatório")
    .min(4, "Username deve conter no mínimo quatro caracteres")
    .max(60, "Username deve conter no máximo sessenta caracteres"),
  password: yup
    .string()
    .required("Senha obrigatória")
    .min(6, "Senha deve conter no mínimo seis caracteres")
    .max(20, "Senha deve conter no máximo vinte caracteres")
});

export function SignIn() {
  const { signIn } = useAuth();
  const { register, handleSubmit, formState, control } =
    useForm<SignInFormData>({
      resolver: yupResolver(signInFormSchema),
    });
  const { errors, isSubmitting } = formState;

  const handleSignIn: SubmitHandler<SignInFormData> = async (values) => {
    try {
      await signIn(values);
    } catch (err: any) {
      if (err.response.status === 404) {
        toast.error("Usuário não encontrado.");
      } else if (err.response.status === 401) {
        toast.error("Username/Password inválidos.");
      } else if (err.response.status === 418) {
        toast.error("Ip bloqueado por excesso de tentativas de login.");
      } else {
        toast.error(
          "Instabilidade com o servidor detectada, tente novamente mais tarde!"
        );
      }
    }
  };

  return (
    <div className="min-h-screen min-w-screen flex justify-center items-center flex-col gap-5">
      <h1 className="text-green-500 font-bold text-4xl">Kraken.</h1>

      <form
        className="bg-white drop-shadow-md rounded-md p-5 flex justify-center items-stretch flex-col max-w-sm"
        onSubmit={handleSubmit(handleSignIn)}
      >
        <h2 className="text-gray-900 text-xl">Bem Vindo! 👋</h2>
        <p className="text-gray-600 mt-1 mb-4">
          Faça login na sua conta e comece a aventura
        </p>

        <InputText
          label="Username"
          placeholder="Username"
          id="username"
          type="text"
          error={errors.username}
          name="username"
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
          option={
            <Link
              to="/forgot-password"
              className="text-green-500 hover:brightness-75 text-sm"
            >
              Esqueceu sua senha?
            </Link>
          }
        />

        <Button type="submit" loading={isSubmitting}>
          Entrar
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
      </form>
    </div>
  );
}
