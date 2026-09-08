# Auto-Escola 3ESPG — API ReST

Checkpoint 4 — **SOA e WebServices** — Prof. Carlos Eduardo Machado de Oliveira

API ReST em Spring Boot para agendamento de instruções de uma auto-escola, com
autenticação via JWT, controle de acesso por perfil e senhas armazenadas apenas
como hash BCrypt.

## Integrantes do grupo

| Nome | RM |
| --- | --- |
| Glauco Heitor Gonçalves e Silva | 555978 |
| Pedro Henrique Junqueira | 556278 |

## Tecnologias

- Java 25
- Spring Boot 4.0.8 (Web MVC, Data JPA, Security, Validation)
- Flyway (versionamento do banco)
- MySQL 8
- java-jwt (Auth0) 4.6.0
- Lombok

## Como executar

1. Subir o MySQL e criar o banco:

   ```sql
   create database autoescola3espg;
   ```

2. Conferir as credenciais em `src/main/resources/application.properties`
   (padrão: usuário `root`, senha `fiap`). Elas podem ser sobrescritas pelas
   variáveis de ambiente `DATASOURCE_USERNAME`, `DATASOURCE_PASSWORD` e `JWT_SECRET`.

3. Rodar a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

O Flyway cria todas as tabelas automaticamente na primeira execução. A API sobe
em `http://localhost:8085`.

### Usuário inicial

A migration `V10` cadastra o administrador necessário para criar os demais
usuários (o endpoint de cadastro é restrito a ADMIN):

| Login | Senha | Perfil |
| --- | --- | --- |
| `admin` | `admin` | ADMIN |

## Collection do Insomnia

O arquivo [`docs/insomnia-autoescola3espg.json`](docs/insomnia-autoescola3espg.json)
traz todas as requisições da API já prontas, organizadas em seis pastas —
incluindo uma pasta só com os casos de erro que demonstram cada regra de negócio.

Para importar: **Insomnia → Import → From File** e selecione o arquivo. Depois
rode `Login ADMIN`, copie o `tokenJWT` da resposta e cole na variável `token` do
Environment (`Ctrl+E`).

## Autenticação

Todos os endpoints exigem um token JWT, exceto `POST /login` e `GET /health-check`.

```bash
curl -X POST http://localhost:8085/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","senha":"admin"}'
```

A resposta traz o token, que deve ser enviado nas demais requisições:

```
Authorization: Bearer <token>
```

O token expira em 30 minutos.

## Perfis de acesso

| Perfil | Permissões |
| --- | --- |
| `ADMIN` | Todos os endpoints |
| `USER` | Consultar instrutores e alunos, agendar e cancelar instruções, alterar a própria senha |

## Endpoints

### Health check

| Método | Rota | Acesso |
| --- | --- | --- |
| GET | `/health-check` | Público |

### Login

| Método | Rota | Acesso |
| --- | --- | --- |
| POST | `/login` | Público |

### Usuários

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| POST | `/usuarios` | ADMIN | Cadastra usuário (senha gravada em BCrypt) |
| GET | `/usuarios` | ADMIN | Lista paginada (10 por página, ordenada por login) |
| GET | `/usuarios/{id}` | ADMIN | Detalha um usuário |
| PUT | `/usuarios` | ADMIN | Atualiza o perfil de um usuário |
| DELETE | `/usuarios/{id}` | ADMIN | Exclui um usuário |
| PUT | `/usuarios/senha` | Autenticado | Altera a **própria** senha |

A senha, mesmo criptografada, nunca é devolvida pela API.

```bash
# Cadastro de usuário (ADMIN)
curl -X POST http://localhost:8085/usuarios \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"login":"pedro","senha":"senha123","perfil":"USER"}'

# Alteração da própria senha (qualquer usuário autenticado)
curl -X PUT http://localhost:8085/usuarios/senha \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"senhaAtual":"senha123","novaSenha":"novaSenha456"}'
```

Regras:

- Não é permitido cadastrar dois usuários com o mesmo login.
- A troca de senha exige a senha atual como confirmação e a nova senha precisa
  ser diferente da atual.
- Um usuário não pode excluir a própria conta.

### Instrutores

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| POST | `/instrutores` | ADMIN | Cadastra instrutor |
| GET | `/instrutores` | ADMIN, USER | Lista paginada (10 por página, ordenada por nome) |
| GET | `/instrutores/{id}` | ADMIN, USER | Detalha um instrutor |
| PUT | `/instrutores` | ADMIN | Atualiza nome, telefone e endereço |
| DELETE | `/instrutores/{id}` | ADMIN | Exclusão lógica (marca como inativo) |

```bash
curl -X POST http://localhost:8085/instrutores \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nome":"Carlos Lima",
    "email":"carlos@email.com",
    "telefone":"11988887777",
    "cnh":"12345678901",
    "especialidade":"CARROS",
    "endereco":{
      "logradouro":"Rua B","numero":"20","bairro":"Vila",
      "cidade":"Sao Paulo","uf":"SP","cep":"02002-000"
    }
  }'
```

Regras:

