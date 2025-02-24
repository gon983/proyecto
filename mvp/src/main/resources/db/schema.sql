--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-02-24 19:07:13

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
-- TOC entry 6 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 6087 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 17479)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    id_category uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100)
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17491)
-- Name: city; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.city (
    id_city uuid NOT NULL,
    name character varying(45) NOT NULL,
    fk_country uuid NOT NULL
);


ALTER TABLE public.city OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17541)
-- Name: collection_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.collection_point (
    id_collection_point uuid NOT NULL,
    name character varying(45) NOT NULL,
    fk_neighborhood uuid NOT NULL,
    use_price double precision,
    fk_owner uuid NOT NULL,
    description character varying(1500),
    ubication public.geometry(Point,4326) NOT NULL
);


ALTER TABLE public.collection_point OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17591)
-- Name: collection_point_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.collection_point_history (
    id_collection_point_history uuid NOT NULL,
    fk_collection_point uuid NOT NULL,
    fk_collection_point_state uuid NOT NULL,
    description character varying(1500),
    init timestamp without time zone NOT NULL,
    finish timestamp without time zone
);


ALTER TABLE public.collection_point_history OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17608)
-- Name: collection_point_payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.collection_point_payments (
    id_collection_point_payments uuid NOT NULL,
    fk_collection_point uuid NOT NULL,
    date timestamp without time zone NOT NULL,
    note character varying(45) NOT NULL,
    amount double precision NOT NULL
);


ALTER TABLE public.collection_point_payments OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17536)
-- Name: collection_point_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.collection_point_state (
    id_collection_point_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.collection_point_state OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17484)
-- Name: country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.country (
    id_country uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.country OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17618)
-- Name: default_donation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.default_donation (
    id_default_donation uuid NOT NULL,
    level character varying(1) NOT NULL,
    fk_organization uuid NOT NULL,
    CONSTRAINT default_donation_level_check CHECK (((level)::text = ANY ((ARRAY['1'::character varying, '2'::character varying, '3'::character varying, '4'::character varying, '5'::character varying, '6'::character varying, '7'::character varying])::text[])))
);


ALTER TABLE public.default_donation OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 17661)
-- Name: default_product_x_collection_point_x_week; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.default_product_x_collection_point_x_week (
    id_default_product_x_collection_point uuid NOT NULL,
    fk_collection_point uuid NOT NULL,
    fk_product uuid,
    fk_standar_product uuid,
    date_init_week timestamp without time zone NOT NULL
);


ALTER TABLE public.default_product_x_collection_point_x_week OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17501)
-- Name: locality; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.locality (
    id_locality uuid NOT NULL,
    name character varying(45) NOT NULL,
    fk_city uuid NOT NULL
);


ALTER TABLE public.locality OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17511)
-- Name: neighborhood; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.neighborhood (
    id_neighborhood uuid NOT NULL,
    name character varying(45) NOT NULL,
    fk_locality uuid NOT NULL
);


ALTER TABLE public.neighborhood OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 17723)
-- Name: neighborhood_package; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.neighborhood_package (
    id_neighborhood_package uuid NOT NULL,
    fk_in_charge uuid NOT NULL,
    fk_collection_point uuid NOT NULL,
    date timestamp without time zone NOT NULL
);


ALTER TABLE public.neighborhood_package OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 17738)
-- Name: neighborhood_package_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.neighborhood_package_history (
    id_neighborhood_package_history uuid NOT NULL,
    fk_neighborhood_package uuid NOT NULL,
    fk_neighborhood_package_state uuid NOT NULL,
    description character varying(1500),
    init timestamp without time zone NOT NULL,
    finish timestamp without time zone
);


ALTER TABLE public.neighborhood_package_history OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 17718)
-- Name: neighborhood_package_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.neighborhood_package_state (
    id_neighborhood_package_state uuid NOT NULL,
    name character varying(45)
);


ALTER TABLE public.neighborhood_package_state OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17526)
-- Name: ong; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ong (
    id_ong uuid NOT NULL,
    name character varying(100) NOT NULL,
    account character varying(100) NOT NULL
);


ALTER TABLE public.ong OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17531)
-- Name: payment_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_method (
    id_payment_method uuid NOT NULL,
    name character varying(45)
);


