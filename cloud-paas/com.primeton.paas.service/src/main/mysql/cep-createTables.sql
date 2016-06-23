
create table cep_eps_instance ( 
	id varchar(64) not null, 
	enable varchar(64), 
	eps varchar(4096), 
	event_name varchar(256), 
	listeners varchar(2048), 
	create_time BIGINT(13) default 0, 
	start_time BIGINT(13) default 0, 
	primary key (id) 
) ENGINE=InnoDB default CHARSET=utf8;

create table cep_id_table(
    table_name varchar(128),
    begin_id integer(12) not null,
    increment integer(12) default 1000,
    next_id integer(12) not null,
    primary key (table_name)
) ENGINE=InnoDB default CHARSET=utf8;

