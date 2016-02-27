-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 27, 2016 at 06:59 PM
-- Server version: 5.6.26-log
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
	
		CALL debug_msg(debugMode, (select concat_ws('',"inputWord:", currentInputWord)));
		CALL debug_msg(debugMode, (select concat_ws('',"currentCriteriaWord:", currentCriteriaWord)));
	checkCriteria: WHILE ( !(inputIdx > LENGTH(inputStr) || criteriaIdx > LENGTH(criteria)) ) DO
			#IF ( SUBSTRING( currentInputWord, 1, LENGTH(currentCriteriaWord) ) NOT LIKE currentCriteriaWord ) THEN
			#IF ( SUBSTRING( currentInputWord, 1, LENGTH(currentCriteriaWord) ) NOT LIKE concat('^', currentCriteriaWord, '$') ) THEN
			IF ( STRCMP(SUBSTRING(currentInputWord, 1, LENGTH(currentCriteriaWord)), currentCriteriaWord) != 0 ) THEN
				SET bOk = FALSE;
				LEAVE checkCriteria;
			END IF;
			
			CALL debug_msg(debugMode, "MATCHED DETECTED!!!");
			SET inputIdx = wordBoundInputIdx + 2;
			SET criteriaIdx = wordBoundIdxCriteria + 2;

			SET wordBoundIdxCriteria = IF((LOCATE(" ", criteria, criteriaIdx) != 0), (LOCATE(" ", criteria, criteriaIdx) - 1), LENGTH(criteria) );
			SET currentCriteriaWord = SUBSTRING(criteria, criteriaIdx, wordBoundIdxCriteria - criteriaIdx + 1);
			
			SET wordBoundInputIdx = IF((LOCATE(" ", inputStr, inputIdx) != 0), (LOCATE(" ", inputStr, inputIdx) - 1), LENGTH(inputStr) );
			SET currentInputWord = SUBSTRING(inputStr, inputIdx, wordBoundInputIdx - inputIdx + 1);
			
			CALL debug_msg(debugMode, (select concat_ws('',"inputWord:", currentInputWord)));
			CALL debug_msg(debugMode, (select concat_ws('',"currentCriteriaWord:", currentCriteriaWord)));
		END WHILE;
		CALL debug_msg(debugMode, (select concat_ws('',"criteriaIdx:", criteriaIdx)));
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
  `saleOff` int(11) DEFAULT '0' COMMENT 'Chiết khấu, đơn vị phần trăm'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `coupontype`
--

CREATE TABLE IF NOT EXISTS `coupontype` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'tên loại phiếu: Nhập mua, nhập hàng trả lại, xuất trả nhà cung cấp',
  `description` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customertype1`
--

CREATE TABLE IF NOT EXISTS `customertype1` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên nhóm khách hàng 1',
  `description` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customertype2`
--

CREATE TABLE IF NOT EXISTS `customertype2` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên nhóm khách hàng 2',
  `description` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `material`
--

