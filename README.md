# POC Aftersale + RabbitMQ

POC para processamento de pedidos pós-venda com RabbitMQ, MongoDB e integrações corporativas mockadas.

## Pré-requisitos

- Docker e Docker Compose
- [Bun](https://bun.sh) (para o simulador de carga)

Crie o arquivo de ambiente antes de subir os composes:

```bash
cat > .docker/.env <<'EOF'
TIMEZONE=America/Sao_Paulo
MOCK_PORT=3001
EOF
```

---

## Desenvolvimento

Stack com hot reload (Maven), debug na porta `5005` e API em `http://localhost:8080`.

```bash
# subir
.docker/scripts/develop up -d --build

# logs
.docker/scripts/develop logs -f app

# parar
.docker/scripts/develop down
```

---

## POC (carga)

Stack com 3 instâncias da aplicação atrás de nginx, simulando ambiente de carga.

```bash
# subir
.docker/scripts/develop-poc up -d --build

# logs (nginx + backends)
.docker/scripts/develop-poc logs -f

# parar
.docker/scripts/develop-poc down
```

API disponível em `http://localhost:8080`.

---

## Simulador de carga

Publica pedidos avançando pelos status via `POST /triggers/publish/order`.

```bash
# instalar dependências (primeira vez)
bun install

# execução padrão (20 pedidos, 5 em paralelo)
bun run load

# exemplo com parâmetros
bun load-simulator.ts --orders 50 --concurrency 10 --delay-ms 500
```

| Opção | Default | Descrição |
|---|---|---|
| `--base-url` | `http://localhost:8080` | URL da API |
| `--orders` | `20` | Quantidade de pedidos |
| `--concurrency` | `5` | Pedidos em paralelo |
| `--delay-ms` | `750` | Intervalo entre status |
| `--cancel-rate` | `0.1` | Fração de pedidos cancelados |

---

## Endpoints úteis

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/triggers/publish/order` | Publica evento de pedido na fila |
| `GET` | `/aftersale/{orderNumber}` | Consulta pedido com timeline |

RabbitMQ Management: `http://localhost:15672` (`root` / `root`)
