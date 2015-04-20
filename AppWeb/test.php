<?php
$result = array();
try{
	require_once 'loginSQLfile.php';
	$_POST['username'] = "vinodkumar";
	$_POST['password'] = "vinodkumar";
	$STH = $DBH->prepare("SELECT count(*) FROM login WHERE username = :username AND password = :password");
	$STH->bindParam(":username", $_POST['username']);
	$STH->bindParam(":password", $_POST['password']);
	
	if($STH->execute())
	{
		file_put_contents('PDOErrors.txt', "first query success\n", FILE_APPEND);
		$result['success'] = 1;
		$STH2 = $DBH->prepare("SELECT class, subject FROM class_subject_teacher WHERE teacher_username = :username");
		$STH2->bindParam(":username", $_POST['username']);
		if($STH2->execute())
		{
			$rows = $STH2->fetchAll(PDO::FETCH_ASSOC);
			//file_put_contents('PDOErrors.txt', $rows, FILE_APPEND);
			echo json_encode($row);
			$result['success'] = 2;
			file_put_contents('PDOErrors.txt', "second query success\n", FILE_APPEND);
		}
	}
	else
	{
		file_put_contents('PDOErrors.txt', "second query failure\n", FILE_APPEND);
		$result['success'] = -1;
	}
}catch (Exception $e){
	file_put_contents('PDOErrors.txt', "failure complete", FILE_APPEND);
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
	$result['success'] = 0;
}

echo json_encode($result);
file_put_contents('PDOErrors.txt', $result, FILE_APPEND);

?>
