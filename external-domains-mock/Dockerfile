FROM oven/bun:1.3-alpine

WORKDIR /app

COPY package.json bun.lock ./
RUN bun install --frozen-lockfile --production

COPY src ./src

ENV PORT=3001
EXPOSE 3001

CMD ["bun", "run", "start"]
