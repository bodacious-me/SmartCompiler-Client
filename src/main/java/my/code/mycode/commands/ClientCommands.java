package my.code.mycode.commands;

import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import my.code.mycode.services.ClientServices;

@ShellComponent
public class ClientCommands {
    private final ClientServices clientServices;

    @Autowired
    public ClientCommands(ClientServices clientServices){
        this.clientServices = clientServices;
    }    

    @ShellMethod(key = "new", value = "starts the application")
    public String sendPost() throws JsonMappingException, JsonProcessingException{
        System.out.println("Please Enter The Github Repo: ");
        String gitrepo = new java.util.Scanner(System.in).nextLine();

        System.out.println("Please Enter The Project Name: ");
        String name = new java.util.Scanner(System.in).nextLine();
    if(clientServices.checkRepo(gitrepo)){
        if(clientServices.checkName(name)){
            String result = clientServices.postRequest(gitrepo, name);
            return result;
        }
        else{
            return "The project name provided already exists, try another one please!!";
        }
    }else{
        return "The repository provided is not valid or doesn't contain java code!!";
    }
    
    }
}
