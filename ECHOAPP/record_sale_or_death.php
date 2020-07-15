<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

$itemID, $amount, $sale_or_death, $salePrice = -1


if(isset($_POST['itemID']) && isset($_POST['amount']) && isset($_POST['sale_or_death']) && isset($_POST['salePrice'])){
	$itemID = $_POST['itemID'];
	$amount = $_POST['amount'];
	$sale_or_death = $_POST['sale_or_death'];
	$salePrice = $_POST['salePrice'];

	$op->recordSaleOrDeath($itemID, $amount, $sale_or_death, $salePrice);
	echo "SUCCESS";
}





?>