<?php

	include '../../database.php'; // declare subprogram access to database

	//if(!empty($_POST)){
        if ($_SERVER['REQUEST_METHOD'] == 'POST'){
                $user = $_POST['user_namen'];
		
		$node_counter = array();

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$sql = 'SELECT * FROM garden WHERE user="' . $user . '"';
		$stmt = $pdo->prepare($sql);
		$stmt->execute();
		while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
			$node_counter[] = $row['garden_id']; 
                        
		}
		Database::disconnect();
		$response = array('tien' => $node_counter);
                echo json_encode($response);
         
	}
// 	}
// 	else{
// 		echo("emty_post");
// 	}
?>