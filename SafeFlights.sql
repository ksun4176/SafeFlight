-- phpMyAdmin SQL Dump
-- version 4.5.0.2
-- http://www.phpmyadmin.net
--
-- Host: my-db-instance.ckej4fsuw7km.us-east-1.rds.amazonaws.com
-- Generation Time: Nov 30, 2017 at 04:47 AM
-- Server version: 5.6.34-log
-- PHP Version: 5.6.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `SafeFlights`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`sf`@`%` PROCEDURE `addCustomer` (IN `id` INT, IN `ccnum` CHAR(16), IN `email` VARCHAR(100), IN `creationDate` DATE)  BEGIN
	INSERT INTO Customer(Id,CreditCardNo,Email,CreationDate) 
	VALUES (id,ccnum,email,creationDate);
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `addEmployee` (`id` INT, `ssn` CHAR(9), `isManager` BOOLEAN, `startDate` DATE, `newRate` DECIMAL(10,2))  BEGIN
	INSERT INTO Employee 
	VALUES(id,ssn,isManager,startDate,newRate);
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `addPerson` (`fname` VARCHAR(50), `lname` VARCHAR(50), `address` VARCHAR(50), `city` VARCHAR(50), `state` VARCHAR(50), `zipcode` INT)  BEGIN
	INSERT INTO Person(FirstName,LastName,Address,City,State,ZipCode) 
	VALUES (fname,lname,address,city,state,zipcode);
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `bid` (`accountNo` INT, `airlineID` CHAR(2), `flightNo` INT, `class` VARCHAR(20), `date` DATE, `bid` DECIMAL(10,2))  BEGIN
	INSERT INTO Auctions 
	VALUES(accountNo,airlineID,flightNo,class,date,bid);
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `deleteCustomer` (`accoutNo` INT)  BEGIN
	DELETE FROM Customer WHERE AccountNo = accoutNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `deleteEmployee` (`id` INT)  BEGIN
	DELETE FROM Employee WHERE Id = id;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `deleteReservation` (`resNo` INT)  BEGIN
	DELETE FROM Includes WHERE ResNo = resNo;
	DELETE FROM Reservation WHERE ResNo = resNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editCustomer` (IN `id` INT, IN `firstName` VARCHAR(50), IN `lastName` VARCHAR(50), IN `address` VARCHAR(100), IN `city` VARCHAR(50), IN `state` VARCHAR(50), IN `zipcode` CHAR(5), IN `email` VARCHAR(100), IN `cc` CHAR(16))  BEGIN
UPDATE Person P SET P.FirstName = firstName, P.LastName = lastName, P.Address = address, P.City = city, P.State = state, P.ZipCode = zipcode WHERE P.Id = id;
UPDATE Customer C SET C.Email = email, C.CreditCardNo = cc WHERE C.Id = id;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editCustomerCC` (`accountNo` INT, `ccnum` CHAR(16))  BEGIN
	UPDATE Customer SET CreditCardNo = ccnum WHERE AccountNo = accoutNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editCustomerCreationDate` (`accountNo` INT, `creationDate` DATE)  BEGIN
	UPDATE Customer SET CreationDate = creationDate WHERE AccountNo = accountNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editCustomerEmail` (`accoutNo` INT, `email` VARCHAR(100))  BEGIN
	UPDATE Customer SET Email = email WHERE AccountNo = accoutNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editCustomerRating` (`accountNo` INT, `rating` INT)  BEGIN
	UPDATE Customer SET Rating = rating WHERE AccountNo = accoutNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editEmployeeManager` (`id` INT, `isManager` BOOLEAN)  BEGIN
	UPDATE Employee SET IsManager = isManager WHERE Id = id;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editEmployeeRate` (`id` INT, `newRate` DECIMAL(10,2))  BEGIN
	UPDATE Employee SET HourlyRate = newRate WHERE Id = id;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `editEmployeeStartDate` (`id` INT, `startDate` DATE)  BEGIN
	UPDATE Employee SET StartDate = startDate;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `findUser` (IN `Username` VARCHAR(50), IN `Password` VARCHAR(50))  BEGIN
	SELECT Id,Role FROM LoginInfo L WHERE L.Username = Username AND L.Password = Password;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getAllReservations` (`accountNo` INT)  BEGIN
	SELECT * FROM Reservation 
	WHERE AccountNo = accountNo ORDER BY ResDate DESC;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getBestEmployee` ()  BEGIN
	SELECT E.Id,SUM(R.TotalFare) FROM Employee E,Reservation R 
	WHERE E.Id = R.RepId GROUP BY E.Id ORDER BY SUM(R.TotalFare) DESC LIMIT 1;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getBidHistory` (`airlineID` CHAR(2), `flightNo` INT, `class` VARCHAR(20))  BEGIN
	SELECT Date, AccountNo, NYOP FROM Auctions 
	WHERE AirlineID = airlineID AND FlightNo = flightNo AND Class = class ORDER BY Date DESC;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getCurrentBid` (`accountNo` INT, `airlineID` CHAR(2), `flightNo` INT, `class` VARCHAR(20))  BEGIN
	SELECT NYOP FROM Auctions 
	WHERE AccountNo = accountNo AND AirlineID = airlineID AND FlightNo = flightNo AND Class = class AND Date = MAX(Date);
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getCurrentReservations` (IN `accountNo` INT)  BEGIN
	SELECT R.ResNo FROM Reservation R ,Includes I
	WHERE I.Date >= CURDATE() AND R.ResNo = I.ResNo AND R.AccountNo = accountNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getDelayedFlights` ()  BEGIN
	SELECT AirlineID,FlightNo,LegNo FROM Leg 
	WHERE OnTime = 0;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getDomesticFlights` ()  BEGIN
	SELECT F.AirlineID, F.FlightNo FROM Flight F, Leg L, Airport Arr, Airport Dep 
	WHERE F.AirlineID = L.AirlineID AND F.FlightNo = L.FlightNo AND L.ArrAirportID = Arr.Id AND L.DepAirportID = Dep.Id AND Arr.Country = Dep.Country;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getFlexTimeFlights` (IN `prefTime` DATETIME)  BEGIN
    SELECT L.AirlineID, L.FlightNo, L.LegNo FROM Leg L
    WHERE L.DepTime >= prefTime;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getFlights` ()  BEGIN
	SELECT * FROM Leg;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getFlightsAtAirport` (`airportId` CHAR(3))  BEGIN
	SELECT FlightNo FROM Leg 
	WHERE ArrAirportID = airportId OR DepAirportID = airportId;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getFlightSuggestions` (`accountNo` INT)  BEGIN
	SELECT L.AirlineID, L.FlightNo FROM Leg L 
	WHERE L.ArrAirportID IN (
		SELECT L2.ArrAirportID FROM Leg L2, Includes I , Reservation R 
		WHERE R.AccountNo = accountNo AND I.ResNo = R.ResNo AND L2.LegNo = I.LegNo AND L2.AirlineID = I.AirlineID AND L2.FlightNo = I.FlightNo) 
	AND L.DepTime > NOW();
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getInternationFlights` ()  BEGIN
	SELECT F.FlightNo FROM Flight F, Leg L, Airport Arr, Airport Dep 
	WHERE F.AirlineID = L.AirlineID AND F.FlightNo = L.FlightNo AND L.ArrAirportID = Arr.Id AND L.DepAirportID = Dep.Id AND Arr.Country <> Dep.Country;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getItinerary` (`resNo` INT)  BEGIN
	SELECT L.DepAirportID, L.DepTime, L.ArrAirportID, L.ArrTime FROM LEG L, Includes I 
	WHERE L.AirlineID = I.AirlineID AND L.FlightNo = I.FlightNo AND L.LegNo = I.LegNo AND I.ResNo = resNo ORDER BY L.ArrTime, L.DepTime ASC;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getMailingList` ()  BEGIN
	SELECT Email FROM Customer C;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getMonthlySalesReport` (`month` INT, `year` INT)  BEGIN
	SELECT * FROM Reservation
	WHERE YEAR(ResDate) = year AND MONTH(ResDate) = month;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getMostActiveFlights` ()  BEGIN
	SELECT AirlineId,FlightNo,COUNT(*) FROM Includes 
	GROUP BY AirlineId,FlightNo ORDER BY COUNT(*) DESC LIMIT 10;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getMultiCityFlights` ()  BEGIN
	SELECT L.AirlineID, L.FlightNo FROM Leg L
	GROUP BY L.AirlineID,L.FlightNo
	HAVING COUNT(DISTINCT L.ArrAirportID) >= 2;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getOneWayFlights` ()  BEGIN
	SELECT AirlineID, FlightNo FROM Fare 
	WHERE FareType='one-way';
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getOnTimeFlights` ()  BEGIN
	SELECT AirlineID,FlightNo,LegNo FROM Leg 
	WHERE OnTime = 1;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getReservationC` (`fname` VARCHAR(50), `lname` VARCHAR(50))  BEGIN
	SELECT * FROM Reservation R,Customer C,Person P 
	WHERE P.LastName = lname AND P.FirstName = fname AND C.Id = P.Id AND R.AccountNo =  C.AccountNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getReservationF` (`airlineId` CHAR(2), `flightNo` INT)  BEGIN
	SELECT * FROM Reservation R,Includes I 
	WHERE I.AirlineID = airlineId AND I.FlightNo = flightNo AND R.ResNo = I.ResNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getReservedCustomers` (IN `airlineId` CHAR(2), IN `flightNo` INT)  BEGIN
	SELECT C.Id FROM Customer C,Reservation R,Includes I 
	WHERE I.AirlineID = airlineId AND I.FlightNo = flightNo AND R.ResNo = I.ResNo AND C.AccountNo = R.AccountNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getRevenueC` (`customerId` INT)  BEGIN
	SELECT R.TotalFare FROM Reservation R,Customer C 
	WHERE C.Id = customerId AND R.AccountNo = C.AccountNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getRevenueDC` (`city` VARCHAR(50))  BEGIN
	SELECT R.TotalFare FROM Reservation R,Includes I,Leg L,Airport A 
	WHERE A.city = city AND L.ArrAirportID = A.Id AND I.LegNo = L.LegNo AND I.AirlineID = L.AirlineID AND I.FlightNo  =  L.Flightno AND R.ResNo = I.ResNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getRevenueF` (`airlineId` CHAR(2), `flightNo` INT)  BEGIN
	SELECT R.TotalFare FROM Reservation R,Includes I 
	WHERE I.AirlineID = airlineId AND I.FlightNo = flightNo AND R.ResNo = I.ResNo;
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `getRoundTripFlights` ()  BEGIN
	SELECT AirlineID, FlightNo FROM Fare 
	WHERE FareType='roundtrip';
