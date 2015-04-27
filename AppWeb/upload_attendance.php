<?php
	require_once 'loginSQLfile.php';
	
	//$_POST['class_name'] = "2k12co";
    //$_POST['subject_name'] = "aca";
    //$_POST['value'] = 1;
    //$_POST['attRecord'] = "011";
try{
	$STH = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");
	$STH->bindParam(":class", $_POST['class_name']);
	
	$STH2 = $DBH->prepare("INSERT INTO attendance.attendance (rollno, subject, type, value) VALUES (:student, :subject, :type, :value);");
	$STH2->bindParam(":subject", $_POST['subject_name']);
	$STH2->bindParam(":value", $_POST['value']);
	
	$success = 0;
	if($STH->execute())
	{
		$ptr = 0;
		while($row = $STH->fetch(PDO::FETCH_ASSOC))
		{
			$STH2->bindParam(":student", $row['student']);
            $type = $_POST['attRecord'][$ptr];
			$STH2->bindParam(":type", $type);
			if( ! $STH2->execute())
				break;
			++$ptr;
		}
		$success = 1;
	}
}catch (Exception $e){
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
}
	
	$data = array();
	$data['success'] = $success;
	echo json_encode($data);
?>
