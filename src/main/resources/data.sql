--DROP TABLE IF EXISTS tb_user_role;
--DROP TABLE IF EXISTS tb_tweet;
--DROP TABLE IF EXISTS tb_user;
--DROP TABLE IF EXISTS tb_role;

INSERT IGNORE INTO tb_role(role_id, name) VALUES(1, 'admin'),(2, 'basic');