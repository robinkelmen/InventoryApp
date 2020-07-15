<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

$itemName = "";
$itemPrice = 0;
$itemLowNum = 10;
$itemTagsIDs = array();
$itemOtherNames = array();

$itemID = -1;


if(isset($_POST["itemName"]) && isset($_POST["itemPrice"]) && isset($_POST["lowNum"])){
	$itemName = $_POST["itemName"];
	$itemPrice = $_POST["itemPrice"];
	$lowNum = $_POST["lowNum"];

	$itemPrice = doubleval($itemPrice);
	$lowNum = intval($lowNum);
	


	$itemID = $op->addItem($itemName, $itemPrice, $lowNum);
	$return["itemID"] = $itemID;
	echo json_encode($return);

}else{
	$toReturn["FAILED"] = "Item Not Added";
		echo json_encode($toReturn);
}



	







?>