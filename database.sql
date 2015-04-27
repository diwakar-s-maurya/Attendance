-- phpMyAdmin SQL Dump
-- version 4.2.13.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 27, 2015 at 03:04 AM
-- Server version: 5.6.17
-- PHP Version: 5.6.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `attendance`
--
DROP DATABASE `attendance`;
CREATE DATABASE IF NOT EXISTS `attendance` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `attendance`;

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance` (
  `rollno` varchar(50) NOT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `subject` varchar(50) NOT NULL,
  `type` int(11) NOT NULL,
  `value` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Truncate table before insert `attendance`
--

TRUNCATE TABLE `attendance`;
--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`rollno`, `datetime`, `subject`, `type`, `value`) VALUES
('2k12co01', '2015-04-23 15:19:37', 'CD', 1, 2),
('2k12co02', '2015-04-23 15:19:37', 'CD', 0, 2),
('2k12co01', '2015-04-23 15:38:43', 'DS', 1, 1),
('2k12co02', '2015-04-23 15:38:43', 'DS', 1, 1),
('2k12co01', '2015-04-23 15:40:24', 'DS', 1, 1),
('2k12co02', '2015-04-23 15:40:24', 'DS', 1, 1),
('2k12co01', '2015-04-23 15:40:29', 'DS', 1, 1),
('2k12co02', '2015-04-23 15:40:29', 'DS', 1, 1),
('2k12co01', '2015-04-23 15:40:39', 'DS', 1, 1),
('2k12co02', '2015-04-23 15:40:39', 'DS', 1, 1),
('2k12co01', '2015-04-23 15:59:47', 'CD', 0, 1),
('2k12co02', '2015-04-23 15:59:47', 'CD', 1, 1),
('2k12co01', '2015-04-23 16:22:16', 'CD', 1, 1),
('2k12co02', '2015-04-23 16:22:16', 'CD', 1, 1),
('2k12co01', '2015-04-23 16:32:21', 'CD', 1, 1),
('2k12co02', '2015-04-23 16:32:21', 'CD', 0, 1),
('2k12co01', '2015-04-23 16:50:36', 'DS', 1, 1),
('2k12co02', '2015-04-23 16:50:38', 'DS', 1, 1),
('2k12co01', '2015-04-23 18:47:15', 'CD', 1, 1),
('2k12co02', '2015-04-23 18:47:15', 'CD', 1, 1),
('2k12co01', '2015-04-23 18:54:01', 'CD', 0, 1),
('2k12co02', '2015-04-23 18:54:01', 'CD', 1, 1),
('2k12co01', '2015-04-23 18:54:45', 'CD', 0, 1),
('2k12co02', '2015-04-23 18:54:46', 'CD', 1, 1),
('2k12ece01', '2015-04-23 19:35:48', 'DSD', 1, 2),
('2k12ece02', '2015-04-23 19:35:49', 'DSD', 1, 2),
('2k12co01', '2015-04-23 20:10:32', 'CD', 1, 2),
('2k12co02', '2015-04-23 20:10:32', 'CD', 1, 2),
('2k12ece01', '2015-04-23 20:15:04', 'DSD', 1, 1),
('2k12ece02', '2015-04-23 20:15:04', 'DSD', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `class_student`
--

DROP TABLE IF EXISTS `class_student`;
CREATE TABLE IF NOT EXISTS `class_student` (
  `class` varchar(50) NOT NULL,
  `student` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='students belonging to a class';

--
-- Truncate table before insert `class_student`
--

TRUNCATE TABLE `class_student`;
--
-- Dumping data for table `class_student`
--

INSERT INTO `class_student` (`class`, `student`) VALUES
('2k12co', '2k12co01'),
('2k12co', '2k12co02'),
('2k12ece', '2k12ece01'),
('2k12ece', '2k12ece02');

-- --------------------------------------------------------

--
-- Table structure for table `class_subject_teacher`
--

DROP TABLE IF EXISTS `class_subject_teacher`;
CREATE TABLE IF NOT EXISTS `class_subject_teacher` (
  `class` varchar(50) NOT NULL,
  `subject` varchar(50) NOT NULL,
  `teacher` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='a teacher teaches which subject to which class';

--
-- Truncate table before insert `class_subject_teacher`
--

TRUNCATE TABLE `class_subject_teacher`;
--
-- Dumping data for table `class_subject_teacher`
--

INSERT INTO `class_subject_teacher` (`class`, `subject`, `teacher`) VALUES
('2k12co', 'CD', 'diwakar'),
('2k12ece', 'DSD', 'diwakar'),
('2k12co', 'DS', 'diwakar');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
CREATE TABLE IF NOT EXISTS `login` (
  `username` varchar(50) NOT NULL,
  `password_hash` text NOT NULL,
  `password_salt` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Truncate table before insert `login`
--

TRUNCATE TABLE `login`;
--
-- Dumping data for table `login`
--

INSERT INTO `login` (`username`, `password_hash`, `password_salt`) VALUES
('diwakar', '377b33b15906a10120f961669e55ea31a4de4d6d3b427dd32306e47725903770b610bf09651d087c7837a18acc6ec131130bce02f0e4ae0cf5b0a68fd977ffc0', 'b0819b814917ba4a80b2a1fa3b4a3237');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `login`
--
ALTER TABLE `login`
 ADD PRIMARY KEY (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
