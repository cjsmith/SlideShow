# Users schema
 
# --- !Ups
 
CREATE TABLE Slide (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    markdown VARCHAR(2048) NOT NULL,
    position INT NOT NULL,
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE Slide;
