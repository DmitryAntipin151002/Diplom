--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.2

-- Started on 2025-05-06 21:16:29

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
-- TOC entry 873 (class 1247 OID 16699)
-- Name: chat_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.chat_type AS ENUM (
    'PRIVATE',
    'GROUP',
    'EVENT'
);


ALTER TYPE public.chat_type OWNER TO postgres;

--
-- TOC entry 942 (class 1247 OID 17058)
-- Name: event_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.event_status AS ENUM (
    'PLANNED',
    'ONGOING',
    'COMPLETED',
    'CANCELLED'
);


ALTER TYPE public.event_status OWNER TO postgres;

--
-- TOC entry 882 (class 1247 OID 16730)
-- Name: friend_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.friend_status AS ENUM (
    'PENDING',
    'ACCEPTED',
    'REJECTED',
    'BLOCKED'
);


ALTER TYPE public.friend_status OWNER TO postgres;

--
-- TOC entry 879 (class 1247 OID 16714)
-- Name: participant_role; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.participant_role AS ENUM (
    'ORGANIZER',
    'PARTICIPANT'
);


ALTER TYPE public.participant_role OWNER TO postgres;

--
-- TOC entry 876 (class 1247 OID 16706)
-- Name: participant_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.participant_status AS ENUM (
    'PENDING',
    'JOINED',
    'REJECTED'
);


ALTER TYPE public.participant_status OWNER TO postgres;

--
-- TOC entry 885 (class 1247 OID 16740)
-- Name: recommendation_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.recommendation_type AS ENUM (
    'EVENT',
    'USER',
    'COMMUNITY'
);


ALTER TYPE public.recommendation_type OWNER TO postgres;

--
-- TOC entry 888 (class 1247 OID 16748)
-- Name: relationship_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.relationship_status AS ENUM (
    'PENDING',
    'ACCEPTED',
    'DECLINED',
    'BLOCKED'
);


ALTER TYPE public.relationship_status OWNER TO postgres;

--
-- TOC entry 891 (class 1247 OID 16758)
-- Name: relationship_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.relationship_type AS ENUM (
    'FRIEND',
    'FOLLOW',
    'BLOCK'
);


ALTER TYPE public.relationship_type OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16840)
-- Name: chat_participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    chat_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_read_at timestamp without time zone,
    is_admin boolean DEFAULT false
);


ALTER TABLE public.chat_participants OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 17095)
-- Name: chat_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.chat_types OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 17094)
-- Name: chat_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chat_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.chat_types_id_seq OWNER TO postgres;

--
-- TOC entry 5061 (class 0 OID 0)
-- Dependencies: 238
-- Name: chat_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chat_types_id_seq OWNED BY public.chat_types.id;


--
-- TOC entry 221 (class 1259 OID 16828)
-- Name: chats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chats (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255),
    event_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type_id bigint,
    creator_id uuid
);


ALTER TABLE public.chats OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16807)
-- Name: event_participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.event_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    event_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status public.participant_status DEFAULT 'PENDING'::public.participant_status NOT NULL,
    role public.participant_role DEFAULT 'PARTICIPANT'::public.participant_role NOT NULL
);


ALTER TABLE public.event_participants OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17078)
-- Name: event_statuses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.event_statuses (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.event_statuses OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17077)
-- Name: event_statuses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.event_statuses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.event_statuses_id_seq OWNER TO postgres;

--
-- TOC entry 5062 (class 0 OID 0)
-- Dependencies: 236
-- Name: event_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.event_statuses_id_seq OWNED BY public.event_statuses.id;


--
-- TOC entry 219 (class 1259 OID 16793)
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.events OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16884)
-- Name: friends; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.friends (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    friend_id uuid NOT NULL,
    status public.friend_status DEFAULT 'PENDING'::public.friend_status NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.friends OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 17136)
-- Name: message_attachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_attachments (
    id uuid NOT NULL,
    message_id uuid NOT NULL,
    file_url character varying(255) NOT NULL,
    file_name character varying(100) NOT NULL,
    file_type character varying(50) NOT NULL,
    file_size bigint NOT NULL
);


ALTER TABLE public.message_attachments OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16859)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.messages OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17034)
-- Name: notification; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.notification OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16925)
-- Name: recommendations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recommendations (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    type public.recommendation_type NOT NULL,
    recommended_id uuid NOT NULL,
    score double precision,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.recommendations OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 17118)
-- Name: relationship_statuses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.relationship_statuses (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.relationship_statuses OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 17117)
-- Name: relationship_statuses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.relationship_statuses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.relationship_statuses_id_seq OWNER TO postgres;

--
-- TOC entry 5063 (class 0 OID 0)
-- Dependencies: 242
-- Name: relationship_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.relationship_statuses_id_seq OWNED BY public.relationship_statuses.id;


--
-- TOC entry 241 (class 1259 OID 17109)
-- Name: relationship_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.relationship_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.relationship_types OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 17108)
-- Name: relationship_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.relationship_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.relationship_types_id_seq OWNER TO postgres;

--
-- TOC entry 5064 (class 0 OID 0)
-- Dependencies: 240
-- Name: relationship_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.relationship_types_id_seq OWNED BY public.relationship_types.id;


--
-- TOC entry 230 (class 1259 OID 16979)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16982)
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.role_id_seq OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 16983)
-- Name: status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.status (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.status OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16986)
-- Name: status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.status_id_seq OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16937)
-- Name: user_activities; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.user_activities OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16951)
-- Name: user_photos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_photos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    photo_url character varying(255) NOT NULL,
    description text,
    upload_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_profile_photo boolean DEFAULT false NOT NULL
);


ALTER TABLE public.user_photos OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16780)
-- Name: user_profiles; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.user_profiles OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16905)
-- Name: user_relationships; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.user_relationships OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16904)
-- Name: user_relationships_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_relationships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_relationships_id_seq OWNER TO postgres;

