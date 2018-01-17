# First Time Setup
CREATE USER 'treffpunkt'@'localhost' IDENTIFIED BY 'SOME_PASSWORD';
CREATE DATABASE treffpunkt;
GRANT ALL PRIVILEGES ON mydb.treffpunkt TO 'treffpunkt'@'localhost';
FLUSH PRIVILEGES;
