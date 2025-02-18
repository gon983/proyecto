-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: proyecto
-- ------------------------------------------------------
-- Server version	11.6.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `id_city` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  `fk_country` uuid NOT NULL,
  PRIMARY KEY (`id_city`),
  UNIQUE KEY `id_city_UNIQUE` (`id_city`),
  KEY `fk_country_idx` (`fk_country`),
  CONSTRAINT `fk_country_city` FOREIGN KEY (`fk_country`) REFERENCES `country` (`id_country`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES ('602ec196-d4d7-4194-a660-9b4afc9a07ea','Cordoba','6a9fe61b-be17-4821-a4a3-71fa282e9289'),('49519714-aad2-4b19-8f6b-bf688e565642','CABA','6a9fe61b-be17-4821-a4a3-71fa282e9289'),('29559450-d74c-45a0-83f4-cdfc0a55db7d','Rosario','6a9fe61b-be17-4821-a4a3-71fa282e9289');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collectionpoint`
--

DROP TABLE IF EXISTS `collectionpoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collectionpoint` (
  `id_collection_point` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  `fk_neighborhood` uuid NOT NULL,
  `use_price` double DEFAULT NULL,
  `fk_owner` uuid NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `ubication` point NOT NULL,
  PRIMARY KEY (`id_collection_point`),
  KEY `fk_neighborhood_idx` (`fk_neighborhood`),
  KEY `fk_owner_idx` (`fk_owner`),
  CONSTRAINT `fk_neighborhood_collection_point` FOREIGN KEY (`fk_neighborhood`) REFERENCES `neighborhood` (`id_neighborhood`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_owner_collection_point` FOREIGN KEY (`fk_owner`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collectionpoint`
--

LOCK TABLES `collectionpoint` WRITE;
/*!40000 ALTER TABLE `collectionpoint` DISABLE KEYS */;
/*!40000 ALTER TABLE `collectionpoint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collectionpointhistory`
--

DROP TABLE IF EXISTS `collectionpointhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collectionpointhistory` (
  `id_collection_point_history` uuid NOT NULL,
  `fk_collection_point` uuid NOT NULL,
  `fk_collection_point_state` uuid NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `init` datetime NOT NULL,
  `finish` datetime DEFAULT NULL,
  PRIMARY KEY (`id_collection_point_history`),
  KEY `fk_collection_point_idx` (`fk_collection_point`),
  KEY `fk_collection_point_state_idx` (`fk_collection_point_state`),
  CONSTRAINT `fk_collection_point_collection_point_history` FOREIGN KEY (`fk_collection_point`) REFERENCES `collectionpoint` (`id_collection_point`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_collection_point_state_collection_point_history` FOREIGN KEY (`fk_collection_point_state`) REFERENCES `collectionpointstate` (`id_collection_point_state`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collectionpointhistory`
--

LOCK TABLES `collectionpointhistory` WRITE;
/*!40000 ALTER TABLE `collectionpointhistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `collectionpointhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collectionpointpayments`
--

DROP TABLE IF EXISTS `collectionpointpayments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collectionpointpayments` (
  `id_collection_point_payments` uuid NOT NULL,
  `fk_collection_point` uuid NOT NULL,
  `date` datetime NOT NULL,
  `note` varchar(45) NOT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id_collection_point_payments`),
  KEY `fk_collection_point_idx` (`fk_collection_point`),
  CONSTRAINT `fk_collection_point_collection_point_payment` FOREIGN KEY (`fk_collection_point`) REFERENCES `collectionpoint` (`id_collection_point`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collectionpointpayments`
--

LOCK TABLES `collectionpointpayments` WRITE;
/*!40000 ALTER TABLE `collectionpointpayments` DISABLE KEYS */;
/*!40000 ALTER TABLE `collectionpointpayments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collectionpointstate`
--

DROP TABLE IF EXISTS `collectionpointstate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collectionpointstate` (
  `id_collection_point_state` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_collection_point_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collectionpointstate`
--

LOCK TABLES `collectionpointstate` WRITE;
/*!40000 ALTER TABLE `collectionpointstate` DISABLE KEYS */;
INSERT INTO `collectionpointstate` VALUES ('eb9caee8-dc88-4bcb-a804-3ab6fa0984a1','pending');
/*!40000 ALTER TABLE `collectionpointstate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `country` (
  `id_country` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_country`),
  UNIQUE KEY `id_country_UNIQUE` (`id_country`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES ('6a9fe61b-be17-4821-a4a3-71fa282e9289','Argentina'),('de76b765-5927-4fa4-965b-031835705b18','Brasil'),('578dde76-7898-41a1-8ebd-4338f2cb12ba','Chile'),('91c06a87-4500-4223-9a08-42bcd4786681','Peru'),('6a79ddc2-60e2-4be4-9b11-dcc7a894c4af','Uruguay'),('9bba1fb7-b382-4fb3-a205-372a5bd3caec','Venezuela');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locality`
--

DROP TABLE IF EXISTS `locality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locality` (
  `id_locality` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  `fk_city` uuid NOT NULL,
  PRIMARY KEY (`id_locality`),
  UNIQUE KEY `id_locality_UNIQUE` (`id_locality`),
  KEY `fk_city_idx` (`fk_city`),
  CONSTRAINT `fk_city_locality` FOREIGN KEY (`fk_city`) REFERENCES `city` (`id_city`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locality`
--

LOCK TABLES `locality` WRITE;
/*!40000 ALTER TABLE `locality` DISABLE KEYS */;
INSERT INTO `locality` VALUES ('10fbe8c3-0291-4bc7-a96a-7d10782b36ec','Capital','602ec196-d4d7-4194-a660-9b4afc9a07ea');
/*!40000 ALTER TABLE `locality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `neighborhood`
--

DROP TABLE IF EXISTS `neighborhood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `neighborhood` (
  `id_neighborhood` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  `fk_locality` uuid NOT NULL,
  PRIMARY KEY (`id_neighborhood`),
  UNIQUE KEY `id_neighborhood_UNIQUE` (`id_neighborhood`),
  KEY `fk_neighborhood_idx` (`fk_locality`),
  CONSTRAINT `fk_locality_neighborhood` FOREIGN KEY (`fk_locality`) REFERENCES `locality` (`id_locality`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `neighborhood`
--

LOCK TABLES `neighborhood` WRITE;
/*!40000 ALTER TABLE `neighborhood` DISABLE KEYS */;
INSERT INTO `neighborhood` VALUES ('23f4f7d4-7923-495d-85d0-27f34d932537','Barrio Jardin','10fbe8c3-0291-4bc7-a96a-7d10782b36ec');
/*!40000 ALTER TABLE `neighborhood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `neighborhoodpackage`
--

DROP TABLE IF EXISTS `neighborhoodpackage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `neighborhoodpackage` (
  `fk_in_charge` uuid NOT NULL,
  `fk_collection_point` uuid NOT NULL,
  `date` datetime NOT NULL,
  `id_neighborhood_package` uuid NOT NULL,
  PRIMARY KEY (`id_neighborhood_package`),
  KEY `fk_collection_point_idx` (`fk_collection_point`),
  KEY `fk_user_neighborhood_package` (`fk_in_charge`),
  CONSTRAINT `fk_collection_point_neighborhood_package` FOREIGN KEY (`fk_collection_point`) REFERENCES `collectionpoint` (`id_collection_point`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_neighborhood_package` FOREIGN KEY (`fk_in_charge`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `neighborhoodpackage`
--

LOCK TABLES `neighborhoodpackage` WRITE;
/*!40000 ALTER TABLE `neighborhoodpackage` DISABLE KEYS */;
/*!40000 ALTER TABLE `neighborhoodpackage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `neighborhoodpackagehistory`
--

DROP TABLE IF EXISTS `neighborhoodpackagehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `neighborhoodpackagehistory` (
  `id_neighborhood_package_history` uuid NOT NULL,
  `fk_neighborhood_package` uuid NOT NULL,
  `fk_neighborhood_package_state` uuid NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `init` datetime NOT NULL,
  `finish` datetime DEFAULT NULL,
  PRIMARY KEY (`id_neighborhood_package_history`),
  KEY `fk_neighborhood_package_idx` (`fk_neighborhood_package`),
  KEY `fk_neigborhood_package_state_idx` (`fk_neighborhood_package_state`),
  CONSTRAINT `fk_neigborhood_package_state_neigh_pack_hist` FOREIGN KEY (`fk_neighborhood_package_state`) REFERENCES `neighborhoodpackagestate` (`id_neighborhood_package_state`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_neighborhood_package_neigh_pack_hist` FOREIGN KEY (`fk_neighborhood_package`) REFERENCES `neighborhoodpackage` (`id_neighborhood_package`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `neighborhoodpackagehistory`
--

LOCK TABLES `neighborhoodpackagehistory` WRITE;
/*!40000 ALTER TABLE `neighborhoodpackagehistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `neighborhoodpackagehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `neighborhoodpackagestate`
--

DROP TABLE IF EXISTS `neighborhoodpackagestate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `neighborhoodpackagestate` (
  `id_neighborhood_package_state` uuid NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_neighborhood_package_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `neighborhoodpackagestate`
--

LOCK TABLES `neighborhoodpackagestate` WRITE;
/*!40000 ALTER TABLE `neighborhoodpackagestate` DISABLE KEYS */;
/*!40000 ALTER TABLE `neighborhoodpackagestate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentmethod`
--

DROP TABLE IF EXISTS `paymentmethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paymentmethod` (
  `id_payment_method` uuid NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_payment_method`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentmethod`
--

LOCK TABLES `paymentmethod` WRITE;
/*!40000 ALTER TABLE `paymentmethod` DISABLE KEYS */;
/*!40000 ALTER TABLE `paymentmethod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id_product` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  `stock` double NOT NULL,
  `alert_stock` double DEFAULT NULL,
  `photo` varchar(45) DEFAULT NULL,
  `unit_measurement` varchar(100) NOT NULL,
  `fk_productor` uuid NOT NULL,
  `unity_price` double NOT NULL,
  PRIMARY KEY (`id_product`),
  UNIQUE KEY `id_productor_UNIQUE` (`id_product`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `product_user_FK` (`fk_productor`),
  CONSTRAINT `product_user_FK` FOREIGN KEY (`fk_productor`) REFERENCES `user` (`id_user`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producthistory`
--

DROP TABLE IF EXISTS `producthistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producthistory` (
  `id_product_history` uuid NOT NULL,
  `fk_product` uuid NOT NULL,
  `fk_product_state` uuid NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `init` datetime NOT NULL,
  `finish` datetime DEFAULT NULL,
  PRIMARY KEY (`id_product_history`),
  KEY `fk_product_idx` (`fk_product`),
  KEY `fk_product_state_idx` (`fk_product_state`),
  CONSTRAINT `fk_product_product_history` FOREIGN KEY (`fk_product`) REFERENCES `product` (`id_product`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_state_product_history` FOREIGN KEY (`fk_product_state`) REFERENCES `productstate` (`id_product_state`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producthistory`
--

LOCK TABLES `producthistory` WRITE;
/*!40000 ALTER TABLE `producthistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `producthistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productstate`
--

DROP TABLE IF EXISTS `productstate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productstate` (
  `id_product_state` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_product_state`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productstate`
--

LOCK TABLES `productstate` WRITE;
/*!40000 ALTER TABLE `productstate` DISABLE KEYS */;
/*!40000 ALTER TABLE `productstate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `id_purchase` uuid NOT NULL,
  `fk_user` uuid NOT NULL,
  `fk_type_purchase` uuid NOT NULL,
  `amount` double NOT NULL,
  `fk_neighborhood_package` uuid DEFAULT NULL,
  `fk_payment_method` uuid NOT NULL,
  PRIMARY KEY (`id_purchase`),
  KEY `fk_purchase_type_idx` (`fk_type_purchase`),
  KEY `fk_user_idx` (`fk_user`),
  KEY `fk_neighborhood_package_purchase_idx` (`fk_neighborhood_package`),
  KEY `fk_payment_method_purchase_idx` (`fk_payment_method`),
  CONSTRAINT `fk_neighborhood_package_purchase` FOREIGN KEY (`fk_neighborhood_package`) REFERENCES `neighborhoodpackage` (`id_neighborhood_package`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_payment_method_purchase` FOREIGN KEY (`fk_payment_method`) REFERENCES `paymentmethod` (`id_payment_method`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_purchase_type_purchase` FOREIGN KEY (`fk_type_purchase`) REFERENCES `purchasetype` (`id_purchase_type`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_purchase` FOREIGN KEY (`fk_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasedetail`
--

DROP TABLE IF EXISTS `purchasedetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasedetail` (
  `id_purchase_detail` uuid NOT NULL,
  `fk_product` uuid NOT NULL,
  `quantity` double NOT NULL,
  `fk_purchase` uuid NOT NULL,
  PRIMARY KEY (`id_purchase_detail`),
  KEY `fk_purchase_idx` (`fk_purchase`),
  KEY `fk_product_purchase_detail_idx` (`fk_product`),
  CONSTRAINT `fk_product_purchase_detail` FOREIGN KEY (`fk_product`) REFERENCES `product` (`id_product`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_purchase_purchase_detail` FOREIGN KEY (`fk_purchase`) REFERENCES `purchase` (`id_purchase`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasedetail`
--

LOCK TABLES `purchasedetail` WRITE;
/*!40000 ALTER TABLE `purchasedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasedetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasedetailstate`
--

DROP TABLE IF EXISTS `purchasedetailstate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasedetailstate` (
  `id_purchase_detail_state` uuid NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id_purchase_detail_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasedetailstate`
--

LOCK TABLES `purchasedetailstate` WRITE;
/*!40000 ALTER TABLE `purchasedetailstate` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasedetailstate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasedetailstatehistory`
--

DROP TABLE IF EXISTS `purchasedetailstatehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasedetailstatehistory` (
  `id_purchase_detail_state_history` uuid NOT NULL,
  `fk_purchase_detail` uuid NOT NULL,
  `fk_purchase_detail_state` uuid NOT NULL,
  `initial_date` datetime NOT NULL,
  `final_date` datetime DEFAULT NULL,
  `description` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id_purchase_detail_state_history`),
  KEY `purchasedetailstatehistory_purchasedetailstate_FK` (`fk_purchase_detail_state`),
  KEY `purchasedetailstatehistory_purchasedetail_FK` (`fk_purchase_detail`),
  CONSTRAINT `purchasedetailstatehistory_purchasedetail_FK` FOREIGN KEY (`fk_purchase_detail`) REFERENCES `purchasedetail` (`id_purchase_detail`) ON UPDATE CASCADE,
  CONSTRAINT `purchasedetailstatehistory_purchasedetailstate_FK` FOREIGN KEY (`fk_purchase_detail_state`) REFERENCES `purchasedetailstate` (`id_purchase_detail_state`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasedetailstatehistory`
--

LOCK TABLES `purchasedetailstatehistory` WRITE;
/*!40000 ALTER TABLE `purchasedetailstatehistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasedetailstatehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasestate`
--

DROP TABLE IF EXISTS `purchasestate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasestate` (
  `id_purchase_state` uuid NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id_purchase_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasestate`
--

LOCK TABLES `purchasestate` WRITE;
/*!40000 ALTER TABLE `purchasestate` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasestate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasestatehistory`
--

DROP TABLE IF EXISTS `purchasestatehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasestatehistory` (
  `id_purchase_state_history` uuid NOT NULL,
  `fk_purchase_state` uuid NOT NULL,
  `fk_purchase` uuid NOT NULL,
  `initial_date` datetime NOT NULL,
  `final_date` datetime DEFAULT NULL,
  `description` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id_purchase_state_history`),
  KEY `purchasestatehistory_purchase_FK` (`fk_purchase`),
  KEY `purchasestatehistory_purchasestate_FK` (`fk_purchase_state`),
  CONSTRAINT `purchasestatehistory_purchase_FK` FOREIGN KEY (`fk_purchase`) REFERENCES `purchase` (`id_purchase`) ON UPDATE CASCADE,
  CONSTRAINT `purchasestatehistory_purchasestate_FK` FOREIGN KEY (`fk_purchase_state`) REFERENCES `purchasestate` (`id_purchase_state`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasestatehistory`
--

LOCK TABLES `purchasestatehistory` WRITE;
/*!40000 ALTER TABLE `purchasestatehistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasestatehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchasetype`
--

DROP TABLE IF EXISTS `purchasetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasetype` (
  `id_purchase_type` uuid NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_purchase_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasetype`
--

LOCK TABLES `purchasetype` WRITE;
/*!40000 ALTER TABLE `purchasetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchasetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sale`
--

DROP TABLE IF EXISTS `sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale` (
  `id_sale` uuid NOT NULL,
  `amount` double NOT NULL,
  `fk_productor` uuid NOT NULL,
  `fk_deliver_guy` uuid NOT NULL,
  `fk_payment_method` uuid DEFAULT NULL,
  `bill` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_sale`),
  KEY `fk_user_idx` (`fk_productor`),
  KEY `fk_deliver_guy_idx` (`fk_deliver_guy`),
  KEY `fk_payment_method_sale_idx` (`fk_payment_method`),
  CONSTRAINT `fk_deliver_guy_sale` FOREIGN KEY (`fk_deliver_guy`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_payment_method_sale` FOREIGN KEY (`fk_payment_method`) REFERENCES `paymentmethod` (`id_payment_method`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_sale` FOREIGN KEY (`fk_productor`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sale`
--

LOCK TABLES `sale` WRITE;
/*!40000 ALTER TABLE `sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saledetail`
--

DROP TABLE IF EXISTS `saledetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saledetail` (
  `id_sale_detail` uuid NOT NULL,
  `fk_product` uuid NOT NULL,
  `quantity` double NOT NULL,
  `fk_sale` uuid NOT NULL,
  PRIMARY KEY (`id_sale_detail`),
  KEY `fk_sale_idx` (`fk_sale`),
  KEY `fk_product_idx` (`fk_product`),
  CONSTRAINT `fk_product_sale_detail` FOREIGN KEY (`fk_product`) REFERENCES `product` (`id_product`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sale_sale_detail` FOREIGN KEY (`fk_sale`) REFERENCES `sale` (`id_sale`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saledetail`
--

LOCK TABLES `saledetail` WRITE;
/*!40000 ALTER TABLE `saledetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `saledetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salehistory`
--

DROP TABLE IF EXISTS `salehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salehistory` (
  `id_sale_history` uuid NOT NULL,
  `fk_sale` uuid NOT NULL,
  `fk_sale_state` uuid NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `init` datetime NOT NULL,
  `finish` datetime DEFAULT NULL,
  PRIMARY KEY (`id_sale_history`),
  KEY `fk_sale_state_idx` (`fk_sale_state`),
  KEY `fk_sale_idx` (`fk_sale`),
  CONSTRAINT `fk_sale_sale_history` FOREIGN KEY (`fk_sale`) REFERENCES `sale` (`id_sale`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sale_state_sale_history` FOREIGN KEY (`fk_sale_state`) REFERENCES `salestate` (`id_sale_state`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salehistory`
--

LOCK TABLES `salehistory` WRITE;
/*!40000 ALTER TABLE `salehistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `salehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salestate`
--

DROP TABLE IF EXISTS `salestate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salestate` (
  `id_sale_state` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_sale_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salestate`
--

LOCK TABLES `salestate` WRITE;
/*!40000 ALTER TABLE `salestate` DISABLE KEYS */;
/*!40000 ALTER TABLE `salestate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockmovement`
--

DROP TABLE IF EXISTS `stockmovement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockmovement` (
  `id_stock_movement` uuid NOT NULL,
  `fk_product` uuid NOT NULL,
  `quantity` double NOT NULL,
  `type` enum('in','out','edition') NOT NULL,
  `date` date NOT NULL,
  `coment` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`id_stock_movement`),
  KEY `fk_product_idx` (`fk_product`),
  CONSTRAINT `fk_product_stock_movement` FOREIGN KEY (`fk_product`) REFERENCES `product` (`id_product`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockmovement`
--

LOCK TABLES `stockmovement` WRITE;
/*!40000 ALTER TABLE `stockmovement` DISABLE KEYS */;
/*!40000 ALTER TABLE `stockmovement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` uuid NOT NULL,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `created_at` datetime NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `document_type` varchar(45) NOT NULL,
  `document_number` varchar(45) NOT NULL,
  `fk_neighborhood` uuid NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `photo` varchar(45) DEFAULT NULL,
  `minimal_sale_kg` double NOT NULL,
  `minimal_sale_units` int(11) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
  KEY `fk_neighborhood_idx` (`fk_neighborhood`),
  CONSTRAINT `fk_neighborhood_user` FOREIGN KEY (`fk_neighborhood`) REFERENCES `neighborhood` (`id_neighborhood`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userhistory`
--

DROP TABLE IF EXISTS `userhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userhistory` (
  `id_user_history` uuid NOT NULL,
  `fk_user` uuid NOT NULL,
  `fk_user_state` uuid NOT NULL,
  `description` varchar(1500) DEFAULT NULL,
  `init` datetime NOT NULL,
  `finish` datetime DEFAULT NULL,
  PRIMARY KEY (`id_user_history`),
  KEY `fk_user_idx` (`fk_user`),
  KEY `fk_user_state_idx` (`fk_user_state`),
  CONSTRAINT `fk_user_state_user_history` FOREIGN KEY (`fk_user_state`) REFERENCES `userstate` (`id_user_state`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_user_history` FOREIGN KEY (`fk_user`) REFERENCES `user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userhistory`
--

LOCK TABLES `userhistory` WRITE;
/*!40000 ALTER TABLE `userhistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `userhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userstate`
--

DROP TABLE IF EXISTS `userstate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userstate` (
  `id_user_state` uuid NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_user_state`),
  UNIQUE KEY `id_user_state_UNIQUE` (`id_user_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userstate`
--

LOCK TABLES `userstate` WRITE;
/*!40000 ALTER TABLE `userstate` DISABLE KEYS */;
/*!40000 ALTER TABLE `userstate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'proyecto'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-18 16:18:36
