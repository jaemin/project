select *from MEMBER;
select *from game
select *from wordlist;
select *from filtering;

select * from game
where id = 'bb' and game_seq=89

--------------------------------------------------------
create table Member
(
Member_seq number(10)
,ID varchar2(100) primary key
,password varchar2(1000)
,playnum number(10) default 0
,correctnum number(10) default 0
,winningRate number(5,2) default 0
,totalplaytime number(10) default 0
,indate date 
,memo varchar2(3000)
);

create sequence Memberseq;

create table game
(
game_seq number(10)
,id varchar2(100)
,usertype varchar2(100) default 'default'
,word varchar2(100)
,starttime date
,endtime date
,playtime number(10) default 0
);

create table wordlist(
word_seq number(10)
,word varchar2(100)
);

create sequence wordseq;

create table filtering
(
filter_seq number(10)
,filterword varchar2(100)
);

create sequence filterseq;


insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'Sangsu'
,'NaturalSangsuE'
,sysdate
);


insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'Jungsu'
,'0dojungsu'
,sysdate
);
insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'aa'
,'a'
,sysdate
);
insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'bb'
,'b'
,sysdate
);
insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'cc'
,'c'
,sysdate
);
insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'ee'
,'e'
,sysdate
);
insert into member
(
member_seq 
,ID
,password
,indate
)
values
(
memberseq.nextval
,'dd'
,'d'
,sysdate
);
insert into Game --start
(
game_seq
,id 
,word 
,starttime
)
values
(
0
,'Sangsu'
,'고양이'
,sysdate
);

insert into Game --start
(
game_seq
,id 
,word 
,starttime
)
values
(
0
,'Jungsu'
,'고양이'
,sysdate
);


insert into wordList
(
word_seq 
,word 
)
values
(
wordseq.nextval
,'강아지'
);
insert into wordList
(
word_seq 
,word 
)
values
(
wordseq.nextval
,'고양이'
);
insert into wordList
(
word_seq 
,word 
)
values
(
wordseq.nextval
,'새'
);
insert into wordList
(
word_seq 
,word 
)
values
(
wordseq.nextval
,'노트북'
);
insert into wordList
(
word_seq 
,word 
)
values
(
wordseq.nextval
,'모자'
);
insert into wordList
(
word_seq 
,word 
)
values
(
wordseq.nextval
,'색연필'
);

insert into filtering
(
filter_seq 
,filterword 
)
values
(
filterseq.nextval
,'씨발'
);

insert into filtering
(
filter_seq 
,filterword 
)
values
(
filterseq.nextval
,'개새끼'
);

insert into filtering
(
filter_seq 
,filterword 
)
values
(
filterseq.nextval
,'씹창'
);

--------------------------------------------------------------------

drop table member;
drop table game;
drop table wordlist;
drop table filtering;
drop sequence memberseq;
drop sequence wordseq;
drop sequence filterseq;

-----------------------------------------------------------------------

update member
set password = #{password}
where id = #{id}

update game
set 
usertype = 'correct'
,endtime=sysdate
,playtime= 3
where
game_seq = 1 and
id = 'Sangsu'

update member
set 
playnum=1
,correctnum=1
,winningrate=
,totalplaytime=3
where
id = 'Sangsu'

select*
from member

select to_char(sysdate,'MI')
from dual

select sysdate from dual

