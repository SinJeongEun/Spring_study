-- call next value for hibernate_sequence;
insert into my_User (`id`,`name`,`email`,`create_at`,`update_at`) values(1,'martin','martin@naver.com',now(),now());

-- call next value for hibernate_sequence;
insert into my_User (`id`,`name`,`email`,`create_at`,`update_at`)  values(2,'dennis','dennis@naver.com',now(),now());

-- call next value for hibernate_sequence;
insert into my_User (`id`,`name`,`email`,`create_at`,`update_at`)  values(3,'sophia','sophia@naver.com',now(),now());

-- call next value for hibernate_sequence;
insert into my_User (`id`,`name`,`email`,`create_at`,`update_at`)  values(4,'james','james@naver.com',now(),now());

-- call next value for hibernate_sequence;
insert into my_User (`id`,`name`,`email`,`create_at`,`update_at`)  values(5,'martin','lisa@nate.com',now(),now());

-- 현재는 우선 1 2 3 4 id를 지정에줬지만 ,후에 우리가 save()를 하게 되면 seequence에서 id갑ㄱ을 가져오는데, 중복 방지를 뉘해 애한테 알려준다??

insert into publisher(`id`,`name`) values (1,'lisa');

insert into book(`id`,`name`,`publisher_id`, `deleted`, `create_at`,`update_at`, status) values (2,'jpa2', 1,false,now(),now(), 100);
insert into book(`id`,`name`,`publisher_id`, `deleted`, `create_at`,`update_at`, status) values (3,'jpq3', 1,false ,now(),now(), 200);
insert into book(`id`,`name`,`publisher_id`, `deleted`, `create_at`,`update_at`, status) values (1,'JPA1', 1,false,now(),now(), 100);

insert into review(`id`, `title`, `content`, `score`, `user_id`, `book_id`) values (1, "책 1", "너무 좋았어요", 5.0, 1, 1);
insert into review(`id`, `title`, `content`, `score`, `user_id`, `book_id`) values (2, "책 2", "별로였어요", 2.0, 2, 2);

insert into comment(`id`, `comment`, `review_id`) values (1, "댓글1", 1);
insert into comment(`id`, `comment`, `review_id`) values (2, "댓글2", 1);
insert into comment(`id`, `comment`, `review_id`) values (3, "댓글3", 2);

