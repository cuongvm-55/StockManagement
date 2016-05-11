-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 11, 2016 at 05:26 PM
-- Server version: 5.6.24
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `stockmanagement`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `debug_msg`(enabled INTEGER, msg VARCHAR(255))
BEGIN
  IF enabled THEN BEGIN
    select concat("** ", msg) INTO @DEBUG;
  END; END IF;
END$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `convertEncodingValueToOrigin`(inputStr VARCHAR(5000) ) RETURNS varchar(5000) CHARSET utf8
BEGIN
	DECLARE originChars VARCHAR(55)   DEFAULT "aaaaaaaaaaaaaadeeeeeeeeeiiiioooooooooouuuuuuuuuyyyy"; # Origin chars
	DECLARE encodingChars VARCHAR(55) DEFAULT "àáạãẫâấậầăắặằẵđéèẹẽêệếềễĩíìịôốồộỗơớợờỡùụúũưựừứữýỳỵỹ"; # Encoding chars, must has the same size and idx than originChars
	
	# Convert all the encoding char in inputStr
	DECLARE idx INTEGER DEFAULT 1;
	WHILE ( idx <= LENGTH(encodingChars) ) DO
		SET inputStr = REPLACE(inputStr, SUBSTRING(encodingChars, idx, 1), SUBSTRING(originChars, idx, 1));
		SET idx = idx + 1;
	END WHILE;
	
	RETURN inputStr;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `isFirstLettersMatched`(inputStr VARCHAR(5000), criteria VARCHAR(5000) ) RETURNS tinyint(1)
BEGIN
	DECLARE debugMode BOOLEAN;

	DECLARE idx INTEGER DEFAULT 1;            		#<! index to iterate the string
	DECLARE wordBoundInputIdx INTEGER DEFAULT 1;   	#<! index of the last letter of a word in input string
	DECLARE currentInputWord VARCHAR(5000);        	#<! input word
	
	DECLARE criteriaIdx INTEGER DEFAULT 1;			#<! index to iterate the string
	DECLARE wordBoundIdxCriteria INTEGER DEFAULT 1; #<! index of the last letter of first word in criteria string
	DECLARE currentCriteriaWord VARCHAR(5000);      #<! criteria word
	
	DECLARE inputIdx INTEGER DEFAULT 1;
	DECLARE bOk BOOLEAN DEFAULT FALSE;
	
	# trim the inputStr and the criteria
	SET inputStr = TRIM(inputStr);
	SET criteria = TRIM(criteria);
	
	# return TRUE if the criteria's empty
	IF	(STRCMP(criteria, "") = 0) THEN
		RETURN TRUE;
	END IF;
	
	# return FALSE if inputStr is emty here
	IF	( STRCMP(inputStr, "") = 0 || LENGTH(inputStr) < LENGTH(criteria) ) THEN
		RETURN FALSE;
	END IF;

	# make the input and the criteria lower case
	SET inputStr = LOWER(inputStr);
	SET criteria = LOWER(criteria);
	
	# manipulate the encoding
	SET inputStr = convertEncodingValueToOrigin(inputStr);
	SET criteria = convertEncodingValueToOrigin(criteria);
	
	# debug mode active ?
	SET debugMode = TRUE;
	
	WHILE ( idx <= LENGTH(inputStr) ) DO
		# find the space, if no space, retrieve the index of last letter in input string
		SET wordBoundInputIdx = IF((LOCATE(" ", inputStr, idx) != 0), (LOCATE(" ", inputStr, idx) - 1), LENGTH(inputStr) );
		SET currentInputWord = SUBSTRING(inputStr, idx, wordBoundInputIdx - idx + 1);
		
		# now check if the current word start with the first word in criteria
		SET inputIdx = idx;
		# update the idx here because the wordBoundInputIdx will be change
		SET idx = wordBoundInputIdx + 2; # +2 because we have 1 delimiter
		SET criteriaIdx = 1;
		SET bOk = TRUE;
		
		SET wordBoundIdxCriteria = IF((LOCATE(" ", criteria, 1) != 0), (LOCATE(" ", criteria, 1) - 1), LENGTH(criteria) );
		SET currentCriteriaWord = SUBSTRING(criteria, 1, wordBoundIdxCriteria);
	checkCriteria: WHILE ( !(inputIdx > LENGTH(inputStr) || criteriaIdx > LENGTH(criteria)) ) DO
			IF ( STRCMP(SUBSTRING(currentInputWord, 1, LENGTH(currentCriteriaWord)), currentCriteriaWord) != 0 ) THEN
				SET bOk = FALSE;
				LEAVE checkCriteria;
			END IF;
			
			SET inputIdx = wordBoundInputIdx + 2;
			SET criteriaIdx = wordBoundIdxCriteria + 2;

			SET wordBoundIdxCriteria = IF((LOCATE(" ", criteria, criteriaIdx) != 0), (LOCATE(" ", criteria, criteriaIdx) - 1), LENGTH(criteria) );
			SET currentCriteriaWord = SUBSTRING(criteria, criteriaIdx, wordBoundIdxCriteria - criteriaIdx + 1);
			
			SET wordBoundInputIdx = IF((LOCATE(" ", inputStr, inputIdx) != 0), (LOCATE(" ", inputStr, inputIdx) - 1), LENGTH(inputStr) );
			SET currentInputWord = SUBSTRING(inputStr, inputIdx, wordBoundInputIdx - inputIdx + 1);
		END WHILE;
		# check if all the criterias matched
		IF ( bOk AND criteriaIdx > LENGTH(criteria) ) THEN
			RETURN TRUE;
		END IF;
	END WHILE;	

	RETURN FALSE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `area`
