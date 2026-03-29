create table if not exists tb_user (
    id bigint primary key auto_increment,
    username varchar(20) not null unique,
    password varchar(100) not null,
    nickname varchar(20) not null,
    phone varchar(11),
    avatar varchar(100),
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp
);

create table if not exists tb_task (
    id bigint primary key auto_increment,
    user_id bigint not null,
    title varchar(50) not null,
    description varchar(255),
    expected_focus_minutes int not null default 25,
    finished boolean not null default false,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp,
    constraint fk_task_user foreign key (user_id) references tb_user(id)
);

create table if not exists tb_focus_record (
    id bigint primary key auto_increment,
    user_id bigint not null,
    task_id bigint,
    planned_minutes int not null default 25,
    actual_minutes int not null default 0,
    status varchar(20) not null,
    started_at datetime not null,
    ended_at datetime,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp,
    constraint fk_focus_user foreign key (user_id) references tb_user(id),
    constraint fk_focus_task foreign key (task_id) references tb_task(id)
);

create table if not exists tb_check_in_record (
    id bigint primary key auto_increment,
    user_id bigint not null,
    check_in_date date not null,
    streak_days int not null default 1,
    created_at datetime not null default current_timestamp,
    unique key uk_check_in_user_date (user_id, check_in_date),
    constraint fk_check_in_user foreign key (user_id) references tb_user(id)
);