ALTER TABLE public.payment_method OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 17639)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id_product uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(45) NOT NULL,
    stock double precision NOT NULL,
    alert_stock double precision,
    photo character varying(45),
    unit_measurement character varying(100) NOT NULL,
    fk_productor uuid NOT NULL,
    unity_price double precision NOT NULL,
    fk_locality uuid,
    fk_standar_product uuid NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 17688)
-- Name: product_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_history (
    id_product_history uuid NOT NULL,
    fk_product uuid NOT NULL,
    fk_product_state uuid NOT NULL,
    description character varying(1500),
    init timestamp without time zone NOT NULL,
    finish timestamp without time zone
);


ALTER TABLE public.product_history OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 17681)
-- Name: product_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_state (
    id_product_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.product_state OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 17760)
-- Name: purchase; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase (
    id_purchase uuid NOT NULL,
    fk_user uuid NOT NULL,
    amount double precision NOT NULL,
    fk_neighborhood_package uuid,
    fk_payment_method uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    level character varying(10),
    fk_current_state uuid NOT NULL
);


ALTER TABLE public.purchase OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 17807)
-- Name: purchase_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_detail (
    id_purchase_detail uuid NOT NULL,
    fk_product uuid NOT NULL,
    quantity double precision NOT NULL,
    fk_purchase uuid NOT NULL,
    unit_price double precision NOT NULL,
    fk_state uuid NOT NULL,
    created_by uuid,
    updated_by uuid,
    updated_at timestamp without time zone,
    created_at timestamp without time zone
);


ALTER TABLE public.purchase_detail OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 17802)
-- Name: purchase_detail_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_detail_state (
    id_purchase_detail_state uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.purchase_detail_state OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 17827)
-- Name: purchase_detail_state_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_detail_state_history (
    id_purchase_detail_state_history uuid NOT NULL,
    fk_purchase_detail uuid NOT NULL,
    fk_purchase_detail_state uuid NOT NULL,
    initial_date timestamp without time zone NOT NULL,
    final_date timestamp without time zone,
    description character varying(1500)
);


ALTER TABLE public.purchase_detail_state_history OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 17755)
-- Name: purchase_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_state (
    id_purchase_state uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.purchase_state OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 17785)
-- Name: purchase_state_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_state_history (
    id_purchase_state_history uuid NOT NULL,
    fk_purchase_state uuid NOT NULL,
    fk_purchase uuid NOT NULL,
    initial_date timestamp without time zone NOT NULL,
    final_date timestamp without time zone,
    description character varying(1500)
);


ALTER TABLE public.purchase_state_history OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17521)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id_role uuid NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 17849)
-- Name: sale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale (
    id_sale uuid NOT NULL,
    amount double precision NOT NULL,
    fk_productor uuid NOT NULL,
    fk_deliver_guy uuid NOT NULL,
    fk_payment_method uuid,
    bill character varying(45)
);


ALTER TABLE public.sale OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 17869)
-- Name: sale_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_detail (
    id_sale_detail uuid NOT NULL,
    fk_product uuid NOT NULL,
    quantity double precision NOT NULL,
    fk_sale uuid NOT NULL
);


ALTER TABLE public.sale_detail OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 17884)
-- Name: sale_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_history (
    id_sale_history uuid NOT NULL,
    fk_sale uuid NOT NULL,
    fk_sale_state uuid NOT NULL,
    description character varying(1500),
    init timestamp without time zone NOT NULL,
    finish timestamp without time zone
);


ALTER TABLE public.sale_history OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 17844)
-- Name: sale_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_state (
    id_sale_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.sale_state OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 17629)
-- Name: standar_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.standar_product (
    id_standar_product uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100),
    fk_category uuid NOT NULL
);


ALTER TABLE public.standar_product OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 17705)
-- Name: stock_movement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock_movement (
    id_stock_movement uuid NOT NULL,
    fk_product uuid NOT NULL,
    quantity double precision NOT NULL,
    type character varying(7) NOT NULL,
    date date NOT NULL,
    coment character varying(1500),
    CONSTRAINT stock_movement_type_check CHECK (((type)::text = ANY ((ARRAY['in'::character varying, 'out'::character varying, 'edition'::character varying])::text[])))
);


