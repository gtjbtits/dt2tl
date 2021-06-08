create table TLGRM_USERS
(
    ID bigserial not null
        primary key,
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    TLGRM_USER_ID BIGINT not null
);

create table TRIBES
(
    ID bigserial not null
        primary key,
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    ACTIVE BOOLEAN not null default false,
    NAME text not null
);

create table TEAMS
(
    ID bigserial not null
        primary key,
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    NAME text not null,
    TRIBE_ID BIGINT not null,
    constraint team_name_unique_across_tribe
        unique (NAME, TRIBE_ID),
    constraint team_tribe_fk
        foreign key (TRIBE_ID) references TRIBES (ID)
);

create table FELLOWS
(
    ID bigserial not null
        primary key,
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    NAME text,
    TEAM_ID BIGINT,
    constraint fellow_team_fk
        foreign key (TEAM_ID) references TEAMS (ID)
);

create table TLGRM_CHATS
(
    ID bigserial not null
        primary key,
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    TLGRM_CHAT_ID BIGINT not null,
    TRIBE_ID BIGINT not null,
    constraint tlgrm_chat_id_unique
        unique (TLGRM_CHAT_ID),
    constraint tlgrm_chat_tribe_fk
        foreign key (TRIBE_ID) references TRIBES (ID)
);

create table TLGRM_USER_FELLOWS
(
    ID bigserial not null
        primary key,
    CREATED TIMESTAMP,
    UPDATED TIMESTAMP,
    FELLOW_ID BIGINT not null,
    TU_ID BIGINT not null,
    TRIBE_ID BIGINT not null,
    constraint tuf_tlgrm_user_fk
        foreign key (TU_ID) references TLGRM_USERS (ID),
    constraint tuf_fellow_fk
        foreign key (FELLOW_ID) references FELLOWS (ID),
    constraint tuf_tu_must_be_unique_in_tribe
        unique (TU_ID, TRIBE_ID)
);