--
-- TOC entry 5065 (class 0 OID 0)
-- Dependencies: 225
-- Name: user_relationships_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_relationships_id_seq OWNED BY public.user_relationships.id;


--
-- TOC entry 217 (class 1259 OID 16765)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 16987)
-- Name: verification_code; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.verification_code (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    code character varying(6) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    expires_at timestamp without time zone NOT NULL
);


ALTER TABLE public.verification_code OWNER TO postgres;

--
-- TOC entry 4785 (class 2604 OID 17098)
-- Name: chat_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_types ALTER COLUMN id SET DEFAULT nextval('public.chat_types_id_seq'::regclass);


--
-- TOC entry 4784 (class 2604 OID 17081)
-- Name: event_statuses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_statuses ALTER COLUMN id SET DEFAULT nextval('public.event_statuses_id_seq'::regclass);


--
-- TOC entry 4787 (class 2604 OID 17121)
-- Name: relationship_statuses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_statuses ALTER COLUMN id SET DEFAULT nextval('public.relationship_statuses_id_seq'::regclass);


--
-- TOC entry 4786 (class 2604 OID 17112)
-- Name: relationship_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_types ALTER COLUMN id SET DEFAULT nextval('public.relationship_types_id_seq'::regclass);


--
-- TOC entry 4773 (class 2604 OID 16908)
-- Name: user_relationships id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships ALTER COLUMN id SET DEFAULT nextval('public.user_relationships_id_seq'::regclass);


--
-- TOC entry 5033 (class 0 OID 16840)
-- Dependencies: 222
-- Data for Name: chat_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_participants (id, user_id, chat_id, joined_at, last_read_at, is_admin) FROM stdin;
da00902b-7a4e-461b-a087-81895def05e8	f3fb8f09-591f-417d-82eb-c8527c74c719	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 16:51:20.409528	\N	f
e82329e7-6038-4848-91ad-5ce9cf1f9781	f3fb8f09-591f-417d-82eb-c8527c74c719	6170175d-748d-4079-b400-d4096903c461	2025-04-16 19:06:50.436154	2025-04-16 19:06:50.436154	f
36b2b3e4-c610-4ee4-8b84-71d217dcc879	f3fb8f09-591f-417d-82eb-c8527c74c719	9fc47c84-1e7c-490b-8af3-d542e9af942c	2025-04-16 19:07:44.215814	2025-04-16 19:07:44.215814	f
1c8a4d55-84c9-41bb-b58d-bcdeda49ef3c	f3fb8f09-591f-417d-82eb-c8527c74c719	c0050a07-46ab-4811-a41e-8a7ba10baad7	2025-04-16 20:48:10.024247	2025-04-16 20:48:10.024247	f
41c8c9b0-2c5a-4989-a7b1-c8be258d9bc7	f3fb8f09-591f-417d-82eb-c8527c74c719	cba2ae3b-3725-40c4-8da7-8146a59babb2	2025-04-17 01:50:06.688865	2025-04-17 01:50:06.688865	f
033c82a4-d70b-4f73-8423-bcf9079cc08e	5560d4f9-bffa-4572-a03b-a6b68362c718	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-17 01:52:23.17341	\N	f
71a989fe-2292-4968-aeaf-96c2fb3e269b	f3fb8f09-591f-417d-82eb-c8527c74c719	0202fde4-9f71-44b7-a02d-3ba03cc83247	2025-04-17 02:00:27.910168	2025-04-17 02:00:27.910168	f
6e2b2a8e-1de4-4f38-b2cd-cd5345dd23af	5560d4f9-bffa-4572-a03b-a6b68362c718	0202fde4-9f71-44b7-a02d-3ba03cc83247	2025-04-17 02:01:10.177642	\N	f
fcec0149-2217-4a30-9d12-02b484e6407a	f3fb8f09-591f-417d-82eb-c8527c74c719	5733a5bd-6389-408a-a5c0-edebe3f49236	2025-04-17 04:44:55.50285	\N	f
73a365c8-837c-4e87-b18a-3f8e1ce10954	f3fb8f09-591f-417d-82eb-c8527c74c719	d10b9766-c36a-4f08-880c-774f1e0c2d5b	2025-04-17 05:17:42.470146	\N	f
44f0b0ac-594a-40d1-893b-7e6012e44dad	f3fb8f09-591f-417d-82eb-c8527c74c719	8f118829-caaf-4d30-837f-72bef7e2759a	2025-04-17 16:35:24.706526	\N	f
a09c044f-7502-4117-b788-256f1d28999e	5560d4f9-bffa-4572-a03b-a6b68362c718	4184cf72-160c-4f74-868e-bc14c20c0477	2025-04-17 16:46:54.935397	\N	f
a716fe71-60b7-46fa-b6ee-cc0d8a6595bd	f3fb8f09-591f-417d-82eb-c8527c74c719	5a2b61b0-5e2e-4e8d-9b9b-fda5818e3370	2025-04-17 17:16:36.191134	\N	f
0dfa53c4-1129-41ad-a2bf-0a33023cbbc4	f3fb8f09-591f-417d-82eb-c8527c74c719	cad0a461-fc18-4675-9cf5-1b5db56b13f4	2025-04-17 17:17:00.786941	\N	f
c11874bc-d109-4891-bad6-4bc6f352fa4e	5560d4f9-bffa-4572-a03b-a6b68362c718	cad0a461-fc18-4675-9cf5-1b5db56b13f4	2025-04-17 17:18:46.925521	\N	f
cb9e44f9-4d74-4398-b37b-f0cd0c4dc7eb	f3fb8f09-591f-417d-82eb-c8527c74c719	19c8e052-34ab-4d06-b97d-0f0466d35677	2025-04-30 22:03:48.680563	\N	f
cc7772e4-9538-415c-86cc-ac907201fcf3	42542fc1-a797-4e28-ade2-a6feb5567718	19c8e052-34ab-4d06-b97d-0f0466d35677	2025-04-30 22:03:48.682557	\N	f
b4644647-a62d-4f7a-be52-443498c20b83	42542fc1-a797-4e28-ade2-a6feb5567718	879681ca-57cb-4363-81f4-cfc25a42be81	2025-05-01 12:39:53.41432	\N	f
b79852f4-c684-4398-a598-2c8fc8832874	42542fc1-a797-4e28-ade2-a6feb5567718	03617871-6c20-4725-9073-41c558bc851f	2025-05-01 12:45:23.921478	\N	f
bddf59e3-9c33-4557-8385-1002dcc27dc3	42542fc1-a797-4e28-ade2-a6feb5567718	7ad9575a-ef4e-438b-9509-c472e8969655	2025-05-01 12:48:39.326065	\N	f
5091a4a8-b9cb-40ae-8948-5b16036bf609	42542fc1-a797-4e28-ade2-a6feb5567718	cbc37b70-6ea0-4403-8009-5d810b22b880	2025-05-01 12:51:11.779575	\N	f
bd8873e3-e0e2-480d-ac9f-4793b9eac3c0	42542fc1-a797-4e28-ade2-a6feb5567718	b83c4879-435d-4550-aad3-077a01299036	2025-05-01 13:01:31.006581	\N	f
b4dec8f1-551b-4c2d-9383-05a73af550e6	42542fc1-a797-4e28-ade2-a6feb5567718	e6276ffa-b259-4643-ba9f-424b66ef555a	2025-05-01 13:01:56.973827	\N	f
1c58d271-8a7f-4094-8cae-a37aab70d011	f3fb8f09-591f-417d-82eb-c8527c74c719	629693eb-d2ab-4a4e-a9af-32f3c54817e9	2025-05-01 14:52:16.521716	\N	t
03ceda4f-60a8-4cef-9235-7180de7b72bb	42542fc1-a797-4e28-ade2-a6feb5567718	629693eb-d2ab-4a4e-a9af-32f3c54817e9	2025-05-01 14:52:16.524718	\N	f
0f9264cf-63d0-4a8e-8bf5-51e93c454ada	f3fb8f09-591f-417d-82eb-c8527c74c719	92ba584a-1f67-4802-9ec7-7da5dee7b662	2025-05-01 17:19:25.675972	\N	t
51a4953a-64ef-4e80-be03-ece67f57895c	42542fc1-a797-4e28-ade2-a6feb5567718	92ba584a-1f67-4802-9ec7-7da5dee7b662	2025-05-01 17:19:25.680974	\N	f
73eb5575-887c-4bfe-ae9c-09777b766c8c	f3fb8f09-591f-417d-82eb-c8527c74c719	427ac7a2-83a3-4ff8-bd88-135623516b09	2025-05-01 17:54:43.623897	\N	t
149b7f75-81f3-442d-8111-ec987f4ef587	42542fc1-a797-4e28-ade2-a6feb5567718	427ac7a2-83a3-4ff8-bd88-135623516b09	2025-05-01 17:54:43.626897	\N	f
6d43788b-0fc5-4a6e-8b31-16f0ff4e3e3a	f3fb8f09-591f-417d-82eb-c8527c74c719	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	2025-05-01 17:56:00.296094	\N	t
220c5831-2ba1-4daf-8a4f-0b594dcdaec8	42542fc1-a797-4e28-ade2-a6feb5567718	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	2025-05-01 17:56:00.297094	\N	f
dc055dbd-8bd8-4748-874d-df267b7886c9	f3fb8f09-591f-417d-82eb-c8527c74c719	018a8053-1ae6-410c-920a-b55dfa38859c	2025-05-04 00:31:47.468259	\N	t
a9b606b1-9dbf-4673-acd8-d1a1274eb701	42542fc1-a797-4e28-ade2-a6feb5567718	018a8053-1ae6-410c-920a-b55dfa38859c	2025-05-04 00:31:47.471227	\N	f
\.


