<!-- use post to get data -->

<?php

$data = json_decode(file_get_contents('php://input'), true);

print_r($data);
echo $data["name"];

echo $id = $data["id"];
echo $subject = $data["subject"];
echo $startTime = $data["startTime"];
echo $endTime = $data["endTime"];

require_once ('FileMaker.php');
$fm = new FileMaker('TASpoint_C', 'http://119.9.31.62','Allen','apss1943');

$fm->getLayout('TestAllen');
$temp = array(
  "calId" => $id,
  "subject" => $subject,
  "startTime" => $startTime,
  "endTime" => $endTime,
);
$record = $fm->createRecord('TestAllen',$temp);


$result = $record->commit();
if (FileMaker::isError($result)) {
    echo "Error: " . $result->getMessage() . "\n";
    exit;
}
?>
