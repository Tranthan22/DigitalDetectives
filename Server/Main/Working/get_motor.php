<?php

	include '../../database.php'; // declare subprogram access to database

	if(!empty($_GET)){
		$user = $_GET['user'];
		$motor_id = $_GET['motor_id'];
		$node = (object)array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM garden WHERE user="' . $user . '" AND motor_id="' . $motor_id . '" ';
		foreach ($pdo->query($sql) as $row) {
			$motor->node_auto = $row['node_auto'];
			$motor->pump_1 = $row['pump_1'];
			$motor->pump_2 = $row['pump_2'];
			$motor->pump_3 = $row['pump_3'];
			$motor->pumpTime = $row['pumpTime'];
			$motor->dailyPump = $row['dailyPump'];
		}
		$sql = 'SELECT * FROM device_list WHERE user="' . $user . '" AND device="' . $motor_id . '" ';
        $row = $pdo->query($sql)->fetch();
        if ($row) {
            $motor->linked = $row['link'];
        }

		Database::disconnect();



		echo (json_encode($motor));
		
	}
	else{
		echo("emty_get");
	}
?>