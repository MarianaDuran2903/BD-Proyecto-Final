Drop Table COMPRA;
Drop Table RESTRICCION_HORARIO;
Drop Table SOLICITUD_SOBRECUPO;
Drop Table SUPERVISOR;
Drop Table PAREJA;
Drop Table ALMACEN;
Drop Table CLIENTE;


CREATE TABLE CLIENTE (
                         id_usuario               BIGINT PRIMARY KEY,
                         nombre_usuario           VARCHAR(30) NOT NULL UNIQUE,
                         contrasenia              VARCHAR(30) NOT NULL,
                         estado                   VARCHAR(30) NOT NULL CHECK (estado IN ('Activo', 'Inactivo', 'Pendiente')),
                         primer_nombre            VARCHAR(30) NOT NULL,
                         segundo_nombre           VARCHAR(30),
                         primer_apellido          VARCHAR(30) NOT NULL,
                         segundo_apellido         VARCHAR(30),
                         telefono                 VARCHAR(30),
                         cupo_propio              DECIMAL(10,2) NOT NULL CHECK (cupo_propio >= 0),
                         cupo_total_solicitado    DECIMAL(10,2) CHECK (cupo_total_solicitado >= 0),
                         cupo_total_autorizado    DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (cupo_total_autorizado >= 0)
);

CREATE TABLE ALMACEN (
                         id_almacen               BIGSERIAL PRIMARY KEY,
                         nombre_almacen           VARCHAR(30) NOT NULL,
                         ubicacion_ciudad         VARCHAR(30) NOT NULL,
                         ubicacion_avenida        VARCHAR(30),
                         ubicacion_calle          VARCHAR(30)
);

CREATE TABLE PAREJA (
                        id_usuario               BIGINT PRIMARY KEY,
                        nombre_usuario           VARCHAR(30) NOT NULL UNIQUE,
                        contrasenia              VARCHAR(30) NOT NULL,
                        estado                   VARCHAR(30) NOT NULL CHECK (estado IN ('Activo', 'Inactivo')),
                        primer_nombre            VARCHAR(30) NOT NULL,
                        segundo_nombre           VARCHAR(30),
                        primer_apellido          VARCHAR(30) NOT NULL,
                        segundo_apellido         VARCHAR(30),
                        telefono                 VARCHAR(30),
                        cupo_asignado            DECIMAL(10,2) NOT NULL CHECK (cupo_asignado >= 0),
                        id_usuario_cliente       BIGINT NOT NULL,
                        FOREIGN KEY (id_usuario_cliente) REFERENCES CLIENTE (id_usuario)
);

CREATE TABLE SUPERVISOR (
                            id_usuario               BIGINT PRIMARY KEY,
                            nombre_usuario           VARCHAR(30) NOT NULL UNIQUE,
                            contrasenia              VARCHAR(30) NOT NULL,
                            estado                   VARCHAR(30) NOT NULL CHECK (estado IN ('Activo', 'Inactivo')),
                            correo                   VARCHAR(30),
                            telefono                 VARCHAR(30),
                            primer_nombre            VARCHAR(30) NOT NULL,
                            segundo_nombre           VARCHAR(30),
                            primer_apellido          VARCHAR(30) NOT NULL,
                            segundo_apellido         VARCHAR(30),
                            id_almacen               BIGINT NOT NULL,
                            FOREIGN KEY (id_almacen) REFERENCES ALMACEN (id_almacen)
);

CREATE TABLE COMPRA (
                        cod_compra               BIGSERIAL PRIMARY KEY,
                        monto                    DECIMAL(10,2) NOT NULL CHECK (monto >= 0),
                        fecha                    DATE NOT NULL,
                        hora                     TIME NOT NULL,
                        id_usuario_pareja        BIGINT,
                        id_usuario_cliente       BIGINT,
                        id_almacen               BIGINT NOT NULL,
                        id_usuario_supervisor    BIGINT NOT NULL,
                        FOREIGN KEY (id_usuario_pareja) REFERENCES PAREJA (id_usuario),
                        FOREIGN KEY (id_usuario_cliente) REFERENCES CLIENTE (id_usuario),
                        FOREIGN KEY (id_almacen) REFERENCES ALMACEN (id_almacen),
                        FOREIGN KEY (id_usuario_supervisor) REFERENCES SUPERVISOR (id_usuario),
                        CHECK (
                            (id_usuario_pareja IS NOT NULL AND id_usuario_cliente IS NULL)
                                OR (id_usuario_pareja IS NULL AND id_usuario_cliente IS NOT NULL)
                            )
);

