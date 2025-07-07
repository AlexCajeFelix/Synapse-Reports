CREATE TABLE reports (
    id CHAR(36) PRIMARY KEY,
    product VARCHAR(100) NOT NULL,
    sale_date DATE NOT NULL,
    details TEXT
);

INSERT INTO reports (id, product, sale_date, details) VALUES
(UUID(), 'Celular A1', '2025-06-01', 'Vendido no dia 01/06, categoria A'),
(UUID(), 'Notebook B2', '2025-06-10', 'Vendido no dia 10/06, categoria B'),
(UUID(), 'Tablet C3', '2025-05-15', 'Vendido no dia 15/05, categoria C');
