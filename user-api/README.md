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