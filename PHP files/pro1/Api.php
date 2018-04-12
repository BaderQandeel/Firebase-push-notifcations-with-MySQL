<?php 

	
	require_once 'DbOperation.php';
 $db = new DbOperation();  
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'signup':
              
				if(isTheseParametersAvailable(array('username','email','password','gender'))){
					$username = $_POST['username']; 
					$email = $_POST['email']; 
					$password = md5($_POST['password']);
					$gender = $_POST['gender']; 
                   $response=$db->signup($username,$email,$password,$gender); 
                }
				
			break; 
			
			case 'login':
				
				if(isTheseParametersAvailable(array('username', 'password'))){
					
					$username = $_POST['username'];
					$password = md5($_POST['password']); 
					
				$response=$db->login($username,$password); 
                    
				}
			break; 
			// extra 
			case 'getAllUser':
				
				$response=$db->getAllUser(); 
				
			break;
			case 'getGenderUser':
				$gender = $_POST['gender'];
				$response=$db->getGenderUser($gender);   
			break;
                case 'updDevice':
	
					$email = $_POST['email']; 
				$token = $_POST['token'];
			
				$db->updDevice($email,$token);   
			break;
			case 'RegisterDevice':
			$token = $_POST['token'];
		    $email = $_POST['email'];
        $result = $db->registerDevice($email,$token);
        if($result == 0){
			$response['error'] = false; 
			$response['message'] = 'Device registered successfully';
		}elseif($result == 2){
			$response['error'] = true; 
			$response['message'] = 'Device already registered';
		}else{
			$response['error'] = true;
			$response['message']='Device not registered';
		}
			break;
                	case 'DeleteDevice':
			
		    $email = $_POST['email'];
        $result = $db->deleteDevice($email);
        if($result == 0){
			$response['error'] = false; 
			$response['message'] = 'Device registered successfully';
		}elseif($result == 2){
			$response['error'] = true; 
			$response['message'] = 'Device already registered';
		}else{
			$response['error'] = true;
			$response['message']='Device not registered';
		}
			break;
			default: 
				$response['error'] = true; 
				$response['message'] = 'Invalid Operation Called';
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	echo json_encode($response);
	
	function isTheseParametersAvailable($params){
		
		foreach($params as $param){
			if(!isset($_POST[$param])){
				return false; 
			}
		}
		return true; 
	}