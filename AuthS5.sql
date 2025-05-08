--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.2

-- Started on 2025-04-20 23:45:51

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

DROP DATABASE IF EXISTS final;
--
-- TOC entry 5030 (class 1262 OID 16697)
-- Name: final; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE final WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'ru-RU';


\connect final

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

SET default_tablespace = '';

--
-- TOC entry 222 (class 1259 OID 16840)
-- Name: chat_participants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chat_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    chat_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_read_at timestamp without time zone,
    is_admin boolean DEFAULT false
);


--
-- TOC entry 239 (class 1259 OID 17095)
-- Name: chat_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chat_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


--
-- TOC entry 238 (class 1259 OID 17094)
-- Name: chat_types_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.chat_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5031 (class 0 OID 0)
-- Dependencies: 238
-- Name: chat_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.chat_types_id_seq OWNED BY public.chat_types.id;


--
-- TOC entry 221 (class 1259 OID 16828)
-- Name: chats; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chats (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255),
    event_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type_id bigint
);


--
-- TOC entry 220 (class 1259 OID 16807)
-- Name: event_participants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.event_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    event_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status public.participant_status DEFAULT 'PENDING'::public.participant_status NOT NULL,
    role public.participant_role DEFAULT 'PARTICIPANT'::public.participant_role NOT NULL
);


--
-- TOC entry 237 (class 1259 OID 17078)
-- Name: event_statuses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.event_statuses (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL
);


--
-- TOC entry 236 (class 1259 OID 17077)
-- Name: event_statuses_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.event_statuses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5032 (class 0 OID 0)
-- Dependencies: 236
-- Name: event_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.event_statuses_id_seq OWNED BY public.event_statuses.id;


--
-- TOC entry 219 (class 1259 OID 16793)
-- Name: events; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.events (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    start_time timestamp without time zone NOT NULL,
    sport_type character varying(100),
    end_time timestamp without time zone,
    location character varying(255),
    organizer_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status integer NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 16884)
