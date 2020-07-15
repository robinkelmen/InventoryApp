	<?php
	 error_reporting(E_ALL);
ini_set('display_errors', 1);
	class DbOperation
	{
	    //Database connection link
	    private $con;
	 
	    //Class constructor
	    function __construct()
	    {
	        //Getting the DbConnect.php file
	        require_once dirname(__FILE__) . '/connect_db.php';
	 
	        //Creating a DbConnect object to connect to the database
	        $db = new Connect_db();
	 
	        //Initializing our connection link of this class
	        //by calling the method connect of DbConnect class
	        $this->con = $db->connect();
	    }
		/*
		*
		* adds user
		*
		*/
		function addUser($userID, $email, $fName, $lName){
			if($stmt = $this->con->prepare("INSERT INTO USERS (userID, email, fName, lName) VALUES (?,?,?,?) ")){
			$stmt->bind_param("ssss", $userID, $email, $fName, $lName);
			if($stmt->execute())
				return true;
			return false;
		}else {
    	$error =  $this->con->error. ' ' . $this->con->errno ;
    	echo $error; // 1054 Unknown column 'foo' in 'field list'
		}
		}
		/*
		* The create operation
		* When this method is called a new record is created in the database
		*/
		function addItem($itemName, $price, $lowNum){
			if($stmt = $this->con->prepare("INSERT INTO PLANT (itemName, Price, lowNum) VALUES (?, ?, ?)")){
			$stmt->bind_param("sds", $itemName, $price, $lowNum );

			$itemreturnVal = $stmt->execute();

			$itemID = $this->con->insert_id;
			$amount = 0;

			if($availTrack = $this->con->prepare("INSERT INTO ITEMS_AVAILABLE (itemID, Amount) VALUES (?, ?)" )){
			$availTrack->bind_param("ii", $itemID, $amount);
			$amountreturnVal = $availTrack->execute();

			if($itemreturnVal && $amountreturnVal)
				return $itemID;
			return false;
		}
		}else {
    	$error =   $this->con->error. ' ' . $this->con->errno;
    	echo $error; // 1054 Unknown column 'foo' in 'field list'
		}
			 
		}

		/*
		* add new tags to all available tags
		*
		*/
		function addTags($string){
			
			
				if($stmt = $this->con->prepare("INSERT INTO TAGS (TagName) VALUES ( ?)")){
				$stmt->bind_param("s", $string);
			

			if($stmt->execute())
				return true; 
			return false; 
		}else {
    	$error =   $this->con->error. ' ' . $this->con->errno;
    	echo $error; // 1054 Unknown column 'foo' in 'field list'
		}
		}
		/*
		*
		* add a tag to the list of tags for a particular plant
		*
		*/
		function addItemTag($itemID, $tagID){
			if($stmt = $this->con->prepare("INSERT INTO plant_tags (itemID, TagID) VALUES (?,?)")){
				$stmt->bind_param("ii", $itemID, $tagID);

			if($stmt->execute())
				return true; 
			echo $this->con->error;
			return false; 
		} else {
    		echo "failed";
		}


		}
		function addOtherNames($string, $Language, $itemID){

			if($stmt = $this->con->prepare("INSERT INTO PLANT_OtherNames (itemID, Language, OtherNames) VALUES (?,?,?)")){
				$stmt->bind_param("iss", $itemID, $Language, $string);
			
			

			if($stmt->execute())
				return true; 
			return false; 
		}else {
    	$error =   $this->con->error. ' ' . $this->con->errno;
    	echo $error; // 1054 Unknown column 'foo' in 'field list'
		}

		}
	
		function updateAvailableItems($itemID, $amount){
			if($stmt = $this->con->prepare("UPDATE ITEMS_AVAILABLE SET Amount = ? WHERE itemID = ?")){
			$stmt->bind_param("ii", $amount, $itemID);

			if($stmt->execute())
				return true;
			return false;
		}else {
    	$error =   $this->con->error. ' ' . $this->con->errno;
    	echo $error; // 1054 Unknown column 'foo' in 'field list'
		}

		}

		/*
		* increase the number of plants in a cohort
		*
		*
		*/
		function increaseItemAmount($itemID, $amount){

			if($stmt = $this->con->prepare("UPDATE ITEMS_AVAILABLE SET Amount = ? WHERE itemID = ?")){
			$newTotal = ($this->getAmountItems($itemID) + $amount);
			$stmt->bind_param("ii", $newTotal, $itemID);
			
			if($stmt->execute())
				return true;
			return false;
		}else {
    	$error =   $this->con->error. ' ' . $this->con->errno;
    	echo $error; // 1054 Unknown column 'foo' in 'field list'
		}
		}
			/*
		* increase the number of plants in a cohort
		*
		*
		*/
		function decreaseItemAmount($itemID, $amount){
			if($this->checkItemID($itemID) && $this->checkAmountAvailable($itemID, $amount)){

			$stmt = $this->con->prepare("UPDATE ITEMS_AVAILABLE SET Amount = ? WHERE itemID = ?");
			$newTotal = ($this->getAmountItems($itemID) - $amount);
			$stmt->bind_param("ii", $newTotal, $itemID);
			
			if($stmt->execute())
				return true;
			return false;
		}else{
			$error =   $this->con->error. ' ' . $this->con->errno;
			echo $error;
		}

		}
		/*
		*
		* get the number of items
		*
		*/
		function getAmountItems($itemID){
			if($stmt = $this->con->prepare("SELECT Amount FROM ITEMS_AVAILABLE WHERE itemID = ?")){
			$stmt->bind_param("i", $itemID);
			

			if($stmt->execute()){
			$stmt->bind_result($amount);
			$stmt->fetch();
			return $amount;
		}
	}
}
	
	

		/*
		* The read operation
		* When this method is called it is returning all the existing record of the database
		*/
		function getPlants(){
			if($stmt = $this->con->prepare("SELECT itemID, Price, itemName, lowNum FROM PLANT")){
			$stmt->execute();
			$stmt->bind_result($id, $price, $name, $lowNum);
			
			$items = array(); 
			
			while($stmt->fetch()){
				$item = array();
				$item['id'] = $id; 
				$item['name'] = $name; 
				$item['price'] = $price; 
				$item['lowNum'] = $lowNum;
				
				array_push($items, $item); 
			}
			$count = 0;

			while($count < sizeof($items)){

				$id = $items[$count]['id'];
				$items[$count]['tags'] = $this->getPlantTags($id);
				$items[$count]['othernames'] = $this->getOtherNames($id);
				$items[$count]['AmountAvailable'] = $this->getAmountItems($item['id']);
				$count++;

			}
			

			
			return $items; 
		}
	}
		/*
		*
		* returns an array of tags associated with a plant
		*
		*
		*/
		function getPlantTags($id){
				if($tagFetch = $this->con->prepare("SELECT TagID FROM plant_tags WHERE itemID = ?")){
				$tagFetch->bind_param("i", $id);
				$tagFetch->execute();
				$tagFetch->bind_result($tagID);

				$plantTags = array();
				$tagIDs = array();
				$tagId = 0;

				while($tagFetch->fetch()){
					array_push($tagIDs, $tagID);
					

					
				}
			
				$count = 0;
				while($count < sizeof($tagIDs)){
					$toGet = $tagIDs[$count];
					$tagId = $toGet;
					if($tagnames = $this->con->prepare("SELECT TagName FROM TAGS WHERE TagID = ?")){
						$tagnames->bind_param("i", $toGet);
						$tagnames->execute();
						$tagnames->bind_result($tag);
						$tagnames->fetch();
						$tagnames->close();


						array_push($plantTags, $tag);
				}else{
					echo "GEtting Tagnames ". $tagId . $this->con->error . ' ' . $this->con->errno . "\n";
    				// 1054 Unknown column 'foo' in 'field list'
				}
				$count++;
				}

					return $plantTags;
				}else{
					echo "Getting TagIds " . $this->con->error . ' ' . $this->con->errno . "\n";
				}
			}
		/*
		*
		* Get a Plants other names
		*
		*/
		function getOtherNames($id){
			
			if($nameFetch = $this->con->prepare("SELECT Language, OtherNames FROM Plant_OtherNames WHERE itemID = ?")){
				$nameFetch->bind_param("i", $id);
				$nameFetch->execute();
				$nameFetch->bind_result($Language, $otherN);
				$otherNames = array();
				while($nameFetch->fetch()){
					$nameAndLang = array();
					$nameAndLang['language'] = $Language;
					$nameAndLang['otherN'] = $otherN;
					array_push($otherNames, $nameAndLang);
				}
				
				return $otherNames;
			}
		}
		/*
		*
		*
		*
		*/
		function getPlantPrice($itemID){
			if($this->checkItemID($itemID)){

			$stmt = $this->con->prepare("SELECT Price FROM PLANT WHERE itemID = ?");
			$stmt->bind_param("i", $itemID);
			$stmt->bind_result($price);

			if($stmt->execute())
				if($stmt->fetch())
					return $price;
			}
			return -1;
		}
		/*
		*
		*
		*/
		function getAllTags(){
			if($stmt = $this->con->prepare("SELECT * FROM TAGS")){
			$stmt->bind_result($TagID, $TagName);
			$tags = array();
			if($stmt->execute())
				while ($stmt->fetch()) {
					$tag = array();
					$tag['TagID'] = $TagID;
					$tag['TagName'] = $TagName;
					array_push($tags, $tag);
				}
			return $tags;
		}else {
    	$error =   $this->con->error. ' ' . $this->con->errno;
    	echo $error; 
		}
			
		}
		/*
		*
		* record Sale and Death
		*
		*/
		function recordSaleOrDeath($itemID, $amount, $sale_or_death, $salePrice = -1){
			if($this->checkAmountAvailable($itemID, $amount)){
			$date = date("Y-m-d");
			$currentItemAmount = $this->getAmountItems($itemID);
			if($salePrice ==-1 && $sale_or_death != "DEATH")
				$salePrice = $this->getPlantPrice($itemID);

			$stmt = $this->con->prepare("INSERT INTO NEWCHANGE  (itemID, userID, DateOfChange, NumChange, Sale_or_death, currentItemAmount, salePrice)
				VALUES (?,?,?,?,?,?,?)");
			$stmt->bind_param("iisisid", $itemID, $userID, $date, $amount, $sale_or_death, $currentItemAmount, $salePrice);
			if($stmt->execute())
				$this->$decreaseItemAmount($itemID, $amount);

		}
		/*
		* The update operation
		* When this method is called the record with the given id is updated with the new given values
		*/
		function updateItem($ido, $newName = "", $newPrice = ""){
			$orig = $this->con->prepare("SELECT * FROM PLANT WHERE itemID = ? ");
			$orig->bind_param("i", $ido);
			$orig->execute();
			$orig->bind_result($id, $name, $price);

			if($newName == ""){
	
			}else{
				$name = $newName;
			}

			if($newPrice == ""){

			}else{
				$price = $newPrice;
			}

			$stmt = $this->con->prepare("UPDATE PLANT SET  Price= ?  itemName = ? WHERE itemID = ?");
			

			$stmt->bind_param("sdi",  $price,$name, $id);
			if($stmt->execute())
				return true; 
			return false; 
		}
		/*
		* updates a plants other name if a mistake was made (e.g spelling error)
		*
		*/
		function updateOtherNames($id, $origName, $newName){
			$stmt = $this->con->prepare("UPDATE Plant_OtherNames SET OtherNames = ? WHERE itemID = ? and OtherNames = ?");
			$stmt->bind_param("sis", $newName, $id, $origName);
			if($stmt->execute())
				return true; 
			return false; 

		}
		/*
		* deletes other name for a plant if it was added incorrectly
		*
		*/
		function deleteOtherNames($id, $origName){
			$stmt = $this->con->prepare("DELETE  FROM Plant_OtherNames  WHERE itemID = ? and OtherNames = ?");
			$stmt->bind_param("is",  $id, $origName);
			if($stmt->execute())
				return true; 
			return false; 

		}
		/*
		* if a tag is incorrectly assigned to plant, it can be removed by this fucntion
		*
		*/
		function deleteTags($itemID, $tagID){
			$stmt = $this->con->prepare("DELETE  FROM Plant_OtherNames  WHERE itemID = ? and TagID = ?");
			$stmt->bind_param("is",  $plantID, $tagID);
			if($stmt->execute())
				return true; 
			return false; 

		}

		/*
		* makes sure itemID exists
		*
		*/
		function checkItemID($itemID){
			$stmt = $this->con->prepare("SELECT itemID FROM PLANT WHERE itemID = ?");
			$stmt->bind_param("i", $itemID);
			//$stmt->bind_result($itemidN);
			$stmt->execute();

			return $stmt->fetch();


		}
		function checkAmountAvailable($itemID, $amount){
			if($this->checkItemID($itemID)){
				$stmt = $this->con->prepare("SELECT Amount FROM ITEMS_AVAILABLE WHERE itemID = ?");
				$stmt->bind_param("i", $itemID);
				if($stmt->execute()){
					$stmt->bind_result($amountCurrent);
					$stmt->fetch();
					if($amountCurrent >= $amount)
						return true;

			}
				}
			
				return false;
		}
			/*
		* makes sure tagID exists
		*
		*/
		function checkTagID($tagID){
			$stmt = $this->con->prepare("SELECT TagID FROM TAGS WHERE TagID = ?");
			$stmt->bind_param("i", $tagID);
			//$stmt->bind_result($itemidN);
			$stmt->execute();

			return $stmt->fetch();

		}
			/*
		* makes sure cohortID exists
		*
		*/
		function checkCohortID($cohortID){
			$stmt = $this->con->prepare("SELECT cohortID FROM COHORT WHERE cohortID = ?");
			$stmt->bind_param("i", $cohortID);
			//$stmt->bind_result($itemidN);
			$stmt->execute();

			return $stmt->fetch();
		

		}
		function checkCohortAmount($cohortID, $amount){
			$stmt = $this->con->prepare("SELECT numInit, numDeceased, numSold FROM COHORT WHERE cohortID = ?");
			$stmt->bind_param("i", $cohortID);
			$stmt->bind_result($numInit, $numDeceased, $numSold);

			if($stmt->execute())
				if($stmt->fetch()){
					$depleted = $numDeceased + $numSold;
					$available = $numInit - $depleted;

					if($amount > $available)
						return false;
					else
						return true;
				}
				return false;
		}
	
		function deactivateCohort($cohortID){
			$stmt = $this->con->prepare("UPDATE COHORT SET Active = ? WHERE cohortID = ?");
			$inactive = 0;
			$stmt->bind_param("ii", $inactive, $cohortID);

			if($stmt->execute())
				return true;
			return false;
		}
		function verifyEmail($email){
			$stmt = $this->con->prepare("SELECT email FROM USERS WHERE email = ?");
			$stmt->bind_param("s", $email);
			$stmt->execute();

			return $stmt->fetch();
		
		}
			/*
		*
		* Add new Cohort of plant 
		*
		*/
		function addCohort($itemID, $method, $numInit, $date = "", $numSold = 0, $numDeceased = 0, $active = 1, $price = -1){
			
			if($date =="")
				$date = date("Y-m-d");
			if($price == -1)
				$price = $this->getPlantPrice($itemID);
			$stmt = $this->con->prepare("INSERT INTO COHORT (itemID, DatePlanted, method, numInit, numDeceased, numSold, cohortPrice, Active) VALUES (?,?,?,?,?,?, ?)");
			$stmt->bind_param("issiiidi", $itemID, $date, $method,$numInit,$numDeceased, $numSold, $price, $active);

			if($stmt->execute() && $this->increaseItemAmount($itemID,$numInit))
				return true;
			return false;
		}
		/*
		*
		* records a new sale
		*
		*/
		function recordSale($userID, $cohortID, $amountSold, $salePrice = -1){
			if($this->checkAmountAvailable($itemID, $amountSold) && $this->checkCohortAmount($cohortID, $amountSold)){
			$itemID = $this->getitemIDFromCohort($cohortID);
			$date = date("Y-m-d");
			$currentItemAmount = $this->getAmountItems($itemID);
			$currentCohortAmount = $this->getNumberItemsInCohort($cohortID);

			if($salePrice ==-1)
				$salePrice = $this->getPlantPrice($itemID);
			$stmt = $this->con->prepare("INSERT INTO NEWCHANGE (userID, cohortID, DateOfChange, NumChange, Sale_or_Death, currentCohortAmount, currentItemAmount, salePrice) VALUES (?,?,?,?,?, ?,?,?)" );
			$sale = "SALE";
			$stmt->bind_param("iisisiid", $userID, $cohortID,$date , $amountSold, $sale, $currentCohortAmount, $currentItemAmount, $salePrice);
			if($stmt->execute()){
				$this->decreaseItemAmount($this->getitemIDFromCohort($cohortID), $amountSold);
				$this->updateCohortStats($cohortID, $amountSold, $sale);
				return true;
			}

		}
		return false;


		}
		/*
		*
		* bassically record Sale but with death in mind, it is redundant but helps with function calls being easy
		*
		*/
		function recordDeath($userID, $cohortID, $amountDead, $salePrice = -1){
			if($this->checkAmountAvailable($itemID, $amountSold) && $this->checkCohortAmount($cohortID, $amountSold)){
			$itemID = $this->getitemIDFromCohort($cohortID);
			$date = date("Y-m-d");
			$currentItemAmount = $this->getAmountItems($itemID);
			$currentCohortAmount = $this->getNumberItemsInCohort($cohortID);
				$stmt = $this->con->prepare("INSERT INTO NEWCHANGE (userID, cohortID, DateOfChange, NumChange, Sale_or_Death, currentCohortAmount, currentItemAmount, salePrice) VALUES (?,?,?,?,?, ?,?,?)" );
			$death = "DEATH";
			$stmt->bind_param("iisisiid", $userID, $cohortID,$date , $amountSold, $death, $currentCohortAmount, $currentItemAmount, $salePrice);
			if($stmt->execute()){
				$this->decreaseItemAmount($this->getitemIDFromCohort($cohortID), $amountSold);
				$this->updateCohortStats($cohortID, $amountSold, $death);
				return true;
			}

		}
		return false;
		}
		/*
		* update the cohorts information
		*
		*/
		function updateCohortStats($cohortID, $amount, $death_or_sale){
			$stmt = $this->con->prepare("SELECT numInit, numDeceased, numSold FROM COHORT WHERE cohortID = ?");
			$updateC = $this->con->prepare("UPDATE COHORT SET numDeceased = ?, numSold = ? , Active = ? WHERE cohortID = ?");

			$stmt->bind_param("i", $cohortID);
			$stmt->bind_result($initial, $dead, $sold);

			if($stmt->execute())
				if($stmt->fetch()){
					$newSold = $sold;
					$newDead = $dead;
					$isActive = 1;
					if($death_or_sale == "SALE")
						$newSold += $amount;
						
					if($death_or_sale == "DEATH")
						$newDead +=$amount;
					if(($numInit - ($newDead + $newSold)) == 0)
							$isActive = 0;
					$updateC->bind_param("iiii", $newDead, $newSold, $isActive, $cohortID);

					if($updateC->execute())
						return true;

				}
				return false;


		}
			/*
		* get Cohorts 
		* 
		*/
		function getCohorts(){
			$stmt = $this->con->prepare("SELECT * FROM COHORT");
			$stmt->bind_result($cohortID, $itemID, $DatePlanted, $method, $numInit, $numDeceased, $numSold, $cohortPrice, $Active );
			$cohorts = array();
			if($stmt->execute())
				while($stmt->fetch()){
					$cohort = array();
					$cohort['cohortID'] = $cohortID;
					$cohort['itemID'] = $itemID;
					$cohort['DatePlanted'] = $DatePlanted;
					$cohort['method'] = $method;
					$cohort['numInit'] = $numInit;
					$cohort['numDeceased'] = $numDeceased;
					$cohort['numSold'] = $cohortID;
					$cohort['cohortPrice'] = $cohortID;
					$cohort['$Active'] = $Active;

					array_push($cohorts, $cohort);
				}
			return $cohorts;


		}
			function getitemIDFromCohort($cohortID){
			$itemIdRec = $this->con->prepare("SELECT itemID FROM COHORT WHERE cohortID = ?");
			$itemIDRec->bind_param("id", $cohortID);
			$itemIDRec->bind_result($itemID);
			if($itemIDRec->execute()){
				if($itemIDRec->fetch())
					return $itemID;
			}
			return -1;

		}
			/*
		*
		* get number of items left in cohort
		*
		*/
		function getNumberItemsInCohort($cohortID){
			if($this->checkCohortID($cohortID)){}

			$stmt = $this->con->prepare("SELECT numInit, numDeceased, numSold FROM COHORT WHERE cohortID = ?");
			$stmt->bind_param("i", $cohortID);
			$stmt->bind_result($numInit, $numDeceased, $numSold);

			if($stmt->execute())
				if($stmt->fetch())
					return $numInit - ($numSold + $numDeceased);

			return false;

			}
		}

	}