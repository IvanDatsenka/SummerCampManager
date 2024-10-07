
ALTER TABLE Отряд
DROP CONSTRAINT FK_Отряд_Корпус 
GO

ALTER TABLE Комната
DROP CONSTRAINT FK_Комната_Корпус
GO

ALTER TABLE Отряд
DROP CONSTRAINT FK_Отряд_Смена 
GO

ALTER TABLE Сотрудник
DROP CONSTRAINT FK_Сотрудник_Должность
GO

ALTER TABLE Участие
DROP CONSTRAINT FK_Участие_Мероприятие 
GO

ALTER TABLE Участие
DROP CONSTRAINT FK_Участие_Отряд 
GO

ALTER TABLE Ребёнок
DROP CONSTRAINT FK_Ребёнок_Спортивная_секция 
GO

ALTER TABLE Ребёнок
DROP CONSTRAINT FK_Ребёнок_Отряд 
GO

ALTER TABLE Ребёнок
DROP CONSTRAINT FK_Ребёнок_Комната 
GO


ALTER TABLE Отряд
DROP CONSTRAINT FK_Отряд_Сотрудник 
GO


ALTER TABLE Мероприятие
DROP CONSTRAINT FK_Мероприятие_Сотрудник 
GO


DROP TABLE Мероприятие 
GO
DROP TABLE Участие
GO
DROP TABLE Ребёнок
GO
DROP TABLE Сотрудник
GO
DROP TABLE Должность
GO
DROP TABLE Отряд
GO
DROP TABLE Корпус
GO
DROP TABLE Спортивная_секция
GO
DROP TABLE Комната
GO
DROP TABLE Смена
GO


create table Мероприятие(
  код int identity primary key,
  название varchar(50) not null default 'открытие смены',
  дата date not null,
  код_сотрудника int not null 
)
go

create table Отряд(
  код int identity primary key,
  код_сотрудника int not null,
  код_смены int not null,
  название varchar(24) not null,
  код_корпуса int not null 
)
go

create table Ребёнок(
  код int identity primary key,
  имя varchar(24) not null,
  фамилия varchar(24) not null,
  код_отряда int not null,
  код_спотивной_секции int not null,
  код_комнаты int not null
)
go

create table Сотрудник(
  код int identity primary key,
  имя varchar(24) not null,
  фамилия varchar(24) not null,
  код_должности int 
)
go

create table Комната(
  код int identity primary key,
  код_корпуса int not null,
  номер int not null check(номер BETWEEN  1 AND 100)
)
go

create table Должность(
  код int identity primary key,
  название varchar(24) unique not null	
)
go

create table Корпус(
  код int identity primary key,
  название varchar(24) unique not null
)
go

create table Смена(
  код int identity primary key,
  дата date unique not null,
  название varchar(24) not null	
)
go

create table Участие(
  код int identity primary key,
  код_мероприятия int not null,
  код_отряда int not null,
)
go

create table Спортивная_секция(
  код int identity primary key,
  название varchar(24) unique not null
)
go


alter table Комната
add constraint FK_Комната_Корпус foreign key(код_корпуса)
references Корпус(код)
go

alter table Отряд
add constraint FK_Отряд_Корпус foreign key(код_корпуса)
references Корпус(код)
go

alter table Участие
add constraint FK_Участие_Мероприятие foreign key(код_мероприятия)
references Мероприятие(код)
go

alter table Участие
add constraint FK_Участие_Отряд foreign key(код_отряда)
references Отряд(код)
go

alter table Ребёнок
add constraint FK_Ребёнок_Спортивная_секция foreign key(код_спотивной_секции)
references Спортивная_секция(код)
go


alter table Ребёнок
add constraint FK_Ребёнок_Отряд foreign
key(код_отряда)
references Отряд(код)
go

alter table Ребёнок
add constraint FK_Ребёнок_Комната foreign key(код_комнаты)
references Комната(код)
go

alter table Отряд 
add constraint FK_Отряд_Смена foreign key(код_смены)
references Смена(код)
go

