CREATE TABLE comments (
  id INT NOT NULL AUTO_INCREMENT,
  MYUSER VARCHAR(30) NOT NULL,
  EMAIL VARCHAR(30),
  WEBPAGE VARCHAR(100) NOT NULL,
  DATUM DATE NOT NULL,
  SUMMARY VARCHAR(40) NOT NULL,
  PRIMARY KEY (ID)
);