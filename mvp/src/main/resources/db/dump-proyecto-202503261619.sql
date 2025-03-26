--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-03-26 16:19:51

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
-- TOC entry 6016 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 1743 (class 1247 OID 17962)
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
-- TOC entry 231 (class 1259 OID 17541)
-- Name: collection_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.collection_point (
    id_collection_point uuid NOT NULL,
    name character varying(45) NOT NULL,
    fk_neighborhood uuid NOT NULL,
    use_price double precision,
    fk_owner uuid NOT NULL,
    description character varying(1500),
    ubication public.geometry(Point,4326),
    collection_recurrent_day integer
);


ALTER TABLE public.collection_point OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17591)
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
-- TOC entry 230 (class 1259 OID 17536)
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
-- TOC entry 234 (class 1259 OID 17618)
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
-- TOC entry 237 (class 1259 OID 17661)
-- Name: default_product_x_collection_point_x_week; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.default_product_x_collection_point_x_week (
    id_default_product_x_collection_point uuid DEFAULT gen_random_uuid() NOT NULL,
    fk_collection_point uuid NOT NULL,
    fk_product uuid,
    fk_standar_product uuid,
    date_renewal_default_products timestamp with time zone,
    rating double precision,
    n_votes integer DEFAULT 1 NOT NULL
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
-- TOC entry 229 (class 1259 OID 17526)
-- Name: ong; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ong (
    id_ong uuid NOT NULL,
    name character varying(100) NOT NULL,
    account character varying(100) NOT NULL
);


ALTER TABLE public.ong OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17639)
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
-- TOC entry 239 (class 1259 OID 17688)
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
-- TOC entry 238 (class 1259 OID 17681)
-- Name: product_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_state (
    id_product_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.product_state OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 17760)
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
-- TOC entry 245 (class 1259 OID 17807)
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
-- TOC entry 244 (class 1259 OID 17802)
-- Name: purchase_detail_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_detail_state (
    id_purchase_detail_state uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.purchase_detail_state OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 17827)
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
-- TOC entry 241 (class 1259 OID 17755)
-- Name: purchase_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase_state (
    id_purchase_state uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.purchase_state OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 17785)
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
-- TOC entry 235 (class 1259 OID 17629)
-- Name: standar_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.standar_product (
    id_standar_product uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100),
    fk_category uuid NOT NULL
);


ALTER TABLE public.standar_product OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 17705)
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
-- TOC entry 248 (class 1259 OID 17906)
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
-- TOC entry 247 (class 1259 OID 17901)
-- Name: user_state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_state (
    id_user_state uuid NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE public.user_state OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17553)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id_user uuid DEFAULT gen_random_uuid() NOT NULL,
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
    fk_collection_point_suscribed uuid,
    level integer DEFAULT 1 NOT NULL,
    mp_public_key_encrypted character varying,
    mp_access_token_encrypted character varying,
    access_token_productor character varying,
    user_productor_mp_id character varying,
    refresh_productor_token character varying,
    password character varying,
    role character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 17923)
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
-- TOC entry 5985 (class 0 OID 17479)
-- Dependencies: 224
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.category VALUES ('097ef14d-26c5-4b3a-a113-25e78afb7f69', 'Frutas');
INSERT INTO public.category VALUES ('0dcf68f8-8171-4320-a9ff-8a812ca45bf4', 'Verduras');
INSERT INTO public.category VALUES ('0c8d442f-f5ee-48ab-a999-c6f46e575194', 'Cereales');


