CREATE TABLE IF NOT EXISTS "academic_year" (
                                               id VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
                                               label VARCHAR(20) NOT NULL,
                                               "order" INT NOT NULL,
                                               promotion_id VARCHAR NOT NULL REFERENCES promotion(id),
                                               published BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE course
    ADD COLUMN IF NOT EXISTS mandatory BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE semester
    ADD COLUMN IF NOT EXISTS academic_year_id VARCHAR REFERENCES academic_year(id);

INSERT INTO academic_year (label, "order", promotion_id, published)
SELECT 'L1', 1, id, TRUE FROM promotion WHERE label = 'Promotion K'
UNION ALL
SELECT 'L2', 2, id, FALSE FROM promotion WHERE label = 'Promotion K'
UNION ALL
SELECT 'L3', 3, id, FALSE FROM promotion WHERE label = 'Promotion K';

UPDATE semester s
SET academic_year_id = ay.id
FROM academic_year ay
WHERE s.promotion_id = ay.promotion_id
  AND ay.label = 'L1'
  AND s.label IN ('S1', 'S2');

UPDATE semester s
SET academic_year_id = ay.id
FROM academic_year ay
WHERE s.promotion_id = ay.promotion_id
  AND ay.label = 'L2'
  AND s.label IN ('S3', 'S4');