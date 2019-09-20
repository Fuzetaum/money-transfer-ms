create table transfer(
	id char(36) primary key,
    sender char(36) not null,
    receiver char(36) not null,
    amount int unsigned not null,
    sender_currency char(3) not null,
    receiver_currency char(3) not null,
    retries_left int not null);

create table transfer_attempt (
    id int primary key auto_increment,
	transfer_id char(36),
    date_of_transfer datetime not null,
    result int unsigned not null,
    foreign key (transfer_id) references transfer(id));

create table pending_withdraw (
    id char(36) primary key,
    foreign key (id) references transfer(id));

create table pending_deposit (
    id char(36) primary key,
    foreign key (id) references transfer(id));