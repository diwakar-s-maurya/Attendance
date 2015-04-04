<?php
$data = array();
$success;
try{
	require_once 'loginSQLfile.php';

	//$_POST['username'] = "vinod";
	//$_POST['password'] = "vinod";
	$STH = $DBH->prepare("SELECT count(*) as count FROM login WHERE username = :username AND password = :password");
	$STH->bindParam(":username", $_POST['username']);
	$STH->bindParam(":password", $_POST['password']);
	$STH->execute(); 
	$login_success = $STH->fetch(PDO::FETCH_ASSOC);
	if($login_success['count'] == 1)
	{
		$STH2 = $DBH->prepare("SELECT class, subject FROM class_subject_teacher WHERE teacher = :username");
		$STH2->bindParam(":username", $_POST['username']);
			
		$STH2->execute();
		$STH3 = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");
		
		$class_subject = array();
		while($row = $STH2->fetch(PDO::FETCH_ASSOC))
		{
			$STH3->bindParam(":class", $row['class']);
			$STH3->execute();
			$current_class = array();
			while($STH3_row = $STH3->fetch(PDO::FETCH_ASSOC))
				array_push($current_class, $STH3_row['student']);

			array_push($class_subject, array_merge($row, array("roll" =>$current_class)));
			//echo json_encode(array($row['class'] => $row2));
			//echo json_encode(array('class_subject'=>$row));
		}
		$data = $class_subject;
		$success = 1;
	}
	else
	{
		$success = 2;
	}
}catch (Exception $e){
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
	$success = -1;
}

array_push($data, array("success" => $success));
echo json_encode($data);

?>
