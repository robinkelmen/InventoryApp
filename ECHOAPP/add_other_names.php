<?php


require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

if(isset($_POST["Name"]) && isset($_POST["Language"]) && isset($_POST["itemID"])){
		
		$language = $_POST["Language"];
		$name = $_POST["Name"];
		$itemID = intval($_POST["itemID"]);

		$op->addOtherNames($name, $language, $itemID);

		
		$toReturn["SUCCESS"] = "Other Name Added";
		echo json_encode($toReturn);
	
}else{

$toReturn["FAILED"] = "Other Name Not Added";
		echo json_encode($toReturn);

}




?>