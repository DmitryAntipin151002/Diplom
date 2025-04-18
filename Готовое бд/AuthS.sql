PGDMP                      }            AuthS    17.4    17.2     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16388    AuthS    DATABASE     m   CREATE DATABASE "AuthS" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'ru-RU';
    DROP DATABASE "AuthS";
                     postgres    false            �            1259    16393    role    TABLE     _   CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
    DROP TABLE public.role;
       public         heap r       postgres    false            �            1259    16396    role_id_seq    SEQUENCE     �   CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.role_id_seq;
       public               postgres    false    217            �           0    0    role_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;
          public               postgres    false    218            �            1259    16397    status    TABLE     a   CREATE TABLE public.status (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
    DROP TABLE public.status;
       public         heap r       postgres    false            �            1259    16400    status_id_seq    SEQUENCE     �   CREATE SEQUENCE public.status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.status_id_seq;
       public               postgres    false    219            �           0    0    status_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.status_id_seq OWNED BY public.status.id;
          public               postgres    false    220            �            1259    16401    users    TABLE     [  CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    email character varying(100) NOT NULL,
    phone_number character varying(15),
    encrypted_password character(60) NOT NULL,
    status bigint,
    is_first_enter boolean DEFAULT false NOT NULL,
    end_date date,
    role_id bigint NOT NULL,
    last_login date
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    16409    verification_code    TABLE     �   CREATE TABLE public.verification_code (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    code character varying(6) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    expires_at timestamp without time zone NOT NULL
);
 %   DROP TABLE public.verification_code;
       public         heap r       postgres    false            .           2604    16413    role id    DEFAULT     b   ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);
 6   ALTER TABLE public.role ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217            /           2604    16414 	   status id    DEFAULT     f   ALTER TABLE ONLY public.status ALTER COLUMN id SET DEFAULT nextval('public.status_id_seq'::regclass);
 8   ALTER TABLE public.status ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219            �          0    16393    role 
   TABLE DATA           (   COPY public.role (id, name) FROM stdin;
    public               postgres    false    217   �       �          0    16397    status 
   TABLE DATA           *   COPY public.status (id, name) FROM stdin;
    public               postgres    false    219           �          0    16401    users 
   TABLE DATA           �   COPY public.users (id, email, phone_number, encrypted_password, status, is_first_enter, end_date, role_id, last_login) FROM stdin;
    public               postgres    false    221   -        �          0    16409    verification_code 
   TABLE DATA           V   COPY public.verification_code (id, user_id, code, created_at, expires_at) FROM stdin;
    public               postgres    false    222   �        �           0    0    role_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.role_id_seq', 1, false);
          public               postgres    false    218            �           0    0    status_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.status_id_seq', 1, false);
          public               postgres    false    220            3           2606    16420    role role_name_key 
   CONSTRAINT     M   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_name_key UNIQUE (name);
 <   ALTER TABLE ONLY public.role DROP CONSTRAINT role_name_key;
       public                 postgres    false    217            5           2606    16422    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public                 postgres    false    217            7           2606    16424    status status_name_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.status
    ADD CONSTRAINT status_name_key UNIQUE (name);
 @   ALTER TABLE ONLY public.status DROP CONSTRAINT status_name_key;
       public                 postgres    false    219            9           2606    16426    status status_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.status
    ADD CONSTRAINT status_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.status DROP CONSTRAINT status_pkey;
       public                 postgres    false    219            ;           2606    16430    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public                 postgres    false    221            =           2606    16432    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    221            ?           2606    16434 (   verification_code verification_code_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.verification_code
    ADD CONSTRAINT verification_code_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.verification_code DROP CONSTRAINT verification_code_pkey;
       public                 postgres    false    222            @           2606    16445    users users_role_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.role(id) ON DELETE CASCADE;
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_role_id_fkey;
       public               postgres    false    4661    221    217            A           2606    16450    users users_status_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_status_fkey FOREIGN KEY (status) REFERENCES public.status(id) ON DELETE SET NULL;
 A   ALTER TABLE ONLY public.users DROP CONSTRAINT users_status_fkey;
       public               postgres    false    221    219    4665            �      x�3�tt����2�v����� +��      �      x�3�tt�s����� ��      �   �   x�Ż�0 й�6S�-�$H�A4��\\xh	��׳�v�"�sD����$�U]J����-|����~�F�M�dE�F�F���P�WӾ��ln��\^��۟�%�t�Dv}��T4�+�C��Q[��'�
"nc��<G)�      �     x�e��u1EcMn�9�L-N���K���w���c�z��FMHdJ���w�]{�Y!�К�s��&�ɪ�lX� ������0��]�!#t��� 0rV��{T���%8̈́�d�;�i���&�������[H�C�E��V��@<�9װ�N0gWl���# �+��F�s댨�,���!�q�C���H$��^t�E��"�<�Ρ=�8��Z�u3Ng�2O{@6V��:ר�03�~���;�E�	�������z5     