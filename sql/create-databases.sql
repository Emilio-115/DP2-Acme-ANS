drop database if exists `Acme-ANS-D01`;
create database `Acme-ANS-D01` 
	character set = 'utf8mb4'
  	collate = 'utf8mb4_unicode_ci';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-ANS-D01`.* to 'acme-user'@'%';

drop database if exists `Acme-ANS-D01-Test`;
create database `Acme-ANS-D01-Test` 
	character set = 'utf8mb4'
  	collate = 'utf8mb4_unicode_ci';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-ANS-D01-Test`.* to 'acme-user'@'%';
