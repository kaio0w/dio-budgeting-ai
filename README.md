# DIO Budgeting AI

Projeto desenvolvido como entrega do desafio final da trilha **Spring Boot + Spring AI** da DIO.

A aplicação permite criar e consultar transações financeiras por API REST, texto em linguagem natural e comandos de voz. A IA interpreta a intenção do usuário e executa funções reais da aplicação através de **Tool Calling**.

Como melhoria do projeto base, foi implementada a consulta inteligente de **gastos por categoria**.

---

## Funcionalidades

- Criar receitas e despesas.
- Listar transações.
- Consultar saldo.
- Consultar gastos por categoria.
- Processar comandos em linguagem natural com `ChatClient`.
- Executar funções reais com `@Tool`.
- Transcrever áudio em texto.
- Gerar resposta em áudio.
- Persistir dados com H2.
- Testar endpoints com Swagger/OpenAPI.

---

## Arquitetura

```text
Cliente
   ↓
REST / Texto / Áudio
   ↓
Spring AI
   ↓
ChatClient
   ↓
Tool Calling
   ↓
Use Case
   ↓
Repository
   ↓
JPA / H2
```

Estrutura principal:

```text
src/main/java/dio/budgeting
├── domain
├── application
└── infrastructure
    ├── ai
    ├── config
    ├── http
    └── persistence
```

A IA interpreta a intenção, mas as regras de negócio permanecem na aplicação Java.

---

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring AI 2.0.1
- Spring Web
- Spring Data JPA
- Bean Validation
- OpenAI
- H2 Database
- Swagger / OpenAPI
- Gradle
- JUnit 5

---

## Como rodar

Configure a chave da OpenAI:

### Linux / WSL / macOS

```bash
export OPENAI_API_KEY="sua-chave-aqui"
```

### Windows PowerShell

```powershell
$env:OPENAI_API_KEY="sua-chave-aqui"
```

> Os endpoints `/api/transactions/*` funcionam sem OpenAI.  
> A chave é necessária para os endpoints `/api/ai/*`.

Execute:

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew.bat bootRun
```

---

## Links principais

Com a aplicação rodando:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs
- H2 Console: http://localhost:8080/h2-console
- API: http://localhost:8080

> A rota `/` pode retornar `404`. Isso é esperado, pois não existe uma página inicial configurada.

JDBC URL do H2:

```text
jdbc:h2:file:./data/budgeting
```

---

## Endpoints principais

```http
POST /api/transactions
GET  /api/transactions
GET  /api/transactions/balance
GET  /api/transactions/expenses/category/{category}

POST /api/ai/text
POST /api/ai/voice
```

---

## Testando pelo Swagger

Acesse:

```text
http://localhost:8080/swagger-ui.html
```

Depois:

1. Abra o endpoint.
2. Clique em `Try it out`.
3. Preencha os dados.
4. Clique em `Execute`.

Exemplo de despesa:

```json
{
  "type": "EXPENSE",
  "amount": 45.90,
  "category": "alimentacao",
  "description": "Almoço"
}
```

---

## Exemplo com IA

```bash
curl -X POST http://localhost:8080/api/ai/text \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Quanto eu gastei com alimentação?"
  }'
```

Exemplos de comandos:

```text
Gastei 80 reais no mercado.
Recebi 250 reais de um trabalho freelancer.
Quanto eu gastei com alimentação?
Qual é o meu saldo?
```

---

## Fluxo de voz

```bash
curl -X POST http://localhost:8080/api/ai/voice \
  -F "audio=@comando.mp3" \
  --output resposta.mp3
```

Fluxo:

```text
Áudio
  ↓
TranscriptionModel
  ↓
ChatClient
  ↓
Tool Calling
  ↓
Use Case
  ↓
H2
  ↓
TextToSpeechModel
  ↓
MP3
```

---

## Testes

Execute:

```bash
./gradlew clean test
```

Resultado esperado:

```text
BUILD SUCCESSFUL
```

Relatório dos testes:

```text
build/reports/tests/test/index.html
```

Para gerar o build:

```bash
./gradlew clean build
```

---

## Melhoria implementada

Foi adicionada a consulta inteligente de gastos por categoria.

Exemplo:

> Quanto eu gastei com alimentação?

A IA identifica a intenção e chama a ferramenta correspondente, que executa o caso de uso e consulta os dados persistidos.

---

## Referências

- DIO Spring Boot Learning Track  
  https://github.com/digitalinnovationone/dio-spring-boot-learning-track

- Projeto final Spring AI  
  https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/main/05-spring-ai/README.md

- Spring AI  
  https://docs.spring.io/spring-ai/reference/

- Springdoc OpenAPI  
  https://springdoc.org/