package server;

import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.NotAuthorizedException;

import java.util.Scanner;

public class Repl {

    public State state = State.SIGNEDOUT;
    public clientRequest clientReq;

    public Repl(String serverUrl) {
        this.clientReq = new clientRequest(serverUrl);
    }

    public void run(){
        System.out.println("Welcome to the Chess Game Interface");
        System.out.println(clientReq.help());
        System.out.print(">>> ");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            //printPrompt();
            String line = scanner.nextLine();

            try {
                result = clientReq.evaluate(line);
                System.out.print(result);
            } catch (AlreadyTakenException | BadRequestException | NotAuthorizedException e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

}
