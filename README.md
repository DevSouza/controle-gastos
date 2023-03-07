<div margin="1rem">
  <h1 align="center">🪐Controle Gastos</h1>
  <br />
</div>

## 🛠️ Construído com

* [Java](https://www.java.com/)
* [PostgreSQL](https://www.postgresql.org/)
* [Spring Boot](https://spring.io/)
* [ReactJS](https://reactjs.org/)
* [Docker](https://www.docker.com/)

### 🔧 Instalação

Para iniciar o projeto execute as etapas abaixo

Vá até a pasta **infrastructure** e rode o comando abaixo para iniciar o PostgreSQL e MailCatcher:
```
docker-compose -f docker-compose.development.yml up -d
```

Vá até a pasta **backend** e rode o comando abaixo para iniciar o backend:
```
mvn spring-boot:run
```

Vá até a pasta **frontend** e rode o comando abaixo para iniciar o frontend:
```
npm run dev
```

<div>
  <br />
  <hr />
  <br />
  <p align="center">⌨️ com ❤️ por <a href="https://github.com/devsouza">DevSouza</a></p>
</div>
