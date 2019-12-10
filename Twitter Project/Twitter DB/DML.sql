use twitter_db;
show tables;

insert into user (user_handle, password, first_name, last_name, email_address, profile_pic, cover_pic, created_date)
    values ('preetom_93', 'password_preetom', 'Preetom', 'Bhowmik', 'bits3dec@gmail.com', 'profile pic', 'cover pic', '2019-07-21'),
    ('rishabh_93', 'password_rishabh', 'Rishabh', 'Pant', 'rishabh@gmail.com', 'profile pic', 'cover pic', '2019-07-22'),
	('virat_93', 'password_virat', 'Virat', 'Kohli', 'virat@gmail.com', 'profile pic', 'cover pic', '2019-07-12'),
    ('rohit_93', 'password_rohit', 'Rohit', 'Sharma', 'rohit@gmail.com', 'profile pic', 'cover pic', '2019-07-16'),
    ('dhoni_93', 'password_dhoni', 'MS', 'Dhoni', 'dhonic@gmail.com', 'profile pic', 'cover pic', '2019-04-11');
        
insert into follower_following (follower_id, following_id)
	values (1, 2),
    (1, 3),
	(1, 4),
	(1, 5),
	(2, 3),
	(2, 4),
	(2, 5),
	(3, 4),
	(3, 5),
	(4, 2),
	(4, 3),
	(5, 3),
	(5, 4);
        
insert into tweet (user_id, tweet_message, created_date, likes)
	values(1, 'Excited for WC-2019 !!!', '2019-05-21', 10),
    (2, 'My WC debut in england 2019. Thank you for your wishes !!!', '2019-05-21', 100),
    (3, 'Dhoni is my idol !!!', '2018-05-10', 230),
    (3, 'We will try our best to win the WC-2019', '2019-05-21', 500),
    (4, 'I hope to get Man of The Tournament !!!', '2019-05-25', 450),
    (5, 'This is a very competetive world cup', '2019-05-12', 600),
    (1, 'NZ vs Eng was full on drama !!!', '2019-07-10', 15),
    (3, 'NZ outplayed us in semi-final', '2019-07-05', 500);
    
insert into comment (tweet_id, user_id, text, created_date)
	values(2, 1, 'All the best Pant for your debut', '2019-05-21'),
    (4, 1, 'Yes Kohli !!! India will win the world cup :) ', '2019-05-22'),
    (5, 1, 'Sure Rohit', '2019-05-21'),
    (6, 3, 'Our team is the best', '2019-05-12'),
    (8, 5, 'Winning and loosing are part of the game', '2019-07-05');
    
select * from user;
select * from follower_following;
select * from tweet;
select * from comment;