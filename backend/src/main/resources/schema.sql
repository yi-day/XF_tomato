create table if not exists tb_user (
    id bigint primary key auto_increment,
    account varchar(50) not null unique comment '账号',
    password varchar(100) not null comment '密码',
    nickname varchar(50) not null comment '昵称',
    name varchar(50) comment '真实姓名',
    birth_date date comment '出生日期',
    school varchar(100) comment '学校',
    major varchar(100) comment '专业',
    grade varchar(20) comment '年级',
    subjects varchar(255) comment '学科',
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp
);

create table if not exists tb_task (
    id bigint primary key auto_increment,
    user_id bigint not null,
    title varchar(50) not null comment '任务标题',
    priority varchar(20) not null default 'MEDIUM' comment '优先级: HIGH, MEDIUM, LOW',
    estimated_pomodoros int not null default 1 comment '预估番茄数',
    deadline date comment '截止日期',
    completed boolean not null default false comment '是否已完成',
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp,
    constraint fk_task_user foreign key (user_id) references tb_user(id)
);

create table if not exists tb_focus_record (
    id bigint primary key auto_increment,
    user_id bigint not null,
    task_id bigint,
    mode varchar(20) not null default 'focus' comment '模式: focus, shortBreak, longBreak',
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

