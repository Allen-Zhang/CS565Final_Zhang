CREATE DATABASE  IF NOT EXISTS `cs565_final_zhang` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `cs565_final_zhang`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: cs565_final_zhang
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
  `StockPrice` double NOT NULL COMMENT 'The stock price at the end of that Date',
  PRIMARY KEY (`StockHistoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhang_stock_history`
--

LOCK TABLES `zhang_stock_history` WRITE;
/*!40000 ALTER TABLE `zhang_stock_history` DISABLE KEYS */;
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
  `StockPrice` double NOT NULL COMMENT 'Current stock price',
  PRIMARY KEY (`StockQuotesId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhang_stock_quotes`
--

LOCK TABLES `zhang_stock_quotes` WRITE;
/*!40000 ALTER TABLE `zhang_stock_quotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhang_stock_quotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhang_transactions`
--

DROP TABLE IF EXISTS `zhang_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhang_transactions` (
  `TransactionId` int(11) NOT NULL AUTO_INCREMENT,
  `CustomerId` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `TransactionDate` int(20) NOT NULL,
  `TransactionType` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `StockSymbol` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Quantity` double NOT NULL,
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

-- Dump completed on 2014-04-04 17:08:05
