import { useAuth } from "../../contexts/AuthContext";

import { Button } from "../../components/Form/Button/Button";
import { InputText } from "../../components/Form/InputText/InputText";

import * as yup from "yup";
import { useEffect } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { SubmitHandler, useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { AxiosError } from "axios";
import { http } from "../../services/api";

type GeneralFormData = {
  username: string;
  fullName: string;
  email: string;
};

const generalFormSchema = yup.object().shape({
  username: yup
    .string()
    .required("Username é obrigatório")
    .min(4, "Username deve conter no mínimo quatro caracteres")
    .max(60, "Username deve conter no máximo sessenta caracteres"),
  fullName: yup
    .string()
    .required("Nome é obrigatório")
    .min(3, "Nome completo deve conter no mínimo três caracteres")
    .max(60, "Nome completo deve conter no máximo sessenta caracteres"),
  email: yup
    .string()
    .email("Email inválido")
    .required("Email é obrigatorio")
    .min(4, "E-mail deve conter no mínimo quatro caracteres")
    .max(60, "E-mail deve conter no máximo sessenta caracteres"),
});

export function Account() {
  const { user } = useAuth();
  const { register, handleSubmit, formState, setValue, control, setError } =
    useForm<GeneralFormData>({
      resolver: yupResolver(generalFormSchema),
    });
  const { errors, isSubmitting } = formState;

  useEffect(() => {
    const loadData = async () => {
      const response = await http.get(
        `/users/${user?.userId}`
      );
      const { username, fullName, email } = response.data;

      setValue("username", username);
      setValue("fullName", fullName);
      setValue("email", email);
    };

    loadData();
  }, []);

  const handleUpdateGeneral: SubmitHandler<GeneralFormData> = async ({
    email,
    fullName,
    username,
  }) => {
    try {
      console.log({ username, fullName, email });
      const response = await http.put(
        `/users/${user?.userId}`,
        {
          email,
          fullName,
          username,
        }
      );

      toast.success("Perfil Atualizado com sucesso.");
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
        toast.error("Erro ao atualizar perfil.");
      }
    }
  };

  return (
    <form
      className="px-5 pt-5 sm:p-0 sm:pt-0"
      onSubmit={handleSubmit(handleUpdateGeneral)}
    >
      <h1 className="text-2xl font-bold text-gray-900 mb-5 border-b-[1px] border-gray-400">
        Perfil
      </h1>

      <div className="grid grid-cols-1 gap-1 w-full max-w-md">
        <InputText
          label="Username"
          placeholder="johndoe123"
          type="text"
          error={errors.username}
          name="username"
          control={control}
        />

        <InputText
          label="Nome"
          placeholder="John Doe"
          id="fullName"
          type="text"
          error={errors.fullName}
          name="fullName"
          control={control}
        />

        <InputText
          label="Email"
          placeholder="johndoe@gmail.com"
          id="email"
          type="email"
          error={errors.email}
          name="email"
          control={control}
        />

        <div>
          <Button type="submit" loading={isSubmitting}>
            Atualizar Perfil
          </Button>
        </div>
      </div>
    </form>
  );
}
