--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements
insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212');

INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('1', true, 'direccion 1', 'cliente 1');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('2', true, 'direccion 2', 'cliente 2');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('3', true, 'direccion 3', 'cliente 3');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('4', true, 'direccion 4', 'cliente 4');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('5', true, 'direccion 5', 'cliente 5');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('6', true, 'direccion 6', 'cliente 6');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('7', true, 'direccion 7', 'cliente 7');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('8', true, 'direccion 8', 'cliente 8');
INSERT INTO clientes(ruc, activo, direccion, nombre) VALUES ('9', true, 'direccion 9', 'cliente 9');

INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('1', true, 'direccion 1', 'proveedor 1');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('2', true, 'direccion 2', 'proveedor 2');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('3', true, 'direccion 3', 'proveedor 3');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('4', true, 'direccion 4', 'proveedor 4');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('5', true, 'direccion 5', 'proveedor 5');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('6', true, 'direccion 6', 'proveedor 6');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('7', true, 'direccion 7', 'proveedor 7');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('8', true, 'direccion 8', 'proveedor 8');
INSERT INTO proveedores(ruc, activo, direccion, nombre) VALUES ('9', true, 'direccion 9', 'proveedor 9');

INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (1, true, 'Producto 1', 100, 10);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (2, true, 'Producto 2', 200, 20);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (3, true, 'Producto 3', 300, 30);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (4, true, 'Producto 4', 400, 40);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (5, true, 'Producto 5', 500, 50);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (6, true, 'Producto 6', 600, 60);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (7, true, 'Producto 7', 700, 70);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (8, true, 'Producto 8', 800, 80);
INSERT INTO productos(id, activo, nombre, precio, stock) VALUES (9, true, 'Producto 9', 900, 90);

INSERT INTO compras(id, fecha, monto_total, ruc_proveedor) VALUES (1, '2015/10/20', 14400, 1);
INSERT INTO compras(id, fecha, monto_total, ruc_proveedor) VALUES (2, '2015/10/21', 14400, 2);
INSERT INTO compras(id, fecha, monto_total, ruc_proveedor) VALUES (3, '2015/10/22', 14400, 3);

INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (1, 5, 100, 1, 1);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (2, 10, 200, 1, 2);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (3, 15, 300, 1, 3);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (4, 20, 400, 1, 4);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (5, 5, 100, 2, 3);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (6, 10, 200, 2, 4);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (7, 15, 300, 2, 5);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (8, 20, 400, 2, 6);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (9, 5, 100, 3, 5);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (10, 10, 200, 3, 6);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (11, 15, 300, 3, 7);
INSERT INTO compra_detalle(id, cantidad, precio, id_compra, id_producto) VALUES (12, 20, 400, 3, 8);

INSERT INTO facturas(id, id_venta_cabecera, impreso, total) VALUES (1, 1, true, 1);
INSERT INTO facturas(id, id_venta_cabecera, impreso, total) VALUES (2, 2, true, 2);

INSERT INTO ventas(numero, fecha, monto_total, nombre_cliente, ruc_cliente, id_factura) VALUES (1, '2015/10/20', 2, 'Lo que sea', '1', 1);
INSERT INTO ventas(numero, fecha, monto_total, nombre_cliente, ruc_cliente, id_factura) VALUES (2, '2015/10/20', 2, 'Lo que sea', '1', 2);


INSERT INTO venta_detalle(id, cantidad, id_venta, precio, id_producto, numero) VALUES (1, 1, 1, 1, 1, 1);
INSERT INTO venta_detalle(id, cantidad, id_venta, precio, id_producto, numero) VALUES (2, 1, 1, 1, 1, 1);
INSERT INTO venta_detalle(id, cantidad, id_venta, precio, id_producto, numero) VALUES (3, 1, 2, 2, 2, 2);