--
-- TOC entry 5987 (class 0 OID 17491)
-- Dependencies: 226
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.city VALUES ('602ec196-d4d7-4194-a660-9b4afc9a07ea', 'Cordoba', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('49519714-aad2-4b19-8f6b-bf688e565642', 'CABA', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('29559450-d74c-45a0-83f4-cdfc0a55db7d', 'Rosario', '6a9fe61b-be17-4821-a4a3-71fa282e9289');


--
-- TOC entry 5992 (class 0 OID 17541)
-- Dependencies: 231
-- Data for Name: collection_point; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.collection_point VALUES ('c446f03c-490c-4fe9-a63a-ad407c981c29', 'Club Social BJ', '23f4f7d4-7923-495d-85d0-27f34d932537', 99.99, '6982b8a2-2a9e-47a4-8df9-85f3482b16e2', 'buen lugar', '0101000020E61000003563D17476723FC01AA88C7F9F0B50C0', 1);


--
-- TOC entry 5994 (class 0 OID 17591)
-- Dependencies: 233
-- Data for Name: collection_point_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5991 (class 0 OID 17536)
-- Dependencies: 230
-- Data for Name: collection_point_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.collection_point_state VALUES ('eb9caee8-dc88-4bcb-a804-3ab6fa0984a1', 'pending');
INSERT INTO public.collection_point_state VALUES ('19a226ee-c0e4-4d78-9ddd-dc4d68a27aaf', 'suspended');


--
-- TOC entry 5986 (class 0 OID 17484)
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
-- TOC entry 5995 (class 0 OID 17618)
-- Dependencies: 234
-- Data for Name: default_donation; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5998 (class 0 OID 17661)
-- Dependencies: 237
-- Data for Name: default_product_x_collection_point_x_week; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.default_product_x_collection_point_x_week VALUES ('07635012-d0fb-4fb4-806b-d7c9caf0b9d1', 'c446f03c-490c-4fe9-a63a-ad407c981c29', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'a634e60f-7070-4f1b-bc22-9926952b5fff', '2025-03-05 16:06:31.79557-03', 4, 1);
INSERT INTO public.default_product_x_collection_point_x_week VALUES ('c6f69136-48e5-4927-85c5-073d57358f7e', 'c446f03c-490c-4fe9-a63a-ad407c981c29', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'a634e60f-7070-4f1b-bc22-9926952b5fff', '2025-03-23 14:58:05.604297-03', 0, 1);
INSERT INTO public.default_product_x_collection_point_x_week VALUES ('4d0e7a7f-7569-4fad-b107-6e87411b6b23', 'c446f03c-490c-4fe9-a63a-ad407c981c29', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', '6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb', '2025-03-23 14:58:43.270079-03', 0, 1);
INSERT INTO public.default_product_x_collection_point_x_week VALUES ('d9e4a5d4-3ec1-4b8e-9795-5ed7eba30291', 'c446f03c-490c-4fe9-a63a-ad407c981c29', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', '6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb', '2025-03-23 15:36:26.368993-03', 3.5, 2);


--
-- TOC entry 5988 (class 0 OID 17501)
-- Dependencies: 227
-- Data for Name: locality; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locality VALUES ('10fbe8c3-0291-4bc7-a96a-7d10782b36ec', 'Capital', '602ec196-d4d7-4194-a660-9b4afc9a07ea');


--
-- TOC entry 5989 (class 0 OID 17511)
-- Dependencies: 228
-- Data for Name: neighborhood; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.neighborhood VALUES ('23f4f7d4-7923-495d-85d0-27f34d932537', 'Barrio Jardin', '10fbe8c3-0291-4bc7-a96a-7d10782b36ec');


--
-- TOC entry 5990 (class 0 OID 17526)
-- Dependencies: 229
-- Data for Name: ong; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5997 (class 0 OID 17639)
-- Dependencies: 236
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.product VALUES ('7f1a0868-3ae1-47f1-921f-9575c82aae2d', 'Harina', 25, 3, 'https://example.com/producto.jpg', 'kg', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 2, '10fbe8c3-0291-4bc7-a96a-7d10782b36ec', 'cbb6f2b1-3c90-4616-b329-966442c9e8fa');
INSERT INTO public.product VALUES ('1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'Tomate 1', 295.2000000000003, 10, 'https://example.com/producto.jpg', 'kg', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 25.99, '10fbe8c3-0291-4bc7-a96a-7d10782b36ec', 'a634e60f-7070-4f1b-bc22-9926952b5fff');
INSERT INTO public.product VALUES ('eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 'Banana 1', 81.49999999999994, 10, 'https://example.com/producto.jpg', 'kg', 'b088c879-3fe7-4bcd-a6fc-89efb341c602', 25.99, '10fbe8c3-0291-4bc7-a96a-7d10782b36ec', '6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb');


--
-- TOC entry 6000 (class 0 OID 17688)
-- Dependencies: 239
-- Data for Name: product_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5999 (class 0 OID 17681)
-- Dependencies: 238
-- Data for Name: product_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6003 (class 0 OID 17760)
-- Dependencies: 242
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
INSERT INTO public.purchase VALUES ('ddb7fdf5-8cae-495e-ab95-34bb7c71f2c9', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-20 21:12:46.476103', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-bf17fd66-f7c8-42b5-9c0c-35bb15fd3732', '105436319407', '2025-03-20 21:15:02-03', 'Purchase_ddb7fdf5-8cae-495e-ab95-34bb7c71f2c9');
INSERT INTO public.purchase VALUES ('73d5012a-f0a2-4b1f-b742-e71d4a54cad4', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-18 15:25:13.561074', NULL, '1', 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-3a8ee6e1-d739-40fc-85ff-acd95cc9038a', '105604120060', '2025-03-18 16:01:13-03', 'Purchase_73d5012a-f0a2-4b1f-b742-e71d4a54cad4');
INSERT INTO public.purchase VALUES ('91da147d-0e49-47bf-a6b5-a08147b9d2ed', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 18:31:22.948065', NULL, '1', '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL);


--
-- TOC entry 6006 (class 0 OID 17807)
-- Dependencies: 245
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


--
-- TOC entry 6005 (class 0 OID 17802)
-- Dependencies: 244
-- Data for Name: purchase_detail_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_detail_state VALUES ('8a69aecf-7441-4e57-bbea-e91cbbfc7487', 'pending');
INSERT INTO public.purchase_detail_state VALUES ('ad842904-786c-4dc6-8620-378ebf85e7c0', 'received');
INSERT INTO public.purchase_detail_state VALUES ('394cb175-ccaa-49d1-a303-71d2456ee695', 'not received');
INSERT INTO public.purchase_detail_state VALUES ('3f190193-de36-4904-ba03-db301f27083b', 'confirmed');
INSERT INTO public.purchase_detail_state VALUES ('5d3616de-9963-4413-a42c-65a73ded54da', 'payed');


--
-- TOC entry 6007 (class 0 OID 17827)
-- Dependencies: 246
-- Data for Name: purchase_detail_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6002 (class 0 OID 17755)
-- Dependencies: 241
-- Data for Name: purchase_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_state VALUES ('11c4c9f4-d5ee-4866-a22e-e1eb824efafa', 'pending');
INSERT INTO public.purchase_state VALUES ('e4aa98f7-2500-44ef-a05b-e44ee3e068ff', 'confirmed');
INSERT INTO public.purchase_state VALUES ('12d76cf7-1a2d-46bd-a43d-30e0828f8250', 'received');
INSERT INTO public.purchase_state VALUES ('739d459e-816b-49ea-acf2-3b20b6e85ef1', 'not received');
INSERT INTO public.purchase_state VALUES ('c0bdc407-43e5-4588-9236-f8db23949ad7', 'payed');


--
-- TOC entry 6004 (class 0 OID 17785)
-- Dependencies: 243
-- Data for Name: purchase_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5719 (class 0 OID 16721)
-- Dependencies: 220
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5996 (class 0 OID 17629)
-- Dependencies: 235
-- Data for Name: standar_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.standar_product VALUES ('6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb', 'Banana', '097ef14d-26c5-4b3a-a113-25e78afb7f69');
INSERT INTO public.standar_product VALUES ('a634e60f-7070-4f1b-bc22-9926952b5fff', 'Tomate', '0dcf68f8-8171-4320-a9ff-8a812ca45bf4');
INSERT INTO public.standar_product VALUES ('cbb6f2b1-3c90-4616-b329-966442c9e8fa', 'Harina', '0c8d442f-f5ee-48ab-a999-c6f46e575194');


--
-- TOC entry 6001 (class 0 OID 17705)
-- Dependencies: 240
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
-- TOC entry 6009 (class 0 OID 17906)
-- Dependencies: 248
-- Data for Name: user_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6008 (class 0 OID 17901)
-- Dependencies: 247
-- Data for Name: user_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5993 (class 0 OID 17553)
-- Dependencies: 232
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('6982b8a2-2a9e-47a4-8df9-85f3482b16e2', 'rodrigo', 'rodrigo@example.com', '2025-02-22 16:12:39.413808', 'Rodrigo', 'Dueño Plaza', 'DNI', '88888888', '23f4f7d4-7923-495d-85d0-27f34d932537', '+5491129898', NULL, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL, '123', 'CP_OWNER');
INSERT INTO public.users VALUES ('3bc2cac3-aadc-468c-9942-39fd2b7f2762', 'gondutari', 'gondutarigd@gmail.com', '2025-02-26 10:38:59.743479', 'Gonzalo', 'Dutari', 'DNI', '12315558', '23f4f7d4-7923-495d-85d0-27f34d932537', '+549112233334', NULL, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL, '123', 'ADMIN');
INSERT INTO public.users VALUES ('bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 'grego', 'gregorio.perez@example.com', '2025-02-24 19:59:42.157521', 'Gregorio', 'Consumidor', 'DNI', '12345558', '23f4f7d4-7923-495d-85d0-27f34d932537', '+549112233464', NULL, NULL, 'c446f03c-490c-4fe9-a63a-ad407c981c29', 1, '', '', NULL, NULL, NULL, '123', 'CONSUMIDOR');
INSERT INTO public.users VALUES ('b088c879-3fe7-4bcd-a6fc-89efb341c602', 'juanperez', 'juan.perez@example.com', '2025-02-22 16:03:26.961483', 'Juan', 'ProductorDeTomates', 'DNI', '12345678', '23f4f7d4-7923-495d-85d0-27f34d932537', '+5491122334455', NULL, NULL, NULL, 0, 'ddefad0c7ca65411b7d7acd510235c326969aad4f7cfca4298df97b002ed0bff0dd63ef79351b02b7e04f0a17fad9302c14660f4be2eb6118de11f2da4959e1b', 'f598461f073ad4789aaf4fd01f5ef29ee46d1565380735660fa5ce43d32284ab14c141f7920c4787d97a5a306e423a77e2d01aac79cd0b5f4c1f36387db56273d3ef505da5725ead050f0ede3da116c087040ad828f0b31188d0f161f1767f06', 'APP_USR-2552125444382264-032014-7bfa4962c5495e6d55c451079266ce04-2338630964', '2338630964', 'TG-67dc5e9e332fbb0001c75856-2338630964', '123', 'PRODUCTOR');


--
-- TOC entry 6010 (class 0 OID 17923)
-- Dependencies: 249
-- Data for Name: vote; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.vote VALUES ('f7a8c1ea-bc72-4d2f-8b91-14ac2c7986e2', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', '2025-03-13 10:14:19.008879-03', 'muy buen producto', '4d0e7a7f-7569-4fad-b107-6e87411b6b23', 0);
INSERT INTO public.vote VALUES ('4bcb36a9-aafb-4ad7-9583-121d226157f0', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', '6982b8a2-2a9e-47a4-8df9-85f3482b16e2', '2025-03-23 18:19:33.338329-03', 'muy buen producto', 'd9e4a5d4-3ec1-4b8e-9795-5ed7eba30291', 5);


--
-- TOC entry 5739 (class 2606 OID 17483)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id_category);


--
-- TOC entry 5745 (class 2606 OID 17495)
-- Name: city city_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id_city);


--
-- TOC entry 5759 (class 2606 OID 17597)
-- Name: collection_point_history collection_point_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_history
    ADD CONSTRAINT collection_point_history_pkey PRIMARY KEY (id_collection_point_history);


--
-- TOC entry 5755 (class 2606 OID 17547)
-- Name: collection_point collection_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point
    ADD CONSTRAINT collection_point_pkey PRIMARY KEY (id_collection_point);


--
-- TOC entry 5753 (class 2606 OID 17540)
-- Name: collection_point_state collection_point_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_state
    ADD CONSTRAINT collection_point_state_pkey PRIMARY KEY (id_collection_point_state);


--
-- TOC entry 5741 (class 2606 OID 17490)
-- Name: country country_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_name_key UNIQUE (name);


--
-- TOC entry 5743 (class 2606 OID 17488)
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id_country);


--
-- TOC entry 5761 (class 2606 OID 17623)
-- Name: default_donation default_donation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_donation
    ADD CONSTRAINT default_donation_pkey PRIMARY KEY (id_default_donation);


--
-- TOC entry 5769 (class 2606 OID 17665)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_week_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_week_pkey PRIMARY KEY (id_default_product_x_collection_point);


--
-- TOC entry 5747 (class 2606 OID 17505)
-- Name: locality locality_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_pkey PRIMARY KEY (id_locality);


--
-- TOC entry 5749 (class 2606 OID 17515)
-- Name: neighborhood neighborhood_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_pkey PRIMARY KEY (id_neighborhood);


--
-- TOC entry 5751 (class 2606 OID 17530)
-- Name: ong ong_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ong
    ADD CONSTRAINT ong_pkey PRIMARY KEY (id_ong);


--
-- TOC entry 5775 (class 2606 OID 17694)
-- Name: product_history product_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_pkey PRIMARY KEY (id_product_history);


--
-- TOC entry 5765 (class 2606 OID 17645)
-- Name: product product_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_name_key UNIQUE (name);


--
-- TOC entry 5767 (class 2606 OID 17643)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id_product);


--
-- TOC entry 5771 (class 2606 OID 17687)
-- Name: product_state product_state_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_name_key UNIQUE (name);


--
-- TOC entry 5773 (class 2606 OID 17685)
-- Name: product_state product_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_pkey PRIMARY KEY (id_product_state);


--
-- TOC entry 5787 (class 2606 OID 17811)
-- Name: purchase_detail purchase_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_pkey PRIMARY KEY (id_purchase_detail);


--
-- TOC entry 5789 (class 2606 OID 17833)
-- Name: purchase_detail_state_history purchase_detail_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_pkey PRIMARY KEY (id_purchase_detail_state_history);


--
-- TOC entry 5785 (class 2606 OID 17806)
-- Name: purchase_detail_state purchase_detail_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state
    ADD CONSTRAINT purchase_detail_state_pkey PRIMARY KEY (id_purchase_detail_state);


--
-- TOC entry 5781 (class 2606 OID 17764)
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (id_purchase);


--
-- TOC entry 5783 (class 2606 OID 17791)
-- Name: purchase_state_history purchase_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_pkey PRIMARY KEY (id_purchase_state_history);


--
-- TOC entry 5779 (class 2606 OID 17759)
-- Name: purchase_state purchase_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state
    ADD CONSTRAINT purchase_state_pkey PRIMARY KEY (id_purchase_state);


--
-- TOC entry 5763 (class 2606 OID 17633)
-- Name: standar_product standar_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_pkey PRIMARY KEY (id_standar_product);


--
-- TOC entry 5777 (class 2606 OID 17712)
-- Name: stock_movement stock_movement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_pkey PRIMARY KEY (id_stock_movement);


--
-- TOC entry 5793 (class 2606 OID 17912)
-- Name: user_history user_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_pkey PRIMARY KEY (id_user_history);


--
-- TOC entry 5757 (class 2606 OID 17558)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id_user);


--
-- TOC entry 5791 (class 2606 OID 17905)
-- Name: user_state user_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_state
    ADD CONSTRAINT user_state_pkey PRIMARY KEY (id_user_state);


--
-- TOC entry 5795 (class 2606 OID 17999)
-- Name: vote vote_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_pkey PRIMARY KEY (id_vote);


--
-- TOC entry 5796 (class 2606 OID 17496)
-- Name: city city_fk_country_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_fk_country_fkey FOREIGN KEY (fk_country) REFERENCES public.country(id_country) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5799 (class 2606 OID 17548)
-- Name: collection_point collection_point_fk_neighborhood_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point
    ADD CONSTRAINT collection_point_fk_neighborhood_fkey FOREIGN KEY (fk_neighborhood) REFERENCES public.neighborhood(id_neighborhood) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5803 (class 2606 OID 17598)
-- Name: collection_point_history collection_point_history_fk_collection_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_history
    ADD CONSTRAINT collection_point_history_fk_collection_point_fkey FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5804 (class 2606 OID 17603)
-- Name: collection_point_history collection_point_history_fk_collection_point_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point_history
    ADD CONSTRAINT collection_point_history_fk_collection_point_state_fkey FOREIGN KEY (fk_collection_point_state) REFERENCES public.collection_point_state(id_collection_point_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5805 (class 2606 OID 17624)
-- Name: default_donation default_donation_fk_organization_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_donation
    ADD CONSTRAINT default_donation_fk_organization_fkey FOREIGN KEY (fk_organization) REFERENCES public.ong(id_ong) ON UPDATE CASCADE;


--
-- TOC entry 5810 (class 2606 OID 17666)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_w_fk_collection_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_w_fk_collection_point_fkey FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point);


--
-- TOC entry 5811 (class 2606 OID 18012)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_we_fk_standar_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_we_fk_standar_product_fkey FOREIGN KEY (fk_standar_product) REFERENCES public.standar_product(id_standar_product) ON DELETE SET NULL;


--
-- TOC entry 5812 (class 2606 OID 18017)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_we_fk_standar_product_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_we_fk_standar_product_key FOREIGN KEY (fk_standar_product) REFERENCES public.standar_product(id_standar_product) ON DELETE SET NULL;


--
-- TOC entry 5813 (class 2606 OID 18007)
-- Name: default_product_x_collection_point_x_week default_product_x_collection_point_x_week_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.default_product_x_collection_point_x_week
    ADD CONSTRAINT default_product_x_collection_point_x_week_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON DELETE SET NULL;


--
-- TOC entry 5800 (class 2606 OID 17586)
-- Name: collection_point fk_owner_collection_point; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.collection_point
    ADD CONSTRAINT fk_owner_collection_point FOREIGN KEY (fk_owner) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5797 (class 2606 OID 17506)
-- Name: locality locality_fk_city_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_fk_city_fkey FOREIGN KEY (fk_city) REFERENCES public.city(id_city) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5798 (class 2606 OID 17516)
-- Name: neighborhood neighborhood_fk_locality_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_fk_locality_fkey FOREIGN KEY (fk_locality) REFERENCES public.locality(id_locality) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5807 (class 2606 OID 17651)
-- Name: product product_fk_locality_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_fk_locality_fkey FOREIGN KEY (fk_locality) REFERENCES public.locality(id_locality) ON UPDATE CASCADE;


--
-- TOC entry 5808 (class 2606 OID 17646)
-- Name: product product_fk_productor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_fk_productor_fkey FOREIGN KEY (fk_productor) REFERENCES public.users(id_user) ON UPDATE CASCADE;


--
-- TOC entry 5809 (class 2606 OID 17656)
-- Name: product product_fk_standar_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_fk_standar_product_fkey FOREIGN KEY (fk_standar_product) REFERENCES public.standar_product(id_standar_product);


--
-- TOC entry 5814 (class 2606 OID 17695)
-- Name: product_history product_history_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5815 (class 2606 OID 17700)
-- Name: product_history product_history_fk_product_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_state_fkey FOREIGN KEY (fk_product_state) REFERENCES public.product_state(id_product_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5822 (class 2606 OID 18047)
-- Name: purchase_detail purchase_detail_collection_point_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_collection_point_fk FOREIGN KEY (fk_collection_point) REFERENCES public.collection_point(id_collection_point);


--
-- TOC entry 5823 (class 2606 OID 17812)
-- Name: purchase_detail purchase_detail_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5824 (class 2606 OID 17817)
-- Name: purchase_detail purchase_detail_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5825 (class 2606 OID 17822)
-- Name: purchase_detail purchase_detail_fk_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_state_fkey FOREIGN KEY (fk_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state);


--
-- TOC entry 5828 (class 2606 OID 17834)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_fkey FOREIGN KEY (fk_purchase_detail) REFERENCES public.purchase_detail(id_purchase_detail) ON UPDATE CASCADE;


--
-- TOC entry 5829 (class 2606 OID 17839)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_state_fkey FOREIGN KEY (fk_purchase_detail_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state) ON UPDATE CASCADE;


--
-- TOC entry 5826 (class 2606 OID 18052)
-- Name: purchase_detail purchase_detail_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_users_fk FOREIGN KEY (fk_productor) REFERENCES public.users(id_user);


--
-- TOC entry 5827 (class 2606 OID 18057)
-- Name: purchase_detail purchase_detail_users_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_users_fk_1 FOREIGN KEY (fk_buyer) REFERENCES public.users(id_user);


--
-- TOC entry 5818 (class 2606 OID 17780)
-- Name: purchase purchase_fk_current_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_current_state_fkey FOREIGN KEY (fk_current_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5819 (class 2606 OID 17765)
-- Name: purchase purchase_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5820 (class 2606 OID 17797)
-- Name: purchase_state_history purchase_state_history_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE;


--
-- TOC entry 5821 (class 2606 OID 17792)
-- Name: purchase_state_history purchase_state_history_fk_purchase_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_state_fkey FOREIGN KEY (fk_purchase_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5806 (class 2606 OID 17634)
-- Name: standar_product standar_product_fk_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_fk_category_fkey FOREIGN KEY (fk_category) REFERENCES public.category(id_category);


--
-- TOC entry 5816 (class 2606 OID 17713)
-- Name: stock_movement stock_movement_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5817 (class 2606 OID 17955)
-- Name: stock_movement stock_movement_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_users_fk FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5801 (class 2606 OID 17581)
-- Name: users user_fk_collection_point_suscribed_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_collection_point_suscribed_fkey FOREIGN KEY (fk_collection_point_suscribed) REFERENCES public.collection_point(id_collection_point);


--
-- TOC entry 5802 (class 2606 OID 17561)
-- Name: users user_fk_neighborhood_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_fk_neighborhood_fkey FOREIGN KEY (fk_neighborhood) REFERENCES public.neighborhood(id_neighborhood) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5830 (class 2606 OID 17913)
-- Name: user_history user_history_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5831 (class 2606 OID 17918)
-- Name: user_history user_history_fk_user_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_state_fkey FOREIGN KEY (fk_user_state) REFERENCES public.user_state(id_user_state);


--
-- TOC entry 5832 (class 2606 OID 17987)
-- Name: vote vote_default_product_x_collection_point_x_week_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_default_product_x_collection_point_x_week_fk FOREIGN KEY (fk_default_product_x_collection_point_x_week) REFERENCES public.default_product_x_collection_point_x_week(id_default_product_x_collection_point) ON UPDATE CASCADE;


--
-- TOC entry 5833 (class 2606 OID 17930)
-- Name: vote vote_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product);


--
-- TOC entry 5834 (class 2606 OID 17935)
-- Name: vote vote_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


-- Completed on 2025-03-26 16:19:52

--
-- PostgreSQL database dump complete
--

