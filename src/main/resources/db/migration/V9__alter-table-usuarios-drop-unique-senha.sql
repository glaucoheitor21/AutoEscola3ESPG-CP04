-- A coluna "senha" havia sido criada com constraint UNIQUE, o que impede que dois
-- usuarios tenham a mesma senha. Com o hash BCrypt isso deixa de fazer sentido
-- (cada hash ja e unico por causa do salt) e passa a ser apenas um efeito colateral
-- indesejado, entao a constraint e removida.
alter table usuarios drop index senha;
