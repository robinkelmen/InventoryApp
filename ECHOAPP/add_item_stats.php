	<?php

	require_once dirname(__FILE__) . '/db_run_op.php';

	$op = new DbOperation();


	if(isset($_POST["op"])){

	$ops = $_POST["op"];

	switch($ops){

		case "addNewTags":

			if(isset($_POST["tag"])){
				$newTags = $_POST["tag"];

				$op->addTags($newTags);
				
				$json["SUCCESS"] = "Tag Added to Database";
				echo json_encode($json);


			}
			else{
				$json["FAILED"] = "Tags not set";
				echo json_encode($json);
		
			}
			break;

		case "addPlantTag":
			if(isset($_POST["itemID"]) && isset($_POST["TagID"])){
		
					$tagID = intval($_POST["TagID"]);
					$itemID = intval($_POST["itemID"]);

					$op->addItemTag($itemID,$tagID);

					$return["SUCCESS"] = "Tag Added to Item";
					echo json_encode( $return);


				}else{
					$return["FAILED"] = "Missing Parameters";
					echo json_encode( $return);

				}

			break;


	}
}else{
		$return["FAILED"] = "Operation not set";
		echo json_encode( $return);
	}












	?>