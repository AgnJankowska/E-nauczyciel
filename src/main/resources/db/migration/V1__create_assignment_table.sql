CREATE table subjects(
id          int primary key auto_increment,
description varchar(100) not null
);


CREATE table sections(
id          int primary key auto_increment,
description varchar(100) not null,
subject_id   int not null,
foreign key (subject_id) references subjects (id)
);


CREATE table assignments (
id          int primary key auto_increment,
question    varchar(500) not null,
answerA     varchar(100) not null,
answerB     varchar(100) not null,
answerC     varchar(100) not null,
answerD     varchar(100) not null,
correct_answer  varchar(100) not null,
path_to_image varchar(500) DEFAULT NULL,

section_id   int not null,
foreign key (section_id) references sections (id)
);