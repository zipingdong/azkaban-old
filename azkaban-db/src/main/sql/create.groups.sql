DROP TABLE IF EXISTS groups;
CREATE TABLE groups
(
name                       varchar(32)       NOT      NULL,
roles                      varchar(64)       NOT      NULL      COMMENT '英文逗号分隔',
PRIMARY KEY (name)
)
ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='组对应角色表';