alter table Отряд
add constraint FK_Отряд_Сотрудник foreign key(код_сотрудника)
references Сотрудник(код)
go

alter table Мероприятие 
add constraint FK_Мероприятие_Сотрудник foreign key(код_сотрудника)
references Сотрудник(код)
go

alter table Сотрудник
add constraint FK_Сотрудник_Должность foreign key(код_должности)
references Должность(код)
go

ALTER TABLE Отряд
ADD CONSTRAINT UQ_Отряд_название UNIQUE(название)
GO
ALTER TABLE Смена
ADD CONSTRAINT CK_Смена_дата CHECK(дата > '2008-05-31')



insert into Должность(название)
values('Директор'),
('Вожатый'),
('Обслуживающий персонал'),
('Доктор'),
('Организатор')
go


insert into Сотрудник(имя, фамилия, код_должности)
values('Федор', 'Емельяненко', 2),
('Максим', 'Бортновкий', 4),
('Надежда', 'Борисова', 3),
('Михаил', 'Щит', 2),
('Федор', 'Дготь', 5),
('Кирилл', 'Барабан', 1)
go


insert into Смена(дата, название)
values('2012-06-01','См-1'),
('2013-06-01','См-2'),
('2014-06-01','См-3'),
('2012-02-01','См-4'),
('2013-02-01','См-5')
go


insert into Спортивная_секция(название)
values('футбол'),
('баскетбол'),
('бадминтон'),
('теннис'),
('плавание')
go


insert into Корпус(название)
values('К-1'),
('К-2'),
('К-3'),
('К-4'),
('К-5')
go


insert into Комната(код_корпуса, номер)
values(1,1),
(2,1),
(3,4),
(4,3),
(5,2),
(1,7)
go



insert into Мероприятие(название,дата,код_сотрудника)
values('открытие смены','2012-06-01',1),
('закрытие смены','2012-06-23',1),
('танцевальный конкурс','2013-06-12',2),
('маскарад','2014-01-12',2),
('день спорта','2013-06-18',2),
('день здоровья','2012-06-05',3),
('выставка','2012-06-04',1)
go

insert into Отряд(код_сотрудника, код_смены, название, код_корпуса)
values(1,1,'Патимейкеры',1),
(1,2,'Самураи',1),
(1,3,'ОгБудды',1),
(1,4,'Звери',2),
(2,5,'Двери',3),
(2,3,'Линкин парк',4),
(1,4,'Дотеры',5),
(2,1,'Прогеры',3)
go

insert into Участие(код_мероприятия, код_отряда)
values(1,2),
(2,1),
(3,3),
(3,2),
(4,4),
(5,5),
(6,6),
(7,7),
(3,8),
(1,2),
(4,3),
(4,1)
go

insert into Ребёнок(имя,фамилия,код_отряда,код_спотивной_секции,код_комнаты)
values('Кирилл','Хялюк',1,1,1),
('Михаил','Солдат',2,2,1),
('Кристина','Крава',3,3,2),
('Маша','Беспалова',4,4,2),
('Ян','Вечно-пьян',5,5,3),
('Женя','Сипченко',6,5,3),
('Анастасия','Стеклова',7,4,4),
('Елена','Демченко',8,3,4),
('Богдан','Скип',1,2,5),
('Владислав','Глазов',2,3,5),
('Леонит','Сотник',3,1,5),
('Тимур','Удаленный',1,1,1)
go

UPDATE Ребёнок 
  SET имя = 'Леонид', фамилия = 'Сотников' 
  WHERE код = 11	
GO

DELETE FROM Ребёнок WHERE код = 12
go


drop view количество_детей_в_отряде
go

CREATE VIEW количество_детей_в_отряде AS 
  SELECT Отряд.код AS код, Отряд.название, (SELECT COUNT(*) FROM Ребёнок WHERE Ребёнок.код_отряда = Отряд.код) AS количество 
    FROM Отряд
go

select * from количество_детей_в_отряде
go


drop proc Все_отряды_в_смене
go