ALTER TABLE public.stock_movement OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 17906)
-- Name: user_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_history (
    id_user_history uuid NOT NULL,
    fk_user uuid NOT NULL,
    fk_user_state uuid NOT NULL,
    description character varying(1500),
    init timestamp without time zone NOT NULL,
    finish timestamp without time zone
);


ALTER TABLE public.user_history OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 17901)
-- Name: user_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_state (
    id_user_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.user_state OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17553)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id_user uuid NOT NULL,
    username character varying(45) NOT NULL,
    email character varying(45) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    first_name character varying(45) NOT NULL,
    last_name character varying(45) NOT NULL,
    document_type character varying(45) NOT NULL,
    document_number character varying(45) NOT NULL,
    fk_neighborhood uuid NOT NULL,
    phone character varying(45),
    photo character varying(45),
    minimal_sale double precision DEFAULT 0,
    fk_role_one uuid NOT NULL,
    fk_rol_two uuid,
    fk_role_three uuid,
    fk_collection_point_suscribed uuid
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 17923)
-- Name: vote; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vote (
    id_vote character varying(100) NOT NULL,
    fk_product uuid NOT NULL,
    fk_user uuid NOT NULL,
    calification integer NOT NULL,
    date timestamp without time zone NOT NULL,
    comment text
);


ALTER TABLE public.vote OWNER TO postgres;

--
-- TOC entry 6046 (class 0 OID 17479)
-- Dependencies: 224
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.category VALUES ('097ef14d-26c5-4b3a-a113-25e78afb7f69', 'Frutas');


