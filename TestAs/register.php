<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['mobile']) && isset($_POST['password']) 
    && isset($_POST['address']) && isset($_POST['user_type'])) {
 
    $name = $_POST['name'];
    $mobile = $_POST['mobile'];
    $password = $_POST['password'];
    $address = $_POST['address'];
    $user_type = $_POST['user_type'];
 
    // include db connect class
   
   require_once __DIR__ . '/db_connect.php';
   
    // connecting to db
    $db = new DB_CONNECT();
    // mysql inserting a new row
    $con = $db->connect();

    $qry = "SELECT * FROM user WHERE mobile = " . $mobile ;

 
   $result = $con->query($qry);
 
   if($result->num_rows > 0){
	 	// user Already registered
        $response["success"] = 0;
        $response["message"] = "User Already Registered.";
        echo json_encode($response);
        //return;
	 }
 
    else
    {
	    $result = mysqli_query($con, "INSERT INTO user(name, mobile, password, address,user_type)
	     VALUES('$name', '$mobile', '$password', '$address', '$user_type')");
	 
	//    $response["success"] = "fdsfsdf";
	//    echo json_encode($response);
	//    return;
	    // check if row inserted or not
	    if ($result) {
	        // successfully inserted into database
	        $response["success"] = 1;
	        $response["message"] = "User successfully Registered.";
	 
	        // echoing JSON response
	        echo json_encode($response);
	    } else {
	        // failed to insert row
	        $response["success"] = 0;
	        $response["message"] = "Oops! An error occurred.";
	 
	        // echoing JSON response
	        echo json_encode($response);
	    }
	    
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