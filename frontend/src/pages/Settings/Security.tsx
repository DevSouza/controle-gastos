import { yupResolver } from "@hookform/resolvers/yup";
import { AxiosError } from "axios";
import jwtDecode from "jwt-decode";
import { parseCookies } from "nookies";
import { SubmitHandler, useForm } from "react-hook-form";
import { toast, ToastContainer } from "react-toastify";
import * as yup from "yup";
import { Button } from "../../components/Form/Button/Button";
import { InputText } from "../../components/Form/InputText/InputText";
import { http } from "../../services/api";

type ChangePasswordFormData = {
  oldPassword: string;
  password: string;
  confirmPassword: string;
};

const changePasswordFormSchema = yup.object().shape({
  oldPassword: yup
    .string()
    .required("Senha antiga obrigatória")
    .min(6, "Senha deve conter no mínimo seis caracteres")
    .max(20, "Senha deve conter no máximo vinte caracteres"),
  password: yup
    .string()
    .required("Nova senha obrigatória")
    .min(6, "Senha deve conter no mínimo seis caracteres")
    .max(20, "Senha deve conter no máximo vinte caracteres"),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref("password"), null], "Confirmação da senha não confere")
    .required("Confirmação da senha obrigatória")
    .min(6, "Senha deve conter no mínimo seis caracteres")
    .max(20, "Senha deve conter no máximo vinte caracteres"),
});

export function Security() {
  const { register, handleSubmit, formState, control, reset } =
    useForm<ChangePasswordFormData>({
      resolver: yupResolver(changePasswordFormSchema),
    });
  const { errors, isSubmitting } = formState;

  const handleSignIn: SubmitHandler<ChangePasswordFormData> = async ({
    oldPassword,
    password,
  }) => {
    try {
      const cookies = parseCookies();
      const { "kraken.accessToken": token } = cookies;
      const { sub } = jwtDecode<{ sub: string }>(token);

      await http.patch(`/users/${sub}/password`, {
        oldPassword,
        password,
      });
      reset();

      toast.success("Senha atualizada com sucesso.");
    } catch (error) {
      const err = error as AxiosError;
      const status = err.response?.status;

      if (status && status === 401) return;

      if (status && status === 409) {
        toast.error("Senha Antiga não confere!");
      } else if (status && status >= 500) {
        toast.error(
          "Instabilidade com o servidor detectada, tente novamente mais tarde!"
        );
      } else {
        toast.error("Erro ao alterar a senha!");
      }
    }
  };

  return (
    <form onSubmit={handleSubmit(handleSignIn)} className="p-5 sm:p-0">
      <h1 className="text-2xl font-bold text-gray-900 mb-5 border-b-[1px] border-gray-500">
        Alterar Senha
      </h1>

      <div className="grid grid-cols-1 gap-1 w-full max-w-md">
        <InputText
          label="Senha antiga"
          id="oldPassword"
          type="password"
          error={errors.oldPassword}
          name="oldPassword"
          control={control}
        />

        <InputText
          label="Nova senha"
          id="password"
          type="password"
          error={errors.password}
          name="password"
          control={control}
        />
        <InputText
          label="Confirme nova senha"
          id="confirmPassword"
          type="password"
          error={errors.confirmPassword}
          name="confirmPassword"
          control={control}
        />

        <div className="flex items-center gap-5">
          <Button type="submit" loading={isSubmitting}>
            Atualizar senha
          </Button>
        </div>
      </div>
    </form>
  );
}
