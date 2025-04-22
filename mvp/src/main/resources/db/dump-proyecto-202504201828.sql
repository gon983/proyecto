--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-04-20 18:28:24

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
-- TOC entry 6008 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 1724 (class 1247 OID 17962)
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
    name character varying(100) NOT NULL,
    photo character varying
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 18136)
-- Name: chat_messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_messages (
    id_message uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    is_from_company boolean NOT NULL,
    content text NOT NULL,
    sent_at timestamp with time zone NOT NULL,
    read boolean DEFAULT false NOT NULL
);


ALTER TABLE public.chat_messages OWNER TO postgres;

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
-- TOC entry 247 (class 1259 OID 18124)
-- Name: device_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.device_tokens (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    device_token character varying(255) NOT NULL,
    device_type character varying(10) NOT NULL,
    created_at timestamp without time zone NOT NULL
);


ALTER TABLE public.device_tokens OWNER TO postgres;

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
    coordinates public.geometry(Point,4326) NOT NULL,
    active boolean DEFAULT true NOT NULL
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
    photo character varying(45),
    unit_measurement character varying(100) NOT NULL,
    unity_price double precision DEFAULT 0 NOT NULL,
    fk_category uuid,
    unity_cost double precision DEFAULT 0 NOT NULL,
    cantidad_vendida_semana character varying,
    dinero_generado_semana character varying
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
-- TOC entry 246 (class 1259 OID 18098)
-- Name: product_x_recommended_pack; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_x_recommended_pack (
    id_product_x_recommended_pack uuid DEFAULT gen_random_uuid() NOT NULL,
    fk_recommended_pack uuid NOT NULL,
    fk_product uuid NOT NULL,
    quantity numeric(10,2) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT product_x_recommended_pack_quantity_check CHECK ((quantity > (0)::numeric))
);


ALTER TABLE public.product_x_recommended_pack OWNER TO postgres;

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
    fk_current_state uuid NOT NULL,
    mp_preference_id character varying,
    mp_payment_id character varying,
    mp_payment_date timestamp with time zone,
    external_reference character varying,
    id_location uuid
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
-- TOC entry 245 (class 1259 OID 18089)
-- Name: recommended_pack; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recommended_pack (
    id_recommended_pack uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    image_url character varying(512),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone
);


ALTER TABLE public.recommended_pack OWNER TO postgres;

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
    password character varying NOT NULL,
    role character varying NOT NULL,
    unread boolean DEFAULT false NOT NULL
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
-- TOC entry 5978 (class 0 OID 17479)
-- Dependencies: 224
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.category VALUES ('0dcf68f8-8171-4320-a9ff-8a812ca45bf4', 'Verduras', NULL);
INSERT INTO public.category VALUES ('7a950b95-9ad0-4838-9999-9fe22e2cbb94', 'Almacen', NULL);
INSERT INTO public.category VALUES ('097ef14d-26c5-4b3a-a113-25e78afb7f69', 'Frutas', '/categorias/frutas.png');
INSERT INTO public.category VALUES ('0c8d442f-f5ee-48ab-a999-c6f46e575194', 'Dietetica', '');


--
-- TOC entry 6002 (class 0 OID 18136)
-- Dependencies: 248
-- Data for Name: chat_messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.chat_messages VALUES ('1ca2d332-401c-41b1-898a-d269f9123e8e', 'f7358a8c-4650-4531-8a53-f879a646e026', true, 'Hola gil', '2025-04-20 16:10:47.361521-03', false);
INSERT INTO public.chat_messages VALUES ('491a3f2d-02f4-4c9c-9628-6d21811780f6', 'f7358a8c-4650-4531-8a53-f879a646e026', true, 'todo bien por suerte', '2025-04-20 16:49:05.330696-03', false);
INSERT INTO public.chat_messages VALUES ('85d38955-1bf9-4c88-983a-2af8e4928a51', 'f7358a8c-4650-4531-8a53-f879a646e026', false, 'Hola', '2025-04-19 12:19:51.487009-03', true);
INSERT INTO public.chat_messages VALUES ('1a855f61-70df-45b3-84ed-b0583e607338', 'f7358a8c-4650-4531-8a53-f879a646e026', false, 'como estas?', '2025-04-19 12:23:52.083384-03', true);
INSERT INTO public.chat_messages VALUES ('02ec9a96-cc0d-4948-aa2c-b43added1164', 'f7358a8c-4650-4531-8a53-f879a646e026', false, 'vos bichinn?', '2025-04-20 18:14:27.599553-03', true);


