<?php
$data = array();
$success;

class subRollData{
	public $subject = "";
	public $rolls = array();
}

try{

	require_once 'loginSQLfile.php';
	require_once 'utilities.php';
	
	$_POST['username'] = 'diwakar';
	$_POST['password'] = 'diwakar';

	$username = $_POST['username'];
	$password = $_POST['password'];
	$STH = $DBH->prepare("SELECT * FROM login WHERE username = :username");
	
	$STH->bindParam(":username", $_POST['username']);
	if($STH->execute() && $STH->rowCount()){
		$result = $STH->fetch(PDO::FETCH_ASSOC);
		$temp = encryptPassword($password, $result['password_salt']);

		if($result['password_hash'] == $temp){ //logged in
			$STH2 = $DBH->prepare("SELECT class, subject FROM class_subject_teacher WHERE teacher = :username");
			$STH2->bindParam(":username", $_POST['username']);

			$STH2->execute();
			$STH3 = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");

			$class_subject = array();
			while($row = $STH2->fetch(PDO::FETCH_ASSOC))
			{
				$STH3->bindParam(":class", $row['class']);
				$STH3->execute();
				$rolls = array();
				while($STH3_row = $STH3->fetch(PDO::FETCH_ASSOC)){
                    array_push($rolls, $STH3_row['student']);}
				//array_push($class_subject, array_merge($row, array("roll" =>$current_class_rolls)));
			//		{"2k12co":[{"subject":"CD","roll":["2k12co01","2k12co02"]},{"subject":"DS","roll":["2k12co01","2k12co02"]}],"2k12ece":[{"subject":"DSD","roll":["2k12ece01","2k12ece02"]}],"0":{"success":1}}
				if( ! isset($class_subject[$row['class']])){
                    $class_subject[$row['class']] = array();}
				$d = new subRollData();
				$d->subject = $row['subject'];
				$d->rolls = $rolls;
				array_push($class_subject[$row['class']], $d);
				//echo json_encode(array($row['class'] => $row2));
				//echo json_encode(array('class_subject'=>$row));
			}
			$data = $class_subject;
			$success = 1;
		}
		else{//password did not match
			$success = 2;
		}
	}
	else{//username not found
		$success = 2;
	}
}catch (Exception $e){
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
	$success = -1;
}

$data["success"] = $success;
echo json_encode($data);

?>
