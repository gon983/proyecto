--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-04-07 23:01:25

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
-- TOC entry 5965 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 1720 (class 1247 OID 17962)
-- Name: stock_movement_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stock_movement_type AS ENUM (
    'INCREASE',
    'DECREASE'
);


ALTER TYPE public.stock_movement_type OWNER TO postgres;

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
-- TOC entry 225 (class 1259 OID 17484)
-- Name: country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.country (
    id_country uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.country OWNER TO postgres;

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
-- TOC entry 244 (class 1259 OID 18063)
-- Name: locations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.locations (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    address character varying(255) NOT NULL,
    neighborhood_id uuid NOT NULL,
    coordinates public.geometry(Point,4326) NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now()
);


ALTER TABLE public.locations OWNER TO postgres;

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
-- TOC entry 231 (class 1259 OID 17639)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id_product uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(45) NOT NULL,
    stock double precision NOT NULL,
    alert_stock double precision,
    photo character varying(45),
    unit_measurement character varying(100) NOT NULL,
    unity_price double precision NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17688)
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
-- TOC entry 232 (class 1259 OID 17681)
-- Name: product_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_state (
    id_product_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.product_state OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17760)
-- Name: purchase; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase (
    id_purchase uuid DEFAULT gen_random_uuid() NOT NULL,
    fk_user uuid NOT NULL,
    amount double precision,
    fk_neighborhood_package uuid,
    fk_payment_method uuid,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    level character varying(10),
    fk_current_state uuid NOT NULL,
    mp_preference_id character varying,
    mp_payment_id character varying,
    mp_payment_date timestamp with time zone,
    external_reference character varying
);


ALTER TABLE public.purchase OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 17807)
-- Name: purchase_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_detail (
    id_purchase_detail uuid DEFAULT gen_random_uuid() NOT NULL,
    fk_product uuid NOT NULL,
    quantity double precision NOT NULL,
    fk_purchase uuid NOT NULL,
    unit_price double precision NOT NULL,
    fk_state uuid NOT NULL,
    created_by uuid,
    updated_by uuid,
    updated_at timestamp without time zone,
    created_at timestamp without time zone,
    fk_productor uuid,
    fk_collection_point uuid,
    fk_buyer uuid
);


ALTER TABLE public.purchase_detail OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 17802)
-- Name: purchase_detail_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_detail_state (
    id_purchase_detail_state uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.purchase_detail_state OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 17827)
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
-- TOC entry 235 (class 1259 OID 17755)
-- Name: purchase_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_state (
    id_purchase_state uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.purchase_state OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17785)
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
-- TOC entry 230 (class 1259 OID 17629)
-- Name: standar_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.standar_product (
    id_standar_product uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100),
    fk_category uuid NOT NULL
);


ALTER TABLE public.standar_product OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17705)
-- Name: stock_movement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock_movement (
    id_stock_movement uuid DEFAULT gen_random_uuid() NOT NULL,
    fk_product uuid NOT NULL,
    quantity double precision NOT NULL,
    date date NOT NULL,
    comment character varying(1500),
    fk_user uuid NOT NULL,
    type character varying(15),
    CONSTRAINT check_movement_type CHECK (((type)::text = ANY ((ARRAY['INCREASE'::character varying, 'DECREASE'::character varying])::text[])))
);


ALTER TABLE public.stock_movement OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 17906)
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
-- TOC entry 241 (class 1259 OID 17901)
-- Name: user_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_state (
    id_user_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.user_state OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17553)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id_user uuid DEFAULT gen_random_uuid() NOT NULL,
    username character varying(45) NOT NULL,
    email character varying(45) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    phone character varying(45),
    password character varying NOT NULL,
    role character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 17923)
