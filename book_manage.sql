-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: book_manage
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_name` varchar(100) NOT NULL,
  `author` varchar(50) DEFAULT NULL,
  `stock` int DEFAULT '0' COMMENT '剩余可借数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (3,'三体','刘慈欣',2,'2026-09-21 09:19:24'),(11,'11','1',6,'2026-09-24 09:24:21'),(12,'11','11',3,'2026-09-24 09:25:08'),(29,'我','千万',1,'2026-09-26 15:40:44');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_record`
--

DROP TABLE IF EXISTS `borrow_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `borrow_time` datetime DEFAULT NULL,
  `return_time` datetime DEFAULT NULL,
  `is_return` tinyint DEFAULT '0' COMMENT '0未还，1已还',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_record`
--

LOCK TABLES `borrow_record` WRITE;
/*!40000 ALTER TABLE `borrow_record` DISABLE KEYS */;
INSERT INTO `borrow_record` VALUES (1,1,1,'2026-09-20 20:37:33','2026-09-20 22:41:15',1),(2,1,1,'2026-09-20 20:40:50','2026-09-20 22:41:15',1),(3,1,1,'2026-09-20 21:24:17','2026-09-20 22:41:15',1),(4,1,1,'2026-09-20 21:25:11','2026-09-20 22:43:11',1),(5,1,1,'2026-09-20 22:47:25','2026-09-20 22:48:39',1),(6,1,1,'2026-09-20 22:50:18','2026-09-20 22:51:05',1),(7,1,1,'2026-09-21 09:56:17','2026-09-21 17:36:51',1),(8,1,1,'2026-09-22 09:38:55','2026-09-22 09:39:19',1),(9,7,1,'2026-09-22 17:23:15','2026-09-27 11:27:29',1),(10,3,1,'2026-09-22 17:23:55','2026-09-27 11:27:26',1),(11,12,1,'2026-09-26 15:28:53','2026-09-27 11:27:24',1),(12,11,1,'2026-09-26 15:29:47','2026-09-27 11:27:21',1),(13,29,1,'2026-09-26 15:40:46','2026-09-27 11:27:19',1),(14,31,1,'2026-09-26 19:22:11','2026-09-27 10:02:53',1),(15,31,1,'2026-09-26 20:00:01','2026-09-27 10:02:50',1),(16,12,1,'2026-09-26 20:00:14','2026-09-27 10:01:58',1),(17,11,1,'2026-09-26 20:00:24','2026-09-27 10:01:54',1),(18,11,1,'2026-09-26 20:03:50','2026-09-27 10:01:43',1),(19,11,1,'2026-09-26 20:03:53','2026-09-27 10:01:19',1),(20,12,1,'2026-09-26 20:04:03','2026-09-27 10:01:17',1),(21,11,1,'2026-09-26 20:04:12','2026-09-27 10:01:14',1),(22,7,1,'2026-09-26 20:06:27','2026-09-27 10:01:12',1),(23,3,1,'2026-09-26 20:06:32','2026-09-27 10:01:08',1),(24,31,1,'2026-09-27 10:02:58','2026-09-27 10:03:05',1),(25,30,1,'2026-09-27 10:03:21','2026-09-27 10:03:27',1),(26,31,1,'2026-09-27 11:16:11','2026-09-27 11:21:22',1),(27,7,1,'2026-09-27 11:28:30','2026-09-27 11:28:38',1),(28,3,1,'2026-09-27 12:39:11','2026-09-27 12:39:21',1),(29,29,1,'2026-09-27 13:18:17','2026-09-27 13:18:24',1),(30,12,1,'2026-09-27 13:27:35','2026-09-27 13:27:42',1),(31,29,1,'2026-09-27 13:28:25','2026-09-27 13:28:42',1),(32,12,1,'2026-09-27 13:28:32','2026-09-27 13:28:40',1),(33,29,1,'2026-09-27 15:00:32','2026-09-27 15:00:38',1),(34,29,1,'2026-09-27 15:37:20','2026-09-27 15:37:27',1),(35,29,2,'2026-09-27 15:44:08','2026-09-27 15:44:16',1);
/*!40000 ALTER TABLE `borrow_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `role` varchar(20) NOT NULL DEFAULT 'student' COMMENT '角色：admin管理员 / student学生',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (2,'stu01','123456','张三','13900139000','student'),(3,'test01','SRPUC2QUi0s4eSutbjQ8xmHJD8bTv4JUBdRUb5LTf2HjXvlp1AzSmQ==','测试用户','12345676567','user'),(4,'admin','r+Ul4zFcYM+w3Kz10n0K71wZRCEBMlu/gd9inwsq5qoRGPWTKtq8qA==','管理员','12343245435','user');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) DEFAULT NULL COMMENT 'admin管理员 / reader读者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$DKnuAWhjdarFM5EOU3WsB.7.ESvZl8ehTKUnSicewoilQog5ZkHqm','admin','2026-09-20 09:50:18'),(2,'user','$2a$10$aXJvF/R85NWMnQpvIWn78.YR27z5zk5Arnh8b2eEpbIaX.BqaB0u6','user','2026-09-27 14:22:05');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-28 13:20:23
