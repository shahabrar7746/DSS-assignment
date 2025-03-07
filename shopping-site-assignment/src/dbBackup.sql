toc.dat                                                                                             0000600 0004000 0002000 00000026435 14757344535 0014472 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        PGDMP                        }            dbhdemo    14.2 (Debian 14.2-1.pgdg110+1)    17.3 )    !           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false         "           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false         #           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false         $           1262    16503    dbhdemo    DATABASE     r   CREATE DATABASE dbhdemo WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE dbhdemo;
                     postgres    false         %           0    0    DATABASE dbhdemo    ACL     *   GRANT ALL ON DATABASE dbhdemo TO dbhuser;
                        postgres    false    3364                     2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                     postgres    false         &           0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                        postgres    false    4         F           1247    16552 
   currencies    TYPE     K   CREATE TYPE public.currencies AS ENUM (
    'INR',
    'USD',
    'EUR'
);
    DROP TYPE public.currencies;
       public               dbhuser    false    4         O           1247    16584    order_status    TYPE     k   CREATE TYPE public.order_status AS ENUM (
    'SHIPPED',
    'DELIVERED',
    'ORDERED',
    'CANCELED'
);
    DROP TYPE public.order_status;
       public               dbhuser    false    4         C           1247    16540    p_type    TYPE     t   CREATE TYPE public.p_type AS ENUM (
    'PHONE',
    'FURNITURE',
    'APPLIANCES',
    'MAKEUP',
    'CLOTHING'
);
    DROP TYPE public.p_type;
       public               dbhuser    false    4         =           1247    16508 	   user_role    TYPE     g   CREATE TYPE public.user_role AS ENUM (
    'ADMIN',
    'SUPER_ADMIN',
    'SELLER',
    'CUSTOMER'
);
    DROP TYPE public.user_role;
       public               dbhuser    false    4         �            1259    16518    customer    TABLE     E  CREATE TABLE public.customer (
    customer_id integer NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    address text NOT NULL,
    role public.user_role,
    registered_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.customer;
       public         heap r       dbhuser    false    4    829         �            1259    16517    customer_customer_id_seq    SEQUENCE     �   CREATE SEQUENCE public.customer_customer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.customer_customer_id_seq;
       public               dbhuser    false    4    210         '           0    0    customer_customer_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.customer_customer_id_seq OWNED BY public.customer.customer_id;
          public               dbhuser    false    209         �            1259    16594    orders    TABLE     �   CREATE TABLE public.orders (
    order_id integer NOT NULL,
    customer_id integer,
    product_id integer,
    status public.order_status,
    ordered_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    seller_id integer
);
    DROP TABLE public.orders;
       public         heap r       dbhuser    false    4    847         �            1259    16593    orders_order_id_seq    SEQUENCE     �   CREATE SEQUENCE public.orders_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.orders_order_id_seq;
       public               dbhuser    false    216    4         (           0    0    orders_order_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.orders_order_id_seq OWNED BY public.orders.order_id;
          public               dbhuser    false    215         �            1259    16560    product    TABLE     �   CREATE TABLE public.product (
    product_id integer NOT NULL,
    price numeric(10,2),
    product_name text,
    currency public.currencies,
    product_type public.p_type
);
    DROP TABLE public.product;
       public         heap r       dbhuser    false    835    4    838         �            1259    16559    product_product_id_seq    SEQUENCE     �   CREATE SEQUENCE public.product_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.product_product_id_seq;
       public               dbhuser    false    4    212         )           0    0    product_product_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.product_product_id_seq OWNED BY public.product.product_id;
          public               dbhuser    false    211         �            1259    16569    seller    TABLE     �   CREATE TABLE public.seller (
    seller_id integer NOT NULL,
    name text,
    role public.user_role,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.seller;
       public         heap r       dbhuser    false    829    4         �            1259    16568    seller_seller_id_seq    SEQUENCE     �   CREATE SEQUENCE public.seller_seller_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.seller_seller_id_seq;
       public               dbhuser    false    214    4         *           0    0    seller_seller_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.seller_seller_id_seq OWNED BY public.seller.seller_id;
          public               dbhuser    false    213         z           2604    16521    customer customer_id    DEFAULT     |   ALTER TABLE ONLY public.customer ALTER COLUMN customer_id SET DEFAULT nextval('public.customer_customer_id_seq'::regclass);
 C   ALTER TABLE public.customer ALTER COLUMN customer_id DROP DEFAULT;
       public               dbhuser    false    209    210    210                    2604    16597    orders order_id    DEFAULT     r   ALTER TABLE ONLY public.orders ALTER COLUMN order_id SET DEFAULT nextval('public.orders_order_id_seq'::regclass);
 >   ALTER TABLE public.orders ALTER COLUMN order_id DROP DEFAULT;
       public               dbhuser    false    216    215    216         |           2604    16563    product product_id    DEFAULT     x   ALTER TABLE ONLY public.product ALTER COLUMN product_id SET DEFAULT nextval('public.product_product_id_seq'::regclass);
 A   ALTER TABLE public.product ALTER COLUMN product_id DROP DEFAULT;
       public               dbhuser    false    212    211    212         }           2604    16572    seller seller_id    DEFAULT     t   ALTER TABLE ONLY public.seller ALTER COLUMN seller_id SET DEFAULT nextval('public.seller_seller_id_seq'::regclass);
 ?   ALTER TABLE public.seller ALTER COLUMN seller_id DROP DEFAULT;
       public               dbhuser    false    214    213    214                   0    16518    customer 
   TABLE DATA           d   COPY public.customer (customer_id, email, password, name, address, role, registered_on) FROM stdin;
    public               dbhuser    false    210       3352.dat           0    16594    orders 
   TABLE DATA           b   COPY public.orders (order_id, customer_id, product_id, status, ordered_on, seller_id) FROM stdin;
    public               dbhuser    false    216       3358.dat           0    16560    product 
   TABLE DATA           Z   COPY public.product (product_id, price, product_name, currency, product_type) FROM stdin;
    public               dbhuser    false    212       3354.dat           0    16569    seller 
   TABLE DATA           C   COPY public.seller (seller_id, name, role, created_on) FROM stdin;
    public               dbhuser    false    214       3356.dat +           0    0    customer_customer_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.customer_customer_id_seq', 7, true);
          public               dbhuser    false    209         ,           0    0    orders_order_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.orders_order_id_seq', 1, false);
          public               dbhuser    false    215         -           0    0    product_product_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.product_product_id_seq', 13, true);
          public               dbhuser    false    211         .           0    0    seller_seller_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.seller_seller_id_seq', 1, true);
          public               dbhuser    false    213         �           2606    16528    customer customer_email_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_email_key UNIQUE (email);
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_email_key;
       public                 dbhuser    false    210         �           2606    16526    customer customer_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (customer_id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public                 dbhuser    false    210         �           2606    16600    orders orders_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);
 <   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_pkey;
       public                 dbhuser    false    216         �           2606    16567    product product_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public                 dbhuser    false    212         �           2606    16577    seller seller_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.seller
    ADD CONSTRAINT seller_pkey PRIMARY KEY (seller_id);
 <   ALTER TABLE ONLY public.seller DROP CONSTRAINT seller_pkey;
       public                 dbhuser    false    214         �           2606    16615    orders fk_seller    FK CONSTRAINT     y   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES public.seller(seller_id);
 :   ALTER TABLE ONLY public.orders DROP CONSTRAINT fk_seller;
       public               dbhuser    false    214    216    3208                                                                                                                                                                                                                                           3352.dat                                                                                            0000600 0004000 0002000 00000000326 14757344535 0014270 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	superAdmin@gmail.com	1234567890	super admin	ambernath	SUPER_ADMIN	2025-02-24 11:10:10.938341
7	tom@gmail.com	Man@2	TOM	mumbai	CUSTOMER	2025-02-25 10:00:33.84347
3	A	A	A	A	CUSTOMER	2025-02-25 14:48:05.476817
\.


                                                                                                                                                                                                                                                                                                          3358.dat                                                                                            0000600 0004000 0002000 00000000005 14757344535 0014270 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        \.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           3354.dat                                                                                            0000600 0004000 0002000 00000000360 14757344535 0014270 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        6	893.20	TABLE FAN	INR	APPLIANCES
7	473.23	FAN	INR	APPLIANCES
8	473.23	FAN	INR	APPLIANCES
9	473.23	IPHONE	INR	PHONE
10	47213.23	IPHONE	INR	PHONE
11	47213.23	SOFA	INR	FURNITURE
12	243.23	t-shirt	INR	CLOTHING
13	83.63	MASKARA	INR	MAKEUP
\.


                                                                                                                                                                                                                                                                                3356.dat                                                                                            0000600 0004000 0002000 00000000061 14757344535 0014270 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	Crompton	ADMIN	2025-02-24 10:23:45.882146
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                               restore.sql                                                                                         0000600 0004000 0002000 00000022641 14757344535 0015412 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        --
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 17.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE dbhdemo;
--
-- Name: dbhdemo; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE dbhdemo WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE dbhdemo OWNER TO postgres;

\connect dbhdemo

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: currencies; Type: TYPE; Schema: public; Owner: dbhuser
--

CREATE TYPE public.currencies AS ENUM (
    'INR',
    'USD',
    'EUR'
);


ALTER TYPE public.currencies OWNER TO dbhuser;

--
-- Name: order_status; Type: TYPE; Schema: public; Owner: dbhuser
--

CREATE TYPE public.order_status AS ENUM (
    'SHIPPED',
    'DELIVERED',
    'ORDERED',
    'CANCELED'
);


ALTER TYPE public.order_status OWNER TO dbhuser;

--
-- Name: p_type; Type: TYPE; Schema: public; Owner: dbhuser
--

CREATE TYPE public.p_type AS ENUM (
    'PHONE',
    'FURNITURE',
    'APPLIANCES',
    'MAKEUP',
    'CLOTHING'
);


ALTER TYPE public.p_type OWNER TO dbhuser;

--
-- Name: user_role; Type: TYPE; Schema: public; Owner: dbhuser
--

CREATE TYPE public.user_role AS ENUM (
    'ADMIN',
    'SUPER_ADMIN',
    'SELLER',
    'CUSTOMER'
);


ALTER TYPE public.user_role OWNER TO dbhuser;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: customer; Type: TABLE; Schema: public; Owner: dbhuser
--

CREATE TABLE public.customer (
    customer_id integer NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    address text NOT NULL,
    role public.user_role,
    registered_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.customer OWNER TO dbhuser;

--
-- Name: customer_customer_id_seq; Type: SEQUENCE; Schema: public; Owner: dbhuser
--

CREATE SEQUENCE public.customer_customer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.customer_customer_id_seq OWNER TO dbhuser;

--
-- Name: customer_customer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbhuser
--

ALTER SEQUENCE public.customer_customer_id_seq OWNED BY public.customer.customer_id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: dbhuser
--

CREATE TABLE public.orders (
    order_id integer NOT NULL,
    customer_id integer,
    product_id integer,
    status public.order_status,
    ordered_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    seller_id integer
);


ALTER TABLE public.orders OWNER TO dbhuser;

--
-- Name: orders_order_id_seq; Type: SEQUENCE; Schema: public; Owner: dbhuser
--

CREATE SEQUENCE public.orders_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_order_id_seq OWNER TO dbhuser;

--
-- Name: orders_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbhuser
--

ALTER SEQUENCE public.orders_order_id_seq OWNED BY public.orders.order_id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: dbhuser
--

CREATE TABLE public.product (
    product_id integer NOT NULL,
    price numeric(10,2),
    product_name text,
    currency public.currencies,
    product_type public.p_type
);


ALTER TABLE public.product OWNER TO dbhuser;

--
-- Name: product_product_id_seq; Type: SEQUENCE; Schema: public; Owner: dbhuser
--

CREATE SEQUENCE public.product_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_product_id_seq OWNER TO dbhuser;

--
-- Name: product_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbhuser
--

ALTER SEQUENCE public.product_product_id_seq OWNED BY public.product.product_id;


--
-- Name: seller; Type: TABLE; Schema: public; Owner: dbhuser
--

CREATE TABLE public.seller (
    seller_id integer NOT NULL,
    name text,
    role public.user_role,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.seller OWNER TO dbhuser;

--
-- Name: seller_seller_id_seq; Type: SEQUENCE; Schema: public; Owner: dbhuser
--

CREATE SEQUENCE public.seller_seller_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seller_seller_id_seq OWNER TO dbhuser;

--
-- Name: seller_seller_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dbhuser
--

ALTER SEQUENCE public.seller_seller_id_seq OWNED BY public.seller.seller_id;


--
-- Name: customer customer_id; Type: DEFAULT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.customer ALTER COLUMN customer_id SET DEFAULT nextval('public.customer_customer_id_seq'::regclass);


--
-- Name: orders order_id; Type: DEFAULT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.orders ALTER COLUMN order_id SET DEFAULT nextval('public.orders_order_id_seq'::regclass);


--
-- Name: product product_id; Type: DEFAULT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.product ALTER COLUMN product_id SET DEFAULT nextval('public.product_product_id_seq'::regclass);


--
-- Name: seller seller_id; Type: DEFAULT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.seller ALTER COLUMN seller_id SET DEFAULT nextval('public.seller_seller_id_seq'::regclass);


--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: dbhuser
--

COPY public.customer (customer_id, email, password, name, address, role, registered_on) FROM stdin;
\.
COPY public.customer (customer_id, email, password, name, address, role, registered_on) FROM '$$PATH$$/3352.dat';

--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: dbhuser
--

COPY public.orders (order_id, customer_id, product_id, status, ordered_on, seller_id) FROM stdin;
\.
COPY public.orders (order_id, customer_id, product_id, status, ordered_on, seller_id) FROM '$$PATH$$/3358.dat';

--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: dbhuser
--

COPY public.product (product_id, price, product_name, currency, product_type) FROM stdin;
\.
COPY public.product (product_id, price, product_name, currency, product_type) FROM '$$PATH$$/3354.dat';

--
-- Data for Name: seller; Type: TABLE DATA; Schema: public; Owner: dbhuser
--

COPY public.seller (seller_id, name, role, created_on) FROM stdin;
\.
COPY public.seller (seller_id, name, role, created_on) FROM '$$PATH$$/3356.dat';

--
-- Name: customer_customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbhuser
--

SELECT pg_catalog.setval('public.customer_customer_id_seq', 7, true);


--
-- Name: orders_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbhuser
--

SELECT pg_catalog.setval('public.orders_order_id_seq', 1, false);


--
-- Name: product_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbhuser
--

SELECT pg_catalog.setval('public.product_product_id_seq', 13, true);


--
-- Name: seller_seller_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dbhuser
--

SELECT pg_catalog.setval('public.seller_seller_id_seq', 1, true);


--
-- Name: customer customer_email_key; Type: CONSTRAINT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_email_key UNIQUE (email);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (customer_id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);


--
-- Name: seller seller_pkey; Type: CONSTRAINT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.seller
    ADD CONSTRAINT seller_pkey PRIMARY KEY (seller_id);


--
-- Name: orders fk_seller; Type: FK CONSTRAINT; Schema: public; Owner: dbhuser
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES public.seller(seller_id);


--
-- Name: DATABASE dbhdemo; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON DATABASE dbhdemo TO dbhuser;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               