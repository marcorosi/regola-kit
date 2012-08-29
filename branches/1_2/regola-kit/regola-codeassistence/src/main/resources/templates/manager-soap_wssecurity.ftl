package ${service_impl_package};

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class ${service_interface_name}PasswordCallBack implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		if (pc.getIdentifer().equals("chiara")) {
			if (!pc.getPassword().equals("alaia")) {
				throw new SecurityException("wrong password");
			}
		}

	}

}