CREATE TABLE IF NOT EXISTS `material` (
  `id` int(11) NOT NULL COMMENT 'Id của vật tư',
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã vật tư',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên vật tư',
  `idMaterialType1` int(11) NOT NULL COMMENT 'FK: id của loại vật tư 1',
  `idMaterialType2` int(11) NOT NULL COMMENT 'FK: id của loại vật tư 2\\n',
  `price` decimal(19,4) NOT NULL COMMENT 'giá bán',
  `idStock` int(11) NOT NULL COMMENT 'FK: id của kho',
  `idUnit` int(11) DEFAULT NULL COMMENT 'Đơn vị tính',
  `quantity` int(11) NOT NULL DEFAULT '0' COMMENT 'Số lượng',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `materialtype1`
--

CREATE TABLE IF NOT EXISTS `materialtype1` (
  `id` int(11) NOT NULL COMMENT 'id của loại vật tư 1\\n',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên của loại vật tư 1',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `materialtype2`
--

CREATE TABLE IF NOT EXISTS `materialtype2` (
  `id` int(11) NOT NULL COMMENT 'id của loại vật tư 2\\n',
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên của loại vật tư 2',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `money`
--

CREATE TABLE IF NOT EXISTS `money` (
  `id` int(11) NOT NULL,
  `amount` decimal(19,4) NOT NULL COMMENT 'Tổng tiền thực hiện giao dịch',
  `date` datetime NOT NULL COMMENT 'Ngày giờ thực hiện giao dịch',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả giao dịch, lấy từ content (diễn giải) của Order và các loại phiếu'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE IF NOT EXISTS `order` (
  `id` int(11) NOT NULL,
  `orderCode` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã hóa đơn - số hóa đơn',
  `idOrderType` int(11) DEFAULT NULL COMMENT 'FK: Loại hóa đơn: xuất bán, xuất bán nội bộ',
  `idCustomer` int(11) DEFAULT NULL COMMENT 'FK: Mã khách hàng',
  `buyer` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Tên người mua hàng\\nMặc định là tên khách hàng, nhưng có thể thay đổi được',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải: Mặc định là: Xuất bán hàng cho khách nhưng có thể sửa được',
  `date` datetime NOT NULL COMMENT 'Ngày - giờ tạo hóa đơn',
  `note` text COLLATE utf8_unicode_ci COMMENT 'Ghi chú trên hóa đơn, nếu thay đổi địa chỉ nhận hàng thì ghi vào ghi chú'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
  `saleOff` int(11) DEFAULT NULL COMMENT 'Chiết khấu, tính theo phần trăm. Là phần trăm giảm giá trên giá gốc'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ordertype`
--

CREATE TABLE IF NOT EXISTS `ordertype` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên loại hóa đơn',
  `description` text COLLATE utf8_unicode_ci COMMENT 'mô tả'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `receivingbill`
--

CREATE TABLE IF NOT EXISTS `receivingbill` (
  `id` int(11) NOT NULL,
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `idCustomer` int(11) NOT NULL COMMENT 'FK: id của khách hàng',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải của phiếu thu',
  `date` datetime NOT NULL COMMENT 'ngày giờ tạo',
  `note` text COLLATE utf8_unicode_ci,
  `idOrder` int(11) DEFAULT NULL COMMENT 'FK: id của hóa đơn\\n- Phiếu chi có thể thuộc một hóa đơn',
  `idCoupon` int(11) DEFAULT NULL COMMENT 'FK: id của phiếu (phiếu xuất trả nhà cung cấp)\\n- Phiếu chi có thể thuộc một phiếu trong các loại phiếu trên'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `spendingbill`
--

CREATE TABLE IF NOT EXISTS `spendingbill` (
  `id` int(11) NOT NULL,
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `idCustomer` int(11) DEFAULT NULL COMMENT 'FK: id của khách hàng',
  `content` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'Diễn giải của phiếu chi',
  `date` datetime NOT NULL COMMENT 'ngày giờ tạo',
  `note` text COLLATE utf8_unicode_ci,
  `idCoupon` int(11) DEFAULT NULL COMMENT 'FK: id của phiếu (phiếu mua, phiếu nhập hàng trả lại)\\n- Phiếu chi có thể thuộc một phiếu trong các loại phiếu trên'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `id` int(11) NOT NULL COMMENT 'id của kho',
  `code` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Mã kho',
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Tên kho',
  `idStockType` int(11) NOT NULL COMMENT 'Foreign key: id của loại kho',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`id`, `code`, `name`, `idStockType`, `description`) VALUES
(3, 'KH2', 'Kho hàng 2', 2, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `stocktype`
--

CREATE TABLE IF NOT EXISTS `stocktype` (
  `id` int(11) NOT NULL COMMENT 'id của kho',
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'tên loại kho',
  `description` text COLLATE utf8_unicode_ci COMMENT 'Mô tả'
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `stocktype`
--

INSERT INTO `stocktype` (`id`, `name`, `description`) VALUES
(2, 'Kho 2sdfsdf', 'sdfsdf'),
(19, 'wqwrq', 'afadf'),
(20, 'qwrqwf', 'dfsdf'),
(21, 'qfqefq', 'dfsdf'),
(26, 'adalsdj', '\nasd1312413414'),
(27, 'qf2234234', ''),
(29, '3423jlkjwklerjklwr', '`t2t34twt'),
(30, '14432adf', '3r232r32'),
(31, '23r23wdatnq3', 'wfwef32f'),
(32, 'weff23f', 'wef23f2f'),
(33, '32fwef3', ''),
(39, 'wqrf', '2342e23e'),
(42, 'Nguy?n Qu?c ??t', '12344'),
(43, 'Vy m?nh C??ng', 'C??ng vm4'),
(44, 'dlfjldfj', 'aldfjlkdf'),
(45, 'aflsjdfl', 'ldfjwlkejfklw');

-- --------------------------------------------------------

--
-- Table structure for table `unit`
--

CREATE TABLE IF NOT EXISTS `unit` (
  `id` int(11) NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `unit`
--

INSERT INTO `unit` (`id`, `name`) VALUES
(1, 'cái'),
(2, 'lít');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `coupon`
--
ALTER TABLE `coupon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `coupondetail`
--
ALTER TABLE `coupondetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `coupontype`
--
ALTER TABLE `coupontype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của khách hàng';
--
-- AUTO_INCREMENT for table `customertype1`
--
ALTER TABLE `customertype1`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `customertype2`
--
ALTER TABLE `customertype2`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `material`
--
ALTER TABLE `material`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id của vật tư';
--
-- AUTO_INCREMENT for table `materialtype1`
--
ALTER TABLE `materialtype1`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của loại vật tư 1\\n';
--
-- AUTO_INCREMENT for table `materialtype2`
--
ALTER TABLE `materialtype2`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của loại vật tư 2\\n';
--
-- AUTO_INCREMENT for table `money`
--
ALTER TABLE `money`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `orderdetail`
--
ALTER TABLE `orderdetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ordertype`
--
ALTER TABLE `ordertype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `receivingbill`
--
ALTER TABLE `receivingbill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `receivingbilldetail`
--
ALTER TABLE `receivingbilldetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `spendingbill`
--
ALTER TABLE `spendingbill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `spendingbilldetail`
--
ALTER TABLE `spendingbilldetail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `stock`
--
ALTER TABLE `stock`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của kho',AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `stocktype`
--
ALTER TABLE `stocktype`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id của kho',AUTO_INCREMENT=46;
--
-- AUTO_INCREMENT for table `unit`
--
ALTER TABLE `unit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
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
ADD CONSTRAINT `fk_SpendingBill_Coupon10` FOREIGN KEY (`idCoupon`) REFERENCES `coupon` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SpendingBill_Customer10` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SpendingBill_Order10` FOREIGN KEY (`idOrder`) REFERENCES `order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `receivingbilldetail`
--
ALTER TABLE `receivingbilldetail`
ADD CONSTRAINT `fk_ReceivingBillDetail_ReceivingBill1` FOREIGN KEY (`idReceivingBill`) REFERENCES `receivingbill` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `spendingbill`
--
ALTER TABLE `spendingbill`
ADD CONSTRAINT `fk_SpendingBill_Coupon1` FOREIGN KEY (`idCoupon`) REFERENCES `coupon` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SpendingBill_Customer1` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
