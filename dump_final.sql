--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

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
-- Name: chat_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.chat_type AS ENUM (
    'PRIVATE',
    'GROUP',
    'EVENT'
);


ALTER TYPE public.chat_type OWNER TO postgres;

--
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
-- Name: participant_role; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.participant_role AS ENUM (
    'ORGANIZER',
    'PARTICIPANT'
);


ALTER TYPE public.participant_role OWNER TO postgres;

--
-- Name: participant_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.participant_status AS ENUM (
    'PENDING',
    'JOINED',
    'REJECTED'
);


ALTER TYPE public.participant_status OWNER TO postgres;

--
-- Name: recommendation_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.recommendation_type AS ENUM (
    'EVENT',
    'USER',
    'COMMUNITY'
);


ALTER TYPE public.recommendation_type OWNER TO postgres;

--
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
-- Name: chat_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255),
    code character varying(20)
);


ALTER TABLE public.chat_types OWNER TO postgres;

--
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
-- Name: chat_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chat_types_id_seq OWNED BY public.chat_types.id;


--
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
-- Name: event_participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.event_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    event_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status character varying DEFAULT 'PENDING'::public.participant_status NOT NULL,
    "ParticipantRole" character varying[],
    role character varying(255)
);


ALTER TABLE public.event_participants OWNER TO postgres;

