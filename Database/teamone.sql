-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Aug 24, 2023 at 04:42 PM
-- Server version: 5.7.36
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `teamone`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(8) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`username`, `password`) VALUES
('abc', '123');

-- --------------------------------------------------------

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
CREATE TABLE IF NOT EXISTS `assignments` (
  `batch` varchar(20) NOT NULL,
  `mis` varchar(20) NOT NULL,
  `subject` varchar(20) NOT NULL,
  `assignmentnumber` varchar(2) NOT NULL,
  `marks` varchar(2) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `batchdetails`
--

DROP TABLE IF EXISTS `batchdetails`;
CREATE TABLE IF NOT EXISTS `batchdetails` (
  `batch` varchar(30) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `batchdetails`
--

INSERT INTO `batchdetails` (`batch`) VALUES
('Select Batch'),
('2022-23'),
(' ');

-- --------------------------------------------------------

--
-- Table structure for table `ojt`
--

DROP TABLE IF EXISTS `ojt`;
CREATE TABLE IF NOT EXISTS `ojt` (
  `batch` varchar(20) NOT NULL,
  `mis` varchar(20) NOT NULL,
  `institutename` varchar(50) NOT NULL,
  `startdate` varchar(20) NOT NULL,
  `enddate` varchar(20) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `address` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `batch` varchar(10) NOT NULL,
  `mis` varchar(29) NOT NULL,
  `misl2` varchar(2) NOT NULL,
  `name` varchar(50) NOT NULL,
  `namewithin` varchar(100) NOT NULL,
  `dob` date NOT NULL,
  `age` varchar(2) NOT NULL,
  `nic` varchar(12) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `hqual` varchar(20) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `email` varchar(50) NOT NULL,
  `address` varchar(120) NOT NULL,
  `nameofguardian` varchar(120) NOT NULL,
  `guardianphone` varchar(10) NOT NULL,
  `image` longblob NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
