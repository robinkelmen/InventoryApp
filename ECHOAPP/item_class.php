<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();

//echo $op->addItem("Moringa", 9.00);
//echo $op->addCohort(5, "cuttings", 40);
//echo $op->getAmountItems(5);

 echo json_encode($op->getPlantTags(116));






?>