CREATE DATABASE IF NOT EXISTS master CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE master;

create table attack_defense_audit
(
    id           int auto_increment
        primary key,
    aid          int         null comment '操作管理员的id',
    rid          int         null comment '对应的攻防记录id',
    operator     varchar(50) null,
    is_passed    tinyint(1)  null comment '是否通过审核',
    comment      text        null comment '批注',
    create_time  datetime    null comment '审核进行时间',
    is_history   tinyint(1)  null comment '是否是历史记录',
    record_title text        null,
    score        double      null comment '评分',
    level        text        null comment '风险评级'
)
    comment '攻防演练审批记录';

create table attack_defense_member
(
    id          int auto_increment
        primary key,
    tid         int          null comment '所属队伍id',
    avatar      text         null comment '用户头像',
    name        varchar(50)  null,
    phone       varchar(50)  null,
    create_time datetime     null,
    email       varchar(50)  null,
    department  varchar(50)  null comment '所属部门名称',
    office      varchar(50)  null comment '职务',
    state       tinyint      null comment '1:活跃.2:暂停，3：失效,4:在线，5：禁用',
    role        varchar(20)  null comment 'admin,umpire，attacker,defender',
    password    varchar(255) null comment '加密的密码',
    constraint attack_defense_member_pk
        unique (phone),
    constraint attack_defense_member_pk_2
        unique (name)
)
    comment '攻防参与人员';

create index attack_defense_member_id_state_index
    on attack_defense_member (id, state);

create table attack_defense_menu
(
    id     int auto_increment
        primary key,
    path   text        null comment '导航路径',
    auth   varchar(30) null comment '权限等级:admin,umpire，attacker,defender',
    title  varchar(50) null comment '菜单标题',
    parent int         null,
    icon   text        null comment '菜单图标'
);

create table attack_defense_record
(
    id          int auto_increment
        primary key,
    title       varchar(50) null,
    attack_team int         null comment '攻击队伍Id',
    defend_team int         null comment '防御队伍id',
    commit_time datetime    null,
    sid         int         null comment '标靶系统id',
    state       tinyint     null comment '1:未审批,2:未通过,3:已通过',
    summary     text        null comment '攻击总结',
    file        text        null comment '文件地址',
    template    varchar(50) null comment '模板id',
    umpire      int         null comment '裁判id(关联系统用户表)',
    file_name   varchar(50) null
)
    comment '攻防审批记录';

create index attack_defense_record_id_commit_time_title_index
    on attack_defense_record (id, commit_time, title);

create index attack_defense_record_id_state_index
    on attack_defense_record (id, state);

create table attack_defense_systems
(
    id             int auto_increment comment '主键ID'
        primary key,
    name           varchar(50) null comment '系统名称',
    ip             varchar(20) null comment '系统IP地址',
    type           int         null comment '系统类型',
    state          tinyint     null comment '系统状态 (1:活跃, 2:暂停, 3:失效)',
    responsible    int         null comment '负责人ID',
    description    text        null comment '系统描述',
    phone          varchar(50) null comment '负责人电话',
    risk_level     varchar(20) null comment '风险等级',
    security_group varchar(20) null comment '安全组',
    project        varchar(30) null comment '所属项目',
    department     varchar(30) null comment '所属部门',
    tid            int         null comment '发布此靶标的防守队伍'
)
    comment '攻防系统信息表';

create table attack_defense_target_system
(
    id             int auto_increment
        primary key,
    name           varchar(50) null comment '系统名',
    type           varchar(30) null comment '系统类型(Web?App?)',
    port           varchar(10) null comment '端口号',
    status         tinyint     null comment '状态(1:活跃,2:离线,3:未启用)',
    description    text        null comment '描述',
    department     varchar(30) null comment '所属部门',
    ip             varchar(30) null comment '地址',
    contact        int         null comment '系统负责人(关联成员表)',
    access_account text        null comment '访问账号',
    password       text        null,
    tid            int         null comment '是哪个防御队伍发布的靶标'
);

create index attack_defense_target_system_type_port_index
    on attack_defense_target_system (type, port);

create table attack_defense_team
(
    id          int auto_increment
        primary key,
    attack      tinyint(1)    null comment 'true为攻击',
    cn_name     varchar(80)   null,
    en_name     varchar(80)   null comment 'leader',
    leader      int           null comment '队长id',
    member_num  int default 0 not null,
    state       tinyint       null comment '1:活跃，2：暂停,3:在线,4:禁用,5:失效',
    create_time datetime      null
);

create index idx_team_cn_name
    on attack_defense_team (cn_name);

create index idx_team_en_name
    on attack_defense_team (en_name);

create table attack_defense_team_members
(
    mid int not null comment '成员id',
    tid int not null comment '队伍id',
    primary key (tid, mid)
)
    comment '平台用户与队伍关联表';

create table attack_defense_templates
(
    id          int auto_increment
        primary key,
    title       varchar(80) not null comment '标题',
    type        varchar(30) null comment '模板类型',
    create_time datetime    null comment '创建时间',
    update_time datetime    null comment '更新时间',
    use_num     int         null comment '使用次数',
    in_use      tinyint     null comment '使用状态',
    description text        null comment '模板描述',
    content     text        null comment '模板文件附件附件地址',
    attack      tinyint(1)  null comment '是否为攻击模板',
    constraint attack_defense_templates_pk
        unique (title)
);

create index attack_defense_templates_id_title_update_time_index
    on attack_defense_templates (id, title, update_time);

create table attack_defense_target_team
(
    tid int not null comment '队伍id',
    sid int not null comment '靶标系统id',
    primary key (tid, sid)
)
    comment '靶标指定的可攻击队伍表';



# 继承自原先系统的上传记录表
create table uploads
(
    id          int auto_increment
        primary key,
    name        varchar(255) null,
    url         varchar(255) null,
    create_time datetime     null,
    update_time datetime     null
)
    comment '文件url表' row_format = DYNAMIC;


-- 第一个注册的人为admin
DELIMITER //

CREATE TRIGGER set_first_user_as_admin
    BEFORE INSERT ON attack_defense_member
    FOR EACH ROW
BEGIN
    DECLARE member_count INT;

    SELECT COUNT(*) INTO member_count FROM attack_defense_member;

    IF member_count = 0 THEN
        SET NEW.role = 'admin';
    END IF;
END //

DELIMITER ;
