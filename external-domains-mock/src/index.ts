import { Hono } from "hono";
import { getCustomer, getOrder, getPayment, getProduct, setOrder } from "./generators";
import type { Order } from "./types";

const app = new Hono();

app.get("/health", (c) => c.json({ status: "ok" }));

app.get("/customers/:customerId", (c) => c.json(getCustomer(c.req.param("customerId"))));

app.get("/orders/:orderNumber", (c) => c.json(getOrder(c.req.param("orderNumber"))));

app.put("/orders/:orderNumber", async (c) => {
  const payload = await c.req.json<Order>();
  setOrder(payload);
  return c.body(null, 204);
});

app.get("/payments/:orderNumber", (c) => c.json(getPayment(c.req.param("orderNumber"))));

app.get("/products/:sku", (c) => c.json(getProduct(c.req.param("sku"))));

const PORT = Number(process.env.PORT ?? 3001);

console.log(`external-domains-mock listening on http://localhost:${PORT}`);

export default {
  port: PORT,
  fetch: app.fetch,
};