-- Name: friends; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.friends (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    friend_id uuid NOT NULL,
    status public.friend_status DEFAULT 'PENDING'::public.friend_status NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 244 (class 1259 OID 17136)
-- Name: message_attachments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.message_attachments (
    id uuid NOT NULL,
    message_id uuid NOT NULL,
    file_url character varying(255) NOT NULL,
    file_name character varying(100) NOT NULL,
    file_type character varying(50) NOT NULL,
    file_size bigint NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 16859)
-- Name: messages; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.messages (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    chat_id uuid NOT NULL,
    sender_id uuid NOT NULL,
    content text NOT NULL,
    sent_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_read boolean DEFAULT false NOT NULL,
    read_at timestamp without time zone,
    reply_to_id uuid,
    status character varying(255)
);


--
-- TOC entry 235 (class 1259 OID 17034)
-- Name: notification; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notification (
    id uuid NOT NULL,
    user_id uuid,
    title character varying(255),
    message text,
    type character varying(50),
    is_read boolean DEFAULT false,
    created_at timestamp without time zone,
    related_entity_type character varying(50),
    related_entity_id uuid
);


--
-- TOC entry 227 (class 1259 OID 16925)
-- Name: recommendations; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recommendations (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    type public.recommendation_type NOT NULL,
    recommended_id uuid NOT NULL,
    score double precision,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 243 (class 1259 OID 17118)
-- Name: relationship_statuses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.relationship_statuses (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


--
-- TOC entry 242 (class 1259 OID 17117)
-- Name: relationship_statuses_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.relationship_statuses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5033 (class 0 OID 0)
-- Dependencies: 242
-- Name: relationship_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.relationship_statuses_id_seq OWNED BY public.relationship_statuses.id;


--
-- TOC entry 241 (class 1259 OID 17109)
-- Name: relationship_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.relationship_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


--
-- TOC entry 240 (class 1259 OID 17108)
-- Name: relationship_types_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.relationship_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5034 (class 0 OID 0)
-- Dependencies: 240
-- Name: relationship_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.relationship_types_id_seq OWNED BY public.relationship_types.id;


--
-- TOC entry 230 (class 1259 OID 16979)
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


--
-- TOC entry 232 (class 1259 OID 16983)
-- Name: status; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.status (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


--
-- TOC entry 228 (class 1259 OID 16937)
-- Name: user_activities; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_activities (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    activity_type character varying(100) NOT NULL,
    distance double precision,
    duration character varying(50),
    calories_burned integer,
    activity_date timestamp without time zone NOT NULL,
    external_id character varying(255),
    raw_data text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 16951)
-- Name: user_photos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_photos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    photo_url character varying(255) NOT NULL,
    description text,
    upload_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_profile_photo boolean DEFAULT false NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 16780)
-- Name: user_profiles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_profiles (
    user_id uuid NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    bio text,
    location character varying(255),
    website character varying(255),
    avatar_url character varying(255),
    sport_type character varying(100),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone
);


--
-- TOC entry 226 (class 1259 OID 16905)
-- Name: user_relationships; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_relationships (
    id bigint NOT NULL,
    user_id uuid NOT NULL,
    related_user_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type_id integer,
    status_id integer
);


--
-- TOC entry 225 (class 1259 OID 16904)
-- Name: user_relationships_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_relationships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 5035 (class 0 OID 0)
-- Dependencies: 225
-- Name: user_relationships_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_relationships_id_seq OWNED BY public.user_relationships.id;


--
-- TOC entry 217 (class 1259 OID 16765)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    email character varying(255) NOT NULL,
    phone_number character varying(20),
    password character varying(255) NOT NULL,
    role bigint NOT NULL,
    is_first_enter boolean DEFAULT true NOT NULL,
    end_date timestamp without time zone,
    last_login timestamp without time zone,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status bigint
);


--
-- TOC entry 234 (class 1259 OID 16987)
-- Name: verification_code; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.verification_code (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    code character varying(6) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    expires_at timestamp without time zone NOT NULL
);


--
-- TOC entry 4756 (class 2604 OID 17098)
-- Name: chat_types id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_types ALTER COLUMN id SET DEFAULT nextval('public.chat_types_id_seq'::regclass);


--
-- TOC entry 4755 (class 2604 OID 17081)
-- Name: event_statuses id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_statuses ALTER COLUMN id SET DEFAULT nextval('public.event_statuses_id_seq'::regclass);


--
-- TOC entry 4758 (class 2604 OID 17121)
-- Name: relationship_statuses id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relationship_statuses ALTER COLUMN id SET DEFAULT nextval('public.relationship_statuses_id_seq'::regclass);


--
-- TOC entry 4757 (class 2604 OID 17112)
-- Name: relationship_types id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relationship_types ALTER COLUMN id SET DEFAULT nextval('public.relationship_types_id_seq'::regclass);


--
-- TOC entry 4744 (class 2604 OID 16908)
-- Name: user_relationships id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_relationships ALTER COLUMN id SET DEFAULT nextval('public.user_relationships_id_seq'::regclass);


--
-- TOC entry 5004 (class 0 OID 16840)
-- Dependencies: 222
-- Data for Name: chat_participants; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.chat_participants VALUES ('da00902b-7a4e-461b-a087-81895def05e8', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '2025-04-16 16:51:20.409528', NULL, false);
INSERT INTO public.chat_participants VALUES ('e82329e7-6038-4848-91ad-5ce9cf1f9781', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '6170175d-748d-4079-b400-d4096903c461', '2025-04-16 19:06:50.436154', '2025-04-16 19:06:50.436154', false);
INSERT INTO public.chat_participants VALUES ('36b2b3e4-c610-4ee4-8b84-71d217dcc879', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '9fc47c84-1e7c-490b-8af3-d542e9af942c', '2025-04-16 19:07:44.215814', '2025-04-16 19:07:44.215814', false);
INSERT INTO public.chat_participants VALUES ('1c8a4d55-84c9-41bb-b58d-bcdeda49ef3c', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'c0050a07-46ab-4811-a41e-8a7ba10baad7', '2025-04-16 20:48:10.024247', '2025-04-16 20:48:10.024247', false);
INSERT INTO public.chat_participants VALUES ('41c8c9b0-2c5a-4989-a7b1-c8be258d9bc7', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'cba2ae3b-3725-40c4-8da7-8146a59babb2', '2025-04-17 01:50:06.688865', '2025-04-17 01:50:06.688865', false);
INSERT INTO public.chat_participants VALUES ('033c82a4-d70b-4f73-8423-bcf9079cc08e', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '2025-04-17 01:52:23.17341', NULL, false);
INSERT INTO public.chat_participants VALUES ('71a989fe-2292-4968-aeaf-96c2fb3e269b', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '0202fde4-9f71-44b7-a02d-3ba03cc83247', '2025-04-17 02:00:27.910168', '2025-04-17 02:00:27.910168', false);
INSERT INTO public.chat_participants VALUES ('6e2b2a8e-1de4-4f38-b2cd-cd5345dd23af', '5560d4f9-bffa-4572-a03b-a6b68362c718', '0202fde4-9f71-44b7-a02d-3ba03cc83247', '2025-04-17 02:01:10.177642', NULL, false);
INSERT INTO public.chat_participants VALUES ('fcec0149-2217-4a30-9d12-02b484e6407a', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '5733a5bd-6389-408a-a5c0-edebe3f49236', '2025-04-17 04:44:55.50285', NULL, false);
INSERT INTO public.chat_participants VALUES ('73a365c8-837c-4e87-b18a-3f8e1ce10954', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'd10b9766-c36a-4f08-880c-774f1e0c2d5b', '2025-04-17 05:17:42.470146', NULL, false);
INSERT INTO public.chat_participants VALUES ('44f0b0ac-594a-40d1-893b-7e6012e44dad', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '8f118829-caaf-4d30-837f-72bef7e2759a', '2025-04-17 16:35:24.706526', NULL, false);
INSERT INTO public.chat_participants VALUES ('a09c044f-7502-4117-b788-256f1d28999e', '5560d4f9-bffa-4572-a03b-a6b68362c718', '4184cf72-160c-4f74-868e-bc14c20c0477', '2025-04-17 16:46:54.935397', NULL, false);
INSERT INTO public.chat_participants VALUES ('a716fe71-60b7-46fa-b6ee-cc0d8a6595bd', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '5a2b61b0-5e2e-4e8d-9b9b-fda5818e3370', '2025-04-17 17:16:36.191134', NULL, false);
INSERT INTO public.chat_participants VALUES ('0dfa53c4-1129-41ad-a2bf-0a33023cbbc4', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'cad0a461-fc18-4675-9cf5-1b5db56b13f4', '2025-04-17 17:17:00.786941', NULL, false);
INSERT INTO public.chat_participants VALUES ('c11874bc-d109-4891-bad6-4bc6f352fa4e', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'cad0a461-fc18-4675-9cf5-1b5db56b13f4', '2025-04-17 17:18:46.925521', NULL, false);


--
-- TOC entry 5019 (class 0 OID 17095)
-- Dependencies: 239
-- Data for Name: chat_types; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.chat_types VALUES (1, 'PRIVATE', 'Private chat between two users');
INSERT INTO public.chat_types VALUES (2, 'GROUP', 'Group chat for multiple users');
INSERT INTO public.chat_types VALUES (3, 'EVENT', 'Event-related chat');


--
-- TOC entry 5003 (class 0 OID 16828)
-- Dependencies: 221
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.chats VALUES ('6170175d-748d-4079-b400-d4096903c461', 'Test Chat', NULL, '2025-04-16 19:06:51.037051', 2);
INSERT INTO public.chats VALUES ('9fc47c84-1e7c-490b-8af3-d542e9af942c', 'Test Chat', NULL, '2025-04-16 19:07:44.216783', 1);
INSERT INTO public.chats VALUES ('c0050a07-46ab-4811-a41e-8a7ba10baad7', 'Test Chat', NULL, '2025-04-16 20:48:10.041249', 1);
INSERT INTO public.chats VALUES ('cba2ae3b-3725-40c4-8da7-8146a59babb2', 'игры', NULL, '2025-04-17 01:50:07.295858', 1);
INSERT INTO public.chats VALUES ('0202fde4-9f71-44b7-a02d-3ba03cc83247', 'Чат', NULL, '2025-04-17 02:00:27.91317', 1);
INSERT INTO public.chats VALUES ('5733a5bd-6389-408a-a5c0-edebe3f49236', 'Чат', NULL, '2025-04-17 04:44:55.503848', 1);
INSERT INTO public.chats VALUES ('d10b9766-c36a-4f08-880c-774f1e0c2d5b', 'Чат', NULL, '2025-04-17 05:17:42.557159', 1);
INSERT INTO public.chats VALUES ('8f118829-caaf-4d30-837f-72bef7e2759a', 'Чат', NULL, '2025-04-17 16:35:24.748525', 1);
INSERT INTO public.chats VALUES ('4184cf72-160c-4f74-868e-bc14c20c0477', 'Work Chat', NULL, '2025-04-17 16:46:54.937427', 2);
INSERT INTO public.chats VALUES ('f3fb8f09-591f-417d-82eb-c8527c74c719', 'Test Chat', NULL, '2025-04-16 16:44:14.631981', 1);
INSERT INTO public.chats VALUES ('5a2b61b0-5e2e-4e8d-9b9b-fda5818e3370', 'Work Chat', NULL, '2025-04-17 17:16:36.1951', 2);
INSERT INTO public.chats VALUES ('cad0a461-fc18-4675-9cf5-1b5db56b13f4', 'пидоры', NULL, '2025-04-17 17:17:00.78794', 2);


--
-- TOC entry 5002 (class 0 OID 16807)
-- Dependencies: 220
-- Data for Name: event_participants; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5017 (class 0 OID 17078)
-- Dependencies: 237
-- Data for Name: event_statuses; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.event_statuses VALUES (1, 'PLANNED', 'Запланировано');
INSERT INTO public.event_statuses VALUES (2, 'ONGOING', 'В процессе');
INSERT INTO public.event_statuses VALUES (3, 'COMPLETED', 'Завершено');
INSERT INTO public.event_statuses VALUES (4, 'CANCELLED', 'Отменено');


--
-- TOC entry 5001 (class 0 OID 16793)
-- Dependencies: 219
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.events VALUES ('b0c16e70-d1e0-464c-ac64-e6faf3c2308b', 'Football Match', 'Weekly football game', '2025-05-01 18:00:00', NULL, NULL, 'Central Park', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '2025-04-16 03:38:36.701034', 1);
INSERT INTO public.events VALUES ('ef0b4022-eb25-4519-b993-f73930815c9d', 'Football Match', 'Weekly football game', '2025-05-01 18:00:00', NULL, NULL, 'Central Park', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '2025-04-16 16:57:18.400137', 1);
INSERT INTO public.events VALUES ('2671fcc6-ee2e-4d22-91b0-432c5ace77ec', 'Football Match', 'Weekly football game', '2025-05-01 18:00:00', NULL, NULL, 'Central Park', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '2025-04-16 18:07:23.175887', 1);
INSERT INTO public.events VALUES ('87bb8ebd-a154-4853-9a43-0559bb5106fe', 'Football матч', 'Weekly football game', '2025-05-01 18:00:00', 'FOOTBALL', '2025-05-01 20:00:00', 'Central Park', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-19 18:37:54.704155', 2);


--
-- TOC entry 5006 (class 0 OID 16884)
-- Dependencies: 224
-- Data for Name: friends; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5024 (class 0 OID 17136)
-- Dependencies: 244
-- Data for Name: message_attachments; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5005 (class 0 OID 16859)
-- Dependencies: 223
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.messages VALUES ('c1b705f7-84e2-47df-9daa-ea34a838c231', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'Hello, world!', '2025-04-16 16:50:39.686894', false, NULL, NULL, NULL);
INSERT INTO public.messages VALUES ('c8f47601-9979-4dda-8a48-d711bda44099', '9fc47c84-1e7c-490b-8af3-d542e9af942c', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'Hello, world!', '2025-04-16 19:09:55.583288', false, NULL, NULL, NULL);
INSERT INTO public.messages VALUES ('c036dd2f-ce08-47dd-8f3a-c9f5b6ac5b48', '9fc47c84-1e7c-490b-8af3-d542e9af942c', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'Hello, world!', '2025-04-16 19:11:26.168319', false, NULL, NULL, NULL);
INSERT INTO public.messages VALUES ('15855497-054b-408a-8fcb-ca30ba03110f', 'cba2ae3b-3725-40c4-8da7-8146a59babb2', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'Hello, buhs!', '2025-04-17 01:54:18.487958', false, NULL, NULL, NULL);
INSERT INTO public.messages VALUES ('8d5b219c-cd9b-4435-a6f5-2b00e8089087', '0202fde4-9f71-44b7-a02d-3ba03cc83247', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'Hello, buhs!', '2025-04-17 02:02:36.863775', false, NULL, NULL, NULL);
INSERT INTO public.messages VALUES ('2c794578-e822-4a41-b2f2-eea01170db4c', 'cad0a461-fc18-4675-9cf5-1b5db56b13f4', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'Hello World', '2025-04-17 19:04:31.597013', false, NULL, NULL, 'SENT');
INSERT INTO public.messages VALUES ('efab5d83-f3a3-41ea-9041-7d89a13144a6', 'cad0a461-fc18-4675-9cf5-1b5db56b13f4', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'Hello World', '2025-04-17 19:10:33.546827', false, NULL, NULL, 'SENT');
INSERT INTO public.messages VALUES ('d55b6566-dc5d-4803-a41e-f719c959ea35', 'cad0a461-fc18-4675-9cf5-1b5db56b13f4', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'Hello World', '2025-04-17 19:12:38.94299', false, NULL, NULL, 'SENT');
INSERT INTO public.messages VALUES ('80b89959-aa40-4ebc-b891-00443a3e5235', 'cad0a461-fc18-4675-9cf5-1b5db56b13f4', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'Hello World', '2025-04-17 19:15:32.971062', false, NULL, NULL, 'SENT');


--
-- TOC entry 5015 (class 0 OID 17034)
-- Dependencies: 235
-- Data for Name: notification; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.notification VALUES ('d3bb56d7-1c70-4840-ab2a-d56e49fb7dcf', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'New Message', 'You have a new message', 'MESSAGE', true, '2025-04-16 03:00:22.593233', 'CHAT', '3fa85f64-5717-4562-b3fc-2c963f66afa6');
INSERT INTO public.notification VALUES ('e6687fad-3410-4dd9-89a2-0cba135eb013', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'New Message', 'You have a new message', 'MESSAGE', false, '2025-04-16 17:33:06.887484', 'CHAT', '3fa85f64-5717-4562-b3fc-2c963f66afa6');
INSERT INTO public.notification VALUES ('b6edbfea-2904-4d56-85a5-9b964f180b6f', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'New Message', 'You have a new message', 'MESSAGE', false, '2025-04-16 23:27:09.173229', 'CHAT', '3fa85f64-5717-4562-b3fc-2c963f66afa6');
INSERT INTO public.notification VALUES ('7a4e1316-6844-413c-ae75-d83666eb8b79', 'f3fb8f09-591f-417d-82eb-c8527c74c719', 'New Message', 'You have a new message', 'MESSAGE', false, '2025-04-16 23:27:18.040154', 'CHAT', '3fa85f64-5717-4562-b3fc-2c963f66afa6');
INSERT INTO public.notification VALUES ('763f9c1d-b7f5-4be9-bbb5-f9e51c9b3afc', '5560d4f9-bffa-4572-a03b-a6b68362c718', 'New Message', 'You have a new message', 'MESSAGE', true, '2025-04-17 02:04:15.669371', 'CHAT', '3fa85f64-5717-4562-b3fc-2c963f66afa6');


--
-- TOC entry 5009 (class 0 OID 16925)
-- Dependencies: 227
-- Data for Name: recommendations; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5023 (class 0 OID 17118)
-- Dependencies: 243
-- Data for Name: relationship_statuses; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.relationship_statuses VALUES (1, 'PENDING', 'Pending status');
INSERT INTO public.relationship_statuses VALUES (2, 'ACCEPTED', 'Accepted status');
INSERT INTO public.relationship_statuses VALUES (3, 'DECLINED', 'Declined status');
INSERT INTO public.relationship_statuses VALUES (4, 'BLOCKED', 'Blocked status');


--
-- TOC entry 5021 (class 0 OID 17109)
-- Dependencies: 241
-- Data for Name: relationship_types; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.relationship_types VALUES (1, 'FRIEND', 'Friend relationship');
INSERT INTO public.relationship_types VALUES (2, 'FOLLOW', 'Following relationship');
INSERT INTO public.relationship_types VALUES (3, 'BLOCK', 'Block relationship');


--
-- TOC entry 5012 (class 0 OID 16979)
-- Dependencies: 230
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.role VALUES (1, 'ADMIN');
INSERT INTO public.role VALUES (2, 'USER');


--
-- TOC entry 5013 (class 0 OID 16983)
-- Dependencies: 232
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.status VALUES (1, 'ACTIVE');
INSERT INTO public.status VALUES (2, 'INACTIVE');


--
-- TOC entry 5010 (class 0 OID 16937)
-- Dependencies: 228
-- Data for Name: user_activities; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 5011 (class 0 OID 16951)
-- Dependencies: 229
-- Data for Name: user_photos; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.user_photos VALUES ('2435894a-1aa9-4520-90e7-a1aa5bef3014', '42542fc1-a797-4e28-ade2-a6feb5567718', 'http://example.com/photo.jpg', NULL, NULL, false);
INSERT INTO public.user_photos VALUES ('0496557f-c17b-4c27-a5d3-d831baccdfa7', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/5b8636e2-4b32-4c55-a046-fc12f5b7e64c.jpg', 'Новая фотография', NULL, false);
INSERT INTO public.user_photos VALUES ('2cea331d-d0be-4fac-8b4f-68f3d5278a33', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/40782e37-9d3d-48d2-80fb-165e52bf012b.jpg', 'Новая фотография', NULL, false);
INSERT INTO public.user_photos VALUES ('c0f9937c-84c5-474f-82b5-002883d1c785', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/aad4351a-4083-4e23-9fe7-92b8039537a6.jpg', 'Новая фотография', NULL, false);
INSERT INTO public.user_photos VALUES ('e3ef7017-39b7-45b7-8e3e-9ac5cda05cdc', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/58637366-f1d0-47bf-9d21-67a4f07d22b1.jpg', 'Новая фотография', NULL, false);
INSERT INTO public.user_photos VALUES ('998a7f70-a546-40a0-a496-7331e68df4cb', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/a251766b-bf07-41a7-b5d6-5c7c4e3e0f6a.jpg', 'Новая фотография', NULL, false);
INSERT INTO public.user_photos VALUES ('a36a39f6-bdf9-4f12-95f6-3e249cd527eb', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/0d93cbb5-067d-4241-8159-4c3c3f0738c8.jpg', 'Новая фотография', NULL, false);
INSERT INTO public.user_photos VALUES ('e86e6cf3-055a-4106-9172-dd9ca116906b', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/61d5d672-5e80-4e66-8194-cf0776f4e8a4.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('fe217557-5910-47dc-bc38-7e062b76ba8b', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/d7e900f1-dcca-4c73-85aa-154c7237429b.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('782aec0b-97b7-465a-b22d-bbadec1f187f', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/fe928b9c-9e03-49ee-8799-eb47bbfc34f0.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('11b99aee-0311-4b33-8f8f-d32644f05a4e', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/34f3fc3f-7a2a-4876-8058-54fb4734ec19.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('bce0d535-7b44-41f7-8681-b05c205e69d5', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/d43afe8d-0e0b-4ab8-9cf6-8719c9393f7b.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('de2e14cb-6d69-4b3c-b759-f2c57564dae8', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/7c90bb3d-8a69-44f9-ab0d-5724b7e1efc5.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('ece017a6-114b-4a22-9446-6dc045f3ace8', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/a0a25186-faaa-44f3-95ec-d58d66a2dd49.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('1ae0a8bc-04d7-441c-af6e-bc2ac0163c26', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/93facbb5-4f32-4e16-bb19-b83d6eeeda5b.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('22271932-c8b2-4227-a82e-62085622d8e4', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/6f36c9f6-d914-40e5-8b17-4a7e4b2e6bdb.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('b95c4704-3a5e-41d2-8988-dde9f07c301c', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/ff56ca8d-2ac4-4796-9bd5-8fea2881ff38.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('9107bfab-2335-4b8c-b813-ced2f0fe57c8', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/ecfa008d-223a-424d-ab15-d6a9a4c7fd86.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('a6943d9d-474f-4c00-bfec-bd14610f603b', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/805061f1-c017-4e06-b670-1bdca1a4434b.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('ed4aa6fe-76bf-4b21-a726-eda7afb7a8be', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/0799d494-9556-4321-8b22-e5c327086b3e.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('19e82d6e-ec18-4a07-b368-e4c6974e9c8a', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/feee9422-7873-4742-b2d4-12f84c25c58d.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('b798bd9f-3fd7-425e-b105-bb0fe79dba4e', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/42507d7b-1ed4-4ff2-8950-07ef67ec6162.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('ef0eea24-78db-4309-92b2-e7f840cf8635', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/726b9318-e38d-4587-bca2-c5790a012963.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('46320741-77f4-4d4b-9ead-56b7f5ab4d25', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/456b3507-7e2c-4cf3-bf81-4441fa8587ce.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('16fa1922-438e-43af-a440-d13c773811b6', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/81c344b4-1dbc-4afc-87a0-d8fe133f3d44.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('7029b230-8c7c-4c8e-85aa-e4511bd893f2', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/4b6ff98e-5ffd-4edd-80e2-25cf6174ea12.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('15cc1135-5b00-43f0-aea9-128c27efcc8a', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/d467831a-766f-47b5-be2a-c6a8731d11fe.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('b974e43b-14fc-40c6-aa8f-136df5259070', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/578c4f51-66b0-4d97-a24c-6b354b46569e.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('e62b7b95-2efe-4529-acad-4b98151516e3', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/8c729e85-b479-4753-a744-4a922a5fee1a.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('aa52c644-fa92-47b5-9411-cb9afd776d48', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/063c5863-cade-476a-a359-619f6706c16c.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('aeef392a-7e31-463f-8287-c9936ff27efb', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/09a289e3-37ae-4a42-bd46-5dc711c781e9.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('0829b500-4e3f-447c-83dd-671f949e6706', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e84b4083-a1cc-486b-8c4a-94acf100eea5.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('6ed21397-280e-4a15-b74c-177fcc1f2305', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/3e3e539b-50e8-46e7-9964-a8ca513436d9.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('4be61d36-9d71-4768-b2d2-bf7111a630d3', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e4ff975c-56db-40a4-a3b9-f6e638e64cda.jpg', 'Новое фото', NULL, true);
INSERT INTO public.user_photos VALUES ('f535bb79-46ed-41df-9b45-66d5a9d2a2a2', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/1946c147-3034-43dc-b892-d1adc398bc1d.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('dfe1fa8d-7f86-4aaa-b6dd-e511693954e7', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e0fb6bbc-192e-4593-b3cf-e75171f6e7be.jpg', 'Новое фото', NULL, false);
INSERT INTO public.user_photos VALUES ('07c9a5e3-60e4-466f-b211-61ded47bf565', '42542fc1-a797-4e28-ade2-a6feb5567718', '/api/files/42542fc1-a797-4e28-ade2-a6feb5567718/a53db720-902a-46b5-af36-9fb327f27bb3.jpg', 'Новое фото', NULL, true);


--
-- TOC entry 5000 (class 0 OID 16780)
-- Dependencies: 218
-- Data for Name: user_profiles; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.user_profiles VALUES ('42542fc1-a797-4e28-ade2-a6feb5567718', 'Олег', 'Тенькофф', 'Богатый уебок в мире', 'Берлин', NULL, '42542fc1-a797-4e28-ade2-a6feb5567718/7f15653f-501a-446a-ba75-4bb0d8620943.jpg', 'Футболл', '2025-04-19 18:19:07.161545', '2025-04-19 21:00:19.261709');
INSERT INTO public.user_profiles VALUES ('f3fb8f09-591f-417d-82eb-c8527c74c719', 'Дмитрий', 'Антипин', 'Sports enthusias', 'Поставы', 'https://github.com/DmitryAntipin151002', 'e2425a97-288c-4a45-bca4-25d40987ef4c.jpg', 'Футбол', '2025-04-16 02:32:49.800068', '2025-04-20 19:36:29.943313');
INSERT INTO public.user_profiles VALUES ('5560d4f9-bffa-4572-a03b-a6b68362c718', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-04-20 23:12:53.7631', NULL);


--
-- TOC entry 5008 (class 0 OID 16905)
-- Dependencies: 226
-- Data for Name: user_relationships; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.user_relationships VALUES (5, '42542fc1-a797-4e28-ade2-a6feb5567718', '5560d4f9-bffa-4572-a03b-a6b68362c718', '2025-04-19 19:46:30.848012', '2025-04-19 19:46:30.848012', 1, 1);
INSERT INTO public.user_relationships VALUES (6, '42542fc1-a797-4e28-ade2-a6feb5567718', '5560d4f9-bffa-4572-a03b-a6b68362c718', '2025-04-19 19:49:43.468816', '2025-04-19 19:49:43.468816', 1, 1);
INSERT INTO public.user_relationships VALUES (7, '42542fc1-a797-4e28-ade2-a6feb5567718', '5560d4f9-bffa-4572-a03b-a6b68362c718', '2025-04-19 19:49:51.246879', '2025-04-19 19:49:51.246879', 1, 1);
INSERT INTO public.user_relationships VALUES (8, '42542fc1-a797-4e28-ade2-a6feb5567718', '5560d4f9-bffa-4572-a03b-a6b68362c718', '2025-04-19 19:52:55.161384', '2025-04-19 19:52:55.161384', 1, 1);
INSERT INTO public.user_relationships VALUES (10, '42542fc1-a797-4e28-ade2-a6feb5567718', '5560d4f9-bffa-4572-a03b-a6b68362c718', '2025-04-19 19:58:18.886659', '2025-04-19 19:58:18.886659', 1, 1);
INSERT INTO public.user_relationships VALUES (12, 'f3fb8f09-591f-417d-82eb-c8527c74c719', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-20 22:14:44.020598', '2025-04-20 23:14:56.855197', 1, 3);
INSERT INTO public.user_relationships VALUES (13, 'f3fb8f09-591f-417d-82eb-c8527c74c719', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-20 22:44:51.820736', '2025-04-20 23:14:58.626253', 1, 3);
INSERT INTO public.user_relationships VALUES (14, 'f3fb8f09-591f-417d-82eb-c8527c74c719', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-20 23:13:43.887628', '2025-04-20 23:14:59.355343', 1, 3);
INSERT INTO public.user_relationships VALUES (15, 'f3fb8f09-591f-417d-82eb-c8527c74c719', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-20 23:14:04.65431', '2025-04-20 23:14:59.910825', 1, 3);
INSERT INTO public.user_relationships VALUES (16, 'f3fb8f09-591f-417d-82eb-c8527c74c719', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-20 23:35:26.546442', '2025-04-20 23:35:36.985032', 1, 2);
INSERT INTO public.user_relationships VALUES (17, 'f3fb8f09-591f-417d-82eb-c8527c74c719', '42542fc1-a797-4e28-ade2-a6feb5567718', '2025-04-20 23:35:55.609229', '2025-04-20 23:36:02.908098', 1, 3);


--
-- TOC entry 4999 (class 0 OID 16765)
-- Dependencies: 217
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES ('f3fb8f09-591f-417d-82eb-c8527c74c719', 'dmitryantipin@gmail.com', NULL, '$2a$10$Hm18XyMTV903gXijf.1DLexCkfEIxtrQPLwrjApfJrpnKex2GyvyW', 1, true, NULL, '2025-04-16 01:07:23.708074', '2025-04-16 01:07:23.631507', '2025-04-16 01:07:23.631507', 1);
INSERT INTO public.users VALUES ('5560d4f9-bffa-4572-a03b-a6b68362c718', 'antoxa@gmail.com', NULL, '$2a$10$TvHb7uVBPyycI7qUUV5s6esGl.UTw2jx99cqJWv6CzhZVgeXR856y', 1, true, NULL, '2025-04-16 17:16:06.196155', '2025-04-16 17:16:05.937114', '2025-04-16 17:16:05.937114', 1);
INSERT INTO public.users VALUES ('42542fc1-a797-4e28-ade2-a6feb5567718', 'dmitryantipin1@gmail.com', NULL, '$2a$10$H6gkqek8ZE8ao8AVPxUBreRxOTm3dE2cwLAuoNoox/Cqv1HbmLoOe', 2, true, NULL, '2025-04-19 18:15:17.547995', '2025-04-19 18:15:17.399897', '2025-04-19 18:15:17.399897', 1);


--
-- TOC entry 5014 (class 0 OID 16987)
-- Dependencies: 234
-- Data for Name: verification_code; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.verification_code VALUES ('a339ee62-ba3d-47bc-8bb2-ecf52604e377', '5560d4f9-bffa-4572-a03b-a6b68362c718', '964496', '2025-04-16 17:17:09.68878', '2025-04-16 17:22:09.68878');
INSERT INTO public.verification_code VALUES ('c81f905e-802e-4008-8519-e49be992d3cd', '42542fc1-a797-4e28-ade2-a6feb5567718', '593181', '2025-04-19 18:15:45.394061', '2025-04-19 18:20:45.394061');
INSERT INTO public.verification_code VALUES ('6dd7dbab-ccea-4ff4-9977-2077312526b8', 'f3fb8f09-591f-417d-82eb-c8527c74c719', '665183', '2025-04-20 19:35:33.25087', '2025-04-20 19:40:33.25087');


--
-- TOC entry 5036 (class 0 OID 0)
-- Dependencies: 238
-- Name: chat_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.chat_types_id_seq', 3, true);


--
-- TOC entry 5037 (class 0 OID 0)
-- Dependencies: 236
-- Name: event_statuses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.event_statuses_id_seq', 4, true);


--
-- TOC entry 5038 (class 0 OID 0)
-- Dependencies: 242
-- Name: relationship_statuses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.relationship_statuses_id_seq', 4, true);


--
-- TOC entry 5039 (class 0 OID 0)
-- Dependencies: 240
-- Name: relationship_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.relationship_types_id_seq', 3, true);


--
-- TOC entry 5040 (class 0 OID 0)
-- Dependencies: 225
-- Name: user_relationships_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_relationships_id_seq', 17, true);


--
-- TOC entry 4780 (class 2606 OID 16846)
-- Name: chat_participants chat_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_pkey PRIMARY KEY (id);


--
-- TOC entry 4782 (class 2606 OID 16848)
-- Name: chat_participants chat_participants_user_id_chat_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_chat_id_key UNIQUE (user_id, chat_id);


--
-- TOC entry 4818 (class 2606 OID 17102)
-- Name: chat_types chat_types_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_name_key UNIQUE (name);


--
-- TOC entry 4820 (class 2606 OID 17100)
-- Name: chat_types chat_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4778 (class 2606 OID 16834)
-- Name: chats chats_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);


--
-- TOC entry 4772 (class 2606 OID 16815)
-- Name: event_participants event_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_pkey PRIMARY KEY (id);


--
-- TOC entry 4774 (class 2606 OID 16817)
-- Name: event_participants event_participants_user_id_event_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_event_id_key UNIQUE (user_id, event_id);


--
-- TOC entry 4814 (class 2606 OID 17085)
-- Name: event_statuses event_statuses_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_code_key UNIQUE (code);


--
-- TOC entry 4816 (class 2606 OID 17083)
-- Name: event_statuses event_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_pkey PRIMARY KEY (id);


--
-- TOC entry 4768 (class 2606 OID 16801)
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- TOC entry 4790 (class 2606 OID 16891)
-- Name: friends friends_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (id);


--
-- TOC entry 4792 (class 2606 OID 16893)
-- Name: friends friends_user_id_friend_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_friend_id_key UNIQUE (user_id, friend_id);


--
-- TOC entry 4830 (class 2606 OID 17140)
-- Name: message_attachments message_attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_pkey PRIMARY KEY (id);


--
-- TOC entry 4788 (class 2606 OID 16868)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- TOC entry 4812 (class 2606 OID 17041)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 4809 (class 2606 OID 17021)
-- Name: role pk_role; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT pk_role PRIMARY KEY (id);


--
-- TOC entry 4801 (class 2606 OID 16931)
-- Name: recommendations recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_pkey PRIMARY KEY (id);


--
-- TOC entry 4826 (class 2606 OID 17125)
-- Name: relationship_statuses relationship_statuses_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_name_key UNIQUE (name);


--
-- TOC entry 4828 (class 2606 OID 17123)
-- Name: relationship_statuses relationship_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_pkey PRIMARY KEY (id);


--
-- TOC entry 4822 (class 2606 OID 17116)
-- Name: relationship_types relationship_types_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_name_key UNIQUE (name);


--
-- TOC entry 4824 (class 2606 OID 17114)
-- Name: relationship_types relationship_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4804 (class 2606 OID 16945)
-- Name: user_activities user_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_pkey PRIMARY KEY (id);


--
-- TOC entry 4807 (class 2606 OID 16960)
-- Name: user_photos user_photos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_pkey PRIMARY KEY (id);


--
-- TOC entry 4766 (class 2606 OID 16787)
-- Name: user_profiles user_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4798 (class 2606 OID 16912)
-- Name: user_relationships user_relationships_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_pkey PRIMARY KEY (id);


--
-- TOC entry 4760 (class 2606 OID 16777)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4762 (class 2606 OID 16779)
-- Name: users users_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_number_key UNIQUE (phone_number);


--
-- TOC entry 4764 (class 2606 OID 16775)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4783 (class 1259 OID 16969)
-- Name: idx_chat_participants_chat; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_chat_participants_chat ON public.chat_participants USING btree (chat_id);


--
-- TOC entry 4784 (class 1259 OID 16968)
-- Name: idx_chat_participants_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_chat_participants_user ON public.chat_participants USING btree (user_id);


--
-- TOC entry 4775 (class 1259 OID 16967)
-- Name: idx_event_participants_event; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_event_participants_event ON public.event_participants USING btree (event_id);


--
-- TOC entry 4776 (class 1259 OID 16966)
-- Name: idx_event_participants_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_event_participants_user ON public.event_participants USING btree (user_id);


--
-- TOC entry 4769 (class 1259 OID 17068)
-- Name: idx_events_organizer; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_events_organizer ON public.events USING btree (organizer_id);


--
-- TOC entry 4770 (class 1259 OID 17091)
-- Name: idx_events_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_events_status ON public.events USING btree (status);


--
-- TOC entry 4793 (class 1259 OID 16973)
-- Name: idx_friends_friend; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_friends_friend ON public.friends USING btree (friend_id);


--
-- TOC entry 4794 (class 1259 OID 16972)
-- Name: idx_friends_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_friends_user ON public.friends USING btree (user_id);


--
-- TOC entry 4785 (class 1259 OID 16970)
-- Name: idx_messages_chat; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_messages_chat ON public.messages USING btree (chat_id);


--
-- TOC entry 4786 (class 1259 OID 16971)
-- Name: idx_messages_sender; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_messages_sender ON public.messages USING btree (sender_id);


--
-- TOC entry 4810 (class 1259 OID 17047)
-- Name: idx_notification_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notification_user ON public.notification USING btree (user_id);


--
-- TOC entry 4799 (class 1259 OID 16976)
-- Name: idx_recommendations_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_recommendations_user ON public.recommendations USING btree (user_id);


--
-- TOC entry 4802 (class 1259 OID 16977)
-- Name: idx_user_activities_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_activities_user ON public.user_activities USING btree (user_id);


--
-- TOC entry 4805 (class 1259 OID 16978)
-- Name: idx_user_photos_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_photos_user ON public.user_photos USING btree (user_id);


--
-- TOC entry 4795 (class 1259 OID 16975)
-- Name: idx_user_relationships_related; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_relationships_related ON public.user_relationships USING btree (related_user_id);


--
-- TOC entry 4796 (class 1259 OID 16974)
-- Name: idx_user_relationships_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_relationships_user ON public.user_relationships USING btree (user_id);


--
-- TOC entry 4838 (class 2606 OID 16854)
-- Name: chat_participants chat_participants_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;


--
-- TOC entry 4839 (class 2606 OID 16849)
-- Name: chat_participants chat_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4836 (class 2606 OID 16835)
-- Name: chats chats_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


--
-- TOC entry 4834 (class 2606 OID 16823)
-- Name: event_participants event_participants_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


--
-- TOC entry 4835 (class 2606 OID 16818)
-- Name: event_participants event_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4832 (class 2606 OID 16802)
-- Name: events events_organizer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_organizer_id_fkey FOREIGN KEY (organizer_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4837 (class 2606 OID 17103)
-- Name: chats fk_chat_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT fk_chat_type FOREIGN KEY (type_id) REFERENCES public.chat_types(id) ON DELETE RESTRICT;


--
-- TOC entry 4833 (class 2606 OID 17086)
-- Name: events fk_event_status; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT fk_event_status FOREIGN KEY (status) REFERENCES public.event_statuses(id);


--
-- TOC entry 4843 (class 2606 OID 16899)
-- Name: friends friends_friend_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_friend_id_fkey FOREIGN KEY (friend_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4844 (class 2606 OID 16894)
-- Name: friends friends_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4853 (class 2606 OID 17141)
-- Name: message_attachments message_attachments_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE;


--
-- TOC entry 4840 (class 2606 OID 16869)
-- Name: messages messages_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;


--
-- TOC entry 4841 (class 2606 OID 16879)
-- Name: messages messages_reply_to_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_reply_to_id_fkey FOREIGN KEY (reply_to_id) REFERENCES public.messages(id) ON DELETE SET NULL;


--
-- TOC entry 4842 (class 2606 OID 16874)
-- Name: messages messages_sender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4852 (class 2606 OID 17042)
-- Name: notification notification_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4849 (class 2606 OID 16932)
-- Name: recommendations recommendations_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4850 (class 2606 OID 16946)
-- Name: user_activities user_activities_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4851 (class 2606 OID 16961)
-- Name: user_photos user_photos_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4831 (class 2606 OID 16788)
-- Name: user_profiles user_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4845 (class 2606 OID 16920)
-- Name: user_relationships user_relationships_related_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_related_user_id_fkey FOREIGN KEY (related_user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4846 (class 2606 OID 17131)
-- Name: user_relationships user_relationships_status_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_status_id_fkey FOREIGN KEY (status_id) REFERENCES public.relationship_statuses(id);


--
-- TOC entry 4847 (class 2606 OID 17126)
-- Name: user_relationships user_relationships_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.relationship_types(id);


--
-- TOC entry 4848 (class 2606 OID 16915)
-- Name: user_relationships user_relationships_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


-- Completed on 2025-04-20 23:45:51

--
-- PostgreSQL database dump complete
--

