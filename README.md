# 📊 **Sistema de Geração e Distribuição de Relatórios Assíncronos**

Este projeto é um sistema distribuído para geração e notificação de relatórios de forma assíncrona. Ele é composto por três microservices que se comunicam através do **Apache Kafka** e **RabbitMQ**, utilizando o ecossistema **Spring Boot**, **Spring Cloud Stream** e processamento em background com `@Async`.

---

## 🧠 **Arquitetura e Fluxo de Dados**

O fluxo de trabalho é totalmente assíncrono para garantir **escalabilidade** e **resiliência**.

### 🔁 **Diagrama do Fluxo:**

```
[Usuário] -> [1. report-request-service] --(Evento: report-requests)--> [Kafka]
                                ^
                                | (Salva Status: PENDING) 
                                v
                            [Banco de Dados]
                                ^
                                | 
                                v
[Kafka] -> [2. report-generation-service] --(Gera Relatório @Async)--> [File Storage] 
     |                                |
     |--(Evento: report-status-updates)-->[Kafka] 
     | (Atualiza Status: GENERATED) 
     |
     `--(Msg: report-downloads)---------->[RabbitMQ] 
                                              |
                                              v
[Kafka] -> [3. notification-service] <------[RabbitMQ] 
     |            |
     |            `--(Log/Email Simulado) 
     |
     `----(Armazena Link) -> [Banco de Dados] <- [Usuário] (GET /download) 
```

---

## 🧩 **Responsabilidades dos Microservices**

### ✅ **report-request-service (API REST)**

- Recebe solicitações de relatórios via endpoint REST (`POST /reports/request`).
- Publica um evento no tópico Kafka `report-requests` utilizando Spring Cloud Stream.
- Armazena um registro da solicitação com status inicial **PENDING**.
- Fornece um endpoint (`GET /reports/{id}/status`) para o usuário verificar o status.

### ⚙️ **report-generation-service (Consumer e Producer)**

- Consome mensagens do tópico Kafka `report-requests`.
- Usa `@Async` para iniciar a geração do relatório em background.
- Gera o relatório consultando um banco de dados e o armazena.
- Atualiza o status para **GENERATED**.
- Publica evento de status no tópico Kafka `report-status-updates`.
- Publica mensagem com link de download na fila RabbitMQ `report-downloads`.

### 📬 **notification-service (Consumer)**

- Consome mensagens do tópico Kafka `report-status-updates`.
- Consome mensagens de link da fila RabbitMQ `report-downloads`.
- Simula uma notificação ao usuário (via `log`) e armazena o link.
- Disponibiliza o endpoint (`GET /reports/{id}/download`) para baixar.

---

## 🛠️ **Tecnologias Utilizadas**

- **Frameworks:** Spring Boot, Spring Web, Spring Cloud Stream, Spring Data JPA
- **Mensageria:** Apache Kafka, RabbitMQ
- **Banco de Dados:** H2 ou PostgreSQL
- **Testes:** JUnit, Mockito, Testcontainers, JaCoCo (90% cobertura)
- **Build:** Maven ou Gradle
- **Containerização:** Docker e Docker Compose

---

## 🚀 **Pré-requisitos**

- JDK 17+
- Docker e Docker Compose
- Maven 3.8+
- Cliente REST (Postman, Insomnia) ou `curl`

---

## ▶️ **Como Executar o Projeto**


### 2. Suba o Ambiente com Docker Compose
```bash
docker-compose up -d --build
```

- `--build`: Reconstrói as imagens Docker.
- `-d`: Executa os contêineres em background.

---

## 💡 **Como Usar (Exemplos)**

### 📤 Passo 1: Solicitar um novo relatório
```bash
curl -X POST http://localhost:8080/reports/request \
-H "Content-Type: application/json" \
-d '{
  "tipoRelatorio": "VENDAS_CONSOLIDADAS",
  "dataInicial": "2025-01-01",
  "dataFinal": "2025-01-31"
}'
```

### 🔄 Passo 2: Verificar o status da geração
```bash
curl http://localhost:8080/reports/{id}/status
```

### 📥 Passo 3: Baixar o relatório pronto
```bash
curl -o relatorio_final.csv http://localhost:8082/reports/{id}/download
```

---

## ✅ **Testes**

- **Tipos:** Testes unitários e de integração.
- **Execução:**
```bash
mvn clean verify
```
- **Cobertura:** Relatório JaCoCo gerado em `target/site/jacoco/index.html` com mínimo de 90%.

---

## 📚 **Tópicos Avançados Abordados**

- Configuração de Binders no Spring Cloud Stream
- Serialização/Desserialização de Mensagens
- Grupos de Consumidores no Kafka
- Idempotência em Consumidores
- Tratamento de Erros com `ErrorChannel`
- Monitoramento e Métricas
- Consistência Eventual
- Escalabilidade Horizontal

---

> ✨ Projeto desenvolvido como referência para estudos de arquitetura de sistemas distribuídos, mensageria e resiliência assíncrona.

