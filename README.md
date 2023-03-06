<div margin="1rem">
  <h1 align="center">Controle Financeiro</h1>
  <br />
  <h3 align="center">O Objetivo é controlar as despesas e receitas e definir limites de gastos de forma simples.</h3>
  <br />
</div>

## Pilares
- Fácil de Utilizar
- Rapido
- Multiplataforma
- Estável

## Funcionalidades Basicas
### Usuario
- [x] Cadastro de Usuario ( Sign Up )
- [x] Recuperação senha ( Forgot Password )
- [x] Atualização dados usuario ( Profile )
- [x] Autenticação Usuario ( Sign In )

### Categorias
- [x] Cadastro de Categoria
- [x] Atualização de Categoria
- [x] Excluir Categoria
- [x] Limite de Gastos

### Despesa / Receita
- [x] Listagem ( Por Mês )
- [x] Cadastro
- [x] Marcar como pago
- [ ] Atualizar
- [x] Excluir

### Dashboard
- [x] Total de Despesas no mes
- [x] Total de Receita no mes
- [x] Saldo do mes.

# Start
1. Inicie a infra com o comando ```docker-compose -f docker-compose.development.yml up -d```
2. Inicie o backend com o comando ```mvn spring-boot:run```
3. Inicie o frontend com o comando ```npm run dev```
