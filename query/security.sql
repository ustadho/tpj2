drop TABLE  c_security_user;
drop table c_security_role_menu;
drop table c_security_role_permission;  
drop table c_security_role ;
drop table c_security_permission  ; 

insert into c_security_role (id, description, name) VALUES
('ADMIN', 'Administrator', 'ADMIN')  ;

insert into c_security_permission(id, permission_label, permission_value) VALUES
('ADMIN', 'Administrator', 'ADMIN');

insert into c_security_user(id, active, username, id_role) VALUES
('admin', true, 'admin', 'ADMIN');

insert into c_security_role_permission(id_role, id_permission) VALUES
('ADMIN', 'ADMIN');

delete from c_security_user_password;
insert into c_security_user_password(id_user, user_password) VALUES
--('admin', '123');  
('admin', '$2a$10$NVAwxgG7.PB8LeLdcl0lX.QTDPHxBXgeDRCr4D94ZFz4PD.78hUDC');  
$2a$10$NVAwxgG7.PB8LeLdcl0lX.QTDPHxBXgeDRCr4D94ZFz4PD.78hUDC
select u.username as username,p.user_password as password, active 
from c_security_user u inner join c_security_user_password p on p.id_user = u.id where username = 'admin'\

select u.username, p.permission_value as authority 
        from c_security_user u 
        inner join c_security_role r on u.id_role = r.id 
        inner join c_security_role_permission rp on rp.id_role = r.id 
        inner join c_security_permission p on rp.id_permission = p.id 
        where u.username = 'admin'