--
-- Name: event_statuses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.event_statuses (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.event_statuses OWNER TO postgres;

--
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
-- Name: event_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.event_statuses_id_seq OWNED BY public.event_statuses.id;


--
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
-- Name: relationship_statuses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.relationship_statuses (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.relationship_statuses OWNER TO postgres;

--
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
-- Name: relationship_statuses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.relationship_statuses_id_seq OWNED BY public.relationship_statuses.id;


--
-- Name: relationship_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.relationship_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.relationship_types OWNER TO postgres;

--
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
-- Name: relationship_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.relationship_types_id_seq OWNED BY public.relationship_types.id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
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
-- Name: status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.status (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.status OWNER TO postgres;

--
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
-- Name: user_relationships_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_relationships_id_seq OWNED BY public.user_relationships.id;


--
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
-- Name: chat_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_types ALTER COLUMN id SET DEFAULT nextval('public.chat_types_id_seq'::regclass);


--
-- Name: event_statuses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_statuses ALTER COLUMN id SET DEFAULT nextval('public.event_statuses_id_seq'::regclass);


--
-- Name: relationship_statuses id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_statuses ALTER COLUMN id SET DEFAULT nextval('public.relationship_statuses_id_seq'::regclass);


--
-- Name: relationship_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_types ALTER COLUMN id SET DEFAULT nextval('public.relationship_types_id_seq'::regclass);


--
-- Name: user_relationships id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships ALTER COLUMN id SET DEFAULT nextval('public.user_relationships_id_seq'::regclass);


--
-- Data for Name: chat_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_participants (id, user_id, chat_id, joined_at, last_read_at, is_admin) FROM stdin;
77b107a9-3fc0-47b1-92eb-03f511e19958	f3fb8f09-591f-417d-82eb-c8527c74c719	ed45cdf1-073a-47e9-89b1-c707cd1a2834	2025-05-21 23:16:35.299287	\N	t
65e6b2d4-d369-4084-9aa2-9c40845044c8	42542fc1-a797-4e28-ade2-a6feb5567718	ed45cdf1-073a-47e9-89b1-c707cd1a2834	2025-05-21 23:16:35.299287	\N	f
53e1a053-fce5-41ac-8836-884b14eb50d5	f3fb8f09-591f-417d-82eb-c8527c74c719	5a52c317-1119-4179-970a-7b4d7d298d2a	2025-05-21 23:17:04.058516	\N	t
190c8f1c-a589-4c83-82a3-283d07e1f2ea	42542fc1-a797-4e28-ade2-a6feb5567718	5a52c317-1119-4179-970a-7b4d7d298d2a	2025-05-21 23:17:04.059518	\N	f
\.


--
-- Data for Name: chat_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_types (id, name, description, code) FROM stdin;
1	PRIVATE	Private chat between two users	PRIVATE
2	GROUP	Group chat for multiple users	GROUP
3	EVENT	Event-related chat	EVENT
\.


--
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chats (id, name, event_id, created_at, type_id, creator_id) FROM stdin;
ed45cdf1-073a-47e9-89b1-c707cd1a2834	чатик	\N	2025-05-21 23:16:35.310286	1	\N
5a52c317-1119-4179-970a-7b4d7d298d2a	яяяя	\N	2025-05-21 23:17:04.060516	1	\N
95e1bca0-b38e-4baf-8b0d-9f9536a896f0	Чат для Football Match	\N	2025-05-21 23:17:17.642791	1	\N
\.


--
-- Data for Name: event_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_participants (id, user_id, event_id, joined_at, status, "ParticipantRole", role) FROM stdin;
\.


--
-- Data for Name: event_statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_statuses (id, code, name) FROM stdin;
1	PLANNED	Запланировано
2	ONGOING	В процессе
3	COMPLETED	Завершено
4	CANCELLED	Отменено
\.


--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.events (id, title, description, start_time, sport_type, end_time, location, organizer_id, created_at, status) FROM stdin;
f17152ee-7990-41c3-931c-c84a2eb94f69	спорт с максом	ебацца	2025-05-26 20:59:40.695	VOLLEYBALL	2025-05-26 20:59:40.695	минск	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-05-27 00:00:19.852055	1
\.


--
-- Data for Name: friends; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friends (id, user_id, friend_id, status, created_at) FROM stdin;
\.


--
-- Data for Name: message_attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message_attachments (id, message_id, file_url, file_name, file_type, file_size) FROM stdin;
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, chat_id, sender_id, content, sent_at, is_read, read_at, reply_to_id, status) FROM stdin;
8404fb3e-39b4-41d7-a733-791e7e7d5c23	5a52c317-1119-4179-970a-7b4d7d298d2a	f3fb8f09-591f-417d-82eb-c8527c74c719	kedkedfc	2025-05-22 18:31:32.71601	f	\N	\N	DELETED
21dccc3e-01ea-499d-8664-80d2323dead3	ed45cdf1-073a-47e9-89b1-c707cd1a2834	f3fb8f09-591f-417d-82eb-c8527c74c719	пидр	2025-05-26 23:58:40.613112	f	\N	\N	SENT
\.


--
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
-- Data for Name: recommendations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recommendations (id, user_id, type, recommended_id, score, created_at) FROM stdin;
\.


--
-- Data for Name: relationship_statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relationship_statuses (id, name, description) FROM stdin;
1	PENDING	Pending status
2	ACCEPTED	Accepted status
3	DECLINED	Declined status
4	BLOCKED	Blocked status
\.


--
-- Data for Name: relationship_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relationship_types (id, name, description) FROM stdin;
1	FRIEND	Friend relationship
2	FOLLOW	Following relationship
3	BLOCK	Block relationship
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name) FROM stdin;
1	ADMIN
2	USER
\.


--
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status (id, name) FROM stdin;
1	ACTIVE
2	INACTIVE
\.


--
-- Data for Name: user_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_activities (id, user_id, activity_type, distance, duration, calories_burned, activity_date, external_id, raw_data, created_at) FROM stdin;
\.


--
-- Data for Name: user_photos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_photos (id, user_id, photo_url, description, upload_date, is_profile_photo) FROM stdin;
2a3888be-ab6d-411e-8432-bd383b8dbd55	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e89d34e9-380d-4fa8-8646-c1b6d4cf1cc1.jpg	\N	\N	f
7432fdb8-a712-4267-9a96-d956c38b4e19	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/329e43f8-95f0-4bb9-bf3d-0ed5960bc7b1.jpg	\N	\N	t
\.


--
-- Data for Name: user_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_profiles (user_id, first_name, last_name, bio, location, website, avatar_url, sport_type, created_at, updated_at) FROM stdin;
42542fc1-a797-4e28-ade2-a6feb5567718	Олег	Тенькофф	Богатый уебок в мире	Берлин	\N	/api/files/42542fc1-a797-4e28-ade2-a6feb5567718/8879550b-fcd3-4073-a70c-3a7739d00d9c.jpg	Футболл	2025-04-19 18:19:07.161545	2025-05-01 22:14:10.451762
5560d4f9-bffa-4572-a03b-a6b68362c718	\N	\N	\N	\N	\N	\N	\N	2025-04-20 23:12:53.7631	\N
f3fb8f09-591f-417d-82eb-c8527c74c719	Дмитрий	Антипи	Sports enthusias	Поставы	https://github.com/DmitryAntipin151002	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/329e43f8-95f0-4bb9-bf3d-0ed5960bc7b1.jpg	Гимнастика	2025-04-16 02:32:49.800068	2025-05-20 16:37:43.009168
\.


--
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
18	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-05-14 19:01:18.632447	2025-05-14 19:01:23.990456	1	2
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, phone_number, password, role, is_first_enter, end_date, last_login, created_at, updated_at, status) FROM stdin;
f3fb8f09-591f-417d-82eb-c8527c74c719	dmitryantipin@gmail.com	\N	$2a$10$Hm18XyMTV903gXijf.1DLexCkfEIxtrQPLwrjApfJrpnKex2GyvyW	1	t	\N	2025-04-16 01:07:23.708074	2025-04-16 01:07:23.631507	2025-04-16 01:07:23.631507	1
5560d4f9-bffa-4572-a03b-a6b68362c718	antoxa@gmail.com	\N	$2a$10$TvHb7uVBPyycI7qUUV5s6esGl.UTw2jx99cqJWv6CzhZVgeXR856y	1	t	\N	2025-04-16 17:16:06.196155	2025-04-16 17:16:05.937114	2025-04-16 17:16:05.937114	1
42542fc1-a797-4e28-ade2-a6feb5567718	dmitryantipin1@gmail.com	\N	$2a$10$H6gkqek8ZE8ao8AVPxUBreRxOTm3dE2cwLAuoNoox/Cqv1HbmLoOe	2	t	\N	2025-04-19 18:15:17.547995	2025-04-19 18:15:17.399897	2025-04-19 18:15:17.399897	1
45992c24-3de8-41f9-9cf0-718553f4edaa	dmitry@gmail.com	\N	$2a$10$WOIVxueNHUk3IyfvzieqtuNH7wJcbBYGXollD3J4wXfs1rCfuU.cS	2	t	\N	2025-05-14 18:26:54.506192	2025-05-14 18:26:54.353182	2025-05-14 18:26:54.353182	1
91f5e66e-16a4-40fd-be77-aeb3c1190c59	dmit1111@gmail.com	\N	$2a$10$TtoJ75uXSvTAJY91j8SlJeQ4q9SGrcR0D8UJVQm00DKGTeoAyhNUG	2	t	\N	2025-05-22 16:02:28.822696	2025-05-22 16:02:28.648912	2025-05-22 16:02:28.648912	1
\.


