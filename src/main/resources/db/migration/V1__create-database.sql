create table transfer(
	id char(32) primary key,
    sender char(32) not null,
    receiver char(32) not null,
    amount int unsigned not null,
    sender_currency char(3) not null,
    receiver_currency char(3) not null,
    retriesLeft int not null);

create table transfer_attempt (
	id char(32) primary key,
    date_of_transfer datetime not null,
    result int unsigned not null,
    foreign key (id) references transfer(id));

create table pending_withdraw (
    id char(32) primary key,
    foreign key (id) references transfer(id));

create table pending_deposit (
    id char(32) primary key,
    foreign key (id) references transfer(id));