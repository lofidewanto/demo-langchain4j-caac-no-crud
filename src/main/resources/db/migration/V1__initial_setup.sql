create table person (
    id bigint generated by default as identity,
    name varchar(255) not null,
    age integer not null,
    email varchar(255) not null unique,
    primary key (id)
);

create table address (
    id bigint generated by default as identity,
    person_id bigint not null,
    city varchar(255) not null,
    state varchar(255) not null,
    street varchar(255) not null,
    zip_code varchar(255) not null,
    primary key (id),
    foreign key (person_id) references person(id)
);
