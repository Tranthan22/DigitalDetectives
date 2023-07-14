<?php
	//this program get data from database 
	//example: http://localhost/.iot/getdata.php?node_id=node_01

	include 'database.php'; // declare subprogram access to database

	if(!empty($_POST)){
		$user = $_POST['user'];
		$node = (object)array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM notification WHERE user="' . $user . '" ';
		foreach ($pdo->query($sql) as $row) {
			$node->content = $row['content'];
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