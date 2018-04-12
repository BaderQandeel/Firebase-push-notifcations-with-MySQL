<?php 

class Push {
    //notification title
    private $title;

    //notification message 
    private $message;

    //notification image url 
    private $image;
	
    //notification image url 
    private $id1;
	
    //notification image url 
    private $id2;
	
    //notification image url 
    private $id3;

    //initializing values in this constructor
    function __construct($title, $message, $image,$id1,$id2,$id3) {
         $this->title = $title;
         $this->message = $message; 
         $this->image = $image; 
		 $this->id1 = $id1; 
		 	 $this->id2 = $id2; 
			 	 $this->id3 = $id3; 
    }
    
    //getting the push notification
    public function getPush() {
        $res = array();
        $res['data']['title'] = $this->title;
        $res['data']['message'] = $this->message;
        $res['data']['image'] = $this->image;
		 $res['data']['id1'] = $this->id1;
		  $res['data']['id2'] = $this->id2;
		   $res['data']['id3'] = $this->id3;
        return $res;
    }
 
}