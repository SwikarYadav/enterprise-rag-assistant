# Enterprise RAG Assistant

## Problem Statement

Organizations store large amounts of information in documents such as PDFs, DOCX files, manuals, reports, and internal knowledge bases. Traditional chatbots cannot answer questions based on these documents without retraining or fine-tuning large language models.

The goal of this project is to build an Enterprise Retrieval-Augmented Generation (RAG) Assistant that allows users to upload documents, convert them into vector embeddings, perform semantic search, and generate context-aware answers using Large Language Models (LLMs).

This solution demonstrates how modern AI applications can combine vector databases, embeddings, retrieval pipelines, and LLMs to provide accurate responses grounded in enterprise data.

---

## Architecture

```text
+------------------+
| Angular Frontend |
+--------+---------+
         |
         v
+------------------+
| Spring Boot API  |
|   (Spring AI)    |
+--------+---------+
         |
         +----------------------+
         |                      |
         v                      v
+----------------+     +----------------+
| PostgreSQL     |     | Ollama         |
| + pgvector     |     | Embeddings     |
+----------------+     +----------------+
         |
         v
+----------------+
| Semantic Search|
+----------------+
         |
         v
+----------------+
| Groq LLM       |
| (Generation)   |
+----------------+
         |
         v
+----------------+
| Final Response |
+----------------+
```

### Workflow

1. User uploads a document (PDF, DOCX, TXT).
2. Document content is extracted.
3. Text is split into chunks.
4. Chunks are converted into vector embeddings using Ollama.
5. Embeddings are stored in PostgreSQL using pgvector.
6. User asks a question.
7. Semantic search retrieves the most relevant chunks.
8. Retrieved context is combined with the user query.
9. Groq generates a context-aware response.
10. Response is returned to the user.

---

## Features

### Document Processing

* PDF document upload
* DOCX document upload
* TXT document upload
* Content extraction
* Chunk generation

### Embeddings & Vector Search

* Local embedding generation using Ollama
* pgvector integration
* Semantic similarity search
* Context retrieval pipeline

### Conversational AI

* Groq-powered response generation
* Context-aware answers
* Multi-turn conversation support
* In-memory conversation memory using ConcurrentHashMap

### Backend Features

* Spring Boot 3.5
* Spring AI integration
* REST APIs
* Exception handling
* Service-layer architecture
* Repository pattern

### Deployment Features

* Dockerized application
* Docker Compose orchestration
* PostgreSQL container
* Ollama container
* Environment variable based secret management

---

## Tech Stack

### Backend

* Java 21
* Spring Boot 3.5
* Spring AI
* Maven

### Frontend

* Angular

### Database

* PostgreSQL
* pgvector

### AI Components

* Groq (LLM Generation)
* Ollama
* nomic-embed-text Embedding Model

### DevOps

* Docker
* Docker Compose
* Git
* GitHub

---

## Project Structure

```text
src
├── main
│   ├── java/com/swikar/rag
│   │   ├── config
│   │   ├── controller
│   │   ├── entity
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   ├── services
│   │   └── util
│   └── resources
│       └── application.yml
└── test
```

### Important Components

* UploadController – Document upload APIs
* ChatController – Chat APIs
* DocumentService – Document processing
* ChunkingService – Text chunking
* EmbeddingService – Embedding generation
* RetrievalService – Semantic retrieval
* RagService – RAG orchestration
* MemoryService – Conversation memory
* VectorStoreService – Vector storage operations

---

## Docker Setup

### Containers

#### PostgreSQL + pgvector

Stores document chunks and vector embeddings.

#### Ollama

Generates embeddings using:

```text
nomic-embed-text
```

#### Spring Boot Application

Handles document ingestion, retrieval, memory management, and response generation.

### Start Containers

```bash
docker compose up
```

### Stop Containers

```bash
docker compose down
```

---

## Running Locally

### Prerequisites

* Java 21+
* Maven
* Docker Desktop
* Git

### Clone Repository

```bash
git clone https://github.com/SwikarYadav/enterprise-rag-assistant.git
cd enterprise-rag-assistant
```

### Configure Environment Variables

Create a `.env` file:

```env
GROQ_API_KEY=your_groq_api_key
```

### Build Application

```bash
./mvnw clean package -DskipTests
```

### Build Docker Image

```bash
docker build -t enterprise-rag-assistant .
```

### Start Application

```bash
docker compose up
```

### Access API

```text
http://localhost:8080
```

---

## Current Status

Implemented:

* Document upload
* Text chunking
* Embedding generation
* Vector storage
* Semantic search
* RAG pipeline
* Conversation memory
* Dockerization
* GitHub integration

---

## Future Improvements

### Memory & Caching

* Redis-based conversation memory
* Persistent chat history
* Session management

### AI Improvements

* Hybrid search (keyword + vector)
* Reranking models
* Source citation support
* Multi-document retrieval

### Security

* Authentication & Authorization
* JWT Security
* API rate limiting

### Observability

* Centralized logging
* Metrics monitoring
* Health checks

### Cloud Deployment

* AWS EC2 deployment
* Nginx reverse proxy
* CI/CD using GitHub Actions
* Container registry integration

---

## Author

Swikar Yadav

Enterprise RAG Assistant is a learning-focused project designed to explore Retrieval-Augmented Generation (RAG), vector databases, LLM integration, Docker-based deployment, and enterprise AI application development.
