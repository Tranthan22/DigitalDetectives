<?php

	include '../../database.php'; // declare subprogram access to database

	if(!empty($_POST)){
		$user = $_POST['user'];
		$sensor_id = $_POST['sensor_id'];
		$motor_id = $_POST['motor_id'];

		$cnt = 0;

		//check garden mode
		if($sensor_id == 0){
			$mode = '2';
		}
		else if($motor_id == 0){
			$mode =  '1';
		}
		else{
			$mode = '3';
		}

		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$query = $pdo->prepare("SELECT * FROM garden WHERE user='". $user ."'");
	    $query->execute();
	    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
	        $cnt = $cnt + 1;
	    }
	    $cnt = $cnt + 1;

	    // add new garden to table 'garden'
		$sql = "INSERT INTO garden (user, garden_id, sensor_id, motor_id, mode) values(?, ?, ?, ?, ?)";
	    $q = $pdo->prepare($sql);
	    $q->execute(array($user,$cnt,$sensor_id,$motor_id,$mode));

	    // remake parameter 'link' in table 'device_list'
	    $sql = "UPDATE device_list SET link = ? WHERE device = ? and user = ?";
	    $q = $pdo->prepare($sql);
	    $q->execute(array($cnt,$motor_id,$user));

	    $sql = "UPDATE device_list SET link = ? WHERE device = ? and user = ?";
	    $q = $pdo->prepare($sql);
	    $q->execute(array($cnt,$sensor_id,$user));

		Database::disconnect();

        echo ('Add garden okk');
	}
	else{
		echo("emty_post");
	}
?>