-- Name: vote; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vote (
    id_vote uuid DEFAULT gen_random_uuid() NOT NULL,
    fk_product uuid NOT NULL,
    fk_user uuid NOT NULL,
    date timestamp with time zone NOT NULL,
    comment text,
    fk_default_product_x_collection_point_x_week uuid,
    calification integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.vote OWNER TO postgres;

--
-- TOC entry 5939 (class 0 OID 17479)
-- Dependencies: 224
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.category VALUES ('097ef14d-26c5-4b3a-a113-25e78afb7f69', 'Frutas');
INSERT INTO public.category VALUES ('0dcf68f8-8171-4320-a9ff-8a812ca45bf4', 'Verduras');
INSERT INTO public.category VALUES ('0c8d442f-f5ee-48ab-a999-c6f46e575194', 'Cereales');


--
-- TOC entry 5941 (class 0 OID 17491)
-- Dependencies: 226
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.city VALUES ('602ec196-d4d7-4194-a660-9b4afc9a07ea', 'Cordoba', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('49519714-aad2-4b19-8f6b-bf688e565642', 'CABA', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('29559450-d74c-45a0-83f4-cdfc0a55db7d', 'Rosario', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('a6e9570d-80eb-48c9-a5e1-fe30072c5666', 'Buenos Aires', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('6597656c-f5e7-4e1a-a156-d62cb86f0bd0', 'Mendoza', '6a9fe61b-be17-4821-a4a3-71fa282e9289');


--
-- TOC entry 5940 (class 0 OID 17484)
-- Dependencies: 225
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.country VALUES ('6a9fe61b-be17-4821-a4a3-71fa282e9289', 'Argentina');


--
-- TOC entry 5942 (class 0 OID 17501)
-- Dependencies: 227
-- Data for Name: locality; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locality VALUES ('10fbe8c3-0291-4bc7-a96a-7d10782b36ec', 'Capital', '602ec196-d4d7-4194-a660-9b4afc9a07ea');


--
-- TOC entry 5959 (class 0 OID 18063)
-- Dependencies: 244
-- Data for Name: locations; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5943 (class 0 OID 17511)
-- Dependencies: 228
-- Data for Name: neighborhood; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.neighborhood VALUES ('23f4f7d4-7923-495d-85d0-27f34d932537', 'Barrio Jardin', '10fbe8c3-0291-4bc7-a96a-7d10782b36ec');
INSERT INTO public.neighborhood VALUES ('533306f2-45f9-4a4e-b0da-06d8073220d5', 'Jose Ignacio Diaz', '10fbe8c3-0291-4bc7-a96a-7d10782b36ec');


--
-- TOC entry 5946 (class 0 OID 17639)
-- Dependencies: 231
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.product VALUES ('7f1a0868-3ae1-47f1-921f-9575c82aae2d', 'Harina', 25, 3, '/products/harina.png', 'kg', 2);
INSERT INTO public.product VALUES ('1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'Tomate ', 295.2000000000003, 10, '/products/tomate.png', 'kg', 4);
INSERT INTO public.product VALUES ('eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 'Banana', 81.49999999999994, 10, '/products/banana.png', 'kg', 3);


--
-- TOC entry 5948 (class 0 OID 17688)
-- Dependencies: 233
-- Data for Name: product_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5947 (class 0 OID 17681)
-- Dependencies: 232
-- Data for Name: product_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5951 (class 0 OID 17760)
-- Dependencies: 236
-- Data for Name: purchase; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase VALUES ('77b613f9-1984-4a2a-a0c5-6302167b0841', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-02-24 20:43:16.944691', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('202a0a36-8737-4cca-98ff-3453cc24aa0c', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-18 16:42:27.438789', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-e3aac817-aeb0-4011-b9aa-1c22c2bd82a3', '105621685942', '2025-03-18 18:57:04-03', 'Purchase_202a0a36-8737-4cca-98ff-3453cc24aa0c');
INSERT INTO public.purchase VALUES ('fda6c680-686c-4493-9dc2-14e9dca88b34', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-02-25 18:36:03.004606', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-c4048b7a-099e-4e38-bb47-061427903932', NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('d6810eef-8620-4cd3-ac2a-13015bc209ee', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 18:40:01.772874', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('03e0becd-a27a-4a75-adee-987fd16ac36f', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 17:52:48.014068', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-af339431-a753-42b2-9de8-f42075d9c82c', '105891092431', '2025-03-25 18:15:32-03', 'Purchase_03e0becd-a27a-4a75-adee-987fd16ac36f');
INSERT INTO public.purchase VALUES ('98bb3d82-a8fe-4242-bc76-fff2bd39ef12', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 18:43:33.059594', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-662e0f3e-a533-40b7-af66-87613462703c', '105894322103', '2025-03-25 18:44:28-03', 'Purchase_98bb3d82-a8fe-4242-bc76-fff2bd39ef12');
INSERT INTO public.purchase VALUES ('6e1c1e93-7d1a-471c-bf5c-948d854b5521', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-18 15:20:48.413873', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('fc852775-ccbf-4666-a90b-fbe44828a621', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-20 03:10:50.518492', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-f57a4a76-8823-4ebd-8626-48795cbe29ba', '105399599779', '2025-03-20 15:43:54-03', 'Purchase_fc852775-ccbf-4666-a90b-fbe44828a621');
INSERT INTO public.purchase VALUES ('3492e50b-35f8-4411-8347-d2ba149ceb1a', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-20 21:08:25.53063', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-1a70a168-b55c-4618-a4f6-9e8c8947f4be', NULL, NULL, 'Purchase_3492e50b-35f8-4411-8347-d2ba149ceb1a');
INSERT INTO public.purchase VALUES ('54615ae8-04f0-475f-af83-7132fe04884e', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-13 16:31:05.356159', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-eee80c12-5b5e-4796-bba1-f36c91469a9d', '105604120060', '2025-03-18 16:01:13-03', NULL);
INSERT INTO public.purchase VALUES ('0bad47fe-34e9-43b2-908e-57312a41c82a', '03cbc4c7-0907-4bd2-8adb-b8e138218250', 0, NULL, NULL, '2025-04-04 21:42:42.761095', NULL, NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('ddb7fdf5-8cae-495e-ab95-34bb7c71f2c9', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-20 21:12:46.476103', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-bf17fd66-f7c8-42b5-9c0c-35bb15fd3732', '105436319407', '2025-03-20 21:15:02-03', 'Purchase_ddb7fdf5-8cae-495e-ab95-34bb7c71f2c9');
INSERT INTO public.purchase VALUES ('f150b171-8d46-4204-bdf3-72374f138493', '3bc2cac3-aadc-468c-9942-39fd2b7f2762', 0, NULL, NULL, '2025-04-02 23:05:09.323741', NULL, NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-3201fe9e-d0f0-4649-acd6-04dde7772c1d', NULL, NULL, 'Purchase_f150b171-8d46-4204-bdf3-72374f138493');
INSERT INTO public.purchase VALUES ('73d5012a-f0a2-4b1f-b742-e71d4a54cad4', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-18 15:25:13.561074', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-3a8ee6e1-d739-40fc-85ff-acd95cc9038a', '105604120060', '2025-03-18 16:01:13-03', 'Purchase_73d5012a-f0a2-4b1f-b742-e71d4a54cad4');
INSERT INTO public.purchase VALUES ('96e9c64d-c64f-4a56-883a-4e9c68401032', 'f7358a8c-4650-4531-8a53-f879a646e026', 0, NULL, NULL, '2025-04-07 22:07:44.684897', NULL, NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('91da147d-0e49-47bf-a6b5-a08147b9d2ed', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 18:31:22.948065', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);


--
-- TOC entry 5954 (class 0 OID 17807)
-- Dependencies: 239
-- Data for Name: purchase_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_detail VALUES ('c634fb7d-4b1a-436b-ac9a-5c3a4d36d013', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 2, 'fda6c680-686c-4493-9dc2-14e9dca88b34', 300.7, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('7d38ee00-02ce-4514-b4a2-a677c8062354', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, 'fda6c680-686c-4493-9dc2-14e9dca88b34', 100, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('84badad5-f32f-4a20-83a8-8cf7d105d9cf', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, 'fda6c680-686c-4493-9dc2-14e9dca88b34', 100, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('1d81ce89-c4c3-445a-a358-c799f0e65262', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '54615ae8-04f0-475f-af83-7132fe04884e', 100, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('73f77ba1-aa7b-4add-8e77-ec29ecaa63e7', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '54615ae8-04f0-475f-af83-7132fe04884e', 10, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('6f7d5ba5-1cb3-4454-b807-eba370f649e5', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '73d5012a-f0a2-4b1f-b742-e71d4a54cad4', 10, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('18bb95ed-9571-4021-ac0c-337bb1784877', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '202a0a36-8737-4cca-98ff-3453cc24aa0c', 5, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('fd6ec271-6dca-44e0-93ac-5b6bbd922070', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, 'fc852775-ccbf-4666-a90b-fbe44828a621', 5, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('b0c006c6-9440-4199-99d5-84c8432eb68c', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1.9, '3492e50b-35f8-4411-8347-d2ba149ceb1a', 300, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('099d284f-df06-4dcd-902d-487ea46a6209', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1.9, 'ddb7fdf5-8cae-495e-ab95-34bb7c71f2c9', 3, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('1348da68-d4c1-4d20-8b3b-ad8967e5a3da', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, '91da147d-0e49-47bf-a6b5-a08147b9d2ed', 8, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, 'c446f03c-490c-4fe9-a63a-ad407c981c29', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe');
INSERT INTO public.purchase_detail VALUES ('18538720-11d4-44de-82a5-1dd5c96062ea', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, 'd6810eef-8620-4cd3-ac2a-13015bc209ee', 8, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, 'c446f03c-490c-4fe9-a63a-ad407c981c29', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe');
INSERT INTO public.purchase_detail VALUES ('16d1c22b-c306-406d-b28b-7e212ed87c94', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, '98bb3d82-a8fe-4242-bc76-fff2bd39ef12', 8, '5d3616de-9963-4413-a42c-65a73ded54da', NULL, NULL, NULL, NULL, 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'c446f03c-490c-4fe9-a63a-ad407c981c29', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe');
INSERT INTO public.purchase_detail VALUES ('8266bc65-929b-418b-9562-dad1c19a871c', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1.9, '03e0becd-a27a-4a75-adee-987fd16ac36f', 3, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('228f28d6-c2ae-4641-8b4b-9bfd9b6baaa2', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, '03e0becd-a27a-4a75-adee-987fd16ac36f', 8, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('e4c5dc2c-b8fa-4625-9ef1-c889355bf0d7', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '96e9c64d-c64f-4a56-883a-4e9c68401032', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('df12ae6d-6798-4395-8f06-4f99941f5124', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '96e9c64d-c64f-4a56-883a-4e9c68401032', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('9d9b1c44-15e7-4ee2-a3f5-29fcca44a4bb', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 3, 'f150b171-8d46-4204-bdf3-72374f138493', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '3bc2cac3-aadc-468c-9942-39fd2b7f2762');
INSERT INTO public.purchase_detail VALUES ('81a24f33-1cf7-4fc9-b032-f5145a6b9568', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '96e9c64d-c64f-4a56-883a-4e9c68401032', 3, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');


--
-- TOC entry 5953 (class 0 OID 17802)
-- Dependencies: 238
-- Data for Name: purchase_detail_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_detail_state VALUES ('8a69aecf-7441-4e57-bbea-e91cbbfc7487', 'pending');
INSERT INTO public.purchase_detail_state VALUES ('ad842904-786c-4dc6-8620-378ebf85e7c0', 'received');
INSERT INTO public.purchase_detail_state VALUES ('394cb175-ccaa-49d1-a303-71d2456ee695', 'not received');
INSERT INTO public.purchase_detail_state VALUES ('3f190193-de36-4904-ba03-db301f27083b', 'confirmed');
INSERT INTO public.purchase_detail_state VALUES ('5d3616de-9963-4413-a42c-65a73ded54da', 'payed');


--
-- TOC entry 5955 (class 0 OID 17827)
-- Dependencies: 240
-- Data for Name: purchase_detail_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5950 (class 0 OID 17755)
-- Dependencies: 235
-- Data for Name: purchase_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_state VALUES ('11c4c9f4-d5ee-4866-a22e-e1eb824efafa', 'pending');
INSERT INTO public.purchase_state VALUES ('e4aa98f7-2500-44ef-a05b-e44ee3e068ff', 'confirmed');
INSERT INTO public.purchase_state VALUES ('12d76cf7-1a2d-46bd-a43d-30e0828f8250', 'received');
INSERT INTO public.purchase_state VALUES ('739d459e-816b-49ea-acf2-3b20b6e85ef1', 'not received');
INSERT INTO public.purchase_state VALUES ('c0bdc407-43e5-4588-9236-f8db23949ad7', 'payed');


--
-- TOC entry 5952 (class 0 OID 17785)
-- Dependencies: 237
-- Data for Name: purchase_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5699 (class 0 OID 16721)
-- Dependencies: 220
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5945 (class 0 OID 17629)
-- Dependencies: 230
-- Data for Name: standar_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.standar_product VALUES ('6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb', 'Banana', '097ef14d-26c5-4b3a-a113-25e78afb7f69');
INSERT INTO public.standar_product VALUES ('a634e60f-7070-4f1b-bc22-9926952b5fff', 'Tomate', '0dcf68f8-8171-4320-a9ff-8a812ca45bf4');
INSERT INTO public.standar_product VALUES ('cbb6f2b1-3c90-4616-b329-966442c9e8fa', 'Harina', '0c8d442f-f5ee-48ab-a999-c6f46e575194');


--
-- TOC entry 5949 (class 0 OID 17705)
-- Dependencies: 234
-- Data for Name: stock_movement; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.stock_movement VALUES ('d4fc9939-da45-48b4-ab69-84c29ba5eb5f', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('4bab9a43-9173-490b-b058-57e9918c5c44', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('d7f1436c-0e20-4cf2-af57-dc12da66ba94', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('eb4e3a53-88e2-4d37-aff7-6a99aadffec6', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('7a59ee1e-8528-4f15-bc05-b6aad7815ef4', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('d9007d38-2b44-4076-8044-9d71736dc523', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 100, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('ffd95857-0bf5-4cad-8e8a-4821f63a1c96', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 100, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('33465dc5-ac54-43be-abf7-a7710b1bf537', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 100, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('15b965fb-c4d8-48df-bb9b-19312c6bcaef', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 100, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('38bed582-7c3b-4ca4-9947-7051b943278b', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 100, '2025-02-26', 'sin comentarios', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 'INCREASE');
INSERT INTO public.stock_movement VALUES ('b72c98af-53ff-4b38-8de6-4bccb27ad620', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1.9, '2025-03-20', '', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('4d51f96c-2e2d-42bc-963b-61ee132f0fc0', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, '2025-03-25', '', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 'DECREASE');


--
-- TOC entry 5957 (class 0 OID 17906)
-- Dependencies: 242
-- Data for Name: user_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5956 (class 0 OID 17901)
-- Dependencies: 241
-- Data for Name: user_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5944 (class 0 OID 17553)
-- Dependencies: 229
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('3bc2cac3-aadc-468c-9942-39fd2b7f2762', 'gondutari', 'gondutarigd@gmail.com', '2025-02-26 10:38:59.743479', '+549112233334', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_ADMIN');
INSERT INTO public.users VALUES ('bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 'rosa', 'seniorarosa@gmail.com', '2025-02-24 19:59:42.157521', '+549112233464', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_USER');
INSERT INTO public.users VALUES ('b088c879-3fe7-4bcd-a6fc-89efb341c602', 'produ', 'juan.perez@example.com', '2025-02-22 16:03:26.961483', '+5491122334455', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_PRODUCTOR');
INSERT INTO public.users VALUES ('6982b8a2-2a9e-47a4-8df9-85f3482b16e2', 'cpown', 'rodrigo@example.com', '2025-02-22 16:12:39.413808', '+5491129898', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_CP_OWNER');
INSERT INTO public.users VALUES ('e4a56db2-0dbf-4896-b27d-5fb676d7fceb', 'usu', 'usuario@example.com', '2025-04-03 12:21:48.030977', NULL, '123', 'ROLE_USER');
INSERT INTO public.users VALUES ('5c938119-df44-43d8-93c2-fe51586a5f60', 'alguien', 'alguien@example.com', '2025-04-04 00:52:15.687755', NULL, '123456', 'ROLE_USER');
INSERT INTO public.users VALUES ('f7358a8c-4650-4531-8a53-f879a646e026', 'a', 'a@example.com', '2025-04-04 11:42:14.346196', NULL, '123456', 'ROLE_USER');
INSERT INTO public.users VALUES ('cba4c14e-b516-465d-93cb-4ce0b6486d0f', 'queee', 'quee@example.com', '2025-04-04 21:26:46.920779', NULL, '123456', 'ROLE_USER');
INSERT INTO public.users VALUES ('03cbc4c7-0907-4bd2-8adb-b8e138218250', 'gon_admin', 'gonzalo_dutari@outlook.com', '2025-04-04 21:42:30.500966', NULL, 'alveurbp91$nmqt', 'ROLE_USER');
INSERT INTO public.users VALUES ('9291aad2-6e6c-4a08-b135-91bafe00fa80', 'sadsa', 'LKSJALK@fssf', '2025-04-04 21:45:39.208589', NULL, '123456', 'ROLE_USER');
INSERT INTO public.users VALUES ('6dfcc17a-16d3-41c5-9098-1dc50fb209ef', 'jejje', 'gonzalito@jejej', '2025-04-04 21:48:12.404082', NULL, '123456', 'ROLE_USER');


--
-- TOC entry 5958 (class 0 OID 17923)
-- Dependencies: 243
-- Data for Name: vote; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.vote VALUES ('f7a8c1ea-bc72-4d2f-8b91-14ac2c7986e2', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', '2025-03-13 10:14:19.008879-03', 'muy buen producto', '4d0e7a7f-7569-4fad-b107-6e87411b6b23', 0);
INSERT INTO public.vote VALUES ('4bcb36a9-aafb-4ad7-9583-121d226157f0', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', '6982b8a2-2a9e-47a4-8df9-85f3482b16e2', '2025-03-23 18:19:33.338329-03', 'muy buen producto', 'd9e4a5d4-3ec1-4b8e-9795-5ed7eba30291', 5);


--
-- TOC entry 5717 (class 2606 OID 17483)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id_category);


--
-- TOC entry 5723 (class 2606 OID 17495)
-- Name: city city_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id_city);


--
-- TOC entry 5719 (class 2606 OID 17490)
-- Name: country country_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_name_key UNIQUE (name);


--
-- TOC entry 5721 (class 2606 OID 17488)
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id_country);


--
-- TOC entry 5725 (class 2606 OID 17505)
-- Name: locality locality_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_pkey PRIMARY KEY (id_locality);


--
-- TOC entry 5763 (class 2606 OID 18072)
-- Name: locations locations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_pkey PRIMARY KEY (id);


--
-- TOC entry 5727 (class 2606 OID 17515)
-- Name: neighborhood neighborhood_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_pkey PRIMARY KEY (id_neighborhood);


--
-- TOC entry 5741 (class 2606 OID 17694)
-- Name: product_history product_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_pkey PRIMARY KEY (id_product_history);


--
-- TOC entry 5733 (class 2606 OID 17645)
-- Name: product product_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_name_key UNIQUE (name);


--
-- TOC entry 5735 (class 2606 OID 17643)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id_product);


--
-- TOC entry 5737 (class 2606 OID 17687)
-- Name: product_state product_state_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_name_key UNIQUE (name);


--
-- TOC entry 5739 (class 2606 OID 17685)
-- Name: product_state product_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_pkey PRIMARY KEY (id_product_state);


--
-- TOC entry 5753 (class 2606 OID 17811)
-- Name: purchase_detail purchase_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_pkey PRIMARY KEY (id_purchase_detail);


--
-- TOC entry 5755 (class 2606 OID 17833)
-- Name: purchase_detail_state_history purchase_detail_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_pkey PRIMARY KEY (id_purchase_detail_state_history);


--
-- TOC entry 5751 (class 2606 OID 17806)
-- Name: purchase_detail_state purchase_detail_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state
    ADD CONSTRAINT purchase_detail_state_pkey PRIMARY KEY (id_purchase_detail_state);


--
-- TOC entry 5747 (class 2606 OID 17764)
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (id_purchase);


--
-- TOC entry 5749 (class 2606 OID 17791)
-- Name: purchase_state_history purchase_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_pkey PRIMARY KEY (id_purchase_state_history);


--
-- TOC entry 5745 (class 2606 OID 17759)
-- Name: purchase_state purchase_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state
    ADD CONSTRAINT purchase_state_pkey PRIMARY KEY (id_purchase_state);


--
-- TOC entry 5731 (class 2606 OID 17633)
-- Name: standar_product standar_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_pkey PRIMARY KEY (id_standar_product);


--
-- TOC entry 5743 (class 2606 OID 17712)
-- Name: stock_movement stock_movement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_pkey PRIMARY KEY (id_stock_movement);


--
-- TOC entry 5759 (class 2606 OID 17912)
-- Name: user_history user_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_pkey PRIMARY KEY (id_user_history);


--
-- TOC entry 5729 (class 2606 OID 17558)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id_user);


--
-- TOC entry 5757 (class 2606 OID 17905)
-- Name: user_state user_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_state
    ADD CONSTRAINT user_state_pkey PRIMARY KEY (id_user_state);


--
-- TOC entry 5761 (class 2606 OID 17999)
-- Name: vote vote_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_pkey PRIMARY KEY (id_vote);


--
-- TOC entry 5764 (class 2606 OID 17496)
-- Name: city city_fk_country_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_fk_country_fkey FOREIGN KEY (fk_country) REFERENCES public.country(id_country) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5765 (class 2606 OID 17506)
-- Name: locality locality_fk_city_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_fk_city_fkey FOREIGN KEY (fk_city) REFERENCES public.city(id_city) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5787 (class 2606 OID 18078)
-- Name: locations locations_neighborhood_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_neighborhood_id_fkey FOREIGN KEY (neighborhood_id) REFERENCES public.neighborhood(id_neighborhood);


--
-- TOC entry 5788 (class 2606 OID 18073)
-- Name: locations locations_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id_user) ON DELETE CASCADE;


--
-- TOC entry 5766 (class 2606 OID 17516)
-- Name: neighborhood neighborhood_fk_locality_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_fk_locality_fkey FOREIGN KEY (fk_locality) REFERENCES public.locality(id_locality) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5768 (class 2606 OID 17695)
-- Name: product_history product_history_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5769 (class 2606 OID 17700)
-- Name: product_history product_history_fk_product_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_state_fkey FOREIGN KEY (fk_product_state) REFERENCES public.product_state(id_product_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5776 (class 2606 OID 17812)
-- Name: purchase_detail purchase_detail_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5777 (class 2606 OID 17817)
-- Name: purchase_detail purchase_detail_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5778 (class 2606 OID 17822)
-- Name: purchase_detail purchase_detail_fk_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_state_fkey FOREIGN KEY (fk_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state);


--
-- TOC entry 5781 (class 2606 OID 17834)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_fkey FOREIGN KEY (fk_purchase_detail) REFERENCES public.purchase_detail(id_purchase_detail) ON UPDATE CASCADE;


--
-- TOC entry 5782 (class 2606 OID 17839)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_state_fkey FOREIGN KEY (fk_purchase_detail_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state) ON UPDATE CASCADE;


--
-- TOC entry 5779 (class 2606 OID 18052)
-- Name: purchase_detail purchase_detail_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_users_fk FOREIGN KEY (fk_productor) REFERENCES public.users(id_user);


--
-- TOC entry 5780 (class 2606 OID 18057)
-- Name: purchase_detail purchase_detail_users_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_users_fk_1 FOREIGN KEY (fk_buyer) REFERENCES public.users(id_user);


--
-- TOC entry 5772 (class 2606 OID 17780)
-- Name: purchase purchase_fk_current_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_current_state_fkey FOREIGN KEY (fk_current_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5773 (class 2606 OID 17765)
-- Name: purchase purchase_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5774 (class 2606 OID 17797)
-- Name: purchase_state_history purchase_state_history_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE;


--
-- TOC entry 5775 (class 2606 OID 17792)
-- Name: purchase_state_history purchase_state_history_fk_purchase_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_state_fkey FOREIGN KEY (fk_purchase_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5767 (class 2606 OID 17634)
-- Name: standar_product standar_product_fk_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_fk_category_fkey FOREIGN KEY (fk_category) REFERENCES public.category(id_category);


--
-- TOC entry 5770 (class 2606 OID 17713)
-- Name: stock_movement stock_movement_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5771 (class 2606 OID 17955)
-- Name: stock_movement stock_movement_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_users_fk FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5783 (class 2606 OID 17913)
-- Name: user_history user_history_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5784 (class 2606 OID 17918)
-- Name: user_history user_history_fk_user_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_state_fkey FOREIGN KEY (fk_user_state) REFERENCES public.user_state(id_user_state);


--
-- TOC entry 5785 (class 2606 OID 17930)
-- Name: vote vote_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product);


--
-- TOC entry 5786 (class 2606 OID 17935)
-- Name: vote vote_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


-- Completed on 2025-04-07 23:01:25

--
-- PostgreSQL database dump complete
--

