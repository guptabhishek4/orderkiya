<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['order_ids']) ) {
 
    $order_ids = $_POST['order_ids'];
    
    
    //$order_array = array();
    $order_array = (array) json_decode($order_ids);
   // echo "<br/>O/P 1:".print_r($order_array);die;
   if (count($order_ids) < 1)
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
    
   	
	foreach ($order_array as $value) {
		  
		//print_r($value);die;
//		    $item_name =   $order_item->item_name;
//		    $qty =   $order_item->quantity;
//		    $unit =   $order_item->unit;
//		    $order_sts = "New";
            //$order_id;
           
		
		     $result = mysqli_query($con, "UPDATE place_order SET order_status = 'Delivered' WHERE order_id = " .$value);
		    
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