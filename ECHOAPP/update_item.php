<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

$itemID = -1;
$newName = "";
$newPrice = "";




if(isset($_POST['itemID'])){
	$itemID = intval($_POST['itemID']);


if(isset($_POST['newName']) ){
	$newName = $_POST['newName'];
	$op->updateItem($itemID, $newName, $newPrice);
}
if(isset($_POST['newPrice']) ){
	$newName = $_POST['newPrice'];
	$op->updateItem($itemID, $newName, $newPrice);
}

if(isset($_POST['removeNames'])){
	$toRemoveNames = $_POST['removeNames'];

	foreach ($toRemoveNames as $names) {
		$op->deleteOtherNames($itemID, $names);
	}

}}else{
	
}

if(isset($_POST['removeTags'])){
	$toRemoveTags = $_POST['removeTags'];
	foreach ($toRemoveTags as $tags) {
		$op->deleteTags($itemID, $tags);
	}

}
if(isset($_POST['addNames'])){
	$toAddNames = $_POST['addNames'];
	foreach ($toAddNames as $name) {
		$newName = $name['name'];
		$newLang = $name['language'];
		$op->addOtherNames($newName, $newLang, $itemID);
	}
}
if(isset($_POST['toAddtagIDs'])){
	$toAddtagIDs = $_POST['toAddtagIDs'];
	foreach ($toAddtagIDs as $tagID) {
		$op->addItemTag($itemID, $tagID);
	}
}

?>