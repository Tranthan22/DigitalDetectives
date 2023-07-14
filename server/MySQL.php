CREATE TABLE `node_status` (
    `user` varchar(255) NOT NULL,
    `node_id` varchar(255) NOT NULL,
    `node_auto` varchar(255) NOT NULL,
    `node_temp` float(10,2) NOT NULL,
    `node_humidity` int(3) NOT NULL,
    `node_PH` int() NOT NULL,
    `bump_1` varchar(255) NOT NULL,
    `bump_2` varchar(255) NOT NULL,
    `battery` int(3) NOT NULL,
    `time` time NOT NULL,
    `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `node_status`(`user`,`node_id`,`node_auto`,`node_temp`, `node_humidity`, `node_PH`, `bump_1`, `bump_2`, `battery`, `time`, `date`) VALUES ('minhtien','node_01','true','20.10','50','7','OFF','OFF','100',NOW(),NOW())


CREATE TABLE `tien_record` (
    `node_id` varchar(255) NOT NULL,
    `node_auto` varchar(255) NOT NULL,
    `node_temp` float(10,2) NOT NULL,
    `node_humidity` int(3) NOT NULL,
    `node_PH` int(3) NOT NULL,
    `bump_1` varchar(255) NOT NULL,
    `bump_2` varchar(255) NOT NULL,
    `battery` int(3) NOT NULL,
    `time` time NOT NULL,
    `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;