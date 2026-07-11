-- PROYECTO FINAL - BASE DE DATOS I

Drop Table COMPRA;
Drop Table RESTRICCION_HORARIO;
Drop Table SOLICITUD_SOBRECUPO;
Drop Table SUPERVISOR;
Drop Table PAREJA;
Drop Table ALMACEN;
Drop Table CLIENTE;


-- Tabla: CLIENTE
CREATE TABLE CLIENTE (
                         id_usuario               BIGINT PRIMARY KEY,
                         nombre_usuario           VARCHAR(30) NOT NULL UNIQUE,
                         contrasenia              VARCHAR(30) NOT NULL,
                         estado                   VARCHAR(30) NOT NULL CHECK (estado IN ('Activo', 'Inactivo')),
                         primer_nombre            VARCHAR(30) NOT NULL,
                         segundo_nombre           VARCHAR(30),
                         primer_apellido          VARCHAR(30) NOT NULL,
                         segundo_apellido         VARCHAR(30),
                         telefono                 VARCHAR(30),
                         cupo_propio              DECIMAL(10,2) NOT NULL CHECK (cupo_propio >= 0)
);

-- Tabla: ALMACEN
CREATE TABLE ALMACEN (
                         id_almacen               BIGSERIAL PRIMARY KEY,
                         nombre_almacen           VARCHAR(30) NOT NULL,
                         ubicacion_ciudad         VARCHAR(30) NOT NULL,
                         ubicacion_avenida        VARCHAR(30),
                         ubicacion_calle          VARCHAR(30)
);

-- Tabla: PAREJA
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

-- Tabla: SUPERVISOR
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

-- Tabla: COMPRA
-- id_usuario_pareja / id_usuario_cliente: arco exclusivo, exactamente uno de los dos debe estar lleno
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

-- Tabla: RESTRICCION_HORARIO
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

-- Tabla: SOLICITUD_SOBRECUPO
-- id_usuario_supervisor es nullable porque el supervisor no siempre interviene
-- monto_autorizado es nullable porque es NULL mientras la solicitud esta pendiente
-- no tiene id_compra: aprobar una solicitud nunca crea una Compra, solo reasigna cupo
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