--
-- Data for Name: verification_code; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verification_code (id, user_id, code, created_at, expires_at) FROM stdin;
a339ee62-ba3d-47bc-8bb2-ecf52604e377	5560d4f9-bffa-4572-a03b-a6b68362c718	964496	2025-04-16 17:17:09.68878	2025-04-16 17:22:09.68878
c81f905e-802e-4008-8519-e49be992d3cd	42542fc1-a797-4e28-ade2-a6feb5567718	593181	2025-04-19 18:15:45.394061	2025-04-19 18:20:45.394061
a339ee62-ba3d-47bc-8bb2-ecf52604e377	5560d4f9-bffa-4572-a03b-a6b68362c718	964496	2025-04-16 17:17:09.68878	2025-04-16 17:22:09.68878
c81f905e-802e-4008-8519-e49be992d3cd	42542fc1-a797-4e28-ade2-a6feb5567718	593181	2025-04-19 18:15:45.394061	2025-04-19 18:20:45.394061
2d62caad-7b4f-470c-9d39-c6c92de3d521	45992c24-3de8-41f9-9cf0-718553f4edaa	859940	2025-05-14 18:48:02.63803	2025-05-14 18:53:02.63803
360af763-0f37-4f2d-b235-0118c3b5ef45	f3fb8f09-591f-417d-82eb-c8527c74c719	421608	2025-05-14 18:56:14.385612	2025-05-14 19:01:14.385612
\.


--
-- Name: chat_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chat_types_id_seq', 7, true);


--
-- Name: event_statuses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.event_statuses_id_seq', 4, true);


--
-- Name: relationship_statuses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.relationship_statuses_id_seq', 4, true);


--
-- Name: relationship_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.relationship_types_id_seq', 3, true);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- Name: status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.status_id_seq', 1, false);


--
-- Name: user_relationships_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_relationships_id_seq', 18, true);


--
-- Name: chat_participants chat_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_pkey PRIMARY KEY (id);


--
-- Name: chat_participants chat_participants_user_id_chat_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_chat_id_key UNIQUE (user_id, chat_id);


--
-- Name: chat_types chat_types_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_name_key UNIQUE (name);


