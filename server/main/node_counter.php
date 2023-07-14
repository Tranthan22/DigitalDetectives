<?php
	//this program get data from database 
	//example: http://localhost/.iot/getdata.php?node_id=node_01

	include 'database.php'; // declare subprogram access to database

	if(!empty($_GET)){
		$user = $_GET['user'];
		$node_counter = array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM node_status WHERE user="' . $user . '"';
		$stmt = $pdo->prepare($sql);
		$stmt->execute();
		while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
			$node_counter['node' . sprintf('%02d', count($node_counter) + 1)] = $row['node_id'];
		}
		Database::disconnect();
		echo json_encode($node_counter);

	}
	else{
		echo("emty_get");
	}
?>