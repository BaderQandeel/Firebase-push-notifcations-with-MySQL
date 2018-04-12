<?php

class DbOperation
{
    //Database connection link
    private $con;
private $response = array();
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';

        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();

        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
    public function getGenderUser($gender){
    
					
			
					
					$stmt = $this->con->prepare("SELECT id, username, email, gender FROM users WHERE gender = ?");
					$stmt->bind_param("s",$gender);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					$i= $stmt->num_rows;
					
					while($i > 0){
						
						$stmt->bind_result($id, $username, $email, $gender);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'username'=>$username, 
							'email'=>$email,
							'gender'=>$gender
						);
						$i--;
					
						$response[] = $user; 
					}
				 return $response;
    }
    public function getRegisteredDevices(){
        $devices = $this->getAllDevices();

	$response['error'] = false; 
	$response['devices'] = array(); 

	while($device = $devices->fetch_assoc()){
		$temp = array();
		$temp['id']=$device['id'];
		$temp['email']=$device['email'];
		$temp['token']=$device['token'];
		array_push($response['devices'],$temp);
         return $response;
	}
    }
     public function getAllUser(){
         	$stmt = $this->con->prepare("SELECT id, username, email, gender FROM users   ");
					
					
					$stmt->execute();
					
					$stmt->store_result();
					
					$i= $stmt->num_rows;
					
					while($i > 0){
						
						$stmt->bind_result($id, $username, $email, $gender);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'username'=>$username, 
							'email'=>$email,
							'gender'=>$gender
						);
						$i--;
					
						$response[] = $user; 
					}
         return $response;
     }
    public function login($username,$password){
    	$stmt =$this->con->prepare("SELECT id, username, email, gender FROM users WHERE username = ? AND password = ?");
					$stmt->bind_param("ss",$username, $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
						$stmt->bind_result($id, $username, $email, $gender);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'username'=>$username, 
							'email'=>$email,
							'gender'=>$gender
						);
						
						$response['error'] = false; 
						$response['message'] = 'Login successfull'; 
						$response['user'] = $user; 
					}else{
						$response['error'] = false; 
						$response['message'] = 'Invalid username or password';
					}
        return 	$response;	
    }
    
  //storing token in database 
    public function signup($username,$email,$password,$gender){
      
				
	$stmt = $this->con->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
	$stmt->bind_param("ss", $username, $email);
	$stmt->execute();
	$stmt->store_result();
					
	if($stmt->num_rows > 0){
		$response['error'] = true;
		$response['message'] = 'User already registered';
		$stmt->close();
		}else{
     	$stmt =$this->con->prepare("INSERT INTO users (username, email, password, gender) VALUES (?, ?, ?, ?)");
     $stmt->bind_param("ssss", $username, $email, $password, $gender);
if($stmt->execute()){
	$stmt = $this->con->prepare("SELECT id, id, username, email, gender FROM users WHERE username = ?"); 
							$stmt->bind_param("s",$username);
							$stmt->execute();
							$stmt->bind_result($userid, $id, $username, $email, $gender);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'username'=>$username, 
								'email'=>$email,
								'gender'=>$gender
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = 'User registered successfully'; 
							$response['user'] = $user; 
						}
					}
		return 	$response;		
			
				
    }
    //storing token in database 
    public function registerDevice($email,$token){
        if(!$this->isEmailExist($email)){
            $stmt = $this->con->prepare("INSERT INTO devices (email, token) VALUES (?,?) ");
            $stmt->bind_param("ss",$email,$token);
            if($stmt->execute())
                return 0; //return 0 means success
            return 1; //return 1 means failure
        }else{
            return 2; //returning 2 means email already exist
        }
    }
      public function deleteDevice($email){
        if(!$this->isEmailExist($email)){
            $stmt = $this->con->prepare("DELETE FROM devices where email= ? ");
            $stmt->bind_param("s",$email);
            if($stmt->execute())
                return 0; //return 0 means success
            return 1; //return 1 means failure
        }else{
            return 2; //returning 2 means email already exist
        }
    }
 public function updDevice($email,$token){
       
            $stmt = $this->con->prepare("UPDATE devices set token = ? where email =? ");
            $stmt->bind_param("ss",$token,$email);
            $stmt->execute();
        
    }
    //the method will check if email already exist 
    private function isEmailexist($email){
        $stmt = $this->con->prepare("SELECT id FROM devices WHERE email = ?");
        $stmt->bind_param("s",$email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    //getting all tokens to send push to all devices
    public function getAllTokens(){
        $stmt = $this->con->prepare("SELECT token FROM devices");
        $stmt->execute(); 
        $result = $stmt->get_result();
        $tokens = array(); 
        while($token = $result->fetch_assoc()){
            array_push($tokens, $token['token']);
        }
        return $tokens; 
    }

    //getting a specified token to send push to selected device
    public function getTokenByEmail($email){
        $stmt = $this->con->prepare("SELECT token FROM devices WHERE email = ?");
        $stmt->bind_param("s",$email);
        $stmt->execute(); 
        $result = $stmt->get_result()->fetch_assoc();
        return array($result['token']);        
    }

    //getting all the registered devices from database 
    public function getAllDevices(){
        $stmt = $this->con->prepare("SELECT * FROM devices");
        $stmt->execute();
        $result = $stmt->get_result();
        return $result; 
    }
}