--
-- Name: chat_types chat_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_pkey PRIMARY KEY (id);


--
-- Name: chats chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);


--
-- Name: event_participants event_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_pkey PRIMARY KEY (id);


--
-- Name: event_participants event_participants_user_id_event_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_event_id_key UNIQUE (user_id, event_id);


--
-- Name: event_statuses event_statuses_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_code_key UNIQUE (code);


--
-- Name: event_statuses event_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_pkey PRIMARY KEY (id);


--
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- Name: friends friends_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (id);


--
-- Name: friends friends_user_id_friend_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_friend_id_key UNIQUE (user_id, friend_id);


--
-- Name: message_attachments message_attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- Name: role pk_role; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT pk_role PRIMARY KEY (id);


--
-- Name: recommendations recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_pkey PRIMARY KEY (id);


--
-- Name: relationship_statuses relationship_statuses_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_name_key UNIQUE (name);


--
-- Name: relationship_statuses relationship_statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_pkey PRIMARY KEY (id);


--
-- Name: relationship_types relationship_types_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_name_key UNIQUE (name);


--
-- Name: relationship_types relationship_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_pkey PRIMARY KEY (id);


--
-- Name: user_activities user_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_pkey PRIMARY KEY (id);


--
-- Name: user_photos user_photos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_pkey PRIMARY KEY (id);


--
-- Name: user_profiles user_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: user_relationships user_relationships_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_number_key UNIQUE (phone_number);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: idx_chat_participants_chat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_chat_participants_chat ON public.chat_participants USING btree (chat_id);


--
-- Name: idx_chat_participants_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_chat_participants_user ON public.chat_participants USING btree (user_id);


--
-- Name: idx_event_participants_event; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_event_participants_event ON public.event_participants USING btree (event_id);


--
-- Name: idx_event_participants_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_event_participants_user ON public.event_participants USING btree (user_id);


--
-- Name: idx_events_organizer; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_events_organizer ON public.events USING btree (organizer_id);


--
-- Name: idx_events_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_events_status ON public.events USING btree (status);


--
-- Name: idx_friends_friend; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friends_friend ON public.friends USING btree (friend_id);


--
-- Name: idx_friends_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friends_user ON public.friends USING btree (user_id);


--
-- Name: idx_messages_chat; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_chat ON public.messages USING btree (chat_id);


--
-- Name: idx_messages_sender; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_sender ON public.messages USING btree (sender_id);


--
-- Name: idx_notification_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_user ON public.notification USING btree (user_id);


--
-- Name: idx_recommendations_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_recommendations_user ON public.recommendations USING btree (user_id);


--
-- Name: idx_user_activities_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activities_user ON public.user_activities USING btree (user_id);


--
-- Name: idx_user_photos_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_photos_user ON public.user_photos USING btree (user_id);


--
-- Name: idx_user_relationships_related; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_relationships_related ON public.user_relationships USING btree (related_user_id);


--
-- Name: idx_user_relationships_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_relationships_user ON public.user_relationships USING btree (user_id);


--
-- Name: chat_participants chat_participants_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;


--
-- Name: chat_participants chat_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: chats chats_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


--
-- Name: event_participants event_participants_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


--
-- Name: event_participants event_participants_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: events events_organizer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_organizer_id_fkey FOREIGN KEY (organizer_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: chats fk_chat_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT fk_chat_type FOREIGN KEY (type_id) REFERENCES public.chat_types(id) ON DELETE RESTRICT;


--
-- Name: events fk_event_status; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT fk_event_status FOREIGN KEY (status) REFERENCES public.event_statuses(id);


--
-- Name: friends friends_friend_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_friend_id_fkey FOREIGN KEY (friend_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: friends friends_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: message_attachments message_attachments_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE;


--
-- Name: messages messages_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;


--
-- Name: messages messages_reply_to_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_reply_to_id_fkey FOREIGN KEY (reply_to_id) REFERENCES public.messages(id) ON DELETE SET NULL;


--
-- Name: messages messages_sender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: notification notification_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: recommendations recommendations_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_activities user_activities_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_photos user_photos_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_profiles user_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_relationships user_relationships_related_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_related_user_id_fkey FOREIGN KEY (related_user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_relationships user_relationships_status_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_status_id_fkey FOREIGN KEY (status_id) REFERENCES public.relationship_statuses(id);


--
-- Name: user_relationships user_relationships_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.relationship_types(id);


--
-- Name: user_relationships user_relationships_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

