insert into tribes (id, name, active) values (1, 'TestTribe', true);
insert into teams (id, name, tribe_id) values (1, 'Team1', 1);
insert into teams (id, name, tribe_id) values (2, 'Team2', 1);
insert into fellows (id, name, team_id) values (1, 'Fellow1_1', 1);
insert into fellows (id, name, team_id) values (2, 'Fellow1_2', 1);
insert into fellows (id, name, team_id) values (3, 'Fellow2_1', 2);
insert into fellows (id, name, team_id) values (4, 'Fellow2_2', 2);

insert into tlgrm_chats (id, tlgrm_chat_id, tribe_id) values (1, 1000, 1);
insert into tlgrm_users (id, tlgrm_user_id) values (1, 1000);
insert into tlgrm_users (id, tlgrm_user_id) values (2, 2000);
insert into tlgrm_users (id, tlgrm_user_id) values (3, 3000);
insert into tlgrm_users (id, tlgrm_user_id) values (4, 4000);
insert into tlgrm_user_fellows (id, tu_id, fellow_id) values (1, 1, 1);
insert into tlgrm_user_fellows (id, tu_id, fellow_id) values (2, 2, 2);
insert into tlgrm_user_fellows (id, tu_id, fellow_id) values (3, 3, 3);
insert into tlgrm_user_fellows (id, tu_id, fellow_id) values (4, 4, 4);