CREATE TABLE RESTRICCION_HORARIO (
                                     id_restriccion           BIGSERIAL PRIMARY KEY,
                                     motivo                   VARCHAR(30),
                                     dia_bloqueo              DATE NOT NULL,
                                     hora_bloqueo_inicio      TIME NOT NULL,
                                     hora_bloqueo_fin         TIME NOT NULL,
                                     id_usuario_pareja        BIGINT NOT NULL,
                                     FOREIGN KEY (id_usuario_pareja) REFERENCES PAREJA (id_usuario),
                                     CHECK (hora_bloqueo_fin > hora_bloqueo_inicio)
);

CREATE TABLE SOLICITUD_SOBRECUPO (
                                     cod_solicitud            BIGSERIAL PRIMARY KEY,
                                     fecha                    DATE NOT NULL,
                                     hora                     TIME NOT NULL,
                                     monto_solicitado         DECIMAL(10,2) NOT NULL CHECK (monto_solicitado >= 0),
                                     monto_autorizado         DECIMAL(10,2) CHECK (monto_autorizado >= 0),
                                     estado                   VARCHAR(30) NOT NULL CHECK (estado IN ('pendiente_cliente', 'aprobada_directa', 'pendiente_supervisor', 'aprobada_supervisor', 'rechazada_cliente', 'rechazada_supervisor')),
                                     id_usuario_cliente       BIGINT NOT NULL,
                                     id_usuario_pareja        BIGINT NOT NULL,
                                     id_usuario_supervisor    BIGINT,
                                     FOREIGN KEY (id_usuario_cliente) REFERENCES CLIENTE (id_usuario),
                                     FOREIGN KEY (id_usuario_pareja) REFERENCES PAREJA (id_usuario),
                                     FOREIGN KEY (id_usuario_supervisor) REFERENCES SUPERVISOR (id_usuario)
);

INSERT INTO ALMACEN (nombre_almacen, ubicacion_ciudad, ubicacion_avenida, ubicacion_calle)
VALUES ('Almacén Norte', 'Bogotá', 'Cra 15', 'Calle 100');

INSERT INTO SUPERVISOR (id_usuario, nombre_usuario, contrasenia, estado, correo, telefono,
                        primer_nombre, primer_apellido, id_almacen)
VALUES (1000000001, 'supervisor1', '1234', 'Activo', 'supervisor1@mercacredit.com', '3001234567',
        'Carlos', 'Ramírez', 1);

INSERT INTO CLIENTE (id_usuario, nombre_usuario, contrasenia, estado,
                     primer_nombre, primer_apellido, telefono,
                     cupo_propio, cupo_total_solicitado, cupo_total_autorizado)
VALUES (1023456789, 'juan1023456789', '1234', 'Activo',
        'Juan', 'Herrera', '3109876543',
        800000, 1100000, 1100000);

INSERT INTO PAREJA (id_usuario, nombre_usuario, contrasenia, estado,
                    primer_nombre, primer_apellido, telefono, cupo_asignado, id_usuario_cliente)
VALUES (2001234567, 'maria2001234567', '1234', 'Activo',
        'María', 'González', '3201112233', 300000, 1023456789);

INSERT INTO COMPRA (monto, fecha, hora, id_usuario_pareja, id_usuario_cliente, id_almacen, id_usuario_supervisor)
VALUES (120000, CURRENT_DATE, '14:30:00', 2001234567, NULL, 1, 1000000001);

INSERT INTO COMPRA (monto, fecha, hora, id_usuario_pareja, id_usuario_cliente, id_almacen, id_usuario_supervisor)
VALUES (85000, CURRENT_DATE, '10:15:00', NULL, 1023456789, 1, 1000000001);



