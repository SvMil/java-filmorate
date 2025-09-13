DROP TABLE IF EXISTS PUBLIC."USER" CASCADE;
DROP TABLE IF EXISTS PUBLIC.FRIENDS CASCADE;
DROP TABLE IF EXISTS PUBLIC.MPA CASCADE;
DROP TABLE IF EXISTS PUBLIC.FILM CASCADE;
DROP TABLE IF EXISTS PUBLIC.GENRE CASCADE;
DROP TABLE IF EXISTS PUBLIC.FILM_GENRE CASCADE;
DROP TABLE IF EXISTS PUBLIC.FILM_LIKE CASCADE;

CREATE TABLE IF NOT EXISTS PUBLIC."USER" (
                                             USER_ID INTEGER NOT NULL AUTO_INCREMENT,
                                             EMAIL CHARACTER VARYING(30) NOT NULL,
                                             LOGIN CHARACTER VARYING(30) NOT NULL,
                                             BIRTHDAY DATE NOT NULL,
                                             NAME CHARACTER VARYING(30) NOT NULL,
                                             CONSTRAINT USER_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
                                                 FIRST_USER_ID INTEGER NOT NULL,
                                                 SECOND_USER_ID INTEGER NOT NULL,
                                                 APPROVE BOOLEAN,
                                                 CONSTRAINT FRIENDS_FK FOREIGN KEY (FIRST_USER_ID) REFERENCES PUBLIC."USER"(USER_ID)
                                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                                 CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (SECOND_USER_ID) REFERENCES PUBLIC."USER"(USER_ID)
                                                     ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (
                                            GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
                                            GENRE_NAME CHARACTER VARYING(30) NOT NULL,
                                            CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.MPA (
                                             MPA_ID INTEGER NOT NULL AUTO_INCREMENT,
                                             RATING CHARACTER VARYING(30) NOT NULL,
                                             CONSTRAINT MPA_PK PRIMARY KEY (MPA_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM (
                                           FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
                                           NAME CHARACTER VARYING(30) NOT NULL,
                                           DESCRIPTION CHARACTER VARYING(100) NOT NULL,
                                           RELEASE_DATE DATE NOT NULL,
                                           DURATION INTEGER NOT NULL,
                                           MPA_ID INTEGER NOT NULL,
                                           CONSTRAINT FILM_PK PRIMARY KEY (FILM_ID),
                                           CONSTRAINT FILM_FK FOREIGN KEY (MPA_ID) REFERENCES PUBLIC.MPA(MPA_ID)
                                               ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_LIKE (
                                                FILM_ID INTEGER NOT NULL,
                                                USER_ID INTEGER NOT NULL,
                                                CONSTRAINT FILM_LIKE_PK PRIMARY KEY (USER_ID,FILM_ID),
                                                CONSTRAINT FILM_LIKE_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(FILM_ID)
                                                    ON DELETE CASCADE ON UPDATE CASCADE,
                                                CONSTRAINT FILM_LIKE_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC."USER"(USER_ID)
                                                    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRE (
                                                 FILM_ID INTEGER NOT NULL,
                                                 GENRE_ID INTEGER NOT NULL,
                                                 CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID, GENRE_ID),
                                                 CONSTRAINT FILM_GENRE_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(FILM_ID)
                                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                                 CONSTRAINT FILM_GENRE_FK_1 FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(GENRE_ID)
                                                     ON DELETE CASCADE ON UPDATE CASCADE
);