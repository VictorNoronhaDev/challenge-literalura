
# Literalura (CLI) — Java + Spring + PostgreSQL + Gutendex

Aplicativo **linha de comando** que consulta a **API Gutendex** para importar livros e autores e persiste em **PostgreSQL**. Feito com **Spring Boot (sem web)**.

## ⚙️ Requisitos
- Java 17+ (recomendado 21)
- Maven 3.9+
- PostgreSQL 13+ (local ou Docker)

## 🔧 Configuração via **variáveis de ambiente**
Você **não precisa editar** `application.properties`. A aplicação lê estas variáveis:

```
DB_HOST            # ex.: localhost
DB_PORT            # ex.: 5432
DB_NAME            # ex.: literalura
DB_USER            # ex.: postgres
DB_PASSWORD        # ex.: postgres
```

### macOS / Linux
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=literalura
export DB_USER=postgres
export DB_PASSWORD=postgres

./mvnw spring-boot:run
```

### Windows (PowerShell)
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="literalura"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres"

mvnw spring-boot:run
```

### Windows (CMD)
```cmd
set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME_LITERALURA=literalura
set DB_USER=postgres
set DB_PASSWORD=postgres

mvnw spring-boot:run
```

## 🧭 Menu
1. Buscar livro pelo **título exato** (Gutendex) → salva e exibe **Título, Autor(es), Idioma, Downloads**  
2. Listar livros cadastrados  
3. Listar autores cadastrados (+ títulos)  
4. Listar autores vivos em um ano  
5. Listar livros por idioma (`es`, `en`, `fr`, `pt`)