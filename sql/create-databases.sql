drop database if exists `Acme-ANS-D02`;
create database `Acme-ANS-D02` 
	character set = 'utf8mb4' 
  	collate = 'utf8mb4_unicode_ci';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-ANS-D02`.* to 'acme-user'@'%';

drop database if exists `Acme-ANS-D02-Test`;
create database `Acme-ANS-D02-Test` 
	character set = 'utf8mb4'
  	collate = 'utf8mb4_unicode_ci';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-ANS-D02-Test`.* to 'acme-user'@'%';