--
-- TOC entry 6048 (class 0 OID 17491)
-- Dependencies: 226
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.city VALUES ('602ec196-d4d7-4194-a660-9b4afc9a07ea', 'Cordoba', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('49519714-aad2-4b19-8f6b-bf688e565642', 'CABA', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('29559450-d74c-45a0-83f4-cdfc0a55db7d', 'Rosario', '6a9fe61b-be17-4821-a4a3-71fa282e9289');


--
-- TOC entry 6055 (class 0 OID 17541)
-- Dependencies: 233
-- Data for Name: collection_point; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.collection_point VALUES ('c446f03c-490c-4fe9-a63a-ad407c981c29', 'Club Social BJ', '23f4f7d4-7923-495d-85d0-27f34d932537', 99.99, '6982b8a2-2a9e-47a4-8df9-85f3482b16e2', 'buen lugar', '0101000020E61000003563D17476723FC01AA88C7F9F0B50C0');


--
-- TOC entry 6057 (class 0 OID 17591)
-- Dependencies: 235
-- Data for Name: collection_point_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6058 (class 0 OID 17608)
-- Dependencies: 236
-- Data for Name: collection_point_payments; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6054 (class 0 OID 17536)
-- Dependencies: 232
-- Data for Name: collection_point_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.collection_point_state VALUES ('eb9caee8-dc88-4bcb-a804-3ab6fa0984a1', 'pending');
INSERT INTO public.collection_point_state VALUES ('19a226ee-c0e4-4d78-9ddd-dc4d68a27aaf', 'suspended');


--
-- TOC entry 6047 (class 0 OID 17484)
-- Dependencies: 225
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.country VALUES ('6a9fe61b-be17-4821-a4a3-71fa282e9289', 'Argentina');
INSERT INTO public.country VALUES ('de76b765-5927-4fa4-965b-031835705b18', 'Brasil');
INSERT INTO public.country VALUES ('578dde76-7898-41a1-8ebd-4338f2cb12ba', 'Chile');
INSERT INTO public.country VALUES ('91c06a87-4500-4223-9a08-42bcd4786681', 'Peru');
INSERT INTO public.country VALUES ('6a79ddc2-60e2-4be4-9b11-dcc7a894c4af', 'Uruguay');
INSERT INTO public.country VALUES ('9bba1fb7-b382-4fb3-a205-372a5bd3caec', 'Venezuela');
INSERT INTO public.country VALUES ('ebb44d2a-cdda-4653-b465-cae942f2bab7', 'Colombia');


--
-- TOC entry 6059 (class 0 OID 17618)
-- Dependencies: 237
-- Data for Name: default_donation; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6062 (class 0 OID 17661)
-- Dependencies: 240
-- Data for Name: default_product_x_collection_point_x_week; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6049 (class 0 OID 17501)
-- Dependencies: 227
-- Data for Name: locality; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locality VALUES ('10fbe8c3-0291-4bc7-a96a-7d10782b36ec', 'Capital', '602ec196-d4d7-4194-a660-9b4afc9a07ea');


--
-- TOC entry 6050 (class 0 OID 17511)
-- Dependencies: 228
-- Data for Name: neighborhood; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.neighborhood VALUES ('23f4f7d4-7923-495d-85d0-27f34d932537', 'Barrio Jardin', '10fbe8c3-0291-4bc7-a96a-7d10782b36ec');


--
-- TOC entry 6067 (class 0 OID 17723)
-- Dependencies: 245
-- Data for Name: neighborhood_package; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6068 (class 0 OID 17738)
-- Dependencies: 246
-- Data for Name: neighborhood_package_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6066 (class 0 OID 17718)
-- Dependencies: 244
-- Data for Name: neighborhood_package_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6052 (class 0 OID 17526)
-- Dependencies: 230
-- Data for Name: ong; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6053 (class 0 OID 17531)
-- Dependencies: 231
-- Data for Name: payment_method; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6061 (class 0 OID 17639)
-- Dependencies: 239
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6064 (class 0 OID 17688)
-- Dependencies: 242
-- Data for Name: product_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6063 (class 0 OID 17681)
-- Dependencies: 241
-- Data for Name: product_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6070 (class 0 OID 17760)
-- Dependencies: 248
-- Data for Name: purchase; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6073 (class 0 OID 17807)
-- Dependencies: 251
-- Data for Name: purchase_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6072 (class 0 OID 17802)
-- Dependencies: 250
-- Data for Name: purchase_detail_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6074 (class 0 OID 17827)
-- Dependencies: 252
-- Data for Name: purchase_detail_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6069 (class 0 OID 17755)
-- Dependencies: 247
-- Data for Name: purchase_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6071 (class 0 OID 17785)
-- Dependencies: 249
-- Data for Name: purchase_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6051 (class 0 OID 17521)
-- Dependencies: 229
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role VALUES ('5c0e356c-ef1a-11ef-b668-52cf1c1a32ab', 'producer');
INSERT INTO public.role VALUES ('6b4492b9-1f7d-4ef6-b3bf-7a8c89e4fe83', 'cp owner');


--
-- TOC entry 6076 (class 0 OID 17849)
-- Dependencies: 254
-- Data for Name: sale; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6077 (class 0 OID 17869)
-- Dependencies: 255
-- Data for Name: sale_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6078 (class 0 OID 17884)
-- Dependencies: 256
-- Data for Name: sale_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6075 (class 0 OID 17844)
-- Dependencies: 253
-- Data for Name: sale_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5756 (class 0 OID 16721)
-- Dependencies: 220
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6060 (class 0 OID 17629)
-- Dependencies: 238
-- Data for Name: standar_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.standar_product VALUES ('6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb', 'Banana', '097ef14d-26c5-4b3a-a113-25e78afb7f69');


--
-- TOC entry 6065 (class 0 OID 17705)
-- Dependencies: 243
-- Data for Name: stock_movement; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6080 (class 0 OID 17906)
-- Dependencies: 258
-- Data for Name: user_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6079 (class 0 OID 17901)
-- Dependencies: 257
-- Data for Name: user_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6056 (class 0 OID 17553)
-- Dependencies: 234
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('b088c879-3fe7-4bcd-a6fc-89efb341c602', 'juanperez', 'juan.perez@example.com', '2025-02-22 16:03:26.961483', 'Juan', 'ProductorDeTomates', 'DNI', '12345678', '23f4f7d4-7923-495d-85d0-27f34d932537', '+5491122334455', NULL, 0, '5c0e356c-ef1a-11ef-b668-52cf1c1a32ab', NULL, NULL, NULL);
INSERT INTO public.users VALUES ('6982b8a2-2a9e-47a4-8df9-85f3482b16e2', 'rodrigo', 'rodrigo@example.com', '2025-02-22 16:12:39.413808', 'Rodrigo', 'Dueño Plaza', 'DNI', '88888888', '23f4f7d4-7923-495d-85d0-27f34d932537', '+5491129898', NULL, 0, '6b4492b9-1f7d-4ef6-b3bf-7a8c89e4fe83', NULL, NULL, NULL);


--
-- TOC entry 6081 (class 0 OID 17923)
-- Dependencies: 259
-- Data for Name: vote; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5767 (class 2606 OID 17483)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id_category);


--
-- TOC entry 5773 (class 2606 OID 17495)
-- Name: city city_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id_city);


