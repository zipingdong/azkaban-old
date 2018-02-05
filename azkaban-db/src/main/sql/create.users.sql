DROP TABLE IF EXISTS users;
CREATE TABLE users
(
user_id                    varchar(32)       NOT      NULL,
password                   varchar(32)       NOT      NULL,
email                      varchar(32)       NOT      NULL,
phone                      varchar(16)       DEFAULT  NULL,
groups                     varchar(64)       DEFAULT  NULL  COMMENT '英文逗号分隔',
roles                      varchar(64)       DEFAULT  NULL  COMMENT '英文逗号分隔',
PRIMARY KEY (user_id)
)
ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户信息表';

