create database if not exists twitter_db;
use twitter_db;

create table if not exists user (
	id int unsigned primary key auto_increment,
    user_handle varchar(25) unique not null,
    password varchar(25) not null,
    first_name varchar(25) not null,
    last_name varchar(25),
    email_address varchar(50) unique not null,
    profile_pic varchar(50),
    cover_pic varchar(50),
    created_date datetime default now()
);

create table if not exists tweet (
	id int unsigned primary key auto_increment,
    user_id int unsigned not null,
    tweet_message varchar(100) not null,
    created_date date,
    likes int unsigned default 0,
    
    constraint foreign key(user_id) references user(id)
);

create table if not exists comment (
	id int unsigned primary key auto_increment,
    tweet_id int unsigned,
    user_id int unsigned,
    text varchar(100) not null,
    created_date date,
    
    constraint foreign key(tweet_id) references tweet(id),
    constraint foreign key(user_id) references user(id)
);

create table if not exists follower_following (
	follower_id int unsigned,
    following_id int unsigned,
    
    constraint foreign key(follower_id) references user(id),
    constraint foreign key(following_id) references user(id),
    constraint primary key(follower_id, following_id)
);

drop table user;
drop table tweet;
drop table comment;
drop table follower_following;

drop database twitter_db;
