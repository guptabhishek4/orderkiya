<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['order_data']) && isset($_POST['order_to']) && isset($_POST['order_from'])) {
 
    $order_data = $_POST['order_data'];
    $order_to = $_POST['order_to'];
    $order_from = $_POST['order_from'];
    
    //$order_array = array();
    $order_array = (array) json_decode($order_data);
   // echo "<br/>O/P 1:".print_r($order_array);die;
   if (count($order_array) < 1)
   {
	    $response["success"] = 0;
        $response["message"] = "Order data not found";
        echo $response;
        return;
   }
   else
   {
   	
   	require_once __DIR__ . '/db_connect.php';
   
    // connecting to db
    $db = new DB_CONNECT();
    // mysql inserting a new row
    $con = $db->connect();
   	
	foreach ($order_array as $order_item) {
		  
		    $item_name =   $order_item->item_name;
		    $qty =   $order_item->quantity;
		    $unit =   $order_item->unit;
		    $order_sts = "New";
		     $result = mysqli_query($con, "INSERT INTO place_order(item_name, quantity, unit, order_to,order_from,order_status,order_date)
     VALUES('$item_name', '$qty', '$unit', '$order_to', '$order_from','$order_sts',now())");
		    
		   }
		 
		  $response["success"] = 1;
          $response["message"] = "Successfully Ordered.";
 
        // echoing JSON response
        echo json_encode($response);
        
        mysqli_close($con);
		   
   }

    
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>