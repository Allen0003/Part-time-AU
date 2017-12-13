<!-- http://localhost/test/getCalendar.php -->


<?php
$account = "Allen";
$password = "apss1943";
$serverUrl = "http://localhost:8080/WebServiceToOfficeCalandar/app/calService";

require_once ('FileMaker.php');
$fm = new FileMaker('TASpoint_C', 'http://119.9.31.62', $account, $password);


// ********** get single data  **********

$findCommand = $fm->newFindAllCommand('ADM_Leave');
$result = $findCommand->execute();
$records = $result->getRecords();

foreach ($records as $record) {

	$fields = $record->_impl->_fields;

	$_User_ID_or_Group_ID = $fields['_User_ID_or_Group_ID'][0];
	$Reason = $fields['Reason'][0];
	$Type = $fields['Type'][0];
	$DateFrom = $fields['DateFrom'][0];
	$DateTo = $fields['DateTo'][0];
	$HoursTaken = $fields['DateTo'][0];
	$Comments = $fields['DateTo'][0];

	//prepare json object
	$data = array("dateFrom" => $DateFrom, "dateTo" => $DateTo , "type" => $Type , "userID" => $_User_ID_or_Group_ID);
	$data_string = json_encode($data);
	$ch = curl_init($serverUrl);
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_HTTPHEADER, array(
	    'Content-Type: application/json',
	    'Content-Length: ' . strlen($data_string))
	);
	//send json object to webSerivce
	// $result = curl_exec($ch);

}

echo "done";

// ********** get single data  **********




// require_once ('FileMaker.php');
// $fm = new FileMaker('TASpoint_C', 'http://119.9.31.62','Allen','apss1943');


// $record = $fm->getRecordById('ADM_Leave', 0 );
// echo "<br><br>";
// // print_r ($record);
// echo "<br><br>";


// $fields = $record->_impl->_fields;
// print_r($fields);
// echo "<br><br>";


// $_User_ID_or_Group_ID = $fields['_User_ID_or_Group_ID'][0];
// $Reason = $fields['Reason'][0];
// $Type = $fields['Type'][0];
// $DateFrom = $fields['DateFrom'][0];
// $DateTo = $fields['DateTo'][0];
// $HoursTaken = $fields['DateTo'][0];
// $Comments = $fields['DateTo'][0];



// $data = array("dateFrom" => $DateFrom, "dateTo" => $DateTo , "type" => $Type , "userID" => $_User_ID_or_Group_ID);
// $data_string = json_encode($data);
// $ch = curl_init('http://localhost:8080/WebServiceToOfficeCalandar/app/ctofservice');
// curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
// curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);
// curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
// curl_setopt($ch, CURLOPT_HTTPHEADER, array(
//     'Content-Type: application/json',
//     'Content-Length: ' . strlen($data_string))
// );
// $result = curl_exec($ch);


?>
