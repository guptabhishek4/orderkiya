<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array(); // creating an array with no elements
 
// check for required fields
if (isset($_POST['user_type']) ) {
 
$user_type = $_POST['user_type'];
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
   
// connecting to db
$db = new DB_CONNECT();
    
// mysql inserting a new row //here arrow is used instead of dot operator
 $con = $db->connect();
 $qry = "SELECT name, mobile, address, user_type FROM user WHERE user_type = " . $user_type ;

 
 $result = $con->query($qry);
 
 if($result->num_rows > 0){
 $user = array();
 while($row = $result->fetch_assoc())
 {
 	$user[] = $row;
 }
 
 
 //('name'=>$name, 'mobile'=>$mobile,'address'=>$address,'user_type'=>$user_type);
 
 $response['success'] = 1; 
 $response['message'] = 'Data fetched'; 
 $response['user'] = $user;
 
    echo json_encode($response);
    mysqli_close($con);
    } 
else 
 {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Invalid Credential";
 
    // echoing JSON response
    echo json_encode($response);
}
}

else
{
	$response["success"] = 0;
    $response["message"] = "Credental is required";
 
    // echoing JSON response
    echo json_encode($response);
}
?>