-- Usuario administrador inicial, necessario para permitir o cadastro dos demais
-- usuarios (o endpoint POST /usuarios e restrito ao perfil ADMIN).
-- Login: admin / Senha: admin  (hash BCrypt)
insert into usuarios (login, senha, perfil)
select 'admin', '$2a$10$S6VZZpa7x8iO61nd0ovIvOLR7eDZyh9CRWjybMS9Yo0.ZM/bTh9w.', 'ADMIN'
from dual
where not exists (select 1 from usuarios u where u.login = 'admin');
