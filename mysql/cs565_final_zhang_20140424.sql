CREATE DATABASE  IF NOT EXISTS `cs565` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `cs565`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: cs565
-- ------------------------------------------------------
-- Server version	5.6.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `zhang_accounts`
--

DROP TABLE IF EXISTS `zhang_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhang_accounts` (
  `CustomerId` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `CustomerName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `OpeningDate` int(20) NOT NULL,
  `OpeningBalance` double NOT NULL,
  PRIMARY KEY (`CustomerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhang_accounts`
--

LOCK TABLES `zhang_accounts` WRITE;
/*!40000 ALTER TABLE `zhang_accounts` DISABLE KEYS */;
INSERT INTO `zhang_accounts` VALUES ('20140401AZ','Allen Zhang',20140401,80000),('20140401JG','Jeff Green',20140401,40707.2),('20140403TL','Tracy Lin',20140403,20000),('20140405JP','Jack Porter',20140405,5000),('20140407DH','David Harris',20140407,100000),('20140407PJ','Paul Jones',20140407,45000),('20140412ST','Steven Taylor',20140412,24000),('20140415MW','Mark Wilson',20140415,35500),('20140416PC','Peter Cook',20140416,1000),('20140421YC','Ye Chen',20140421,7500),('20140423JL','Jason Lee',20140423,68500),('20140425RT','Robin Tracy',20140425,81500);
/*!40000 ALTER TABLE `zhang_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhang_stock_history`
--

DROP TABLE IF EXISTS `zhang_stock_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhang_stock_history` (
  `StockHistoryId` int(11) NOT NULL AUTO_INCREMENT,
  `StockSymbol` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `SDate` int(20) NOT NULL,
  `StockPrice` double NOT NULL,
  PRIMARY KEY (`StockHistoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhang_stock_history`
--

LOCK TABLES `zhang_stock_history` WRITE;
/*!40000 ALTER TABLE `zhang_stock_history` DISABLE KEYS */;
INSERT INTO `zhang_stock_history` VALUES (1,'FB',20140401,58.52),(2,'GOOG',20140401,549.85),(3,'IBM',20140401,186.34),(4,'MSFT',20140401,40.02),(5,'ORCL',20140401,40.1),(6,'TWTR',20140401,43.58),(7,'FB',20140402,59.12),(8,'GOOG',20140402,549.78),(9,'IBM',20140402,187.14),(10,'MSFT',20140402,40.01),(11,'ORCL',20140402,39.98),(12,'TWTR',20140402,45.64),(13,'FB',20140403,59.83),(14,'GOOG',20140403,548.91),(15,'IBM',20140403,187.57),(16,'MSFT',20140403,40.11),(17,'ORCL',20140403,40.01),(18,'TWTR',20140403,45.18),(19,'FB',20140404,58.64),(20,'GOOG',20140404,550.81),(21,'IBM',20140404,188.59),(22,'MSFT',20140404,40.05),(23,'ORCL',20140404,40.04),(24,'TWTR',20140404,45.58),(25,'FB',20140405,59.22),(26,'GOOG',20140405,546.71),(27,'IBM',20140405,186.29),(28,'MSFT',20140405,40.08),(29,'ORCL',20140405,39.83),(30,'TWTR',20140405,44.96),(31,'FB',20140406,59.52),(32,'GOOG',20140406,548.81),(33,'IBM',20140406,187.29),(34,'MSFT',20140406,40.02),(35,'ORCL',20140406,40.03),(36,'TWTR',20140406,44.68),(37,'FB',20140407,59.73),(38,'GOOG',20140407,548.91),(39,'IBM',20140407,187.65),(40,'MSFT',20140407,40.01),(41,'ORCL',20140407,40.03),(42,'TWTR',20140407,45.23),(43,'FB',20140408,57.72),(44,'GOOG',20140408,548.85),(45,'IBM',20140408,187.46),(46,'MSFT',20140408,40.07),(47,'ORCL',20140408,40.03),(48,'TWTR',20140408,46.48),(49,'FB',20140409,59.68),(50,'GOOG',20140409,548.82),(51,'IBM',20140409,187.77),(52,'MSFT',20140409,40.04),(53,'ORCL',20140409,40.03),(54,'TWTR',20140409,45.19),(55,'FB',20140410,58.86),(56,'GOOG',20140410,548.79),(57,'IBM',20140410,188.11),(58,'MSFT',20140410,40.05),(59,'ORCL',20140410,40.03),(60,'TWTR',20140410,45.43),(61,'FB',20140411,58.86),(62,'GOOG',20140411,548.76),(63,'IBM',20140411,188.57),(64,'MSFT',20140411,40.07),(65,'ORCL',20140411,40.03),(66,'TWTR',20140411,44.68),(67,'FB',20140412,59.81),(68,'GOOG',20140412,548.71),(69,'IBM',20140412,188.89),(70,'MSFT',20140412,40.09),(71,'ORCL',20140412,40.03),(72,'TWTR',20140412,45.77),(73,'FB',20140413,58.74),(74,'GOOG',20140413,548.77),(75,'IBM',20140413,189.21),(76,'MSFT',20140413,40.07),(77,'ORCL',20140413,40.03),(78,'TWTR',20140413,45.91),(79,'FB',20140414,59.56),(80,'GOOG',20140414,548.82),(81,'IBM',20140414,188.56),(82,'MSFT',20140414,40.06),(83,'ORCL',20140414,40.03),(84,'TWTR',20140414,45.67),(85,'FB',20140415,59.86),(86,'GOOG',20140415,548.85),(87,'IBM',20140415,188.49),(88,'MSFT',20140415,40.09),(89,'ORCL',20140415,40.03),(90,'TWTR',20140415,46.23),(91,'FB',20140416,59.72),(92,'GOOG',20140416,548.87),(93,'IBM',20140416,187.87),(94,'MSFT',20140416,40.12),(95,'ORCL',20140416,40.03),(96,'TWTR',20140416,44.34),(97,'FB',20140417,60.01),(98,'GOOG',20140417,548.91),(99,'IBM',20140417,187.65),(100,'MSFT',20140417,40.11),(101,'ORCL',20140417,40.03),(102,'TWTR',20140417,44.56),(103,'FB',20140418,59.72),(104,'GOOG',20140418,548.93),(105,'IBM',20140418,186.79),(106,'MSFT',20140418,40.09),(107,'ORCL',20140418,40.03),(108,'TWTR',20140418,44.89),(109,'FB',20140419,60.02),(110,'GOOG',20140419,548.96),(111,'IBM',20140419,187.12),(112,'MSFT',20140419,40.08),(113,'ORCL',20140419,40.03),(114,'TWTR',20140419,44.68),(115,'FB',20140420,60.05),(116,'GOOG',20140420,548.99),(117,'IBM',20140420,187.67),(118,'MSFT',20140420,40.07),(119,'ORCL',20140420,40.03),(120,'TWTR',20140420,44.53),(121,'FB',20140421,60.07),(122,'GOOG',20140421,548.95),(123,'IBM',20140421,187.89),(124,'MSFT',20140421,40.06),(125,'ORCL',20140421,40.03),(126,'TWTR',20140421,43.57),(127,'FB',20140422,60.12),(128,'GOOG',20140422,548.93),(129,'IBM',20140422,188.59),(130,'MSFT',20140422,40.05),(131,'ORCL',20140422,40.03),(132,'TWTR',20140422,45.78),(133,'FB',20140423,60.02),(134,'GOOG',20140423,548.83),(135,'IBM',20140423,189.45),(136,'MSFT',20140423,40.04),(137,'ORCL',20140423,40.03),(138,'TWTR',20140423,45.56),(139,'FB',20140424,59.92),(140,'GOOG',20140424,548.76),(141,'IBM',20140424,189.21),(142,'MSFT',20140424,40.01),(143,'ORCL',20140424,40.03),(144,'TWTR',20140424,44.68),(145,'FB',20140425,59.62),(146,'GOOG',20140425,548.41),(147,'IBM',20140425,188.43),(148,'MSFT',20140425,40.02),(149,'ORCL',20140425,40.03),(150,'TWTR',20140425,44.83),(151,'FB',20140426,59.86),(152,'GOOG',20140426,547.91),(153,'IBM',20140426,190.15),(154,'MSFT',20140426,39.98),(155,'ORCL',20140426,39.93),(156,'TWTR',20140426,45.15),(157,'FB',20140427,60.02),(158,'GOOG',20140427,548.51),(159,'IBM',20140427,189.37),(160,'MSFT',20140427,40.03),(161,'ORCL',20140427,40.02),(162,'TWTR',20140427,45.35),(163,'FB',20140428,58.82),(164,'GOOG',20140428,548.94),(165,'IBM',20140428,188.14),(166,'MSFT',20140428,40.04),(167,'ORCL',20140428,40.01),(168,'TWTR',20140428,45.78),(169,'FB',20140429,59.11),(170,'GOOG',20140429,540.88),(171,'IBM',20140429,187.64),(172,'MSFT',20140429,40.06),(173,'ORCL',20140429,39.97),(174,'TWTR',20140429,45.62);
/*!40000 ALTER TABLE `zhang_stock_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhang_stock_quotes`
--

DROP TABLE IF EXISTS `zhang_stock_quotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhang_stock_quotes` (
  `StockQuotesId` int(11) NOT NULL AUTO_INCREMENT,
  `StockSymbol` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `StockPrice` double NOT NULL,
  PRIMARY KEY (`StockQuotesId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhang_stock_quotes`
--

LOCK TABLES `zhang_stock_quotes` WRITE;
/*!40000 ALTER TABLE `zhang_stock_quotes` DISABLE KEYS */;
INSERT INTO `zhang_stock_quotes` VALUES (1,'FB',59.11),(2,'GOOG',540.88),(3,'IBM',187.64),(4,'MSFT',40.06),(5,'ORCL',39.97),(6,'TWTR',45.62);
/*!40000 ALTER TABLE `zhang_stock_quotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhang_transactions`
--

DROP TABLE IF EXISTS `zhang_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhang_transactions` (
  `TransactionId` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `CustomerId` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `TransactionDate` int(20) NOT NULL,
  `TransactionType` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `StockSymbol` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quantity` double DEFAULT NULL,
  `Price` double NOT NULL,
  PRIMARY KEY (`TransactionId`),
  KEY `zhang_transactions_fk1_idx` (`CustomerId`),
  CONSTRAINT `zhang_transactions_fk1` FOREIGN KEY (`CustomerId`) REFERENCES `zhang_accounts` (`CustomerId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhang_transactions`
--

LOCK TABLES `zhang_transactions` WRITE;
/*!40000 ALTER TABLE `zhang_transactions` DISABLE KEYS */;
INSERT INTO `zhang_transactions` VALUES ('2014040200513720140401JG','20140401JG',20140402,'Buy','FB',150,59.12),('2014040200514820140401JG','20140401JG',20140402,'Buy','MSFT',100,40.01),('2014040200520520140401JG','20140401JG',20140402,'Buy','TWTR',120,44.78),('2014040700530920140401JG','20140401JG',20140407,'Sell','TWTR',50,44.68),('2014040700533720140401JG','20140401JG',20140407,'Buy','IBM',150,187.29),('2014040700535820140401JG','20140401JG',20140407,'Deposit',NULL,NULL,10000),('2014040800544720140401JG','20140401JG',20140408,'Sell','MSFT',50,40.07),('2014040800550620140401JG','20140401JG',20140408,'Buy','TWTR',30,44.68),('2014041100561420140401JG','20140401JG',20140411,'Sell','FB',100,58.86),('2014041100563020140401JG','20140401JG',20140411,'Deposit',NULL,NULL,5000),('2014041100565120140401JG','20140401JG',20140411,'Buy','IBM',100,187.29),('2014041600572620140401JG','20140401JG',20140416,'Sell','FB',50,59.72),('2014041600575720140401JG','20140401JG',20140416,'Buy','ORCL',140,40.03),('2014042100583020140401JG','20140401JG',20140421,'Buy','FB',100,60.07),('2014042100594420140401JG','20140401JG',20140421,'Buy','MSFT',100,40.06),('2014042100595220140401JG','20140401JG',20140421,'Deposit',NULL,NULL,5000),('2014042501003520140401JG','20140401JG',20140425,'Sell','IBM',200,187.29),('2014042501005320140401JG','20140401JG',20140425,'Sell','ORCL',70,40.03),('2014042801012420140401JG','20140401JG',20140428,'Sell','TWTR',60,45.78),('2014042801013720140401JG','20140401JG',20140428,'Sell','MSFT',80,40.04),('2014042801015620140401JG','20140401JG',20140428,'Buy','FB',60,58.82),('2014042801024220140401JG','20140401JG',20140428,'Sell','ORCL',50,40.01),('2014042801030920140401JG','20140401JG',20140428,'Buy','GOOG',100,549.61),('2014042801032420140401JG','20140401JG',20140428,'Deposit',NULL,NULL,40000);
/*!40000 ALTER TABLE `zhang_transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-24  0:18:39