--
-- TOC entry 5050 (class 0 OID 17095)
-- Dependencies: 239
-- Data for Name: chat_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_types (id, name, description) FROM stdin;
1	PRIVATE	Private chat between two users
2	GROUP	Group chat for multiple users
3	EVENT	Event-related chat
\.


--
-- TOC entry 5032 (class 0 OID 16828)
-- Dependencies: 221
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chats (id, name, event_id, created_at, type_id, creator_id) FROM stdin;
6170175d-748d-4079-b400-d4096903c461	Test Chat	\N	2025-04-16 19:06:51.037051	2	\N
9fc47c84-1e7c-490b-8af3-d542e9af942c	Test Chat	\N	2025-04-16 19:07:44.216783	1	\N
c0050a07-46ab-4811-a41e-8a7ba10baad7	Test Chat	\N	2025-04-16 20:48:10.041249	1	\N
cba2ae3b-3725-40c4-8da7-8146a59babb2	игры	\N	2025-04-17 01:50:07.295858	1	\N
0202fde4-9f71-44b7-a02d-3ba03cc83247	Чат	\N	2025-04-17 02:00:27.91317	1	\N
5733a5bd-6389-408a-a5c0-edebe3f49236	Чат	\N	2025-04-17 04:44:55.503848	1	\N
d10b9766-c36a-4f08-880c-774f1e0c2d5b	Чат	\N	2025-04-17 05:17:42.557159	1	\N
8f118829-caaf-4d30-837f-72bef7e2759a	Чат	\N	2025-04-17 16:35:24.748525	1	\N
4184cf72-160c-4f74-868e-bc14c20c0477	Work Chat	\N	2025-04-17 16:46:54.937427	2	\N
f3fb8f09-591f-417d-82eb-c8527c74c719	Test Chat	\N	2025-04-16 16:44:14.631981	1	\N
5a2b61b0-5e2e-4e8d-9b9b-fda5818e3370	Work Chat	\N	2025-04-17 17:16:36.1951	2	\N
cad0a461-fc18-4675-9cf5-1b5db56b13f4	пидоры	\N	2025-04-17 17:17:00.78794	2	\N
19c8e052-34ab-4d06-b97d-0f0466d35677	тест 2х пользователей	\N	2025-04-30 22:03:48.783236	2	\N
879681ca-57cb-4363-81f4-cfc25a42be81	Chat with 1 friend	\N	2025-05-01 12:39:53.419318	1	\N
03617871-6c20-4725-9073-41c558bc851f	Chat with 1 friend	\N	2025-05-01 12:45:23.929481	1	\N
7ad9575a-ef4e-438b-9509-c472e8969655	bhvfgghfvh	\N	2025-05-01 12:48:39.326064	1	\N
cbc37b70-6ea0-4403-8009-5d810b22b880	fbfbfbfbfbb f	\N	2025-05-01 12:51:11.786574	1	\N
b83c4879-435d-4550-aad3-077a01299036	yygg	\N	2025-05-01 13:01:31.015579	1	\N
e6276ffa-b259-4643-ba9f-424b66ef555a	ffff	\N	2025-05-01 13:01:56.974825	1	\N
629693eb-d2ab-4a4e-a9af-32f3c54817e9	нннннн	\N	2025-05-01 14:52:16.537715	1	\N
92ba584a-1f67-4802-9ec7-7da5dee7b662	гггггг	\N	2025-05-01 17:19:25.687974	1	\N
427ac7a2-83a3-4ff8-bd88-135623516b09	пррппп	\N	2025-05-01 17:54:43.632896	1	\N
1b79a95b-c02f-43b5-ae01-790eb94b2f5d	просто 	\N	2025-05-01 17:56:00.298093	1	\N
018a8053-1ae6-410c-920a-b55dfa38859c	d3e3ed	\N	2025-05-04 00:31:47.481228	1	\N
\.


