CREATE TABLE clients (
    client_number INT PRIMARY KEY NOT NULL UNIQUE,
    client_name TEXT NOT NULL
);

-- Initial client list
INSERT INTO clients VALUES(108, "Butler");
INSERT INTO clients VALUES(121, "Samuels");
INSERT INTO clients VALUES(188, "Bond");
INSERT INTO clients VALUES(107, "Chang");
INSERT INTO clients VALUES(122, "Baker");
INSERT INTO clients VALUES(111, "Davis");
INSERT INTO clients VALUES(203, "Zheng");
INSERT INTO clients VALUES(135, "Joe");