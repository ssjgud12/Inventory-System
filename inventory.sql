-- Create database
CREATE DATABASE IF NOT EXISTS inventorysystem;
USE inventorysystem;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Create user table FIRST (required by other tables)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `User_ID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(100) NOT NULL,
  `Email` varchar(150) NOT NULL, 
  `Phone_Number` varchar(20) DEFAULT NULL,
  `Role` enum('Admin','Customer','Staff','Manager') NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`User_ID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- Insert users,email,phone-number,role,password
INSERT INTO `user` (`Username`, `Email`, `Phone_Number`, `Role`, `Password`) VALUES
('sam','sam@example.com','083-306-4831','Admin','sam123'),
('daniel','daniel@example.com','083-409-8943','Manager','1234'),
('guest','guest@example.com','123-456-7890','Customer','guest1'),
('user','user@example.com','234-156-2345','Customer','test'),
('vicky','vicky@example.com','083-777-2345','Customer','cooking'),
('soma','yuki@example.com','897-484-3450','Customer','spices'),
('arthur','pencilgon@example.com','087-677-9000','Customer','bball'),
('erina','alice@example.com','879-942-2345','Customer','tenths'),
('judge','vinsmoke@example.com','086-156-2345','Customer','germa');

-- Create category table
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `Category_ID` int NOT NULL AUTO_INCREMENT,
  `Category_Name` varchar(100) NOT NULL,
  `Description` text,
  PRIMARY KEY (`Category_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Insert CategoryName/Description--
INSERT INTO category (Category_Name, Description) VALUES
('Electronics','Devices and Gadgets'),
('Electronics','Gaming Keyboard & Mouses'),
('Electronics','Earphones and Headphones'),
('Furniture','Kitchen Furniture'),
('Women Clothing','Women clothing'),
('Men Clothing','Men Clothing');

-- Create supplier table
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `Supplier_ID` int NOT NULL AUTO_INCREMENT,
  `Supplier_Name` varchar(150) NOT NULL,
  `Contact_Person` varchar(100) DEFAULT NULL,
  `Phone_Number` varchar(20) DEFAULT NULL,
  `Email` varchar(150) DEFAULT NULL,
  `Address` text,
  PRIMARY KEY (`Supplier_ID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

INSERT INTO supplier (Supplier_Name, Contact_Person, Phone_Number, Email, Address) VALUES
('TechSupply Co.', 'John Doe', '111-222-3333', 'contact@techsupply.com', '123 Tech Street'),
('HomeFurnish Ltd.', 'Jane Smith', '444-555-6666', 'support@homefurnish.com', '456 Furniture Lane'),
('Magee Co.', 'John Smith', '011-8787-900', 'support@mage.com', '456 Memory Lane'),
('Rent-The-Runway.co', 'Lebron James', '099-777-2222', 'support@renttherunway.com', '123 Wall Street');

-- Create product table
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `Product_ID` int NOT NULL AUTO_INCREMENT,
  `Product_Name` varchar(150) NOT NULL,
  `Description` text,
  `Price` decimal(10,2) NOT NULL,
  `Category_ID` int DEFAULT NULL,
  `Supplier_ID` int DEFAULT NULL,
  `Stock_Quantity` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`Product_ID`),
  KEY `Category_ID` (`Category_ID`),
  KEY `Supplier_ID` (`Supplier_ID`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`Category_ID`) REFERENCES `category` (`Category_ID`) ON DELETE SET NULL,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`Supplier_ID`) REFERENCES `supplier` (`Supplier_ID`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

INSERT INTO product(Product_Name,Description,Price,Category_ID,Supplier_ID,Stock_Quantity) VALUES
('Laptop','High-performance laptop',1200.00,1,1,10),
('Office Chair','Ergonomic office chair',150.00,2,2,15),
('Gaming Monitor','144Hz gaming monitor',120.00,1,1,20),
('Standing desk','Adjustable standing desk',80.00,2,2,12),
('Laptop','High-performance laptop',1200.00,1,1,10),
('Smartphone','Latest model smartphone',800.00,1,1,15),
('Microwave','High-power microwave oven',200.00,1,2,8),
('Mens Clothing','T-Shirt',50.00,1,1,15),
('Mens Clothing','Shirt',52.00,1,1,14),
('Mens Clothing','Shorts',48.00,1,1,16),
('Mens Clothing','Jumpers',55.00,1,1,13),
('Mens Clothing','Jumpers',57.00,1,1,12),
('Mens Clothing','Polo Shirts',53.00,1,1,15),
('Mens Clothing','Hoodies',60.00,1,1,14),
('Mens Clothing','T-Shirt',51.00,1,1,13),
('Womens Clothing','Midi Dresses',50.00,1,1,15),
('Womens Clothing','Summer Dresses',49.00,1,1,14),
('Womens Clothing','Short Dresses',52.00,1,1,13),
('Womens Clothing','Long Dresses',55.00,1,1,16),
('Womens Clothing','Skirts',45.00,1,1,17),
('Womens Clothing','Leggings',40.00,1,1,18),
('Womens Clothing','Tops',47.00,1,1,15);  



-- Create orders table
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `Order_ID` int NOT NULL AUTO_INCREMENT,
  `User_ID` int NOT NULL,
  `Total_Amount` decimal(10,2) NOT NULL,
  `Order_Status` enum('Pending','Shipped','Delivered','Cancelled') DEFAULT 'Pending',
  `Order_Date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Order_ID`),
  KEY `User_ID` (`User_ID`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

INSERT INTO `orders` VALUES
(1,3,2550.00,'Pending','2025-03-29 18:53:53'),
(2,4,800.00,'Shipped','2025-03-29 18:53:53');

-- Create basket table
DROP TABLE IF EXISTS `basket`;
CREATE TABLE `basket` (
  `Basket_ID` int NOT NULL AUTO_INCREMENT,
  `User_ID` int NOT NULL,
  `Product_ID` int NOT NULL,
  `Quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`Basket_ID`),
  KEY `User_ID` (`User_ID`),
  KEY `Product_ID` (`Product_ID`),
  CONSTRAINT `basket_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`) ON DELETE CASCADE,
  CONSTRAINT `basket_ibfk_2` FOREIGN KEY (`Product_ID`) REFERENCES `product` (`Product_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

INSERT INTO `basket` VALUES
(4,3,1,1),
(5,3,3,2),
(6,4,2,1);

-- Create inventory_transaction table
DROP TABLE IF EXISTS `inventory_transaction`;
CREATE TABLE `inventory_transaction` (
  `Transaction_ID` int NOT NULL AUTO_INCREMENT,
  `Product_ID` int NOT NULL,
  `User_ID` int NOT NULL,
  `Transaction_Type` enum('Stock In','Stock Out') NOT NULL,
  `Quantity` int NOT NULL,
  `Transaction_Date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Transaction_ID`),
  KEY `Product_ID` (`Product_ID`),
  KEY `User_ID` (`User_ID`),
  CONSTRAINT `inventory_transaction_ibfk_1` FOREIGN KEY (`Product_ID`) REFERENCES `product` (`Product_ID`) ON DELETE CASCADE,
  CONSTRAINT `inventory_transaction_ibfk_2` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

INSERT INTO `inventory_transaction` VALUES
(1,1,1,'Stock In',5,'2025-03-28 21:28:16'),
(2,1,2,'Stock Out',2,'2025-03-28 21:28:16'),
(3,2,1,'Stock In',5,'2025-03-28 21:46:18'),
(4,3,2,'Stock Out',2,'2025-03-28 21:46:18');

-- Create order_items table
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `Order_Item_ID` int NOT NULL AUTO_INCREMENT,
  `Order_ID` int NOT NULL,
  `Product_ID` int NOT NULL,
  `Quantity` int NOT NULL,
  `Subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`Order_Item_ID`),
  KEY `Order_ID` (`Order_ID`),
  KEY `Product_ID` (`Product_ID`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`Order_ID`) REFERENCES `orders` (`Order_ID`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`Product_ID`) REFERENCES `product` (`Product_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

INSERT INTO `order_items` VALUES
(1,1,1,1,1200.00),
(2,1,2,2,300.00),
(3,2,6,1,800.00);

-- Enable foreign key checks again
SET FOREIGN_KEY_CHECKS = 1;