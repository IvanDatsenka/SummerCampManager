
ALTER TABLE �����
DROP CONSTRAINT FK_�����_������ 
GO

ALTER TABLE �������
DROP CONSTRAINT FK_�������_������
GO

ALTER TABLE �����
DROP CONSTRAINT FK_�����_����� 
GO

ALTER TABLE ���������
DROP CONSTRAINT FK_���������_���������
GO

ALTER TABLE �������
DROP CONSTRAINT FK_�������_����������� 
GO

ALTER TABLE �������
DROP CONSTRAINT FK_�������_����� 
GO

ALTER TABLE ������
DROP CONSTRAINT FK_������_����������_������ 
GO

ALTER TABLE ������
DROP CONSTRAINT FK_������_����� 
GO

ALTER TABLE ������
DROP CONSTRAINT FK_������_������� 
GO


ALTER TABLE �����
DROP CONSTRAINT FK_�����_��������� 
GO


ALTER TABLE �����������
DROP CONSTRAINT FK_�����������_��������� 
GO


DROP TABLE ����������� 
GO
DROP TABLE �������
GO
DROP TABLE ������
GO
DROP TABLE ���������
GO
DROP TABLE ���������
GO
DROP TABLE �����
GO
DROP TABLE ������
GO
DROP TABLE ����������_������
GO
DROP TABLE �������
GO
DROP TABLE �����
GO


create table �����������(
  ��� int identity primary key,
  �������� varchar(50) not null default '�������� �����',
  ���� date not null,
  ���_���������� int not null 
)
go

create table �����(
  ��� int identity primary key,
  ���_���������� int not null,
  ���_����� int not null,
  �������� varchar(24) not null,
  ���_������� int not null 
)
go

create table ������(
  ��� int identity primary key,
  ��� varchar(24) not null,
  ������� varchar(24) not null,
  ���_������ int not null,
  ���_���������_������ int not null,
  ���_������� int not null
)
go

create table ���������(
  ��� int identity primary key,
  ��� varchar(24) not null,
  ������� varchar(24) not null,
  ���_��������� int 
)
go

create table �������(
  ��� int identity primary key,
  ���_������� int not null,
  ����� int not null check(����� BETWEEN  1 AND 100)
)
go

create table ���������(
  ��� int identity primary key,
  �������� varchar(24) unique not null	
)
go

create table ������(
  ��� int identity primary key,
  �������� varchar(24) unique not null
)
go

create table �����(
  ��� int identity primary key,
  ���� date unique not null,
  �������� varchar(24) not null	
)
go

create table �������(
  ��� int identity primary key,
  ���_����������� int not null,
  ���_������ int not null,
)
go

create table ����������_������(
  ��� int identity primary key,
  �������� varchar(24) unique not null
)
go


alter table �������
add constraint FK_�������_������ foreign key(���_�������)
references ������(���)
go

alter table �����
add constraint FK_�����_������ foreign key(���_�������)
references ������(���)
go

alter table �������
add constraint FK_�������_����������� foreign key(���_�����������)
references �����������(���)
go

alter table �������
add constraint FK_�������_����� foreign key(���_������)
references �����(���)
go

alter table ������
add constraint FK_������_����������_������ foreign key(���_���������_������)
references ����������_������(���)
go


alter table ������
add constraint FK_������_����� foreign
key(���_������)
references �����(���)
go

alter table ������
add constraint FK_������_������� foreign key(���_�������)
references �������(���)
go

alter table ����� 
add constraint FK_�����_����� foreign key(���_�����)
references �����(���)
go

alter table �����
add constraint FK_�����_��������� foreign key(���_����������)
references ���������(���)
go

alter table ����������� 
add constraint FK_�����������_��������� foreign key(���_����������)
references ���������(���)
go

alter table ���������
add constraint FK_���������_��������� foreign key(���_���������)
references ���������(���)
go

ALTER TABLE �����
ADD CONSTRAINT UQ_�����_�������� UNIQUE(��������)
GO
ALTER TABLE �����
ADD CONSTRAINT CK_�����_���� CHECK(���� > '2008-05-31')



insert into ���������(��������)
values('��������'),
('�������'),
('������������� ��������'),
('������'),
('�����������')
go


insert into ���������(���, �������, ���_���������)
values('�����', '�����������', 2),
('������', '����������', 4),
('�������', '��������', 3),
('������', '���', 2),
('�����', '�����', 5),
('������', '�������', 1)
go


insert into �����(����, ��������)
values('2012-06-01','��-1'),
('2013-06-01','��-2'),
('2014-06-01','��-3'),
('2012-02-01','��-4'),
('2013-02-01','��-5')
go


insert into ����������_������(��������)
values('������'),
('���������'),
('���������'),
('������'),
('��������')
go


insert into ������(��������)
values('�-1'),
('�-2'),
('�-3'),
('�-4'),
('�-5')
go


insert into �������(���_�������, �����)
values(1,1),
(2,1),
(3,4),
(4,3),
(5,2),
(1,7)
go



