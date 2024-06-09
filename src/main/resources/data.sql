alter table categories
    alter column description type varchar(2550) using description::varchar(2550);

alter table producers
    alter column description type varchar(2550) using description::varchar(2550);

alter table products
    alter column description type varchar(2550) using description::varchar(2550);


insert into categories(id, name, description) values
                                     (gen_random_uuid(), 'Automaty oddechowe', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Płetwy', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Suche Skafandry', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Mokre Skafandry', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Maski', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Komputery nurkowe', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Butle', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Akcesoria', 'This is a short description of a category. Little text to show the spot');

insert into producers(id, name) values
                                    (gen_random_uuid(), 'APEKS'),
                                    (gen_random_uuid(), 'SCUBAPRO'),
                                    (gen_random_uuid(), 'TUSA'),
                                    (gen_random_uuid(), 'DIVESYSTEM'),
                                    (gen_random_uuid(), 'HOLLIS'),
                                    (gen_random_uuid(), 'AQUALUNG'),
                                    (gen_random_uuid(), 'X-DEEP'),
                                    (gen_random_uuid(), 'POSEIDON'),
                                    (gen_random_uuid(), 'DIVE SYSTEM'),
                                    (gen_random_uuid(), 'MARES'),
                                    (gen_random_uuid(), 'OCEANIC'),
                                    (gen_random_uuid(), 'SCUBATECH'),
                                    (gen_random_uuid(), 'SEAC'),
                                    (gen_random_uuid(), 'TECHNISUB'),
                                    (gen_random_uuid(), 'TECLINE'),
                                    (gen_random_uuid(), 'ZEAGLE'),
                                    (gen_random_uuid(), 'AMMONITE SYSTEM'),
                                    (gen_random_uuid(), 'AQUA ZONE'),
                                    (gen_random_uuid(), 'BARE'),
                                    (gen_random_uuid(), 'FOURTH ELEMENT'),
                                    (gen_random_uuid(), 'GRALMARINE'),
                                    (gen_random_uuid(), 'HALCYON'),
                                    (gen_random_uuid(), 'KWARK'),
                                    (gen_random_uuid(), 'NO GRAVITY'),
                                    (gen_random_uuid(), 'SANTI');

insert into products(id, name, price, category_id, producer_id) values
                                   (gen_random_uuid(), 'pletwy nr 1', 100.00,(select id from categories where name='Płetwy'), (select id from producers where name='TECLINE')),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Płetwy'), (select id from producers where name='SEAC')),
                                   (gen_random_uuid(), 'pletwy nr 3', 200.00, (select id from categories where name='Płetwy'), (select id from producers where name='TUSA')),
                                   (gen_random_uuid(), 'automat nr 1', 2400.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='APEKS')),
                                   (gen_random_uuid(), 'automat nr 2', 2600.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='TECHNISUB')),
                                   (gen_random_uuid(), 'automat nr 3', 4000.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='APEKS')),
                                   (gen_random_uuid(), 'skafander nr 1', 15000.00, (select id from categories where name='Suche Skafandry'), (select id from producers where name='SANTI')),
                                   (gen_random_uuid(), 'skafander nr 2', 10000.00, (select id from categories where name='Suche Skafandry'), (select id from producers where name='AQUA ZONE')),
                                   (gen_random_uuid(), 'skafander nr 3', 9000.00, (select id from categories where name='Mokre Skafandry'), (select id from producers where name='BARE')),
                                   (gen_random_uuid(), 'maska nr 1', 300.00, (select id from categories where name='Maski'), (select id from producers where name='SEAC')),
                                   (gen_random_uuid(), 'maska nr 2', 200.00, (select id from categories where name='Maski'), (select id from producers where name='HOLLIS'));