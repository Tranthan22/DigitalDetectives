<?php

	include 'database.php'; // declare subprogram access to database

	if(!empty($_POST)){
		$user = $_POST['user'];
		$pdo = Database::connect();
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$sql = "UPDATE Users SET  deviceToken = ? WHERE user_name = ?";
      	$q = $pdo->prepare($sql);
      	$q->execute(array("", $user));
		Database::disconnect();
	}
	else{
		echo("emty_post");
	}
?>