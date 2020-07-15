
/*


Table for the ECHO Inventory A









*/



CREATE TABLE PLANT
(
  Price INT NOT NULL,
  itemName INT NOT NULL,
  itemID INT NOT NULL,
  PRIMARY KEY (itemID)
);

CREATE TABLE TAGS
(
  TagID INT NOT NULL,
  TagName INT NOT NULL,
  PRIMARY KEY (TagID)
);

CREATE TABLE COHORT
(
  DatePlanted INT NOT NULL,
  method INT NOT NULL,
  numDeceased INT NOT NULL,
  Total INT NOT NULL,
  itemID INT NOT NULL,
  PRIMARY KEY (itemID),
  FOREIGN KEY (itemID) REFERENCES PLANT(itemID)
);

CREATE TABLE NEWCHANGE
(
  DateOfChange INT NOT NULL,
  NumChange INT NOT NULL,
  Sale_or_Death INT NOT NULL,
  userID INT NOT NULL,
  itemID INT NOT NULL,
  PRIMARY KEY (userID, itemID),
  FOREIGN KEY (itemID) REFERENCES COHORT(itemID)
);

CREATE TABLE ITEM_IMAGE
(
  imageLinks INT NOT NULL,
  itemID INT NOT NULL,
  PRIMARY KEY (itemID),
  FOREIGN KEY (itemID) REFERENCES PLANT(itemID)
);

CREATE TABLE plant_tags
(
  itemID INT NOT NULL,
  TagID INT NOT NULL,
  PRIMARY KEY (itemID, TagID),
  FOREIGN KEY (itemID) REFERENCES PLANT(itemID),
  FOREIGN KEY (TagID) REFERENCES TAGS(TagID)
);

CREATE TABLE PLANT_OtherNames
(
  OtherNames INT NOT NULL,
  itemID INT NOT NULL,
  PRIMARY KEY (OtherNames, itemID),
  FOREIGN KEY (itemID) REFERENCES PLANT(itemID)
);

CREATE TABLE ITEMS_AVAILABLE
(
  Amount INT NOT NULL,
  itemID INT NOT NULL,
  PRIMARY KEY (itemID),
  FOREIGN KEY (itemID) REFERENCES COHORT(itemID)
);