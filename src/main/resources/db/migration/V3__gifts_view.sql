CREATE VIEW gift_availability AS
SELECT
    g.id,
    g.description,
    g.picture,
    g.price,
    g.stock,
    g.created_at,
    g.stock - COUNT(o.id) AS remain,
    (g.stock - COUNT(o.id)) > 0 AS availability,
    g.wedding_id,
    g.is_active
FROM gifts g
LEFT JOIN orders o
    ON o.gift_id = g.id
    AND o.status = 'PAID'
    AND o.is_active = TRUE
WHERE g.is_active = TRUE
GROUP BY
    g.id,
    g.description,
    g.picture,
    g.price,
    g.stock,
    g.created_at,
    g.wedding_id,
    g.is_active;