--
-- TOC entry 5031 (class 0 OID 16807)
-- Dependencies: 220
-- Data for Name: event_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_participants (id, user_id, event_id, joined_at, status, role) FROM stdin;
\.


--
-- TOC entry 5048 (class 0 OID 17078)
-- Dependencies: 237
-- Data for Name: event_statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_statuses (id, code, name) FROM stdin;
1	PLANNED	Запланировано
2	ONGOING	В процессе
3	COMPLETED	Завершено
4	CANCELLED	Отменено
\.


--
-- TOC entry 5030 (class 0 OID 16793)
-- Dependencies: 219
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.events (id, title, description, start_time, sport_type, end_time, location, organizer_id, created_at, status) FROM stdin;
b0c16e70-d1e0-464c-ac64-e6faf3c2308b	Football Match	Weekly football game	2025-05-01 18:00:00	\N	\N	Central Park	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 03:38:36.701034	1
ef0b4022-eb25-4519-b993-f73930815c9d	Football Match	Weekly football game	2025-05-01 18:00:00	\N	\N	Central Park	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 16:57:18.400137	1
2671fcc6-ee2e-4d22-91b0-432c5ace77ec	Football Match	Weekly football game	2025-05-01 18:00:00	\N	\N	Central Park	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 18:07:23.175887	1
87bb8ebd-a154-4853-9a43-0559bb5106fe	Football матч	Weekly football game	2025-05-01 18:00:00	FOOTBALL	2025-05-01 20:00:00	Central Park	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-19 18:37:54.704155	2
\.


--
-- TOC entry 5035 (class 0 OID 16884)
-- Dependencies: 224
-- Data for Name: friends; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friends (id, user_id, friend_id, status, created_at) FROM stdin;
\.


--
-- TOC entry 5055 (class 0 OID 17136)
-- Dependencies: 244
-- Data for Name: message_attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message_attachments (id, message_id, file_url, file_name, file_type, file_size) FROM stdin;
\.


