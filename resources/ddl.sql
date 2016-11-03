CREATE TABLE unit (
  id INTEGER PRIMARY KEY ASC,
  name TEXT UNIQUE NOT NULL,
  abr1000 TEXT NULL,
  abr1 TEXT NULL
);

CREATE TABLE raw_material (
  id INTEGER PRIMARY KEY ASC,
  name TEXT UNIQUE NOT NULL,
  unitID INT NOT NULL,
  FOREIGN KEY (unitID) REFERENCES unit(id)
);

CREATE TABLE recipe (
  id INTEGER PRIMARY KEY ASC,
  name TEXT UNIQUE NOT NULL
);

CREATE TABLE recipe_line (
  recipeID INT NOT NULL,
  rawMaterialID INT NOT NULL,
  quantity FLOAT NOT NULL,
  PRIMARY KEY (recipeID, rawMaterialID),
  FOREIGN KEY (recipeID) REFERENCES recipe(id),
  FOREIGN KEY (rawMaterialID) REFERENCES raw_material(id)
);

CREATE TABLE stock (
  id INTEGER PRIMARY KEY ASC,
  rawMaterialID INT NOT NULL,
  date TEXT NOT NULL,
  quantity FLOAT NOT NULL,
  UNIQUE (rawMaterialID, date),
  FOREIGN KEY (rawMaterialID) REFERENCES raw_material(id)
);

CREATE TABLE journal (
  id INTEGER PRIMARY KEY ASC,
  recipeID INT NOT NULL,
  date TEXT NOT NULL,
  multiplier FLOAT NOT NULL DEFAULT 1.0,
  FOREIGN KEY (recipeID) REFERENCES recipe(id)
);

CREATE TABLE stock_modification (
  id INTEGER PRIMARY KEY ASC,
  rawMaterialID INT NOT NULL,
  date TEXT NOT NULL,
  quantity FLOAT NOT NULL,
  description TEXT DEFAULT '' NOT NULL,
  UNIQUE (rawMaterialID, date),
  FOREIGN KEY (rawMaterialID) REFERENCES raw_material(id)
);

CREATE VIEW stock_view AS
  SELECT rm.id AS rawMaterialID, s.id AS stockID, rm.unitID AS unitID, rm.name AS name,
    s.quantity AS lastQuantityInStock,
    s.date AS lastDateInStock, 0
  FROM raw_material rm, stock s
  WHERE s.rawMaterialID = rm.id
    AND s.date = (SELECT MAX(DATE(date)) FROM stock
                    WHERE rawMaterialID = rm.id);

INSERT INTO unit (name, abr1000, abr1) VALUES ('kilogrammes', 'kg', 'g');
INSERT INTO unit (name, abr1000, abr1) VALUES ('litres', 'L', 'mL');