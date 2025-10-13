-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`medicamentos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`medicamentos` (
  `idMedicamento` VARCHAR(45) NOT NULL,
  `nombreMedicamento` VARCHAR(45) NOT NULL,
  `presentacionMedicamento` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idMedicamento`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`usuarios` (
  `idUsuario` VARCHAR(45) NOT NULL,
  `claveUsuario` VARCHAR(45) NOT NULL,
  `nombreUsuario` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUsuario`, `claveUsuario`, `nombreUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`medicos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`medicos` (
  `usuarios_idUsuario` VARCHAR(45) NOT NULL,
  `usuarios_claveUsuario` VARCHAR(45) NOT NULL,
  `usuarios_nombreUsuario` VARCHAR(45) NOT NULL,
  `especialidad` VARCHAR(45) NULL,
  INDEX `fk_medicos_usuarios1_idx` (`usuarios_idUsuario` ASC, `usuarios_claveUsuario` ASC, `usuarios_nombreUsuario` ASC) VISIBLE,
  PRIMARY KEY (`usuarios_idUsuario`),
  CONSTRAINT `fk_medicos_usuarios1`
    FOREIGN KEY (`usuarios_idUsuario` , `usuarios_claveUsuario` , `usuarios_nombreUsuario`)
    REFERENCES `mydb`.`usuarios` (`idUsuario` , `claveUsuario` , `nombreUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`farmaceutas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`farmaceutas` (
  `usuarios_idUsuario` VARCHAR(45) NOT NULL,
  `usuarios_claveUsuario` VARCHAR(45) NOT NULL,
  `usuarios_nombreUsuario` VARCHAR(45) NOT NULL,
  INDEX `fk_farmaceutas_usuarios1_idx` (`usuarios_idUsuario` ASC, `usuarios_claveUsuario` ASC, `usuarios_nombreUsuario` ASC) VISIBLE,
  PRIMARY KEY (`usuarios_idUsuario`),
  CONSTRAINT `fk_farmaceutas_usuarios1`
    FOREIGN KEY (`usuarios_idUsuario` , `usuarios_claveUsuario` , `usuarios_nombreUsuario`)
    REFERENCES `mydb`.`usuarios` (`idUsuario` , `claveUsuario` , `nombreUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`pacientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`pacientes` (
  `idPaciente` VARCHAR(45) NOT NULL,
  `nombrePaciente` VARCHAR(45) NULL,
  `fechaNacimiento` DATE NULL,
  `telefono` VARCHAR(45) NULL,
  PRIMARY KEY (`idPaciente`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`recetas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`recetas` (
  `idRecetas` VARCHAR(45) NOT NULL,
  `indicaciones` VARCHAR(45) NULL,
  `cantidad` VARCHAR(45) NULL,
  `duracion` VARCHAR(45) NULL,
  `estado` VARCHAR(45) NULL,
  `fecha` DATE NULL,
  `medicamentos_idMedicamento` VARCHAR(45) NOT NULL,
  `pacientes_idPaciente` VARCHAR(45) NOT NULL,
  `usuarios_idusuarios` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRecetas`),
  INDEX `fk_recetas_medicamentos1_idx` (`medicamentos_idMedicamento` ASC) VISIBLE,
  INDEX `fk_recetas_pacientes1_idx` (`pacientes_idPaciente` ASC) VISIBLE,
  INDEX `fk_recetas_usuarios1_idx` (`usuarios_idusuarios` ASC) VISIBLE,
  CONSTRAINT `fk_recetas_medicamentos1`
    FOREIGN KEY (`medicamentos_idMedicamento`)
    REFERENCES `mydb`.`medicamentos` (`idMedicamento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_recetas_pacientes1`
    FOREIGN KEY (`pacientes_idPaciente`)
    REFERENCES `mydb`.`pacientes` (`idPaciente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_recetas_usuarios1`
    FOREIGN KEY (`usuarios_idusuarios`)
    REFERENCES `mydb`.`usuarios` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