insert into �����������(��������,����,���_����������)
values('�������� �����','2012-06-01',1),
('�������� �����','2012-06-23',1),
('������������ �������','2013-06-12',2),
('��������','2014-01-12',2),
('���� ������','2013-06-18',2),
('���� ��������','2012-06-05',3),
('��������','2012-06-04',1)
go

insert into �����(���_����������, ���_�����, ��������, ���_�������)
values(1,1,'�����������',1),
(1,2,'�������',1),
(1,3,'�������',1),
(1,4,'�����',2),
(2,5,'�����',3),
(2,3,'������ ����',4),
(1,4,'������',5),
(2,1,'�������',3)
go

insert into �������(���_�����������, ���_������)
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

insert into ������(���,�������,���_������,���_���������_������,���_�������)
values('������','�����',1,1,1),
('������','������',2,2,1),
('��������','�����',3,3,2),
('����','���������',4,4,2),
('��','�����-����',5,5,3),
('����','��������',6,5,3),
('���������','��������',7,4,4),
('�����','��������',8,3,4),
('������','����',1,2,5),
('���������','������',2,3,5),
('������','������',3,1,5),
('�����','���������',1,1,1)
go

UPDATE ������ 
  SET ��� = '������', ������� = '��������' 
  WHERE ��� = 11	
GO

DELETE FROM ������ WHERE ��� = 12
go


drop view ����������_�����_�_������
go

CREATE VIEW ����������_�����_�_������ AS 
  SELECT �����.��� AS ���, �����.��������, (SELECT COUNT(*) FROM ������ WHERE ������.���_������ = �����.���) AS ���������� 
    FROM �����
go

select * from ����������_�����_�_������
go


drop proc ���_������_�_�����
go

create proc ���_������_�_����� @id INT
as
  select �����.�������� as ��������_������,���������.��� + ' ' +  ���������.������� as �������, ������.�������� as ������
  from ����� inner join ������ on �����.���_�������=������.��� inner join
    ��������� on �����.���_����������=���������.���
  where �����.���_�����=@id
go


declare @smena_id int = 1;
exec ���_������_�_����� @smena_id;


drop proc ����������������������������������
go
create proc ����������������������������������
  @otryadName varchar(24)='�����������',
  @sportName varchar(24)='������'
as
if @sportName is null
  if @otryadName is null
    select ������.���, ������.���, ������.�������, �����.��� as ���_������, �����.��������, ����������_������.��� as ���_�����_������,
	    ����������_������.�������� as ��������_����������_������, ������.���_�������
	from ������ inner join ����� on �����.���=������.���_������ inner join ����������_������
	    on ������.���_���������_������=����������_������.��� 
  else 
    select ������.���, ������.���, ������.�������, �����.��� as ���_������, �����.��������, ����������_������.��� as ���_�����_������,
	    ����������_������.�������� as ��������_����������_������, ������.���_�������
	from ������ inner join ����� on �����.���=������.���_������ inner join ����������_������
	    on ������.���_���������_������=����������_������.��� 
	where �����.��������=@otryadName
else 
  if @otryadName is null
    select ������.���, ������.���, ������.�������, �����.��� as ���_������, �����.��������, ����������_������.��� as ���_�����_������,
	    ����������_������.�������� as ��������_����������_������, ������.���_�������
	from ������ inner join ����� on �����.���=������.���_������ inner join ����������_������
	    on ������.���_���������_������=����������_������.��� 
	where ����������_������.��������=@sportName
  else 
    select ������.���, ������.���, ������.�������, �����.��� as ���_������, �����.��������, ����������_������.��� as ���_�����_������,
	    ����������_������.�������� as ��������_����������_������, ������.���_�������
	from ������ inner join ����� on �����.���=������.���_������ inner join ����������_������
	    on ������.���_���������_������=����������_������.��� 
	where ����������_������.��������=@sportName and �����.��������=@otryadName 
go

drop proc ����������_���_�����_������_P 
go

create proc ����������_���_�����_������_P @P INT
AS
DECLARE @���������� INT = 0
DECLARE @min INT, @��� INT, @���_������ INT
DECLARE @�������_������ INT = 1
SELECT @��� = MIN(���) FROM ������� 
SET @min = (SELECT ���_������ FROM ������� WHERE ���=@���)
WHILE @��� is not null
BEGIN 
  SET @���_������ = (SELECT ���_������ FROM ������� WHERE ���=@���)
  IF @���_������ < @min
  BEGIN
    IF @�������_������>@P
	BEGIN
	  RETURN 0
	END
    SET @min = @���_������
	SET @���������� = 0
  END
  IF @���_������ = @min and @�������_������<=@P
  BEGIN
    SET @���������� = @���������� + 1
  END 
  SELECT @��� = MIN(���) FROM ������� WHERE ���>@���
  SET @�������_������ = @�������_������ + 1
END 
RETURN @����������
GO

SELECT ���, ���_������ FROM ������� ORDER BY ���
GO

DECLARE @res INT
EXEC @res = ����������_���_�����_������_P 5
SELECT @res AS ����������_���������_������_������������_�_������_P_���������_������������������





