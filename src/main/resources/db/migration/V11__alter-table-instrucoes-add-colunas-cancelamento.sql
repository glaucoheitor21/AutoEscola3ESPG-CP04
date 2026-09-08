-- O cancelamento nao apaga a instrucao: ela permanece na base registrando o motivo
-- e a data em que foi cancelada.
alter table instrucoes add column motivo_cancelamento varchar(30);
alter table instrucoes add column data_cancelamento datetime;
