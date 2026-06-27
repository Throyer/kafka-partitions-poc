db = db.getSiblingDB('tracking');
db.createUser(
  {
    user: 'root',
    pwd: 'root',
    roles: [{ role: 'readWrite', db: 'tracking' }],
  },
);

db = db.getSiblingDB('composition');
db.createUser(
  {
    user: 'root',
    pwd: 'root',
    roles: [{ role: 'readWrite', db: 'composition' }],
  },
);

db = db.getSiblingDB('sender');
db.createUser(
  {
    user: 'root',
    pwd: 'root',
    roles: [{ role: 'readWrite', db: 'sender' }],
  },
);

db = db.getSiblingDB('after_sale');
db.createUser(
  {
    user: 'root',
    pwd: 'root',
    roles: [{ role: 'readWrite', db: 'after_sale' }],
  },
);