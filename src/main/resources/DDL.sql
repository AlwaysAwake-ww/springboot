
-- member table
CREATE TABLE MEMBER (
	member_index		number(10)		primary key,
    member_email		varchar2(200)		NOT NULL,
	member_password	    varchar2(200)		NOT NULL,
	member_name	   		varchar2(200)		NOT NULL,
    member_role 		varchar2(200)       NOT NULL,
    member_introduction varchar2(400),
        join_date       varchar2(10)                NOT NULL,
    member_image        varchar2(200)
);


CREATE TABLE POST (
                      post_index			number(10)		primary key,
                      post_title			varchar2(200)		NOT NULL,
                      post_content	    varchar2(4000)		NOT NULL,
                      post_date           varchar2(10)            NOT NULL,
                      post_temp_url     varchar2(200),
                      member_index	    number(10)		NOT NULL,
                      team_index        number(10),
                      CONSTRAINT post_foreign_member_index FOREIGN KEY(member_index) REFERENCES MEMBER(member_index) on delete cascade,
                      CONSTRAINT post_foreign_team_index FOREIGN KEY(team_index) REFERENCES TEAM(team_index)
);


CREATE TABLE TEAM (
                      team_index			number(10)		primary key,
                      team_name			varchar2(200)		NOT NULL
);

CREATE TABLE TEAM_MEMBER (
                      team_member_index			number(10)		primary key,
                      member_index			number(10)		NOT NULL,
                      team_index            number(10)      NOT NULL,
                      CONSTRAINT team_member_foreign_member FOREIGN KEY(member_index) REFERENCES MEMBER(member_index),
                      CONSTRAINT team_member_foreign_team FOREIGN KEY(team_index) REFERENCES TEAM(team_index) on delete cascade
);


CREATE TABLE IMAGE (
                      image_index			number(10)		primary key,
                      origin_name			varchar2(200)		NOT NULL,
                      new_name	            varchar2(200)		NOT NULL,
                      image_path	        varchar2(200)		NOT NULL,
                      image_ext             varchar2(200)       NOT NULL,
                      post_index            number(10)          NOT NULL,
                      CONSTRAINT image_foreign_post_index FOREIGN KEY(post_index) REFERENCES POSt(post_index) on delete cascade
);


-- member sequence
 create sequence MEMBER_SEQ
 increment by 1
 start with 1
 minvalue 1
 maxvalue 9999999999
 noorder
 ;


-- POST sequence
create sequence POST_SEQ
    increment by 1
    start with 1
    minvalue 1
    maxvalue 9999999999
    noorder
;


-- TEAM sequence
create sequence TEAM_SEQ
    increment by 1
    start with 1
    minvalue 1
    maxvalue 9999999999
    noorder
;

-- TEAM_MEMBER sequence
create sequence TEAM_MEMBER_SEQ
    increment by 1
    start with 1
    minvalue 1
    maxvalue 9999999999
    noorder
;

-- IMAGE sequence
create sequence IMAGE_SEQ
    increment by 1
    start with 1
    minvalue 1
    maxvalue 9999999999
    noorder
;