END$$

CREATE DEFINER=`sf`@`%` PROCEDURE `recordReservation` (IN `accountNo` INT, IN `resNo` INT, IN `repId` INT, IN `airlineID` CHAR(2), IN `flightNo` INT, IN `legNo` INT, IN `flightFare` DECIMAL(10,2), IN `flightDate` DATE)  BEGIN
	IF (SELECT COUNT(*) FROM Reservation WHERE ResNo = resNo) = 0 THEN INSERT INTO Reservation VALUES (resNo,NOW(),0,0,repId,accountNo);END IF;
	INSERT INTO Includes VALUES(resNo,airlineID,flightNo,legNo,flightDate);
	UPDATE Reservation SET TotalFare = TotalFare + flightFare,BookingFee = TotalFare / 10 WHERE ResNo = resNo;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `AdvPurchaseDiscount`
--

CREATE TABLE `AdvPurchaseDiscount` (
  `AirlineID` char(2) NOT NULL DEFAULT '',
  `Days` int(11) NOT NULL DEFAULT '0',
  `Rate` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `AdvPurchaseDiscount`
--

INSERT INTO `AdvPurchaseDiscount` (`AirlineID`, `Days`, `Rate`) VALUES
('AA', 3, '5.00'),
('AA', 7, '6.00'),
('AA', 14, '7.00'),
('AA', 21, '8.00'),
('AF', 3, '5.00'),
('AF', 7, '6.00'),
('AF', 14, '7.00'),
('AF', 21, '8.00'),
('BA', 3, '5.00'),
('BA', 7, '6.00'),
('BA', 14, '7.00'),
('BA', 21, '8.00'),
('BR', 3, '5.00'),
('BR', 7, '6.00'),
('BR', 14, '7.00'),
('BR', 21, '8.00'),
('CZ', 3, '5.00'),
('CZ', 7, '6.00'),
('CZ', 14, '7.00'),
('CZ', 21, '8.00'),
('DL', 3, '5.00'),
('DL', 7, '6.00'),
('DL', 14, '7.00'),
('DL', 21, '8.00'),
('EI', 3, '5.00'),
('EI', 7, '6.00'),
('EI', 14, '7.00'),
('EI', 21, '8.00'),
('NK', 3, '5.00'),
('NK', 7, '6.00'),
('NK', 14, '7.00'),
('NK', 21, '8.00'),
('UA', 3, '5.00'),
('UA', 7, '6.00'),
('UA', 14, '7.00'),
('UA', 21, '8.00'),
('VX', 3, '5.00'),
('VX', 7, '6.00'),
('VX', 14, '7.00'),
('VX', 21, '8.00'),
('WN', 3, '5.00'),
('WN', 7, '6.00'),
('WN', 14, '7.00'),
('WN', 21, '8.00'),
('WS', 3, '5.00'),
('WS', 7, '6.00'),
('WS', 14, '7.00'),
('WS', 21, '8.00');

-- --------------------------------------------------------

--
-- Table structure for table `Airline`
--

CREATE TABLE `Airline` (
  `Id` char(2) NOT NULL DEFAULT '',
  `Name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Airline`
--

INSERT INTO `Airline` (`Id`, `Name`) VALUES
('AA', 'American Airlines'),
('AF', 'Air France'),
('BA', 'British Airways'),
('BR', 'EVA Air'),
('CZ', 'China Southwest Airlines'),
('DL', 'Delta Airlines'),
('EI', 'Air Lingus'),
('NK', 'Spirit Airlines'),
('UA', 'United Airlines'),
('VX', 'Virgin Airlines'),
('WN', 'Southwest Airlines'),
('WS', 'WestJet');

-- --------------------------------------------------------

--
-- Table structure for table `Airport`
--

CREATE TABLE `Airport` (
  `Id` char(3) NOT NULL DEFAULT '',
  `Name` varchar(100) NOT NULL,
  `City` varchar(50) NOT NULL,
  `Country` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Airport`
--

INSERT INTO `Airport` (`Id`, `Name`, `City`, `Country`) VALUES
('BOS', 'Boston Logan International Airport', 'Boston', 'United States'),
('CAN', 'Guangzhou Baiyun International Airport', 'Guangzhou', 'China'),
('CDG', 'Charles de Gaulle Aiport', 'Paris', 'France'),
('DEN', 'Denver International Airport', 'Denver', 'United States'),
('DTW', 'Detroit Metropolitan Wayne County Airport', 'Detroit', 'United States'),
('DUB', 'Dublin Airport', 'Dublin', 'Ireland'),
('FLL', 'Fort Lauderdale Hollywood International Airport', 'Fort Lauderdale', 'United States'),
('JFK', 'John F. Kennedy International Airport', 'New York City', 'United States'),
('LAS', 'McCarran International Airport', 'Las Vegas', 'United States'),
('LAX', 'Los Angeles International Airport', 'Los Angeles', 'United States'),
('LGA', 'LaGuardia Airport', 'New York City', 'United States'),
('LHR', 'Heathrow Airport', 'London', 'United Kingdom'),
('MDW', 'Midway International Airport', 'Chicago', 'United States'),
('ORD', 'O''Hare International Airport', 'Chicago', 'United States'),
('SFO', 'San Francisco International Airport', 'San Francisco', 'United States'),
('SJD', 'Los Cabos International Airport', 'San Jose del Cabo', 'Mexico'),
('SLC', 'Salt Lake City International Airport', 'Salt Lake City', 'United States'),
('TPE', 'Taiwan Taoyuan International Airport', 'Taoyuan City', 'Taiwan'),
('WUH', 'Wuhan Tianhe International Airport', 'Wuhan', 'China'),
('YVR', 'Vancouver International Airport', 'Richmond', 'Canada');

-- --------------------------------------------------------

--
-- Table structure for table `Auctions`
--

CREATE TABLE `Auctions` (
  `AccountNo` int(11) NOT NULL DEFAULT '0',
  `AirlineID` char(2) NOT NULL DEFAULT '',
  `FlightNo` int(11) NOT NULL DEFAULT '0',
  `Class` varchar(20) NOT NULL DEFAULT '',
  `Date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `NYOP` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Customer`
--

CREATE TABLE `Customer` (
  `Id` int(11) NOT NULL,
  `AccountNo` int(11) NOT NULL,
  `CreditCardNo` char(16) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `CreationDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Rating` int(11) DEFAULT '10'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Customer`
--

INSERT INTO `Customer` (`Id`, `AccountNo`, `CreditCardNo`, `Email`, `CreationDate`, `Rating`) VALUES
(5, 1, '1234567890123456', 'AliceBanana@gmail.com', '2017-11-29 00:00:00', 10),
(6, 2, '2345678901234567', 'CarlDoom@gmail.com', '2017-11-29 00:00:00', 10),
(7, 3, '3456789012345678', 'EugeneFap@gmail.com', '2017-11-29 00:00:00', 10),
(8, 4, '4567890123456789', 'GraceHugme@gmail.com', '2017-11-29 00:00:00', 10);

-- --------------------------------------------------------

--
-- Table structure for table `CustomerPreferences`
--

CREATE TABLE `CustomerPreferences` (
  `AccountNo` int(11) NOT NULL DEFAULT '0',
  `Preference` varchar(100) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Employee`
--

CREATE TABLE `Employee` (
  `Id` int(11) NOT NULL,
  `SSN` char(9) NOT NULL DEFAULT '',
  `IsManager` tinyint(1) NOT NULL DEFAULT '0',
  `StartDate` date NOT NULL,
  `HourlyRate` decimal(10,2) NOT NULL DEFAULT '11.00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Employee`
--

INSERT INTO `Employee` (`Id`, `SSN`, `IsManager`, `StartDate`, `HourlyRate`) VALUES
(1, '123456789', 1, '2017-11-29', '9999.99'),
(2, '234567890', 0, '2017-11-29', '10.00'),
(3, '345678901', 0, '2017-11-29', '10.00'),
(4, '456789012', 0, '2017-11-29', '10.00');

-- --------------------------------------------------------

--
-- Table structure for table `Fare`
--

CREATE TABLE `Fare` (
  `AirlineID` char(2) NOT NULL DEFAULT '',
  `FlightNo` int(11) NOT NULL DEFAULT '0',
  `FareType` varchar(20) NOT NULL DEFAULT '',
  `Class` varchar(20) NOT NULL DEFAULT '',
  `Fare` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Fare`
--

INSERT INTO `Fare` (`AirlineID`, `FlightNo`, `FareType`, `Class`, `Fare`) VALUES
('AA', 1058, 'one-way', 'economy', '200.00'),
('AA', 1058, 'one-way', 'first', '500.00'),
('AA', 1058, 'roundtrip', 'economy', '300.00'),
('AA', 1058, 'rountrip', 'first', '750.00'),
('AA', 1105, 'one-way', 'economy', '100.00'),
('AA', 1105, 'one-way', 'first', '250.00'),
('AA', 1105, 'roundtrip', 'economy', '150.00'),
('AA', 1105, 'rountrip', 'first', '375.00'),
('AA', 1569, 'one-way', 'economy', '200.00'),
('AA', 1569, 'one-way', 'first', '500.00'),
('AA', 1569, 'roundtrip', 'economy', '300.00'),
('AA', 1569, 'rountrip', 'first', '750.00'),
('AA', 3029, 'one-way', 'economy', '150.00'),
('AA', 3029, 'one-way', 'first', '375.00'),
('AA', 3029, 'roundtrip', 'economy', '225.00'),
('AA', 3029, 'rountrip', 'first', '550.00'),
('AF', 11, 'one-way', 'economy', '1000.00'),
('AF', 11, 'one-way', 'first', '2500.00'),
('AF', 11, 'roundtrip', 'economy', '1500.00'),
('AF', 11, 'rountrip', 'first', '3750.00'),
('BA', 172, 'one-way', 'economy', '750.00'),
('BA', 172, 'one-way', 'first', '1875.00'),
('BA', 172, 'roundtrip', 'economy', '1125.00'),
('BA', 172, 'rountrip', 'first', '3000.00'),
('BR', 55, 'one-way', 'economy', '1500.00'),
('BR', 55, 'one-way', 'first', '4250.00'),
('BR', 55, 'roundtrip', 'economy', '2250.00'),
('BR', 55, 'rountrip', 'first', '6375.00'),
('CZ', 472, 'one-way', 'economy', '1500.00'),
('CZ', 472, 'one-way', 'first', '4250.00'),
('CZ', 472, 'roundtrip', 'economy', '2250.00'),
('CZ', 472, 'rountrip', 'first', '6375.00'),
('DL', 426, 'one-way', 'economy', '300.00'),
('DL', 426, 'one-way', 'first', '750.00'),
('DL', 426, 'roundtrip', 'economy', '450.00'),
('DL', 426, 'rountrip', 'first', '1125.00'),
('DL', 427, 'one-way', 'economy', '300.00'),
('DL', 427, 'one-way', 'first', '750.00'),
('DL', 427, 'roundtrip', 'economy', '450.00'),
('DL', 427, 'rountrip', 'first', '1125.00'),
('DL', 1441, 'one-way', 'economy', '200.00'),
('DL', 1441, 'one-way', 'first', '500.00'),
('DL', 1441, 'roundtrip', 'economy', '300.00'),
('DL', 1441, 'rountrip', 'first', '750.00'),
('EI', 122, 'one-way', 'economy', '600.00'),
('EI', 122, 'one-way', 'first', '1500.00'),
('EI', 122, 'roundtrip', 'economy', '900.00'),
('EI', 122, 'rountrip', 'first', '2250.00'),
('NK', 970, 'one-way', 'economy', '200.00'),
('NK', 970, 'one-way', 'first', '500.00'),
('NK', 970, 'roundtrip', 'economy', '300.00'),
('NK', 970, 'rountrip', 'first', '750.00'),
('UA', 257, 'one-way', 'economy', '200.00'),
('UA', 257, 'one-way', 'first', '500.00'),
('UA', 257, 'roundtrip', 'economy', '300.00'),
('UA', 257, 'rountrip', 'first', '750.00'),
('UA', 938, 'one-way', 'economy', '400.00'),
('UA', 938, 'one-way', 'first', '1000.00'),
('UA', 938, 'roundtrip', 'economy', '600.00'),
('UA', 938, 'rountrip', 'first', '1500.00'),
('WN', 206, 'one-way', 'economy', '300.00'),
('WN', 206, 'one-way', 'first', '750.00'),
('WN', 206, 'roundtrip', 'economy', '450.00'),
('WN', 206, 'rountrip', 'first', '1125.00'),
('WN', 720, 'one-way', 'economy', '400.00'),
('WN', 720, 'one-way', 'first', '1000.00'),
('WN', 720, 'roundtrip', 'economy', '600.00'),
('WN', 720, 'rountrip', 'first', '1500.00'),
('WN', 6438, 'one-way', 'economy', '100.00'),
('WN', 6438, 'one-way', 'first', '250.00'),
('WN', 6438, 'roundtrip', 'economy', '150.00'),
('WN', 6438, 'rountrip', 'first', '375.00'),
('WN', 6512, 'one-way', 'economy', '200.00'),
('WN', 6512, 'one-way', 'first', '500.00'),
('WN', 6512, 'roundtrip', 'economy', '300.00'),
('WN', 6512, 'rountrip', 'first', '750.00'),
('WS', 1699, 'one-way', 'economy', '300.00'),
('WS', 1699, 'one-way', 'first', '750.00'),
('WS', 1699, 'roundtrip', 'economy', '450.00'),
('WS', 1699, 'rountrip', 'first', '1125.00');

-- --------------------------------------------------------

--
-- Table structure for table `Flight`
--

CREATE TABLE `Flight` (
  `AirlineID` char(2) NOT NULL DEFAULT '',
  `FlightNo` int(11) NOT NULL DEFAULT '0',
  `NoOfSeats` int(11) NOT NULL,
  `DaysOperating` char(7) NOT NULL,
  `MinLengthOfStay` int(11) NOT NULL DEFAULT '0',
  `MaxLengthOfStay` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Flight`
--

INSERT INTO `Flight` (`AirlineID`, `FlightNo`, `NoOfSeats`, `DaysOperating`, `MinLengthOfStay`, `MaxLengthOfStay`) VALUES
('AA', 1058, 140, '1010101', 3, 5),
('AA', 1105, 161, '1110101', 3, 7),
('AA', 1569, 120, '1101010', 13, 20),
('AA', 3029, 176, '1010101', 11, 14),
('AF', 11, 101, '1100110', 3, 6),
('BA', 172, 126, '0101011', 5, 9),
('BR', 55, 200, '1001111', 14, 28),
('CZ', 472, 134, '1111111', 5, 8),
('DL', 426, 143, '1011111', 4, 8),
('DL', 427, 139, '1011011', 7, 14),
('DL', 1441, 149, '1101011', 7, 14),
('EI', 122, 101, '1111011', 1, 2),
('NK', 970, 163, '1010111', 3, 7),
('UA', 257, 131, '1101100', 7, 10),
('UA', 938, 170, '1010101', 3, 5),
('WN', 206, 140, '1111000', 1, 3),
('WN', 720, 118, '1111111', 6, 8),
('WN', 6438, 139, '1000111', 3, 7),
('WN', 6512, 140, '0001111', 1, 4),
('WS', 1699, 149, '1110111', 4, 11);

-- --------------------------------------------------------

--
-- Table structure for table `Includes`
--

CREATE TABLE `Includes` (
  `ResNo` int(11) NOT NULL DEFAULT '0',
  `AirlineID` char(2) NOT NULL DEFAULT '',
  `FlightNo` int(11) NOT NULL DEFAULT '0',
  `LegNo` int(11) NOT NULL DEFAULT '0',
  `Date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Leg`
--

CREATE TABLE `Leg` (
  `AirlineID` char(2) NOT NULL DEFAULT '',
  `FlightNo` int(11) NOT NULL DEFAULT '0',
  `LegNo` int(11) NOT NULL,
  `DepAirportID` char(3) NOT NULL,
  `ArrAirportID` char(3) NOT NULL,
  `ArrTime` datetime NOT NULL,
  `DepTime` datetime NOT NULL,
  `OnTime` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Leg`
--

INSERT INTO `Leg` (`AirlineID`, `FlightNo`, `LegNo`, `DepAirportID`, `ArrAirportID`, `ArrTime`, `DepTime`, `OnTime`) VALUES
('AA', 1058, 1, 'ORD', 'SLC', '2017-12-18 07:45:00', '2017-12-18 10:30:00', 1),
('AA', 1058, 2, 'SLC', 'LAX', '2017-12-18 11:00:00', '2017-12-18 12:10:00', 1),
('AA', 1105, 1, 'JFK', 'BOS', '2017-12-17 09:30:00', '2017-12-17 10:30:00', 1),
('AA', 1569, 1, 'ORD', 'BOS', '2017-12-16 21:50:00', '2017-12-17 00:00:00', 1),
('AA', 3029, 1, 'ORD', 'DTW', '2017-12-15 15:00:00', '2017-12-15 17:15:00', 1),
('AF', 11, 1, 'JFK', 'CDG', '2017-12-23 21:46:00', '2017-12-24 10:11:00', 1),
('BA', 172, 1, 'JFK', 'LHR', '2017-12-20 20:30:00', '2017-12-21 08:30:00', 1),
('BR', 55, 1, 'ORD', 'TPE', '2017-12-19 00:20:00', '2017-12-19 05:50:00', 1),
('CZ', 472, 1, 'LAX', 'SFO', '2017-11-23 08:00:00', '2017-11-23 09:45:00', 1),
('CZ', 472, 2, 'SFO', 'WUH', '2017-11-23 12:55:00', '2017-11-24 18:35:00', 1),
('CZ', 472, 3, 'WUH', 'CAN', '2017-11-24 20:20:00', '2017-11-24 22:25:00', 1),
('DL', 426, 1, 'JFK', 'SFO', '2017-12-28 07:00:00', '2017-12-28 10:00:00', 1),
('DL', 427, 1, 'JFK', 'LAX', '2017-12-19 08:30:00', '2017-12-19 11:00:00', 1),
('DL', 1441, 1, 'DEN', 'DTW', '2017-12-16 06:00:00', '2017-12-16 11:00:00', 1),
('DL', 1441, 2, 'DTW', 'JFK', '2017-12-16 12:15:00', '2017-12-16 14:15:00', 1),
('EI', 122, 1, 'LAX', 'ORD', '2017-11-19 07:00:00', '2017-11-19 13:00:00', 1),
('EI', 122, 2, 'ORD', 'DUB', '2017-11-19 16:00:00', '2017-11-20 05:15:00', 1),
('EI', 122, 3, 'DUB', 'LHR', '2017-11-20 06:30:00', '2017-11-20 08:05:00', 1),
('NK', 970, 1, 'DEN', 'FLL', '2017-12-26 00:00:00', '2017-12-27 05:40:00', 1),
('UA', 257, 1, 'DEN', 'SFO', '2017-12-17 05:30:00', '2017-12-17 07:15:00', 1),
('UA', 938, 1, 'ORD', 'LHR', '2017-12-16 21:30:00', '2017-12-17 11:10:00', 1),
('WN', 206, 1, 'LAX', 'DTW', '2017-11-14 09:25:00', '2017-11-14 17:00:00', 1),
('WN', 206, 2, 'DTW', 'MDW', '2017-11-14 17:50:00', '2017-11-14 18:25:00', 1),
('WN', 720, 1, 'DEN', 'LAX', '2017-12-11 06:40:00', '2017-12-11 08:40:00', 1),
('WN', 720, 2, 'LAX', 'SJD', '2017-12-11 11:40:00', '2017-12-11 15:15:00', 1),
('WN', 6438, 1, 'LAX', 'LAS', '2017-11-14 19:40:00', '2017-11-14 20:40:00', 1),
('WN', 6512, 1, 'DEN', 'LAX', '2017-12-29 13:00:00', '2017-12-29 14:30:00', 1),
('WS', 1699, 1, 'LAX', 'YVR', '2017-12-13 12:00:00', '2017-12-13 15:00:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `LoginInfo`
--

CREATE TABLE `LoginInfo` (
  `Id` int(11) NOT NULL DEFAULT '0',
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `Role` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `LoginInfo`
--

INSERT INTO `LoginInfo` (`Id`, `Username`, `Password`, `Role`) VALUES
(1, 'kevsun', 'kevsun', 'manager'),
(2, 'timsitorus', 'timsitorus', 'employee'),
(3, 'wawong', 'wawong', 'employee'),
(4, 'rogli', 'rogli', 'employee'),
(5, 'alibanana', 'alibanana', 'customer'),
(6, 'cardoom', 'cardoom', 'customer'),
(7, 'eufap', 'eufap', 'customer'),
(8, 'grahugme', 'grahugme', 'customer');

-- --------------------------------------------------------

--
-- Table structure for table `Passenger`
--

CREATE TABLE `Passenger` (
  `Id` int(11) NOT NULL DEFAULT '0',
  `AccountNo` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Person`
--

CREATE TABLE `Person` (
  `Id` int(11) NOT NULL,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `City` varchar(50) NOT NULL,
  `State` varchar(50) NOT NULL,
  `ZipCode` char(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Person`
--

INSERT INTO `Person` (`Id`, `FirstName`, `LastName`, `Address`, `City`, `State`, `ZipCode`) VALUES
(1, 'Kevin', 'Sun', '450 Circle Road', 'Stony Brook', 'New York', '11790'),
(2, 'Timotius', 'Sitorus', '69 Love Place', 'Brooklyn', 'New York', '10769'),
(3, 'Warren', 'Wong', '500 Circle Road', 'Stony Brook', 'New York', '11790'),
(4, 'Roger', 'Li', '400 Circle Road', 'Stony Brook', 'New York', '11790'),
(5, 'Alice', 'Banana', '123 Square Road', 'Boston', 'Massachusetts', '01841'),
(6, 'Carl', 'Doom', '234 Turtle Street', 'San Francisco', 'California', '94016'),
(7, 'Eugene', 'Fap', '345 Triangle Avenue', 'Chicago', 'Illinois', '60007'),
(8, 'Grace', 'Hugme', '456 Star Place', 'Las Vegas', 'Nevada', '88901');

-- --------------------------------------------------------

--
-- Table structure for table `Reservation`
--

CREATE TABLE `Reservation` (
  `ResNo` int(11) NOT NULL,
  `ResDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `BookingFee` decimal(10,2) NOT NULL,
  `TotalFare` decimal(10,2) NOT NULL,
  `RepId` int(11) NOT NULL,
  `AccountNo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ReservationPassenger`
--

CREATE TABLE `ReservationPassenger` (
  `ResNo` int(11) NOT NULL DEFAULT '0',
  `Id` int(11) NOT NULL DEFAULT '0',
  `AccountNo` int(11) NOT NULL DEFAULT '0',
  `SeatNo` char(5) NOT NULL,
  `Class` varchar(20) NOT NULL,
  `Meal` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `AdvPurchaseDiscount`
--
ALTER TABLE `AdvPurchaseDiscount`
  ADD PRIMARY KEY (`AirlineID`,`Days`);

--
-- Indexes for table `Airline`
--
ALTER TABLE `Airline`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `Airport`
--
ALTER TABLE `Airport`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `Auctions`
--
ALTER TABLE `Auctions`
  ADD PRIMARY KEY (`AccountNo`,`AirlineID`,`FlightNo`,`Class`,`Date`),
  ADD KEY `AuctionsB` (`AirlineID`,`FlightNo`);

--
-- Indexes for table `Customer`
--
ALTER TABLE `Customer`
  ADD PRIMARY KEY (`AccountNo`),
  ADD KEY `CustomerA` (`Id`);

--
-- Indexes for table `CustomerPreferences`
--
ALTER TABLE `CustomerPreferences`
  ADD PRIMARY KEY (`AccountNo`,`Preference`);

--
-- Indexes for table `Employee`
--
ALTER TABLE `Employee`
  ADD PRIMARY KEY (`SSN`),
  ADD UNIQUE KEY `Id` (`Id`);

--
-- Indexes for table `Fare`
--
ALTER TABLE `Fare`
  ADD PRIMARY KEY (`AirlineID`,`FlightNo`,`FareType`,`Class`);

--
-- Indexes for table `Flight`
--
ALTER TABLE `Flight`
  ADD PRIMARY KEY (`AirlineID`,`FlightNo`);

--
-- Indexes for table `Includes`
--
ALTER TABLE `Includes`
  ADD PRIMARY KEY (`ResNo`,`AirlineID`,`FlightNo`,`LegNo`),
  ADD KEY `IncludesB` (`AirlineID`,`FlightNo`,`LegNo`);

--
-- Indexes for table `Leg`
--
ALTER TABLE `Leg`
  ADD PRIMARY KEY (`AirlineID`,`FlightNo`,`LegNo`),
  ADD UNIQUE KEY `AirlineID` (`AirlineID`,`FlightNo`,`DepAirportID`),
  ADD KEY `LegB` (`DepAirportID`),
  ADD KEY `LegC` (`ArrAirportID`);

--
-- Indexes for table `LoginInfo`
--
ALTER TABLE `LoginInfo`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Indexes for table `Passenger`
--
ALTER TABLE `Passenger`
  ADD PRIMARY KEY (`Id`,`AccountNo`),
  ADD KEY `PassengerB` (`AccountNo`);

--
-- Indexes for table `Person`
--
ALTER TABLE `Person`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `Reservation`
--
ALTER TABLE `Reservation`
  ADD PRIMARY KEY (`ResNo`),
  ADD KEY `ReservationA` (`RepId`),
  ADD KEY `ReservationB` (`AccountNo`);

--
-- Indexes for table `ReservationPassenger`
--
ALTER TABLE `ReservationPassenger`
  ADD PRIMARY KEY (`ResNo`,`Id`,`AccountNo`),
  ADD KEY `ResPassengerB` (`Id`,`AccountNo`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Customer`
--
ALTER TABLE `Customer`
  MODIFY `AccountNo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `Person`
--
ALTER TABLE `Person`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `Reservation`
--
ALTER TABLE `Reservation`
  MODIFY `ResNo` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `AdvPurchaseDiscount`
--
ALTER TABLE `AdvPurchaseDiscount`
  ADD CONSTRAINT `DiscountA` FOREIGN KEY (`AirlineID`) REFERENCES `Airline` (`Id`);

--
-- Constraints for table `Auctions`
--
ALTER TABLE `Auctions`
  ADD CONSTRAINT `AuctionsA` FOREIGN KEY (`AccountNo`) REFERENCES `Customer` (`AccountNo`),
  ADD CONSTRAINT `AuctionsB` FOREIGN KEY (`AirlineID`,`FlightNo`) REFERENCES `Flight` (`AirlineID`, `FlightNo`);

--
-- Constraints for table `Customer`
--
ALTER TABLE `Customer`
  ADD CONSTRAINT `CustomerA` FOREIGN KEY (`Id`) REFERENCES `Person` (`Id`);

--
-- Constraints for table `CustomerPreferences`
--
ALTER TABLE `CustomerPreferences`
  ADD CONSTRAINT `PreferencesA` FOREIGN KEY (`AccountNo`) REFERENCES `Customer` (`AccountNo`);

--
-- Constraints for table `Employee`
--
ALTER TABLE `Employee`
  ADD CONSTRAINT `EmployeeA` FOREIGN KEY (`Id`) REFERENCES `Person` (`Id`);

--
-- Constraints for table `Fare`
--
ALTER TABLE `Fare`
  ADD CONSTRAINT `FareA` FOREIGN KEY (`AirlineID`,`FlightNo`) REFERENCES `Flight` (`AirlineID`, `FlightNo`);

--
-- Constraints for table `Flight`
--
ALTER TABLE `Flight`
  ADD CONSTRAINT `FlightA` FOREIGN KEY (`AirlineID`) REFERENCES `Airline` (`Id`);

--
-- Constraints for table `Includes`
--
ALTER TABLE `Includes`
  ADD CONSTRAINT `IncludesA` FOREIGN KEY (`ResNo`) REFERENCES `Reservation` (`ResNo`),
  ADD CONSTRAINT `IncludesB` FOREIGN KEY (`AirlineID`,`FlightNo`,`LegNo`) REFERENCES `Leg` (`AirlineID`, `FlightNo`, `LegNo`);

--
-- Constraints for table `Leg`
--
ALTER TABLE `Leg`
  ADD CONSTRAINT `LegA` FOREIGN KEY (`AirlineID`,`FlightNo`) REFERENCES `Flight` (`AirlineID`, `FlightNo`),
  ADD CONSTRAINT `LegB` FOREIGN KEY (`DepAirportID`) REFERENCES `Airport` (`Id`),
  ADD CONSTRAINT `LegC` FOREIGN KEY (`ArrAirportID`) REFERENCES `Airport` (`Id`);

--
-- Constraints for table `LoginInfo`
--
ALTER TABLE `LoginInfo`
  ADD CONSTRAINT `LoginA` FOREIGN KEY (`Id`) REFERENCES `Person` (`Id`);

--
-- Constraints for table `Passenger`
--
ALTER TABLE `Passenger`
  ADD CONSTRAINT `PassengerA` FOREIGN KEY (`Id`) REFERENCES `Person` (`Id`),
  ADD CONSTRAINT `PassengerB` FOREIGN KEY (`AccountNo`) REFERENCES `Customer` (`AccountNo`);

--
-- Constraints for table `Reservation`
--
ALTER TABLE `Reservation`
  ADD CONSTRAINT `ReservationA` FOREIGN KEY (`RepId`) REFERENCES `Employee` (`Id`),
  ADD CONSTRAINT `ReservationB` FOREIGN KEY (`AccountNo`) REFERENCES `Customer` (`AccountNo`);

--
-- Constraints for table `ReservationPassenger`
--
ALTER TABLE `ReservationPassenger`
  ADD CONSTRAINT `ResPassengerA` FOREIGN KEY (`ResNo`) REFERENCES `Reservation` (`ResNo`),
  ADD CONSTRAINT `ResPassengerB` FOREIGN KEY (`Id`,`AccountNo`) REFERENCES `Passenger` (`Id`, `AccountNo`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
