<?php
	//this program get data from database 
	//example: http://localhost/.iot/getdata.php?node_id=node_01

	include 'database.php'; // declare subprogram access to database

	if ($_SERVER['REQUEST_METHOD'] == 'POST'){
                $user = $_POST['user_namen'];
		
		$node_counter = array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM node_status WHERE user="' . $user . '"';
		$stmt = $pdo->prepare($sql);
		$stmt->execute();
		while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
			$node_counter[] = $row['node_id']; 
                        
		}
		Database::disconnect();
		$response = array('tien' => $node_counter);
                echo json_encode($response);
         
	}
?>