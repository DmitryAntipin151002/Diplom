toc.dat                                                                                             0000600 0004000 0002000 00000130701 15001260361 0014433 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        PGDMP   )                     }            final    17.4    17.2 �    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false         �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false         �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false         �           1262    16697    final    DATABASE     k   CREATE DATABASE final WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'ru-RU';
    DROP DATABASE final;
                     postgres    false         �            1259    16840    chat_participants    TABLE     0  CREATE TABLE public.chat_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    chat_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_read_at timestamp without time zone,
    is_admin boolean DEFAULT false
);
 %   DROP TABLE public.chat_participants;
       public         heap r       postgres    false         �            1259    17095 
   chat_types    TABLE     �   CREATE TABLE public.chat_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);
    DROP TABLE public.chat_types;
       public         heap r       postgres    false         �            1259    17094    chat_types_id_seq    SEQUENCE     �   CREATE SEQUENCE public.chat_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.chat_types_id_seq;
       public               postgres    false    239         �           0    0    chat_types_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.chat_types_id_seq OWNED BY public.chat_types.id;
          public               postgres    false    238         �            1259    16828    chats    TABLE     �   CREATE TABLE public.chats (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255),
    event_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type_id bigint
);
    DROP TABLE public.chats;
       public         heap r       postgres    false         �            1259    16807    event_participants    TABLE     �  CREATE TABLE public.event_participants (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    event_id uuid NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status public.participant_status DEFAULT 'PENDING'::public.participant_status NOT NULL,
    role public.participant_role DEFAULT 'PARTICIPANT'::public.participant_role NOT NULL
);
 &   DROP TABLE public.event_participants;
       public         heap r       postgres    false         �            1259    17078    event_statuses    TABLE     �   CREATE TABLE public.event_statuses (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL
);
 "   DROP TABLE public.event_statuses;
       public         heap r       postgres    false         �            1259    17077    event_statuses_id_seq    SEQUENCE     �   CREATE SEQUENCE public.event_statuses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.event_statuses_id_seq;
       public               postgres    false    237         �           0    0    event_statuses_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.event_statuses_id_seq OWNED BY public.event_statuses.id;
          public               postgres    false    236         �            1259    16793    events    TABLE     �  CREATE TABLE public.events (
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
    DROP TABLE public.events;
       public         heap r       postgres    false         �            1259    16884    friends    TABLE     )  CREATE TABLE public.friends (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    friend_id uuid NOT NULL,
    status public.friend_status DEFAULT 'PENDING'::public.friend_status NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);
    DROP TABLE public.friends;
       public         heap r       postgres    false         �            1259    17136    message_attachments    TABLE     
  CREATE TABLE public.message_attachments (
    id uuid NOT NULL,
    message_id uuid NOT NULL,
    file_url character varying(255) NOT NULL,
    file_name character varying(100) NOT NULL,
    file_type character varying(50) NOT NULL,
    file_size bigint NOT NULL
);
 '   DROP TABLE public.message_attachments;
       public         heap r       postgres    false         �            1259    16859    messages    TABLE     ~  CREATE TABLE public.messages (
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
    DROP TABLE public.messages;
       public         heap r       postgres    false         �            1259    17034    notification    TABLE     ;  CREATE TABLE public.notification (
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
     DROP TABLE public.notification;
       public         heap r       postgres    false         �            1259    16925    recommendations    TABLE     .  CREATE TABLE public.recommendations (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    type public.recommendation_type NOT NULL,
    recommended_id uuid NOT NULL,
    score double precision,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);
 #   DROP TABLE public.recommendations;
       public         heap r       postgres    false         �            1259    17118    relationship_statuses    TABLE     �   CREATE TABLE public.relationship_statuses (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);
 )   DROP TABLE public.relationship_statuses;
       public         heap r       postgres    false         �            1259    17117    relationship_statuses_id_seq    SEQUENCE     �   CREATE SEQUENCE public.relationship_statuses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.relationship_statuses_id_seq;
       public               postgres    false    243         �           0    0    relationship_statuses_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.relationship_statuses_id_seq OWNED BY public.relationship_statuses.id;
          public               postgres    false    242         �            1259    17109    relationship_types    TABLE     �   CREATE TABLE public.relationship_types (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(255)
);
 &   DROP TABLE public.relationship_types;
       public         heap r       postgres    false         �            1259    17108    relationship_types_id_seq    SEQUENCE     �   CREATE SEQUENCE public.relationship_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.relationship_types_id_seq;
       public               postgres    false    241         �           0    0    relationship_types_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.relationship_types_id_seq OWNED BY public.relationship_types.id;
          public               postgres    false    240         �            1259    16979    role    TABLE     _   CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
    DROP TABLE public.role;
       public         heap r       postgres    false         �            1259    16983    status    TABLE     a   CREATE TABLE public.status (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
    DROP TABLE public.status;
       public         heap r       postgres    false         �            1259    16937    user_activities    TABLE     �  CREATE TABLE public.user_activities (
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
 #   DROP TABLE public.user_activities;
       public         heap r       postgres    false         �            1259    16951    user_photos    TABLE     '  CREATE TABLE public.user_photos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid,
    photo_url character varying(255) NOT NULL,
    description text,
    upload_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_profile_photo boolean DEFAULT false NOT NULL
);
    DROP TABLE public.user_photos;
       public         heap r       postgres    false         �            1259    16780    user_profiles    TABLE     �  CREATE TABLE public.user_profiles (
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
 !   DROP TABLE public.user_profiles;
       public         heap r       postgres    false         �            1259    16905    user_relationships    TABLE     K  CREATE TABLE public.user_relationships (
    id bigint NOT NULL,
    user_id uuid NOT NULL,
    related_user_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type_id integer,
    status_id integer
);
 &   DROP TABLE public.user_relationships;
       public         heap r       postgres    false         �            1259    16904    user_relationships_id_seq    SEQUENCE     �   CREATE SEQUENCE public.user_relationships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.user_relationships_id_seq;
       public               postgres    false    226         �           0    0    user_relationships_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.user_relationships_id_seq OWNED BY public.user_relationships.id;
          public               postgres    false    225         �            1259    16765    users    TABLE     "  CREATE TABLE public.users (
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
    DROP TABLE public.users;
       public         heap r       postgres    false         �            1259    16987    verification_code    TABLE     �   CREATE TABLE public.verification_code (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    code character varying(6) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    expires_at timestamp without time zone NOT NULL
);
 %   DROP TABLE public.verification_code;
       public         heap r       postgres    false         �           2604    17098    chat_types id    DEFAULT     n   ALTER TABLE ONLY public.chat_types ALTER COLUMN id SET DEFAULT nextval('public.chat_types_id_seq'::regclass);
 <   ALTER TABLE public.chat_types ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    238    239    239         �           2604    17081    event_statuses id    DEFAULT     v   ALTER TABLE ONLY public.event_statuses ALTER COLUMN id SET DEFAULT nextval('public.event_statuses_id_seq'::regclass);
 @   ALTER TABLE public.event_statuses ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    236    237    237         �           2604    17121    relationship_statuses id    DEFAULT     �   ALTER TABLE ONLY public.relationship_statuses ALTER COLUMN id SET DEFAULT nextval('public.relationship_statuses_id_seq'::regclass);
 G   ALTER TABLE public.relationship_statuses ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    243    242    243         �           2604    17112    relationship_types id    DEFAULT     ~   ALTER TABLE ONLY public.relationship_types ALTER COLUMN id SET DEFAULT nextval('public.relationship_types_id_seq'::regclass);
 D   ALTER TABLE public.relationship_types ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    241    240    241         �           2604    16908    user_relationships id    DEFAULT     ~   ALTER TABLE ONLY public.user_relationships ALTER COLUMN id SET DEFAULT nextval('public.user_relationships_id_seq'::regclass);
 D   ALTER TABLE public.user_relationships ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    226    225    226         �          0    16840    chat_participants 
   TABLE DATA           d   COPY public.chat_participants (id, user_id, chat_id, joined_at, last_read_at, is_admin) FROM stdin;
    public               postgres    false    222       5004.dat �          0    17095 
   chat_types 
   TABLE DATA           ;   COPY public.chat_types (id, name, description) FROM stdin;
    public               postgres    false    239       5019.dat �          0    16828    chats 
   TABLE DATA           H   COPY public.chats (id, name, event_id, created_at, type_id) FROM stdin;
    public               postgres    false    221       5003.dat �          0    16807    event_participants 
   TABLE DATA           \   COPY public.event_participants (id, user_id, event_id, joined_at, status, role) FROM stdin;
    public               postgres    false    220       5002.dat �          0    17078    event_statuses 
   TABLE DATA           8   COPY public.event_statuses (id, code, name) FROM stdin;
    public               postgres    false    237       5017.dat �          0    16793    events 
   TABLE DATA           �   COPY public.events (id, title, description, start_time, sport_type, end_time, location, organizer_id, created_at, status) FROM stdin;
    public               postgres    false    219       5001.dat �          0    16884    friends 
   TABLE DATA           M   COPY public.friends (id, user_id, friend_id, status, created_at) FROM stdin;
    public               postgres    false    224       5006.dat �          0    17136    message_attachments 
   TABLE DATA           h   COPY public.message_attachments (id, message_id, file_url, file_name, file_type, file_size) FROM stdin;
    public               postgres    false    244       5024.dat �          0    16859    messages 
   TABLE DATA           s   COPY public.messages (id, chat_id, sender_id, content, sent_at, is_read, read_at, reply_to_id, status) FROM stdin;
    public               postgres    false    223       5005.dat �          0    17034    notification 
   TABLE DATA           �   COPY public.notification (id, user_id, title, message, type, is_read, created_at, related_entity_type, related_entity_id) FROM stdin;
    public               postgres    false    235       5015.dat �          0    16925    recommendations 
   TABLE DATA           _   COPY public.recommendations (id, user_id, type, recommended_id, score, created_at) FROM stdin;
    public               postgres    false    227       5009.dat �          0    17118    relationship_statuses 
   TABLE DATA           F   COPY public.relationship_statuses (id, name, description) FROM stdin;
    public               postgres    false    243       5023.dat �          0    17109    relationship_types 
   TABLE DATA           C   COPY public.relationship_types (id, name, description) FROM stdin;
    public               postgres    false    241       5021.dat �          0    16979    role 
   TABLE DATA           (   COPY public.role (id, name) FROM stdin;
    public               postgres    false    230       5012.dat �          0    16983    status 
   TABLE DATA           *   COPY public.status (id, name) FROM stdin;
    public               postgres    false    232       5013.dat �          0    16937    user_activities 
   TABLE DATA           �   COPY public.user_activities (id, user_id, activity_type, distance, duration, calories_burned, activity_date, external_id, raw_data, created_at) FROM stdin;
    public               postgres    false    228       5010.dat �          0    16951    user_photos 
   TABLE DATA           i   COPY public.user_photos (id, user_id, photo_url, description, upload_date, is_profile_photo) FROM stdin;
    public               postgres    false    229       5011.dat �          0    16780    user_profiles 
   TABLE DATA           �   COPY public.user_profiles (user_id, first_name, last_name, bio, location, website, avatar_url, sport_type, created_at, updated_at) FROM stdin;
    public               postgres    false    218       5000.dat �          0    16905    user_relationships 
   TABLE DATA           v   COPY public.user_relationships (id, user_id, related_user_id, created_at, updated_at, type_id, status_id) FROM stdin;
    public               postgres    false    226       5008.dat �          0    16765    users 
   TABLE DATA           �   COPY public.users (id, email, phone_number, password, role, is_first_enter, end_date, last_login, created_at, updated_at, status) FROM stdin;
    public               postgres    false    217       4999.dat �          0    16987    verification_code 
   TABLE DATA           V   COPY public.verification_code (id, user_id, code, created_at, expires_at) FROM stdin;
    public               postgres    false    234       5014.dat �           0    0    chat_types_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.chat_types_id_seq', 3, true);
          public               postgres    false    238         �           0    0    event_statuses_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.event_statuses_id_seq', 4, true);
          public               postgres    false    236         �           0    0    relationship_statuses_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.relationship_statuses_id_seq', 4, true);
          public               postgres    false    242         �           0    0    relationship_types_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.relationship_types_id_seq', 3, true);
          public               postgres    false    240         �           0    0    user_relationships_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.user_relationships_id_seq', 17, true);
          public               postgres    false    225         �           2606    16846 (   chat_participants chat_participants_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.chat_participants DROP CONSTRAINT chat_participants_pkey;
       public                 postgres    false    222         �           2606    16848 7   chat_participants chat_participants_user_id_chat_id_key 
   CONSTRAINT     ~   ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_chat_id_key UNIQUE (user_id, chat_id);
 a   ALTER TABLE ONLY public.chat_participants DROP CONSTRAINT chat_participants_user_id_chat_id_key;
       public                 postgres    false    222    222         �           2606    17102    chat_types chat_types_name_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_name_key UNIQUE (name);
 H   ALTER TABLE ONLY public.chat_types DROP CONSTRAINT chat_types_name_key;
       public                 postgres    false    239         �           2606    17100    chat_types chat_types_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.chat_types
    ADD CONSTRAINT chat_types_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.chat_types DROP CONSTRAINT chat_types_pkey;
       public                 postgres    false    239         �           2606    16834    chats chats_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.chats DROP CONSTRAINT chats_pkey;
       public                 postgres    false    221         �           2606    16815 *   event_participants event_participants_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.event_participants DROP CONSTRAINT event_participants_pkey;
       public                 postgres    false    220         �           2606    16817 :   event_participants event_participants_user_id_event_id_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_event_id_key UNIQUE (user_id, event_id);
 d   ALTER TABLE ONLY public.event_participants DROP CONSTRAINT event_participants_user_id_event_id_key;
       public                 postgres    false    220    220         �           2606    17085 &   event_statuses event_statuses_code_key 
   CONSTRAINT     a   ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_code_key UNIQUE (code);
 P   ALTER TABLE ONLY public.event_statuses DROP CONSTRAINT event_statuses_code_key;
       public                 postgres    false    237         �           2606    17083 "   event_statuses event_statuses_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.event_statuses
    ADD CONSTRAINT event_statuses_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.event_statuses DROP CONSTRAINT event_statuses_pkey;
       public                 postgres    false    237         �           2606    16801    events events_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.events DROP CONSTRAINT events_pkey;
       public                 postgres    false    219         �           2606    16891    friends friends_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_pkey;
       public                 postgres    false    224         �           2606    16893 %   friends friends_user_id_friend_id_key 
   CONSTRAINT     n   ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_friend_id_key UNIQUE (user_id, friend_id);
 O   ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_user_id_friend_id_key;
       public                 postgres    false    224    224         �           2606    17140 ,   message_attachments message_attachments_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.message_attachments DROP CONSTRAINT message_attachments_pkey;
       public                 postgres    false    244         �           2606    16868    messages messages_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.messages DROP CONSTRAINT messages_pkey;
       public                 postgres    false    223         �           2606    17041    notification notification_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.notification DROP CONSTRAINT notification_pkey;
       public                 postgres    false    235         �           2606    17021    role pk_role 
   CONSTRAINT     J   ALTER TABLE ONLY public.role
    ADD CONSTRAINT pk_role PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.role DROP CONSTRAINT pk_role;
       public                 postgres    false    230         �           2606    16931 $   recommendations recommendations_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.recommendations DROP CONSTRAINT recommendations_pkey;
       public                 postgres    false    227         �           2606    17125 4   relationship_statuses relationship_statuses_name_key 
   CONSTRAINT     o   ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_name_key UNIQUE (name);
 ^   ALTER TABLE ONLY public.relationship_statuses DROP CONSTRAINT relationship_statuses_name_key;
       public                 postgres    false    243         �           2606    17123 0   relationship_statuses relationship_statuses_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.relationship_statuses
    ADD CONSTRAINT relationship_statuses_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.relationship_statuses DROP CONSTRAINT relationship_statuses_pkey;
       public                 postgres    false    243         �           2606    17116 .   relationship_types relationship_types_name_key 
   CONSTRAINT     i   ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_name_key UNIQUE (name);
 X   ALTER TABLE ONLY public.relationship_types DROP CONSTRAINT relationship_types_name_key;
       public                 postgres    false    241         �           2606    17114 *   relationship_types relationship_types_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.relationship_types
    ADD CONSTRAINT relationship_types_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.relationship_types DROP CONSTRAINT relationship_types_pkey;
       public                 postgres    false    241         �           2606    16945 $   user_activities user_activities_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.user_activities DROP CONSTRAINT user_activities_pkey;
       public                 postgres    false    228         �           2606    16960    user_photos user_photos_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.user_photos DROP CONSTRAINT user_photos_pkey;
       public                 postgres    false    229         �           2606    16787     user_profiles user_profiles_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (user_id);
 J   ALTER TABLE ONLY public.user_profiles DROP CONSTRAINT user_profiles_pkey;
       public                 postgres    false    218         �           2606    16912 *   user_relationships user_relationships_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.user_relationships DROP CONSTRAINT user_relationships_pkey;
       public                 postgres    false    226         �           2606    16777    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public                 postgres    false    217         �           2606    16779    users users_phone_number_key 
   CONSTRAINT     _   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_number_key UNIQUE (phone_number);
 F   ALTER TABLE ONLY public.users DROP CONSTRAINT users_phone_number_key;
       public                 postgres    false    217         �           2606    16775    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    217         �           1259    16969    idx_chat_participants_chat    INDEX     [   CREATE INDEX idx_chat_participants_chat ON public.chat_participants USING btree (chat_id);
 .   DROP INDEX public.idx_chat_participants_chat;
       public                 postgres    false    222         �           1259    16968    idx_chat_participants_user    INDEX     [   CREATE INDEX idx_chat_participants_user ON public.chat_participants USING btree (user_id);
 .   DROP INDEX public.idx_chat_participants_user;
       public                 postgres    false    222         �           1259    16967    idx_event_participants_event    INDEX     _   CREATE INDEX idx_event_participants_event ON public.event_participants USING btree (event_id);
 0   DROP INDEX public.idx_event_participants_event;
       public                 postgres    false    220         �           1259    16966    idx_event_participants_user    INDEX     ]   CREATE INDEX idx_event_participants_user ON public.event_participants USING btree (user_id);
 /   DROP INDEX public.idx_event_participants_user;
       public                 postgres    false    220         �           1259    17068    idx_events_organizer    INDEX     O   CREATE INDEX idx_events_organizer ON public.events USING btree (organizer_id);
 (   DROP INDEX public.idx_events_organizer;
       public                 postgres    false    219         �           1259    17091    idx_events_status    INDEX     F   CREATE INDEX idx_events_status ON public.events USING btree (status);
 %   DROP INDEX public.idx_events_status;
       public                 postgres    false    219         �           1259    16973    idx_friends_friend    INDEX     K   CREATE INDEX idx_friends_friend ON public.friends USING btree (friend_id);
 &   DROP INDEX public.idx_friends_friend;
       public                 postgres    false    224         �           1259    16972    idx_friends_user    INDEX     G   CREATE INDEX idx_friends_user ON public.friends USING btree (user_id);
 $   DROP INDEX public.idx_friends_user;
       public                 postgres    false    224         �           1259    16970    idx_messages_chat    INDEX     I   CREATE INDEX idx_messages_chat ON public.messages USING btree (chat_id);
 %   DROP INDEX public.idx_messages_chat;
       public                 postgres    false    223         �           1259    16971    idx_messages_sender    INDEX     M   CREATE INDEX idx_messages_sender ON public.messages USING btree (sender_id);
 '   DROP INDEX public.idx_messages_sender;
       public                 postgres    false    223         �           1259    17047    idx_notification_user    INDEX     Q   CREATE INDEX idx_notification_user ON public.notification USING btree (user_id);
 )   DROP INDEX public.idx_notification_user;
       public                 postgres    false    235         �           1259    16976    idx_recommendations_user    INDEX     W   CREATE INDEX idx_recommendations_user ON public.recommendations USING btree (user_id);
 ,   DROP INDEX public.idx_recommendations_user;
       public                 postgres    false    227         �           1259    16977    idx_user_activities_user    INDEX     W   CREATE INDEX idx_user_activities_user ON public.user_activities USING btree (user_id);
 ,   DROP INDEX public.idx_user_activities_user;
       public                 postgres    false    228         �           1259    16978    idx_user_photos_user    INDEX     O   CREATE INDEX idx_user_photos_user ON public.user_photos USING btree (user_id);
 (   DROP INDEX public.idx_user_photos_user;
       public                 postgres    false    229         �           1259    16975    idx_user_relationships_related    INDEX     h   CREATE INDEX idx_user_relationships_related ON public.user_relationships USING btree (related_user_id);
 2   DROP INDEX public.idx_user_relationships_related;
       public                 postgres    false    226         �           1259    16974    idx_user_relationships_user    INDEX     ]   CREATE INDEX idx_user_relationships_user ON public.user_relationships USING btree (user_id);
 /   DROP INDEX public.idx_user_relationships_user;
       public                 postgres    false    226         �           2606    16854 0   chat_participants chat_participants_chat_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.chat_participants DROP CONSTRAINT chat_participants_chat_id_fkey;
       public               postgres    false    4778    222    221         �           2606    16849 0   chat_participants chat_participants_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 Z   ALTER TABLE ONLY public.chat_participants DROP CONSTRAINT chat_participants_user_id_fkey;
       public               postgres    false    222    4764    217         �           2606    16835    chats chats_event_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;
 C   ALTER TABLE ONLY public.chats DROP CONSTRAINT chats_event_id_fkey;
       public               postgres    false    4768    221    219         �           2606    16823 3   event_participants event_participants_event_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;
 ]   ALTER TABLE ONLY public.event_participants DROP CONSTRAINT event_participants_event_id_fkey;
       public               postgres    false    220    4768    219         �           2606    16818 2   event_participants event_participants_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.event_participants
    ADD CONSTRAINT event_participants_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 \   ALTER TABLE ONLY public.event_participants DROP CONSTRAINT event_participants_user_id_fkey;
       public               postgres    false    220    4764    217         �           2606    16802    events events_organizer_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_organizer_id_fkey FOREIGN KEY (organizer_id) REFERENCES public.users(id) ON DELETE CASCADE;
 I   ALTER TABLE ONLY public.events DROP CONSTRAINT events_organizer_id_fkey;
       public               postgres    false    217    219    4764         �           2606    17103    chats fk_chat_type    FK CONSTRAINT     �   ALTER TABLE ONLY public.chats
    ADD CONSTRAINT fk_chat_type FOREIGN KEY (type_id) REFERENCES public.chat_types(id) ON DELETE RESTRICT;
 <   ALTER TABLE ONLY public.chats DROP CONSTRAINT fk_chat_type;
       public               postgres    false    239    4820    221         �           2606    17086    events fk_event_status    FK CONSTRAINT     }   ALTER TABLE ONLY public.events
    ADD CONSTRAINT fk_event_status FOREIGN KEY (status) REFERENCES public.event_statuses(id);
 @   ALTER TABLE ONLY public.events DROP CONSTRAINT fk_event_status;
       public               postgres    false    219    237    4816         �           2606    16899    friends friends_friend_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_friend_id_fkey FOREIGN KEY (friend_id) REFERENCES public.users(id) ON DELETE CASCADE;
 H   ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_friend_id_fkey;
       public               postgres    false    224    217    4764         �           2606    16894    friends friends_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_user_id_fkey;
       public               postgres    false    224    4764    217         �           2606    17141 7   message_attachments message_attachments_message_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.message_attachments
    ADD CONSTRAINT message_attachments_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE;
 a   ALTER TABLE ONLY public.message_attachments DROP CONSTRAINT message_attachments_message_id_fkey;
       public               postgres    false    223    4788    244         �           2606    16869    messages messages_chat_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id) ON DELETE CASCADE;
 H   ALTER TABLE ONLY public.messages DROP CONSTRAINT messages_chat_id_fkey;
       public               postgres    false    223    221    4778         �           2606    16879 "   messages messages_reply_to_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_reply_to_id_fkey FOREIGN KEY (reply_to_id) REFERENCES public.messages(id) ON DELETE SET NULL;
 L   ALTER TABLE ONLY public.messages DROP CONSTRAINT messages_reply_to_id_fkey;
       public               postgres    false    223    4788    223         �           2606    16874     messages messages_sender_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(id) ON DELETE CASCADE;
 J   ALTER TABLE ONLY public.messages DROP CONSTRAINT messages_sender_id_fkey;
       public               postgres    false    223    4764    217         �           2606    17042 &   notification notification_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);
 P   ALTER TABLE ONLY public.notification DROP CONSTRAINT notification_user_id_fkey;
       public               postgres    false    4764    235    217         �           2606    16932 ,   recommendations recommendations_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.recommendations
    ADD CONSTRAINT recommendations_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 V   ALTER TABLE ONLY public.recommendations DROP CONSTRAINT recommendations_user_id_fkey;
       public               postgres    false    227    217    4764         �           2606    16946 ,   user_activities user_activities_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 V   ALTER TABLE ONLY public.user_activities DROP CONSTRAINT user_activities_user_id_fkey;
       public               postgres    false    4764    228    217         �           2606    16961 $   user_photos user_photos_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_photos
    ADD CONSTRAINT user_photos_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 N   ALTER TABLE ONLY public.user_photos DROP CONSTRAINT user_photos_user_id_fkey;
       public               postgres    false    229    4764    217         �           2606    16788 (   user_profiles user_profiles_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.user_profiles DROP CONSTRAINT user_profiles_user_id_fkey;
       public               postgres    false    217    218    4764         �           2606    16920 :   user_relationships user_relationships_related_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_related_user_id_fkey FOREIGN KEY (related_user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 d   ALTER TABLE ONLY public.user_relationships DROP CONSTRAINT user_relationships_related_user_id_fkey;
       public               postgres    false    226    217    4764         �           2606    17131 4   user_relationships user_relationships_status_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_status_id_fkey FOREIGN KEY (status_id) REFERENCES public.relationship_statuses(id);
 ^   ALTER TABLE ONLY public.user_relationships DROP CONSTRAINT user_relationships_status_id_fkey;
       public               postgres    false    226    4828    243         �           2606    17126 2   user_relationships user_relationships_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.relationship_types(id);
 \   ALTER TABLE ONLY public.user_relationships DROP CONSTRAINT user_relationships_type_id_fkey;
       public               postgres    false    4824    241    226         �           2606    16915 2   user_relationships user_relationships_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_relationships
    ADD CONSTRAINT user_relationships_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
 \   ALTER TABLE ONLY public.user_relationships DROP CONSTRAINT user_relationships_user_id_fkey;
       public               postgres    false    217    4764    226                                                                       5004.dat                                                                                            0000600 0004000 0002000 00000004334 15001260361 0014240 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        da00902b-7a4e-461b-a087-81895def05e8	f3fb8f09-591f-417d-82eb-c8527c74c719	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 16:51:20.409528	\N	f
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
\.


                                                                                                                                                                                                                                                                                                    5019.dat                                                                                            0000600 0004000 0002000 00000000157 15001260361 0014245 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	PRIVATE	Private chat between two users
2	GROUP	Group chat for multiple users
3	EVENT	Event-related chat
\.


                                                                                                                                                                                                                                                                                                                                                                                                                 5003.dat                                                                                            0000600 0004000 0002000 00000001653 15001260361 0014240 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        6170175d-748d-4079-b400-d4096903c461	Test Chat	\N	2025-04-16 19:06:51.037051	2
9fc47c84-1e7c-490b-8af3-d542e9af942c	Test Chat	\N	2025-04-16 19:07:44.216783	1
c0050a07-46ab-4811-a41e-8a7ba10baad7	Test Chat	\N	2025-04-16 20:48:10.041249	1
cba2ae3b-3725-40c4-8da7-8146a59babb2	игры	\N	2025-04-17 01:50:07.295858	1
0202fde4-9f71-44b7-a02d-3ba03cc83247	Чат	\N	2025-04-17 02:00:27.91317	1
5733a5bd-6389-408a-a5c0-edebe3f49236	Чат	\N	2025-04-17 04:44:55.503848	1
d10b9766-c36a-4f08-880c-774f1e0c2d5b	Чат	\N	2025-04-17 05:17:42.557159	1
8f118829-caaf-4d30-837f-72bef7e2759a	Чат	\N	2025-04-17 16:35:24.748525	1
4184cf72-160c-4f74-868e-bc14c20c0477	Work Chat	\N	2025-04-17 16:46:54.937427	2
f3fb8f09-591f-417d-82eb-c8527c74c719	Test Chat	\N	2025-04-16 16:44:14.631981	1
5a2b61b0-5e2e-4e8d-9b9b-fda5818e3370	Work Chat	\N	2025-04-17 17:16:36.1951	2
cad0a461-fc18-4675-9cf5-1b5db56b13f4	пидоры	\N	2025-04-17 17:17:00.78794	2
\.


                                                                                     5002.dat                                                                                            0000600 0004000 0002000 00000000005 15001260361 0014225 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        \.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           5017.dat                                                                                            0000600 0004000 0002000 00000000204 15001260361 0014234 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	PLANNED	Запланировано
2	ONGOING	В процессе
3	COMPLETED	Завершено
4	CANCELLED	Отменено
\.


                                                                                                                                                                                                                                                                                                                                                                                            5001.dat                                                                                            0000600 0004000 0002000 00000001347 15001260361 0014236 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        b0c16e70-d1e0-464c-ac64-e6faf3c2308b	Football Match	Weekly football game	2025-05-01 18:00:00	\N	\N	Central Park	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 03:38:36.701034	1
ef0b4022-eb25-4519-b993-f73930815c9d	Football Match	Weekly football game	2025-05-01 18:00:00	\N	\N	Central Park	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 16:57:18.400137	1
2671fcc6-ee2e-4d22-91b0-432c5ace77ec	Football Match	Weekly football game	2025-05-01 18:00:00	\N	\N	Central Park	f3fb8f09-591f-417d-82eb-c8527c74c719	2025-04-16 18:07:23.175887	1
87bb8ebd-a154-4853-9a43-0559bb5106fe	Football матч	Weekly football game	2025-05-01 18:00:00	FOOTBALL	2025-05-01 20:00:00	Central Park	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-19 18:37:54.704155	2
\.


                                                                                                                                                                                                                                                                                         5006.dat                                                                                            0000600 0004000 0002000 00000000005 15001260361 0014231 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        \.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           5024.dat                                                                                            0000600 0004000 0002000 00000000005 15001260361 0014231 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        \.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           5005.dat                                                                                            0000600 0004000 0002000 00000002675 15001260361 0014247 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        c1b705f7-84e2-47df-9daa-ea34a838c231	f3fb8f09-591f-417d-82eb-c8527c74c719	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, world!	2025-04-16 16:50:39.686894	f	\N	\N	\N
c8f47601-9979-4dda-8a48-d711bda44099	9fc47c84-1e7c-490b-8af3-d542e9af942c	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, world!	2025-04-16 19:09:55.583288	f	\N	\N	\N
c036dd2f-ce08-47dd-8f3a-c9f5b6ac5b48	9fc47c84-1e7c-490b-8af3-d542e9af942c	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, world!	2025-04-16 19:11:26.168319	f	\N	\N	\N
15855497-054b-408a-8fcb-ca30ba03110f	cba2ae3b-3725-40c4-8da7-8146a59babb2	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, buhs!	2025-04-17 01:54:18.487958	f	\N	\N	\N
8d5b219c-cd9b-4435-a6f5-2b00e8089087	0202fde4-9f71-44b7-a02d-3ba03cc83247	f3fb8f09-591f-417d-82eb-c8527c74c719	Hello, buhs!	2025-04-17 02:02:36.863775	f	\N	\N	\N
2c794578-e822-4a41-b2f2-eea01170db4c	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:04:31.597013	f	\N	\N	SENT
efab5d83-f3a3-41ea-9041-7d89a13144a6	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:10:33.546827	f	\N	\N	SENT
d55b6566-dc5d-4803-a41e-f719c959ea35	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:12:38.94299	f	\N	\N	SENT
80b89959-aa40-4ebc-b891-00443a3e5235	cad0a461-fc18-4675-9cf5-1b5db56b13f4	5560d4f9-bffa-4572-a03b-a6b68362c718	Hello World	2025-04-17 19:15:32.971062	f	\N	\N	SENT
\.


                                                                   5015.dat                                                                                            0000600 0004000 0002000 00000001661 15001260361 0014242 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        d3bb56d7-1c70-4840-ab2a-d56e49fb7dcf	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	t	2025-04-16 03:00:22.593233	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
e6687fad-3410-4dd9-89a2-0cba135eb013	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	f	2025-04-16 17:33:06.887484	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
b6edbfea-2904-4d56-85a5-9b964f180b6f	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	f	2025-04-16 23:27:09.173229	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
7a4e1316-6844-413c-ae75-d83666eb8b79	f3fb8f09-591f-417d-82eb-c8527c74c719	New Message	You have a new message	MESSAGE	f	2025-04-16 23:27:18.040154	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
763f9c1d-b7f5-4be9-bbb5-f9e51c9b3afc	5560d4f9-bffa-4572-a03b-a6b68362c718	New Message	You have a new message	MESSAGE	t	2025-04-17 02:04:15.669371	CHAT	3fa85f64-5717-4562-b3fc-2c963f66afa6
\.


                                                                               5009.dat                                                                                            0000600 0004000 0002000 00000000005 15001260361 0014234 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        \.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           5023.dat                                                                                            0000600 0004000 0002000 00000000155 15001260361 0014236 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	PENDING	Pending status
2	ACCEPTED	Accepted status
3	DECLINED	Declined status
4	BLOCKED	Blocked status
\.


                                                                                                                                                                                                                                                                                                                                                                                                                   5021.dat                                                                                            0000600 0004000 0002000 00000000135 15001260361 0014232 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	FRIEND	Friend relationship
2	FOLLOW	Following relationship
3	BLOCK	Block relationship
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                   5012.dat                                                                                            0000600 0004000 0002000 00000000024 15001260361 0014227 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	ADMIN
2	USER
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            5013.dat                                                                                            0000600 0004000 0002000 00000000031 15001260361 0014226 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        1	ACTIVE
2	INACTIVE
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       5010.dat                                                                                            0000600 0004000 0002000 00000000005 15001260361 0014224 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        \.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           5011.dat                                                                                            0000600 0004000 0002000 00000016012 15001260361 0014232 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        2435894a-1aa9-4520-90e7-a1aa5bef3014	42542fc1-a797-4e28-ade2-a6feb5567718	http://example.com/photo.jpg	\N	\N	f
0496557f-c17b-4c27-a5d3-d831baccdfa7	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/5b8636e2-4b32-4c55-a046-fc12f5b7e64c.jpg	Новая фотография	\N	f
2cea331d-d0be-4fac-8b4f-68f3d5278a33	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/40782e37-9d3d-48d2-80fb-165e52bf012b.jpg	Новая фотография	\N	f
c0f9937c-84c5-474f-82b5-002883d1c785	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/aad4351a-4083-4e23-9fe7-92b8039537a6.jpg	Новая фотография	\N	f
e3ef7017-39b7-45b7-8e3e-9ac5cda05cdc	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/58637366-f1d0-47bf-9d21-67a4f07d22b1.jpg	Новая фотография	\N	f
998a7f70-a546-40a0-a496-7331e68df4cb	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/a251766b-bf07-41a7-b5d6-5c7c4e3e0f6a.jpg	Новая фотография	\N	f
a36a39f6-bdf9-4f12-95f6-3e249cd527eb	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/f3fb8f09-591f-417d-82eb-c8527c74c719/0d93cbb5-067d-4241-8159-4c3c3f0738c8.jpg	Новая фотография	\N	f
e86e6cf3-055a-4106-9172-dd9ca116906b	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/61d5d672-5e80-4e66-8194-cf0776f4e8a4.jpg	Новое фото	\N	f
fe217557-5910-47dc-bc38-7e062b76ba8b	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/d7e900f1-dcca-4c73-85aa-154c7237429b.jpg	Новое фото	\N	f
782aec0b-97b7-465a-b22d-bbadec1f187f	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/fe928b9c-9e03-49ee-8799-eb47bbfc34f0.jpg	Новое фото	\N	f
11b99aee-0311-4b33-8f8f-d32644f05a4e	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/34f3fc3f-7a2a-4876-8058-54fb4734ec19.jpg	Новое фото	\N	f
bce0d535-7b44-41f7-8681-b05c205e69d5	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/d43afe8d-0e0b-4ab8-9cf6-8719c9393f7b.jpg	Новое фото	\N	f
de2e14cb-6d69-4b3c-b759-f2c57564dae8	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/7c90bb3d-8a69-44f9-ab0d-5724b7e1efc5.jpg	Новое фото	\N	f
ece017a6-114b-4a22-9446-6dc045f3ace8	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/a0a25186-faaa-44f3-95ec-d58d66a2dd49.jpg	Новое фото	\N	f
1ae0a8bc-04d7-441c-af6e-bc2ac0163c26	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/93facbb5-4f32-4e16-bb19-b83d6eeeda5b.jpg	Новое фото	\N	f
22271932-c8b2-4227-a82e-62085622d8e4	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/6f36c9f6-d914-40e5-8b17-4a7e4b2e6bdb.jpg	Новое фото	\N	f
b95c4704-3a5e-41d2-8988-dde9f07c301c	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/ff56ca8d-2ac4-4796-9bd5-8fea2881ff38.jpg	Новое фото	\N	f
9107bfab-2335-4b8c-b813-ced2f0fe57c8	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/ecfa008d-223a-424d-ab15-d6a9a4c7fd86.jpg	Новое фото	\N	f
a6943d9d-474f-4c00-bfec-bd14610f603b	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/805061f1-c017-4e06-b670-1bdca1a4434b.jpg	Новое фото	\N	f
ed4aa6fe-76bf-4b21-a726-eda7afb7a8be	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/0799d494-9556-4321-8b22-e5c327086b3e.jpg	Новое фото	\N	f
19e82d6e-ec18-4a07-b368-e4c6974e9c8a	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/feee9422-7873-4742-b2d4-12f84c25c58d.jpg	Новое фото	\N	f
b798bd9f-3fd7-425e-b105-bb0fe79dba4e	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/42507d7b-1ed4-4ff2-8950-07ef67ec6162.jpg	Новое фото	\N	f
ef0eea24-78db-4309-92b2-e7f840cf8635	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/726b9318-e38d-4587-bca2-c5790a012963.jpg	Новое фото	\N	f
46320741-77f4-4d4b-9ead-56b7f5ab4d25	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/456b3507-7e2c-4cf3-bf81-4441fa8587ce.jpg	Новое фото	\N	f
16fa1922-438e-43af-a440-d13c773811b6	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/81c344b4-1dbc-4afc-87a0-d8fe133f3d44.jpg	Новое фото	\N	f
7029b230-8c7c-4c8e-85aa-e4511bd893f2	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/4b6ff98e-5ffd-4edd-80e2-25cf6174ea12.jpg	Новое фото	\N	f
15cc1135-5b00-43f0-aea9-128c27efcc8a	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/d467831a-766f-47b5-be2a-c6a8731d11fe.jpg	Новое фото	\N	f
b974e43b-14fc-40c6-aa8f-136df5259070	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/578c4f51-66b0-4d97-a24c-6b354b46569e.jpg	Новое фото	\N	f
e62b7b95-2efe-4529-acad-4b98151516e3	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/8c729e85-b479-4753-a744-4a922a5fee1a.jpg	Новое фото	\N	f
aa52c644-fa92-47b5-9411-cb9afd776d48	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/063c5863-cade-476a-a359-619f6706c16c.jpg	Новое фото	\N	f
aeef392a-7e31-463f-8287-c9936ff27efb	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/09a289e3-37ae-4a42-bd46-5dc711c781e9.jpg	Новое фото	\N	f
0829b500-4e3f-447c-83dd-671f949e6706	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e84b4083-a1cc-486b-8c4a-94acf100eea5.jpg	Новое фото	\N	f
6ed21397-280e-4a15-b74c-177fcc1f2305	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/3e3e539b-50e8-46e7-9964-a8ca513436d9.jpg	Новое фото	\N	f
4be61d36-9d71-4768-b2d2-bf7111a630d3	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e4ff975c-56db-40a4-a3b9-f6e638e64cda.jpg	Новое фото	\N	t
f535bb79-46ed-41df-9b45-66d5a9d2a2a2	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/1946c147-3034-43dc-b892-d1adc398bc1d.jpg	Новое фото	\N	f
dfe1fa8d-7f86-4aaa-b6dd-e511693954e7	f3fb8f09-591f-417d-82eb-c8527c74c719	/api/files/f3fb8f09-591f-417d-82eb-c8527c74c719/e0fb6bbc-192e-4593-b3cf-e75171f6e7be.jpg	Новое фото	\N	f
07c9a5e3-60e4-466f-b211-61ded47bf565	42542fc1-a797-4e28-ade2-a6feb5567718	/api/files/42542fc1-a797-4e28-ade2-a6feb5567718/a53db720-902a-46b5-af36-9fb327f27bb3.jpg	Новое фото	\N	t
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      5000.dat                                                                                            0000600 0004000 0002000 00000001131 15001260361 0014224 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        42542fc1-a797-4e28-ade2-a6feb5567718	Олег	Тенькофф	Богатый уебок в мире	Берлин	\N	42542fc1-a797-4e28-ade2-a6feb5567718/7f15653f-501a-446a-ba75-4bb0d8620943.jpg	Футболл	2025-04-19 18:19:07.161545	2025-04-19 21:00:19.261709
f3fb8f09-591f-417d-82eb-c8527c74c719	Дмитрий	Антипин	Sports enthusias	Поставы	https://github.com/DmitryAntipin151002	e2425a97-288c-4a45-bca4-25d40987ef4c.jpg	Футбол	2025-04-16 02:32:49.800068	2025-04-20 19:36:29.943313
5560d4f9-bffa-4572-a03b-a6b68362c718	\N	\N	\N	\N	\N	\N	\N	2025-04-20 23:12:53.7631	\N
\.


                                                                                                                                                                                                                                                                                                                                                                                                                                       5008.dat                                                                                            0000600 0004000 0002000 00000002715 15001260361 0014245 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        5	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:46:30.848012	2025-04-19 19:46:30.848012	1	1
6	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:49:43.468816	2025-04-19 19:49:43.468816	1	1
7	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:49:51.246879	2025-04-19 19:49:51.246879	1	1
8	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:52:55.161384	2025-04-19 19:52:55.161384	1	1
10	42542fc1-a797-4e28-ade2-a6feb5567718	5560d4f9-bffa-4572-a03b-a6b68362c718	2025-04-19 19:58:18.886659	2025-04-19 19:58:18.886659	1	1
12	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 22:14:44.020598	2025-04-20 23:14:56.855197	1	3
13	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 22:44:51.820736	2025-04-20 23:14:58.626253	1	3
14	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:13:43.887628	2025-04-20 23:14:59.355343	1	3
15	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:14:04.65431	2025-04-20 23:14:59.910825	1	3
16	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:35:26.546442	2025-04-20 23:35:36.985032	1	2
17	f3fb8f09-591f-417d-82eb-c8527c74c719	42542fc1-a797-4e28-ade2-a6feb5567718	2025-04-20 23:35:55.609229	2025-04-20 23:36:02.908098	1	3
\.


                                                   4999.dat                                                                                            0000600 0004000 0002000 00000001204 15001260361 0014257 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        f3fb8f09-591f-417d-82eb-c8527c74c719	dmitryantipin@gmail.com	\N	$2a$10$Hm18XyMTV903gXijf.1DLexCkfEIxtrQPLwrjApfJrpnKex2GyvyW	1	t	\N	2025-04-16 01:07:23.708074	2025-04-16 01:07:23.631507	2025-04-16 01:07:23.631507	1
5560d4f9-bffa-4572-a03b-a6b68362c718	antoxa@gmail.com	\N	$2a$10$TvHb7uVBPyycI7qUUV5s6esGl.UTw2jx99cqJWv6CzhZVgeXR856y	1	t	\N	2025-04-16 17:16:06.196155	2025-04-16 17:16:05.937114	2025-04-16 17:16:05.937114	1
42542fc1-a797-4e28-ade2-a6feb5567718	dmitryantipin1@gmail.com	\N	$2a$10$H6gkqek8ZE8ao8AVPxUBreRxOTm3dE2cwLAuoNoox/Cqv1HbmLoOe	2	t	\N	2025-04-19 18:15:17.547995	2025-04-19 18:15:17.399897	2025-04-19 18:15:17.399897	1
\.


                                                                                                                                                                                                                                                                                                                                                                                            5014.dat                                                                                            0000600 0004000 0002000 00000000626 15001260361 0014241 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        a339ee62-ba3d-47bc-8bb2-ecf52604e377	5560d4f9-bffa-4572-a03b-a6b68362c718	964496	2025-04-16 17:17:09.68878	2025-04-16 17:22:09.68878
c81f905e-802e-4008-8519-e49be992d3cd	42542fc1-a797-4e28-ade2-a6feb5567718	593181	2025-04-19 18:15:45.394061	2025-04-19 18:20:45.394061
6dd7dbab-ccea-4ff4-9977-2077312526b8	f3fb8f09-591f-417d-82eb-c8527c74c719	665183	2025-04-20 19:35:33.25087	2025-04-20 19:40:33.25087
\.


                                                                                                          restore.sql                                                                                         0000600 0004000 0002000 00000104510 15001260361 0015357 0                                                                                                    ustar 00postgres                        postgres                        0000000 0000000                                                                                                                                                                        --
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.2

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

DROP DATABASE final;
--
-- Name: final; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE final WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'ru-RU';


ALTER DATABASE final OWNER TO postgres;

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
    description character varying(255)
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
    type_id bigint
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
    status public.participant_status DEFAULT 'PENDING'::public.participant_status NOT NULL,
    role public.participant_role DEFAULT 'PARTICIPANT'::public.participant_role NOT NULL
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
-- Name: status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.status (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.status OWNER TO postgres;

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
\.
COPY public.chat_participants (id, user_id, chat_id, joined_at, last_read_at, is_admin) FROM '$$PATH$$/5004.dat';

--
-- Data for Name: chat_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_types (id, name, description) FROM stdin;
\.
COPY public.chat_types (id, name, description) FROM '$$PATH$$/5019.dat';

--
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chats (id, name, event_id, created_at, type_id) FROM stdin;
\.
COPY public.chats (id, name, event_id, created_at, type_id) FROM '$$PATH$$/5003.dat';

--
-- Data for Name: event_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_participants (id, user_id, event_id, joined_at, status, role) FROM stdin;
\.
COPY public.event_participants (id, user_id, event_id, joined_at, status, role) FROM '$$PATH$$/5002.dat';

--
-- Data for Name: event_statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_statuses (id, code, name) FROM stdin;
\.
COPY public.event_statuses (id, code, name) FROM '$$PATH$$/5017.dat';

--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.events (id, title, description, start_time, sport_type, end_time, location, organizer_id, created_at, status) FROM stdin;
\.
COPY public.events (id, title, description, start_time, sport_type, end_time, location, organizer_id, created_at, status) FROM '$$PATH$$/5001.dat';

--
-- Data for Name: friends; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friends (id, user_id, friend_id, status, created_at) FROM stdin;
\.
COPY public.friends (id, user_id, friend_id, status, created_at) FROM '$$PATH$$/5006.dat';

--
-- Data for Name: message_attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message_attachments (id, message_id, file_url, file_name, file_type, file_size) FROM stdin;
\.
COPY public.message_attachments (id, message_id, file_url, file_name, file_type, file_size) FROM '$$PATH$$/5024.dat';

--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, chat_id, sender_id, content, sent_at, is_read, read_at, reply_to_id, status) FROM stdin;
\.
COPY public.messages (id, chat_id, sender_id, content, sent_at, is_read, read_at, reply_to_id, status) FROM '$$PATH$$/5005.dat';

--
-- Data for Name: notification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notification (id, user_id, title, message, type, is_read, created_at, related_entity_type, related_entity_id) FROM stdin;
\.
COPY public.notification (id, user_id, title, message, type, is_read, created_at, related_entity_type, related_entity_id) FROM '$$PATH$$/5015.dat';

--
-- Data for Name: recommendations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recommendations (id, user_id, type, recommended_id, score, created_at) FROM stdin;
\.
COPY public.recommendations (id, user_id, type, recommended_id, score, created_at) FROM '$$PATH$$/5009.dat';

--
-- Data for Name: relationship_statuses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relationship_statuses (id, name, description) FROM stdin;
\.
COPY public.relationship_statuses (id, name, description) FROM '$$PATH$$/5023.dat';

--
-- Data for Name: relationship_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relationship_types (id, name, description) FROM stdin;
\.
COPY public.relationship_types (id, name, description) FROM '$$PATH$$/5021.dat';

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name) FROM stdin;
\.
COPY public.role (id, name) FROM '$$PATH$$/5012.dat';

--
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status (id, name) FROM stdin;
\.
COPY public.status (id, name) FROM '$$PATH$$/5013.dat';

--
-- Data for Name: user_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_activities (id, user_id, activity_type, distance, duration, calories_burned, activity_date, external_id, raw_data, created_at) FROM stdin;
\.
COPY public.user_activities (id, user_id, activity_type, distance, duration, calories_burned, activity_date, external_id, raw_data, created_at) FROM '$$PATH$$/5010.dat';

--
-- Data for Name: user_photos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_photos (id, user_id, photo_url, description, upload_date, is_profile_photo) FROM stdin;
\.
COPY public.user_photos (id, user_id, photo_url, description, upload_date, is_profile_photo) FROM '$$PATH$$/5011.dat';

--
-- Data for Name: user_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_profiles (user_id, first_name, last_name, bio, location, website, avatar_url, sport_type, created_at, updated_at) FROM stdin;
\.
COPY public.user_profiles (user_id, first_name, last_name, bio, location, website, avatar_url, sport_type, created_at, updated_at) FROM '$$PATH$$/5000.dat';

--
-- Data for Name: user_relationships; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_relationships (id, user_id, related_user_id, created_at, updated_at, type_id, status_id) FROM stdin;
\.
COPY public.user_relationships (id, user_id, related_user_id, created_at, updated_at, type_id, status_id) FROM '$$PATH$$/5008.dat';

--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, phone_number, password, role, is_first_enter, end_date, last_login, created_at, updated_at, status) FROM stdin;
\.
COPY public.users (id, email, phone_number, password, role, is_first_enter, end_date, last_login, created_at, updated_at, status) FROM '$$PATH$$/4999.dat';

--
-- Data for Name: verification_code; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verification_code (id, user_id, code, created_at, expires_at) FROM stdin;
\.
COPY public.verification_code (id, user_id, code, created_at, expires_at) FROM '$$PATH$$/5014.dat';

--
-- Name: chat_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chat_types_id_seq', 3, true);


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
-- Name: user_relationships_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_relationships_id_seq', 17, true);


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

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        