- Especialidades aceitas: `MOTOS`, `CARROS`, `VANS`, `CAMINHOES`.
- Número e complemento do endereço são opcionais; os demais campos são obrigatórios.
- **E-mail, CNH e especialidade não podem ser alterados** — por isso não existem no
  corpo da requisição de atualização.
- A exclusão não apaga o registro: apenas marca o instrutor como inativo, e ele
  deixa de aparecer na listagem.

### Alunos

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| POST | `/alunos` | ADMIN | Cadastra aluno |
| GET | `/alunos` | ADMIN, USER | Lista paginada (10 por página, ordenada por nome) |
| GET | `/alunos/{id}` | ADMIN, USER | Detalha um aluno |
| PUT | `/alunos` | ADMIN | Atualiza nome, telefone e endereço |
| DELETE | `/alunos/{id}` | ADMIN | Exclusão lógica (marca como inativo) |

```bash
curl -X POST http://localhost:8085/alunos \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nome":"Ana Souza",
    "email":"ana@email.com",
    "telefone":"11999990000",
    "cpf":"12345678901",
    "endereco":{
      "logradouro":"Rua A","numero":"10","bairro":"Centro",
      "cidade":"Sao Paulo","uf":"SP","cep":"01001-000"
    }
  }'
```

Regras:

- **E-mail e CPF não podem ser alterados** — por isso não existem no corpo da
  requisição de atualização.
- A exclusão não apaga o registro: apenas marca o aluno como inativo.

### Instruções

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| POST | `/instrucoes` | ADMIN, USER | Agenda uma instrução |
| DELETE | `/instrucoes` | ADMIN, USER | Cancela uma instrução |

#### Agendamento

```bash
# Com instrutor definido
curl -X POST http://localhost:8085/instrucoes \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"id_aluno":1,"id_instrutor":1,"data_hora":"11/09/2026 - 10:00"}'

# Sem instrutor: o sistema sorteia um instrutor disponível da especialidade
curl -X POST http://localhost:8085/instrucoes \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"id_aluno":1,"especialidade":"CARROS","data_hora":"11/09/2026 - 14:00"}'
```

Regras validadas:

- Funcionamento de segunda a sábado, das 06:00 às 21:00. Como a instrução dura
  1 hora fixa, o último horário de início é 20:00.
- Instruções só começam em horas cheias (09:00, 13:00, ...).
- Antecedência mínima de 30 minutos.
- Aluno e instrutor precisam estar ativos.
- No máximo 2 instruções por dia para o mesmo aluno.
- Um instrutor não pode ter duas instruções na mesma data/hora.
- Instrutor é opcional: quando omitido, o sistema sorteia aleatoriamente um
  instrutor ativo e livre naquele horário dentro da especialidade informada.

#### Cancelamento

```bash
curl -X DELETE http://localhost:8085/instrucoes \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"id_instrucao":1,"motivo_cancelamento":"ALUNO_DESISTIU"}'
```

Regras validadas:

- O motivo é obrigatório e deve ser um destes: `ALUNO_DESISTIU`,
  `INSTRUTOR_CANCELOU` ou `OUTROS`.
- Antecedência mínima de 24 horas em relação à data/hora da instrução.
- Uma instrução já cancelada não pode ser cancelada novamente.
- O cancelamento não apaga a instrução: grava o motivo e a data do cancelamento.
  O horário volta a ficar livre na agenda do instrutor.

## Códigos de resposta

| Código | Situação |
| --- | --- |
| 200 | Operação concluída |
| 201 | Recurso criado |
| 204 | Operação concluída sem conteúdo (exclusão, troca de senha) |
| 400 | Dados inválidos ou regra de negócio violada |
| 401 | Credenciais inválidas |
| 403 | Sem permissão para a operação |
| 404 | Recurso não encontrado |

## Estrutura do projeto

```
src/main/java/br/com/fiap3espg/autoescola3espg
├── controller           # Endpoints ReST e autorização por perfil (@PreAuthorize)
├── domain
│   ├── aluno            # Entidade, repository e DTOs de aluno
│   ├── endereco         # Endereço embutido nas entidades
│   ├── instrucao        # Agendamento, cancelamento e validadores de regras
│   ├── instrutor        # Entidade, repository e DTOs de instrutor
│   └── usuario          # Usuário, perfis e DTOs de autenticação
├── infra
│   ├── exception        # Tratamento global de erros
│   └── security         # Configuração do Spring Security, filtro e JWT
└── service              # Regras de aplicação
```

As regras de negócio de agendamento e de cancelamento são implementadas como
validadores independentes (`ValidadorAgendamento` e `ValidadorCancelamento`).
Cada regra é um `@Component` isolado, e o service recebe a lista completa por
injeção de dependência — para adicionar uma nova regra basta criar uma classe,
sem alterar o service.

## Migrations

| Versão | Descrição |
| --- | --- |
| V1–V3 | Tabela de instrutores (+ telefone e flag de ativo) |
| V4–V5 | Tabela de usuários (+ coluna de perfil) |
| V6, V8 | Tabela de alunos (+ flag de ativo) |
| V7 | Tabela de instruções |
| V9 | Remove a constraint UNIQUE da coluna de senha |
| V10 | Cadastra o usuário administrador inicial |
| V11 | Colunas de motivo e data do cancelamento nas instruções |
