SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `stockmanagement` ;
CREATE SCHEMA IF NOT EXISTS `stockmanagement` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;
USE `stockmanagement` ;

-- -----------------------------------------------------
-- Table `stockmanagement`.`MaterialType1`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`MaterialType1` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`MaterialType1` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id của loại vật tư 1\\n' ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên của loại vật tư 1' ,
  `description` TEXT NULL COMMENT 'Mô tả' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`MaterialType2`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`MaterialType2` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`MaterialType2` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id của loại vật tư 2\\n' ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên của loại vật tư 2' ,
  `description` TEXT NULL COMMENT 'Mô tả' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`StockType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`StockType` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`StockType` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id của kho' ,
  `name` VARCHAR(45) NOT NULL COMMENT 'tên loại kho' ,
  `description` TEXT NULL COMMENT 'Mô tả' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Stock` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Stock` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id của kho' ,
  `code` VARCHAR(45) NOT NULL COMMENT 'Mã kho' ,
  `name` VARCHAR(45) NOT NULL COMMENT 'Tên kho' ,
  `idStockType` INT NOT NULL COMMENT 'Foreign key: id của loại kho' ,
  `description` TEXT NULL COMMENT 'Mô tả' ,
  PRIMARY KEY (`id`, `idStockType`) ,
  INDEX `fk_Stock_StockType1_idx` (`idStockType` ASC) ,
  CONSTRAINT `fk_Stock_StockType1`
    FOREIGN KEY (`idStockType` )
    REFERENCES `stockmanagement`.`StockType` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Material` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Material` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Id của vật tư' ,
  `code` VARCHAR(45) NOT NULL COMMENT 'Mã vật tư' ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên vật tư' ,
  `idMaterialType1` INT NOT NULL COMMENT 'FK: id của loại vật tư 1' ,
  `idMaterialType2` INT NOT NULL COMMENT 'FK: id của loại vật tư 2\\n' ,
  `price` DECIMAL(19,4) NOT NULL COMMENT 'giá bán' ,
  `idStock` INT NOT NULL COMMENT 'FK: id của kho' ,
  `unit` VARCHAR(45) NULL COMMENT 'Đơn vị tính' ,
  `quantity` INT NOT NULL COMMENT 'Số lượng' ,
  `description` TEXT NULL COMMENT 'Mô tả' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Material_MaterialType1_idx` (`idMaterialType1` ASC) ,
  INDEX `fk_Material_MaterialType21_idx` (`idMaterialType2` ASC) ,
  INDEX `fk_Material_Stock1_idx` (`idStock` ASC) ,
  CONSTRAINT `fk_Material_MaterialType1`
    FOREIGN KEY (`idMaterialType1` )
    REFERENCES `stockmanagement`.`MaterialType1` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Material_MaterialType21`
    FOREIGN KEY (`idMaterialType2` )
    REFERENCES `stockmanagement`.`MaterialType2` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Material_Stock1`
    FOREIGN KEY (`idStock` )
    REFERENCES `stockmanagement`.`Stock` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`CustomerType1`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`CustomerType1` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`CustomerType1` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên nhóm khách hàng 1' ,
  `description` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`CustomerType2`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`CustomerType2` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`CustomerType2` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên nhóm khách hàng 2' ,
  `description` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Area`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Area` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Area` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên khu vực' ,
  `description` TEXT NULL COMMENT 'Mô tả' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Customer` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Customer` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id của khách hàng' ,
  `code` VARCHAR(45) NOT NULL COMMENT 'Mã khách hàng' ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên khách hàng' ,
  `address` TEXT NULL COMMENT 'Địa chỉ' ,
  `phoneNumber` VARCHAR(20) NULL COMMENT 'Số điện thoại' ,
  `idArea` INT NULL COMMENT 'Foreign key: khu vực' ,
  `email` VARCHAR(256) NULL COMMENT 'Địa chỉ Email' ,
  `bankName` VARCHAR(256) NULL COMMENT 'Tên ngân hàng' ,
  `bankAccount` VARCHAR(45) NULL COMMENT 'Số tk ngân hàng' ,
  `debt` DECIMAL(19,4) NOT NULL COMMENT 'Công nợ. Nếu công nợ là số dương thì khách hàng đang nợ mình, ngược lại mình nợ khách hàng' ,
  `idCustomerType1` INT NULL COMMENT 'FK: id nhóm khách hàng 1' ,
  `idCustomerType2` INT NULL COMMENT 'FK: id nhóm khách hàng 2' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Customer_CustomerType11_idx` (`idCustomerType1` ASC) ,
  INDEX `fk_Customer_CustomerType21_idx` (`idCustomerType2` ASC) ,
  INDEX `fk_Customer_Area1_idx` (`idArea` ASC) ,
  CONSTRAINT `fk_Customer_CustomerType11`
    FOREIGN KEY (`idCustomerType1` )
    REFERENCES `stockmanagement`.`CustomerType1` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Customer_CustomerType21`
    FOREIGN KEY (`idCustomerType2` )
    REFERENCES `stockmanagement`.`CustomerType2` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Customer_Area1`
    FOREIGN KEY (`idArea` )
    REFERENCES `stockmanagement`.`Area` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`OrderType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`OrderType` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`OrderType` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(128) NOT NULL COMMENT 'Tên loại hóa đơn' ,
  `description` TEXT NULL COMMENT 'mô tả' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Order` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Order` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `orderCode` VARCHAR(45) NOT NULL COMMENT 'Mã hóa đơn - số hóa đơn' ,
  `idOrderType` INT NULL COMMENT 'FK: Loại hóa đơn: xuất bán, xuất bán nội bộ' ,
  `idCustomer` INT NULL COMMENT 'FK: Mã khách hàng' ,
  `buyer` VARCHAR(128) NULL COMMENT 'Tên người mua hàng\\nMặc định là tên khách hàng, nhưng có thể thay đổi được' ,
  `content` TEXT NOT NULL COMMENT 'Diễn giải: Mặc định là: Xuất bán hàng cho khách nhưng có thể sửa được' ,
  `date` DATETIME NOT NULL COMMENT 'Ngày - giờ tạo hóa đơn' ,
  `note` TEXT NULL COMMENT 'Ghi chú trên hóa đơn, nếu thay đổi địa chỉ nhận hàng thì ghi vào ghi chú' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Order_OrderType1_idx` (`idOrderType` ASC) ,
  INDEX `fk_Order_Customer1_idx` (`idCustomer` ASC) ,
  CONSTRAINT `fk_Order_OrderType1`
    FOREIGN KEY (`idOrderType` )
    REFERENCES `stockmanagement`.`OrderType` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Customer1`
    FOREIGN KEY (`idCustomer` )
    REFERENCES `stockmanagement`.`Customer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`OrderDetail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`OrderDetail` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`OrderDetail` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `idMaterial` INT NOT NULL COMMENT 'FK: Id của vật tư' ,
  `idOrder` INT NOT NULL COMMENT 'FK: Id của Hóa đơn' ,
  `quantityNeeded` INT NOT NULL COMMENT 'Số lượng đặt hàng' ,
  `quantityDelivered` INT NOT NULL COMMENT 'Số lượng hàng đã xuất' ,
  `price` DECIMAL(19,4) NOT NULL COMMENT 'Giá chuẩn, mặc định là lấy giá vật tư nhưng có thể sửa được' ,
  `saleOff` INT NULL COMMENT 'Chiết khấu, tính theo phần trăm. Là phần trăm giảm giá trên giá gốc' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_OrderDetail_Material1_idx` (`idMaterial` ASC) ,
  INDEX `fk_OrderDetail_Order1_idx` (`idOrder` ASC) ,
  CONSTRAINT `fk_OrderDetail_Material1`
    FOREIGN KEY (`idMaterial` )
    REFERENCES `stockmanagement`.`Material` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrderDetail_Order1`
    FOREIGN KEY (`idOrder` )
    REFERENCES `stockmanagement`.`Order` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`CouponType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`CouponType` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`CouponType` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(128) NOT NULL COMMENT 'tên loại phiếu: Nhập mua, nhập hàng trả lại, xuất trả nhà cung cấp' ,
  `description` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Coupon`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Coupon` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Coupon` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(45) NOT NULL COMMENT 'Mã phiếu hóa đơn' ,
  `idCouponType` INT NOT NULL COMMENT 'FK: Loại phiếu: Nhập mua, nhập hàng trả lại, xuất trả nhà cung cấp' ,
  `idCustomer` INT NULL COMMENT 'FK: id của khách hàng' ,
  `buyer` VARCHAR(128) NULL COMMENT 'Người giao hàng/người nhận hàng' ,
  `content` TEXT NOT NULL COMMENT 'Diễn giải: Mặc định là lấy tên của loại phiếu' ,
  `date` DATETIME NOT NULL COMMENT 'Ngày giờ tạo' ,
  `note` TEXT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Coupon_CouponType1_idx` (`idCouponType` ASC) ,
  INDEX `fk_Coupon_Customer1_idx` (`idCustomer` ASC) ,
  CONSTRAINT `fk_Coupon_CouponType1`
    FOREIGN KEY (`idCouponType` )
    REFERENCES `stockmanagement`.`CouponType` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Coupon_Customer1`
    FOREIGN KEY (`idCustomer` )
    REFERENCES `stockmanagement`.`Customer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`CouponDetail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`CouponDetail` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`CouponDetail` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `idMaterial` INT NOT NULL COMMENT 'FK: id của vật tư' ,
  `idCoupon` INT NOT NULL COMMENT 'FK: id của phiếu' ,
  `quantity` INT NOT NULL ,
  `price` DECIMAL(19,4) NOT NULL COMMENT 'Giá chuẩn: mặc định lấy từ vật tư, có thể sửa được' ,
  `saleOff` INT NULL COMMENT 'Chiết khấu, đơn vị phần trăm' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_CouponDetail_Coupon1_idx` (`idCoupon` ASC) ,
  INDEX `fk_CouponDetail_Material1_idx` (`idMaterial` ASC) ,
  CONSTRAINT `fk_CouponDetail_Coupon1`
    FOREIGN KEY (`idCoupon` )
    REFERENCES `stockmanagement`.`Coupon` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CouponDetail_Material1`
    FOREIGN KEY (`idMaterial` )
    REFERENCES `stockmanagement`.`Material` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`SpendingBill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`SpendingBill` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`SpendingBill` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(45) NOT NULL ,
  `idCustomer` INT NULL COMMENT 'FK: id của khách hàng' ,
  `content` TEXT NOT NULL COMMENT 'Diễn giải của phiếu chi' ,
  `date` DATETIME NOT NULL COMMENT 'ngày giờ tạo' ,
  `note` TEXT NULL ,
  `idCoupon` INT NULL COMMENT 'FK: id của phiếu (phiếu mua, phiếu nhập hàng trả lại)\\n- Phiếu chi có thể thuộc một phiếu trong các loại phiếu trên' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_SpendingBill_Customer1_idx` (`idCustomer` ASC) ,
  INDEX `fk_SpendingBill_Coupon1_idx` (`idCoupon` ASC) ,
  CONSTRAINT `fk_SpendingBill_Customer1`
    FOREIGN KEY (`idCustomer` )
    REFERENCES `stockmanagement`.`Customer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SpendingBill_Coupon1`
    FOREIGN KEY (`idCoupon` )
    REFERENCES `stockmanagement`.`Coupon` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`SpendingBillDetail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`SpendingBillDetail` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`SpendingBillDetail` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `idSpendingBill` INT NOT NULL ,
  `category` VARCHAR(256) NULL COMMENT 'Mặc định là \\\"Chi tiền\\\", có thể sửa được' ,
  `reason` TEXT NULL COMMENT 'lý do chi tiền' ,
  `amount` DECIMAL(19,4) NOT NULL COMMENT 'số tiền chi ra' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_SpendingBillDetail_SpendingBill1_idx` (`idSpendingBill` ASC) ,
  CONSTRAINT `fk_SpendingBillDetail_SpendingBill1`
    FOREIGN KEY (`idSpendingBill` )
    REFERENCES `stockmanagement`.`SpendingBill` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`ReceivingBill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`ReceivingBill` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`ReceivingBill` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(45) NOT NULL ,
  `idCustomer` INT NOT NULL COMMENT 'FK: id của khách hàng' ,
  `content` TEXT NOT NULL COMMENT 'Diễn giải của phiếu thu' ,
  `date` DATETIME NOT NULL COMMENT 'ngày giờ tạo' ,
  `note` TEXT NULL ,
  `idOrder` INT NULL COMMENT 'FK: id của hóa đơn\\n- Phiếu chi có thể thuộc một hóa đơn' ,
  `idCoupon` INT NULL COMMENT 'FK: id của phiếu (phiếu xuất trả nhà cung cấp)\\n- Phiếu chi có thể thuộc một phiếu trong các loại phiếu trên' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_SpendingBill_Customer1_idx` (`idCustomer` ASC) ,
  INDEX `fk_SpendingBill_Order1_idx` (`idOrder` ASC) ,
  INDEX `fk_SpendingBill_Coupon1_idx` (`idCoupon` ASC) ,
  CONSTRAINT `fk_SpendingBill_Customer10`
    FOREIGN KEY (`idCustomer` )
    REFERENCES `stockmanagement`.`Customer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SpendingBill_Order10`
    FOREIGN KEY (`idOrder` )
    REFERENCES `stockmanagement`.`Order` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SpendingBill_Coupon10`
    FOREIGN KEY (`idCoupon` )
    REFERENCES `stockmanagement`.`Coupon` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`ReceivingBillDetail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`ReceivingBillDetail` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`ReceivingBillDetail` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `idReceivingBill` INT NOT NULL ,
  `category` VARCHAR(256) NULL COMMENT 'Mặc định là \\\"Thu tiền\\\", có thể sửa được' ,
  `reason` TEXT NULL COMMENT 'lý do thu tiền' ,
  `amount` DECIMAL(19,4) NOT NULL COMMENT 'số thu vào' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_ReceivingBillDetail_ReceivingBill1_idx` (`idReceivingBill` ASC) ,
  CONSTRAINT `fk_ReceivingBillDetail_ReceivingBill1`
    FOREIGN KEY (`idReceivingBill` )
    REFERENCES `stockmanagement`.`ReceivingBill` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `stockmanagement`.`Money`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `stockmanagement`.`Money` ;

CREATE  TABLE IF NOT EXISTS `stockmanagement`.`Money` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `amount` DECIMAL(19,4) NOT NULL COMMENT 'Tổng tiền thực hiện giao dịch' ,
  `date` DATETIME NOT NULL COMMENT 'Ngày giờ thực hiện giao dịch' ,
  `description` TEXT NULL COMMENT 'Mô tả giao dịch, lấy từ content (diễn giải) của Order và các loại phiếu' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