--
-- TOC entry 5793 (class 2606 OID 17597)
-- Name: collection_point_history collection_point_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_history
    ADD CONSTRAINT collection_point_history_pkey PRIMARY KEY (id_collection_point_history);


--
-- TOC entry 5795 (class 2606 OID 17612)
-- Name: collection_point_payments collection_point_payments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_payments
    ADD CONSTRAINT collection_point_payments_pkey PRIMARY KEY (id_collection_point_payments);


--
-- TOC entry 5787 (class 2606 OID 17547)
-- Name: collection_point collection_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point
    ADD CONSTRAINT collection_point_pkey PRIMARY KEY (id_collection_point);


--
-- TOC entry 5785 (class 2606 OID 17540)
-- Name: collection_point_state collection_point_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_state
    ADD CONSTRAINT collection_point_state_pkey PRIMARY KEY (id_collection_point_state);


--
-- TOC entry 5769 (class 2606 OID 17490)
-- Name: country country_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_name_key UNIQUE (name);


--
-- TOC entry 5771 (class 2606 OID 17488)
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id_country);


--
-- TOC entry 5797 (class 2606 OID 17623)
-- Name: default_donation default_donation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_donation
    ADD CONSTRAINT default_donation_pkey PRIMARY KEY (id_default_donation);


--
-- TOC entry 5805 (class 2606 OID 17665)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_week_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_week_pkey PRIMARY KEY (id_default_product_x_collection_point);


--
-- TOC entry 5775 (class 2606 OID 17505)
-- Name: locality locality_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_pkey PRIMARY KEY (id_locality);


--
-- TOC entry 5819 (class 2606 OID 17744)
-- Name: neighborhood_package_history neighborhood_package_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package_history
    ADD CONSTRAINT neighborhood_package_history_pkey PRIMARY KEY (id_neighborhood_package_history);


--
-- TOC entry 5817 (class 2606 OID 17727)
-- Name: neighborhood_package neighborhood_package_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package
    ADD CONSTRAINT neighborhood_package_pkey PRIMARY KEY (id_neighborhood_package);


--
-- TOC entry 5815 (class 2606 OID 17722)
-- Name: neighborhood_package_state neighborhood_package_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package_state
    ADD CONSTRAINT neighborhood_package_state_pkey PRIMARY KEY (id_neighborhood_package_state);


--
-- TOC entry 5777 (class 2606 OID 17515)
-- Name: neighborhood neighborhood_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_pkey PRIMARY KEY (id_neighborhood);


--
-- TOC entry 5781 (class 2606 OID 17530)
-- Name: ong ong_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ong
    ADD CONSTRAINT ong_pkey PRIMARY KEY (id_ong);


--
-- TOC entry 5783 (class 2606 OID 17535)
-- Name: payment_method payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (id_payment_method);


--
-- TOC entry 5811 (class 2606 OID 17694)
-- Name: product_history product_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_pkey PRIMARY KEY (id_product_history);


--
-- TOC entry 5801 (class 2606 OID 17645)
-- Name: product product_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_name_key UNIQUE (name);


--
-- TOC entry 5803 (class 2606 OID 17643)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id_product);


--
-- TOC entry 5807 (class 2606 OID 17687)
-- Name: product_state product_state_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_name_key UNIQUE (name);


--
-- TOC entry 5809 (class 2606 OID 17685)
-- Name: product_state product_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_pkey PRIMARY KEY (id_product_state);


--
-- TOC entry 5829 (class 2606 OID 17811)
-- Name: purchase_detail purchase_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_pkey PRIMARY KEY (id_purchase_detail);


