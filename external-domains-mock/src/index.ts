import { Hono } from "hono";
import { getCustomer, getOrder, getPayment } from "./generators";

const app = new Hono();

app.get("/health", (c) => c.json({ status: "ok" }));

app.get("/customers/:customerId", (c) => c.json(getCustomer(c.req.param("customerId"))));

app.get("/orders/:orderNumber", (c) => c.json(getOrder(c.req.param("orderNumber"))));

app.get("/payments/:orderNumber", (c) => c.json(getPayment(c.req.param("orderNumber"))));

const PORT = Number(process.env.PORT ?? 3001);

console.log(`external-domains-mock listening on http://localhost:${PORT}`);

export default {
  port: PORT,
  fetch: app.fetch,
};
