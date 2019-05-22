
-- 根目录
INSERT INTO `yomic-drive`.file
(id, create_by, created_date, updated_by, updated_date, content_type, is_dir, limit_size, limit_suffix, modify_by, modify_date, name, share, `size`, status, upload_by, upload_date, uuid, parent_id)
VALUES(NULL, 'system', now(), 'system', now(), NULL, 1, NULL, NULL, 'system', now(), 'Home', 1, NULL, 1, 'system', now(), NULL, NULL);

-- 根部门
INSERT INTO `yomic-drive`.dept
(id, create_by, created_date, updated_by, updated_date, name, parent_id)
VALUES(NULL, 'system', now(), 'system', now(), '总部门', NULL);


-- 初始化用户
INSERT INTO `yomic-drive`.`user`
(id, create_by, created_date, updated_by, updated_date, cname, ip, password, status, username, dept_id)
VALUES(NULL, 'system', now(), 'system', now(), '普通用户', NULL, '{bcrypt}$2a$10$tGyTXIojAe/kj3p8kE4fbe4D9GR3o03NWBgs5Jn5JgGYD9JYzQFti', 1, 'user', 1);

INSERT INTO `yomic-drive`.`user`
(id, create_by, created_date, updated_by, updated_date, cname, ip, password, status, username, dept_id)
VALUES(NULL, 'system', now(), 'system', now(), '超级管理员', NULL, '{bcrypt}$2a$10$zk7NjB3fsHUJCmyr/821Deg1agLZ8viaprZmIp3dKgntCh/yZnuce', 1, 'root', 1);


-- 初始化角色
INSERT INTO `yomic-drive`.`role`
(id, create_by, created_date, updated_by, updated_date, role_code, role_name)
VALUES(NULL, 'init', now(), 'init', now(), 'SUPER', '超级管理员');

INSERT INTO `yomic-drive`.`role`
(id, create_by, created_date, updated_by, updated_date, role_code, role_name)
VALUES(NULL, 'init', now(), 'init', now(), 'USER', '用户');


-- 初始化用户角色
INSERT INTO `yomic-drive`.user_role
(username, role_code)
VALUES('root', 'SUPER');

INSERT INTO `yomic-drive`.user_role
(username, role_code)
VALUES('user', 'USER');
