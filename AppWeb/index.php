<?php

$result = array();

# connect to the database
try {
	$host = "localhost";
	$dbname = "attendance";
	$user = "root";
	$pass = "root";
	$DBH = new PDO("mysql:host=$host;dbname=$dbname", $user, $pass);
	$DBH->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
	
	$STH = $DBH->prepare("INSERT INTO attendance.attendance (rollno, date, sub, type, value) values ( :rollno, :date, :sub, :type, :value)");
	$STH->bindParam(":date", $_POST['date']);
	$STH->bindParam(":sub", $_POST['subjectName']);
	$STH->bindParam(":value", $_POST['value']);
	
	$students = $_POST['students'];
	for ($i = 1; $i <= $students; $i++) {
		$STH->bindParam(":rollno", $i);
		$STH->bindParam("type", $_POST[$i]);
		
		$STH->execute();
	}
	
	$result['success'] = 1;
}
catch(PDOException $e) {
	echo "I'm sorry, Dave. I'm afraid I can't do that.";
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
	//echo json_encode($e->getMessage());
	$result['success'] = 0;
}

	echo json_encode($result);
?>
