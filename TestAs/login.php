<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array(); // creating an array with no elements
 
// check for required fields
if (isset($_POST['mobile']) && isset($_POST['password']) ) {
 
$mobile = $_POST['mobile'];
$password = $_POST['password'];
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
   
// connecting to db
$db = new DB_CONNECT();
    
// mysql inserting a new row
$con = $db->connect();
$stmt = $con->prepare("SELECT name, mobile, user_type FROM user WHERE mobile = ? AND password = ?");
$stmt->bind_param("ss",$mobile, $password);
 
$stmt->execute();
 
 $stmt->store_result();
 
 if($stmt->num_rows > 0){
 
 $stmt->bind_result($name, $mobile, $user_type);
 $stmt->fetch();
 
 $user = array('name'=>$name, 'mobile'=>$mobile,'user_type'=>$user_type);
 
 $response['success'] = 1; 
 $response['message'] = 'Login successfull'; 
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
    $response["message"] = "All Credental is required";
 
    // echoing JSON response
    echo json_encode($response);
}
?>