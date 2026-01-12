-- Alter contact and whatsapp number columns
ALTER TABLE users 
MODIFY contact_no VARCHAR(15) NOT NULL;

ALTER TABLE users 
MODIFY whatsapp_no VARCHAR(15);

