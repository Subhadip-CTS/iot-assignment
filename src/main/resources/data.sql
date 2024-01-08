INSERT INTO driver (name, address, licence_number, phone_number)
VALUES ('John', 'Pune', 'LIN321', '1234567890'),
('Peter', 'Pune', 'RIN321', '1234567890');


INSERT INTO Fleet (count, route)
VALUES (1, 'Pune'),
(1, 'Mumbai'),
(2, 'Delhi'),
(1, 'Kolkata');


INSERT INTO Vehicle (registration_number, model, style,fleet_id)
VALUES ('MH12', 'Mustang', 'Sports',1),
('MH01', 'Fiesta', 'Car',2),
('KL09', 'F150', 'Truck',3),
('KL49', 'Fig', 'Car',3),
('WB01', 'Fiesta', 'Car',4);

INSERT INTO telemetry (driver_id, vehicle_id, parameter, param_value, time)
VALUES (1, 1, 'fast_acceleration', 7, CURRENT_TIMESTAMP(0)),
(1, 1, 'distance', 30, CURRENT_TIMESTAMP(0)+1),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(1, 1, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2),
(2, 2, 'distance', 30, CURRENT_TIMESTAMP(0)),
(2, 2, 'fast_acceleration', 7, CURRENT_TIMESTAMP(0)+1),
(2, 2, 'seat_belt', 1, CURRENT_TIMESTAMP(0)+2);


