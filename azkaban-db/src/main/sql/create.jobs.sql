DROP TABLE IF EXISTS jobs;
CREATE TABLE jobs
(
project_name               varchar(64)    NOT      NULL  COMMENT '项目名',
job_name                   varchar(64)    NOT      NULL  COMMENT '作业名',
create_time                datetime       DEFAULT  NULL,
update_time                datetime       DEFAULT  NULL,
create_user                varchar(64)    DEFAULT  NULL,
update_user                varchar(64)    DEFAULT  NULL,
type                       varchar(64)    NOT      NULL  COMMENT '作业类型:command、hiveSql、......',
command                    text           NOT      NULL  COMMENT '作业命令',
dependencies               varchar(1024)  DEFAULT  NULL  COMMENT '依赖的作业（英文逗号分隔）',
warn_level                 tinyint        DEFAULT  1     COMMENT '报警级别：1，高；2，中；,3，低；4，不报警',
max_elapse                 smallint       DEFAULT  0     COMMENT '最大执行时长（分钟）',
retries                    tinyint        DEFAULT  0     COMMENT '重试次数',
retries_interval           int(7)         DEFAULT  0     COMMENT '重试间隔（毫秒）',
notify_user                varchar(32)    DEFAULT  ''    COMMENT '失败时通知的用户',
proxy_user                 varchar(32)    DEFAULT  ''    COMMENT '代理执行用户',
PRIMARY KEY (project_name, job_name)
)
ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='作业信息表';