--
-- TOC entry 5034 (class 0 OID 16859)
-- Dependencies: 223
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, chat_id, sender_id, content, sent_at, is_read, read_at, reply_to_id, status) FROM stdin;
c1b705f7-84e2-47df-9daa-ea34a838c231	f3fb8f09-591f-417d-82eb-c8527c74c719	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, world!	2025-04-16 16:50:39.686894	f	\N	\N	\N
c8f47601-9979-4dda-8a48-d711bda44099	9fc47c84-1e7c-490b-8af3-d542e9af942c	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, world!	2025-04-16 19:09:55.583288	f	\N	\N	\N
c036dd2f-ce08-47dd-8f3a-c9f5b6ac5b48	9fc47c84-1e7c-490b-8af3-d542e9af942c	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, world!	2025-04-16 19:11:26.168319	f	\N	\N	\N
8d5b219c-cd9b-4435-a6f5-2b00e8089087	0202fde4-9f71-44b7-a02d-3ba03cc83247	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, buhs!	2025-04-17 02:02:36.863775	f	\N	\N	\N
2c794578-e822-4a41-b2f2-eea01170db4c	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:04:31.597013	f	\N	\N	SENT
efab5d83-f3a3-41ea-9041-7d89a13144a6	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:10:33.546827	f	\N	\N	SENT
d55b6566-dc5d-4803-a41e-f719c959ea35	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:12:38.94299	f	\N	\N	SENT
80b89959-aa40-4ebc-b891-00443a3e5235	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:15:32.971062	f	\N	\N	SENT
5b2762f7-cc86-482e-b2cc-87c8b894aa8f	cba2ae3b-3725-40c4-8da7-8146a59babb2	f3fb8f09-591f-417d-82eb-c8527c74c719	ввввввввввввввввввввввввввввввввввввввввввввввввв	2025-04-30 21:59:21.989383	f	\N	\N	DELETED
557b0c06-05a9-4717-b353-642cead6f211	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	aqaqaqaqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq	2025-05-04 00:30:20.024658	f	\N	\N	DELETED
02591d4b-42d3-4edb-ba72-7644a32a504b	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	hhhhhhhhhh	2025-05-01 18:25:54.494437	f	\N	\N	DELETED
004880be-5909-4c9b-9db5-e806188bb067	19c8e052-34ab-4d06-b97d-0f0466d35677	42542fc1-a797-4e28-ade2-a6feb5567718	как сам	2025-05-01 12:05:53.238489	f	\N	\N	SENT
f9ebe2ee-a9e7-46cd-8e85-fc19e095ddcc	c0050a07-46ab-4811-a41e-8a7ba10baad7	f3fb8f09-591f-417d-82eb-c8527c74c719	аааааааааа	2025-04-30 21:58:26.302436	f	\N	\N	SENT
38fe5c38-9933-4d5a-9f7a-ac8e20c0de4f	018a8053-1ae6-410c-920a-b55dfa38859c	f3fb8f09-591f-417d-82eb-c8527c74c719	frcccc1111	2025-05-04 00:31:56.753442	f	\N	\N	EDITED
eac70c98-f36a-49a2-a316-fae04e104e83	19c8e052-34ab-4d06-b97d-0f0466d35677	f3fb8f09-591f-417d-82eb-c8527c74c719	лох	2025-05-01 11:20:32.049737	f	\N	\N	SENT
3116b06a-98c8-4202-9422-db6a019ee329	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	длдддд	2025-05-01 18:49:28.221948	f	\N	\N	DELETED
15855497-054b-408a-8fcb-ca30ba03110f	cba2ae3b-3725-40c4-8da7-8146a59babb2	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, buhs!	2025-04-17 01:54:18.487958	f	\N	\N	DELETED
4bff7833-3c71-4603-b20b-5dd0187da52f	19c8e052-34ab-4d06-b97d-0f0466d35677	f3fb8f09-591f-417d-82eb-c8527c74c719	Привет пидр	2025-04-30 22:07:40.463162	f	\N	\N	DELETED
19e90785-d8e7-42cc-a8b8-0c64c3b45170	cba2ae3b-3725-40c4-8da7-8146a59babb2	f3fb8f09-591f-417d-82eb-c8527c74c719	привет	2025-04-29 23:03:14.496972	f	\N	\N	EDITED
90b97adb-8c36-4f9b-a80a-82089964e39b	19c8e052-34ab-4d06-b97d-0f0466d35677	42542fc1-a797-4e28-ade2-a6feb5567718	салам алейкум	2025-04-30 22:07:59.227761	f	\N	\N	SENT
068ae9a1-110d-4c98-8710-f270e043bbc8	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	р6нерноекгаонг	2025-05-04 00:29:33.223505	f	\N	\N	SENT
57c37069-2ab0-411e-a111-1332f6ca1584	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	оооооооооооооооооооооо	2025-05-01 18:30:51.988917	f	\N	\N	DELETED
0661e865-ef6c-4006-973d-4e3603e0689b	cba2ae3b-3725-40c4-8da7-8146a59babb2	f3fb8f09-591f-417d-82eb-c8527c74c719	бимб ипмиюб	2025-05-01 15:07:29.81665	f	\N	\N	SENT
8a27ffd4-d23f-4def-a4c9-79e7456c873f	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	лдддддддддд	2025-05-01 18:49:07.070918	f	\N	\N	SENT
053bd7c5-8620-4f9a-96b3-9220648f0388	427ac7a2-83a3-4ff8-bd88-135623516b09	f3fb8f09-591f-417d-82eb-c8527c74c719	иииииии	2025-05-01 17:54:50.282327	f	\N	\N	SENT
695a06bc-cd59-4130-b574-544cd06f9511	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	f3fb8f09-591f-417d-82eb-c8527c74c719	привет лох ебанный	2025-05-01 17:56:12.497259	f	\N	\N	DELETED
7a3ed441-3f1b-4078-9343-e05f202bb740	19c8e052-34ab-4d06-b97d-0f0466d35677	42542fc1-a797-4e28-ade2-a6feb5567718	авмаумваавм	2025-05-01 22:14:52.351521	f	\N	\N	SENT
c0395d0d-0aad-4b39-97c4-497af717d1f2	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	42542fc1-a797-4e28-ade2-a6feb5567718	сдарова пидор	2025-05-01 17:56:25.693837	f	\N	\N	DELETED
bc52b8d8-4a4c-4280-af36-2ee05660e3ab	1b79a95b-c02f-43b5-ae01-790eb94b2f5d	42542fc1-a797-4e28-ade2-a6feb5567718	оаркаоакугшакугоашрукапгшзорукцгрпоукецощгшзпуепготзщшукепшщл0мек90ошпуккпма	2025-05-01 17:57:06.299325	f	\N	\N	EDITED
\.


--
-- TOC entry 5046 (class 0 OID 17034)
-- Dependencies: 235
-- Data for Name: notification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notification (id, user_id, title, message, type, is_read, created_at, related_entity_type, related_entity_id) FROM stdin;
d3bb56d7-1c70-4840-ab2a-d56e49fb7dcf	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	t	2025-04-16 03:00:22.593233	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
e6687fad-3410-4dd9-89a2-0cba135eb013	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	f	2025-04-16 17:33:06.887484	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
b6edbfea-2904-4d56-85a5-9b964f180b6f	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	f	2025-04-16 23:27:09.173229	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
7a4e1316-6844-413c-ae75-d83666eb8b79	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	f	2025-04-16 23:27:18.040154	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
763f9c1d-b7f5-4be9-bbb5-f9e51c9b3afc	5560d4f9-bffa-4572-a03b-a6b68362c718	New Message	You have a new message	MESSAGE	t	2025-04-17 02:04:15.669371	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
\.


