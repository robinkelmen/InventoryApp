<? php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

$ops = 0;
$amount = 0;


if(isset($_POST['update'])){
	$ops = $_POST['operation'];
	$amount = $_POST['amount'];
	$itemID = $_POST['itemID'];


	switch ($ops) {
		case 1:
			$op->increaseItemAmount($itemID, $amount);
			break;
		case 2:
			$op->decreaseItemAmount($itemID, $amount);
			break;
		default:
			# code...
			break;
	}

}






?>