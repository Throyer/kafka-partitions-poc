export type Customer = {
  id: string;
  name: string;
  email: string;
};

export type OrderStatus = {
  id: number;
  name: string;
};

export type HistoricStatus = {
  creationDate: string;
  status: OrderStatus;
};

export type OrderItem = {
  productId: number;
  totalItem: number;
  quantity: number;
};

export type Order = {
  customerId: string;
  orderNumber: number;
  status: OrderStatus;
  historicStatus: HistoricStatus[];
  items: OrderItem[];
};

export type Payment = {
  id: string;
  paymentType: string;
  totalValue: number;
};

export type Product = {
  sku: number;
  name: string;
  description: string;
  imageUrl: string;
};
