<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array(); // creating an array with no elements
 
// check for required fields
if (isset($_POST['order_to']) && isset($_POST['order_from']) ) {
 
$order_to = $_POST['order_to'];
$order_from = $_POST['order_from'];
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
   
// connecting to db
$db = new DB_CONNECT();
    
// mysql inserting a new row //here arrow is used instead of dot operator
 $con = $db->connect();
 $qry = "SELECT * FROM place_order WHERE order_to = " . $order_to . " AND order_from = " . $order_from;

 
 $result = $con->query($qry);
 
 if($result->num_rows > 0)
 {
 $orders = array();
 while($row = $result->fetch_assoc())
 {
 	$orders[] = $row;
 }
 
 
 //('name'=>$name, 'mobile'=>$mobile,'address'=>$address,'user_type'=>$user_type);
 
 $response['success'] = 1; 
 $response['message'] = 'Data fetched'; 
 $response['orders'] = $orders;
 
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