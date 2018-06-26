--数据库初始化脚本

--创建数据库
create database seckill;
--use
use seckill;
--创建秒杀库存表
create table seckill(
seckill_id bigint not null auto_increment comment 'id',
name varchar(120) not null comment '商品名称',
number int not null comment '库存数量',
start_time timestamp not null comment '秒杀开始时间',
end_time timestamp  not null comment '秒杀结束时间',
create_time timestamp not null default current_timestamp comment '创建时间 ',
primary key (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 comment='秒杀库存表';

--初始化数据
insert into seckill(name,number,start_time,end_time)
values('只要998女朋友带回家','100','2018-06-11 00:00:00','2018-06-29 00:00:00'),
      ('只要138抽水马桶带回家','100','2018-06-11 00:00:00','2018-06-29 00:00:00'),
      ('只要100大黄蜂带回家','100','2018-06-11 00:00:00','2018-06-29 00:00:00'),
      ('只要998兰博基尼带回家','100','2018-06-11 00:00:00','2018-06-29 00:00:00'),
      ('只要1998劳斯莱斯回家','100','2018-06-11 00:00:00','2018-06-29 00:00:00');

create table success_killed(
seckill_id bigint not null comment '秒杀商品id',
user_phone bigint not null comment '用户手机号',
state tinyint not null default -1 comment '状态标识：-1：无效 0：成功 1：已付款',
create_time timestamp  not null comment '创建时间',
primary key (seckill_id,user_phone),/*联合主键*/
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='秒杀成功明细表';

--连接数据库控制台
mysql -uroot -p root

--记录每次上线的ddl修改