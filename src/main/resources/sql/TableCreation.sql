-- Rene Ziller, Rene.Ziller@stud-inf.unibz.it

CREATE TABLE IF NOT EXISTS Product (
ProductId uuid NOT NULL,
Name varchar(100),
Description text,
Price float,
PRIMARY KEY(ProductId));


CREATE TABLE IF NOT EXISTS PaymentMethodEnum (
PaymentMethodEnumId int NOT NULL,
Name varchar(50),
Description varchar (255),
PRIMARY KEY (PaymentMethodEnumId));


CREATE TABLE IF NOT EXISTS OrderStatusEnum (
OrderStatusEnumId int NOT NULL,
Name varchar(50),
Description varchar (255),
PRIMARY KEY	(OrderStatusEnumId));

	
CREATE TABLE IF NOT EXISTS Country (
CountryId int NOT NULL,
Iso char(2) NOT NULL,
Name varchar(80) NOT NULL,
DisplayName varchar(80) NOT NULL,
Iso3 char(3) DEFAULT NULL,
Numcode int DEFAULT NULL,
Phonecode int NOT NULL,
PRIMARY KEY (CountryId));


CREATE TABLE IF NOT EXISTS Address (
AddressId uuid NOT NULL,
Name varchar(100),
Surname varchar(100),
Street varchar(255),
ZIPCode varchar(20),
City varchar(255),
CountryId int,
PRIMARY KEY (AddressId),
FOREIGN KEY (CountryId) REFERENCES Country(CountryId));


CREATE TABLE IF NOT EXISTS Customer (
CustomerId varchar(255) NOT NULL,
IsRegistered boolean DEFAULT false,
IsAdmin boolean DEFAULT false,
Name varchar(40) NOT NULL DEFAULT 'guest',
Surname varchar(40) NOT NULL DEFAULT 'guest',
EMail varchar (40),
ShippingAddressId uuid,
BillingAddressId uuid,
PaymentMethodEnumId int,
PRIMARY KEY (CustomerId),
FOREIGN KEY (ShippingAddressId) REFERENCES Address(AddressId),
FOREIGN KEY (BillingAddressId) REFERENCES Address(AddressId),
FOREIGN KEY (PaymentMethodEnumId) REFERENCES PaymentMethodEnum(PaymentMethodEnumId));


CREATE TABLE IF NOT EXISTS CustOrder (
CustOrderId uuid NOT NULL,
CustomerId varchar(255),
OrderDate timestamp,
ShippingAddressId uuid,
BillingAddressId uuid,
PaymentMethodEnumId int,
OrderStatusEnumId int,
PRIMARY KEY (CustOrderId),
FOREIGN KEY (ShippingAddressId) REFERENCES Address(AddressId),
FOREIGN KEY (BillingAddressId) REFERENCES Address(AddressId),
FOREIGN KEY (PaymentMethodEnumId) REFERENCES PaymentMethodEnum(PaymentMethodEnumId),
FOREIGN KEY (OrderStatusEnumId) REFERENCES OrderStatusEnum(OrderStatusEnumId));


CREATE TABLE IF NOT EXISTS CustOrderLine (
CustOrderLineId uuid NOT NULL,
CustOrderId uuid NOT NULL,
ProductId uuid NOT NULL,
Qty int,
Price float,
PRIMARY KEY(CustOrderLineId),
FOREIGN KEY (CustOrderId) REFERENCES CustOrder(CustOrderId),
FOREIGN KEY (ProductId) REFERENCES Product(ProductId));