--
-- TOC entry 5038 (class 0 OID 16925)
-- Dependencies: 227
-- Data for Name: recommendations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recommendations (id, user_id, type, recommended_id, score, created_at) FROM stdin;
\.


--
-- TOC entry 5054 (class 0 OID 17118)
-- Dependencies: 243
-- Data for Name: relationship_statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relationship_statuses (id, name, description) FROM stdin;
1	PENDING	Pending status
2	ACCEPTED	Accepted status
3	DECLINED	Declined status
4	BLOCKED	Blocked status
\.


--
-- TOC entry 5052 (class 0 OID 17109)
-- Dependencies: 241
-- Data for Name: relationship_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relationship_types (id, name, description) FROM stdin;
1	FRIEND	Friend relationship
2	FOLLOW	Following relationship
3	BLOCK	Block relationship
\.


--
-- TOC entry 5041 (class 0 OID 16979)
-- Dependencies: 230
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name) FROM stdin;
1	ADMIN
2	USER
\.


--
-- TOC entry 5043 (class 0 OID 16983)
-- Dependencies: 232
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status (id, name) FROM stdin;
1	ACTIVE
2	INACTIVE
\.


--
-- TOC entry 5039 (class 0 OID 16937)
-- Dependencies: 228
-- Data for Name: user_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_activities (id, user_id, activity_type, distance, duration, calories_burned, activity_date, external_id, raw_data, created_at) FROM stdin;
\.


--
-- TOC entry 5040 (class 0 OID 16951)
-- Dependencies: 229
-- Data for Name: user_photos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_photos (id, user_id, photo_url, description, upload_date, is_profile_photo) FROM stdin;
e510e273-8a94-4a78-ae8a-bf7867ad80f0	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/6fa29459-5c2a-4410-b50b-8401bb1518b5.jpg	Новое фото	\N	f
11b7b4b3-540b-44d8-a472-9e896e09fbb0	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f0337d1f-1942-4f5c-aff0-7f06cb706a7d.jpg	Новое фото	\N	f
c8a915f3-c8b3-4c46-ae2b-67b2f6c82742	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/6f016854-996c-467a-80cd-2af1e92f4f37.jpg	\N	\N	t
13bbfba7-a92a-4448-b1d6-7f3239338672	42542fc1-a797-4e28-ade2-a6feb5567718	/api/files/42542fc1-a797-4e28-ade2-a6feb5567718/b48bf8e5-722f-4c73-9838-64f3995e39e3.jpg	\N	\N	f
c1c617b5-d7ac-4d25-b12f-9028e7e0208c	42542fc1-a797-4e28-ade2-a6feb5567718	/api/files/42542fc1-a797-4e28-ade2-a6feb5567718/8879550b-fcd3-4073-a70c-3a7739d00d9c.jpg	\N	\N	t
\.


--
-- TOC entry 5029 (class 0 OID 16780)
-- Dependencies: 218
-- Data for Name: user_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_profiles (user_id, first_name, last_name, bio, location, website, avatar_url, sport_type, created_at, updated_at) FROM stdin;
f3fb8f09-591f-417d-82eb-c8527c74c719	Дмитрий	Антипи	Sports enthusias	Поставы	https://github.com/DmitryAntipin151002	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/6f016854-996c-467a-80cd-2af1e92f4f37.jpg	Гимнастика	2025-04-16 02:32:49.800068	2025-04-25 18:53:19.140316
42542fc1-a797-4e28-ade2-a6feb5567718	Олег	Тенькофф	Богатый уебок в мире	Берлин	\N	/api/files/42542fc1-a797-4e28-ade2-a6feb5567718/8879550b-fcd3-4073-a70c-3a7739d00d9c.jpg	Футболл	2025-04-19 18:19:07.161545	2025-05-01 22:14:10.451762
5560d4f9-bffa-4572-a03b-a6b68362c718	\N	\N	\N	\N	\N	\N	\N	2025-04-20 23:12:53.7631	\N
\.


--
-- TOC entry 5037 (class 0 OID 16905)
-- Dependencies: 226
-- Data for Name: user_relationships; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_relationships (id, user_id, related_user_id, created_at, updated_at, type_id, status_id) FROM stdin;
5	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:46:30.848012	2025-04-19 19:46:30.848012	1	1
6	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:49:43.468816	2025-04-19 19:49:43.468816	1	1
7	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:49:51.246879	2025-04-19 19:49:51.246879	1	1
8	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:52:55.161384	2025-04-19 19:52:55.161384	1	1
10	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:58:18.886659	2025-04-19 19:58:18.886659	1	1
12	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 22:14:44.020598	2025-04-20 23:14:56.855197	1	3
13	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 22:44:51.820736	2025-04-20 23:14:58.626253	1	3
14	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:13:43.887628	2025-04-20 23:14:59.355343	1	3
15	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:14:04.65431	2025-04-20 23:14:59.910825	1	3
17	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:35:55.609229	2025-04-20 23:36:02.908098	1	3
21	42542fc1-a797-4e28-ade2-a6feb5567718	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-05-01 22:14:24.518449	2025-05-01 22:14:31.22558	1	2
22	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-05-04 00:28:44.280548	2025-05-04 00:29:03.487352	1	2
\.


