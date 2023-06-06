<?php
	//this program get data from database 
	//example: http://localhost/.iot/getdata.php?node_id=node_01

	include 'database.php'; // declare subprogram access to database

	if(!empty($_GET)){

		$node_id = $_GET['node_id'];
		$node = (object)array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM node_status WHERE node_id="' . $node_id . '"';
		foreach ($pdo->query($sql) as $row) {
      		$node->node_id = $row['node_id'];
			$node->node_auto = $row['node_auto'];
			$node->node_temp = $row['node_temp'];
			$node->node_humidity = $row['node_humidity'];
			$node->node_PH = $row['node_PH'];
			$node->bump_1 = $row['bump_1'];
			$node->bump_2 = $row['bump_2'];	

			$node->date = $row['date'];
      		$node->time = $row['time'];

			echo (json_encode($node));
		}
		Database::disconnect();
	}
	else{
		echo("emty_get");
	}
?>