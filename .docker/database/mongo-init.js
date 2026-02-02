db = db.getSiblingDB('poc');
db.createUser(
  {
    user: 'root',
    pwd: 'root',
    roles: [{ role: 'readWrite', db: 'poc' }],
  },
);