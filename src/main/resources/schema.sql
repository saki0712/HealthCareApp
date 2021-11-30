CREATE TABLE user
(
   user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_name VARCHAR(100) NOT NULL UNIQUE,
   password VARCHAR(100) NOT NULL,
   authority VARCHAR(100)
);

CREATE TABLE condition
(
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_name INT NOT NULL REFERENCES user (user_name),
   day datetime NOT NULL,
   condition INT NOT NULL,
   menatl INT NOT NULL,
   achievement INT NOT NULL,
   memo VARCHAR(200) 
);