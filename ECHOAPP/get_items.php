<?php

require_once dirname(__FILE__) . '/db_run_op.php';

$op = new DbOperation();




echo json_encode($op->getPlants());




?>