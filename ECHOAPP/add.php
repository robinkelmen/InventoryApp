<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

$itemName = "";
$itemPrice = 0;
$itemTagsIDs = -1;
$itemOtherNames = -1;
$itemID = 0;

if(isset($_POST['itemName']) && isset($_POST['itemPrice'])){
	$itemName = $_POST['itemName'];
	$itemPrice = $_POST['itemPrice'];

	$op->addItem($itemName, $itemPrice);
}






?>