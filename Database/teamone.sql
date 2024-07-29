-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Oct 07, 2023 at 05:48 PM
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
-- Table structure for table `assignmentnumber`
--

DROP TABLE IF EXISTS `assignmentnumber`;
CREATE TABLE IF NOT EXISTS `assignmentnumber` (
  `assignmentno` varchar(2) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `assignmentnumber`
--

INSERT INTO `assignmentnumber` (`assignmentno`) VALUES
('1'),
('2');

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
  `marks` varchar(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `assignments`
--

INSERT INTO `assignments` (`batch`, `mis`, `subject`, `assignmentnumber`, `marks`) VALUES
('2023-24', '01', 'Software Programming', '1', '100'),
('2023-24', '04', 'Software Programming', '1', '50'),
('2023-24', '23', 'Software Programming', '1', '80'),
('2023-24', '23', 'Software Programming', '2', '50'),
('2023-24', '04', 'Software Programming', '2', '20'),
('2023-24', '01', 'Software Programming', '2', '90'),
('2023-24', '23', 'Database Systems', '1', '60'),
('2023-24', '04', 'Database Systems', '1', '70'),
('2023-24', '01', 'Database Systems', '1', '80');

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
('2023-24'),
(' ');

-- --------------------------------------------------------

--
-- Table structure for table `examresults`
--

DROP TABLE IF EXISTS `examresults`;
CREATE TABLE IF NOT EXISTS `examresults` (
  `batch` varchar(20) NOT NULL,
  `misl2` varchar(2) NOT NULL,
  `semester` varchar(2) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `result` varchar(7) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `examresults`
--

INSERT INTO `examresults` (`batch`, `misl2`, `semester`, `subject`, `result`) VALUES
('2023-24', '04', '1', 'Software Programming', 'C'),
('2023-24', '01', '1', 'Software Programming', 'C'),
('2023-24', '23', '1', 'Software Programming', 'C'),
('2023-24', '01', '1', 'Database Systems', 'C'),
('2023-24', '23', '1', 'Database Systems', 'C'),
('2023-24', '23', '1', 'Software Programming', 'Repeat');

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
-- Table structure for table `person_images`
--

DROP TABLE IF EXISTS `person_images`;
CREATE TABLE IF NOT EXISTS `person_images` (
  `person_id` varchar(30) NOT NULL,
  `image_data` longblob,
  PRIMARY KEY (`person_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `semesternumber`
--

DROP TABLE IF EXISTS `semesternumber`;
CREATE TABLE IF NOT EXISTS `semesternumber` (
  `semesterno` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `semesternumber`
--

INSERT INTO `semesternumber` (`semesterno`) VALUES
('Select Semester'),
('1');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `batch` varchar(10) NOT NULL,
  `mis` varchar(29) NOT NULL,
  `misl2` varchar(2) NOT NULL,
  `name` varchar(150) NOT NULL,
  `namewithin` varchar(100) NOT NULL,
  `dob` date NOT NULL,
  `age` varchar(2) NOT NULL,
  `nic` varchar(12) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `hqual` varchar(50) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `email` varchar(50) NOT NULL,
  `address` varchar(120) NOT NULL,
  `nameofguardian` varchar(120) NOT NULL,
  `guardianphone` varchar(10) NOT NULL,
  `image` longblob NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `subjectdetails`
--

DROP TABLE IF EXISTS `subjectdetails`;
CREATE TABLE IF NOT EXISTS `subjectdetails` (
  `semesterno` varchar(1) NOT NULL,
  `subjectname` varchar(100) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subjectdetails`
--

INSERT INTO `subjectdetails` (`semesterno`, `subjectname`) VALUES
('1', 'Software Programming'),
('1', 'Database Systems');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
