alter table categories
    alter column description type varchar(2550) using description::varchar(2550);

alter table producers
    alter column description type varchar(2550) using description::varchar(2550);

alter table products
    alter column description type varchar(2550) using description::varchar(2550);


insert into categories(id, name, description) values
                                     (gen_random_uuid(), 'Automaty oddechowe', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Fins', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Dry suits', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Mokre Skafandry', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Masks', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Komputery nurkowe', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Butle', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Akcesoria', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Portki', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Kąpielówki', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Okularki', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Lekarstwa', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Ksiązki nurkowe', 'This is a short description of a category. Little text to show the spot'),
                                     (gen_random_uuid(), 'Noże nurkowe', 'This is a short description of a category. Little text to show the spot');

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
                                    (gen_random_uuid(), 'GUI'),
                                    (gen_random_uuid(), 'OMS'),
                                    (gen_random_uuid(), 'NO GRAVITY'),
                                    (gen_random_uuid(), 'SANTI');

insert into products(id, name, price, category_id, producer_id, created_date, discount_percentage) values
                                   (gen_random_uuid(), 'pletwy nr 1', 100.00,(select id from categories where name='Fins'), (select id from producers where name='TECLINE'), now(), 10.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'), '2020-01-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-02-20 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-03-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-04-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-05-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-06-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-07-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-08-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-09-29 10:00:00', 5.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-10-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-11-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2024-12-29 10:00:00', 90.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2020-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2021-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2023-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 3', 200.00, (select id from categories where name='Fins'), (select id from producers where name='TUSA'),'2024-09-21 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 1', 100.00,(select id from categories where name='Fins'), (select id from producers where name='TECLINE'),'2014-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 2', 150.00, (select id from categories where name='Fins'), (select id from producers where name='SEAC'),'2015-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 3', 200.00, (select id from categories where name='Fins'), (select id from producers where name='TUSA'),'2016-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 3', 200.00, (select id from categories where name='Fins'), (select id from producers where name='TUSA'),'2017-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 3', 200.00, (select id from categories where name='Fins'), (select id from producers where name='TUSA'),'2018-09-29 10:00:00', 0.00),
                                   (gen_random_uuid(), 'pletwy nr 3', 200.00, (select id from categories where name='Fins'), (select id from producers where name='TUSA'),'2019-09-29 10:00:00', 10.00),
                                   (gen_random_uuid(), 'automat nr 1', 199.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='APEKS'),'2020-01-09 10:00:00', 0.00),
                                   (gen_random_uuid(), 'automat nr 2', 200.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='TECHNISUB'),'2023-03-11 10:00:00', 0.00),
                                   (gen_random_uuid(), 'automat nr 3', 201.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='APEKS'),'2021-02-03 10:00:00', 0.00),
                                   (gen_random_uuid(), 'automat nr 4', 1.00, (select id from categories where name='Automaty oddechowe'), (select id from producers where name='GUI'),'2020-02-01 10:00:00', 0.00),
                                   (gen_random_uuid(), 'skafander nr 1', 15000.00, (select id from categories where name='Dry suits'), (select id from producers where name='SANTI'),'2024-02-14 10:00:00', 0.00),
                                   (gen_random_uuid(), 'skafander nr 2', 10000.00, (select id from categories where name='Dry suits'), (select id from producers where name='AQUA ZONE'),'2022-08-04 10:00:00', 27.00),
                                   (gen_random_uuid(), 'skafander nr 3', 9000.00, (select id from categories where name='Mokre Skafandry'), (select id from producers where name='BARE'),'2024-07-04 10:00:00', 0.00),
                                   (gen_random_uuid(), 'maska nr 1', 300.00, (select id from categories where name='Masks'), (select id from producers where name='SEAC'),'2024-05-10 10:00:00', 0.00),
                                   (gen_random_uuid(), 'maska nr 2', 200.00, (select id from categories where name='Masks'), (select id from producers where name='HOLLIS'),'2024-06-23 10:00:00', 0.00),
                                   (gen_random_uuid(), 'maska nr 3', 140.00, (select id from categories where name='Masks'), (select id from producers where name='OMS'),'2024-07-30 10:00:00', 99.00);