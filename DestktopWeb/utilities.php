<?php
function encryptPassword($password, $salt){
	return hash('sha512', join('', array($password, $salt)));
}

function getSalt(){
	return 	hash('md5', join('', array(time(), rand())));
}
?>