--
-- TOC entry 5980 (class 0 OID 17491)
-- Dependencies: 226
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.city VALUES ('602ec196-d4d7-4194-a660-9b4afc9a07ea', 'Cordoba', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('49519714-aad2-4b19-8f6b-bf688e565642', 'CABA', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('29559450-d74c-45a0-83f4-cdfc0a55db7d', 'Rosario', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('a6e9570d-80eb-48c9-a5e1-fe30072c5666', 'Buenos Aires', '6a9fe61b-be17-4821-a4a3-71fa282e9289');
INSERT INTO public.city VALUES ('6597656c-f5e7-4e1a-a156-d62cb86f0bd0', 'Mendoza', '6a9fe61b-be17-4821-a4a3-71fa282e9289');


--
-- TOC entry 5979 (class 0 OID 17484)
-- Dependencies: 225
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.country VALUES ('6a9fe61b-be17-4821-a4a3-71fa282e9289', 'Argentina');


--
-- TOC entry 6001 (class 0 OID 18124)
-- Dependencies: 247
-- Data for Name: device_tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5981 (class 0 OID 17501)
-- Dependencies: 227
-- Data for Name: locality; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locality VALUES ('10fbe8c3-0291-4bc7-a96a-7d10782b36ec', 'Capital', '602ec196-d4d7-4194-a660-9b4afc9a07ea');


--
-- TOC entry 5998 (class 0 OID 18063)
-- Dependencies: 244
-- Data for Name: locations; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locations VALUES ('d0382da3-11d8-446d-a70b-efc1ab32ff94', 'e96122cf-e77b-4c2a-ba78-ab86d6d2f238', 'Tunel Plaza España, Nueva Córdoba, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000AF590FCBCF0B50C02C579740B96D3FC0', true);
INSERT INTO public.locations VALUES ('366f01a0-f089-43e3-bbf9-c869d3447f0f', 'e96122cf-e77b-4c2a-ba78-ab86d6d2f238', '1722, Crisólogo Oliva, Alto Alberdi, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5002, Argentina', '0101000020E6100000DCC9E0A47A0D50C0AE51CC13E06B3FC0', true);
INSERT INTO public.locations VALUES ('6241c846-402e-48a3-80d9-b61af456ffba', 'f7358a8c-4650-4531-8a53-f879a646e026', '3949, Pedro de Brizuela, Ampliación Jardín Espinosa, Villa San Carlos, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000A9EE984E110B50C025BD13B52F753FC0', false);
INSERT INTO public.locations VALUES ('4405b2cd-a446-4e0f-b0ba-ba452764a2c8', 'f7358a8c-4650-4531-8a53-f879a646e026', 'Fuente del Perdón, Avenida Vélez Sarsfield, Güemes, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000075F984C150C50C0B30C71AC8B6B3FC0', false);
INSERT INTO public.locations VALUES ('d177b44d-12aa-4c4a-9d5a-a569300ebd4a', 'f7358a8c-4650-4531-8a53-f879a646e026', '1074, La Pampa, Cupani, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E610000027907454420D50C0288D0EC8196C3FC0', false);
INSERT INTO public.locations VALUES ('1bbc4f24-55a3-4849-9eb5-03156392d214', 'f7358a8c-4650-4531-8a53-f879a646e026', '1120, 27 de Abril, Alberdi, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E610000065445370C60C50C0725F95019F693FC0', false);
INSERT INTO public.locations VALUES ('da324c37-1b2d-45e7-aa91-fb33bae65fc7', 'f7358a8c-4650-4531-8a53-f879a646e026', '1297, Francisco N. de Laprida, Observatorio, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E610000021079A8A200D50C0A0662685896B3FC0', false);
INSERT INTO public.locations VALUES ('13bd69d5-fa93-413c-af07-678070d9845c', 'f1bfec18-a7f4-4419-ba55-c74abe4c445a', 'Country Ayres del Sur, San Antonio, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000A89A0FBEE60B50C034C97ECCBD763FC0', false);
INSERT INTO public.locations VALUES ('b7d5af2b-31c8-493a-8572-f76c840c1d12', 'f1bfec18-a7f4-4419-ba55-c74abe4c445a', 'Fuente del Perdón, Avenida Vélez Sarsfield, Güemes, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000075F984C150C50C0B30C71AC8B6B3FC0', false);
INSERT INTO public.locations VALUES ('8058829b-121f-4d52-a892-2e4f3a494f95', 'f1bfec18-a7f4-4419-ba55-c74abe4c445a', 'Fuente del Perdón, Avenida Vélez Sarsfield, Güemes, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000075F984C150C50C0B30C71AC8B6B3FC0', false);
INSERT INTO public.locations VALUES ('2fb6ee3f-b0ca-456b-9458-db5e093e6c73', 'f7358a8c-4650-4531-8a53-f879a646e026', 'Fuente del Perdón, Avenida Vélez Sarsfield, Güemes, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000075F984C150C50C0B30C71AC8B6B3FC0', false);
INSERT INTO public.locations VALUES ('bfb9bc92-40fc-4a01-a311-a6701a976f0d', 'f7358a8c-4650-4531-8a53-f879a646e026', 'Fuente del Perdón, Avenida Vélez Sarsfield, Güemes, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000075F984C150C50C0B30C71AC8B6B3FC0', false);
INSERT INTO public.locations VALUES ('f4c8830b-5661-4caf-bd73-b4459f82f768', 'f7358a8c-4650-4531-8a53-f879a646e026', '3950, Pedro de Brizuela, Ampliación Jardín Espinosa, Villa San Carlos, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000C74ACCB3120B50C055849B8C2A753FC0', false);
INSERT INTO public.locations VALUES ('c58a9610-eca2-458d-8ec2-9facd30ac9e0', 'f7358a8c-4650-4531-8a53-f879a646e026', 'Country Ayres del Sur, San Antonio, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000A89A0FBEE60B50C034C97ECCBD763FC0', false);
INSERT INTO public.locations VALUES ('60a13a68-f1f6-47b8-90cc-fe1af4c46490', 'f7358a8c-4650-4531-8a53-f879a646e026', '34, Ituzaingó, Centro, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000BDC946B0A10B50C0055BC237C96A3FC0', false);
INSERT INTO public.locations VALUES ('80fb0562-3a2f-4c24-a789-63d3ca471cf5', 'f7358a8c-4650-4531-8a53-f879a646e026', '502, Rivadavia, Centro, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000C1648943880B50C0B96A345918693FC0', false);
INSERT INTO public.locations VALUES ('687c11da-db8a-485f-b646-bbc53ee441d8', 'f7358a8c-4650-4531-8a53-f879a646e026', '3951, Pedro de Brizuela, Ampliación Jardín Espinosa, Villa San Carlos, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000624E6160110B50C0BC01F40A31753FC0', false);
INSERT INTO public.locations VALUES ('9cf4ee5c-5281-422e-b3f4-1a8a29fe0ca8', 'f7358a8c-4650-4531-8a53-f879a646e026', '3960, Pedro de Brizuela, Ampliación Jardín Espinosa, Villa San Carlos, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000434CD3B1140B50C0595D3DDD2F753FC0', false);
INSERT INTO public.locations VALUES ('61b075c7-4600-4525-9e20-c823ce325709', 'f7358a8c-4650-4531-8a53-f879a646e026', 'Fuente del Perdón, Avenida Vélez Sarsfield, Güemes, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000075F984C150C50C0B30C71AC8B6B3FC0', false);
INSERT INTO public.locations VALUES ('c549317a-494f-49c3-a085-a7851f7ae83e', 'f87bcb7f-a067-4266-b3c0-01a84c348d92', '3945, Pedro de Brizuela, Ampliación Jardín Espinosa, Villa San Carlos, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E610000068E0EF3A0F0B50C0CF76323933753FC0', false);
INSERT INTO public.locations VALUES ('6e60549b-2324-490d-8ca0-9819f53b1d68', '9c012300-e1b0-45f0-9e53-45d519b7e1bf', 'Altos de Manantiales, Lomas de Manatiales, Córdoba, Municipio de Córdoba, Pedanía Capital, Departamento Capital, Córdoba, X5000, Argentina', '0101000020E6100000DB1C3668260F50C058AFFD4016753FC0', false);


--
-- TOC entry 5982 (class 0 OID 17511)
-- Dependencies: 228
-- Data for Name: neighborhood; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.neighborhood VALUES ('23f4f7d4-7923-495d-85d0-27f34d932537', 'Barrio Jardin', '10fbe8c3-0291-4bc7-a96a-7d10782b36ec');
INSERT INTO public.neighborhood VALUES ('533306f2-45f9-4a4e-b0da-06d8073220d5', 'Jose Ignacio Diaz', '10fbe8c3-0291-4bc7-a96a-7d10782b36ec');


--
-- TOC entry 5985 (class 0 OID 17639)
-- Dependencies: 231
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.product VALUES ('7f1a0868-3ae1-47f1-921f-9575c82aae2d', 'Harina', '/images/products/harina.png', 'kg', 2, '0c8d442f-f5ee-48ab-a999-c6f46e575194', 1, NULL, NULL);
INSERT INTO public.product VALUES ('eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 'Banana', '/images/products/banana.png', 'kg', 5, '097ef14d-26c5-4b3a-a113-25e78afb7f69', 2, NULL, NULL);
INSERT INTO public.product VALUES ('206005a3-c17c-424d-af04-523203728bfd', 'Manzana', '', 'kg', 0, '097ef14d-26c5-4b3a-a113-25e78afb7f69', 0, NULL, NULL);
INSERT INTO public.product VALUES ('cce70d24-bd68-447e-847d-799747e9188c', 'Berenjena', '', 'kg', 0, '0dcf68f8-8171-4320-a9ff-8a812ca45bf4', 0, NULL, NULL);
INSERT INTO public.product VALUES ('a96dd07f-676d-4aec-8e4c-0ce06b813ce2', 'Lechuga', '', 'kg', 5, '0dcf68f8-8171-4320-a9ff-8a812ca45bf4', 2, NULL, NULL);
INSERT INTO public.product VALUES ('3de87695-9b87-489c-a0e9-e15822282c60', 'Fideos', '', 'kg', 0, '0c8d442f-f5ee-48ab-a999-c6f46e575194', 0, NULL, NULL);
INSERT INTO public.product VALUES ('1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'Tomate ', '/images/products/tomate.png', 'kg', 100, '0dcf68f8-8171-4320-a9ff-8a812ca45bf4', 1, NULL, NULL);
INSERT INTO public.product VALUES ('c8084762-1fe9-420a-b406-cf29485a8722', 'Yerba', '', 'kg', 0, '7a950b95-9ad0-4838-9999-9fe22e2cbb94', 0, NULL, NULL);


--
-- TOC entry 5987 (class 0 OID 17688)
-- Dependencies: 233
-- Data for Name: product_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5986 (class 0 OID 17681)
-- Dependencies: 232
-- Data for Name: product_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6000 (class 0 OID 18098)
-- Dependencies: 246
-- Data for Name: product_x_recommended_pack; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5990 (class 0 OID 17760)
-- Dependencies: 236
-- Data for Name: purchase; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase VALUES ('77b613f9-1984-4a2a-a0c5-6302167b0841', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-02-24 20:43:16.944691', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('fda6c680-686c-4493-9dc2-14e9dca88b34', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-02-25 18:36:03.004606', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-c4048b7a-099e-4e38-bb47-061427903932', NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('d6810eef-8620-4cd3-ac2a-13015bc209ee', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 18:40:01.772874', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 'f7358a8c-4650-4531-8a53-f879a646e026', 0, NULL, NULL, '2025-04-08 11:04:22.840513', NULL, 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-7a4d6f4a-3b5f-49dd-a21e-09e232b69d5c', '107906282466', '2025-04-09 20:27:55-03', 'Purchase_2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 'd177b44d-12aa-4c4a-9d5a-a569300ebd4a');
INSERT INTO public.purchase VALUES ('6e1c1e93-7d1a-471c-bf5c-948d854b5521', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-18 15:20:48.413873', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('3492e50b-35f8-4411-8347-d2ba149ceb1a', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-20 21:08:25.53063', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-1a70a168-b55c-4618-a4f6-9e8c8947f4be', NULL, NULL, 'Purchase_3492e50b-35f8-4411-8347-d2ba149ceb1a', NULL);
INSERT INTO public.purchase VALUES ('0bad47fe-34e9-43b2-908e-57312a41c82a', '03cbc4c7-0907-4bd2-8adb-b8e138218250', 0, NULL, NULL, '2025-04-04 21:42:42.761095', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('f150b171-8d46-4204-bdf3-72374f138493', '3bc2cac3-aadc-468c-9942-39fd2b7f2762', 0, NULL, NULL, '2025-04-02 23:05:09.323741', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-3201fe9e-d0f0-4649-acd6-04dde7772c1d', NULL, NULL, 'Purchase_f150b171-8d46-4204-bdf3-72374f138493', NULL);
INSERT INTO public.purchase VALUES ('7bccaafb-1608-4399-a5e1-5b694a7bb438', 'f7358a8c-4650-4531-8a53-f879a646e026', 0, NULL, NULL, '2025-04-09 20:28:19.359065', NULL, 'e4aa98f7-2500-44ef-a05b-e44ee3e068ff', '447529108-9f38153d-ec3c-4a76-9eec-a9556ac9ab04', '107476383553', '2025-04-09 20:38:42-03', 'Purchase_7bccaafb-1608-4399-a5e1-5b694a7bb438', '1bbc4f24-55a3-4849-9eb5-03156392d214');
INSERT INTO public.purchase VALUES ('833c95b2-74cd-4c23-b024-82950cc728be', 'f87bcb7f-a067-4266-b3c0-01a84c348d92', 0, NULL, NULL, '2025-04-16 10:21:32.743811', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-da754545-678b-44f5-9b6a-f4ba16b9cf38', NULL, NULL, 'Purchase_833c95b2-74cd-4c23-b024-82950cc728be', 'c549317a-494f-49c3-a085-a7851f7ae83e');
INSERT INTO public.purchase VALUES ('91da147d-0e49-47bf-a6b5-a08147b9d2ed', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 0, NULL, NULL, '2025-03-25 18:31:22.948065', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('c3e980f2-f990-4521-8593-84387dc5c689', 'f1bfec18-a7f4-4419-ba55-c74abe4c445a', 0, NULL, NULL, '2025-04-10 14:43:21.785661', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-27fd8a0f-9161-455e-b44c-b2b52354ee2f', NULL, NULL, 'Purchase_c3e980f2-f990-4521-8593-84387dc5c689', 'b7d5af2b-31c8-493a-8572-f76c840c1d12');
INSERT INTO public.purchase VALUES ('13ec2d09-56f5-44b6-aa71-a538d7107687', '9c012300-e1b0-45f0-9e53-45d519b7e1bf', 0, NULL, NULL, '2025-04-16 10:56:16.566014', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-b3a69726-9976-4c24-a892-0ccabc5a15f2', NULL, NULL, 'Purchase_13ec2d09-56f5-44b6-aa71-a538d7107687', '6e60549b-2324-490d-8ca0-9819f53b1d68');
INSERT INTO public.purchase VALUES ('12b58e37-46cf-4fca-817d-bbe5d507d1e0', 'f7358a8c-4650-4531-8a53-f879a646e026', 0, NULL, NULL, '2025-04-18 10:54:38.804982', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('10dad1fe-0611-4b77-96fa-739598f532b2', '37b705c2-13d9-47ff-95b1-4cbbe124f892', 0, NULL, NULL, '2025-04-19 12:08:28.96255', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase VALUES ('fe89ba52-5179-4d02-b9a1-39017904209e', 'e96122cf-e77b-4c2a-ba78-ab86d6d2f238', 0, NULL, NULL, '2025-04-08 17:36:04.320472', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', '447529108-ce38b9af-cee7-47fd-93c3-26f0281cad86', NULL, NULL, 'Purchase_fe89ba52-5179-4d02-b9a1-39017904209e', 'd0382da3-11d8-446d-a70b-efc1ab32ff94');
INSERT INTO public.purchase VALUES ('f7392c69-7c21-4050-bf4b-6beff8b586dd', '93d55d96-452f-40b2-872e-7d4b19a1ce0e', 0, NULL, NULL, '2025-04-11 01:56:27.148519', NULL, '11c4c9f4-d5ee-4866-a22e-e1eb824efafa', NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 5993 (class 0 OID 17807)
-- Dependencies: 239
-- Data for Name: purchase_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_detail VALUES ('c634fb7d-4b1a-436b-ac9a-5c3a4d36d013', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 2, 'fda6c680-686c-4493-9dc2-14e9dca88b34', 300.7, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('7d38ee00-02ce-4514-b4a2-a677c8062354', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, 'fda6c680-686c-4493-9dc2-14e9dca88b34', 100, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('84badad5-f32f-4a20-83a8-8cf7d105d9cf', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, 'fda6c680-686c-4493-9dc2-14e9dca88b34', 100, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('b0c006c6-9440-4199-99d5-84c8432eb68c', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1.9, '3492e50b-35f8-4411-8347-d2ba149ceb1a', 300, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.purchase_detail VALUES ('703dcfb9-1223-4596-80fd-b166ff6d046a', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 2, 'fe89ba52-5179-4d02-b9a1-39017904209e', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'e96122cf-e77b-4c2a-ba78-ab86d6d2f238');
INSERT INTO public.purchase_detail VALUES ('d815a05e-49ea-4ffb-9db0-561a0d29316c', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, 'fe89ba52-5179-4d02-b9a1-39017904209e', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'e96122cf-e77b-4c2a-ba78-ab86d6d2f238');
INSERT INTO public.purchase_detail VALUES ('2e502af4-034d-410f-b60e-08c616a863b4', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 3, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('2a624edf-e3fd-4c01-9732-b2b1b697e986', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 4, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('1348da68-d4c1-4d20-8b3b-ad8967e5a3da', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, '91da147d-0e49-47bf-a6b5-a08147b9d2ed', 8, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, 'c446f03c-490c-4fe9-a63a-ad407c981c29', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe');
INSERT INTO public.purchase_detail VALUES ('df0bbbe9-7e49-4649-bd08-7706df1e1807', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 3, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('976bcbee-72d0-4423-a3f4-948b30e51b44', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 4, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('8bb2dca5-8f94-4134-8c96-8e76553f3fdc', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 3, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('305c7280-5ebb-4a99-b0b4-d4c414ba019d', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 4, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('18538720-11d4-44de-82a5-1dd5c96062ea', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 0.4, 'd6810eef-8620-4cd3-ac2a-13015bc209ee', 8, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, 'c446f03c-490c-4fe9-a63a-ad407c981c29', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe');
INSERT INTO public.purchase_detail VALUES ('b3149390-1b2d-451e-a5f8-5a148a68af66', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 3, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('977e1a9e-bfa2-40fd-9ac4-077965c04bcb', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 2, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('b07fb8e8-d995-4e21-a9a5-84d1c91475e4', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '2f6eeb0c-db4b-4320-aa2b-407eea1b17e4', 2, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('9d9b1c44-15e7-4ee2-a3f5-29fcca44a4bb', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 3, 'f150b171-8d46-4204-bdf3-72374f138493', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '3bc2cac3-aadc-468c-9942-39fd2b7f2762');
INSERT INTO public.purchase_detail VALUES ('78a5588b-183b-4129-a48e-06fd61498091', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '7bccaafb-1608-4399-a5e1-5b694a7bb438', 2, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('9cdbd4d8-fd55-4e32-bf4c-0384f948ca50', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '7bccaafb-1608-4399-a5e1-5b694a7bb438', 3, '3f190193-de36-4904-ba03-db301f27083b', NULL, NULL, NULL, NULL, NULL, NULL, 'f7358a8c-4650-4531-8a53-f879a646e026');
INSERT INTO public.purchase_detail VALUES ('a41e4ac9-7337-4b31-bf68-8440564ec063', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1.5, 'c3e980f2-f990-4521-8593-84387dc5c689', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f1bfec18-a7f4-4419-ba55-c74abe4c445a');
INSERT INTO public.purchase_detail VALUES ('722f61e9-64cf-4c1e-8d8e-27a4fd439d94', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1.5, 'c3e980f2-f990-4521-8593-84387dc5c689', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f1bfec18-a7f4-4419-ba55-c74abe4c445a');
INSERT INTO public.purchase_detail VALUES ('923a5e5b-a937-4800-8379-893f3d9c60c7', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 0.5, 'fe89ba52-5179-4d02-b9a1-39017904209e', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'e96122cf-e77b-4c2a-ba78-ab86d6d2f238');
INSERT INTO public.purchase_detail VALUES ('9dc94eb6-21b7-4731-80d8-93d5e7c9ac55', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, 'f7392c69-7c21-4050-bf4b-6beff8b586dd', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '93d55d96-452f-40b2-872e-7d4b19a1ce0e');
INSERT INTO public.purchase_detail VALUES ('501ad389-38d6-4e8b-8897-57aaa9c59944', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, 'f7392c69-7c21-4050-bf4b-6beff8b586dd', 3, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '93d55d96-452f-40b2-872e-7d4b19a1ce0e');
INSERT INTO public.purchase_detail VALUES ('c249903a-f44a-4c12-ba63-03beb92c939d', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '833c95b2-74cd-4c23-b024-82950cc728be', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f87bcb7f-a067-4266-b3c0-01a84c348d92');
INSERT INTO public.purchase_detail VALUES ('6f26be23-872b-4001-a0ff-de9c4ac444b1', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '13ec2d09-56f5-44b6-aa71-a538d7107687', 3, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '9c012300-e1b0-45f0-9e53-45d519b7e1bf');
INSERT INTO public.purchase_detail VALUES ('563da0f3-9c99-426e-93bf-a474791bb05c', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '13ec2d09-56f5-44b6-aa71-a538d7107687', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '9c012300-e1b0-45f0-9e53-45d519b7e1bf');
INSERT INTO public.purchase_detail VALUES ('a24ca8ba-fbb2-4890-bba3-f32160f1a73b', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 2, '13ec2d09-56f5-44b6-aa71-a538d7107687', 2, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, '9c012300-e1b0-45f0-9e53-45d519b7e1bf');
INSERT INTO public.purchase_detail VALUES ('40fdb86a-a728-4d6e-ba5f-291f19deba1f', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '833c95b2-74cd-4c23-b024-82950cc728be', 3, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f87bcb7f-a067-4266-b3c0-01a84c348d92');
INSERT INTO public.purchase_detail VALUES ('081cb21e-4794-4551-91d6-1e698f0c87fb', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '833c95b2-74cd-4c23-b024-82950cc728be', 4, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f87bcb7f-a067-4266-b3c0-01a84c348d92');
INSERT INTO public.purchase_detail VALUES ('1b738535-7a4c-4a44-af27-3bd2a64ffc66', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '833c95b2-74cd-4c23-b024-82950cc728be', 3, '8a69aecf-7441-4e57-bbea-e91cbbfc7487', NULL, NULL, NULL, NULL, NULL, NULL, 'f87bcb7f-a067-4266-b3c0-01a84c348d92');


--
-- TOC entry 5992 (class 0 OID 17802)
-- Dependencies: 238
-- Data for Name: purchase_detail_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_detail_state VALUES ('8a69aecf-7441-4e57-bbea-e91cbbfc7487', 'pending');
INSERT INTO public.purchase_detail_state VALUES ('ad842904-786c-4dc6-8620-378ebf85e7c0', 'received');
INSERT INTO public.purchase_detail_state VALUES ('394cb175-ccaa-49d1-a303-71d2456ee695', 'not received');
INSERT INTO public.purchase_detail_state VALUES ('3f190193-de36-4904-ba03-db301f27083b', 'confirmed');
INSERT INTO public.purchase_detail_state VALUES ('5d3616de-9963-4413-a42c-65a73ded54da', 'payed');


--
-- TOC entry 5994 (class 0 OID 17827)
-- Dependencies: 240
-- Data for Name: purchase_detail_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5989 (class 0 OID 17755)
-- Dependencies: 235
-- Data for Name: purchase_state; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.purchase_state VALUES ('11c4c9f4-d5ee-4866-a22e-e1eb824efafa', 'pending');
INSERT INTO public.purchase_state VALUES ('e4aa98f7-2500-44ef-a05b-e44ee3e068ff', 'confirmed');
INSERT INTO public.purchase_state VALUES ('12d76cf7-1a2d-46bd-a43d-30e0828f8250', 'received');
INSERT INTO public.purchase_state VALUES ('739d459e-816b-49ea-acf2-3b20b6e85ef1', 'not received');
INSERT INTO public.purchase_state VALUES ('c0bdc407-43e5-4588-9236-f8db23949ad7', 'payed');


--
-- TOC entry 5991 (class 0 OID 17785)
-- Dependencies: 237
-- Data for Name: purchase_state_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5999 (class 0 OID 18089)
-- Dependencies: 245
-- Data for Name: recommended_pack; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.recommended_pack VALUES ('cb110379-bcb1-4b6f-ba44-77c1bcad78ea', 'Dietetica - 4 personas', 'Productos de dietetica clasicos para consumo de 4 personas', 'ddd', '2025-04-17 12:35:04.316818', NULL);
INSERT INTO public.recommended_pack VALUES ('af220313-730b-4762-9000-6f3b45011a3d', 'Dietetica - 2 personas', 'Productos de dietetica clasicos para dos personas', 'ss', '2025-04-17 12:46:50.610592', NULL);
INSERT INTO public.recommended_pack VALUES ('a2b305ab-5749-4687-bf21-f9bf3a7d3ee5', 'Verduras - 4 personas', 'Verduras mas pedidas para 4 personas', 'sss', '2025-04-17 12:38:16.173665', NULL);
INSERT INTO public.recommended_pack VALUES ('0d49ea02-20e3-4be8-ba0e-c589dac3d4a5', 'Fruta - 4 Personas', 'Las frutas mas pedidas para 4 personas', 'ss', '2025-04-17 13:01:02.39779', NULL);
INSERT INTO public.recommended_pack VALUES ('60bc159c-b744-4d09-8be5-8791ca850d9f', 'Almacen', 'Tu compra de almacen', 'ss', '2025-04-17 12:48:57.096082', NULL);
INSERT INTO public.recommended_pack VALUES ('1f8ea6b4-1e38-4f9a-b0a7-214e2a372d5c', 'Pastas - 4 personas', 'Productos para hacer a la olla para 4 personas', '/images/recommended-packs/packUno.png', '2025-04-10 20:59:27.950731', NULL);


--
-- TOC entry 5715 (class 0 OID 16721)
-- Dependencies: 220
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5984 (class 0 OID 17629)
-- Dependencies: 230
-- Data for Name: standar_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.standar_product VALUES ('6ac6bc96-2a1b-4df2-abc2-ba78f96d96eb', 'Banana', '097ef14d-26c5-4b3a-a113-25e78afb7f69');
INSERT INTO public.standar_product VALUES ('a634e60f-7070-4f1b-bc22-9926952b5fff', 'Tomate', '0dcf68f8-8171-4320-a9ff-8a812ca45bf4');
INSERT INTO public.standar_product VALUES ('cbb6f2b1-3c90-4616-b329-966442c9e8fa', 'Harina', '0c8d442f-f5ee-48ab-a999-c6f46e575194');


--
-- TOC entry 5988 (class 0 OID 17705)
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
INSERT INTO public.stock_movement VALUES ('dc40cf13-02bc-44d1-96ac-b9402f07ebe4', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('75d978de-609f-4c47-b74a-5937fe50fb6a', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('c1f285a1-0711-4562-88e8-f8f26b94768b', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('eb9b3265-5c04-4690-ab8e-9d69fb75e5d9', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('aa8ebafd-9bd1-4df4-bbbc-6792338865a2', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('4512419f-4eee-493c-99d0-04b16b8e167c', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('ff7632c4-e48f-415a-a5b0-b9ed04a41f82', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('459d7591-fd56-4f46-b619-cfacdd8ecd91', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('df94663f-adbe-4c23-a167-e65b7eb7618f', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('67b31d1a-8b84-46bf-a1e2-f9c5a6bc891c', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');
INSERT INTO public.stock_movement VALUES ('30570e18-05ee-440a-bcee-2eda1f999674', '7f1a0868-3ae1-47f1-921f-9575c82aae2d', 1, '2025-04-09', '', 'f7358a8c-4650-4531-8a53-f879a646e026', 'DECREASE');


--
-- TOC entry 5996 (class 0 OID 17906)
-- Dependencies: 242
-- Data for Name: user_history; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5995 (class 0 OID 17901)
-- Dependencies: 241
-- Data for Name: user_state; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5983 (class 0 OID 17553)
-- Dependencies: 229
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('3bc2cac3-aadc-468c-9942-39fd2b7f2762', 'gondutari', 'gondutarigd@gmail.com', '2025-02-26 10:38:59.743479', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_ADMIN', false);
INSERT INTO public.users VALUES ('bfb6dbcc-9f06-4b7e-9754-28368ef642fe', 'rosa', 'seniorarosa@gmail.com', '2025-02-24 19:59:42.157521', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('b088c879-3fe7-4bcd-a6fc-89efb341c602', 'produ', 'juan.perez@example.com', '2025-02-22 16:03:26.961483', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_PRODUCTOR', false);
INSERT INTO public.users VALUES ('6982b8a2-2a9e-47a4-8df9-85f3482b16e2', 'cpown', 'rodrigo@example.com', '2025-02-22 16:12:39.413808', '$2a$10$y5L4QsT2N.wtuhZtZZZ3EuhF2oIrw8nigK4FxefuhyWj7SEy2RFku', 'ROLE_CP_OWNER', false);
INSERT INTO public.users VALUES ('5c938119-df44-43d8-93c2-fe51586a5f60', 'alguien', 'alguien@example.com', '2025-04-04 00:52:15.687755', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('cba4c14e-b516-465d-93cb-4ce0b6486d0f', 'queee', 'quee@example.com', '2025-04-04 21:26:46.920779', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('03cbc4c7-0907-4bd2-8adb-b8e138218250', 'gon_admin', 'gonzalo_dutari@outlook.com', '2025-04-04 21:42:30.500966', 'alveurbp91$nmqt', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('9291aad2-6e6c-4a08-b135-91bafe00fa80', 'sadsa', 'LKSJALK@fssf', '2025-04-04 21:45:39.208589', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('6dfcc17a-16d3-41c5-9098-1dc50fb209ef', 'jejje', 'gonzalito@jejej', '2025-04-04 21:48:12.404082', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('e96122cf-e77b-4c2a-ba78-ab86d6d2f238', 'Catu', 'catalinalinda@mi.unc.edu.ar', '2025-04-08 17:29:41.160589', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('f1bfec18-a7f4-4419-ba55-c74abe4c445a', 'u', 'usuario@gmail.com', '2025-04-10 14:43:12.519191', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('93d55d96-452f-40b2-872e-7d4b19a1ce0e', 'bla', 'blaaaaaaaaa@bla', '2025-04-11 01:55:29.630614', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('f87bcb7f-a067-4266-b3c0-01a84c348d92', 'monica', 'monica@gmail.com', '2025-04-16 10:21:22.061508', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('9c012300-e1b0-45f0-9e53-45d519b7e1bf', 'Bruno', 'brunotodaro9@gmail.com', '2025-04-16 10:55:53.061732', 'chimichurri', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('37b705c2-13d9-47ff-95b1-4cbbe124f892', 'gonza', 'gonza@ff', '2025-04-19 12:08:20.088861', '123456', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('e4a56db2-0dbf-4896-b27d-5fb676d7fceb', 'usu', 'usuario@example.com', '2025-04-03 12:21:48.030977', '123', 'ROLE_USER', false);
INSERT INTO public.users VALUES ('f7358a8c-4650-4531-8a53-f879a646e026', 'a', 'a@example.com', '2025-04-04 11:42:14.346196', '123456', 'ROLE_USER', true);


--
-- TOC entry 5997 (class 0 OID 17923)
-- Dependencies: 243
-- Data for Name: vote; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.vote VALUES ('f7a8c1ea-bc72-4d2f-8b91-14ac2c7986e2', '1a6acd60-a74e-4724-96a8-6ad7359c2fd0', 'bfb6dbcc-9f06-4b7e-9754-28368ef642fe', '2025-03-13 10:14:19.008879-03', 'muy buen producto', '4d0e7a7f-7569-4fad-b107-6e87411b6b23', 0);
INSERT INTO public.vote VALUES ('4bcb36a9-aafb-4ad7-9583-121d226157f0', 'eb12c24a-9948-4aaa-a6cc-cd57f359b4f6', '6982b8a2-2a9e-47a4-8df9-85f3482b16e2', '2025-03-23 18:19:33.338329-03', 'muy buen producto', 'd9e4a5d4-3ec1-4b8e-9795-5ed7eba30291', 5);


--
-- TOC entry 5742 (class 2606 OID 17483)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id_category);


--
-- TOC entry 5798 (class 2606 OID 18143)
-- Name: chat_messages chat_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT chat_messages_pkey PRIMARY KEY (id_message);


--
-- TOC entry 5748 (class 2606 OID 17495)
-- Name: city city_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id_city);


--
-- TOC entry 5744 (class 2606 OID 17490)
-- Name: country country_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_name_key UNIQUE (name);


--
-- TOC entry 5746 (class 2606 OID 17488)
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id_country);


--
-- TOC entry 5794 (class 2606 OID 18128)
-- Name: device_tokens device_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.device_tokens
    ADD CONSTRAINT device_tokens_pkey PRIMARY KEY (id);


--
-- TOC entry 5796 (class 2606 OID 18130)
-- Name: device_tokens device_tokens_user_id_device_type_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.device_tokens
    ADD CONSTRAINT device_tokens_user_id_device_type_key UNIQUE (user_id, device_type);


--
-- TOC entry 5750 (class 2606 OID 17505)
-- Name: locality locality_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_pkey PRIMARY KEY (id_locality);


--
-- TOC entry 5788 (class 2606 OID 18072)
-- Name: locations locations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_pkey PRIMARY KEY (id);


--
-- TOC entry 5752 (class 2606 OID 17515)
-- Name: neighborhood neighborhood_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_pkey PRIMARY KEY (id_neighborhood);


--
-- TOC entry 5766 (class 2606 OID 17694)
-- Name: product_history product_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_pkey PRIMARY KEY (id_product_history);


--
-- TOC entry 5758 (class 2606 OID 17645)
-- Name: product product_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_name_key UNIQUE (name);


--
-- TOC entry 5760 (class 2606 OID 17643)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id_product);


--
-- TOC entry 5762 (class 2606 OID 17687)
-- Name: product_state product_state_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_name_key UNIQUE (name);


--
-- TOC entry 5764 (class 2606 OID 17685)
-- Name: product_state product_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_state
    ADD CONSTRAINT product_state_pkey PRIMARY KEY (id_product_state);


--
-- TOC entry 5792 (class 2606 OID 18105)
-- Name: product_x_recommended_pack product_x_recommended_pack_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_x_recommended_pack
    ADD CONSTRAINT product_x_recommended_pack_pkey PRIMARY KEY (id_product_x_recommended_pack);


--
-- TOC entry 5778 (class 2606 OID 17811)
-- Name: purchase_detail purchase_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_pkey PRIMARY KEY (id_purchase_detail);


--
-- TOC entry 5780 (class 2606 OID 17833)
-- Name: purchase_detail_state_history purchase_detail_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_pkey PRIMARY KEY (id_purchase_detail_state_history);


--
-- TOC entry 5776 (class 2606 OID 17806)
-- Name: purchase_detail_state purchase_detail_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state
    ADD CONSTRAINT purchase_detail_state_pkey PRIMARY KEY (id_purchase_detail_state);


--
-- TOC entry 5772 (class 2606 OID 17764)
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (id_purchase);


--
-- TOC entry 5774 (class 2606 OID 17791)
-- Name: purchase_state_history purchase_state_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_pkey PRIMARY KEY (id_purchase_state_history);


--
-- TOC entry 5770 (class 2606 OID 17759)
-- Name: purchase_state purchase_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state
    ADD CONSTRAINT purchase_state_pkey PRIMARY KEY (id_purchase_state);


--
-- TOC entry 5790 (class 2606 OID 18097)
-- Name: recommended_pack recommended_pack_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommended_pack
    ADD CONSTRAINT recommended_pack_pkey PRIMARY KEY (id_recommended_pack);


--
-- TOC entry 5756 (class 2606 OID 17633)
-- Name: standar_product standar_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_pkey PRIMARY KEY (id_standar_product);


--
-- TOC entry 5768 (class 2606 OID 17712)
-- Name: stock_movement stock_movement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_pkey PRIMARY KEY (id_stock_movement);


--
-- TOC entry 5784 (class 2606 OID 17912)
-- Name: user_history user_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_pkey PRIMARY KEY (id_user_history);


--
-- TOC entry 5754 (class 2606 OID 17558)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id_user);


--
-- TOC entry 5782 (class 2606 OID 17905)
-- Name: user_state user_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_state
    ADD CONSTRAINT user_state_pkey PRIMARY KEY (id_user_state);


--
-- TOC entry 5786 (class 2606 OID 17999)
-- Name: vote vote_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_pkey PRIMARY KEY (id_vote);


--
-- TOC entry 5827 (class 2606 OID 18144)
-- Name: chat_messages chat_messages_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_messages
    ADD CONSTRAINT chat_messages_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id_user);


--
-- TOC entry 5799 (class 2606 OID 17496)
-- Name: city city_fk_country_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_fk_country_fkey FOREIGN KEY (fk_country) REFERENCES public.country(id_country) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5826 (class 2606 OID 18131)
-- Name: device_tokens device_tokens_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.device_tokens
    ADD CONSTRAINT device_tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id_user);


--
-- TOC entry 5800 (class 2606 OID 17506)
-- Name: locality locality_fk_city_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locality
    ADD CONSTRAINT locality_fk_city_fkey FOREIGN KEY (fk_city) REFERENCES public.city(id_city) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5823 (class 2606 OID 18073)
-- Name: locations locations_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id_user) ON DELETE CASCADE;


--
-- TOC entry 5801 (class 2606 OID 17516)
-- Name: neighborhood neighborhood_fk_locality_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.neighborhood
    ADD CONSTRAINT neighborhood_fk_locality_fkey FOREIGN KEY (fk_locality) REFERENCES public.locality(id_locality) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5803 (class 2606 OID 17695)
-- Name: product_history product_history_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5804 (class 2606 OID 17700)
-- Name: product_history product_history_fk_product_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_fk_product_state_fkey FOREIGN KEY (fk_product_state) REFERENCES public.product_state(id_product_state) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5824 (class 2606 OID 18111)
-- Name: product_x_recommended_pack product_x_recommended_pack_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_x_recommended_pack
    ADD CONSTRAINT product_x_recommended_pack_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON DELETE CASCADE;


--
-- TOC entry 5825 (class 2606 OID 18106)
-- Name: product_x_recommended_pack product_x_recommended_pack_fk_recommended_pack_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_x_recommended_pack
    ADD CONSTRAINT product_x_recommended_pack_fk_recommended_pack_fkey FOREIGN KEY (fk_recommended_pack) REFERENCES public.recommended_pack(id_recommended_pack) ON DELETE CASCADE;


--
-- TOC entry 5812 (class 2606 OID 17812)
-- Name: purchase_detail purchase_detail_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5813 (class 2606 OID 17817)
-- Name: purchase_detail purchase_detail_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5814 (class 2606 OID 17822)
-- Name: purchase_detail purchase_detail_fk_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_fk_state_fkey FOREIGN KEY (fk_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state);


--
-- TOC entry 5817 (class 2606 OID 17834)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_fkey FOREIGN KEY (fk_purchase_detail) REFERENCES public.purchase_detail(id_purchase_detail) ON UPDATE CASCADE;


--
-- TOC entry 5818 (class 2606 OID 17839)
-- Name: purchase_detail_state_history purchase_detail_state_history_fk_purchase_detail_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail_state_history
    ADD CONSTRAINT purchase_detail_state_history_fk_purchase_detail_state_fkey FOREIGN KEY (fk_purchase_detail_state) REFERENCES public.purchase_detail_state(id_purchase_detail_state) ON UPDATE CASCADE;


--
-- TOC entry 5815 (class 2606 OID 18052)
-- Name: purchase_detail purchase_detail_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_users_fk FOREIGN KEY (fk_productor) REFERENCES public.users(id_user);


--
-- TOC entry 5816 (class 2606 OID 18057)
-- Name: purchase_detail purchase_detail_users_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_detail
    ADD CONSTRAINT purchase_detail_users_fk_1 FOREIGN KEY (fk_buyer) REFERENCES public.users(id_user);


--
-- TOC entry 5807 (class 2606 OID 17780)
-- Name: purchase purchase_fk_current_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_current_state_fkey FOREIGN KEY (fk_current_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5808 (class 2606 OID 17765)
-- Name: purchase purchase_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5809 (class 2606 OID 18083)
-- Name: purchase purchase_locations_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_locations_fk FOREIGN KEY (id_location) REFERENCES public.locations(id);


--
-- TOC entry 5810 (class 2606 OID 17797)
-- Name: purchase_state_history purchase_state_history_fk_purchase_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_fkey FOREIGN KEY (fk_purchase) REFERENCES public.purchase(id_purchase) ON UPDATE CASCADE;


--
-- TOC entry 5811 (class 2606 OID 17792)
-- Name: purchase_state_history purchase_state_history_fk_purchase_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase_state_history
    ADD CONSTRAINT purchase_state_history_fk_purchase_state_fkey FOREIGN KEY (fk_purchase_state) REFERENCES public.purchase_state(id_purchase_state) ON UPDATE CASCADE;


--
-- TOC entry 5802 (class 2606 OID 17634)
-- Name: standar_product standar_product_fk_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.standar_product
    ADD CONSTRAINT standar_product_fk_category_fkey FOREIGN KEY (fk_category) REFERENCES public.category(id_category);


--
-- TOC entry 5805 (class 2606 OID 17713)
-- Name: stock_movement stock_movement_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 5806 (class 2606 OID 17955)
-- Name: stock_movement stock_movement_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_users_fk FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5819 (class 2606 OID 17913)
-- Name: user_history user_history_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


--
-- TOC entry 5820 (class 2606 OID 17918)
-- Name: user_history user_history_fk_user_state_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_fk_user_state_fkey FOREIGN KEY (fk_user_state) REFERENCES public.user_state(id_user_state);


--
-- TOC entry 5821 (class 2606 OID 17930)
-- Name: vote vote_fk_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_product_fkey FOREIGN KEY (fk_product) REFERENCES public.product(id_product);


--
-- TOC entry 5822 (class 2606 OID 17935)
-- Name: vote vote_fk_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_fk_user_fkey FOREIGN KEY (fk_user) REFERENCES public.users(id_user);


-- Completed on 2025-04-20 18:28:25

--
-- PostgreSQL database dump complete
--

