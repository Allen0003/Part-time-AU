<?php
// get data from HTTP POST and then insert data to QuickBase's Calander

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
  $data = json_decode(file_get_contents("php://input"));
  print_r($data);
}

$dateFrom = $data->dateFrom;
$dateTo = $data->dateTo;
$type = $data->type;
$userID = $data->userID;


$dateFrom = date("d-m-Y", strtotime($dateFrom));
$dateTo = date("d-m-Y", strtotime($dateTo));

// receive json type



$qbUser     = 'Allen.Wu@tripleasuper.com.au';
$qbPassword = 'apss1943';
$qbAppToken = 'd25qirddiecbacezx8i4gbjsgx';
$db_id      = 'bmi7f26ku';
 /*---------------------------------------------------------------------
 // User Configurable Options
 -----------------------------------------------------------------------*/

$xml_packet = new SimpleXMLElement('<qdbapi></qdbapi>');
$xml_packet->addChild('username',$qbUser);
$xml_packet->addChild('password',$qbPassword);
$xml_packet = $xml_packet->asXML();
$url = "https://brianpedretti.quickbase.com/db/main";

$headers = array(
	"Content-Type: text/xml;",
	'QUICKBASE-ACTION: API_Authenticate'
);
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_packet);

$qb = curl_exec($ch);

$response = new SimpleXMLElement($qb);

$xml_packet = new SimpleXMLElement('<qdbapi></qdbapi>');
$xml_packet->addChild('ticket', $response->ticket);
$xml_packet->addChild('apptoken',$qbAppToken);
$Start_Date = $xml_packet->addChild('field', $dateFrom);
$Start_Date->addAttribute('name', 'Start_Date');

$Return_Date = $xml_packet->addChild('field', $dateTo);
$Return_Date->addAttribute('name', 'Return_Date');

$Start_Date = $xml_packet->addChild('field', $type);
$Start_Date->addAttribute('name', 'Type_of_Time_Off');

$name = $xml_packet->addChild('field', $userID);
$name->addAttribute('name', 'Name');


$xml_packet = $xml_packet->asXML();
print ($xml_packet);
$url = "https://brianpedretti.quickbase.com/db/".$db_id;

$headers = array(
	"Content-Type: text/xml;",
	'QUICKBASE-ACTION: API_AddRecord'
);
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_packet);

$qb = curl_exec($ch);

$response = new SimpleXMLElement($qb);

?>
