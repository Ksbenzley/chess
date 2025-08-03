package server;

import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.NotAuthorizedException;

import java.util.Scanner;

public class Repl {

    public State state = State.SIGNEDOUT;
    public ClientRequest clientReq;

    public Repl(String serverUrl) {
        this.clientReq = new ClientRequest(serverUrl);
    }

    public void run(){
        System.out.println("Welcome to the Chess Game Interface");
        System.out.println(clientReq.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(">>> ");
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
