# First Time Setup
CREATE USER 'treffpunkt'@'localhost' IDENTIFIED BY 'SOME_PASSWORD';
CREATE DATABASE treffpunkt;
GRANT ALL PRIVILEGES ON treffpunkt.* TO 'treffpunkt'@'localhost';
FLUSH PRIVILEGES;
