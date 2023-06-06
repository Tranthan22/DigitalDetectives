
CREATE TABLE `node_status` (
    `node_id` varchar(255) NOT NULL,
    `node_auto` varchar(255) NOT NULL,
    `node_temp` float(10,2) NOT NULL,
    `node_humidity` int(3) NOT NULL,
    `node_PH` int() NOT NULL,
    `bump_1` varchar(255) NOT NULL,
    `bump_2` varchar(255) NOT NULL,
    `time` time NOT NULL,
    `date` date NOT NULL,
    PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `node_status`(`node_id`,`node_auto`,`node_temp`, `node_humidity`, `node_PH`, `bump_1`, `bump_2`, `time`, `date`) VALUES ('node_01','true','0.00','0','7','OFF','OFF',NOW(),NOW())


CREATE TABLE `node_record` (
    `node_id` varchar(255) NOT NULL,
    `node_auto` varchar(255) NOT NULL,
    `node_temp` float(10,2) NOT NULL,
    `node_humidity` int(3) NOT NULL,
    `node_PH` int() NOT NULL,
    `bump_1` varchar(255) NOT NULL,
    `bump_2` varchar(255) NOT NULL,
    `time` time NOT NULL,
    `date` date NOT NULL,
    PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;