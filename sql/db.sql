CREATE TABLE BUGS (
  ID INT IDENTITY,
  TEXT VARCHAR(300) NOT NULL,
  DESCRIPTION VARCHAR(1000) NOT NULL,
  AUTHOR_ID INT NOT NULL,
  CREATE_TIME TIMESTAMP NOT NULL,
  STATUS VARCHAR(20) NOT NULL CHECK (STATUS IN ('OPENED', 'ASSIGNED', 'RESOLVED', 'REOPENED', 'FIXED')),
  ASSIGNED_ID INT,
  PRIORITY INT NOT NULL CHECK (PRIORITY IN (0, 1, 2))
);

CREATE TABLE BUG_HISTORY (
  ID INT IDENTITY,
  BUG_ID INT NOT NULL,
  USER_ID INT NOT NULL,
  CHANGE_TIME TIMESTAMP NOT NULL,
  STATUS_OLD VARCHAR(20) NOT NULL,
  STATUS_NEW VARCHAR(20) NOT NULL,
  ASSIGNED_OLD INT,
  ASSIGNED_NEW INT,
  COMMENT_TEXT VARCHAR(300)
);

CREATE TABLE USERS (
  ID INT IDENTITY,
  NAME VARCHAR(100) NOT NULL,
  USER_ROLE VARCHAR(20) NOT NULL CHECK (USER_ROLE IN ('MANAGER', 'DEVELOPER', 'TESTER')),
  LOGIN VARCHAR(100) NOT NULL,
  PASS_HASH VARCHAR(100)
);

ALTER TABLE BUGS ADD CONSTRAINT FK_BUG_AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID);
ALTER TABLE BUGS ADD CONSTRAINT FK_BUG_ASSIGNED FOREIGN KEY (ASSIGNED_ID) REFERENCES USERS (ID);

ALTER TABLE BUG_HISTORY ADD CONSTRAINT FK_HISTORY_BUG FOREIGN KEY (BUG_ID) REFERENCES BUGS (ID) ON DELETE CASCADE;
ALTER TABLE BUG_HISTORY ADD CONSTRAINT FK_HISTORY_USER FOREIGN KEY (USER_ID) REFERENCES USERS (ID);
ALTER TABLE BUG_HISTORY ADD CONSTRAINT FK_HISTORY_ASS_OLD FOREIGN KEY (ASSIGNED_OLD) REFERENCES USERS (ID);
ALTER TABLE BUG_HISTORY ADD CONSTRAINT FK_HISTORY_ASS_NEW FOREIGN KEY (ASSIGNED_NEW) REFERENCES USERS (ID);

INSERT INTO USERS (LOGIN, PASS_HASH, NAME, USER_ROLE) VALUES ('me', HASH('MD5', STRINGTOUTF8('1234'), 1), 'Я', 'MANAGER');

INSERT INTO BUGS
  (TEXT, DESCRIPTION, AUTHOR_ID, CREATE_TIME, STATUS, PRIORITY)
  VALUES
  ('Написать программу', 'Написать программу', 1, CURRENT_TIMESTAMP, 'OPENED', 1);
