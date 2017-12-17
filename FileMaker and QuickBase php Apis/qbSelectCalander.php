<?php

// application url => https://brianpedretti.quickbase.com/db/bmi7f26gr

// get data from QuickBase's Calander


$qbUser     = 'Allen.Wu@tripleasuper.com.au';
$qbPassword = 'apss1943';
$qbAppToken = 'd25qirddiecbacezx8i4gbjsgx';
 /*---------------------------------------------------------------------
 // User Configurable Options
 -----------------------------------------------------------------------*/
$db_id       = 'bmi7f26ku';
$qb_site     = "www.quickbase.com";
$qb_ssl      = "https://www.quickbase.com/db/";
 /*---------------------------*/
 /*---------------------------------------------------------------------
 //	Do Not Change
 -----------------------------------------------------------------------*/

$realm = 'brianpedretti';
$qb_site = $realm . '.quickbase.com';
$qb_ssl = 'https://' . $realm . '.quickbase.com/db/';
$xml = true;

$xml_packet = new SimpleXMLElement('<qdbapi></qdbapi>');
$xml_packet->addChild('username',$qbUser);
$xml_packet->addChild('password',$qbPassword);
$xml_packet = $xml_packet->asXML();

$input = $xml_packet;
$action_name = 'API_Authenticate';
$url = $qb_ssl."main";

$content_length = strlen($input);
$headers = array(
	"POST /db/".$db_id." HTTP/1.0",
	"Content-Type: text/xml;",
  "Accept: text/xml",
  "Cache-Control: no-cache",
  "Pragma: no-cache",
	"Content-Length: ".$content_length,
	'QUICKBASE-ACTION: '.$action_name
);


$ch = curl_init($url);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_POSTFIELDS, $input);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, FALSE);

$qb = curl_exec($ch);
$response = new SimpleXMLElement($qb);




// List Fields of Table Records

//changing here for different APIs
$url = 'https://brianpedretti.quickbase.com/db/'.$db_id.'?a=API_DoQuery&ticket='.$response->ticket.'&apptoken='.$qbAppToken.'&qid=1'; // Working fine, Live Query

// header('Content-Type: application/xml');

$payload = file_get_contents($url);

print ($payload);
//Write to a file

?>