--
-- TOC entry 5831 (class 2606 OID 17833)
-- Name: purchase_detail_state_history purchase_detail_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_pkey PRIMARY KEY (id_purchase_detail_state_history);


--
-- TOC entry 5827 (class 2606 OID 17806)
-- Name: purchase_detail_state purchase_detail_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state
    ADD CONSTRAINT purchase_detail_state_pkey PRIMARY KEY (id_purchase_detail_state);


--
-- TOC entry 5823 (class 2606 OID 17764)
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (id_purchase);


--
-- TOC entry 5825 (class 2606 OID 17791)
-- Name: purchase_state_history purchase_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_pkey PRIMARY KEY (id_purchase_state_history);


--
-- TOC entry 5821 (class 2606 OID 17759)
-- Name: purchase_state purchase_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state
    ADD CONSTRAINT purchase_state_pkey PRIMARY KEY (id_purchase_state);


--
-- TOC entry 5779 (class 2606 OID 17525)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id_role);


--
-- TOC entry 5837 (class 2606 OID 17873)
-- Name: sale_detail sale_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_detail
    ADD CONSTRAINT sale_detail_pkey PRIMARY KEY (id_sale_detail);


--
-- TOC entry 5839 (class 2606 OID 17890)
-- Name: sale_history sale_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_history
    ADD CONSTRAINT sale_history_pkey PRIMARY KEY (id_sale_history);


--
-- TOC entry 5835 (class 2606 OID 17853)
-- Name: sale sale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (id_sale);


--
-- TOC entry 5833 (class 2606 OID 17848)
-- Name: sale_state sale_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_state
    ADD CONSTRAINT sale_state_pkey PRIMARY KEY (id_sale_state);


--
-- TOC entry 5799 (class 2606 OID 17633)
-- Name: standar_product standar_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_pkey PRIMARY KEY (id_standar_product);


--
-- TOC entry 5813 (class 2606 OID 17712)
-- Name: stock_movement stock_movement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_pkey PRIMARY KEY (id_stock_movement);


--
-- TOC entry 5789 (class 2606 OID 17560)
-- Name: users user_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_email_key UNIQUE (email);


--
-- TOC entry 5843 (class 2606 OID 17912)
-- Name: user_history user_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_pkey PRIMARY KEY (id_user_history);


--
-- TOC entry 5791 (class 2606 OID 17558)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id_user);


--
-- TOC entry 5841 (class 2606 OID 17905)
-- Name: user_state user_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_state
    ADD CONSTRAINT user_state_pkey PRIMARY KEY (id_user_state);


--
-- TOC entry 5845 (class 2606 OID 17929)
-- Name: vote vote_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_pkey PRIMARY KEY (id_vote);


--
-- TOC entry 5846 (class 2606 OID 17496)
-- Name: city city_fk_country_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_fk_country_fkey FOREIGN KEY (fk_country) REFERENCES public.country(id_country) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5849 (class 2606 OID 17548)
-- Name: collection_point collection_point_fk_neighborhood_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point
    ADD CONSTRAINT collection_point_fk_neighborhood_fkey FOREIGN KEY (fk_neighborhood) REFERENCES public.neighborhood(id_neighborhood) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5856 (class 2606 OID 17598)
-- Name: collection_point_history collection_point_history_fk_collection_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_history
    ADD CONSTRAINT collection_point_history_fk_collection_point_fkey FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5857 (class 2606 OID 17603)
-- Name: collection_point_history collection_point_history_fk_collection_point_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_history
    ADD CONSTRAINT collection_point_history_fk_collection_point_state_fkey FOREIGN KEY (fk_collection_point_state) REFERENCES public.collection_point_state(id_collection_point_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5858 (class 2606 OID 17613)
-- Name: collection_point_payments collection_point_payments_fk_collection_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_payments
    ADD CONSTRAINT collection_point_payments_fk_collection_point_fkey FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5859 (class 2606 OID 17624)
-- Name: default_donation default_donation_fk_organization_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_donation
    ADD CONSTRAINT default_donation_fk_organization_fkey FOREIGN KEY (fk_organization) REFERENCES public.ong(id_ong) ON UPDATE CASCADE;


--
-- TOC entry 5864 (class 2606 OID 17666)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_w_fk_collection_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_w_fk_collection_point_fkey FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point);


