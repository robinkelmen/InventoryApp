<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();


$itemID = -1;
$toRemove = "";

if(isset($_POST['itemID']))
	$itemID = $_POST['itemID'];

if(isset($_POST['name'])){
	$toRemove = $_POST['name'];
	$op->deleteOtherNames($itemID, $toRemove);
	echo json_encode({"SUCCESS": "Name Removed"});
}









?>