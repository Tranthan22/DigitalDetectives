<?php
	//this program get data from database 
	//example: http://localhost/.iot/getdata.php?node_id=node_01

	include 'database.php'; // declare subprogram access to database

	if(!empty($_POST)){
		$user = $_POST['user'];
		$node_id = $_POST['node_id'];
		$node = (object)array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM node_status WHERE user="' . $user . '" AND node_id="' . $node_id . '" ';
		foreach ($pdo->query($sql) as $row) {
			$node->bumpTime = $row['bumpTime'];

			echo (json_encode($node));
		}
		Database::disconnect();
	}
	else{
		echo("emty_get");
	}
?>