--

CREATE TABLE IF NOT EXISTS `area` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên khu vực',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `area`
--

INSERT INTO `area` (`id`, `name`, `description`) VALUES
(1, 'Mĩ Đình', 'Khu vực Mĩ Đình - Từ Liêm - Hà Nội'),
(3, 'Cầu Giấy', 'Khu vực quận Cầu Giấy - Hà Nội');

-- --------------------------------------------------------

--
-- Table structure for table `coupon`
--

CREATE TABLE IF NOT EXISTS `coupon` (
  `id` int(11) NOT NULL,
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã phiếu hóa đơn',
  `idCouponType` int(11) NOT NULL COMMENT 'FK: Loại phiếu: Nhập mua, nhập hàng trả lại, xuất trả nhà cung cấp',
  `idCustomer` int(11) DEFAULT NULL COMMENT 'FK: id của khách hàng',
  `buyer` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Người giao hàng/người nhận hàng',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải: Mặc định là lấy tên của loại phiếu',
  `date` datetime NOT NULL COMMENT 'Ngày giờ tạo',
  `note` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `coupon`
--

INSERT INTO `coupon` (`id`, `code`, `idCouponType`, `idCustomer`, `buyer`, `content`, `date`, `note`) VALUES
(1, 'PH127451', 1, 33, '', 'Nh?p Mua Hàng', '2016-04-18 14:00:00', ''),
(2, 'PH236997', 1, 33, '', 'Nh?p Mua Hàng', '2016-04-18 14:07:13', ''),
(3, 'PH324945', 1, 33, '', 'Nh?p Mua Hàng', '2016-04-18 14:15:52', ''),
(4, 'PH435358', 1, 33, '', 'Nh?p Mua Hàng', '2016-04-18 15:54:33', ''),
(5, 'PH526493', 1, 33, '', 'Nh?p Mua Hàng', '2016-04-18 17:18:21', '');

-- --------------------------------------------------------

--
-- Table structure for table `coupondetail`
--

CREATE TABLE IF NOT EXISTS `coupondetail` (
  `id` int(11) NOT NULL,
  `idMaterial` int(11) NOT NULL COMMENT 'FK: id của vật tư',
  `idCoupon` int(11) NOT NULL COMMENT 'FK: id của phiếu',
  `quantity` int(11) NOT NULL,
  `price` decimal(19,4) NOT NULL COMMENT 'Giá chuẩn: mặc định lấy từ vật tư, có thể sửa được',
  `saleOff` float DEFAULT '0' COMMENT 'Chiết khấu, đơn vị phần trăm'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `coupondetail`
--

INSERT INTO `coupondetail` (`id`, `idMaterial`, `idCoupon`, `quantity`, `price`, `saleOff`) VALUES
(1, 50, 3, 2, '100000.0000', 0),
(2, 50, 4, 3, '100000.0000', 0),
(3, 50, 5, 2, '100000.0000', 0.02);

-- --------------------------------------------------------

--
-- Table structure for table `coupontype`
--

CREATE TABLE IF NOT EXISTS `coupontype` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'tên loại phiếu: Nhập mua, nhập hàng trả lại, xuất trả nhà cung cấp',
  `description` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `coupontype`
--

INSERT INTO `coupontype` (`id`, `name`, `description`) VALUES
(1, 'PH_NHAPMUA', 'Phiếu Mua'),
(2, 'PH_NHAPHANGTRALAI', 'Phiếu Nhập Hàng Trả Lại'),
(3, 'PH_XUATTRANHACUNGCAP', 'Phiếu Xuất Trả Nhà Cung Cấp');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id` int(11) NOT NULL COMMENT 'id của khách hàng',
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã khách hàng',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên khách hàng',
  `address` text COLLATE utf8_unicode_ci COMMENT 'Địa chỉ',
  `phoneNumber` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Số điện thoại',
  `idArea` int(11) DEFAULT NULL COMMENT 'Foreign key: khu vực',
  `email` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Địa chỉ Email',
  `bankName` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên ngân hàng',
  `bankAccount` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Số tk ngân hàng',
  `debt` decimal(19,4) NOT NULL DEFAULT '0.0000' COMMENT 'Công nợ. Nếu công nợ là số dương thì khách hàng đang nợ mình, ngược lại mình nợ khách hàng',
  `idCustomerType1` int(11) DEFAULT NULL COMMENT 'FK: id nhóm khách hàng 1',
  `idCustomerType2` int(11) DEFAULT NULL COMMENT 'FK: id nhóm khách hàng 2'
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `code`, `name`, `address`, `phoneNumber`, `idArea`, `email`, `bankName`, `bankAccount`, `debt`, `idCustomerType1`, `idCustomerType2`) VALUES
(33, 'KH002', 'Vy Mạnh Cường', 'Mĩ Đình', '01663724852', 1, 'cuongvm.55@gmail.com', 'Tiên Phong Bank', '00352078001', '100000.0000', 3, 1),
(34, 'KH1', 'Nguyễn Quốc Đạt', 'Mễ Trì Hạ', '01662329830', 1, 'datnq.55@gmail.com', 'TP Bank', '21231', '5000000.0000', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `customertype1`
--

CREATE TABLE IF NOT EXISTS `customertype1` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên nhóm khách hàng 1',
  `description` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `customertype1`
--

INSERT INTO `customertype1` (`id`, `name`, `description`) VALUES
(1, 'Loại KH 1 1', 'adadad fgfg'),
(3, 'Loại KH 1 2', 'ádadadadasd');

-- --------------------------------------------------------

--
-- Table structure for table `customertype2`
--

CREATE TABLE IF NOT EXISTS `customertype2` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên nhóm khách hàng 2',
  `description` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `customertype2`
--

INSERT INTO `customertype2` (`id`, `name`, `description`) VALUES
(1, 'Loại KH 2 1', ''),
(2, 'Loại KH 2 2', '');

-- --------------------------------------------------------

--
-- Table structure for table `material`
--

CREATE TABLE IF NOT EXISTS `material` (
  `id` int(11) NOT NULL COMMENT 'Id của vật tư',
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã vật tư',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên vật tư',
  `idMaterialType1` int(11) DEFAULT NULL COMMENT 'FK: id của loại vật tư 1',
  `idMaterialType2` int(11) DEFAULT NULL COMMENT 'FK: id của loại vật tư 2\\n',
  `price` decimal(19,4) NOT NULL COMMENT 'giá bán',
  `idStock` int(11) NOT NULL COMMENT 'FK: id của kho',
  `idUnit` int(11) DEFAULT NULL COMMENT 'Đơn vị tính',
  `quantity` int(11) NOT NULL DEFAULT '0' COMMENT 'Số lượng',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `material`
--

INSERT INTO `material` (`id`, `code`, `name`, `idMaterialType1`, `idMaterialType2`, `price`, `idStock`, `idUnit`, `quantity`, `description`) VALUES
(50, 'VT1', 'Xích1', 1, 2, '100000.0000', 2, 1, 6, 'adasdasdas'),
(51, 'VT2', 'Xích2', 1, 2, '100001.0000', 2, 1, 11, 'adasdasdas'),
(52, 'VT3', 'Xích3', 1, 2, '100002.0000', 16, 1, 12, 'adasdasdas'),
(53, 'VT4', 'Xích4', 1, 2, '100003.0000', 16, 1, 13, 'adasdasdas'),
(54, 'VT5', 'Xích5', 1, 2, '100004.0000', 16, 1, 14, 'adasdasdas'),
(55, 'VT6', 'Xích6', 1, 2, '100005.0000', 16, 1, 15, 'adasdasdas'),
(56, 'VT7', 'Xích7', 1, 2, '100006.0000', 16, 1, 16, 'adasdasdas'),
(57, 'VT8', 'Xích8', 1, 2, '100007.0000', 16, 1, 17, 'adasdasdas'),
(58, 'VT9', 'Xích9', 1, 2, '100008.0000', 16, 1, 18, 'adasdasdas'),
(59, 'VT10', 'Xích10', 1, 2, '100009.0000', 16, 1, 8, 'adasdasdas'),
(60, 'VT11', 'Xích11', 1, 2, '100010.0000', 16, 1, 14, 'adasdasdas'),
(61, 'VT12', 'Xích12', 1, 2, '100011.0000', 16, 1, 15, 'adasdasdas'),
(62, 'VT13', 'Xích13', 1, 2, '100012.0000', 16, 1, 22, 'adasdasdas'),
(63, 'VT14', 'Xích14', 1, 2, '100013.0000', 16, 1, 18, 'adasdasdas'),
(64, 'VT15', 'Xích15', 1, 2, '100014.0000', 16, 1, 24, 'adasdasdas'),
(65, 'VT16', 'Xích16', 1, 2, '100015.0000', 16, 1, 23, 'adasdasdas'),
(66, 'VT17', 'Xích17', 1, 2, '100016.0000', 16, 1, 23, 'adasdasdas'),
(67, 'VT18', 'Xích18', 1, 2, '100017.0000', 16, 1, 27, 'adasdasdas'),
(68, 'VT19', 'Xích19', 1, 2, '100018.0000', 16, 1, 28, 'adasdasdas');

-- --------------------------------------------------------

--
-- Table structure for table `materialinputhistory`
--

CREATE TABLE IF NOT EXISTS `materialinputhistory` (
  `id` int(11) NOT NULL,
  `idMaterial` int(11) NOT NULL COMMENT 'Id vật tư',
  `quantity` int(11) NOT NULL COMMENT 'Số lượng vt nhập',
  `inputPrice` decimal(19,0) NOT NULL COMMENT 'Giá nhập gần nhất',
  `date` date NOT NULL COMMENT 'Ngày history được ghi'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `materialinputhistory`
--

INSERT INTO `materialinputhistory` (`id`, `idMaterial`, `quantity`, `inputPrice`, `date`) VALUES
(1, 60, 2, '100010', '2016-04-18'),
(2, 50, 17, '98000', '2016-04-18');

-- --------------------------------------------------------

--
-- Table structure for table `materialoutputhistory`
--

CREATE TABLE IF NOT EXISTS `materialoutputhistory` (
  `id` int(11) NOT NULL,
  `idMaterial` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `outputPrice` decimal(19,0) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `materialoutputhistory`
--

INSERT INTO `materialoutputhistory` (`id`, `idMaterial`, `quantity`, `outputPrice`, `date`) VALUES
(5, 66, 3, '99016', '2016-04-17'),
(6, 59, 2, '98009', '2016-04-17'),
(7, 65, 2, '100015', '2016-04-17'),
(8, 63, 5, '100013', '2016-04-17'),
(9, 60, 3, '95010', '2016-04-17'),
(10, 50, 11, '100000', '2016-04-18');

-- --------------------------------------------------------

--
-- Table structure for table `materialtype1`
--

CREATE TABLE IF NOT EXISTS `materialtype1` (
  `id` int(11) NOT NULL COMMENT 'id của loại vật tư 1\\n',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên của loại vật tư 1',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `materialtype1`
--

INSERT INTO `materialtype1` (`id`, `name`, `description`) VALUES
(1, 'Nhông xích', 'sadadasdn fghgfhfgh'),
(2, 'Lốp', 'ádadadadasd');

-- --------------------------------------------------------

--
-- Table structure for table `materialtype2`
--

CREATE TABLE IF NOT EXISTS `materialtype2` (
  `id` int(11) NOT NULL COMMENT 'id của loại vật tư 2\\n',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên của loại vật tư 2',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `materialtype2`
--

INSERT INTO `materialtype2` (`id`, `name`, `description`) VALUES
(1, 'Sanyo', ''),
(2, 'SamSung', '');

-- --------------------------------------------------------

--
-- Table structure for table `money`
--

CREATE TABLE IF NOT EXISTS `money` (
  `id` int(11) NOT NULL,
  `amount` decimal(19,4) NOT NULL COMMENT 'Tổng tiền thực hiện giao dịch',
  `date` datetime NOT NULL COMMENT 'Ngày giờ thực hiện giao dịch',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả giao dịch, lấy từ content (diễn giải) của Order và các loại phiếu'
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `money`
--

INSERT INTO `money` (`id`, `amount`, `date`, `description`) VALUES
(1, '493065.1600', '2016-04-17 23:06:34', 'Mã phi?u thu PT116706 lý do Xu?t Bán Hàng Cho Khách'),
(2, '1193160.1600', '2016-04-17 23:09:32', 'Mã phi?u thu PT938713 lý do Xu?t Bán Hàng Cho Khách'),
(3, '1478188.6600', '2016-04-17 23:26:47', 'Mã phi?u thu PT1036311 lý do Xu?t Bán Hàng Cho Khách'),
(4, '1278168.6600', '2016-04-18 14:00:31', 'Mã phi?u mua hàng  lý do Nh?p Mua Hàng'),
(5, '308168.6600', '2016-04-18 14:08:37', 'Mã phi?u mua hàng PC232960 lý do Nh?p Mua Hàng'),
(6, '5308168.6600', '2016-04-18 14:15:36', 'Mã phi?u thu PT1141997 lý do Xu?t Bán Hàng Cho Khách'),
(7, '5108168.6600', '2016-04-18 14:16:29', 'Mã phi?u mua hàng PC341488 lý do Nh?p Mua Hàng'),
(8, '5298168.6600', '2016-04-18 15:47:40', 'Mã phi?u thu PT1239305 lý do Xu?t Bán Hàng Cho Khách'),
(9, '5498168.6600', '2016-04-18 15:54:58', 'Mã phi?u thu PT1338294 lý do Xu?t Bán Hàng Cho Khách'),
(10, '5198168.6600', '2016-04-18 15:55:26', 'Mã phi?u mua hàng PC41781 lý do Nh?p Mua Hàng'),
(11, '5398168.6600', '2016-04-18 17:16:28', 'Mã phi?u thu PT146295 lý do Xu?t Bán Hàng Cho Khách'),
(12, '5202168.6600', '2016-04-18 17:18:39', 'Mã phi?u mua hàng PC548741 lý do Nh?p Mua Hàng');

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE IF NOT EXISTS `order` (
  `id` int(11) NOT NULL,
  `orderCode` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã hóa đơn - số hóa đơn',
  `idOrderType` int(11) DEFAULT NULL COMMENT 'FK: Loại hóa đơn: xuất bán, xuất bán nội bộ',
  `idCustomer` int(11) DEFAULT '0' COMMENT 'FK: Mã khách hàng',
  `buyer` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên người mua hàng\\nMặc định là tên khách hàng, nhưng có thể thay đổi được',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải: Mặc định là: Xuất bán hàng cho khách nhưng có thể sửa được',
  `date` datetime NOT NULL COMMENT 'Ngày - giờ tạo hóa đơn',
  `note` text COLLATE utf8_unicode_ci COMMENT 'Ghi chú trên hóa đơn, nếu thay đổi địa chỉ nhận hàng thì ghi vào ghi chú'
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`id`, `orderCode`, `idOrderType`, `idCustomer`, `buyer`, `content`, `date`, `note`) VALUES
(15, 'HD137790', 1, 33, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-17 23:05:42', ''),
(16, 'HD1636559', 1, 34, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-17 23:08:57', ''),
(17, 'HD1711775', 1, 33, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-17 23:26:13', ''),
(18, 'HD184722', 1, 34, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-18 14:13:45', ''),
(19, 'HD1938016', 1, 33, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-18 15:46:51', ''),
(20, 'HD2030872', 1, 33, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-18 15:54:33', ''),
(21, 'HD2144413', 1, 33, '', 'Xu?t Bán Hàng Cho Khách', '2016-04-18 17:15:52', '');

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

CREATE TABLE IF NOT EXISTS `orderdetail` (
  `id` int(11) NOT NULL,
  `idMaterial` int(11) NOT NULL COMMENT 'FK: Id của vật tư',
  `idOrder` int(11) NOT NULL COMMENT 'FK: Id của Hóa đơn',
  `quantityNeeded` int(11) NOT NULL COMMENT 'Số lượng đặt hàng',
  `quantityDelivered` int(11) NOT NULL COMMENT 'Số lượng hàng đã xuất',
  `price` decimal(19,4) NOT NULL COMMENT 'Giá chuẩn, mặc định là lấy giá vật tư nhưng có thể sửa được',
  `saleOff` float DEFAULT '0' COMMENT 'Chiết khấu, tính theo phần trăm. Là phần trăm giảm giá trên giá gốc'
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`id`, `idMaterial`, `idOrder`, `quantityNeeded`, `quantityDelivered`, `price`, `saleOff`) VALUES
(25, 66, 15, 3, 3, '100016.0000', 0.01),
(26, 59, 15, 2, 2, '100009.0000', 0.02),
(27, 65, 16, 2, 2, '100015.0000', 0),
(28, 63, 16, 5, 5, '100013.0000', 0),
(29, 60, 17, 3, 3, '100010.0000', 0.05),
(30, 50, 18, 5, 5, '1000000.0000', 0),
(31, 50, 19, 2, 2, '100000.0000', 0.05),
(32, 50, 20, 2, 2, '100000.0000', 0),
(33, 50, 21, 2, 2, '100000.0000', 0);

-- --------------------------------------------------------

--
-- Table structure for table `ordertype`
--

CREATE TABLE IF NOT EXISTS `ordertype` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên loại hóa đơn',
  `description` text COLLATE utf8_unicode_ci COMMENT 'mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `ordertype`
--

INSERT INTO `ordertype` (`id`, `name`, `description`) VALUES
(1, 'Xuất bán', 'adadad fgfg'),
(2, 'Xuất bán nội bộ', 'sadsa');

-- --------------------------------------------------------

--
-- Table structure for table `receivingbill`
--

CREATE TABLE IF NOT EXISTS `receivingbill` (
  `id` int(11) NOT NULL,
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `idCustomer` int(11) DEFAULT '0' COMMENT 'FK: id của khách hàng',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải của phiếu thu',
  `date` datetime NOT NULL COMMENT 'ngày giờ tạo',
  `note` text COLLATE utf8_unicode_ci,
  `idOrder` int(11) DEFAULT '0' COMMENT 'FK: id của hóa đơn\\n- Phiếu chi có thể thuộc một hóa đơn',
  `idCoupon` int(11) DEFAULT '0' COMMENT 'FK: id của phiếu (phiếu xuất trả nhà cung cấp)\\n- Phiếu chi có thể thuộc một phiếu trong các loại phiếu trên'
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `receivingbill`
--

INSERT INTO `receivingbill` (`id`, `code`, `idCustomer`, `content`, `date`, `note`, `idOrder`, `idCoupon`) VALUES
(8, 'PT116706', 33, 'Xu?t Bán Hàng Cho Khách', '2016-04-17 23:05:42', '', 15, NULL),
(9, 'PT938713', 34, 'Xu?t Bán Hàng Cho Khách', '2016-04-17 23:08:57', '', 16, NULL),
(10, 'PT1036311', 33, 'Xu?t Bán Hàng Cho Khách', '2016-04-17 23:26:13', '', 17, NULL),
(11, 'PT1141997', 34, 'Xu?t Bán Hàng Cho Khách', '2016-04-18 14:13:45', '', 18, NULL),
(12, 'PT1239305', 33, 'Xu?t Bán Hàng Cho Khách', '2016-04-18 15:46:51', '', 19, NULL),
(13, 'PT1338294', 33, 'Xu?t Bán Hàng Cho Khách', '2016-04-18 15:54:33', '', 20, NULL),
(14, 'PT146295', 33, 'Xu?t Bán Hàng Cho Khách', '2016-04-18 17:15:52', '', 21, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `receivingbilldetail`
--

CREATE TABLE IF NOT EXISTS `receivingbilldetail` (
  `id` int(11) NOT NULL,
  `idReceivingBill` int(11) NOT NULL,
  `category` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Mặc định là \\"Thu tiền\\", có thể sửa được',
  `reason` text COLLATE utf8_unicode_ci COMMENT 'lý do thu tiền',
  `amount` decimal(19,4) NOT NULL COMMENT 'số thu vào'
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `receivingbilldetail`
--

INSERT INTO `receivingbilldetail` (`id`, `idReceivingBill`, `category`, `reason`, `amount`) VALUES
(5, 8, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '493065.1600'),
(6, 9, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '700095.0000'),
(7, 10, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '285028.5000'),
(8, 11, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '5000000.0000'),
(9, 12, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '190000.0000'),
(10, 13, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '200000.0000'),
(11, 14, 'Thu Ti?n', 'Xu?t Bán Hàng Cho Khách', '200000.0000');

-- --------------------------------------------------------

--
-- Table structure for table `spendingbill`
--

CREATE TABLE IF NOT EXISTS `spendingbill` (
  `id` int(11) NOT NULL,
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `idCustomer` int(11) DEFAULT '0' COMMENT 'FK: id của khách hàng',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải của phiếu chi',
  `date` datetime NOT NULL COMMENT 'ngày giờ tạo',
  `note` text COLLATE utf8_unicode_ci,
  `idCoupon` int(11) DEFAULT '0' COMMENT 'FK: id của phiếu (phiếu mua, phiếu nhập hàng trả lại)\\n- Phiếu chi có thể thuộc một phiếu trong các loại phiếu trên'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `spendingbill`
--

INSERT INTO `spendingbill` (`id`, `code`, `idCustomer`, `content`, `date`, `note`, `idCoupon`) VALUES
(1, '', 33, 'Nh?p Mua Hàng', '2016-04-18 14:00:00', '', 1),
(2, 'PC232960', 33, 'Nh?p Mua Hàng', '2016-04-18 14:07:13', '', 2),
(3, 'PC341488', 33, 'Nh?p Mua Hàng', '2016-04-18 14:15:52', '', 3),
(4, 'PC41781', 33, 'Nh?p Mua Hàng', '2016-04-18 15:54:33', '', 4),
(5, 'PC548741', 33, 'Nh?p Mua Hàng', '2016-04-18 17:18:21', '', 5);

-- --------------------------------------------------------

--
-- Table structure for table `spendingbilldetail`
--

CREATE TABLE IF NOT EXISTS `spendingbilldetail` (
  `id` int(11) NOT NULL,
  `idSpendingBill` int(11) NOT NULL,
  `category` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Mặc định là \\"Chi tiền\\", có thể sửa được',
  `reason` text COLLATE utf8_unicode_ci COMMENT 'lý do chi tiền',
  `amount` decimal(19,4) NOT NULL COMMENT 'số tiền chi ra'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `spendingbilldetail`
--

INSERT INTO `spendingbilldetail` (`id`, `idSpendingBill`, `category`, `reason`, `amount`) VALUES
(1, 1, 'Thu Ti?n', 'Nh?p Mua Hàng', '200020.0000'),
(2, 2, 'Thu Ti?n', 'Nh?p Mua Hàng', '970000.0000'),
(3, 3, 'Thu Ti?n', 'Nh?p Mua Hàng', '200000.0000'),
(4, 4, 'Thu Ti?n', 'Nh?p Mua Hàng', '300000.0000'),
(5, 5, 'Thu Ti?n', 'Nh?p Mua Hàng', '196000.0000');

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `id` int(11) NOT NULL COMMENT 'id của kho',
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã kho',
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên kho',
  `idStockType` int(11) DEFAULT NULL COMMENT 'Foreign key: id của loại kho',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`id`, `code`, `name`, `idStockType`, `description`) VALUES
(2, 'KHO_QN_BAN', 'Kho hàng bán Quan Nhân', 108, 'Kho hàng bán Quan Nhân'),
(16, 'KHO_QN_LOI', 'Kho hàng lỗi Quan Nhân', 109, 'Kho hàng lỗi Quan Nhân');

-- --------------------------------------------------------

--
-- Table structure for table `stocktype`
--

CREATE TABLE IF NOT EXISTS `stocktype` (
  `id` int(11) NOT NULL COMMENT 'id của kho',
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'tên loại kho',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `stocktype`
--

INSERT INTO `stocktype` (`id`, `name`, `description`) VALUES
(108, 'KH_HANGBAN', 'Kho chứa hàng bán'),
(109, 'KH_HANGLOI', 'Kho chứa hàng lỗi');

-- --------------------------------------------------------

--
-- Table structure for table `unit`
--

CREATE TABLE IF NOT EXISTS `unit` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `unit`
--

INSERT INTO `unit` (`id`, `name`) VALUES
(1, 'cái'),
(6, 'Lít'),
(7, 'Mét');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `area`
--
ALTER TABLE `area`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `coupon`
--
ALTER TABLE `coupon`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_Coupon_CouponType1_idx` (`idCouponType`), ADD KEY `fk_Coupon_Customer1_idx` (`idCustomer`);

--
-- Indexes for table `coupondetail`
--
ALTER TABLE `coupondetail`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_CouponDetail_Coupon1_idx` (`idCoupon`), ADD KEY `fk_CouponDetail_Material1_idx` (`idMaterial`);

--
-- Indexes for table `coupontype`
--
ALTER TABLE `coupontype`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_Customer_CustomerType11_idx` (`idCustomerType1`), ADD KEY `fk_Customer_CustomerType21_idx` (`idCustomerType2`), ADD KEY `fk_Customer_Area1_idx` (`idArea`);

--
-- Indexes for table `customertype1`
--
ALTER TABLE `customertype1`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customertype2`
--
ALTER TABLE `customertype2`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `material`
--
ALTER TABLE `material`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_Material_MaterialType1_idx` (`idMaterialType1`), ADD KEY `fk_Material_MaterialType21_idx` (`idMaterialType2`), ADD KEY `fk_Material_Stock1_idx` (`idStock`), ADD KEY `fk_unit_id` (`idUnit`);

--
-- Indexes for table `materialinputhistory`
--
ALTER TABLE `materialinputhistory`
  ADD PRIMARY KEY (`id`), ADD KEY `id` (`id`), ADD KEY `id_2` (`id`), ADD KEY `fk_material_id` (`idMaterial`);

--
-- Indexes for table `materialoutputhistory`
--
ALTER TABLE `materialoutputhistory`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_material_id2` (`idMaterial`);

--
-- Indexes for table `materialtype1`
--
ALTER TABLE `materialtype1`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `materialtype2`
--
ALTER TABLE `materialtype2`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `money`
--
ALTER TABLE `money`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_Order_OrderType1_idx` (`idOrderType`), ADD KEY `fk_Order_Customer1_idx` (`idCustomer`);

--
-- Indexes for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_OrderDetail_Material1_idx` (`idMaterial`), ADD KEY `fk_OrderDetail_Order1_idx` (`idOrder`);

--
-- Indexes for table `ordertype`
--
ALTER TABLE `ordertype`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `receivingbill`
--
ALTER TABLE `receivingbill`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_SpendingBill_Customer1_idx` (`idCustomer`), ADD KEY `fk_SpendingBill_Order1_idx` (`idOrder`), ADD KEY `fk_SpendingBill_Coupon1_idx` (`idCoupon`);

--
-- Indexes for table `receivingbilldetail`
--
ALTER TABLE `receivingbilldetail`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_ReceivingBillDetail_ReceivingBill1_idx` (`idReceivingBill`);

--
-- Indexes for table `spendingbill`
--
ALTER TABLE `spendingbill`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_SpendingBill_Customer1_idx` (`idCustomer`), ADD KEY `fk_SpendingBill_Coupon1_idx` (`idCoupon`);

--
-- Indexes for table `spendingbilldetail`
--
ALTER TABLE `spendingbilldetail`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_SpendingBillDetail_SpendingBill1_idx` (`idSpendingBill`);

--
-- Indexes for table `stock`
--
ALTER TABLE `stock`
  ADD PRIMARY KEY (`id`), ADD KEY `fk_Stock_StockType1_idx` (`idStockType`);

--
-- Indexes for table `stocktype`
--
ALTER TABLE `stocktype`
  ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `unit`
--
ALTER TABLE `unit`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `area`
--
ALTER TABLE `area`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `coupon`
--
ALTER TABLE `coupon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `coupondetail`
--
ALTER TABLE `coupondetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `coupontype`
--
ALTER TABLE `coupontype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của khách hàng',AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT for table `customertype1`
--
ALTER TABLE `customertype1`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `customertype2`
--
ALTER TABLE `customertype2`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `material`
--
ALTER TABLE `material`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id của vật tư',AUTO_INCREMENT=69;
--
-- AUTO_INCREMENT for table `materialinputhistory`
--
ALTER TABLE `materialinputhistory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `materialoutputhistory`
--
ALTER TABLE `materialoutputhistory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `materialtype1`
--
ALTER TABLE `materialtype1`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của loại vật tư 1\\n',AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `materialtype2`
--
ALTER TABLE `materialtype2`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của loại vật tư 2\\n',AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `money`
--
ALTER TABLE `money`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT for table `orderdetail`
--
ALTER TABLE `orderdetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=34;
--
-- AUTO_INCREMENT for table `ordertype`
--
ALTER TABLE `ordertype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `receivingbill`
--
ALTER TABLE `receivingbill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `receivingbilldetail`
--
ALTER TABLE `receivingbilldetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `spendingbill`
--
ALTER TABLE `spendingbill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `spendingbilldetail`
--
ALTER TABLE `spendingbilldetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `stock`
--
ALTER TABLE `stock`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của kho',AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `stocktype`
--
ALTER TABLE `stocktype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của kho',AUTO_INCREMENT=110;
--
-- AUTO_INCREMENT for table `unit`
--
ALTER TABLE `unit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `coupon`
--
ALTER TABLE `coupon`
ADD CONSTRAINT `fk_Coupon_CouponType1` FOREIGN KEY (`idCouponType`) REFERENCES `coupontype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Coupon_Customer1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `coupondetail`
--
ALTER TABLE `coupondetail`
ADD CONSTRAINT `fk_CouponDetail_Coupon1` FOREIGN KEY (`idCoupon`) REFERENCES `coupon` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_CouponDetail_Material1` FOREIGN KEY (`idMaterial`) REFERENCES `material` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
ADD CONSTRAINT `fk_Customer_Area1` FOREIGN KEY (`idArea`) REFERENCES `area` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Customer_CustomerType11` FOREIGN KEY (`idCustomerType1`) REFERENCES `customertype1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Customer_CustomerType21` FOREIGN KEY (`idCustomerType2`) REFERENCES `customertype2` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `material`
--
ALTER TABLE `material`
ADD CONSTRAINT `fk_Material_MaterialType1` FOREIGN KEY (`idMaterialType1`) REFERENCES `materialtype1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Material_MaterialType21` FOREIGN KEY (`idMaterialType2`) REFERENCES `materialtype2` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Material_Stock1` FOREIGN KEY (`idStock`) REFERENCES `stock` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_unit_id` FOREIGN KEY (`idUnit`) REFERENCES `unit` (`id`);

--
-- Constraints for table `materialinputhistory`
--
ALTER TABLE `materialinputhistory`
ADD CONSTRAINT `fk_material_id` FOREIGN KEY (`idMaterial`) REFERENCES `material` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `materialoutputhistory`
--
ALTER TABLE `materialoutputhistory`
ADD CONSTRAINT `fk_material_id2` FOREIGN KEY (`idMaterial`) REFERENCES `material` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order`
--
ALTER TABLE `order`
ADD CONSTRAINT `fk_Order_Customer1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Order_OrderType1` FOREIGN KEY (`idOrderType`) REFERENCES `ordertype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `orderdetail`
--
ALTER TABLE `orderdetail`
ADD CONSTRAINT `fk_OrderDetail_Material1` FOREIGN KEY (`idMaterial`) REFERENCES `material` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_OrderDetail_Order1` FOREIGN KEY (`idOrder`) REFERENCES `order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `receivingbill`
--
ALTER TABLE `receivingbill`
ADD CONSTRAINT `fk_Receivingbill_Customer1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `receivingbilldetail`
--
ALTER TABLE `receivingbilldetail`
ADD CONSTRAINT `fk_ReceivingBillDetail_ReceivingBill1` FOREIGN KEY (`idReceivingBill`) REFERENCES `receivingbill` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `spendingbill`
--
ALTER TABLE `spendingbill`
ADD CONSTRAINT `fk_Spendingbill_Customer1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`);

--
-- Constraints for table `spendingbilldetail`
--
ALTER TABLE `spendingbilldetail`
ADD CONSTRAINT `fk_SpendingBillDetail_SpendingBill1` FOREIGN KEY (`idSpendingBill`) REFERENCES `spendingbill` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `stock`
--
ALTER TABLE `stock`
ADD CONSTRAINT `fk_Stock_StockType1` FOREIGN KEY (`idStockType`) REFERENCES `stocktype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
