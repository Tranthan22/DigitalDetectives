<?php
	include '../../database.php'; // declare subprogram access to the database

	if (!empty($_POST)) {
	    $user = $_POST['user_nameg'];
	    $node = (object) array();

	    $pdo = Database::connect();
	    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	    $sql = 'SELECT device FROM device_list WHERE user = :user AND type = "1" AND link <> "0"';
	    $stmt = $pdo->prepare($sql);
	    $stmt->bindParam(':user', $user, PDO::PARAM_STR);
	    $stmt->execute();
	    $sensor_id = $stmt->fetchAll(PDO::FETCH_COLUMN);

	    $sql = 'SELECT device FROM device_list WHERE user = :user AND type = "2" AND link <> "0"';
	    $stmt = $pdo->prepare($sql);
	    $stmt->bindParam(':user', $user, PDO::PARAM_STR);
	    $stmt->execute();
	    $motor_id = $stmt->fetchAll(PDO::FETCH_COLUMN);

	    Database::disconnect();

	    $response = array('sensor' => $sensor_id, 'motor' => $motor_id);
	    echo json_encode($response);
	} else {
	    echo("empty_post");
	}
?>