--
-- TOC entry 5865 (class 2606 OID 17676)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_we_fk_standar_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_we_fk_standar_product_fkey FOREIGN KEY (fk_standar_product) REFERENCES public.standar_product(id_standar_product);


--
-- TOC entry 5866 (class 2606 OID 17671)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_week_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_week_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product);


--
-- TOC entry 5850 (class 2606 OID 17586)
-- Name: collection_point fk_owner_collection_point; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point
    ADD CONSTRAINT fk_owner_collection_point FOREIGN KEY (fk_owner) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5847 (class 2606 OID 17506)
-- Name: locality locality_fk_city_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_fk_city_fkey FOREIGN KEY (fk_city) REFERENCES public.city(id_city) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5848 (class 2606 OID 17516)
-- Name: neighborhood neighborhood_fk_locality_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_fk_locality_fkey FOREIGN KEY (fk_locality) REFERENCES public.locality(id_locality) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5870 (class 2606 OID 17733)
-- Name: neighborhood_package neighborhood_package_fk_collection_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package
    ADD CONSTRAINT neighborhood_package_fk_collection_point_fkey FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5871 (class 2606 OID 17728)
-- Name: neighborhood_package neighborhood_package_fk_in_charge_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package
    ADD CONSTRAINT neighborhood_package_fk_in_charge_fkey FOREIGN KEY (fk_in_charge) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5872 (class 2606 OID 17745)
-- Name: neighborhood_package_history neighborhood_package_history_fk_neighborhood_package_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package_history
    ADD CONSTRAINT neighborhood_package_history_fk_neighborhood_package_fkey FOREIGN KEY (fk_neighborhood_package) REFERENCES public.neighborhood_package(id_neighborhood_package) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5873 (class 2606 OID 17750)
