<?php

	include '../../database.php';// declare subprogram access to database

	if(!empty($_POST)){
		$user = $_POST['user_nameg'];
		$node = (object)array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM device_list WHERE user="' . $user . '" AND type=1 AND link=0';
		foreach ($pdo->query($sql) as $row) {
			$sensor_id[] = $row['device'];
		}

		$sql = 'SELECT * FROM device_list WHERE user="' . $user . '" AND type=2 AND link=0';
		foreach ($pdo->query($sql) as $row) {
			$motor_id[] = $row['device'];
		}

		Database::disconnect();
		$response = array('sensor' => $sensor_id, 'motor' => $motor_id);
        echo json_encode($response);
	}
	else{
		echo("emty_get");
	}
?>