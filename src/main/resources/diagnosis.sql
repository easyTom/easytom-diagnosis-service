
DROP TABLE IF EXISTS `diagnosis`;
CREATE TABLE `diagnosis` (
  `uuid` binary(16) NOT NULL,
  `creation_datetime` datetime(6) DEFAULT NULL,
  `update_datetime` datetime(6) DEFAULT NULL,
  `deletion_datetime` datetime(6) DEFAULT NULL,
  `state` tinyint DEFAULT NULL,
  `message` varchar(128) DEFAULT NULL,
  `diagnosis_uuid` binary(16) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

SET FOREIGN_KEY_CHECKS = 1;