-- Name: neighborhood_package_history neighborhood_package_history_fk_neighborhood_package_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood_package_history
    ADD CONSTRAINT neighborhood_package_history_fk_neighborhood_package_state_fkey FOREIGN KEY (fk_neighborhood_package_state) REFERENCES public.neighborhood_package_state(id_neighborhood_package_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5861 (class 2606 OID 17651)
-- Name: product product_fk_locality_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_fk_locality_fkey FOREIGN KEY (fk_locality) REFERENCES public.locality(id_locality) ON UPDATE CASCADE;


--
-- TOC entry 5862 (class 2606 OID 17646)
-- Name: product product_fk_productor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_fk_productor_fkey FOREIGN KEY (fk_productor) REFERENCES public.users(id_user) ON UPDATE CASCADE;


--
-- TOC entry 5863 (class 2606 OID 17656)
-- Name: product product_fk_standar_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_fk_standar_product_fkey FOREIGN KEY (fk_standar_product) REFERENCES public.standar_product(id_standar_product);


--
-- TOC entry 5867 (class 2606 OID 17695)
-- Name: product_history product_history_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5868 (class 2606 OID 17700)
-- Name: product_history product_history_fk_product_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_state_fkey FOREIGN KEY (fk_product_state) REFERENCES public.product_state(id_product_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5880 (class 2606 OID 17812)
-- Name: purchase_detail purchase_detail_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5881 (class 2606 OID 17817)
-- Name: purchase_detail purchase_detail_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5882 (class 2606 OID 17822)
-- Name: purchase_detail purchase_detail_fk_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_state_fkey FOREIGN KEY (fk_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state);


--
-- TOC entry 5883 (class 2606 OID 17834)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_fkey FOREIGN KEY (fk_purchase_detail) REFERENCES public.purchase_detail(id_purchase_detail) ON UPDATE CASCADE;


--
-- TOC entry 5884 (class 2606 OID 17839)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_state_fkey FOREIGN KEY (fk_purchase_detail_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state) ON UPDATE CASCADE;


--
-- TOC entry 5874 (class 2606 OID 17780)
-- Name: purchase purchase_fk_current_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_current_state_fkey FOREIGN KEY (fk_current_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5875 (class 2606 OID 17770)
-- Name: purchase purchase_fk_neighborhood_package_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_neighborhood_package_fkey FOREIGN KEY (fk_neighborhood_package) REFERENCES public.neighborhood_package(id_neighborhood_package) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5876 (class 2606 OID 17775)
-- Name: purchase purchase_fk_payment_method_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_payment_method_fkey FOREIGN KEY (fk_payment_method) REFERENCES public.payment_method(id_payment_method) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5877 (class 2606 OID 17765)
-- Name: purchase purchase_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5878 (class 2606 OID 17797)
-- Name: purchase_state_history purchase_state_history_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE;


--
-- TOC entry 5879 (class 2606 OID 17792)
-- Name: purchase_state_history purchase_state_history_fk_purchase_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_state_fkey FOREIGN KEY (fk_purchase_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5888 (class 2606 OID 17874)
-- Name: sale_detail sale_detail_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_detail
    ADD CONSTRAINT sale_detail_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5889 (class 2606 OID 17879)
-- Name: sale_detail sale_detail_fk_sale_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_detail
    ADD CONSTRAINT sale_detail_fk_sale_fkey FOREIGN KEY (fk_sale) REFERENCES public.sale(id_sale) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5885 (class 2606 OID 17859)
-- Name: sale sale_fk_deliver_guy_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_fk_deliver_guy_fkey FOREIGN KEY (fk_deliver_guy) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5886 (class 2606 OID 17864)
-- Name: sale sale_fk_payment_method_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_fk_payment_method_fkey FOREIGN KEY (fk_payment_method) REFERENCES public.payment_method(id_payment_method) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5887 (class 2606 OID 17854)
-- Name: sale sale_fk_productor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_fk_productor_fkey FOREIGN KEY (fk_productor) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5890 (class 2606 OID 17891)
-- Name: sale_history sale_history_fk_sale_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_history
    ADD CONSTRAINT sale_history_fk_sale_fkey FOREIGN KEY (fk_sale) REFERENCES public.sale(id_sale) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5891 (class 2606 OID 17896)
-- Name: sale_history sale_history_fk_sale_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_history
    ADD CONSTRAINT sale_history_fk_sale_state_fkey FOREIGN KEY (fk_sale_state) REFERENCES public.sale_state(id_sale_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5860 (class 2606 OID 17634)
-- Name: standar_product standar_product_fk_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_fk_category_fkey FOREIGN KEY (fk_category) REFERENCES public.category(id_category);


--
-- TOC entry 5869 (class 2606 OID 17713)
-- Name: stock_movement stock_movement_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5851 (class 2606 OID 17581)
-- Name: users user_fk_collection_point_suscribed_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_collection_point_suscribed_fkey FOREIGN KEY (fk_collection_point_suscribed) REFERENCES public.collection_point(id_collection_point);


--
-- TOC entry 5852 (class 2606 OID 17561)
-- Name: users user_fk_neighborhood_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_neighborhood_fkey FOREIGN KEY (fk_neighborhood) REFERENCES public.neighborhood(id_neighborhood) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5853 (class 2606 OID 17571)
-- Name: users user_fk_rol_two_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_rol_two_fkey FOREIGN KEY (fk_rol_two) REFERENCES public.role(id_role);


--
-- TOC entry 5854 (class 2606 OID 17566)
-- Name: users user_fk_role_one_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_role_one_fkey FOREIGN KEY (fk_role_one) REFERENCES public.role(id_role) ON UPDATE CASCADE;


--
-- TOC entry 5855 (class 2606 OID 17576)
-- Name: users user_fk_role_three_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_role_three_fkey FOREIGN KEY (fk_role_three) REFERENCES public.role(id_role) ON UPDATE CASCADE;


--
-- TOC entry 5892 (class 2606 OID 17913)
-- Name: user_history user_history_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5893 (class 2606 OID 17918)
-- Name: user_history user_history_fk_user_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_state_fkey FOREIGN KEY (fk_user_state) REFERENCES public.user_state(id_user_state);


--
-- TOC entry 5894 (class 2606 OID 17930)
-- Name: vote vote_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product);


--
-- TOC entry 5895 (class 2606 OID 17935)
-- Name: vote vote_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


-- Completed on 2025-02-24 19:07:13

--
-- PostgreSQL database dump complete
--

