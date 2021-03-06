package com.kronos.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.kronos.model.User;
import com.kronos.pushNotification.FcmClient;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
@RequestMapping(value = "/push")
public class RegistryController {


  @Autowired
  private FcmClient fcmClient;

 
  @GetMapping("/register/{token}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> register(@PathVariable("token")String token,
		  @SessionAttribute("username") String username) {
	  Mono<String> mono= Mono.just(token);
	  System.out.println(mono);
    return mono.doOnNext(t -> this.fcmClient.subscribe(username, t)).then();
  }
  
  @GetMapping("/sendPush")
  public void sendPush() {
	 
	  Map<String,String> data= new HashMap<>();
	  data.put("id","1");
	  data.put("joke","Hola Mundo");
	  try {
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		  String body="Se ha agregado un nuevo Acuerdo\n"+
				  "Agregado por: "+"Concejo Municipal"+"\n"+
				  "En la fecha:" + LocalDateTime.now().format(formatter)+  "\n";
				  
				
	  this.fcmClient.send("SecretariadeAlcaldia", "Acuerdo Agregado", body);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  System.out.println(e.getMessage());
	  }
	  
  }

}