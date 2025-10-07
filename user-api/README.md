## Leia-me!

Arquiteturas que é necessário "conciliar".

1. Three-layer Architecture
2. Model-View-Controller Architecture


### Requisição/Feature


```plain
Request                    Response
⬇️   +-----------------------+ ⬆️
⬇️   | Camada 1              | ⬆️
⬇️   +-----------------------+ ⬆️
⬇️   | Camada 2              | ⬆️
⬇️   +-----------------------+ ⬆️
⬇️   | Camada n...           | ⬆️
⬇️   +-----------------------+ ⬆️
```

### Three Layer Architecture

```plain
+-----------------------+
| Camada 1: Controller  |
+-----------------------+
| Camada 2: Domain      |
+-----------------------+
| Camada 3: Repository  |
+-----------------------+
```

### OBJECT/RELATIONAL MAPPING (O/RM)

RDBMS
Relational DataBase Management System

É um tipo de mapping (mapeamento):
mapear um objeto para outro, objeto para outro tipo de dado, um tipo de dado qualquer para outro tipo dado qualquer.

```json
{
    "id": 3435,
    "email": "user@mail.com",
    "username": "userhandle",
    "createdAt": "2025-10-01"
}
```

```json
{
    "user": {
        "user_id": 3435,
        "email": {
            "handle": "user",
            "server": "mail.com"
        }
    }
}
```

```xml
<user id="3435">
    <email>user@mail.com</email>
    <username>userhandle</username>
    <created-at>2025-10-01</created-at>
</user>
```

Padrão Mapper:
<https://martinfowler.com/eaaCatalog/mapper.html>

Mapper para **Objeto <-> Relacional**.

Modelo Objeto

```java
class User {
    Long id;
    String email;
    LocalDateTime createdAt;
}
```

```csharp
class User {
    long Id {get;set;}
    string Email {get;set;}
    DateTime CreatedAt { get; set; }
    string Nome {get;set;}
    byte[] Avatar {get;set;}

}
```

Relacional


```sql
CREATE TABLE users ( -- RELAÇÃO DE USUÁRIOS
    NUMBER id               NOT NULL
                            PRIMARY KEY AUTO_INCREMENT,
    VARCHAR(255) EMAIL      NOT NULL UNIQUE,
    TIMESTAMP    CREATED_AT NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE profiles (
    user_id NUMBER FOREIGN KEY REFERENCES users(id),
    nome VARCHAR(255),
    avatar bytea 
);
```

E se usarmos NoSQL, ex.: banco orientado a documentos (ex.: Mongo, CouchDB,...), pares chave-valor, objetos, grafo, etc.

O/R M? O/D M (Object/Document Mapping).

- Evitar escrever SQL
- Não ficar dependente da sintaxe
- Gerenciar os relacionamentos

#### ORM: framework

Plataforma

(C#) .NET: EntityFramework (de facto standard)  (DAP, NHibernate, ...)
Node.JS: Prisma, Sequelize, TypeORM, Drizzle ...
Java: JOOQ, JPA (de facto standard)

Java
Persistence
API

Hibernate implementa o JPA
EclipseLink implementa o JPA

JPA é estendido pelo Spring (Spring Data JPA)

Exemplo:

Ilhas (Island) de trabalho
    Mesa pair, square, rectangle, triangle
    Workstation (estação de trabalho)

