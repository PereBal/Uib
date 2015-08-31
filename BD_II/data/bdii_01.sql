-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-12-2014 a las 17:46:35
-- Versión del servidor: 5.6.21
-- Versión de PHP: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `bdii_01`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `anunci`
--
CREATE DATABASE BDII_01;
use BDII_01;
CREATE TABLE IF NOT EXISTS `anunci` (
`Id` int(11) NOT NULL,
  `Titol_curt` tinytext NOT NULL,
  `Text` text,
  `Data_publicacio` date NOT NULL,
  `Data_web` date DEFAULT NULL,
  `Data_no_web` date DEFAULT NULL,
  `Telefon` varchar(16) NOT NULL,
  `Imatge` varchar(64) DEFAULT NULL,
  `Num_canvis` int(11) DEFAULT '0',
  `Preu` decimal(6,2) DEFAULT '0.00',
  `Pagat` bit(1) DEFAULT b'0',
  `Usuari` varchar(32) DEFAULT NULL,
  `Seccio` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `anunci`
--

INSERT INTO `anunci` (`Id`, `Titol_curt`, `Text`, `Data_publicacio`, `Data_web`, `Data_no_web`, `Telefon`, `Imatge`, `Num_canvis`, `Preu`, `Pagat`, `Usuari`, `Seccio`) VALUES
(1, 'Penguins', ' Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis nulla mi, convallis ut blandit eget, maximus et sapien. Praesent at luctus mi. Pellentesque dignissim hendrerit nibh, id faucibus enim vehicula quis. Suspendisse tempus interdum arcu vel ultricies. Nunc eget tellus urna. Praesent lobortis sodales lorem vel imperdiet. Mauris lacinia venenatis mollis. Nulla quis dignissim lacus, et finibus dui. Quisque eros lectus, scelerisque ac sodales sed, egestas sagittis lorem. Phasellus augue urna, auctor a sollicitudin id, sodales id nisl. Mauris sollicitudin ac mauris non molestie.\r\n\r\nIn nec tempor sapien. In hac habitasse platea dictumst. Proin ullamcorper purus mollis hendrerit tempus. Aenean auctor pulvinar elit, non mattis risus vulputate et. Vestibulum lacinia lacus sed dictum commodo. Phasellus nec ornare mauris, in scelerisque metus. Aenean blandit ex velit. Vivamus tincidunt in quam vitae imperdiet. Fusce fermentum lorem nec pharetra auctor. Fusce eleifend quam vel odio consectetur vestibulum feugiat ac nunc. ', '2014-12-15', '2014-12-15', '2014-12-30', '123123123', 'Penguins.jpg', 1, '1.10', b'0', 'Joan', 1),
(2, 'Tulipa', ' Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis nulla mi, convallis ut blandit eget, maximus et sapien. Praesent at luctus mi. Pellentesque dignissim hendrerit nibh, id faucibus enim vehicula quis. Suspendisse tempus interdum arcu vel ultricies. Nunc eget tellus urna. Praesent lobortis sodales lorem vel imperdiet. Mauris lacinia venenatis mollis. Nulla quis dignissim lacus, et finibus dui. Quisque eros lectus, scelerisque ac sodales sed, egestas sagittis lorem. Phasellus augue urna, auctor a sollicitudin id, sodales id nisl. Mauris sollicitudin ac mauris non molestie.\r\n\r\nIn nec tempor sapien. In hac habitasse platea dictumst. Proin ullamcorper purus mollis hendrerit tempus. Aenean auctor pulvinar elit, non mattis risus vulputate et. Vestibulum lacinia lacus sed dictum commodo. Phasellus nec ornare mauris, in scelerisque metus. Aenean blandit ex velit. Vivamus tincidunt in quam vitae imperdiet. Fusce fermentum lorem nec pharetra auctor. Fusce eleifend quam vel odio consectetur vestibulum feugiat ac nunc. ', '2014-12-15', '2014-12-18', '2014-12-31', '123123123', 'Tulips.jpg', 1, NULL, b'0', 'Joan', 2),
(3, 'Koala', ' Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis nulla mi, convallis ut blandit eget, maximus et sapien. Praesent at luctus mi. Pellentesque dignissim hendrerit nibh, id faucibus enim vehicula quis. Suspendisse tempus interdum arcu vel ultricies. Nunc eget tellus urna. Praesent lobortis sodales lorem vel imperdiet. Mauris lacinia venenatis mollis. Nulla quis dignissim lacus, et finibus dui. Quisque eros lectus, scelerisque ac sodales sed, egestas sagittis lorem. Phasellus augue urna, auctor a sollicitudin id, sodales id nisl. Mauris sollicitudin ac mauris non molestie.\r\n\r\nIn nec tempor sapien. In hac habitasse platea dictumst. Proin ullamcorper purus mollis hendrerit tempus. Aenean auctor pulvinar elit, non mattis risus vulputate et. Vestibulum lacinia lacus sed dictum commodo. Phasellus nec ornare mauris, in scelerisque metus. Aenean blandit ex velit. Vivamus tincidunt in quam vitae imperdiet. Fusce fermentum lorem nec pharetra auctor. Fusce eleifend quam vel odio consectetur vestibulum feugiat ac nunc. ', '2014-12-15', '2014-12-23', '2014-12-26', '123123123', 'Koala.jpg', 1, NULL, b'0', 'Joan', 1);

--
-- Disparadores `anunci`
--
DELIMITER //
CREATE TRIGGER `act_preu` BEFORE UPDATE ON `anunci`
 FOR EACH ROW BEGIN  
    IF NEW.Pagat = 1 AND OLD.Pagat = 0 THEN -- Acabam de pagar l'anunci
        SET NEW.Preu = 0;
        SET NEW.Num_canvis = 0;
    ELSE
        SET NEW.Num_canvis=(OLD.Num_canvis+1);
        IF OLD.Pagat = 1 THEN -- Acabam de reeditar un anunci pagat
            SET NEW.Pagat = 0;
            SET NEW.Preu = ((SELECT Seccio.Preu FROM Anunci INNER JOIN Seccio ON OLD.Seccio=Seccio.Codi_seccio WHERE NEW.Id=Anunci.Id)+ NEW.Num_canvis*0.1);
        ELSE
            IF NOT (NEW.Seccio = OLD.Seccio) THEN -- S'ha fet un canvi de seccio, recalcular el preu
SET NEW.Preu=((SELECT Seccio.Preu FROM Anunci INNER JOIN Seccio ON NEW.Seccio=Seccio.Codi_seccio WHERE NEW.Id=Anunci.Id) + NEW.Num_canvis*0.1);
            ELSE
                SET NEW.Preu=(OLD.Preu + 0.1);
            END IF;
        END IF;
    END IF;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seccio`
--

CREATE TABLE IF NOT EXISTS `seccio` (
`Codi_seccio` int(11) NOT NULL,
  `Titol` varchar(64) NOT NULL,
  `Descripcio` text,
  `Preu` decimal(6,2) DEFAULT '0.00',
  `Imatge` varchar(64) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `seccio`
--

INSERT INTO `seccio` (`Codi_seccio`, `Titol`, `Descripcio`, `Preu`, `Imatge`) VALUES
(1, 'Animal', 'Els pingüins australs són un grup d''espècies d''ocells no voladors que habiten l''hemisferi sud. Contràriament a la creença popular, els pingüins no habiten únicament en climes freds. Moltes espècies de pingüins habiten fins a les illes Galàpagos. La majoria dels pingüins s''alimenten de krill, peix, calamars i altres criatures marines que capturen en les seves immersions submarines.', '1.00', 'Penguins.jpg'),
(2, 'Flors', 'Anapenemicament, la flor Ã©s un eix de creixement limitat (braquiblast), que porta lateralment, i densament disposats, Ã²rgans que serveixen per a la reproducciÃ³. ', '3.00', 'Tulips.jpg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuari`
--

CREATE TABLE IF NOT EXISTS `usuari` (
  `UserID` varchar(32) NOT NULL,
  `Nom` varchar(64) DEFAULT NULL,
  `Password` char(160) NOT NULL,
  `Tipo` bit(1) NOT NULL DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuari`
--

INSERT INTO `usuari` (`UserID`, `Nom`, `Password`, `Tipo`) VALUES
('Admin', 'Admin', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', b'1'),
('Joan', 'Joan CatalÃ ', '7c222fb2927d828af22f592134e8932480637c0d', b'0');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `anunci`
--
ALTER TABLE `anunci`
 ADD PRIMARY KEY (`Id`), ADD KEY `Usuari` (`Usuari`), ADD KEY `Seccio` (`Seccio`);

--
-- Indices de la tabla `seccio`
--
ALTER TABLE `seccio`
 ADD PRIMARY KEY (`Codi_seccio`), ADD UNIQUE KEY `Titol` (`Titol`);

--
-- Indices de la tabla `usuari`
--
ALTER TABLE `usuari`
 ADD PRIMARY KEY (`UserID`), ADD UNIQUE KEY `UserID` (`UserID`);

--
-- Filtros para la tabla `anunci`
--
ALTER TABLE `anunci`
ADD CONSTRAINT `Anunci_ibfk_1` FOREIGN KEY (`Usuari`) REFERENCES `usuari` (`UserID`),
ADD CONSTRAINT `Anunci_ibfk_2` FOREIGN KEY (`Seccio`) REFERENCES `seccio` (`Codi_seccio`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
