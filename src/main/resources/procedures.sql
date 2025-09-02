-- Search in a specific table the columns that match
DELIMITER $$

DROP PROCEDURE IF EXISTS GetRowByColumnValue;
CREATE PROCEDURE GetRowByColumnValue(
    IN SelectColumns VARCHAR(255),
    IN TableName VARCHAR(128),
    IN ColumnName VARCHAR(128),
    IN SearchValue VARCHAR(255)
        -- Comma-separated list of columns to select
)
BEGIN
    SET @query = CONCAT('SELECT ', SelectColumns,' FROM ', TableName , ' WHERE ', ColumnName , ' = ' , SearchValue);

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;