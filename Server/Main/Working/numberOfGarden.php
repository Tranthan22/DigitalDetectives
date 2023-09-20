<?php

	include '../../database.php'; // declare subprogram access to database

	if(!empty($_GET)){
		$user = $_GET['user'];
		$response = array();
		$mode = array();
		$cnt = 0;

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$query = $pdo->prepare("SELECT * FROM garden WHERE user='". $user ."'");
	    $query->execute();
	    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
	        $mode[] = $row['mode'];
	        $cnt = $cnt + 1;
	    }

		Database::disconnect();
		$response['quantity'] = $cnt;
		$response['mode'] = $mode;

        echo json_encode($response);
	}
	else{
		echo("emty_get");
	}
?>