package crm;

import java.util.Collection;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import com.itheima.crm.domain.Customer;

public class test {

	public static void main(String[] args) {
		
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:8180/crm/webservice/cs/customer")
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		for (Customer customer : collection) {
			System.out.println(customer);
		}
		
		
	}

}