--
-- TOC entry 5028 (class 0 OID 16765)
-- Dependencies: 217
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, phone_number, password, role, is_first_enter, end_date, last_login, created_at, updated_at, status) FROM stdin;
f3fb8f09-591f-417d-82eb-c8527c74c719	dmitryantipin@gmail.com	\N	$2a$10$Hm18XyMTV903gXijf.1DLexCkfEIxtrQPLwrjApfJrpnKex2GyvyW	1	t	\N	2025-04-16 01:07:23.708074	2025-04-16 01:07:23.631507	2025-04-16 01:07:23.631507	1
5560d4f9-bffa-4572-a03b-a6b68362c718	antoxa@gmail.com	\N	$2a$10$TvHb7uVBPyycI7qUUV5s6esGl.UTw2jx99cqJWv6CzhZVgeXR856y	1	t	\N	2025-04-16 17:16:06.196155	2025-04-16 17:16:05.937114	2025-04-16 17:16:05.937114	1
42542fc1-a797-4e28-ade2-a6feb5567718	dmitryantipin1@gmail.com	\N	$2a$10$H6gkqek8ZE8ao8AVPxUBreRxOTm3dE2cwLAuoNoox/Cqv1HbmLoOe	2	t	\N	2025-04-19 18:15:17.547995	2025-04-19 18:15:17.399897	2025-04-19 18:15:17.399897	1
\.


--
-- TOC entry 5045 (class 0 OID 16987)
-- Dependencies: 234
-- Data for Name: verification_code; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verification_code (id, user_id, code, created_at, expires_at) FROM stdin;
a339ee62-ba3d-47bc-8bb2-ecf52604e377	5560d4f9-bffa-4572-a03b-a6b68362c718	964496	2025-04-16 17:17:09.68878	2025-04-16 17:22:09.68878
c81f905e-802e-4008-8519-e49be992d3cd	42542fc1-a797-4e28-ade2-a6feb5567718	593181	2025-04-19 18:15:45.394061	2025-04-19 18:20:45.394061
5f36aa7e-a454-4941-a0a0-1bca9c3a25ca	f3fb8f09-591f-417d-82eb-c8527c74c719	697166	2025-05-04 00:26:20.64155	2025-05-04 00:31:20.64155
\.


--
-- TOC entry 5066 (class 0 OID 0)
-- Dependencies: 238
-- Name: chat_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chat_types_id_seq', 3, true);


--
-- TOC entry 5067 (class 0 OID 0)
-- Dependencies: 236
-- Name: event_statuses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.event_statuses_id_seq', 4, true);


--
-- TOC entry 5068 (class 0 OID 0)
-- Dependencies: 242
-- Name: relationship_statuses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.relationship_statuses_id_seq', 4, true);


--
-- TOC entry 5069 (class 0 OID 0)
-- Dependencies: 240
-- Name: relationship_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.relationship_types_id_seq', 3, true);


--
-- TOC entry 5070 (class 0 OID 0)
-- Dependencies: 231
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- TOC entry 5071 (class 0 OID 0)
-- Dependencies: 233
-- Name: status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.status_id_seq', 1, false);


--
-- TOC entry 5072 (class 0 OID 0)
-- Dependencies: 225
-- Name: user_relationships_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_relationships_id_seq', 22, true);


--
-- TOC entry 4809 (class 2606 OID 16846)
-- Name: chat_participants chat_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_pkey PRIMARY KEY (id);


--
-- TOC entry 4811 (class 2606 OID 16848)
-- Name: chat_participants chat_participants_user_id_chat_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_chat_id_key UNIQUE (user_id, chat_id);


--
-- TOC entry 4847 (class 2606 OID 17102)
-- Name: chat_types chat_types_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_name_key UNIQUE (name);


--
-- TOC entry 4849 (class 2606 OID 17100)
-- Name: chat_types chat_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4807 (class 2606 OID 16834)
-- Name: chats chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);


--
-- TOC entry 4801 (class 2606 OID 16815)
-- Name: event_participants event_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_pkey PRIMARY KEY (id);


--
-- TOC entry 4803 (class 2606 OID 16817)
-- Name: event_participants event_participants_user_id_event_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_event_id_key UNIQUE (user_id, event_id);


--
-- TOC entry 4843 (class 2606 OID 17085)
-- Name: event_statuses event_statuses_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_code_key UNIQUE (code);


--
-- TOC entry 4845 (class 2606 OID 17083)
-- Name: event_statuses event_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_pkey PRIMARY KEY (id);


--
-- TOC entry 4797 (class 2606 OID 16801)
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- TOC entry 4819 (class 2606 OID 16891)
-- Name: friends friends_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (id);


--
-- TOC entry 4821 (class 2606 OID 16893)
-- Name: friends friends_user_id_friend_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_friend_id_key UNIQUE (user_id, friend_id);


--
-- TOC entry 4859 (class 2606 OID 17140)
-- Name: message_attachments message_attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_pkey PRIMARY KEY (id);


--
-- TOC entry 4817 (class 2606 OID 16868)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- TOC entry 4841 (class 2606 OID 17041)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 4838 (class 2606 OID 17021)
-- Name: role pk_role; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT pk_role PRIMARY KEY (id);


--
-- TOC entry 4830 (class 2606 OID 16931)
-- Name: recommendations recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_pkey PRIMARY KEY (id);


--
-- TOC entry 4855 (class 2606 OID 17125)
-- Name: relationship_statuses relationship_statuses_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_name_key UNIQUE (name);


--
-- TOC entry 4857 (class 2606 OID 17123)
-- Name: relationship_statuses relationship_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_pkey PRIMARY KEY (id);


--
-- TOC entry 4851 (class 2606 OID 17116)
-- Name: relationship_types relationship_types_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_name_key UNIQUE (name);


--
-- TOC entry 4853 (class 2606 OID 17114)
-- Name: relationship_types relationship_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4833 (class 2606 OID 16945)
-- Name: user_activities user_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_pkey PRIMARY KEY (id);


--
-- TOC entry 4836 (class 2606 OID 16960)
-- Name: user_photos user_photos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_pkey PRIMARY KEY (id);


