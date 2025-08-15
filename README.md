# 📚 Literalura

**Literalura** é uma aplicação Java com **Spring Boot** que integra dados da API pública **[Gutendex](https://gutendex.com/)** (catálogo de livros do Projeto Gutenberg) e armazena as informações em um banco de dados **PostgreSQL**.

O projeto permite buscar, cadastrar e consultar livros e autores, unindo dados vindos da API com persistência local.

---

## 🚀 Funcionalidades

- 🔎 Buscar livros e autores diretamente da API Gutendex.
- 💾 Salvar livros e autores no banco PostgreSQL.
- 📖 Consultar livros por título ou idioma.
- 👤 Consultar todos os livros de um determinado autor.
- 📊 Listar e ordenar livros conforme o número de downloads.
- 🔄 Integração entre API externa e banco de dados usando JPA/Hibernate.
- 🖥️ Interface de menu no terminal para interação.

---

## 🛠 Tecnologias Utilizadas

- **Java 24** ☕
- **Spring Boot 3.5.4** (Web, Data JPA)
- **Hibernate ORM**
- **PostgreSQL**
- **Maven**
- **Jakarta Persistence API**
  
---

## 📂 Estrutura do Projeto

- src/
├── main/
│ ├── java/com/alura/literalura/
│ │ ├── model/ # Entidades JPA (Livro, Autor)
│ │ ├── repository/ # Interfaces do Spring Data JPA
│ │ ├── service/ # Regras de negócio (integração com Gutendex e banco)
│ │ ├── controller/ # Entrada da aplicação / menu interativo
│ └── resources/
│ ├── application.properties # Configurações do banco e do Spring Boot
- **Jackson Databind** (para consumir e mapear JSON da API)

---

## 📊 Modelo de Dados

```mermaid
erDiagram
    AUTOR ||--o{ LIVRO : "escreve"
    AUTOR {
        Long id
        String nome
    }
    LIVRO {
        Long id
        String titulo
        String idioma
        int numeroDeDownloads
        Autor autor
    }

---

⚙️ Configuração do Banco de Dados
Edite o arquivo src/main/resources/application.properties e insira suas credenciais do PostgreSQL:

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

---

💡 Antes de rodar, crie o banco literalura no PostgreSQL:

CREATE DATABASE literalura;

---

▶️ Como Executar o Projeto

# Clonar repositório
git clone https://github.com/RomuloFelipe1309/litealura.git

# Entrar na pasta do projeto
cd litealura

# Rodar aplicação com Maven
mvn spring-boot:run

---

💻 Exemplo de Uso no Terminal
Quando a aplicação é executada, um menu interativo é exibido:

Sejam bem vindos ao Literalura!

--- LITERALURA ---
1 - Buscar livro por título
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em um ano
5 - Listar livros por idioma
0 - Sair

Selecione uma opção:

Ao sair da aplicação:

--- LITERALURA ---
1 - Buscar livro por título
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em um ano
5 - Listar livros por idioma
0 - Sair

Selecione uma opção: 0
Saindo...
Obrigado por acessar o Literalura!

---

🌐 Integração com a API Gutendex
A aplicação consome dados diretamente da API Gutendex, como no exemplo abaixo:

Exemplo de chamada:
GET https://gutendex.com/books/?search=tolstoy

Resposta:
{
  "results": [
    {
      "id": 123,
      "title": "War and Peace",
      "authors": [{"name": "Leo Tolstoy"}],
      "languages": ["en"],
      "download_count": 1500
    }
  ]
}

---

📌 Exemplos de Endpoints

Buscar livro por título
GET /livros/titulo/{titulo}

Buscar livros por idioma
GET /livros/idioma/{idioma}

Criar novo livro
POST /livros
Content-Type: application/json

{
  "titulo": "Dom Casmurro",
  "idioma": "pt",
  "numeroDeDownloads": 1500,
  "autor": {
    "nome": "Machado de Assis"
  }
}

---

📜 Licença
Este projeto é livre para uso sob a licença MIT.

---

📖 Desafio inspirado no curso Alura + Oracle Next Education.





