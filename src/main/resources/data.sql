INSERT INTO product (name, price, image_url)
VALUES ('아이스 카페 아메리카노 T',
        600,
        'https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg');

INSERT INTO product (name, price, image_url)
VALUES ('코코아 파우더',
        7600,
        'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcRWDlZJBZiXDT8JlPp8iS5eeyJDdsISVl9lBSTXgcLhUAjCjU-qttjbpgg8-JPRHMWWOKEuGqlQWTqIvg_Hw_dAaEdZYPM-JF5z7KMOGPGkpGIj3z9WZMJG');

-- 상품 ID: 1 (아이스 카페 아메리카노 T)
insert into product_option (product_id, option, quantity)
values (1, 'Tall 사이즈 (얼음 많음)', 1000);

insert into product_option (product_id, option, quantity)
values (1, 'Tall 사이즈 (연하게)', 850);

-- 상품 ID: 2 (코코아 파우더)
insert into product_option (product_id, option, quantity)
values (2, '200g 소포장 [+]', 500);

insert into product_option (product_id, option, quantity)
values (2, '500g 중간포장 [Best]', 300);

insert into product_option (product_id, option, quantity)
values (2, '1kg 대용량 (선물용)', 150);