--
-- TOC entry 4795 (class 2606 OID 16787)
-- Name: user_profiles user_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4827 (class 2606 OID 16912)
-- Name: user_relationships user_relationships_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_pkey PRIMARY KEY (id);


--
-- TOC entry 4789 (class 2606 OID 16777)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4791 (class 2606 OID 16779)
-- Name: users users_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_number_key UNIQUE (phone_number);


--
-- TOC entry 4793 (class 2606 OID 16775)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4812 (class 1259 OID 16969)
-- Name: idx_chat_participants_chat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_chat_participants_chat ON public.chat_participants USING btree (chat_id);


--
-- TOC entry 4813 (class 1259 OID 16968)
-- Name: idx_chat_participants_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_chat_participants_user ON public.chat_participants USING btree (user_id);


--
-- TOC entry 4804 (class 1259 OID 16967)
-- Name: idx_event_participants_event; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_event_participants_event ON public.event_participants USING btree (event_id);


--
-- TOC entry 4805 (class 1259 OID 16966)
-- Name: idx_event_participants_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_event_participants_user ON public.event_participants USING btree (user_id);


--
-- TOC entry 4798 (class 1259 OID 17068)
-- Name: idx_events_organizer; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_events_organizer ON public.events USING btree (organizer_id);


--
-- TOC entry 4799 (class 1259 OID 17091)
-- Name: idx_events_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_events_status ON public.events USING btree (status);


--
-- TOC entry 4822 (class 1259 OID 16973)
-- Name: idx_friends_friend; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friends_friend ON public.friends USING btree (friend_id);


--
-- TOC entry 4823 (class 1259 OID 16972)
-- Name: idx_friends_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friends_user ON public.friends USING btree (user_id);


--
-- TOC entry 4814 (class 1259 OID 16970)
-- Name: idx_messages_chat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_chat ON public.messages USING btree (chat_id);


--
-- TOC entry 4815 (class 1259 OID 16971)
-- Name: idx_messages_sender; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_sender ON public.messages USING btree (sender_id);


--
-- TOC entry 4839 (class 1259 OID 17047)
-- Name: idx_notification_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_user ON public.notification USING btree (user_id);


--
-- TOC entry 4828 (class 1259 OID 16976)
-- Name: idx_recommendations_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_recommendations_user ON public.recommendations USING btree (user_id);


--
-- TOC entry 4831 (class 1259 OID 16977)
-- Name: idx_user_activities_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activities_user ON public.user_activities USING btree (user_id);


--
-- TOC entry 4834 (class 1259 OID 16978)
-- Name: idx_user_photos_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_photos_user ON public.user_photos USING btree (user_id);


--
-- TOC entry 4824 (class 1259 OID 16975)
-- Name: idx_user_relationships_related; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_relationships_related ON public.user_relationships USING btree (related_user_id);


--
-- TOC entry 4825 (class 1259 OID 16974)
-- Name: idx_user_relationships_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_relationships_user ON public.user_relationships USING btree (user_id);


--
-- TOC entry 4867 (class 2606 OID 16854)
-- Name: chat_participants chat_participants_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;


--
-- TOC entry 4868 (class 2606 OID 16849)
-- Name: chat_participants chat_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4865 (class 2606 OID 16835)
-- Name: chats chats_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


--
-- TOC entry 4863 (class 2606 OID 16823)
-- Name: event_participants event_participants_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


--
-- TOC entry 4864 (class 2606 OID 16818)
-- Name: event_participants event_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4861 (class 2606 OID 16802)
-- Name: events events_organizer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_organizer_id_fkey FOREIGN KEY (organizer_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4866 (class 2606 OID 17103)
-- Name: chats fk_chat_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT fk_chat_type FOREIGN KEY (type_id) REFERENCES public.chat_types(id) ON DELETE RESTRICT;


--
-- TOC entry 4862 (class 2606 OID 17086)
-- Name: events fk_event_status; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT fk_event_status FOREIGN KEY (status) REFERENCES public.event_statuses(id);


--
-- TOC entry 4872 (class 2606 OID 16899)
-- Name: friends friends_friend_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_friend_id_fkey FOREIGN KEY (friend_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4873 (class 2606 OID 16894)
-- Name: friends friends_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4882 (class 2606 OID 17141)
-- Name: message_attachments message_attachments_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE;


--
-- TOC entry 4869 (class 2606 OID 16869)
-- Name: messages messages_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;


--
-- TOC entry 4870 (class 2606 OID 16879)
-- Name: messages messages_reply_to_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_reply_to_id_fkey FOREIGN KEY (reply_to_id) REFERENCES public.messages(id) ON DELETE SET NULL;


--
-- TOC entry 4871 (class 2606 OID 16874)
-- Name: messages messages_sender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4881 (class 2606 OID 17042)
-- Name: notification notification_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4878 (class 2606 OID 16932)
-- Name: recommendations recommendations_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4879 (class 2606 OID 16946)
-- Name: user_activities user_activities_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4880 (class 2606 OID 16961)
-- Name: user_photos user_photos_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4860 (class 2606 OID 16788)
-- Name: user_profiles user_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4874 (class 2606 OID 16920)
-- Name: user_relationships user_relationships_related_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_related_user_id_fkey FOREIGN KEY (related_user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4875 (class 2606 OID 17131)
-- Name: user_relationships user_relationships_status_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_status_id_fkey FOREIGN KEY (status_id) REFERENCES public.relationship_statuses(id);


--
-- TOC entry 4876 (class 2606 OID 17126)
-- Name: user_relationships user_relationships_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.relationship_types(id);


--
-- TOC entry 4877 (class 2606 OID 16915)
-- Name: user_relationships user_relationships_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


-- Completed on 2025-05-06 21:16:29

--
-- PostgreSQL database dump complete
--

