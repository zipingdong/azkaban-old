DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
name                       varchar(32)       NOT      NULL,
permissions                varchar(64)       NOT      NULL      COMMENT '英文逗号分隔',
PRIMARY KEY (name)
)
ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色表';