create proc Все_отряды_в_смене @id INT
as
  select Отряд.название as название_отряда,Сотрудник.имя + ' ' +  Сотрудник.фамилия as Вожатый, Корпус.название as корпус
  from Отряд inner join Корпус on Отряд.код_корпуса=Корпус.код inner join
    Сотрудник on Отряд.код_сотрудника=Сотрудник.код
  where Отряд.код_смены=@id
go


declare @smena_id int = 1;
exec Все_отряды_в_смене @smena_id;


drop proc записанныеВСпортивнуюСекциюВОтряде
go
create proc записанныеВСпортивнуюСекциюВОтряде
  @otryadName varchar(24)='Патимейкеры',
  @sportName varchar(24)='футбол'
as
if @sportName is null
  if @otryadName is null
    select Ребёнок.код, Ребёнок.имя, Ребёнок.фамилия, Отряд.код as код_отряда, Отряд.название, Спортивная_секция.код as код_спорт_секции,
	    Спортивная_секция.название as название_спортивной_секции, Ребёнок.код_комнаты
	from Ребёнок inner join Отряд on Отряд.код=Ребёнок.код_отряда inner join Спортивная_секция
	    on Ребёнок.код_спотивной_секции=Спортивная_секция.код 
  else 
    select Ребёнок.код, Ребёнок.имя, Ребёнок.фамилия, Отряд.код as код_отряда, Отряд.название, Спортивная_секция.код as код_спорт_секции,
	    Спортивная_секция.название as название_спортивной_секции, Ребёнок.код_комнаты
	from Ребёнок inner join Отряд on Отряд.код=Ребёнок.код_отряда inner join Спортивная_секция
	    on Ребёнок.код_спотивной_секции=Спортивная_секция.код 
	where Отряд.название=@otryadName
else 
  if @otryadName is null
    select Ребёнок.код, Ребёнок.имя, Ребёнок.фамилия, Отряд.код as код_отряда, Отряд.название, Спортивная_секция.код as код_спорт_секции,
	    Спортивная_секция.название as название_спортивной_секции, Ребёнок.код_комнаты
	from Ребёнок inner join Отряд on Отряд.код=Ребёнок.код_отряда inner join Спортивная_секция
	    on Ребёнок.код_спотивной_секции=Спортивная_секция.код 
	where Спортивная_секция.название=@sportName
  else 
    select Ребёнок.код, Ребёнок.имя, Ребёнок.фамилия, Отряд.код as код_отряда, Отряд.название, Спортивная_секция.код as код_спорт_секции,
	    Спортивная_секция.название as название_спортивной_секции, Ребёнок.код_комнаты
	from Ребёнок inner join Отряд on Отряд.код=Ребёнок.код_отряда inner join Спортивная_секция
	    on Ребёнок.код_спотивной_секции=Спортивная_секция.код 
	where Спортивная_секция.название=@sportName and Отряд.название=@otryadName 
go

drop proc количество_мин_среди_первых_P 
go

create proc количество_мин_среди_первых_P @P INT
AS
DECLARE @количество INT = 0
DECLARE @min INT, @код INT, @код_отряда INT
DECLARE @текущая_строка INT = 1
SELECT @код = MIN(код) FROM Участие 
SET @min = (SELECT код_отряда FROM Участие WHERE код=@код)
WHILE @код is not null
BEGIN 
  SET @код_отряда = (SELECT код_отряда FROM Участие WHERE код=@код)
  IF @код_отряда < @min
  BEGIN
    IF @текущая_строка>@P
	BEGIN
	  RETURN 0
	END
    SET @min = @код_отряда
	SET @количество = 0
  END
  IF @код_отряда = @min and @текущая_строка<=@P
  BEGIN
    SET @количество = @количество + 1
  END 
  SELECT @код = MIN(код) FROM Участие WHERE код>@код
  SET @текущая_строка = @текущая_строка + 1
END 
RETURN @количество
GO

SELECT код, код_отряда FROM Участие ORDER BY код
GO

DECLARE @res INT
EXEC @res = количество_мин_среди_первых_P 5
SELECT @res AS количесвто_элементов_равных_минимальному_в_первых_P_элементах_последовательности





