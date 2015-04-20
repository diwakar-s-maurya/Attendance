<html>
<head>
<meta charset="utf-8">
<title>Attendance</title>
<link rel="stylesheet" href="stylesheets/normalize.css" type="text/css"/>
<link rel="stylesheet" href="stylesheets/app.css" type="text/css"/>
<script src="bower_components/modernizr/modernizr.js"></script>
<?php
session_start();
if(!isset($_SESSION['username']))
{
	header("Location: attendance.php");
	die();
}

if( ! isset($_POST['class_name']) || ! isset($_POST['subject_name']) || !isset($_POST['value']))
{
    header("Location: home.php");
    die();
}

require_once("loginSQLfile.php");
try{
$STH = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");
$STH->bindParam(":class", $_POST['class_name']);
$STH->execute();
//echo $STH->rowCount(); todo: make sure that atleast one student is here
$rollList = $STH->fetchAll(PDO::FETCH_COLUMN, 0);
}catch(Exception $e){
    echo $e->getMessage();
    die();
}
?>
<script type="text/javascript">
    var currentRollIndex = 0;
    var rollList = <?php echo json_encode($rollList); echo ';'; ?>
    var attRecord = new Array(rollList.length);
    
    var presentList = document.getElementById("presentList");
    var currentRoll = document.getElementById("currentRollNo");
    var absentList = document.getElementById("absentList");

    window.onload = function initialize(){
        presentList = document.getElementById("presentList");
        currentRoll = document.getElementById("currentRollNo");
        absentList = document.getElementById("absentList");
        
        currentRoll.value = rollList[currentRollIndex];
        currentRoll.innerHTML = rollList[currentRollIndex];
    };
    function markPresent(){
        var label = document.createElement('a');
        label.id = currentRoll.innerHTML;
        label.innerHTML = currentRoll.innerHTML;
        presentList.appendChild(label);
        
        attRecord[currentRollIndex] = 1;
        ++currentRollIndex;
        if(currentRollIndex === rollList.length)
        {
            btnPresent = document.getElementById("btnPresent");
            btnAbsent = document.getElementById("btnAbsent");
            btnPresent.removeAttribute("onclick");
            btnAbsent.removeAttribute("onclick");
            currentRoll.innerHTML = "Done";
            finish();
            return;
        }
        currentRoll.innerHTML = rollList[currentRollIndex];
    };
    
    function markAbsent(){
        var label = document.createElement('label');
        label.id = currentRoll.innerHTML;
        label.innerHTML = currentRoll.innerHTML;
        absentList.appendChild(label); //here is difference between present:absent
        
        attRecord[currentRollIndex] = 0;
        ++currentRollIndex;
        if(currentRollIndex === rollList.length)
        {
            btnPresent = document.getElementById("btnPresent");
            btnAbsent = document.getElementById("btnAbsent");
            btnPresent.removeAttribute("onclick");
            btnAbsent.removeAttribute("onclick");
            currentRoll.innerHTML = "Done";
            finish();
            return;
        }
        currentRoll.innerHTML = rollList[currentRollIndex];
    };
    function finish(){
        console.log("finish");
        btnPresent = document.getElementById("btnPresent")
        btnAbsent = document.getElementById("btnAbsent");
        btnPresent.setAttribute("style", "display:none");
        btnAbsent.setAttribute("style", "display:none");
        btnUpload.setAttribute("style", "display:block");
    }
    function confirmation(){
        if(confirm("Do you want to upload?") === true){
            document.getElementById("attRecord").value = JSON.stringify(attRecord);
            document.getElementById("rollList").value = JSON.stringify(rollList);
            return true;
        }
        else
            return false;
    }
</script>
</head>
<body>
<?php
	include("header.html");
?>
<center>
	<div><label>Class: <?php echo $_POST['class_name']; ?></label><label>Subject: <?php echo $_POST['subject_name']; ?></label>
	<label>No. of classes: <?php echo $_POST['value'];?></label>
	</div>
    <div style="display:flex; flex-direction:row; justify-content:center;">
        <fieldset>
        <legend>Present</legend>
        <div id="presentList" style="display:flex; justify-content:space-around; flex-wrap:wrap">
            
        </div>
        </fieldset>
        <fieldset>
        <legend>Current Roll No.</legend>
        <div style="display:flex; justify-content:center">
            <label style="font-size:100px" id="currentRollNo">1</label>
        </div>
        </fieldset>
        
        <fieldset>
        <legend>Absent</legend>
        <div id="absentList" style="display:flex; justify-content:space-around; flex-wrap:wrap">
            
        </div>
        </fieldset>
    </div>
    <button type="button" id="btnPresent" class="button success" onclick="markPresent()">Present</button>
    <button type="button" id="btnAbsent" class="button alert" onclick="markAbsent()">Absent</button>
    <form method="post" action="upload.php">
        <input type="hidden"name="class_name" value="<?php echo $_POST['class_name'];?>">
        <input type="hidden"name="subject_name" value="<?php echo $_POST['subject_name'];?>">
        <input type="hidden" name="value" value="<?php echo $_POST['value']?>">
        <input type="hidden" id="attRecord" name="attRecord">
        <button type="submit" id="btnUpload" class="button highlight" onclick="return confirmation()" style="display: none">Upload</button>
    </form>
</center>
<?php
	include("footer.html");
?>
</body>
</html>
