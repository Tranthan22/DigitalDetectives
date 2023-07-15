<?php

	include 'database.php'; // declare subprogram access to database

	if(!empty($_GET)){
		$user = $_GET['user'];
		$node_id = $_GET['node_id'];
		$node = (object)array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM node_status WHERE user="' . $user . '" AND node_id="' . $node_id . '" ';
		foreach ($pdo->query($sql) as $row) {
			$node->user = $row['user'];
      		$node->node_id = $row['node_id'];
			$node->node_auto = $row['node_auto'];
			$node->node_air = $row['node_air'];
			$node->node_soil = $row['node_soil'];
			$node->node_PH = $row['node_PH'];
			$node->bump_1 = $row['bump_1'];
			$node->bump_2 = $row['bump_2'];
			$node->bump_3 = $row['bump_3'];
			$node->battery = $row['battery'];
			$node->airWarnning = $row['airWarnning'];
			$node->soilWarnning = $row['soilWarnning'];
			$node->batteryWarnning = $row['batteryWarnning'];
			$node->bumpTime = $row['bumpTime'];
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