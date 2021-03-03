create table TEAM
(
    ID BIGINT not null
        primary key,
    NAME VARCHAR(255)
);

create table DEVELOPER
(
    ID BIGINT not null
        primary key,
    USERNAME VARCHAR(255),
    TEAM_ID BIGINT,
    constraint FKB0AWDUPI48YB8632O6JSCHHQW
        foreign key (TEAM_ID) references TEAM (ID)
);

insert into TEAM values (1, 'VAS-Партнеры');
insert into TEAM values (2, 'VAS-Развитие');
insert into TEAM values (3, 'VAS-Технологии');
insert into TEAM values (4, 'VAS-Интеграции');

insert into DEVELOPER values (1, 'VoroninMaxim', 1);
insert into DEVELOPER values (2, 'SeregAnto', 1);
insert into DEVELOPER values (3, 'qazpoikw', 2);
insert into DEVELOPER values (4, 'T1ma98', 2);
insert into DEVELOPER values (5, 'menshikov_m_o', 2);
insert into DEVELOPER values (6, 'vladpushkarev', 3);
insert into DEVELOPER values (7, 'damimoor', 3);
insert into DEVELOPER values (8, 'malikin_t', 4);
insert into DEVELOPER values (9, 'dimok106', 4);
insert into DEVELOPER values (10, 'gtjbtits', 2);
